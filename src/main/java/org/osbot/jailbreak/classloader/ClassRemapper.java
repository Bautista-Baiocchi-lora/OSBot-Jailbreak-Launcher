package org.osbot.jailbreak.classloader;

/**
 * Created by Ethan on 2/1/2018.
 */


import jdk.internal.org.objectweb.asm.commons.Remapper;

import java.util.HashMap;

public class ClassRemapper extends Remapper {
	private static HashMap<String, String> remapNames = new HashMap<String, String>();

	static {
		remapNames.put("java/net/NetworkInterface", "org/NetworkInterface");
	}

	@Override
	public String map(String str) {
		String s = remapNames.get(str);
		if (s != null) {
			return s;
		} else {
			return str;
		}
	}
}