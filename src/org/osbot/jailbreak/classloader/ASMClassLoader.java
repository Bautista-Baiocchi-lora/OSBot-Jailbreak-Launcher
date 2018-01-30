package org.osbot.jailbreak.classloader;

/**
 * Created by Ethan on 7/11/2017.
 */


import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;


public class ASMClassLoader extends ClassLoader {

    public Map<String, Class<?>> classCache;
    public ClassArchive classArchive;

    public ASMClassLoader(final ClassArchive classArchive) {
        this.classCache = new HashMap<>();
        this.classArchive = classArchive;

    }


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return findClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String key = name.replace('.', '/');
        if (classCache.containsKey(key)) {
            return classCache.get(key);
        }
        ClassNode node = classArchive.classes.get(key);
        if (node != null) {
            Class<?> c = nodeToClass(node);
            classCache.put(key, c);

            return c;
        }
        return getSystemClassLoader().loadClass(name);
    }

    private final Class<?> nodeToClass(ClassNode node) {
        if (super.findLoadedClass(node.name) != null) {
            return findLoadedClass(node.name);
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(cw);
        byte[] b = cw.toByteArray();
        return defineClass(node.name.replace('/', '.'), b, 0, b.length,
                getDomain());
    }

    private final ProtectionDomain getDomain() {
        CodeSource code = null;
        try {
            code = new CodeSource(new URL("http://127.0.0.1"), (Certificate[]) null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new ProtectionDomain(code, getPermissions());
    }

    private final Permissions getPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }

}

