package administrateur;

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

public class SupprSemServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		
		if(user == null) {
			out.println("<h1>Erreur :</h1>");
			out.println("<p>Vous ne pouvez accéder à ce service sans être connecté" +
						" en tant qu'administrateur.</p>");
			out.println("<a href='index.html'> Aller à l'accueil </a>");
			out.println(Page.retourAccueil());
			out.println(Page.footer());
			return;
		}
		
		if(user.getId() != User.ADMIN) {
			out.println("<h1>Erreur !</h1>");
			out.println("<p>Vous n'etes pas administrateur.</p>");
			out.println(Page.retourAccueil());
			out.println(Page.footer());
			return;
		}
		
		try {
			int res = RequeteSQL.executeU(12, new Integer(request.getParameter("ids")));
			response.sendRedirect("gerer-les-seminaires?idTheme=0");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}