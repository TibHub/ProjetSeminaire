package administrateur;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import communes.Page;

import outilsBD.RequeteSQL;
import societe.AfficherSemDate;

public class CreerSemServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");

		String titre = "Cr&eacute;er un s&eacute;minaire - Etape 2 sur 2";
		out.println(Page.header(titre));

		if (request.getParameter("intituleSem") != null) {
			Date d = AfficherSemDate.getDate(request.getParameter("jj"),
					request.getParameter("mm"), request.getParameter("aaaa"));
			if (d == null) {
				out.println("<body style='font-family:Palatino Linotype'>");
				out.println("<h1>ERREUR 511</h1></br>");
				out.println("<p style='font-style:italic'>"
						+ "La date sp&eacute;cifi&eacute;e est incorrecte.</p>");
				out.println(Page.retourAccueil());
				out.println(Page.footer());
				return;
			} else {
				Object tVerif[] = { request.getParameter("intituleSem"), d };
				try {
					ResultSet res = RequeteSQL.executeQ(20, tVerif);
					if (res.next()) {
						out.println("<h1> Erreur ! </h1>");
						out.println("<p>Ce seminaire existe déjà.</p>");
						out.println(Page.retourAccueil());
						out.println(Page.footer());
						return;
					}

					Object tSem[] = { d, request.getParameter("intituleSem"),
							new Float(Float.parseFloat(request.getParameter("prixSem"))),
							new Integer(Integer.parseInt(request.getParameter("libTheme"))),
							new Integer(Integer.parseInt(request.getParameter("nbrePlaceMax"))) };

					int i = RequeteSQL.executeU(11, tSem);

					out.println("<h1> Ajout effectué. </h1>");
					out.println(Page.retourAccueil());
					out.println(Page.footer());

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
