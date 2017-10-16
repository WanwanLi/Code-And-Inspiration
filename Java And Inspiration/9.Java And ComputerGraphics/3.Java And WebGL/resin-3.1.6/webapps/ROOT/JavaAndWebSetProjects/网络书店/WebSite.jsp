<%@ page  contentType="text/html;charset=GB2312" %>
<%@ page  import="java.util.*"%>
<%@ page  import="java.sql.*"%>

<Html>
<Head><Title></Title></Head>
<Body Text=White BackGround=<%=request.getParameterValues("Submits")[0]%>.jpg VLink=White ALink=Black>
	<Br><Br>
	<Center><Font Face=微软雅黑 Size=30 ><B>计算机类丛书网络书店网站中心<Br></B></Font></Center>
	<Center><B>______________________________________________________________________________________</B></Center>
	<Br><Br><Br><Br><Marquee Behavior=Alternate ScrollAmount=20><B>网站作者：李万万</B></Marquee>
	<Form Action=WebSite.jsp Method=post>
		<Center>
			<Table Width=1200 Border=1 BorderColorLight=White BorderColorDark=Black >
				<Tr>
					<Td Align=Center><Img Src=7.jpg Width=200 Height=150></Td>
					<Td Align=Center><Img Src=8.jpg Width=200 Height=150></Td>
					<Td Align=Center><Img Src=9.jpg Width=200 Height=150></Td>
					<Td Align=Center><Img Src=10.jpg Width=200 Height=150></Td>
				</Tr>
				<Tr>
					<Td Align=Center><Input Type=Submit Value=Java语言类 Name=Submits></Td>
					<Td Align=Center><Input Type=Submit Value=数据结构类 Name=Submits></Td>
					<Td Align=Center><Input Type=Submit Value=计算机硬件类 Name=Submits></Td>
					<Td Align=Center><Input Type=Submit Value=操作系统类 Name=Submits></Td>
				</Tr>
		</Center>
	</Form>
	<% String[] s=request.getParameterValues("Submits");%>
	<Center><B>您所选择的图书类别是:<%= s[0]%></B></Center>

	<Center>
	<Br><Br>
	<Form Action=WebSite1.jsp Method=post>
	       <Table Width=1200 Border=1 BorderColorDark=Black BorderColorLight=White >	
	<%
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection Connection1=DriverManager.getConnection("jdbc:odbc:DataSource3");
		ResultSet ResultSet1=Connection1.createStatement().executeQuery("Select * From Table1 Where Kind='"+s[0]+"'");
		while(ResultSet1.next())
		{
	%>
			<Tr>
		              	             <Td><B>书名：</B></Td>
			             <Td><A Href=<%=ResultSet1.getString("Link")%>><%=ResultSet1.getString("Name")%></Td>
		             	             <Td><B>作者：</B></Td><Td><%=ResultSet1.getString("Writer")%></Td>
			             <Td><B>出版社：</B></Td><Td><%=ResultSet1.getString("Publisher")%></Td>
			             <Td><B>价格：</B></Td><Td><%=ResultSet1.getFloat("Price")%></Td>
			             <Td>购买:<Input Type=CheckBox Name=CheckBoxes Value=<%= ResultSet1.getInt("ID")%> ></Td>
			</Tr>
	<%
		}
		ResultSet1.close();
		Connection1.close();		
	%>
	</Table>
		<Br><Input Type=Submit Name=Submit1 Value=确定选购><Input Type=Reset Name=Reset1 Value=重新选购>
	</Center>
		
</Body>
</Html>






