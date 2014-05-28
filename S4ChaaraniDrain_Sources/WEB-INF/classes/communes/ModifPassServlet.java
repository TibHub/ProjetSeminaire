package communes;

import outilsBD.RequeteSQL;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import data.User;

public class ModifPassServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {

		/**
		 * On r�cup�re le PrintWriter de la r�ponse
		 */
		PrintWriter out = response.getWriter();

		/**
		 * On positionne le type du contenu de la r�ponse
		 */
		response.setContentType("text/html");

		/**
		 * On ouvre la session
		 */
		HttpSession laSession = request.getSession(true);

		/**
		 * On r�cup�re l'utilisateur qui est authentifi�, dans la session
		 */
		User logg� = (User) laSession.getAttribute("user");

		/**
		 * On r�cup�re les param�tres de la requ�te
		 */
		String prev = request.getParameter("previousPass");
		String new1 = request.getParameter("newPass1");
		String new2 = request.getParameter("newPass2");

		/**
		 * On proc�de aux v�rifications n�cessaires avant d'agir sur la BD
		 */

		// le previousPass doit correspondre au mot de passe stock�
		String passSession = logg�.getPassword();
		out.println(passSession);

		if (!prev.equals(passSession)) {
			// pour l'instant // r�afficher la page avec l'erreur
			out.println("<h1> Le mot de passe saisie n'est pas correct </h1>");
			
		} 
		else { // ancien mot de passe correct
			if (!new1.equals(new2)) {
				out.println("<h1> Veuillez saisir votre nouveau mot de passe. </h1>");
			} 
			else { // new1 et new2 identiques
						// on effectue la MAJ du mot de passe

				String[] args = new String[2];
				args[0] = new1;
				args[1] = logg�.getLogin();

				try {
					int ok = RequeteSQL.executeU(1, args);

					if (ok > 0) {
						// confirmer modification du mot de passe
						out.println("<h1>Votre mot de passe a bien �t� modifi� !</h1>");
						/*out.println("<form id='retourAccueil' action='authent' method='post'>");
							out.println("<input type='submit' value='Revenir � l'accueil'/>");
						out.println("</form>");*/
						
						out.println(Page.retourAccueil());
						out.println(Page.footer());


					} 
					else {
						// infirmer modification du mot de passe <=> erreur BD !!
						out.println("erreur lors du traitement.");
						out.println(Page.retourAccueil());
						out.println(Page.footer());
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		}

	}

}
