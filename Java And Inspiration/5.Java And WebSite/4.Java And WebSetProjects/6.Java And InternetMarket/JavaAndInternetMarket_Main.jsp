<%@ page contentType="text/html;charset=GB2312" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<Html>
	<Head>
		<Title>JavaAndInternetMarket</Title>
	</Head>
	<Script Language=JavaScript>
		function function_Insert()
		{
			if(Form1.Text_Name.value=="")alert("Name Should not be Empty!");
			else if(Form1.Text_Classification.value=="")alert("Classification Should not be Empty!");
			else if(Form1.Text_Description.value=="")alert("Description Should not be Empty!");
			else if(Form1.Text_Price.value=="")alert("Price Should not be Empty!");
			else if(Form1.Text_Stocks.value=="")alert("Stocks Should not be Empty!");
			else {Form1.Hidden_Parameter.value="Insert";Form1.submit();}
		}
		function function_Delete()
		{
			if(Form1.Text_Name.value=="")alert("Name Should not be Empty!");
			else {Form1.Hidden_Parameter.value="Delete";Form1.submit();}
		}
		function function_Select()
		{
			if(Form1.Text_Name.value=="")alert("Name Should not be Empty!");
			else {Form1.Hidden_Parameter.value="Select";Form1.submit();}
		}
		function function_Update()
		{
			if(Form1.Text_Name.value=="")alert("Name Should not be Empty!");
			else if(Form1.Text_Classification.value==""&&Form1.Text_Description.value==""&&Form1.Text_Price.value==""&&Form1.Text_Stocks.value=="")alert("At Least One Item Should be Updated!");
			else {Form1.Hidden_Parameter.value="Update";Form1.submit();}
		}
		function function_Submit_Image()
		{
			if(Form1.File_Image.value=="")alert("Image File Name Should not be Empty!");
			else {Form1.Hidden_Parameter.value="Submit_Image";Form1.submit();}
		}
	</Script>
	<Body Text=597C17 VLink=White ALink=Blue>
		<Form Name=Form1 Action=JavaAndInternetMarket_Customer_LogOn.jsp Method=post>
			<Table Align=Center Width=1000 Height=300 BackGround="Internet Market\InternetMarket.jpg">
				<Image Src="Internet Market\InternetMarket_Head.jpg"><Br>
				<Tr><Marquee  Behavior=Alternate ScrollAmount=10 BackGround="Internet Market\InternetMarket.jpg" BGColor=#FFD457><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Welcome to Internet Market !Have A Good Time!<B></Font></Marquee></Tr><Br>
				<Tr><Td Align=Center>
					<Table Align=Center Width=800  Height=50 Border=1 BorderColor=597C17>
						<Tr><Td  Align=Center ColSpan=4><Font Color= 597C17 Size=10 Face=Î¢ÈíÑÅºÚ ><B>Merchandise Show :</B></Td></Td></Tr>
						<%
							Class.forName("com.mysql.jdbc.Driver");
							Connection Connection1=DriverManager.getConnection("jdbc:mysql://localhost:3306/InternetMarket","root","11235813");
							String StructuredQueryLanguage="SELECT Name,Classification,Description,Price,MerchandiseID FROM Merchandise Order By Stocks DESC";
							ResultSet ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
							boolean Break=false;
							for(int i=0;i<3;i++)
							{
						%>
								<Tr>
						<%
								for(int j=0;j<4;j++)
								{
									if(!ResultSet1.next()){Break=true;break;}
						%>
									<Td Align=Center>
										<Table Align=Center Width=200 Border=1>
											<Tr><Td Align=Center><Image Src="Merchandise Images\<%=ResultSet1.getString("Name")%>.jpg"></Td></Tr>
											<Tr><Td Align=Center><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>Name:  <%=ResultSet1.getString("Name")%></B></Td></Tr>
											<Tr><Td Align=Center><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>Price:  <%=ResultSet1.getFloat("Price")%></B></Td></Tr>
											<Tr><Td Align=Center><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>I Want to Buy it!</B><Input Type=CheckBox Name=CheckBoxes Value=<%= ResultSet1.getInt("MerchandiseID")%>></Td></Tr>
										</Table>
									</Td>
						<%
								}
						%>
								</Tr>
						<%
								if(Break)break;
							}
							ResultSet1.close();
							Connection1.close();
						%>
						<Tr>
							<Td  Align=Center ColSpan=4><Font Color= 597C17 Size=10 Face=Î¢ÈíÑÅºÚ ><B>I Want to Buy Them!</B><Input Type=Submit Name=Submit_LogOn Value=Yes!></Td></Td>
						</Tr>
					</Table>
				</Td></Tr>
			</Table>		
		</Form>
	</Body>
</Html>