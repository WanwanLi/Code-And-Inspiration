import java.io.*;
import java.net.*;
import java.util.*;
public class JavaAndServerSocket
{
	public static void main(String[] args)
	{
		if(args[0].equals("receive"))receive();
		else if(args[0].equals("send"))send();
		else System.exit(0);
	}
	public static void send()
	{
		try
		{
			Scanner Scanner1=new Scanner(System.in);
			ServerSocket ServerSocket1=new ServerSocket(8080);
			Socket Socket1=ServerSocket1.accept();
			PrintWriter PrintWriter1=new PrintWriter(Socket1.getOutputStream(),true);
			while(true)
			{
				System.out.print("send():");
				String s=Scanner1.next();
				PrintWriter1.println(s);
				if(s.equals("exit"))break;
				s=Scanner1.nextLine();
			}
			Socket1.close();
		}
		catch(Exception e){System.err.println(e);}
	}
	public static void receive()
	{
		try
		{
			Socket Socket1=new Socket("127.0.0.1",8080);
			BufferedReader BufferedReader1=new BufferedReader(new InputStreamReader(Socket1.getInputStream()));
			while(true)
			{
				System.out.print("receive():");
				String s=BufferedReader1.readLine();
				if(s.equals("exit"))break;
				System.out.println(s);
			}
			Socket1.close();
		}
		catch(Exception e){}
	}
}