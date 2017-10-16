import java.io.*;
import java.net.*;
import java.util.*;
public class JavaAndClientServer
{
	private static String IP="127.0.0.1";
	private static int port=8080;
	public static void main(String[] args)
	{
		if(args[0].equals("server"))runServer();
		else if(args[0].equals("client"))runClient();
		else System.exit(0);
	}
	private static void runServer()
	{
		ServerSocket ServerSocket1=new ServerSocket(port);
		try
		{

			System.out.println("Server.start()");
			while(true)
			{
				Socket socket=ServerSocket1.accept();
				System.out.println("Server.accept()");
				BufferedReader BufferedReader1=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				DataOutputStream DataOutputStream1=new DataOutputStream(socket.getOutputStream());
				String input=BufferedReader1.readLine();
				System.out.println("Sever.Read>"+input);
				String reply="\'"+input+"\' has been recieved...\n";
				DataOutputStream1.writeBytes(reply);
				System.out.println("Server.Write>"+reply);
				if(input.equals("exit"))break;
			}

		}
		catch(Exception e){e.printStackTrace();}
	}
	private static void runClient()
	{
		while(true)
		{
			try
			{
				Socket socket=new Socket(IP,port);
				DataOutputStream DataOutputStream1=new DataOutputStream(socket.getOutputStream());
				BufferedReader BufferedReader1=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedReader BufferedReader2=new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Client.Input>");
				String input=BufferedReader2.readLine()+"\n";
				DataOutputStream1.writeBytes(input);
				String reply=BufferedReader1.readLine();
				System.out.println("Server.Reply>"+reply);
				BufferedReader1.close();
				socket.close();
				if(input.equals("exit\n"))break;
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
}
class Runnable_InetIOStream implements Runnable
{
	Socket socket;
	public Runnable_InetIOStream(Socket socket)
	{
		this.socket=socket;
	}
	public void run()
	{
		
	}
}