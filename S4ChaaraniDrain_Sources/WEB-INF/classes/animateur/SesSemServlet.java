package animateur;

import outilsBD.RequeteSQL;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import administrateur.GererSemServlet;

import communes.Page;


import data.User;

public class SesSemServlet extends GererSemServlet {
	
	private int idAnim;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		/**
		 * On récupère le PrintWriter de la réponse
		 */
		PrintWriter out = response.getWriter();

		/**
		 * On positionne le type du contenu de la page de réponse
		 */
		response.setContentType("text/html");

		/**
		 * On ouvre la session
		 */
		HttpSession laSession = request.getSession(true);

		/**
		 * On récupère l'User stocké dans la session
		 */
		User util = (User) laSession.getAttribute("user");

		/**
		 * On vérifie qu'il s'agit bien d'un animateur
		 */
		if (util.getType() != User.ANIM) 
			// cas d'erreur à gérer
			response.sendRedirect("erreur.html");
		else {
			/**
			 * On recherche tous ses séminaires <=> ceux sur lesquels il
			 * intervient
			 */

			// on récupère son identifiant dans la BD
			this.idAnim = util.getId();

			// on le recherche dans la BD pour avoir ses noms
			ResultSet animateur;
			String prenom, nom;
			try {
				animateur = RequeteSQL.executeQ(15, idAnim);
				if (!animateur.next()) {
					// cas d'erreur à gérer // pas d'anim de cet id là dans
					// la BD
					response.sendRedirect("erreur.html");
				} else {
					// on a l'animateur
					prenom = animateur.getString("PrenomAnim");
					nom = animateur.getString("NomAnim");

					// on recherche ses séminaires

					int idth = Integer
							.parseInt(request.getParameter("idTheme"));

					ResultSet seminaires;

					if (idth == 0)
						seminaires = RequeteSQL.executeQ(8, idAnim);
					else {
						Integer[] args = { new Integer(idth), idAnim };
						seminaires = RequeteSQL
								.executeQ(9, args /* idAnimSession */);
					}

					String[] tColonnes = 
						{
							"Id", "Intitul&eacute;", "Date", "Prix", "Theme", 
							"Nombre de places (max.)", "Dur&eacute;e d'intervention", 
							"Ordre de passage", "Supprimer"
						};
					
					affichSem(seminaires, out, tColonnes);

					/*out.println("</select>"
							+ "<input type=\"submit\" value=\"Envoyer\" id=\"button\"/>"
							+ "</div></fieldset></form></div>");

					out.println("<li><a href=\"\">Modifier mon mot de passe"
							+ "</a></li>");
					out.println("<li><a href='deconnexion'>Me d&eacute;connecter</a></li>"
							+ "</ul></div></fieldset>");
					out.println(Page.retourAccueil());
					out.println(Page.footer());*/
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void specColumns(PrintWriter out, ResultSet sem) 
			throws SQLException {

		out.println("<td>" + sem.getInt("DureeInter") + "</td>");
		out.println("<td>" + sem.getInt("OrdrePassage") + "</td>");

		String supprimer = "<a href='supprimer-intervention?ida="
				+ idAnim + "&ids="
				+ sem.getInt("IdSem")
				+ "'>Supprimer</a>";
		out.println("<td>" + supprimer + "</td></tr>");
	}
}
