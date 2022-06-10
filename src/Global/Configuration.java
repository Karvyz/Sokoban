package Global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Configuration {
	static final int silence = 1;

	public static InputStream charge(String s) {
		InputStream in = null;
		try {
			s = s.replace('/', File.separatorChar);
			in = new FileInputStream("res" + File.separator + s);
		} catch (FileNotFoundException e) {
			erreur("impossible de trouver le fichier " + s);
		}
		return in;
	}

	public static void affiche(int niveau, String message) {
		if (niveau > silence)
			System.err.println(message);
	}

	public static void info(String s) {
		affiche(1, "INFO : " + s);
	}

	public static void erreur(String s) {
		affiche(3, "ERREUR : " + s);
		System.exit(1);
	}
}
