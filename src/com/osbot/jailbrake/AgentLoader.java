package com.osbot.jailbrake;

import com.osbot.jailbrake.data.Constants;
import com.osbot.jailbrake.ui.LoaderFrame;
import com.osbot.jailbrake.utils.NetUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class AgentLoader {

	public static void main(String[] args) {
		try {
			if (NetUtils.isVerified(getHWID())) {
				File file = new File(Constants.DIRECTORY_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				new LoaderFrame().setVisible(true);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}


	public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
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

}

