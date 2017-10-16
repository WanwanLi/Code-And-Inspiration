import java.net.*;
import java.util.*;
public class JavaAndDatagramSocket
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
			DatagramSocket DatagramSocket1=new DatagramSocket();
			while(true)
			{
				System.out.print("send():");
				String s=Scanner1.next();
				DatagramPacket DatagramPacket1=new DatagramPacket(s.getBytes(),s.length(),InetAddress.getByName("localhost"),8080);
				DatagramSocket1.send(DatagramPacket1);
				if(s.equals("exit"))break;
				s=Scanner1.nextLine();
			}
			DatagramSocket1.close();
		}
		catch(Exception e){}
	}
	public static void receive()
	{
		try
		{
			DatagramSocket DatagramSocket1=new DatagramSocket(8080);
			while(true)
			{
				System.out.print("receive():");
				byte[] bytes=new byte[1024];
				DatagramPacket DatagramPacket1=new DatagramPacket(bytes,1024);
				DatagramSocket1.receive(DatagramPacket1);
				String s=new String(bytes,0,DatagramPacket1.getLength());
				if(s.equals("exit"))break;
				System.out.println(s);
			}
			DatagramSocket1.close();
		}
		catch(Exception e){}
	}
}