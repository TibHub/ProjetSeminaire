package communes;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;


public class Deconnexion extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session =  request.getSession(true);
		session.removeAttribute("user");
		response.sendRedirect("index.html");
	}
	
}
