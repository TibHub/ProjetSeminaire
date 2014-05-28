package societe;


import outilsBD.RequeteSQL;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.*;
import javax.servlet.http.*;


import communes.Page;


public class AfficherSemDate extends AfficherSemTheme {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		PrintWriter out = response.getWriter();

		/*
		 * out.println("<html>"); out.println("<head>");
		 * out.println("<title> MySeminar - Seminaires </title>");
		 * out.println("</head>"); out.println("<body>");
		 */

		out.println(request.getParameter("jj1") + "/"
				+ request.getParameter("mm1") + "/"
				+ request.getParameter("aaaa1"));

		Date d = getDate(request.getParameter("jj1"),
				request.getParameter("mm1"), request.getParameter("aaaa1"));
		out.println(d);
		if (d == null) {// modif 01/03
			// response.sendRedirect("erreur.html");
			out.println("<body style='font-family:Palatino Linotype'>"
					+ "<html><h1>ERREUR 511</h1></br>"
					+ "<p style='font-style:italic'>"
					+ " La date sp&eacute;cifi&eacute;e est incorrecte.</p></html></body>");
			Page.retourDate();

		}

		else {
			ResultSet res = null;
			try {
				if (request.getParameter("jj2") != ""
						&& request.getParameter("mm2") != ""
						&& request.getParameter("aaaa2") != "") {
					Date d2 = getDate(request.getParameter("jj2"),
							request.getParameter("mm2"),
							request.getParameter("aaaa2"));

					if (d2 == null) { // modif 01/03
						// response.sendRedirect("erreur.html");
						out.println("<body style='font-family:Palatino Linotype'>"
								+ "<html><h1>ERREUR 512</h1></br>"
								+ "<p style='font-style:italic'>"
								+ " La deuxième date sp&eacute;cifi&eacute;e est incorrecte.</p></html></body>");
						Page.retourDate();
					}

					Date Td[] = { d, d2 };
					res = RequeteSQL.executeQ(7, Td);
				} else {
					res = RequeteSQL.executeQ(6, d);
				}
				// modif 01/03
				String title = "Séminaires";
				out.println(Page.header(title));

				afficherSem(res, out);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		response.setContentType("text/html");
		out.println(Page.footer());
		/*
		 * out.println("</body>"); out.println("</html>");
		 */
	}

	private static boolean verifDate(String date) {
		SimpleDateFormat s = new SimpleDateFormat("ddMMyyyy");
		// La chaine doit pouvoir être directement convertie en date
		// Le décalage automatique réalisé sur les dates incorrectes doit
		// être désactivé
		s.setLenient(false);
		try {
			java.util.Date d = s.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static Date getDate(String jour, String mois, String annee) {
		Date d = null;
		if (verifDate(jour + mois + annee)) {
			int j = Integer.parseInt(jour);
			int m = Integer.parseInt(mois);
			int a = Integer.parseInt(annee);

			Calendar c = new GregorianCalendar(a, m - 1, j);
			d = new Date(c.getTimeInMillis());
			return d;
		}
		return null;
	}
}
