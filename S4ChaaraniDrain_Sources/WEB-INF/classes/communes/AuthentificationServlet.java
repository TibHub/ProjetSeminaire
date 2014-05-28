package communes;

import outilsBD.RequeteSQL;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import communes.Page;

import data.User;


public class AuthentificationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String[] fonctions = { "Société", "Animateur",
	"Administrateur" };

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");

		/**
		 * On édite le début de l'en-tête de la réponse
		 */
		/*String title = "Mon espace";
		out.println(Page.header(title));*/

		boolean isLogged = false;

		HttpSession session = request.getSession(true);
		if (session.getAttribute("user") != null)
			isLogged = true;

		boolean erreur = false;
		if (!isLogged) {
			erreur = login(request.getParameter("login"), request.getParameter("password")
					, response, session);
			if(erreur) // MODIF 01/03
				//response.sendRedirect("erreur.html");
				out.println("<h1>ERREUR 412</h1></br>"
								+ "<p style='font-style:italic'>"
								+ "Vos login et mot de passe sont erron&eacute;s</p>");
		}
		if (!erreur) {
			//modif 01/03
			/**
			 * On édite le début de l'en-tête de la réponse
			 */
			String title = "Mon espace";
			out.println(Page.header(title));
			
			boolean soc = false, anim = false, admin = false;
			User user = (User) session.getAttribute("user");

			if (user.getType() == User.SOC)
				soc = true;
			else if (user.getType() == User.ANIM)
				anim = true;
			else if (user.getType() == User.ADMIN)
				admin = true;

			try {
				/**
				 * On fini d'éditer la page
				 */

				String prenom = "";
				String nom = "administrateur";
				out.println("<div id=\"main\">" 
						+ "<h1 class=\"espace\">Mon Espace");
				if (soc) {
					out.println(fonctions[0]
							+ "</h1>"
							+ "<ul id=\"menu\">"
							+ "<li><a href='SemSociete?IdTheme=1'>Visualiser tous les s&eacute;mi" 
							+ "naires d'un th&egrave;me</a></li>"
							+ "<li><a href='html/semDate.html'>Visualiser les s&eacute;minaire" 
							+ "s ayant lieu &agrave; une date donn&eacute;e ou" 
							+ " sur une p&eacute;riode donn&eacute;e</a></li>"
							+ "</ul></div>");

					ResultSet societe = RequeteSQL.executeQ(16, new Integer(user.getId()));
					if (societe.next()) {
						nom = societe.getString("RaisonSociale");
					}
				}

				if (anim) {
					out.println(fonctions[1]
							+ "</h1>"
							+ "<ul id=\"menu\">"
							+ "<li><a href='visualiser-mes-seminaires?idTheme=0'>Visualiser mes " +
							"s&eacute;minaires</a></li>"
							+ "</ul></div>"); //MODIF 01/03
					ResultSet animateur = RequeteSQL.executeQ(15, new Integer(user.getId()));
					if (animateur.next()) {
						prenom = animateur.getString("PrenomAnim");
						nom = animateur.getString("NomAnim");
					}
				}

				if (admin) {
					out.println(fonctions[2]
							+ "</h1>"
							+ "<ul id=\"menu\">"
							+ "<li><a href='creer-seminaire-1'>Cr&eacute;er un " +
							"s&eacute;minaire</a></li>"
							+ "<li><a href='gerer-les-seminaires?idTheme=0'>Supprimer un " +
							"s&eacute;minaire</a></li>"
							+ "<li><a href='creer-intervention'>Enregistrer une nouvelle " +
							"intervention pour un animateur</a></li>"
							+ "</ul></div>");
				}

				out.println(Page.gestionCompte(prenom, nom));

				if(soc || anim)
					out.println("<li><a href='html/modifpass.html'>Modifier mon mot de passe" +
							"</a></li>");
				out.println("<li><a href='deconnexion'>Me d&eacute;connecter</a></li>"
						+ "</ul></div></fieldset></div>");
				
				
				out.println(Page.footer());

			} catch (SQLException e) {
				out.println(e);
			}
			out.println("</body></html>");
		}

	}

	private boolean login(String login, String password,
			HttpServletResponse response,
			HttpSession session) {
		/**
		 * On procède à l'authentification
		 */
		// on prépare le tableau des arguments nécessaires à cette opération
		String[] args = new String[2];
		args[0] = login;
		args[1] = password;

		ResultSet resultat;

		try {
			resultat = RequeteSQL.executeQ(0, args);

			if (!resultat.next()) {
				return true;
			}
			else {
				// on récupère les données à encapsuler dans la variable session
				String loginBD = resultat.getString("login");
				String passBD = resultat.getString("password");
				int idBD = 0; // par défaut, correspond à l'admin
				int type = User.ADMIN; // par défaut, correspond à l'admin
				// on cherche à savoir s'il s'agit d'un animateur ou d'une société

				int idSocBD = resultat.getInt("idSoc");
				int idAnimBD = resultat.getInt("idAnim");

				if(idSocBD != 0 ) {
					idBD = idSocBD;
					type = User.SOC;
				}

				if(idAnimBD != 0) {
					idBD = idAnimBD;
					type = User.ANIM;
				}

				if(idSocBD == 0 && idAnimBD == 0){
					if(resultat.getInt("estAdmin") != 0)
						idBD = 0;
				}
				/**
				 * On encapsule les données nécessaires dans un objet User
				 */
				User user = new User(loginBD, idBD, type, passBD);

				/**
				 * On enregistre l'User dans la session
				 */
				session.setAttribute("user", user);

				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}

}
