<%@ page contentType="text/html;charset=GB2312"%>
<%@ page import="java.io.*"%>

<Html>
	<Head>
		<Title>DownLoad</Title>
	</Head>
	<Body>
		<%
			
			response.setHeader("Content-disposition","attachment;filename=W1.zip");
			response.setContentType("application/x-tar");
			OutputStream OutputStream1=response.getOutputStream();
			File File1=new File(request.getRealPath("./Submit Files"),"W1.zip");
			FileInputStream FileInputStream1=new FileInputStream(File1);
			byte[] bytes=new byte[1024];
			int n=FileInputStream1.read(bytes);
			while(n!=-1)
			{
				OutputStream1.write(bytes,0,n);		
				n=FileInputStream1.read(bytes);
			}
			OutputStream1.close();
			FileInputStream1.close();		
		%>
	</Body>
</Html>
tarenamis