package org.osbot.jailbreak.data;

import java.io.File;

public class Constants {

	public static final String DIRECTORY_PATH = System.getProperty("user.home") + File.separator + ".OSBot";
	public static final String CONFIG_FILE = "config.jailbreak";
	public static final String JAILBREAK_JAR = "Jailbreak.jar";
	public static final String APPLICATION_NAME = "BotApplication";
	public static final String SYMMETRIC_ALGORITHM = "AES";
	public static final String SYMMETRIC_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	public static final boolean DEV_MODE = true;
	public static final boolean VERIFY_LAUNCHER_VERSION = !DEV_MODE;
	public static final boolean LOAD_LOCAL_JAILBREAK = DEV_MODE;
	public static final boolean RUN_THROUGH_IDE = DEV_MODE;


}
