<%@ page contentType="text/html;charset=GB2312" %>
<Html>
	<Head>
		<Title>JavaAndInternetMarket_Administrator_LogOn</Title>
	</Head>
	<Script Language=JavaScript>
		function function1()
		{
			if(Form1.Password1.value=="")alert("Password Should not be Empty!");
			else if(Form1.Password1.value.length!=6){alert("Be Sure There is 6 Numbers in the Password ");Form1.reset();}
			else Form1.submit();
		}
	</Script>
	<Body Text=597C17 VLink=White ALink=Blue>
		<Form Name=Form1 Action=JavaAndInternetMarket_Administrator.jsp Method=post>
			<Center>
				<Table Align=Center Width=1000 Height=300 BackGround="Internet Market\InternetMarket.jpg">
					<Image Src="Internet Market\InternetMarket_Head.jpg"><Br>
					<Tr><Marquee  Behavior=Alternate ScrollAmount=10 BackGround="Internet Market\InternetMarket.jpg" BGColor=#FFD457><Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Welcome to  Administrate the Internet Market !<B></Font></Marquee></Tr><Br>
					<Tr><Td Align=Center>
						<Table Align=Center Width=500  Height=350 BackGround="Internet Market\InternetMarket_Input.jpg" Border=1 BorderColor=597C17>
							<Tr><Td Align=Center>
								<Font Color= 597C17 Size=5 Face=Î¢ÈíÑÅºÚ ><B>Please Input Password:</B>
								<Input Type=Password Name=Password1><Input Type=Button Value=OK onClick=function1()>
							</Td></Tr>
						</Table>
					</Td></Tr>
				</Table>
			</Center>
		</Form>
	</Body>
</Html>