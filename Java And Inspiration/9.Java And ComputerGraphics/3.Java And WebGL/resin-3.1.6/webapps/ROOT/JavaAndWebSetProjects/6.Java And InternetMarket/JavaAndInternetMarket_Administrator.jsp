<%@ page contentType="text/html;charset=GB2312" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<Html>
	<Head>
		<Title>JavaAndInternetMarket_Administrator</Title>
	</Head>
	<Script Language=JavaScript>
		function function_Insert()
		{
			if(Form1.Text_Name.value=="")alert("Name Should not be Empty!");
			else if(Form1.Text_Classification.value=="")alert("Classification Should not be Empty!");
			else if(Form1.Text_Description.value=="")alert("Description Should not be Empty!");
			else if(Form1.Text_Price.value=="")alert("Price Should not be Empty!");
			else if(Form1.Text_Stocks.value=="")alert("Stocks Should not be Empty!");
			else if(Form1.File_Image.value=="")alert("Image Should not be Empty!");
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
			else if(Form1.Text_Classification.value==""&&Form1.Text_Description.value==""&&Form1.Text_Price.value==""&&Form1.Text_Stocks.value==""&&Form1.File_Image.value=="")alert("At Least One Item Should be Updated!");
			else {Form1.Hidden_Parameter.value="Update";Form1.submit();}
		}
	</Script>
	<Body Text=597C17 VLink=White ALink=Blue>
		<Form Name=Form1 Action=JavaAndInternetMarket_Administrator.jsp Method=post>
			<Table Align=Center Width=1000 Height=300 BackGround="Internet Market\InternetMarket.jpg">
				<Image Src="Internet Market\InternetMarket_Head.jpg"><Br>
				<Tr><Marquee  Behavior=Alternate ScrollAmount=10 BackGround="Internet Market\InternetMarket.jpg" BGColor=#FFD457><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Welcome to  Administrate the Internet Market !<B></Font></Marquee></Tr><Br>
				<Tr><Td Align=Center>
					<Table Align=Center Width=500  Height=350 BackGround="Internet Market\InternetMarket_Input.jpg" Border=1 BorderColor=597C17>
						<Tr>
							<%
								if(request.getParameter("Password1")!=null&&!request.getParameter("Password1").equals("112358"))	
								{
									out.print("<Td Align=Center ColSpan=2><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>I am Sorry,Your Password is Wrong!<Br>Please Input Your Password Again !</B></Font></Td>");
									response.setHeader("refresh","5;URL=JavaAndInternetMarket_Administrator_LogOn.jsp");
								}
							%>
						</Tr>
						<Tr><Td Align=Center ColSpan=2><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Informations Of Merchandise</B></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Name:</B></Td><Td Align=Center><Input Type=Text Name=Text_Name></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Classification:</B></Td><Td Align=Center><Input Type=Text Name=Text_Classification></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Description:</B></Td><Td Align=Center><Input Type=Text Name=Text_Description></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Price:</B></Td><Td Align=Center><Input Type=Text Name=Text_Price></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Stocks:</B></Td><Td Align=Center><Input Type=Text Name=Text_Stocks></Td></Tr>
						<Tr><Td Align=Left><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Image:</B></Td><Td  Align=Center><Input Type=File Name=File_Image Size=10></Td></Tr>
					</Table>
				</Td></Tr>
				<Tr><Td Align=Center>
					<Table Align=Center Width=500  Height=50 BackGround="Internet Market\InternetMarket_Input.jpg" Border=1 BorderColor=597C17>
							<Td  Align=Center><Input Type=Button Name=Button_Insert Value=Insert onclick=function_Insert()></Td>
							<Td  Align=Center><Input Type=Button Name=Button_Delete Value=Delete onclick=function_Delete()></Td>
							<Td  Align=Center><Input Type=Button Name=Button_Select Value=Select onclick=function_Select()></Td>
							<Td  Align=Center><Input Type=Button Name=Button_Update Value=Update onclick=function_Update()></Td>
							<Td  Align=Center><Input Type=Hidden Name=Hidden_Parameter></Td>
						</Tr>
					</Table>
				</Td></Tr>
			</Table>		
		</Form>
		<%
			
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				Connection Connection1=DriverManager.getConnection("jdbc:mysql://localhost:3306/InternetMarket","root","11235813");
				String StructuredQueryLanguage="SELECT * FROM Merchandise";
				if(request.getParameter("Hidden_Parameter")!=null&&request.getParameter("Hidden_Parameter").equals("Insert"))
				{
					StructuredQueryLanguage="Insert Into Merchandise (Name,Classification,Description,Price,Stocks) Values('"+request.getParameter("Text_Name")+"','"+request.getParameter("Text_Classification")+"','"+request.getParameter("Text_Description")+"',"+request.getParameter("Text_Price")+","+request.getParameter("Text_Stocks")+")";
					Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
					try
					{
						File File1=new File(request.getParameter("File_Image"));
						FileInputStream FileInputStream1=new FileInputStream(File1);
						File File2=new File("C:\\resin-3.1.6\\webapps\\ROOT\\JavaAndWebSetProjects\\6.Java And InternetMarket\\Merchandise Images",request.getParameter("Text_Name")+".jpg");
						FileOutputStream FileOutputStream1=new FileOutputStream(File2);
						byte[] bytes=new byte[1024];
						int n=FileInputStream1.read(bytes);
						while(n!=-1)
						{
							FileOutputStream1.write(bytes,0,n);
							n=FileInputStream1.read(bytes);
						}
						FileOutputStream1.close();
						FileInputStream1.close();
					}
					catch(Exception e){}	
				}
				if(request.getParameter("Hidden_Parameter")!=null&&request.getParameter("Hidden_Parameter").equals("Delete"))
				{
					StructuredQueryLanguage="Delete From Merchandise Where Name='"+request.getParameter("Text_Name")+"'";
					Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
				}
				if(request.getParameter("Hidden_Parameter")!=null&&request.getParameter("Hidden_Parameter").equals("Select"))
				{
					StructuredQueryLanguage="Select * From Merchandise Where Name='"+request.getParameter("Text_Name")+"'";
					ResultSet ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
		%>
					<Table Align=Center Width=1168 BackGround="Internet Market\InternetMarket.jpg">
						<Tr><Td>
							<Table Align=Center Width=500 Border=1 BackGround="Internet Market\InternetMarket_Result.jpg" BorderColor=597C17>
								<%
									while(ResultSet1.next())
									{
								%>
										<Tr>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Name:</B></Td>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B><%=ResultSet1.getString("Name")%></B></Td>
										</Tr>
										<Tr>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Classification:</B></Td>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B><%=ResultSet1.getString("Classification")%></B></Td>
										</Tr>
										<Tr>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Description:</B></Td>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B><%=ResultSet1.getString("Description")%></B></Td>
										</Tr>
										<Tr>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Price:</B></Td>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B><%=ResultSet1.getFloat("Price")%></B></Td>
										</Tr>
										<Tr>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Stocks:</B></Td>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B><%=ResultSet1.getInt("Stocks")%></B></Td>
										</Tr>
										<Tr>
											<Td><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Image:</B></Td>
											<Td><Image Src="Merchandise Images\<%=""+ResultSet1.getString("Name")%>.jpg"></Td>
										</Tr>
								<%
									}
									ResultSet1.close();
								%>
							</Table>
						</Td></Tr>
					</Table>
		<%
				}
				if(request.getParameter("Hidden_Parameter")!=null&&request.getParameter("Hidden_Parameter").equals("Update"))
				{
					if(!request.getParameter("Text_Classification").equals(""))
					{
						StructuredQueryLanguage="Update Merchandise Set Classification='"+request.getParameter("Text_Classification")+"' Where Name='"+request.getParameter("Text_Name")+"'";
						Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
					}
					if(!request.getParameter("Text_Description").equals(""))
					{
						StructuredQueryLanguage="Update Merchandise Set Description='"+request.getParameter("Text_Description")+"' Where Name='"+request.getParameter("Text_Name")+"'";
						Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
					}
					if(!request.getParameter("Text_Price").equals(""))
					{
						StructuredQueryLanguage="Update Merchandise Price="+request.getParameter("Text_Price")+" Where Name='"+request.getParameter("Text_Name")+"'";
						Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
					}
					if(!request.getParameter("Text_Stocks").equals(""))
					{
						StructuredQueryLanguage="Update Merchandise Set Stocks="+request.getParameter("Text_Stocks")+" Where Name='"+request.getParameter("Text_Name")+"'";
						Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
					}
					if(!request.getParameter("File_Image").equals(""))
					{
						try
						{
							File File1=new File(request.getParameter("File_Image"));
							FileInputStream FileInputStream1=new FileInputStream(File1);
							File File2=new File("C:\\resin-3.1.6\\webapps\\ROOT\\JavaWebSetPrograming\\6.Java And InternetMarket\\Merchandise Images",request.getParameter("Text_Name")+".jpg");
							FileOutputStream FileOutputStream1=new FileOutputStream(File2);
							byte[] bytes=new byte[1024];
							int n=FileInputStream1.read(bytes);
							while(n!=-1)
							{
								FileOutputStream1.write(bytes,0,n);
								n=FileInputStream1.read(bytes);
							}
							FileOutputStream1.close();
							FileInputStream1.close();
						}
						catch(Exception e){}	
					}
				}
				Connection1.close();
			}
			catch(Exception e){out.print(e.toString());}
		%>
	</Body>
</Html>