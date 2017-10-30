package pt.inesc.gsd.homepad;

public class Logger {

	public static final int ER = -1; // error
	public static final int MS = 0;  // message
	public static final int LE = 1;  // layout event
	public static final int GE = 2;  // global event
	public static final int AP = 3;  // application
	
	private static final int[] _active = {
			ER, 
			MS, 
			LE, 
			GE, 
			AP
		};
	
	private String _author;
	
	public Logger(String author) {
		_author = author;
	}

	public static Logger getLogger(Class<?> c) {
		return new Logger(c.getSimpleName());
	}
	
	public void log(int type, String s) {
		for (int i = 0; i < _active.length; i++) {
			if (_active[i] == type) {
				switch (type) {
					case ER:
						System.out.println("ER: [" + _author + "] " + s);
						break;
					case MS:
						System.out.println("MS: [" + _author + "] " + s);
						break;
					case LE:
						System.out.println("LE: [" + _author + "] " + s);
						break;
					case GE:
						System.out.println("GE: [" + _author + "] " + s);
						break;
					case AP:
						System.out.println("AP: [" + _author + "] " + s);
						break;
				}
				return;
			}
		}
	}
}