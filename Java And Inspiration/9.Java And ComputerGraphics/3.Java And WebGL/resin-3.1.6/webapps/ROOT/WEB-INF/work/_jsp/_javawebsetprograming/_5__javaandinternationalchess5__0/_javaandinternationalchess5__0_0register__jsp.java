/*
 * JSP generated by Resin-3.1.6 (built Sun, 04 May 2008 03:25:50 PDT)
 */

package _jsp._javawebsetprograming._5__javaandinternationalchess5__0;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;

public class _javaandinternationalchess5__0_0register__jsp extends com.caucho.jsp.JavaPage
{
  private static final java.util.HashMap<String,java.lang.reflect.Method> _jsp_functionMap = new java.util.HashMap<String,java.lang.reflect.Method>();
  private boolean _caucho_isDead;
  
  public void
  _jspService(javax.servlet.http.HttpServletRequest request,
              javax.servlet.http.HttpServletResponse response)
    throws java.io.IOException, javax.servlet.ServletException
  {
    com.caucho.server.webapp.WebApp _jsp_application = _caucho_getApplication();
    javax.servlet.ServletContext application = _jsp_application;
    com.caucho.jsp.PageContextImpl pageContext = com.caucho.jsp.QJspFactory.allocatePageContext(this, _jsp_application, request, response, null, null, 8192, true, false);
    javax.servlet.jsp.JspWriter out = pageContext.getOut();
    final javax.el.ELContext _jsp_env = pageContext.getELContext();
    javax.servlet.ServletConfig config = getServletConfig();
    javax.servlet.Servlet page = this;
    response.setContentType("text/html;charset=GB2312");
    request.setCharacterEncoding("GB2312");
    try {
      out.write(_jsp_string0, 0, _jsp_string0.length);
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
    depend = new com.caucho.vfs.Depend(appDir.lookup("JavaWebSetPrograming/5.JavaAndInternationalChess5.0/JavaAndInternationalChess5.0_Register.jsp"), -8216478193306906134L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }

  private final static char []_jsp_string0;
  static {
    _jsp_string0 = "\r\n<Html>\r\n	<Title>International  Chess 5.0: Made By LiWanwan</Title>\r\n       	<Body Text=White BackGround=WebSite/1.jpg><Br><Br><Br>\r\n	<Center><Font Face=\u5fae\u8f6f\u96c5\u9ed1 Size=30 ><H1><B>International Chess 5.0 Version\u2015\u2015\u2015Harbin Technology Institution<Br></B></H1></Font></Center>\r\n	<Center><B>______________________________________________________________________________________</B></Font></Center>\r\n	<Br><Marquee Behavior=Alternate ScrollAmount=20><B>Producer:LiWanwan</B></Marquee><Br>\r\n		<Form>\r\n			<Table Align=Right Width=400 Border=1>\r\n				<Tr><Td>Input Your Account Number:</Td><Td><Input Name=Password1></Td></Tr>\r\n				<Tr><Td>Input Your Name:</Td><Td><Input Name=Text1></Td></Tr>\r\n				<Tr><Td>Input Your Password:</Td><Td><Input Type=Password Name=Password1></Td></Tr>\r\n				<Tr><Td>Input Your BirthDay:</Td><Td><Input Name=Text_BirthDay></Td></Tr>\r\n				<Tr><Td>Input Your Hobby:</Td><Td><Input Name=Text_Hobby></Td></Tr>\r\n				<Tr><Td>Input Your Favorate Color:</Td><Td><Input Name=Text_Color></Td></Tr>\r\n				<Tr><Td>Input Your On Line Partner:</Td><Td><Input Name=Text_Partner></Td></Tr>\r\n				<Tr><Td>Input Your E-mail:</Td><Td><Input Name=Text_Email></Td></Tr>\r\n				<Tr><Td ColSpan=2>Choose Your International Ability:</Td></Tr>\r\n				<Tr><Td ColSpan=2 Align=Center><Input Type=CheckBox Name=CheckBoxes Value=Level1>International Chess Ability :Level1</Td></Tr>\r\n				<Tr><Td ColSpan=2 Align=Center><Input Type=CheckBox Name=CheckBoxes Value=Level2>International Chess Ability :Level2</Td></Tr>\r\n				<Tr><Td ColSpan=2 Align=Center><Input Type=CheckBox Name=CheckBoxes Value=Level3>International Chess Ability :Level3</Td></Tr>\r\n				<Tr><Td><Input Type=Submit Name=Submit1 Value=\"Submit All Information\"></Td><Td><Input Type=Reset Name=Reset1 Value=\"Reset Information\"></Td></Tr>\r\n			</Table>\r\n		</Form>\r\n	</Body>\r\n</Html>".toCharArray();
  }
}
