package org.osbot.jailbreak.utils;


import org.osbot.jailbreak.data.Constants;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;


/**
 * Created by Ethan on 1/31/2018.
 */
public class OSBotDownload {
    ClassLoader classLoader;

    public OSBotDownload() {
        File environment = new File(Constants.DIRECTORY_PATH + File.separator + "environment.jar");
        try {
            String loaderURL = "" + environment.toURI().toURL();
            classLoader = new URLClassLoader(new URL[]{new URL(loaderURL)});
        } catch (Exception e) {
            e.printStackTrace();
        }
        downloadNewestOSBot();
    }

    public boolean downloadNewestOSBot() {
        try {
            HttpsURLConnection urlConnection = getOSBotSSLConnection("https://osbot.org/mvc/get");
            urlConnection.setRequestProperty("User-Agent", "OSBot Comms");
            long contentLength = urlConnection.getContentLengthLong();
            Channel channel = Channels.newChannel(urlConnection.getInputStream());
            File fileLocation = new File(Constants.DIRECTORY_PATH + File.separator + "osbot.jar");
            FileOutputStream fileOutputStream = new FileOutputStream(fileLocation);
            long currentLength;
            long l2 = (long) Math.ceil((double) contentLength / 100.0);
            DecimalFormat format = new DecimalFormat("#.#");
            long l3 = 0;
            long l4 = 0;
            while ((currentLength = fileOutputStream.getChannel().transferFrom((ReadableByteChannel) channel, l3, l2)) > 0) {
                l3 += currentLength;
                String string = format.format((double) (l4 += currentLength) / (double) contentLength * 100.0);
                System.out.println(new StringBuilder().insert(0, "Status: OSBotDownload ").append(Integer.parseInt(string)).append("% complete...").toString());
            }
            if ((currentLength = fileOutputStream.getChannel().transferFrom((ReadableByteChannel) channel, l3, l2)) <= 0) {
                System.out.println("Downloaded");
                fileOutputStream.close();
                channel.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public HttpsURLConnection getOSBotSSLConnection(String link) {
        try {
            Class<?> c = classLoader.loadClass("org.osbot.LPT8");
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

}
