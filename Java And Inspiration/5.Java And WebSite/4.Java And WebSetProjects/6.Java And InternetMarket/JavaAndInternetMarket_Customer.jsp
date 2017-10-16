<%@ page contentType="text/html;charset=GB2312" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<Html>
	<Head>
		<Title>JavaAndInternetMarket_Customer</Title>
	</Head>
	<%
		if(request.getParameter("Text1")==null)response.sendRedirect("JavaAndInternetMarket_Customer_LogOn.jsp");
	%>
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
							<Td Align=Center><Input Type=Submit Value=Query Name=Submit_Name onClick=fuction1()><Input Type=Reset Value=Reset></Td>
						</Tr>
					</Table>
				</Form>
			</Td></Tr>
			<Tr><Td Align=Center>
				<Form Name=Form1 Action=JavaAndInternetMarket_OrderForm.jsp Method=post>
					<Table Align=Center Width=800  Height=50 Border=1 BorderColor=597C17>
						<Tr><Td  Align=Center ColSpan=4><Font Color= 597C17 Size=10 Face=Î¢ÈíÑÅºÚ ><B>Merchandise Informations :</B></Td></Tr>
						<jsp:useBean id="PasswordProcessor1" class="JavaAndInternetMarket.PasswordProcessor" scope="page"/>
						<%
							try
							{
								Class.forName("com.mysql.jdbc.Driver");
								Connection Connection1=DriverManager.getConnection("jdbc:mysql://localhost:3306/InternetMarket","root","11235813");
								String StructuredQueryLanguage="Select  CustomerID From Customer Where Name='"+request.getParameter("Text1")+"'";
								ResultSet ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
								if(!ResultSet1.next())response.sendRedirect("JavaAndInternetMarket_Customer_Register.jsp");
								StructuredQueryLanguage="Select  CustomerID From Customer Where Name='"+request.getParameter("Text1")+"' And Password='"+PasswordProcessor1.getCodeByPassword(request.getParameter("Password1"))+"'";
								ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
								if(!ResultSet1.next())
								{
									out.print("<Tr><Td Align=Center ColSpan=2><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>I am Sorry,Your Password is Wrong! Please Input it Again!</B></Td></Tr>");
									response.setHeader("refresh","4;URL=JavaAndInternetMarket_Customer_LogOn.jsp");
								}
								else session.setAttribute("CustomerID",ResultSet1.getInt("CustomerID"));
								out.print("CustomerID="+session.getAttribute("CustomerID"));
								String[] MerchandiseIDs=request.getParameterValues("Hiddens");
								if(MerchandiseIDs!=null&&(!MerchandiseIDs[0].equals("null")))
								{
									for(int i=0;i<MerchandiseIDs.length;i++)
									{
										StructuredQueryLanguage="Select  * From Merchandise Where MerchandiseID="+MerchandiseIDs[i];
										ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
										while(ResultSet1.next())
										{
						%>
											<Tr>
												<Td Align=Center><Image Height=220 Width=220 src="Merchandise Images\<%=ResultSet1.getString("Name")%>.jpg"></Td>
												<Td>
													<Table Width=750 Align=Center Border=1 BorderColor=597C17>
														<Tr><Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>MerchandiseID:<%=ResultSet1.getString("MerchandiseID")%></B></Td></Tr>
														<Tr><Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Name:<%=ResultSet1.getString("Name")%></B></Td></Tr>
														<Tr><Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Classification:<%=ResultSet1.getString("Classification")%></B></Td></Tr>
														<Tr><Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Description:<%=ResultSet1.getString("Description")%></B></Td></Tr>
														<Tr><Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Price:<%=ResultSet1.getFloat("Price")%></B></Td></Tr>
														<Tr><Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Stocks:<%=ResultSet1.getInt("Stocks")%></B></Td></Tr>
														<Tr><Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>I Want to Buy this:<Input Type=CheckBox Name=CheckBoxes Value=<%= ResultSet1.getInt("MerchandiseID")%>></B></Td></Tr>
														<Tr><Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>How Many Would You Buy?<Input Type=Text Name=Text<%= ResultSet1.getInt("MerchandiseID")%> Value="1"></B></Td></Tr>
													</Table>
												</Td>
											</Tr>
						<%
										}
									}
								}
								else
								{
									StructuredQueryLanguage="Select  * From Merchandise Where Stocks>0";
									ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
									for(int i=0;i<6&&ResultSet1.next();i++)
									{

						%>
										<Tr>
											<Td Align=Center><Image Height=120 Width=120 src="Merchandise Images\<%=ResultSet1.getString("Name")%>.jpg"></Td>
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