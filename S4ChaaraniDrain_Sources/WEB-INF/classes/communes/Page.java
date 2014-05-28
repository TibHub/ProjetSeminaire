package communes;

public class Page {

	public static String head = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML"
			+ " 1.0 Strict//EN\" "
			+ "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
			+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ "<head><meta content=\"text/html; charset=utf-8\" "
			+ "http-equiv=\"Content-Type\" />"
			+ "<link rel=\"stylesheet\" type=\"text/css\" "
			+ "href=\"styleCss/style.css\"/>"
			+ "<title>MySeminar - titre</title>" // "titre" à changer
			+ "</head><body>" + "<h1 id=\"logo\">MySeminar</h1>";

	public static String foot = "<div class=\"clear_both\">"
			+ "<p class=\"centre\">&copy;Tous droits r&eacute;serv&eacute;s - "
			+ "Descartes Inc.</p></div></body></html>";

	public static String compte = "<div id=\"formcompte\">"
			+ "<fieldset class=\"compte\">" + "<legend>Mon Compte</legend>"
			+ "<div id=\"liens_compte\">" + "<h4>Bonjour prenom nom !</h4>" // prenom
			// nom
			+ "<ul id=\"gestion_compte\">";

	public static String retourAccueil = 
			"<form id='retourAccueil' action='authent' method='post'>"
					+ "<input style='width:12em; border:none' "
					+ "type='submit' value=\"Revenir à l'accueil\"/>" +
					"</form>";
	///MODIF 02/03
	public static String retourDate = 
			"<a href='html/semDate.html'>Retour</a>";

	static String change(String toBeChanged, String toBeReplaced, String value) {

		if (value == null)
			return null;

		if (toBeChanged.indexOf(toBeReplaced) != -1) {
			toBeChanged = toBeChanged.replace(toBeReplaced, value);
		} else
			return null;

		return toBeChanged;
	}

	/*public static String notifErrFormPass() {
		return change(modifPass, "errorF", errFormer);
	}

	public static String notifErrNewPass() {
		return change(modifPass, "errorN", errNew);
	}

	public static String confModifPass() {
		return change(modifPass, "Modifier mon mot de passe",
				"Mot de passe changé !");
	}*/

	public static String header(String titre) {
		synchronized(head) {
			return change(head, "titre", titre);
		}
	}

	public static String footer() {
		return foot;
	}

	public static String gestionCompte(String prenom, String nom) {
		synchronized(compte) {
			return change(change(compte, "prenom", prenom), "nom", nom);
		}
	}

	public static String retourAccueil(){
		return retourAccueil;
	}
	public static String retourDate(){
		return retourDate;
	}

}
