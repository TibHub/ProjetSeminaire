package administrateur;

import outilsBD.RequeteSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


public class Appli {
	private static ArrayList<String> quitter = new ArrayList<String>();
	private static String[] keys = { "q", "quit", "exit" };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner clavier = new Scanner(System.in);
		initialiser(quitter);
		// Scénario : l'admin s'authentifie donc on ne lui (re)demande pas son
		// mot de passe actuel pour le changer

		String[] donnees;
		donnees = getData(clavier);
		boolean authentifié = false;
		try {
			authentifié = authentification(donnees);

			while (!authentifié) {
				System.out.println("Détails de connexion invalides. "
						+ "Réessayez ci-dessous.");
				donnees = getData(clavier);
				authentifié = authentification(donnees);
				if (authentifié)
					break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String loginBD = donnees[0];
		String previousPass = donnees[1];

		donnees = getPass(clavier);

		boolean okpass = check(donnees);

		while (!okpass) {
			System.out.println("Le nouveau mot de passe "
					+ "et sa confirmation ne sont pas identiques !");
			donnees = getPass(clavier);
			okpass = check(donnees);
			if (okpass)
				break;
		}

		if (donnees[0].equals(previousPass))
			System.out.println("Votre mot de passe est inchangé car identique"
					+ "à votre mot de passe actuel !!! ");
		else {
			try {
				String[] toto = { donnees[0], loginBD };
				majPass(toto);
				System.out.println("Votre mot de passe a bien été modifié !!!");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("!!! ERREUR BD !!! ");
			}
		} /* fin else */

	}

	private static void initialiser(ArrayList<String> keywords) {
		StringBuffer ligne = new StringBuffer(
				"Vous pouvez quitter à tout moment le programme"
						+ "\nen saisissant l'un des mots-clés suivants "
						+ "\n(quelque soit leur casse) : \n");
		for (int i = 0; i < keys.length; ++i) {
			keywords.add(i, keys[i]);
			ligne.append(" " + keywords.get(i) + " ");
		}
		
		System.out.println(ligne.toString());
	}

	private static void majPass(String[] nouveauPass) throws SQLException {

		/* int changement = */RequeteSQL.executeU(1, nouveauPass);
	}

	private static boolean check(String[] donnees) {
		if (donnees[0].equals(donnees[1])) // seule condition ?! longueur ?
			return true;
		return false;
	}

	private static String[] getPass(Scanner clavier) {
		System.out.println("Saisissez votre nouveau mot de passe : ");
		String newpass = clavier.nextLine();
		if (quitter.contains(traiter(newpass).toLowerCase()))
			System.exit(-1);
		System.out.println("Confirmez votre nouveau mot de passe : ");
		String conf = clavier.nextLine();
		if (quitter.contains(traiter(conf).toLowerCase()))
			System.exit(-1);

		String[] passwords = { traiter(newpass), traiter(conf) };
		return passwords;
	}

	private static String traiter(String toto) {
		return toto.trim();
	}

	private static String[] getData(Scanner clavier) {
		String loginlu, mdplu;

		System.out.println("Saisissez votre login : ");

		loginlu = clavier.nextLine();
		if (quitter.contains(traiter(loginlu).toLowerCase()))
			System.exit(-1);

		System.out.println("Saississez votre mot de passe : ");

		mdplu = clavier.nextLine();
		if (quitter.contains(traiter(mdplu).toLowerCase()))
			System.exit(-1);

		String[] data = { traiter(loginlu), traiter(mdplu) };

		return data;
	}

	private static boolean authentification(String[] donnees)
			throws SQLException {

		ResultSet admin = RequeteSQL.executeQ(0, donnees);

		if (!admin.next())
			return false;

		else {

			if (admin.getInt("estAdmin") == 1)
				return true;
			else
				return false;

		}
	}

}
