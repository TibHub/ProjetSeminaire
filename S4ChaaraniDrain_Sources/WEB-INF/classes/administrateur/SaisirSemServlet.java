package administrateur;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import communes.Page;

import outilsBD.RequeteSQL;

public class SaisirSemServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");

		String titre = "Cr&eacute;er un s&eacute;minaire - Etape 1 sur 2";
		out.println(Page.header(titre));
		
		try {
			ResultSet themes = RequeteSQL.executeQ(2, null);
			out.println("<h1>" + titre + "</h1>");
			out.println("<form id='fSem' "
					+ "action='creer-seminaire-2' method='post'>");
			out.println("<br/><label> Intitule du seminaire </label>"
					+ "<input type='text' name='intituleSem'>");
			out.println("<br/><label> Date du seminaire (jj/mm/aaaa) </label>"
					+ "<input type='text' name='jj' maxlength='2'>"
					+ " / <input type='text' name='mm' maxlength='2'>"
					+ " / <input type='text' name='aaaa' maxlength='4'>");
			out.println("<br/><label> Prix du seminaire </label>"
					+ "<input type='text' name='prixSem'>");
			out.println("<br/><label> Libelle du theme </label>");
			out.println("<select name='libTheme'>");
			while (themes.next()) {
				out.println("<option value='" + themes.getInt("IdTheme") + "'>"
						+ themes.getString("LibelleTheme") + "</option>");
			}
			out.println("</select><br/><label> Nombre de places maximum </label>"
					+ "<input type='text' name='nbrePlaceMax'>");
			out.println("<input type='submit' value='Envoyer'>");
			out.println("</form>");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println(Page.retourAccueil());
		out.println(Page.footer());
	}

}
