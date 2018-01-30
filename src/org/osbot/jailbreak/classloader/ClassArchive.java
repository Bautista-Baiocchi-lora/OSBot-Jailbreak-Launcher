package org.osbot.jailbreak.classloader;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import org.osbot.jailbreak.data.Constants;

import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Ethan on 7/11/2017.
 */
public class ClassArchive {
    public final ArrayList<String> classNames;
    public final HashMap<String, ClassNode> classes;
    public final Map<String, File> resources;

    public ClassArchive() {
        this.classNames = new ArrayList<>();
        this.classes = new HashMap<>();
        this.resources = new HashMap<>();
        addToSystemClassLoader();
    }

    public void loadResource(final String name, final InputStream in) throws IOException {
        String path;
        path = System.getProperty("user.home") + File.separator + "OSBot" + File.separator + "Data" + File.separator;
        File f1 = new File(path);

        final File f = File.createTempFile("bot", ".tmp", f1);
        f.deleteOnExit();
        try (OutputStream out = new FileOutputStream(f)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
        }
        resources.put(name, f);
    }

    protected void loadClass(InputStream in) throws IOException {
        ClassReader cr = new ClassReader(in);
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.EXPAND_FRAMES);
        classNames.add(cn.name.replace('/', '.'));
        classes.put(cn.name, cn);

    }

    public void addToSystemClassLoader() {
        try {
            String jarPath = Constants.DIRECTORY_PATH + File.separator + "environment.jar";

            File file = new File(jarPath);
            URL url = file.toURI().toURL();

            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addJar(final File file) {
        try {
            addJar(file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void addJar(final URL url) {
        try {
            addJar(url.openConnection());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addJar(final URLConnection connection) {
        try {
            final ZipInputStream zin = new ZipInputStream(connection.getInputStream());
            ZipEntry e;
            while ((e = zin.getNextEntry()) != null) {
                if (e.isDirectory())
                    continue;
                if (e.getName().endsWith(".class")) {
                    loadClass(zin);
                } else {
                    loadResource(e.getName(), zin);
                }

            }
            zin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}