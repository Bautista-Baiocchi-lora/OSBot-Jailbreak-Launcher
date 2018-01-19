package com.osbot.jailbrake.utils;

import com.osbot.jailbrake.data.Constants;
import com.osbot.jailbrake.data.RuntimeVariables;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class NetUtils {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/57.0";

	public static boolean isVerified(String HWID) throws IOException {
		URL obj = new URL(Constants.VERIFY_ACCESS_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		String urlParameters = "search=" + HWID + "&submit=Search";
		os.write(urlParameters.getBytes());
		os.flush();
		os.close();
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			if (response.toString().trim().contains("true")) {
				RuntimeVariables.jarUrl = response.toString().trim().split(" ")[1];
				return true;
			}
		} else {
			System.out.println("Error Verifying License!");
		}
		return false;
	}

	public static void downloadJailbreak(String url) {
		try {
			URL download = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(download.openStream());
			FileOutputStream fileOut = new FileOutputStream(Constants.DIRECTORY_PATH + File.separator + Constants.JAILBREAK_JAR);
			fileOut.getChannel().transferFrom(rbc, 0, 1 << 24);
			fileOut.close();
			rbc.close();
		} catch (Exception e) { e.printStackTrace(); }
	}

	public static boolean openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean openWebpage(URL url) {
		try {
			return openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}
}
