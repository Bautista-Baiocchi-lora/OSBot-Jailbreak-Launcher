package org.osbot.jailbreak.utils;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.utils.reflection.ReflectedClass;
import org.osbot.jailbreak.utils.reflection.ReflectionEngine;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.LinkedList;

/**
 * Created by Ethan on 1/30/2018.
 */
public class BotAuth {
    private static LinkedList<String> listString = null;
    private static LinkedList<X509Certificate> listCert = null;
    private static KeyStore keyStore = null;
    private static SSLContext ssl = null;
    private ReflectionEngine reflectionEngine;

    public BotAuth(ReflectionEngine reflectionEngine) {
        this.reflectionEngine = reflectionEngine;
        ReflectedClass clazz = reflectionEngine.getClass("org.osbot.sB");
        Object uiInst = clazz.getNewInstance();
        reflectionEngine.getMethodValue("org.osbot.sB", "iIiiiiiiiIII", 0, "void", uiInst);
        reflectionEngine.setFieldValue("org.osbot.Boot", "iIIiiiiiIIIi", uiInst);
        setKeyStore();
        setListCert();
        setListString();
        setSSL();
        if (clazz != null) {
            int i = getBotReturn("NewBoi2", "123123123");
            if (i == 0) {
                String jarPath = Constants.DIRECTORY_PATH + File.separator + "environment.jar";
                String xBootPath = System.getProperty("user.home") + File.separator + "OSBot" + File.separator + "Data" + File.separator + "filter_d85cc9ad.jar";
                ProcessBuilder osbotBuilder = new ProcessBuilder("java", "-Xbootclasspath/p:" + xBootPath, "-cp", jarPath, "org.osbot.BotApplication", "0,1,0,null,null,-1,0,0,0,0,0,null");
                try {
                    osbotBuilder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public int getBotReturn(String user, String pass) {
        try {
            final File[] listFiles;
            if ((listFiles = new File(filePath()).listFiles()) != null) {
                final File[] array;
                final int length = (array = listFiles).length;
                int n;
                int i = n = 0;
                while (i < length) {
                    final File file;
                    if ((file = array[n]).getName().contains(".tmp")) {
                        //  file.delete();
                        System.out.println(file.getName());
                    }
                    i = ++n;
                }
            }
            final NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] array2 = null;
            if (byInetAddress != null) {
                array2 = byInetAddress.getHardwareAddress();
            }
            if (array2 == null) {
                array2 = "".getBytes();
            }
            final StringBuilder sb = new StringBuilder();
            int n2;
            int j = n2 = 0;
            while (j < array2.length) {
                final StringBuilder sb2 = sb;
                final String s = "%02X%s";
                final Object[] array3 = {array2[n2], (n2 < array2.length - 1) ? "-" : ""};
                ++n2;
                sb2.append(String.format(s, array3));
                j = n2;
            }
            final String property;
            String s2;
            if ((property = System.getProperty("os.name")).contains("Mac")) {
                s2 = "Mac";
            } else if (property.contains("Windows")) {
                s2 = "Windows";
            } else if (property.contains("Linux")) {
                s2 = "Linux";
            } else {
                s2 = "Unknown";
            }

            pass = new StringBuilder().insert(0, "name=").append(URLEncoder.encode(user, "UTF-8")).append("&password=").append(URLEncoder.encode(pass, "UTF-8")).append("&identifier=").append(sb.toString()).append("&v1=").append(2).append("&v2=").append(4).append("&v3=").append(160).append("&os=").append(s2).append("&add=").append(lpt2String()).toString();
            final HttpsURLConnection iiIiiiiiIIII = urlCon("https://osbot.org:443/mvc/botauth/create");
            final String s3 = "Content-Length";
            final HttpsURLConnection httpsURLConnection = iiIiiiiiIIII;
            final String s4 = "charset";
            final String s5 = "Content-Type";
            final HttpsURLConnection httpsURLConnection2 = iiIiiiiiIIII;
            final boolean instanceFollowRedirects = false;
            final HttpsURLConnection httpsURLConnection3 = iiIiiiiiIIII;
            final HttpsURLConnection httpsURLConnection4 = iiIiiiiiIIII;
            final boolean b = true;
            httpsURLConnection4.setDoOutput(b);
            httpsURLConnection3.setDoInput(b);
            httpsURLConnection3.setInstanceFollowRedirects(instanceFollowRedirects);
            httpsURLConnection2.setRequestMethod("POST");
            httpsURLConnection2.setRequestProperty(s5, "application/x-www-form-urlencoded");
            httpsURLConnection.setRequestProperty(s4, "utf-8");
            httpsURLConnection.setRequestProperty(s3, new StringBuilder().insert(0, "").append(Integer.toString(pass.getBytes().length)).toString());
            final boolean useCaches = false;
            final HttpsURLConnection httpsURLConnection5 = iiIiiiiiIIII;
            httpsURLConnection5.setRequestProperty("User-Agent", "OSBot Comms");
            httpsURLConnection5.setUseCaches(useCaches);
            final DataOutputStream dataOutputStream = new DataOutputStream(iiIiiiiiIIII.getOutputStream());
            dataOutputStream.writeBytes(pass);
            dataOutputStream.flush();
            dataOutputStream.close();
            int code = httpsURLConnection5.getResponseCode();
            if (code == 200) {
                System.out.println("we got 200");
                final DataInputStream dataInputStream2;
                final DataInputStream dataInputStream = dataInputStream2 = new DataInputStream(iiIiiiiiIIII.getInputStream());
                final String iiIiIiiiiiII = getLBString((InputStream) dataInputStream);
                final boolean b2 = dataInputStream.readUnsignedByte() == 0;
                final String iiIiIiiiiiII2 = getLBString((InputStream) dataInputStream2);
                final int[] array4 = new int[4];
                int n3;
                int k = n3 = 0;
                while (k < 4) {
                    final int[] array5 = array4;
                    final int n4 = n3;
                    final int int1 = dataInputStream2.readInt();
                    ++n3;
                    array5[n4] = int1;
                    k = n3;
                }
                iiIiiiiiIIII.disconnect();
                printTemp(new StringBuilder().insert(0, new BigInteger(32, new SecureRandom()).toString(32)).append(",").append(iiIiIiiiiiII).append(",").append(b2).append(",").append(array4[0]).append(",").append(array4[1]).append(",").append(array4[2]).append(",").append(array4[3]).append(",").append(user).append(",password,").append(iiIiIiiiiiII2).toString());
                printTemp2(new StringBuilder().insert(0, new BigInteger(32, new SecureRandom()).toString(32)).append(",").append(iiIiIiiiiiII).append(",").append(b2).append(",").append(array4[0]).append(",").append(array4[1]).append(",").append(array4[2]).append(",").append(array4[3]).append(",").append(user).append(",password,").append(iiIiIiiiiiII2).toString());

                return 0;
            } else {
                System.out.println("We got something other than 200: " + code);
                switch (code) {
                    case 400:
                    case 401:
                    case 403:
                    case 500:
                    case 503:

                    default: {
                        return 2;
                    }
                }
            }
        } catch (Exception ex) {
            if (ex instanceof IllegalAccessException) {
                return 4;
            }
            ex.printStackTrace();
            return 3;
        }
    }


    private void printTemp(final String str) {
        try {
            final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(new StringBuilder().insert(0, filePath()).append(str.split(",")[0]).append(".tmp").toString(), true)));
            printWriter.println(Base64.getEncoder().encodeToString(getTString(str)));
            printWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printTemp2(final String str) {
        try {
            final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(new StringBuilder().insert(0, filePath()).append(str.split(",")[0]).append(".nah").toString(), true)));
            printWriter.println(Base64.getEncoder().encodeToString(getTString(str)));
            printWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String filePath() {
        return (String) reflectionEngine.getFieldValue("org.osbot.Constants", "IiiiiiiIiiII");
    }

    public String getLBString(Object obj) {
        String s = (String) reflectionEngine.getMethodValue("org.osbot.LB", "iiIiIiiiiiII", 1, "public final class java.lang.String", null, obj);
        System.out.println("LB: " + s);
        return s;
    }

    public byte[] getTString(Object obj) {
        byte[] s = (byte[]) reflectionEngine.getMethodValue("org.osbot.t", "IiIiiiiiIIII", 1, "public abstract final class [B", null, obj);
        return s;
    }

    public String lpt2String() {
        String s = (String) reflectionEngine.getMethodValue("org.osbot.lpt2", "iiIiIiiiiiII", 0, "public final class java.lang.String", null);
        System.out.println(s);
        return s;
    }

    public void setSSL() {
        ssl = (SSLContext) reflectionEngine.getFieldValue("org.osbot.LPT8", "iiiiiiiiIiiI");
    }

    public void setKeyStore() {
        keyStore = (KeyStore) reflectionEngine.getFieldValue("org.osbot.LPT8", "IiIiIiiIIIiI");
    }

    public void setListCert() {
        listCert = (LinkedList<X509Certificate>) reflectionEngine.getFieldValue("org.osbot.LPT8", "iiIIiiiiIIIi");
    }

    public void setListString() {
        listString = (LinkedList<String>) reflectionEngine.getFieldValue("org.osbot.LPT8", "IiiiiiiIiiII");
    }

    public static HttpsURLConnection urlCon(final String link) throws Exception {
        final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) new URL(link).openConnection();
        httpsURLConnection.setSSLSocketFactory(ssl.getSocketFactory());
        return httpsURLConnection;
    }

}
