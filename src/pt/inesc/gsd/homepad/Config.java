package pt.inesc.gsd.homepad;

public class Config {

	public static final String DOT_PATH = "/usr/local/bin/dot";
	public static final String SWIPL_PATH = "/usr/local/Cellar/swi-prolog/7.2.3/libexec/bin/swipl";
	

	public static String getManifestGraphvizDir() {
		String classPath = Config.class.getProtectionDomain().
				getCodeSource().getLocation().getPath();
		return classPath + "../out/";
	}

	public static String getModelDir() {
		String classPath = Config.class.getProtectionDomain().
				getCodeSource().getLocation().getPath();
		return classPath + "../out/";
	}

	public static String getModelCheckerDir() {
		String classPath = Config.class.getProtectionDomain().
				getCodeSource().getLocation().getPath();
		return classPath + "../out/";
	}
}
