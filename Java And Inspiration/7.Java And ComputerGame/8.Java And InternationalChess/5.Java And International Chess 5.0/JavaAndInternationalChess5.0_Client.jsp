<%@ page  contentType="text/html;charset=GB2312" %>
<Html>
	<Title>International  Chess 5.0: Made By LiWanwan</Title>
	<Body>
		<%!
			void count()
			{
				ServletContext  application=getServletContext(); 
				String C=(String)application.getAttribute("Count"); 
				if(C==null)C="0";
				int c=Integer.parseInt(C);
				application.setAttribute("Count",String.valueOf(c+1)); 
			}
		%>
		<%
			if(session.isNew())
			{
				count();
				session.setAttribute("Count",application.getAttribute("Count"));
			}
			int i=Integer.parseInt((String)session.getAttribute("Count"));
		%>
		<Applet Code=InternationalChess<%=(i%2)%>.class Width=1364 Height=640/>
	</Body>
</Html>
