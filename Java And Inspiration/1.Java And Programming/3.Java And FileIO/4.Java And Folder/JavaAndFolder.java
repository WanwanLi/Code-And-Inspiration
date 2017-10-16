import java.io.*;
public class JavaAndFolder
{
	public static void main(String[] args)
	{
		traverseFolder(args[0],"");
	}
	static void traverseFolder(String path, String prefix) 
	{  
      
		File File1 = new File(path);  
		if (File1.exists()) 
		{  
			File[] files = File1.listFiles();  
			if (files.length == 0) 
			{  
				System.out.println(prefix+"Folder is Empty.");  
				return;  
			}
			for (File file : files) 
			{  
				if (file.isDirectory()) 
				{
					String dir=file.getPath();
					System.out.println(prefix+"Folder: " + dir);  
					traverseFolder(dir,prefix+"____");  
				}
				else System.out.println(prefix+"File Name:" + file.getPath());  
			}
		}
		else System.out.println(prefix+"File dir: "+path+" doesn't exists!"); 
	}
}