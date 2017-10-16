<%@ page  contentType="text/html;charset=GB2312"%>
<%@ page  import="java.util.*"  %>
<%@ page  import="java.io.*"  %>
<%@ page  import="java.sql.DriverManager"  %>
<%@ page  import="java.sql.Connection"  %>
<%@ page  import="java.sql.ResultSet"  %>
<%@ page  import="java.awt.*"  %>

<%! String String1="JavaAndJavaServerPage"; %>
<Html>
	<Head>
		<Title><%=String1%></Title>
	</Head>	
	<Script Language=JavaScript For=window Event=onresize>
		alert('onResize');
	</Script>
	<Script Language=JavaScript>
		function function1()
		{
			if(Form1.TextArea1.value=="")alert("Form1.TextArea1.value=null;");
			else if(Form1.Text1.value.indexOf("@")==-1){alert("Form1.Text1.value.DoNotHas(@);");Form1.reset();}
			else Form1.submit();
		}
	</Script>
	<Body BackGround=W.jpg BGColor=Black Text=White Link=White ALink=Purple >
		<Marquee Behavior=Alternate ScrollAmount=50><B><H1><%=String1%></H1></B></Marquee>
		<% out.println("<B><Font Size=30 Face=Î¢ÈíÑÅºÚ>"+String1+"<Br>Time:"+new Date()+"</Font></B><Br>"); %>
		<H1><B><%= request.getRealPath("/") %></B></H1><Br>
		<B><%= request.getRealPath("./") %></B><Br>
		<%
			File[] Files=(new File(request.getRealPath("./"))).listFiles();
			for(int i=0;i<Files.length;i++)out.println(Files[i].getName()+"<Br>");
			File File1=new File(request.getRealPath("./File1.jsp"));
			if(File1.exists()){File1.delete();out.println("Deleted!+<Br>");}
			else {File1.createNewFile();out.println("Created!");}
			PrintWriter PrintWriter1=new PrintWriter(request.getRealPath("./Text1.txt"));
			PrintWriter1.print("\nTextArea1:"+request.getParameter("TextArea1"));PrintWriter1.close();
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(request.getRealPath("./Text1.txt")));
			String s=BufferedReader1.readLine();while(s!=null){out.print(s+"<Br>");s=BufferedReader1.readLine();}				BufferedReader1.close();
			String[] Strings=request.getParameterValues("CheckBoxes");
			if(Strings!=null)for(int i=0;i<Strings.length;i++)out.println("CheckBoxes["+i+"]="+Strings[i]+"   ");
			Strings=request.getParameterValues("Radios");
			if(Strings!=null)out.print("<Br>Radios[0]="+Strings[0]);
			Strings=request.getParameterValues("Select1");
			if(Strings!=null)for(int i=0;i<Strings.length;i++)out.print("  Select1["+i+"]="+Strings[i]+"   ");
			if(Strings!=null&&Strings[0]!=null)
				if(Strings[0].equals("Option1"))response.setContentType("application/msword;charset=GB2312");	
				else if(Strings[0].equals("Option2"))response.setContentType("text/html;charset=GB2312");				
				else response.sendRedirect("JavaServerPagePrograming1.jsp");
   			         out.print("<Center><Table BackGround=X.jpg Border=1  Width=500  BorderColor=Purple`>");	
			         try
			         {
				Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
				Connection Connection1=DriverManager.getConnection("jdbc:odbc:DataSource2");
				ResultSet ResultSet1=Connection1.createStatement().executeQuery("Select * From Table1");
				while(ResultSet1.next())
				{
			  		out.print("<Tr>");
					    out.print("<Td>Key:</Td><Td>"+ResultSet1.getString("Key")+"</Td>");
					    out.print("<Td>Data:</Td><Td>"+ResultSet1.getString("Data1")+"</Td>");
					out.print("</Tr>");
				}									
				ResultSet1.close();
				Connection1.close();
			         }
			         catch(Exception e){out.print("SQLException:"+e.toString());}
			out.print("</Table></Center>");
		%>
		<Br>
		<Center><Table  Border=1 BorderColor=White Width=500>
			<Tr><Td><B><H1>ÉùÃ÷.txt:</H1></B></Td></Tr>
			<Tr><Td><B><H5><%@  include file="ÉùÃ÷.txt" %></H5></B></Td></Tr>			
		</Table></Center>
		<Form Name=Form1 Action=JavaAndJavaServerPage.jsp Method=post>
			<Center><TextArea Name=TextArea1 Rows=15 Cols=40></TextArea></Center><Br>
			<Center>Text1<Input Name=Text1>     Password<Input Type=Password Name=Password1></Center><Br>
			<Center><Input Type=Submit Value=Submit1><Input Type=Reset Value=Reset1 ></Center><Br>
		           	<Center>
				<Input Type=CheckBox Name=CheckBoxes Value=CheckBox1>CheckBox1
		                	<Input Type=CheckBox Name=CheckBoxes Value=CheckBox2>CheckBox2 		            	
				<Input Type=CheckBox Name=CheckBoxes Value=CheckBox3>CheckBox3 
				<Input Type=CheckBox Name=CheckBoxes Value=CheckBox4>CheckBox4<Br>			
				<Input Type=Radio Name=Radios Value=Radio1>Radio1	               		
				<Input Type=Radio Name=Radios Value=Radio2>Radio2
				<Input Type=Radio Name=Radios Value=Radio3>Radio3
				<Input Type=Radio Name=Radios Value=Radio4>Radio4<Br>
				<Select Name=Select1 Multiple>
					<Option Value=Option1>Option1</Option>
					<Option Value=Option2>Option2</Option>
					<Option Value=Option3>Option3</Option>
					<Option Value=Option4>Option4</Option>
				</Select>				
			</Center>
		</Form>

		<Center>
			TextArea1:<%= request.getParameter("TextArea1") %><Br>
			Text1:<%= request.getParameter("Text1") %><Br>
			Password1:<%= request.getParameter("Password1") %><Br>
			<A Href=JavaAndJavaServerPage.jsp?Text1=T&Password1=P>JavaAndJavaServerPage.jsp</A><Br>
		</Center>
		
		<Center><Input Type=Button Value=Button1WithFunction1 Size=30 onClick=function1()></Center><Br>


		<Form>
			<Table Align=Center BackGround=W.jpg Border=1 Width=500 Height=500 BorderColor=Purple >
				<Tr>
					<Td><Img Src=X.jpg Width=300 Height=200 Style=Visibility:Hidden></Td>
					<Td><jsp:include page="H.html" ></jsp:include></Td>
					<Td><Img Src=W.jpg Width=300 Height=200></Td>				
				</Tr>
				<Tr>				

					<Td Align=Center>
						<jsp:plugin type="applet"  code="Julia.class" width="200" height="500">
							<jsp:fallback>File Can Not Be Surported By Browser</jsp:fallback>
						</jsp:plugin>
					</Td>
					<Td Align=Center><Applet Code=Julia.class Width=500 Height=500></Applet></Td>
					<Td Align=Center><Object Code=Julia.class Width=200 Height=500></Object></Td>
				</Tr>
				<Tr>				

					<Td Align=Center ColSpan=3>
					        <jsp:useBean id="Bean1" class="JavaBeanPrograming.JavaBean1" scope="page"/>						        <Center>
						      <% Bean1.setString("Bean1");%> Bean1=<%= Bean1.getString()%><Br>
						      ALink1=<%= Bean1.getALink("H.html") %><Br>
						      Image1=<%= Bean1.getImage("W.jpg") %><Br>						      			      readFile=<%= Bean1.readFile("Text1.txt")%><Br>
						      writeFile=<%= Bean1.writeFile("Text2.txt","JavaBean")%><Br>
					        </Center>
					</Td>					
				</Tr>
			</Table>
		</Form>
		<Form>
			<Marquee><Table  Align=Center><Tr><Td><Input value=""></Td></Tr></Table></Marquee>
			<IFrame Image Src="Submit Files\W.jpg" Scrolling=Yes Width=700 Height=500 Align=Center></IFrame><Br>
		</Form>
		<Form Action=JavaAndJavaServerPage.jsp Method=post EncType="multipart/form-data">
			<Center><Input Type=File Name=File1 Size=50> 
				<Input Type=Submit Name=SubmitFile Value=SubmitFile>
			</Center>
		</Form>
		<%
			try
			{
				File1=new File(request.getRealPath("./Submit Files"),(String)session.getId()+".dat");
				FileOutputStream FileOutputStream1=new FileOutputStream(File1);
				InputStream InputStream1=request.getInputStream();
				byte[] bytes=new byte[1024];
				int n=InputStream1.read(bytes);
				while(n!=-1)
				{
					FileOutputStream1.write(bytes,0,n);
					n=InputStream1.read(bytes);
				}
				FileOutputStream1.close();
				InputStream1.close();
				RandomAccessFile RandomAccessFile1=new RandomAccessFile(File1,"r");
				String S1=RandomAccessFile1.readLine();
				String S2=RandomAccessFile1.readLine();
				out.print("<Br>First Line In File Is:"+S1+"<Br>Second Line In File Is:"+S2);
				int StartIndexOfFileName=S2.lastIndexOf('\\')+1;
				String FileName=S2.substring(StartIndexOfFileName,S2.length()-1);
				out.print("<Br>Old FileName Is:"+FileName);				
				FileName=new String(FileName.getBytes("ISO-8859-1"));				
				out.print("<Br> New FileName Is:"+FileName);
				session.setAttribute("FileName",FileName);
				RandomAccessFile1.seek(0);
				int NumberOf_n=0;
				long StartIndexOfFile=0;
				n=RandomAccessFile1.readByte();
				while(n!=-1 &&NumberOf_n<4)
				{
					if(n=='\n')
					{
						StartIndexOfFile=RandomAccessFile1.getFilePointer();
						NumberOf_n++;
					}
					n=RandomAccessFile1.readByte();
				}
				RandomAccessFile1.seek(RandomAccessFile1.length());
				NumberOf_n=0;
				long EndIndexOfFile=0;
				long RecentIndex=RandomAccessFile1.getFilePointer();
				while(RecentIndex>0&&NumberOf_n<6)
				{
					RandomAccessFile1.seek(--RecentIndex);
					n=RandomAccessFile1.readByte();
					if(n=='\n')
					{
						EndIndexOfFile=RandomAccessFile1.getFilePointer();
						NumberOf_n++;
					}
				}								
				File File2=new File(request.getRealPath("./Submit Files"),FileName);	
				RandomAccessFile RandomAccessFile2=new RandomAccessFile(File2,"rw");
				RandomAccessFile1.seek(StartIndexOfFile);
				RecentIndex=RandomAccessFile1.getFilePointer();
				while(RecentIndex<EndIndexOfFile)
				{
					RandomAccessFile2.write(RandomAccessFile1.readByte());
					RecentIndex=RandomAccessFile1.getFilePointer();
				}
				File1.delete();
				RandomAccessFile1.close();				
				RandomAccessFile2.close();	
			}
			catch(Exception e){}
		%>
		<Center>
			<A Href="Submit Files\<%=(String)session.getAttribute("FileName") %>"><B>Image File Submited is:</B>
			</A><Br><Image Src="Submit Files\<%=(String)session.getAttribute("FileName") %>"><Br>
		</Center>
		<Form  Action=JavaServerPagePrograming2.jsp Method=post>
			<Center><Input Type=Submit Value="Click Here to DownLoad FILE!" Name=DownLoad></Center>
		</Form>		
		<%
			String String1="Attribute1="+application.getAttribute("Attribute1");
		 	application.setAttribute("Attribute1","application.Attribute1");		 
			String String2=(String)application.getAttribute("Attribute2");		  
			int int1=1;if(String2!=null)int1+=Integer.parseInt(String2);
			application.setAttribute("Attribute2",String.valueOf(int1)); 	
			Date Date1=new Date(session.getCreationTime()) ;
			Date Date2=new Date(session.getLastAccessedTime()) ;				  	
			Date Date3=new Date() ;
		%>
		<Center><%= String1 %>    int1=<%= int1 %></Center>
		<Center>CreationTime:<%=Date1 %> LastAccessedTime:<%=Date2 %> </Center>
		<%
			String String0=request.getParameter("Submit2");
			if(String0==null)
			{
				session.setAttribute("Time0",new Date());
		%>
				<Form  Name=Form2 Action=JavaAndJavaServerPage.jsp Method=post>
					<Center>
						<TextArea Rows=5 Cols=40 Name=TextArea2></TextArea><Br>
						<Input Type=Submit Name=Submit2 Value=Submit2>
					</Center>
				</Form>
		<%
			}
			else
			{
				Date Date0=(Date)session.getAttribute("Time0");
		%>
				<Center>
					<Br>---------------------------------------------------------------------------------------------<Br>
					TextArea2:<%= request.getParameter("TextArea2")%><Br>
					Using Time:<%=( (new Date()).getTime()-Date0.getTime() )/1000 %>
					<Br>---------------------------------------------------------------------------------------------<Br>
				</Center>
		<%
			}
		%>

		<Center>__________________________H.request__________________________</Center>
		<%
			String HString1=request.getParameter("HText1");
			String HString2=request.getParameter("HPassword1");
			if(HString1!=null && HString2!=null)
			{
				if(HString1.length()==0||HString2.length()==0)response.sendRedirect("H.html");
				else
				{
					session.setAttribute("HText1",HString1);
					session.setAttribute("HPassword1",HString2);
					out.print("<Center>HText1:"+HString1+"  HPassword1:"+HString2+"</Center>");
					Cookie Cookie1=new Cookie("HText1",HString1);
					Cookie Cookie2=new Cookie("HPassword1",HString2);
					response.addCookie(Cookie1);
					response.addCookie(Cookie2);
				}
			}
		%>
		<Center>__________________________H.session__________________________</Center>
		<%
			String HString3=(String)session.getAttribute("HText1");
			String HString4=(String)session.getAttribute("HPassword1");
			if(HString1!=null && HString2!=null)
			{
		%>
				<Center>
					<Form Action=JavaAndJavaServerPage.jsp Method=post>
						Text1:<Input Name=Text1 Value=<%=HString3%> ><Br>
						Password1:<Input Type=Password Value=<%=HString4%>><Br>
						Submit1: <Input Type=Submit Name=Submit1 Value=Submit1>
					</Form>
				</Center>
		<%
			}
		%>
		<Center>__________________________H.Cookies__________________________</Center>	
		<%	
			Cookie[] Cookies=request.getCookies();
			if(Cookies!=null)
			{
				for(int i=0;i<Cookies.length;i++)
				{
					if(Cookies[i].getName().equals("HText1"))
						out.print("<Center>HText1="+Cookies[i].getValue()+"<Br></Center>");
					if(Cookies[i].getName().equals("HPassword1"))
						out.print("<Center>HPassword1="+Cookies[i].getValue()+"<Br></Center>");
				}
			}
		%>
	</Body>
</Html>












