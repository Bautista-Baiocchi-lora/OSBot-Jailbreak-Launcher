package com.osbot.jailbrake.ui;

import com.osbot.jailbrake.data.Constants;
import com.osbot.jailbrake.utils.NetUtils;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class LauncherModel {

	private final LauncherController controller;
	private int id;
	private String jailbreakUrl;

	public LauncherModel(final LauncherController controller) {
		this.controller = controller;
	}

	public boolean isVIP() {
		StringBuilder parameters = new StringBuilder();
		parameters.append("uid=").append(id).append("&submit=Search");
		String response = null;
		try {
			response = NetUtils.getResponse(Constants.VERIFY_VIP_URL, parameters.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response != null) {
			if (!response.equalsIgnoreCase("nope")) {
				return true;
			}
		}
		return false;
	}

	public boolean verifyHwid() {
		StringBuilder parameters = new StringBuilder();
		try {
			parameters.append("search=").append(getHwid()).append("&submit=Search");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String response = null;
		try {
			response = NetUtils.getResponse(Constants.VERIFY_ACCESS_URL, parameters.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response != null) {
			if (response.contains("true")) {
				jailbreakUrl = response.toString().trim().split(" ")[1];
			}
		}
		return false;
	}

	private String getHwid() throws UnsupportedEncodingException, NoSuchAlgorithmException {
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
		return s;
	}

	public void downloadJailbreak(String url) {

	}

	public void startJailbreak() {
		String jvmPid = null;
		List<VirtualMachineDescriptor> jvms = VirtualMachine.list();
		for (VirtualMachineDescriptor jvm : jvms) {
			if (jvm.displayName().contains(Constants.APPLICATION_NAME)) {
				System.out.println("Status: OSBot client found!");
				jvmPid = jvm.id();
				break;
			}

		}
		if (jvmPid != null) {
			System.out.println("Status: Preparing jailbreak...");
			File agentFile = new File(Constants.DIRECTORY_PATH + File.separator + Constants.JAILBREAK_JAR);
			if (!agentFile.exists()) {
				NetUtils.downloadJailbreak(RuntimeVariables.jarUrl);
			}
			if (agentFile.isFile()) {
				String agentFileName = agentFile.getName();
				String agentFileExtension = agentFileName.substring(agentFileName.lastIndexOf(".") + 1);
				if (agentFileExtension.equalsIgnoreCase("jar")) {
					try {
						System.out.println("Status: Starting jailbreak...");
						VirtualMachine jvm = VirtualMachine.attach(jvmPid);
						jvm.loadAgent(agentFile.getAbsolutePath());
						jvm.detach();
						System.out.println("Status: Jailbreak started!");
					} catch (Exception exception) {
						throw new RuntimeException(exception);
					}
					System.exit(0);
				}
			}
		} else {
			System.out.println("Status: OSBot client not found!");
		}
	}

	public boolean login(String email, String password) {
		StringBuilder parameters = new StringBuilder();
		parameters.append("email=").append(email).append("&password=").append(password);
		String response = null;
		try {
			response = NetUtils.getResponse(Constants.LOGIN_URL, parameters.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response != null) {
			if (response.contains("SUCCESS")) {
				id = Integer.parseInt(response.split(" => ")[4].replace(" )", ""));
				return true;
			}
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
			NetUtils.openWebpage(new URL("http://botupgrade.us/forums/").toURI());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	}
}
