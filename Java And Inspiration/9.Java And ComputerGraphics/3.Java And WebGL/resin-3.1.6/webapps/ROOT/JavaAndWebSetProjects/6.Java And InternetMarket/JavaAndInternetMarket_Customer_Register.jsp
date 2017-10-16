<%@ page contentType="text/html;charset=GB2312" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<Html>
	<Head>
		<Title>JavaAndInternetMarket_Customer_Register</Title>
	</Head>
	<Script Language=JavaScript>
		function function_Submit()
		{
			if(Form1.Text_Name.value=="")alert("Name Should not be Empty!");
			else if(Form1.Password_Password.value==""||Form1.Password_PasswordAgain.value=="")alert("Password Should not be Empty!");
			else if(Form1.Password_Password.value!=Form1.Password_PasswordAgain.value){alert("Password is not the same,please identify it again!");Form1.Password_Password.value="";Form1.Password_PasswordAgain.value="";}
			else if(Form1.Text_Email.value=="")alert("Email Should not be Empty!");
			else if(Form1.Text_Email.value.indexOf('@')==-1){alert("The Form Of Email might not be right,please input it again!");Form1.Text_Email.value="";}
			else if(Form1.Text_Telephone.value=="")alert("Telephone Should not be Empty!");
			else if(Form1.Text_Address.value=="")alert("Address Should not be Empty!");
			else Form1.submit();
		}
	</Script>
	<jsp:useBean id="PasswordProcessor1" class="JavaAndInternetMarket.PasswordProcessor" scope="page"/>
	<%
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection Connection1=DriverManager.getConnection("jdbc:mysql://localhost:3306/InternetMarket","root","11235813");
			if(request.getParameter("Text_Name")!=null)
			{
				String StructuredQueryLanguage="Insert Into Customer(Name,Password,Email,Telephone,Address) Values('"+request.getParameter("Text_Name")+"','"+PasswordProcessor1.getCodeByPassword(request.getParameter("Password_Password"))+"','"+request.getParameter("Text_Email")+"','"+request.getParameter("Text_Telephone")+"','"+request.getParameter("Text_Address")+"')";
				Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
				Connection1.close();
				response.sendRedirect("JavaAndInternetMarket_Customer.jsp");
			}
			Connection1.close();
		}
		catch(Exception e){out.print(e.toString());}
	%>
	<Body Text=597C17 VLink=White ALink=Blue>
		<Form Name=Form1 Action=JavaAndInternetMarket_Customer_Register.jsp Method=post>
			<Table Align=Center Width=1000 Height=300 BackGround="Internet Market\InternetMarket.jpg">
				<Image Src="Internet Market\InternetMarket_Head.jpg"><Br>
				<Tr><Marquee  Behavior=Alternate ScrollAmount=10 BackGround="Internet Market\InternetMarket.jpg" BGColor=#FFD457><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Welcome to Register On the Internet Market !<B></Font></Marquee></Tr><Br>
				<Tr><Td Align=Center>
					<Table Align=Center Width=500  Height=350 BackGround="Internet Market\InternetMarket_Input.jpg" Border=1 BorderColor=597C17>
						<Tr><Td Align=Center ColSpan=2><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Input Your Informations:</B></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Name:</B></Td><Td Align=Center><Input Type=Text Name=Text_Name></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Password:</B></Td><Td Align=Center><Input Type=Password Name=Password_Password></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Password Again:</B></Td><Td Align=Center><Input Type=Password Name=Password_PasswordAgain></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Email:</B></Td><Td Align=Center><Input Type=Text Name=Text_Email></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Telephone:</B></Td><Td Align=Center><Input Type=Text Name=Text_Telephone></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Address:</B></Td><Td  Align=Center><Input Type=Text Name=Text_Address></Td></Tr>
					</Table>
				</Td></Tr>
				<Tr><Td Align=Center>
					<Table Align=Center Width=500  Height=50 BackGround="Internet Market\InternetMarket_Input.jpg" Border=1 BorderColor=597C17>
						<Tr>
							<Td  Align=Center ColSpan=2><Input Type=Button Name=Button_Submit Value="Submit The Informations" onclick=function_Submit()></Td>
							<Td  Align=Center ColSpan=2><Input Type=Reset  Value="Reset The Informations"></Td>
						</Tr>
					</Table>
				</Td></Tr>
			</Table>		
		</Form>
	</Body>
</Html>