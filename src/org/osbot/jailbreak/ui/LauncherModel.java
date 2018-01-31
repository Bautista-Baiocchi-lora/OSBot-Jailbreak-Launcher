package org.osbot.jailbreak.ui;


import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.json.simple.JSONObject;
import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.utils.Account;
import org.osbot.jailbreak.utils.NetUtils;
import org.osbot.jailbreak.utils.OSBotDownload;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LauncherModel {
	private ClassLoader classLoader;
	private final LauncherController controller;
	private int id;
	private String jailbreakUrl, hwid;

	public LauncherModel(final LauncherController controller) {
		this.controller = controller;
	}

	public int getID() {
		return id;
	}


	public boolean isVIP() {
		final String VERIFY_VIP_URL = "http://botupgrade.us/private/check/paid.php?";
		StringBuilder parameters = new StringBuilder();
		parameters.append("uid=").append(id).append("&submit=Search");
		String response = null;
		try {
			response = NetUtils.postResponse(VERIFY_VIP_URL, parameters.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response != null) {
			if (response.trim().equals("1")) {
				return true;
			}
		}
		return false;
	}

	public boolean verifyHWID() {
		StringBuilder parameters = new StringBuilder();
		final String VERIFY_ACCESS_URL = "http://botupgrade.us/hwid/check/check.php?";
		try {
			parameters.append("search=").append(hwid = getHWID()).append("&submit=Search");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String response = null;
		try {
			response = NetUtils.postResponse(VERIFY_ACCESS_URL, parameters.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response != null) {
			if (response.contains("true")) {
				jailbreakUrl = response.toString().trim().split(" ")[1];
				return true;
			}
		}
		return false;
	}

	private final Cipher getEncryptionCipher(int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
		final Cipher cipher = Cipher.getInstance(Constants.SYMMETRIC_TRANSFORMATION);
		cipher.init(mode, new SecretKeySpec(getHWID().substring(0, 16).getBytes("UTF-8"), Constants.SYMMETRIC_ALGORITHM));
		return cipher;
	}

	public void saveAccount(Account account) {
		try {
			final Cipher encryptionCipher = getEncryptionCipher(Cipher.ENCRYPT_MODE);
			final SealedObject encryptedAccount = new SealedObject(account.toJson(), encryptionCipher);
			final CipherOutputStream encryptionOutputStream = new CipherOutputStream(new FileOutputStream(Constants.DIRECTORY_PATH + File.separator + Constants.CONFIG_FILE), encryptionCipher);
			final ObjectOutputStream objectOutputStream = new ObjectOutputStream(encryptionOutputStream);
			objectOutputStream.writeObject(encryptedAccount);
			objectOutputStream.close();
		} catch (IOException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
		}
	}

	public Account getSavedAccount() {
		File accountFile = new File(Constants.DIRECTORY_PATH + File.separator + Constants.CONFIG_FILE);
		if (accountFile.exists()) {
			try {
				final Cipher encryptionCipher = getEncryptionCipher(Cipher.DECRYPT_MODE);
				final CipherInputStream encryptionInputStream = new CipherInputStream(new FileInputStream(accountFile), encryptionCipher);
				final ObjectInputStream objectInputStream = new ObjectInputStream(encryptionInputStream);
				final SealedObject encryptedAccount = (SealedObject) objectInputStream.readObject();
				objectInputStream.close();
				return Account.wrap((JSONObject) encryptedAccount.getObject(encryptionCipher));
			} catch (IOException | InvalidKeyException | ClassNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public HashMap<String, VirtualMachineDescriptor> getJVMs() {
		final HashMap<String, VirtualMachineDescriptor> jvms = new HashMap<>();
		for (VirtualMachineDescriptor jvm : VirtualMachine.list().stream().filter(new Predicate<VirtualMachineDescriptor>() {
			@Override
			public boolean test(final VirtualMachineDescriptor virtualMachineDescriptor) {
				return virtualMachineDescriptor.displayName().contains(Constants.APPLICATION_NAME);
			}
		}).collect(Collectors.toList())) {
			jvms.put("Client " + jvm.id(), jvm);
		}
		return jvms;
	}

	public void copyHWID() {
		try {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(getHWID()), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getHWID() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		if (hwid != null) {
			return hwid;
		}
		String s = "";
		final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
		final byte[] bytes = main.getBytes("UTF-8");
		final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		final byte[] md5 = messageDigest.digest(bytes);
		int i = 0;
		for (final byte b : md5) {
			s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
			if (i != md5.length - 1) {
				s += "-";
			}
			i++;
		}
		return s.trim();
	}

	public void downloadJailbreak() throws IOException {
		if (!Constants.LOAD_LOCAL_JAILBREAK) {
			NetUtils.downloadJailbreak(jailbreakUrl);
		}
	}

	public void validateEnvironment() {
		final File environmentZip = new File(Constants.DIRECTORY_PATH + File.separator + "environment.zip");
		HttpURLConnection connection = null;
		try {
			connection = NetUtils.getConnection("http://botupgrade.us/private/osbot155/environment.zip");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!environmentZip.exists()) {
			controller.showDownloadView(environmentZip, connection);
		} else {
			try {
				final URLConnection savedFileConnection = environmentZip.toURI().toURL().openConnection();
				if (connection.getContentLengthLong() != savedFileConnection.getContentLengthLong()) {
					controller.showDownloadView(environmentZip, connection);
				} else {
					controller.showSelectorView();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean appendedClasses = false;

	public void startOSBotClient(String username, String password) {
		if (!appendedClasses) {
			NetUtils.addClassLoader(ClassLoader.getSystemClassLoader(), "osbot.jar");
			appendedClasses = true;
		}
		if (getOSBotLoginResponse(username, password) == 0) {
			try {
				ProcessBuilder osbotBuilder = new ProcessBuilder("java", "-Xbootclasspath/p:" + Constants.DIRECTORY_PATH + File.separator + "filter.jar", "-cp", Constants.DIRECTORY_PATH + File.separator + "environment.jar", "org.osbot.BotApplication", "0,1,0,null,null,-1,0,0,0,0,0,null");
				osbotBuilder.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showConfirmDialog(null, "There was an error processing your login, please review your details and try again.", "Error!", JOptionPane.DEFAULT_OPTION);
		}


	}

	public int getOSBotLoginResponse(String user, String pass) {
		try {
			Class<?> loginClass = ClassLoader.getSystemClassLoader().loadClass("org.osbot." + loginClass());
			for (Method m : loginClass.getDeclaredMethods()) {
				if (m.getParameterCount() == 3) {
					m.setAccessible(true);
					int i = (int) m.invoke(null, user, pass, false);
					return i;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private String loginClass() throws Exception {
		return NetUtils.readUrl("http://www.botupgrade.us/private/main.txt");
	}

	public void downloadNewestOSBot() {
		new OSBotDownload();
	}

	public boolean login(String email, String password) {
		final String LOGIN_URL = "http://www.botupgrade.us/private/login.php?email=" + email + "&password=" + password;
		String response = null;
		try {
			response = NetUtils.getResponse(LOGIN_URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (response != null) {
			if (response.contains("SUCCESS")) {
				id = Integer.parseInt(response.split(" => ")[5].replace(")", ""));
				return true;
			}
		} else {
			System.out.println("No response from login server.");
		}
		return false;
	}

	public void openForums() {
		try {
			NetUtils.openWebpage(new URL("http://botupgrade.us/forums/").toURI());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	public void openRegisterPage() {
		try {
			NetUtils.openWebpage(new URL("http://botupgrade.us/forums/?_fromLogout=1").toURI());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}

	public void openRequestAccessPage() {
		try {
			NetUtils.openWebpage(new URL("http://botupgrade.us/forums/index.php?/topic/6-request-trial/").toURI());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}
}
