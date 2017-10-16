package JavaAndServlet;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class JavaAndServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		out.println("<Html><Head><Tiltle>JavaAndServlet</Tiltle></Head><Body BGColor=Black><Marquee><H1>Java And Servlet</H1></Marquee><Br><Br>@http://localhost/servlet/JavaAndServlet</Body></Html>");
	}
}