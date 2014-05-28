package societe;

import outilsBD.RequeteSQL;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.http.*;

import communes.Page;

import data.User;


public class InscriptionSem extends HttpServlet {

	private static Object verrou = new Object();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");

		try {
			String title = "Confirmation d'inscription";
			out.println(Page.header(title));
			out.println(Page.retourAccueil());
			// Récupération du nombre de places réservées et nombre maximum
			// de places admises
			int r, nbDem;
			ResultSet res;

			synchronized(this) {
				Integer tVerif[] = { user.getId(),
						Integer.parseInt(request.getParameter("rBtn")) };
				res = RequeteSQL.executeQ(19, tVerif);

				if (res.next()) {
					out.println("<body style='font-family:Palatino Linotype'>"
							+ "<html><h1>ERREUR 307</h1></br>"
							+ "<p style='font-style:italic'>"
							+ "Vous êtes d&eacute;jà inscrit &agrave; ce s&eacute;minaire !</p></html></body>");
					return;
				}
				res = RequeteSQL.executeQ(17, request.getParameter("rBtn"));
				int nb = 0, nbMax = 0;
				int i = Integer.parseInt(request.getParameter("rBtn"));

				res.next();
				nb = res.getInt("NbInterventions");
				nbMax = res.getInt("NbrePlaceMax");

				// Comparaison avec le nombre de places demandées
				nbDem = Integer.parseInt(request.getParameter("Nb"));
				if (nbDem == 0 || nbDem > nbMax - nb)
					// response.sendRedirect("erreur.html");
					out.println("<body style='font-family:Palatino Linotype'>"
							+ "<html><h1>ERREUR 308</h1></br>"
							+ "<p style='font-style:italic'>"
							+ "Il ne reste pas assez de places pour le nombre de place demand&eacute; ! </p></html></body>");

				Integer tInscription[] = { user.getId(),
						Integer.parseInt(request.getParameter("rBtn")),
						Integer.parseInt(request.getParameter("Nb")) };

				r = RequeteSQL.executeU(5, tInscription);
			}

			if (r <= 0) {
				out.println("<h1>Vous ne pouvez pas vous inscrire deux fois au"
						+ " même séminaire.</h1>");
			} else {
				// On récupére les informations du séminaire
				res = RequeteSQL.executeQ(18, request.getParameter("rBtn"));
				res.next();
				out.println("<div id='confirmation'>");
				out.println("<h2> Récapitulatif de l'inscription : </h2>");
				out.println("<p id='recap'>" + res.getString("IntituleSem")
						+ "<br/>" + res.getDate("DateSem") + "<br/>"
						+ res.getString("LibelleTheme") + "<br/>"
						+ "Nombre de places reservées : " + nbDem + "<br/>"
						+ "Prix total : " + (nbDem * res.getInt("PrixSem"))
						+ "euro(s) ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		response.setContentType("text/html");
		out.println(Page.footer());

	}
}
