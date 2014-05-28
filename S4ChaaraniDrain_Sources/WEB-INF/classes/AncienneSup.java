

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

import outilsBD.RequeteSQL;

import data.User;

public class AncienneSup extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		/**
		 * On r�cup�re le PrintWriter de la r�ponse
		 */
		PrintWriter out = response.getWriter();

		/**
		 * On positionne le type du contenu de la page de r�ponse
		 */
		response.setContentType("text/html");

		/**
		 * On ouvre la session
		 */
		HttpSession laSession = request.getSession(true);

		/**
		 * On r�cup�re l'User stock� dans la session
		 */
		User util = (User) laSession.getAttribute("user");

		/**
		 * On v�rifie qu'il s'agit bien d'un animateur
		 */
		if (util.getType() != User.ANIM) {
			// cas d'erreur � g�rer
			response.sendRedirect("erreur.html");
		} else {
			/**
			 * On recherche tous ses s�minaires <=> ceux sur lesquels il
			 * intervient
			 */

			// on r�cup�re son identifiant dans la BD
			Integer idAnimSession = util.getId();

			// on le recherche dans la BD pour avoir ses noms
			ResultSet animateur;
			String prenom, nom;
			try {
				animateur = RequeteSQL.executeQ(15, idAnimSession);
				if (!animateur.next()) {
					// cas d'erreur � g�rer // pas d'anim de cet id l� dans
					// la BD
					response.sendRedirect("erreur.html");
				} else {
					// on a l'animateur
					prenom = animateur.getString("PrenomAnim");
					nom = animateur.getString("NomAnim");

					// on recherche ses s�minaires

					int idth = Integer
							.parseInt(request.getParameter("idTheme"));

					ResultSet seminaires;

					if (idth == 0)
						seminaires = RequeteSQL.executeQ(8, idAnimSession);
					else {
						Integer[] args = { new Integer(idth), idAnimSession };
						seminaires = RequeteSQL
								.executeQ(9, args /* idAnimSession */);
					}

					if (!seminaires.next()) {
						// cas d'erreur � g�rer // pas de s�minaires
						// response.sendRedirect("erreur.html");
						out.println("<body style='font-family:Palatino Linotype'>"
								+ "<html><h1>ERREUR 201</h1></br>"
								+ "<p>Aucun s&eacute;minaire.</p>"
								+ "<p style='font-style:italic'>"
								+ " Remarque : Vous les avez peut-�tre "
								+ "tous effac&eacute;s.</p></html></body>");
					} else {

						/**
						 * On �dite la page de r�ponse
						 */
						String title = "Tous mes s�minaires";
						out.println(Page.header(title));

						out.println("<div id=\"main\">"
								+ "<h1 class=\"seminaires\">"
								+ "Tous mes s&eacute;minaires</h1>"
								+ "<table id=\"liste\"><thead><tr class=\"l1\">"
								+ "<th class=\"gf\">id</th><th>Intitul�</th>"
								+ "<th>Date</th><th>Prix</th><th>Th�me</th>"
								+ "<th>Nombre de places (max.)</th>"
								+ "<th>Dur&eacute;e d'intervention</th>"
								+ "<th>Ordre de passage</th>"
								+ "<th>Supprimer</th>" + "</tr>" + "</thead>");
						int nbTuples = 0;
						String class1 = "gf", class2 = "gc"; // pour le css
						// while (seminaires.next()) {
						do {
							nbTuples++;
							if (nbTuples % 2 == 0)
								out.println("<tr class='" + class2 + "'>"
										+ "<td class='" + class2 + "'>");
							else
								out.println("<tr class='" + class1 + "'>"
										+ "<td class='" + class1 + "'>");

							out.println(seminaires.getInt("IdSem") + "</td>"
									+ "<td>"
									+ seminaires.getString("IntituleSem")
									+ "</td>" + "<td>"
									+ seminaires.getDate("DateSem") + "</td>"
									+ "<td>" + seminaires.getInt("PrixSem")
									+ "</td>" + "<td>");
							// if (idth != 0)
							out.println(seminaires.getString("LibelleTheme")
									+ "</td>" + "<td>");

							out.println(seminaires.getInt("NbrePlaceMax")
									+ "</td>"
									+ "<td>"
									+ seminaires.getInt("DureeInter")
									+ "</td>"
									+ "<td>"
									+ seminaires.getInt("OrdrePassage")
									+ "</td>"
									+ "<td><a href='supprimer-intervention?ida="
									+ idAnimSession + "&ids="
									+ seminaires.getInt("IdSem")
									+ "'>Supprimer</a></td>" + "</tr>");
						} while (seminaires.next());/* fin while */

						out.println(Page.gestionCompte(prenom, nom));

						out.println("<div id=\"formthm\">"
								+ "<form action=\"\" method=\"get\" id=\"form_thm\">"
								+ "<fieldset class=\"themes\">"
								+ "<legend>Choisir le filtre</legend>"
								+ "<div id=\"selection\">"
								+ "<label for=\"idTheme\">Th�me choisi : </label>"
								+ "<select name=\"idTheme\">"
								+ "<option value=\"0\" selected=\"selected\">Aucun</option>");

						ResultSet themes = RequeteSQL.executeQ(2, null);
						if (!themes.next()) {
							// / g�rer l'erreur
							response.sendRedirect("erreur.html");
						} else {
							while (themes.next())
								out.println("<option value='"
										+ themes.getInt("IdTheme") + "'>"
										+ themes.getString("LibelleTheme")
										+ "</option>");
						}

						out.println("</select>"
								+ "<input type=\"submit\" value=\"Envoyer\" id=\"button\"/>"
								+ "</div></fieldset></form></div>");

						out.println("<li><a href=\"\">Modifier mon mot de passe"
								+ "</a></li>");
						out.println("<li><a href='deconnexion'>Me d&eacute;connecter</a></li>"
								+ "</ul></div></fieldset>");
						out.println(Page.retourAccueil());
						out.println(Page.footer());
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		out.println("</body></html>");
	}
}
