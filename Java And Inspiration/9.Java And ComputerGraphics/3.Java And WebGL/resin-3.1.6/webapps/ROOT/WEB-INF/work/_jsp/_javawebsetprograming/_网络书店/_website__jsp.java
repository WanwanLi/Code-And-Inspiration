/*
 * JSP generated by Resin-3.1.6 (built Sun, 04 May 2008 03:25:50 PDT)
 */

package _jsp._javawebsetprograming._\u7f51\u7edc\u4e66\u5e97;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;

public class _website__jsp extends com.caucho.jsp.JavaPage
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
      out.print((request.getParameterValues("Submits")[0]));
      out.write(_jsp_string1, 0, _jsp_string1.length);
       String[] s=request.getParameterValues("Submits");
      out.write(_jsp_string2, 0, _jsp_string2.length);
      out.print(( s[0]));
      out.write(_jsp_string3, 0, _jsp_string3.length);
      
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection Connection1=DriverManager.getConnection("jdbc:odbc:DataSource3");
		ResultSet ResultSet1=Connection1.createStatement().executeQuery("Select * From Table1 Where Kind='"+s[0]+"'");
		while(ResultSet1.next())
		{
	
      out.write(_jsp_string4, 0, _jsp_string4.length);
      out.print((ResultSet1.getString("Link")));
      out.write('>');
      out.print((ResultSet1.getString("Name")));
      out.write(_jsp_string5, 0, _jsp_string5.length);
      out.print((ResultSet1.getString("Writer")));
      out.write(_jsp_string6, 0, _jsp_string6.length);
      out.print((ResultSet1.getString("Publisher")));
      out.write(_jsp_string7, 0, _jsp_string7.length);
      out.print((ResultSet1.getFloat("Price")));
      out.write(_jsp_string8, 0, _jsp_string8.length);
      out.print(( ResultSet1.getInt("ID")));
      out.write(_jsp_string9, 0, _jsp_string9.length);
      
		}
		ResultSet1.close();
		Connection1.close();		
	
      out.write(_jsp_string10, 0, _jsp_string10.length);
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
    depend = new com.caucho.vfs.Depend(appDir.lookup("JavaWebSetPrograming/\u7f51\u7edc\u4e66\u5e97/WebSite.jsp"), -5391484569557907264L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }

  private final static char []_jsp_string5;
  private final static char []_jsp_string2;
  private final static char []_jsp_string8;
  private final static char []_jsp_string1;
  private final static char []_jsp_string0;
  private final static char []_jsp_string9;
  private final static char []_jsp_string3;
  private final static char []_jsp_string10;
  private final static char []_jsp_string7;
  private final static char []_jsp_string6;
  private final static char []_jsp_string4;
  static {
    _jsp_string5 = "</Td>\r\n		             	             <Td><B>\u4f5c\u8005\uff1a</B></Td><Td>".toCharArray();
    _jsp_string2 = "\r\n	<Center><B>\u60a8\u6240\u9009\u62e9\u7684\u56fe\u4e66\u7c7b\u522b\u662f:".toCharArray();
    _jsp_string8 = "</Td>\r\n			             <Td>\u8d2d\u4e70:<Input Type=CheckBox Name=CheckBoxes Value=".toCharArray();
    _jsp_string1 = ".jpg VLink=White ALink=Black>\r\n	<Br><Br>\r\n	<Center><Font Face=\u5fae\u8f6f\u96c5\u9ed1 Size=30 ><B>\u8ba1\u7b97\u673a\u7c7b\u4e1b\u4e66\u7f51\u7edc\u4e66\u5e97\u7f51\u7ad9\u4e2d\u5fc3<Br></B></Font></Center>\r\n	<Center><B>______________________________________________________________________________________</B></Center>\r\n	<Br><Br><Br><Br><Marquee Behavior=Alternate ScrollAmount=20><B>\u7f51\u7ad9\u4f5c\u8005\uff1a\u674e\u4e07\u4e07</B></Marquee>\r\n	<Form Action=WebSite.jsp Method=post>\r\n		<Center>\r\n			<Table Width=1200 Border=1 BorderColorLight=White BorderColorDark=Black >\r\n				<Tr>\r\n					<Td Align=Center><Img Src=7.jpg Width=200 Height=150></Td>\r\n					<Td Align=Center><Img Src=8.jpg Width=200 Height=150></Td>\r\n					<Td Align=Center><Img Src=9.jpg Width=200 Height=150></Td>\r\n					<Td Align=Center><Img Src=10.jpg Width=200 Height=150></Td>\r\n				</Tr>\r\n				<Tr>\r\n					<Td Align=Center><Input Type=Submit Value=Java\u8bed\u8a00\u7c7b Name=Submits></Td>\r\n					<Td Align=Center><Input Type=Submit Value=\u6570\u636e\u7ed3\u6784\u7c7b Name=Submits></Td>\r\n					<Td Align=Center><Input Type=Submit Value=\u8ba1\u7b97\u673a\u786c\u4ef6\u7c7b Name=Submits></Td>\r\n					<Td Align=Center><Input Type=Submit Value=\u64cd\u4f5c\u7cfb\u7edf\u7c7b Name=Submits></Td>\r\n				</Tr>\r\n		</Center>\r\n	</Form>\r\n	".toCharArray();
    _jsp_string0 = "\r\n\r\n\r\n\r\n<Html>\r\n<Head><Title></Title></Head>\r\n<Body Text=White BackGround=".toCharArray();
    _jsp_string9 = " ></Td>\r\n			</Tr>\r\n	".toCharArray();
    _jsp_string3 = "</B></Center>\r\n\r\n	<Center>\r\n	<Br><Br>\r\n	<Form Action=WebSite1.jsp Method=post>\r\n	       <Table Width=1200 Border=1 BorderColorDark=Black BorderColorLight=White >	\r\n	".toCharArray();
    _jsp_string10 = "\r\n	</Table>\r\n		<Br><Input Type=Submit Name=Submit1 Value=\u786e\u5b9a\u9009\u8d2d><Input Type=Reset Name=Reset1 Value=\u91cd\u65b0\u9009\u8d2d>\r\n	</Center>\r\n		\r\n</Body>\r\n</Html>\r\n\r\n\r\n\r\n\r\n\r\n\r\n".toCharArray();
    _jsp_string7 = "</Td>\r\n			             <Td><B>\u4ef7\u683c\uff1a</B></Td><Td>".toCharArray();
    _jsp_string6 = "</Td>\r\n			             <Td><B>\u51fa\u7248\u793e\uff1a</B></Td><Td>".toCharArray();
    _jsp_string4 = "\r\n			<Tr>\r\n		              	             <Td><B>\u4e66\u540d\uff1a</B></Td>\r\n			             <Td><A Href=".toCharArray();
  }
}
