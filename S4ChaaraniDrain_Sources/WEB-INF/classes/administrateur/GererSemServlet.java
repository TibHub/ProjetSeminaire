package administrateur;

import outilsBD.RequeteSQL;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import societe.AfficherSemDate;

import communes.Page;


public class GererSemServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");

		/*
		 * CREATION if (request.getParameter("intituleSem") != null) { Date d =
		 * AfficherSemDate.getDate(request.getParameter("jj")
		 * ,request.getParameter("mm") ,request.getParameter("aaaa")); if(d ==
		 * null) { //response.sendRedirect("erreur.html");
		 * out.println("<body style='font-family:Palatino Linotype'>");
		 * out.println("<h1>ERREUR 511</h1></br>");
		 * out.println("<p style='font-style:italic'>" +
		 * "La date sp&eacute;cifi&eacute;e est incorrecte.</p>");
		 * out.println("</body></html>"); } else { Object tSem[] = { d,
		 * request.getParameter("prixSem"), request.getParameter("intituleSem"),
		 * request.getParameter("idTheme"), request.getParameter("nbrePlaceMax")
		 * }; try { RequeteSQL.executeU(11, tSem); } catch (SQLException e) {
		 * e.printStackTrace(); } } }
		 */

		int idTheme = Integer.parseInt(request.getParameter("idTheme"));
		ResultSet seminaires;
		try {
			if (idTheme == 0)
				seminaires = RequeteSQL.executeQ(4, null);
			else
				seminaires = RequeteSQL.executeQ(3,
						request.getParameter("idTheme"));

			String[] tColonnes = { "Id", "Intitul&eacute;", "Date", "Prix",
					"Theme", "Nombre de places (max.)", "Supprimer" };

			affichSem(seminaires, out, tColonnes);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/*
		 * try { ResultSet themes = RequeteSQL.executeQ(2, null);
		 * 
		 * out.println("<form id='fSem' " + "action='creerSem' method='post'>");
		 * out.println("<br/><label> Intitule du seminaire </label>" +
		 * "<input type='text' name='intituleSem'>");
		 * out.println("<br/><label> Date du seminaire (jj/mm/aaaa) </label>" +
		 * "<input type='text' name='jj' maxlength='2'>" +
		 * " / <input type='text' name='mm' maxlength='2'>" +
		 * " / <input type='text' name='aaaa' maxlength='4'>");
		 * out.println("<br/><label> Prix du seminaire </label>" +
		 * "<input type='text' name='prixSem'>");
		 * out.println("<br/><label> Libelle du theme </label>");
		 * out.println("<select name='libTheme'>"); while(themes.next()) {
		 * out.println("<option value='" + themes.getInt("IdTheme") + "'>" +
		 * themes.getString("LibelleTheme") + "</option>"); }
		 * out.println("<br/><label> Nombre de places maximum </label>" +
		 * "<input type='text' name='nbrePlaceMax'>");
		 * out.println("<input type='submit' value='Envoyer'>");
		 * out.println("</form>");
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); }
		 */
	}

	protected int affichSem(ResultSet seminaires, PrintWriter out,
			String[] tColonnes) throws SQLException {
		if (!seminaires.next()) {
			// cas d'erreur à gérer // pas de séminaires
			// response.sendRedirect("erreur.html");
			out.println("<body style='font-family:Palatino Linotype'>"
					+ "<html><h1>Aucun s&eacute;minaire.</h1></br>"
					+ "<p style='font-style:italic'>"
					+ " Remarque : Vous les avez peut-être "
					+ "tous effac&eacute;s.</p></html></body>");

			affichFiltre(out);
			out.println(Page.retourAccueil());
			out.println(Page.footer());

			return -1;
		} else {

			/**
			 * On édite la page de réponse
			 */
			String title = "Tous les s&eacute;minaires";
			out.println(Page.header(title));

			out.println("<div id='main'>");
			out.println("<h1 class='seminaires'>Tous mes s&eacute;minaires</h1>");
			out.println("<table id='liste'><thead><tr class='l1'>");

			String classe = "";
			for (int i = 0; i < tColonnes.length; i++) {
				if (i == 0)
					classe = " class='gf'";
				else
					classe = "";

				out.println("<th" + classe + ">" + tColonnes[i] + "</th>");
			}

			out.println("</tr> </thead>");

			int nbTuples = 0;
			String class1 = "gf", class2 = "gc"; // pour le css
			// while (seminaires.next()) {
			do {
				nbTuples++;
				if (nbTuples % 2 == 0)
					out.println("<tr class='" + class2 + "'>" + "<td class='"
							+ class2 + "'>");
				else
					out.println("<tr class='" + class1 + "'>" + "<td class='"
							+ class1 + "'>");

				out.println(seminaires.getInt("IdSem") + "</td>");
				out.println("<td>" + seminaires.getString("IntituleSem")
						+ "</td>");
				out.println("<td>" + seminaires.getDate("DateSem") + "</td>");
				out.println("<td>" + seminaires.getInt("PrixSem") + "</td>");
				// if (idth != 0)
				out.println("<td>" + seminaires.getString("LibelleTheme")
						+ "</td>");
				out.println("<td>" + seminaires.getInt("NbrePlaceMax")
						+ "</td>");

				specColumns(out, seminaires);

			} while (seminaires.next());/* fin while */

			affichFiltre(out);
			out.println(Page.retourAccueil());
			out.println(Page.footer());
			return 0;
		}
	}

	private void affichFiltre(PrintWriter out) throws SQLException {
		out.println("<div id=\"formthm\">"
				+ "<form action=\"\" method=\"get\" id=\"form_thm\">"
				+ "<fieldset class=\"themes\">"
				+ "<legend>Choisir le filtre</legend>"
				+ "<div id=\"selection\">"
				+ "<label for=\"idTheme\">Thème choisi : </label>"
				+ "<select name=\"idTheme\">"
				+ "<option value=\"0\" selected=\"selected\">Aucun</option>");

		ResultSet themes = RequeteSQL.executeQ(2, null);
		if (!themes.next()) {
			return;
		} else {
			while (themes.next())
				out.println("<option value='" + themes.getInt("IdTheme") + "'>"
						+ themes.getString("LibelleTheme") + "</option>");
		}

		out.println("</select>"
				+ "<input type=\"submit\" value=\"Envoyer\" id=\"button\"/>"
				+ "</div></fieldset></form></div>");
	}

	protected void specColumns(PrintWriter out, ResultSet seminaires)
			throws SQLException {
		String supprimer = "<a href='supprimer-seminaire?ids="
				+ seminaires.getInt("IdSem") + "'>Supprimer</a>";
		out.println("<td>" + supprimer + "</td></tr>");
	}
}
