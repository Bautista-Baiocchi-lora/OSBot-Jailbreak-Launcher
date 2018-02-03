package org.osbot.jailbreak.ui;


import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.osbot.jailbreak.classloader.ClassArchive;
import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.utils.Account;
import org.osbot.jailbreak.utils.NetUtils;
import org.osbot.jailbreak.utils.reflection.ReflectedClass;
import org.osbot.jailbreak.utils.reflection.ReflectedMethod;
import org.osbot.jailbreak.utils.reflection.ReflectionEngine;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LauncherModel {
	private final LauncherController controller;
	private int id;
	private String jailbreakUrl, hwid;
	private boolean appendedClasses = false;
	private ReflectionEngine reflectionEngine;
	private ClassLoader downloadClassLoader;

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

	private Properties getProperties() {
		final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		try {
			encryptor.setPassword(getHWID());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new EncryptableProperties(encryptor);
	}

	public void saveAccount(Account account) {
		Properties properties = getProperties();
		properties.setProperty("Email", account.getEmail());
		properties.setProperty("Password", account.getPassword());
		try {
			properties.store(new FileOutputStream(Constants.DIRECTORY_PATH + File.separator + Constants.CONFIG_FILE), "update");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Account getSavedAccount() {
		File accountFile = new File(Constants.DIRECTORY_PATH + File.separator + Constants.CONFIG_FILE);
		if (accountFile.exists()) {
			Properties properties = getProperties();
			try {
				properties.load(new FileInputStream(accountFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new Account(properties.getProperty("Email"), properties.getProperty("Password"));
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
			controller.showEnvironmentDownload(environmentZip, connection);
		} else {
			try {
				final URLConnection savedFileConnection = environmentZip.toURI().toURL().openConnection();
				if (connection.getContentLengthLong() != savedFileConnection.getContentLengthLong()) {
					controller.showEnvironmentDownload(environmentZip, connection);
				} else {
					controller.environmentValidated();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void startOSBotClient(String username, String password) {
		if (!appendedClasses) {
			File file = new File(Constants.DIRECTORY_PATH + File.separator + "osbot.jar");
			ClassArchive classArchive = new ClassArchive();
			try {
				classArchive.addJar(new File(Constants.DIRECTORY_PATH + File.separator + "Network.jar").toURI().toURL());
				classArchive.addJar(file);
				reflectionEngine = new ReflectionEngine(classArchive);
				classArchive.dump(new File(Constants.DIRECTORY_PATH + File.separator + "Test.jar"));
			} catch (Exception e) {
				e.printStackTrace();
				appendedClasses = true;
			}
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
			ReflectedClass loginClass = reflectionEngine.getClass("org.osbot." + loginClass());
			for (ReflectedMethod m : loginClass.getMethods()) {
				if (m.getParameterCount() == 3) {
					int i = (int) m.invoke(user, pass, false);
					System.out.println(i);
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

	public void validateOSBotClient() {
		File environment = new File(Constants.DIRECTORY_PATH + File.separator + "environment.jar");
		try {
			String loaderURL = "" + environment.toURI().toURL();
			downloadClassLoader = new URLClassLoader(new URL[] {new URL(loaderURL)});
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpsURLConnection urlConnection = getOSBotSSLConnection("https://osbot.org/mvc/get");
		urlConnection.setRequestProperty("User-Agent", "OSBot Comms");
		try {
			long contentLength = urlConnection.getContentLengthLong();
			File fileLocation = new File(Constants.DIRECTORY_PATH + File.separator + "osbot.jar");
			final long savedContentLength = fileLocation.toURI().toURL().openConnection().getContentLengthLong();
			if (savedContentLength != contentLength) {
				controller.showOSBotJarDownload(fileLocation, urlConnection);
			} else {
				controller.osbotJarValidated();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private HttpsURLConnection getOSBotSSLConnection(String link) {
		try {
			Class<?> c = downloadClassLoader.loadClass("org.osbot.LPT8");
			if (c != null) {
				for (Method m : c.getDeclaredMethods()) {
					if (m.getName().equals("IiIiiiiiIIII")) {
						if (m.getParameterCount() == 1) {
							if (m.getReturnType().toGenericString().equals("public abstract class javax.net.ssl.HttpsURLConnection")) {
								m.setAccessible(true);
								return (HttpsURLConnection) m.invoke(null, link);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
