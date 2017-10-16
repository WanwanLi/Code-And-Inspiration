import java.io.*;
import java.net.*;
import java.util.*;
public class JavaAndUserDatagramProtocol
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
		try
		{
			DatagramSocket DatagramSocket1=new DatagramSocket(port);
			System.out.println("Server.start()");
			byte[] receiveBytes=new byte[1024];
			while(true)
			{
				DatagramPacket DatagramPacket1=new DatagramPacket(receiveBytes,receiveBytes.length);
				DatagramSocket1.receive(DatagramPacket1);
				String input=new String(DatagramPacket1.getData());
				for(int i=0;i<input.length();i++)if(input.charAt(i)=='\0'){input=input.substring(0,i);break;}
				System.out.println("Sever.receive>"+input);
				String reply="\'"+input+"\' has been recieved...\n";
				byte[] replyBytes=reply.getBytes();
				DatagramPacket DatagramPacket2=new DatagramPacket(replyBytes,replyBytes.length,DatagramPacket1.getAddress(),DatagramPacket1.getPort());
				DatagramSocket1.send(DatagramPacket2);
				System.out.println("Server.send>"+reply);
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
				DatagramSocket DatagramSocket1=new DatagramSocket();
				InetAddress InetAddress1=InetAddress.getByName(IP);
				BufferedReader BufferedReader1=new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Client.Input>");
				String input=BufferedReader1.readLine()+"\0";
				byte[] sendBytes=input.getBytes();
				DatagramPacket DatagramPacket1=new DatagramPacket(sendBytes,sendBytes.length,InetAddress1,port);
				DatagramSocket1.send(DatagramPacket1);
				System.out.print("Server.Reply>");
				byte[] receiveBytes=new byte[1024];
				DatagramPacket DatagramPacket2=new DatagramPacket(receiveBytes,receiveBytes.length);
				DatagramSocket1.receive(DatagramPacket2);
				String reply=new String(DatagramPacket2.getData());
				System.out.println("Server.Reply>"+reply);
				if(input.equals("exit\0"))break;
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
}