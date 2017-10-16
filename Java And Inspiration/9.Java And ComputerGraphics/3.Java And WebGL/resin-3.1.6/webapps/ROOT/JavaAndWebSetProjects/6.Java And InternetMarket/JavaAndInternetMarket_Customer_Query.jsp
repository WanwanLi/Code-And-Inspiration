<%@ page contentType="text/html;charset=GB2312" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<Html>
	<Head>
		<Title>JavaAndInternetMarket_Customer_Query</Title>
	</Head>
	<Body Text=597C17 VLink=White ALink=Blue>
		<Table Align=Center Width=1000 Height=300 BackGround="Internet Market\InternetMarket.jpg">
			<Image Src="Internet Market\InternetMarket_Head.jpg"><Br>
			<Tr><Marquee  Behavior=Alternate ScrollAmount=10 BackGround="Internet Market\InternetMarket.jpg" BGColor=#FFD457><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Welcome to the Internet Market! Have a Good Time!<B></Font></Marquee></Tr><Br>
			<Tr><Td Align=Center>
				<Form Name=Form2 Action=JavaAndInternetMarket_Customer_Query.jsp Method=post>
					<Table Align=Center Width=900  Height=50 Border=1 BorderColor=597C17>
						<Tr>
							<Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Merchandise Name:</B></Td>
							<Td Align=Center><Input Type=Text Name=Text_Name  Size=70></Td>
							<Td Align=Center><Input Type=Submit Value=Query Name=Submit_Name><Input Type=Reset Value=Reset></Td>
						</Tr>
					</Table>
				</Form>
			</Td></Tr>
			<Tr><Td Align=Center>
				<Form Name=Form1 Action=JavaAndInternetMarket_OrderForm.jsp Method=post>
					<Table Align=Center Width=800  Height=50 Border=1 BorderColor=597C17>
						<Tr><Td  Align=Center ColSpan=4><Font Color= 597C17 Size=10 Face=Î¢ÈíÑÅºÚ ><B>Merchandise Informations :</B></Td></Tr>
						<%
							try
							{
								if(session.getAttribute("CustomerID")==null)response.sendRedirect("JavaAndInternetMarket_Customer_LogOn.jsp");
								Class.forName("com.mysql.jdbc.Driver");
								Connection Connection1=DriverManager.getConnection("jdbc:mysql://localhost:3306/InternetMarket","root","11235813");
								String Name=request.getParameter("Text_Name");
								String StructuredQueryLanguage="Select  * From Merchandise Where Name='"+Name+"' Or Name Like '%"+Name+"%' Or Name Like '"+Name+"%' Or Name Like '"+Name+"%' Or Name Like '%"+Name+"%' And Stocks>0";
								ResultSet ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
								while(ResultSet1.next())
								{
						%>
									<Tr>
										<Td Align=Center><Image Height=140 Width=140 src="Merchandise Images\<%=ResultSet1.getString("Name")%>.jpg"></Td>
										<Td>
											<Table Width=750 Align=Center Border=1 BorderColor=597C17>
												<Tr><Td><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>MerchandiseID:<%=ResultSet1.getString("MerchandiseID")%></B></Td></Tr>
												<Tr><Td><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>Name:<%=ResultSet1.getString("Name")%></B></Td></Tr>
												<Tr><Td><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>Classification:<%=ResultSet1.getString("Classification")%></B></Td></Tr>
												<Tr><Td><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>Price:<%=ResultSet1.getFloat("Price")%></B></Td></Tr>
												<Tr><Td><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>I Want to Buy this:<Input Type=CheckBox Name=CheckBoxes Value=<%= ResultSet1.getInt("MerchandiseID")%>></B></Td></Tr>
												<Tr><Td><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>How Many Would You Buy?<Input Type=Text Name=Text<%= ResultSet1.getInt("MerchandiseID")%> Value="1"></B></Td></Tr>
											</Table>
										</Td>
									</Tr>
						<%
								}
								Connection1.close();
							}
							catch(Exception e){out.print(e.toString());}
						%>
						<Tr><Td Align=Center><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>Images</B></Td><Td  Align=Center><Input Type=Submit Value="I Want To Add All Of Them To My OrderForm !"><Input Type=Reset Value="I Want To Reset All Of The Records !"></Td></Tr>
					</Table>
				</Form>
			</Td></Tr>
		</Table>		
	</Body>
</Html>




