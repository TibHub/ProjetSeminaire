package animateur;

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

public class SupprIntervServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Integer[] args = { Integer.parseInt(request.getParameter("ida")), Integer.parseInt(request.getParameter("ids")) };
		
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		
		if(user == null || user.getId() != User.ANIM) {
			PrintWriter out = response.getWriter();
			out.println("<h1>Erreur :</h1>");
			out.println("<p>Vous ne pouvez acc�der � ce service sans �tre connect�" +
						" en tant qu'animateur.</p>");
			out.println("<a href='index.html'> Aller � l'accueil </a>");
			out.println(Page.retourAccueil());
			out.println(Page.footer());
			return;
		}
		
		int effac� = -1;
		try {
			effac� = RequeteSQL.executeU(10, args);
			response.sendRedirect("visualiser-mes-seminaires?idTheme=0");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
