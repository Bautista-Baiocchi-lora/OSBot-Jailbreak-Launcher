package org.osbot.jailbreak.data;

import java.io.File;

public class Constants {

	public static final String DIRECTORY_PATH = System.getProperty("user.home") + File.separator + ".OSBot";
	public static final String JAILBREAK_JAR = "Jailbreak.jar";
	public static final String JAILBREAK_LAUNCHER = "Jailbreak Launcher.jar";
	public static final String[] JDK_EXT_FILES = {
			"cldrdata.jar", "dnsns.jar", "jaccess.jar", "jfxrt.jar", "localedata.jar", "nashorn.jar",
			"sunec.jar", "sunjce_provider.jar", "sunpkcs11.jar", "zipfs.jar",
	};
	public static final String[] JDK_JRE_LIB_FILES = {
			"charsets.jar", "deploy.jar", "javaws.jar", "jce.jar", "jfr.jar", "jfxswt.jar", "jsse.jar", "management-agent.jar", "plugin.jar", "resources.jar",
			"rt.jar",
	};
	public static final String[] JDK_LIB_FILES = {
			"ant-javafx.jar", "dt.jar", "javafx-mx.jar", "jconsole.jar", "packager.jar", "sa-jdi.jar", "tools.jar"
	};
	public static final String APPLICATION_NAME = "BotApplication";
	public static final boolean LOAD_LOCAL = true;


}
