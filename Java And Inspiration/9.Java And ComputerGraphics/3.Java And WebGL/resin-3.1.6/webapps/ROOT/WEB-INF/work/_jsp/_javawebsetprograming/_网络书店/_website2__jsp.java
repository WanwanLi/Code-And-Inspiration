/*
 * JSP generated by Resin-3.1.6 (built Sun, 04 May 2008 03:25:50 PDT)
 */

package _jsp._javawebsetprograming._\u7f51\u7edc\u4e66\u5e97;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;

public class _website2__jsp extends com.caucho.jsp.JavaPage
{
  private static final java.util.HashMap<String,java.lang.reflect.Method> _jsp_functionMap = new java.util.HashMap<String,java.lang.reflect.Method>();
  private boolean _caucho_isDead;
  
  public void
  _jspService(javax.servlet.http.HttpServletRequest request,
              javax.servlet.http.HttpServletResponse response)
    throws java.io.IOException, javax.servlet.ServletException
  {
    javax.servlet.http.HttpSession session = request.getSession(true);
    com.caucho.server.webapp.WebApp _jsp_application = _caucho_getApplication();
    javax.servlet.ServletContext application = _jsp_application;
    com.caucho.jsp.PageContextImpl pageContext = com.caucho.jsp.QJspFactory.allocatePageContext(this, _jsp_application, request, response, null, session, 8192, true, false);
    javax.servlet.jsp.JspWriter out = pageContext.getOut();
    final javax.el.ELContext _jsp_env = pageContext.getELContext();
    javax.servlet.ServletConfig config = getServletConfig();
    javax.servlet.Servlet page = this;
    response.setContentType("text/html;charset=GB2312");
    request.setCharacterEncoding("GB2312");
    try {
      out.write(_jsp_string0, 0, _jsp_string0.length);
       
	          String[] s=request.getParameterValues("CheckBoxes");
	          int number=0;
	          double money=0;
	          try
	          {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection Connection1=DriverManager.getConnection("jdbc:odbc:DataSource3");
		ResultSet ResultSet1;
		float price=0.0f;		
		if(s!=null)
		        for(int i=0;i<s.length;i++)
		        {
			number=Integer.parseInt(request.getParameter("Text"+s[i]));
			ResultSet1=Connection1.createStatement().executeQuery("Select * From Table1 Where ID="+s[i]);
			while(ResultSet1.next())
			{
			        price=ResultSet1.getFloat("Price");
			        out.print("<Tr>");
				out.print("<Td><B>\u4e66\u540d\uff1a</B></Td><Td>"+ResultSet1.getString("Name")+"</Td>");
				out.print("<Td><B>\u4f5c\u8005\uff1a</B></Td><Td>"+ResultSet1.getString("Writer")+"</Td>");
				out.print("<Td><B>\u51fa\u7248\u793e\uff1a</B></Td><Td>"+ResultSet1.getString("Publisher")+"</Td>");
				out.print("<Td><B>\u4ef7\u683c\uff1a</B></Td><Td>"+price+"</Td>");
				out.print("<Td><B>\u6570\u91cf\uff1a</B></Td><Td>"+number+"</Td>");
			        out.print("</Tr>");
			       money+=number*price;	
		             }	
		             ResultSet1.close();	            
		        }		 
		Connection1.close();
		out.print("<Tr><Td ColSpan=10 Align=Right>\u7f51\u7edc\u8d2d\u4e66\u4ef7\u683c\u603b\u8ba1\uff1a"+money+"\uffe5</Td></Tr>");
	          }catch(Exception e){}
	
      out.write(_jsp_string1, 0, _jsp_string1.length);
    } catch (java.lang.Throwable _jsp_e) {
      pageContext.handlePageException(_jsp_e);
    } finally {
      com.caucho.jsp.QJspFactory.freePageContext(pageContext);
    }
  }

  private java.util.ArrayList _caucho_depends = new java.util.ArrayList();

