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

public class CreerInterServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response)
					throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		String titre = "Cr&eacute;er une intervention";
		out.println(Page.header(titre));

		try {
			ResultSet sem = RequeteSQL.executeQ(4, null);
			ResultSet anim = RequeteSQL.executeQ(21, null);

			out.println("<form id='formInter' action='creer-intervention' method='post'>");
			out.println("<br/><label> Intitul&eacute; du seminaire </label>");
			out.println("<select name='sem'>");
			while(sem.next())
				out.println("<option value='" + sem.getInt("idSem") + "'>" 
						+ sem.getString("intituleSem") + "</option>");
			out.println("</select>");
			out.println("<br/><label> Identit&eacute; de l'animateur </label>");
			out.println("<select name='anim'>");
			while(anim.next())
				out.println("<option value='" + anim.getInt("idAnim") + "'>" 
						+ anim.getString("NomAnim") + " "
						+ anim.getString("PrenomAnim")
						+ "</option>");
			out.println("</select>");
			out.println("<br/><label> Ordre de passage </label>");
			out.println("<input type ='text' name='ordre'/> ");
			out.println("<br/><label> Dur&eacute; de l'intervention</label>");
			out.println("<input type='text' name='duree'>");
			out.println("<input type='submit' value='envoyer'>");
			out.println("</form>");

		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		out.println(Page.retourAccueil());
		out.println(Page.footer());

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
									throws IOException, ServletException {
		if(request.getParameter("anim") != null) {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			
			String titre = "Cr&eacute;er une intervention";
			out.println(Page.header(titre));

			Integer tInt[] =  {
					Integer.parseInt(request.getParameter("anim")),
					Integer.parseInt(request.getParameter("sem"))
			};

			try {
				ResultSet res = RequeteSQL.executeQ(22, tInt);
				if (res.next()) {
					out.println("<body style='font-family:Palatino Linotype'>");
					out.println("<h1>ERREUR 399</h1></br>");
					out.println("<p style='font-style:italic'>" +
							"Cet animateur intervient déjà dans ce seminaire.</p>");
					out.println(Page.retourAccueil());
					out.println(Page.footer());
					return;
				}

				Integer[] tInt2 = {
						Integer.parseInt(request.getParameter("anim")),
						Integer.parseInt(request.getParameter("sem")),
						Integer.parseInt(request.getParameter("duree")),
						Integer.parseInt(request.getParameter("ordre"))
				};

				int i = RequeteSQL.executeU(14, tInt2);
				out.println("Ajout effectué.");
				
				out.println(Page.retourAccueil());
				out.println(Page.footer());


			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
