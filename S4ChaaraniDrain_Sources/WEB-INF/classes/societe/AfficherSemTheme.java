package societe;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

import communes.Page;

import outilsBD.RequeteSQL;

public class AfficherSemTheme extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		
		try {
			ResultSet sem = RequeteSQL.executeQ(3, request.getParameter("IdTheme"));
			String title = "Séminaires par th&eagrave;me";
			out.println(Page.header(title));
			
			afficherSem(sem, out);
		} catch (SQLException e) {
			out.println("exception");
			e.printStackTrace();
		}
		response.setContentType("text/html");
		out.println(Page.footer());
	}
	
	protected void afficherSem(ResultSet sem, PrintWriter out) throws SQLException {
		
		if(!sem.next()) {
			out.println("<body style='font-family:Palatino Linotype'>"
						+ "<html><h1>Aucun s&eacute;minaire.</h1></br>");
			out.println(Page.retourAccueil());
		}
		else {
			out.println("<form id='fSem' " +
					"action='inscription' method='post'>");
			//modif
			out.println("<table id='tSem'>");
			out.println("<tr> <th> S'inscrire </th>" +
						"<th>Intitul&eacute; du seminaire </th>" +
						"<th>Date</th>" +
						"<th>Prix</th>" +
						"<th>Libelle du theme</th>" +
						"<th>Nombre de place (max.)</th></tr>");
			do {
				out.println("<tr><td><input type='radio' name='rBtn' " +
							"value ='" + sem.getInt("IdSem") + "'></td>"); 
				out.println("<td>" + sem.getString("IntituleSem") + "</td>");
				out.println("<td>" + sem.getDate("DateSem") + "</td>");
				out.println("<td>" + sem.getFloat("PrixSem") + "euro(s)</td>");
				out.println("<td>" + sem.getString("LibelleTheme") + "</td>");
				out.println("<td>" + sem.getInt("NbrePlaceMax") + "</td></tr>");
			} while (sem.next());
			out.println("<br/><label> Nombre de places </label>" +
						"<input type='text' name='Nb'>");
			out.println("<input type='submit' value='Envoyer'>");
			out.println("</form>");
		}
	}
}
