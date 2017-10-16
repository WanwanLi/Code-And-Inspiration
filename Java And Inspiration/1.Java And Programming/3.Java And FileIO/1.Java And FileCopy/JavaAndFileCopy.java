import java.io.*;
public class JavaAndFileCopy
{
	public static void main(String[] args)
	{
		copyFile("File/File1.test","File/File2.test");
	}
	static void copyFile(String fileName1,String fileName2)
	{
		try
		{
			File File1=new File(fileName1);
			File File2=new File(fileName2);
     			FileInputStream FileInputStream1=new FileInputStream(File1);
     			FileOutputStream FileOutputStream1=new FileOutputStream(File2);
			byte[] bytes=new byte[(int)File1.length()];			
			FileInputStream1.read(bytes);
			FileOutputStream1.write(bytes);
			FileInputStream1.close();
			FileOutputStream1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
}