  public java.util.ArrayList _caucho_getDependList()
  {
    return _caucho_depends;
  }

  public void _caucho_addDepend(com.caucho.vfs.PersistentDependency depend)
  {
    super._caucho_addDepend(depend);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }

  public boolean _caucho_isModified()
  {
    if (_caucho_isDead)
      return true;
    if (com.caucho.server.util.CauchoSystem.getVersionId() != -7072480922035483583L)
      return true;
    for (int i = _caucho_depends.size() - 1; i >= 0; i--) {
      com.caucho.vfs.Dependency depend;
      depend = (com.caucho.vfs.Dependency) _caucho_depends.get(i);
      if (depend.isModified())
        return true;
    }
    return false;
  }

  public long _caucho_lastModified()
  {
    return 0;
  }

  public java.util.HashMap<String,java.lang.reflect.Method> _caucho_getFunctionMap()
  {
    return _jsp_functionMap;
  }

  public void init(ServletConfig config)
    throws ServletException
  {
    com.caucho.server.webapp.WebApp webApp
      = (com.caucho.server.webapp.WebApp) config.getServletContext();
    super.init(config);
    com.caucho.jsp.TaglibManager manager = webApp.getJspApplicationContext().getTaglibManager();
    com.caucho.jsp.PageContextImpl pageContext = new com.caucho.jsp.PageContextImpl(webApp, this);
  }

  public void destroy()
  {
      _caucho_isDead = true;
      super.destroy();
  }

  public void init(com.caucho.vfs.Path appDir)
    throws javax.servlet.ServletException
  {
    com.caucho.vfs.Path resinHome = com.caucho.server.util.CauchoSystem.getResinHome();
    com.caucho.vfs.MergePath mergePath = new com.caucho.vfs.MergePath();
    mergePath.addMergePath(appDir);
    mergePath.addMergePath(resinHome);
    com.caucho.loader.DynamicClassLoader loader;
    loader = (com.caucho.loader.DynamicClassLoader) getClass().getClassLoader();
    String resourcePath = loader.getResourcePathSpecificFirst();
    mergePath.addClassPath(resourcePath);
    com.caucho.vfs.Depend depend;
    depend = new com.caucho.vfs.Depend(appDir.lookup("JavaWebSetPrograming/\u7f51\u7edc\u4e66\u5e97/WebSite2.jsp"), 445806486588053962L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }

  private final static char []_jsp_string0;
  private final static char []_jsp_string1;
  static {
    _jsp_string0 = "\r\n\r\n\r\n\r\n<Html>\r\n<Head><Title></Title></Head>\r\n<Body Text=White BackGround=13.jpg>\r\n	<Br><Br>\r\n	<Center><Font Face=\u5fae\u8f6f\u96c5\u9ed1 Size=30 ><B>\u8ba1\u7b97\u673a\u7c7b\u4e1b\u4e66\u7f51\u7edc\u4e66\u5e97\u7f51\u7ad9\u8d2d\u4e70\u786e\u8ba4\u4e2d\u5fc3<Br></B></Font></Center>\r\n	<Center><B>________________________________________________________________________________________________________</B></Center>\r\n	<Br><Br><Br><Br><Marquee Behavior=Alternate ScrollAmount=20><B>\u7f51\u7ad9\u4f5c\u8005\uff1a\u674e\u4e07\u4e07</B></Marquee>\r\n	<Center><B>\u60a8\u6240\u9009\u8d2d\u7684\u56fe\u4e66\u5206\u522b\u662f:</B></Center>\r\n	<Center>\r\n	<Br><Br>\r\n	<Form Action=WebSite1.jsp Method=post>\r\n	<Table Width=1200 Border=1 BorderColorDark=Black BorderColorLight=White>\r\n	".toCharArray();
    _jsp_string1 = "\r\n	\r\n	</Table>\r\n	</Form>\r\n	</Center>\r\n		\r\n</Body>\r\n</Html>\r\n\r\n\r\n\r\n\r\n\r\n\r\n".toCharArray();
  }
}
