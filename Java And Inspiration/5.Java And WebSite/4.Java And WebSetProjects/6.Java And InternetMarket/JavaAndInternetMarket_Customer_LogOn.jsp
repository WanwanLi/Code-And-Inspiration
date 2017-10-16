<%@ page contentType="text/html;charset=GB2312" %>
<Html>
	<Head>
		<Title>JavaAndInternetMarket_Customer_LogOn</Title>
	</Head>
	<Script Language=JavaScript>
		function function1()
		{
			if(Form1.Text1.value=="")alert("Name Should not be Empty!");
			else if(Form1.Password1.value=="")alert("Password Should not be Empty!");
			else Form1.submit();
		}
	</Script>
	<Body Text=597C17 VLink=White ALink=Blue>
		<Form Name=Form1 Action=JavaAndInternetMarket_Customer.jsp Method=post>
			<Center>
				<Table Align=Center Width=1000 Height=300 BackGround="Internet Market\InternetMarket.jpg">
					<Image Src="Internet Market\InternetMarket_Head.jpg"><Br>
					<Tr><Marquee  Behavior=Alternate ScrollAmount=10 BackGround="Internet Market\InternetMarket.jpg" BGColor=#FFD457><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Welcome to Log On the Internet Market !<B></Font></Marquee></Tr><Br>
					<Tr><Td Align=Center>
						<Table Align=Center Width=500  Height=350 BackGround="Internet Market\InternetMarket_Input.jpg" Border=1 BorderColor=597C17>
							<Tr>
								<Td Align=Center><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Name:</B></Td>
								<Td Align=Center><Input Type=Text Name=Text1></Td>
							</Tr>
							<Tr>
								<Td Align=Center><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Password:</B></Td>
								<Td Align=Center><Input Type=Password Name=Password1></Td>
							</Tr>
							<Tr><Td Align=Center ColSpan=2><Input Type=Button Value="   Log On   " onClick=function1()>          <Input Type=Reset Value="   Clear   "></Td></Tr>
							<%
								String[] MerchandiseIDs=request.getParameterValues("CheckBoxes");
								if(MerchandiseIDs!=null)for(int i=0;i<MerchandiseIDs.length;i++)out.print("<Input Type=Hidden Name=Hiddens Value="+MerchandiseIDs[i]+">");
								else out.print("<Input Type=Hidden Name=Hiddens Value=null>");
							%>
						</Table>
					</Td></Tr>
				</Table>
			</Center>
		</Form>
	</Body>
</Html>