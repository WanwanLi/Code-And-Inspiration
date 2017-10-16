<%@ page contentType="text/html;charset=GB2312" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<Html>
	<Head>
		<Title>JavaAndInternetMarket_OrderForm</Title>
	</Head>
	<Body Text=597C17 VLink=White ALink=Blue>
		<Form Name=Form1 Action=JavaAndInternetMarket_OrderList.jsp Method=post>
			<Table Align=Center Width=1000 Height=300 BackGround="Internet Market\InternetMarket.jpg">
				<Image Src="Internet Market\InternetMarket_Head.jpg"><Br>
				<Tr><Marquee  Behavior=Alternate ScrollAmount=10 BackGround="Internet Market\InternetMarket.jpg" BGColor=#FFD457><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Welcome to the Internet Market! Have a Good Time!<B></Font></Marquee></Tr><Br>
				<Tr><Td Align=Center>
					<Table Align=Center Width=800  Height=50 Border=1 BorderColor=597C17 BackGround="Internet Market\InternetMarket_OrderForm.jpg">
						<Tr><Td  Align=Center><Font Color= 597C17 Size=10 Face=Î¢ÈíÑÅºÚ ><B>OrderForms Information :</B></Td></Tr>
						<%
							try
							{
								Class.forName("com.mysql.jdbc.Driver");
								Connection Connection1=DriverManager.getConnection("jdbc:mysql://localhost:3306/InternetMarket","root","11235813");
								String[] MerchandiseIDs=request.getParameterValues("CheckBoxes");
								if(session.getAttribute("CustomerID")==null)response.sendRedirect("JavaAndInternetMarket_Customer_LogOn.jsp");
								int CustomerID=(Integer)session.getAttribute("CustomerID");
								String StructuredQueryLanguage="Select  Name From Customer Where CustomerID="+CustomerID;
								ResultSet ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
								ResultSet1.next();
								String CustomerName=ResultSet1.getString("Name");
								GregorianCalendar GregorianCalendar1=new GregorianCalendar();
								String date=GregorianCalendar1.get(Calendar.YEAR)+"-"+(GregorianCalendar1.get(Calendar.MONTH)+1)+"-"+GregorianCalendar1.get(Calendar.DAY_OF_MONTH);
								float FinalPrice=0.0f;
								if(MerchandiseIDs!=null)
								{
									for(int i=0;i<MerchandiseIDs.length;i++)
									{
										StructuredQueryLanguage="Select  * From Merchandise Where MerchandiseID="+MerchandiseIDs[i];
										ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
										ResultSet1.next();
										String MerchandiseName=ResultSet1.getString("Name");
										int MerchandiseSum=Integer.parseInt(request.getParameter("Text"+MerchandiseIDs[i]));
										int Stocks=ResultSet1.getInt("Stocks");
										if(MerchandiseSum>Stocks)MerchandiseSum=Stocks;
										StructuredQueryLanguage="Update Merchandise Set Stocks="+(Stocks-MerchandiseSum)+" Where MerchandiseID="+MerchandiseIDs[i];
										Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
										float MerchandisePrice=ResultSet1.getFloat("Price");
										FinalPrice+=MerchandiseSum*MerchandisePrice;
										StructuredQueryLanguage="Insert Into OrderForm (CustomerID,MerchandiseID,Sum,Date) Values ("+CustomerID+","+MerchandiseIDs[i]+","+MerchandiseSum+",'"+date+"')";
										Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
										StructuredQueryLanguage="Select Max(OrderFormID) As NewOrderFormID From OrderForm";
										ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
										ResultSet1.next();
										int OrderFormID=ResultSet1.getInt("NewOrderFormID");

						%>
										<Tr>
											<Td>
												<Table Width=750 Align=Center Border=1 BorderColor=597C17>
													<Tr><Td ColSpan=2><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>OrderFormID:<%=OrderFormID%></B></Td></Tr>
													<Tr>
														<Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>CustomerID:<%=CustomerID%></B></Td>
														<Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Customer Name:<%=CustomerName%></B></Td>
													</Tr>
													<Tr>
														<Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>MerchandiseID:<%=MerchandiseIDs[i]%></B></Td>
														<Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Merchandise Name:<%=MerchandiseName%></B></Td>
													</Tr>
													<Tr>
														<Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Merchandise Sum:<%=MerchandiseSum%></B></Td>
														<Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Merchandise Price:<%=MerchandisePrice%></B></Td>
													</Tr>
												</Table>
											</Td>
										</Tr>
						<%
									}
								}
								else
								{
						%>
									<Tr>
										<Td>
											<Table Width=750 Align=Center Border=1 BorderColor=597C17>
												<Tr><Td><Font Color= 597C17 Size=2 Face=Î¢ÈíÑÅºÚ ><B>You Have Not Choose Any One!</B></Td></Tr>	
											</Table>
										</Td>
									</Tr>
						<%
									
								}
								Connection1.close();
						%>
								<Tr>
									<Td>
										<Table Width=750 Align=Center Border=1 BorderColor=597C17>
											<Tr>
												<Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Date:<%=date%></B></Td>
												<Td><Font Color= 597C17 Size=4 Face=Î¢ÈíÑÅºÚ ><B>Final Price:<%=FinalPrice%></B></Td>
											</Tr>
										</Table>
									</Td>
								</Tr>
						<%
							}
							catch(Exception e){out.print(e.toString());}
						%>
					</Table>
				</Td></Tr>
			</Table>		
		</Form>
	</Body>
</Html>