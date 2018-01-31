package org.osbot.jailbreak.utils;

import org.osbot.jailbreak.data.Constants;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NetUtils {

    public static HttpURLConnection getConnection(String url) throws IOException {
        final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; …) Gecko/20100101 Firefox/57.0";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", USER_AGENT);
        return connection;
    }

    public static void unZip(String zipFile) {

        byte[] buffer = new byte[1024];

        try {

            File folder = new File(Constants.DIRECTORY_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }

            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(folder + File.separator + fileName);
                System.out.println("file unzip : " + newFile.getAbsoluteFile());
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static String postResponse(String url, String parameter) throws IOException {
        HttpURLConnection connection = getConnection(url);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(parameter.getBytes());
        outputStream.close();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString().trim();
        }
        System.out.println("HTTP request error!");
        return null;
    }

    public static String getResponse(String url) throws Exception {
        HttpURLConnection con = getConnection(url);
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString().trim();
    }

    public static void downloadJailbreak(String url) {
        try {
            File jailbreak = new File(Constants.DIRECTORY_PATH + File.separator + Constants.JAILBREAK_JAR);
            URL download = new URL(url);
            URLConnection downloadConnection = download.openConnection();
            final URLConnection savedFileConnection = jailbreak.toURI().toURL().openConnection();
            if (savedFileConnection.getContentLength() == downloadConnection.getContentLength()) {
                return;
            }
            ReadableByteChannel rbc = Channels.newChannel(downloadConnection.getInputStream());
            FileOutputStream fileOut = new FileOutputStream(jailbreak);
            fileOut.getChannel().transferFrom(rbc, 0, 1 << 24);
            fileOut.close();
            rbc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public static void addToSystemClassLoader() {
        try {
            File file = new File(Constants.DIRECTORY_PATH + File.separator + "osbot.jar");
            URL url = file.toURI().toURL();

            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
