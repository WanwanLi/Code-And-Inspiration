import java.awt.*;
import java.awt.event.*;
class JavaAndCubicStorehouseScript
{
	public static void main(String[] args)
	{
		Frame Frame1=new Frame();
		Frame1.setLayout(null);
		final CubicStorehouseScriptPanel CubicStorehouseScriptPanel1=new CubicStorehouseScriptPanel(50,50,900,700);
		Frame1.add(CubicStorehouseScriptPanel1);
		Frame1.setVisible(true);
		Frame1.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				String[] sql=CubicStorehouseScriptPanel1.getSQLcommand();
				if(sql!=null)for(int i=0;i<sql.length;i++)System.out.println(sql[i]);
				System.exit(0);
			}
		});
	}
}
class CubicStorehouseScriptPanel extends Panel implements TextListener
{
	TextWindow textWindow_InputHint;
	TextArea textArea_Input,textArea_Lines,textArea_Error;
	int textInterval=12;
	int charInterval=6;
	int numberInterval=20;
	int x0,y0;
	int beginIndex=0;
	int startRow,startColumn,row,column;
	String errorMessage;
	//MySQLprocessor mySQLprocessor;
	public CubicStorehouseScriptPanel(int x0,int y0,int width,int height)//,MySQLprocessor mySQLprocessor)
	{
		this.x0=x0;
		this.y0=y0;
		this.setLayout(null);
		this.textArea_Input=new TextArea("",0,0,textArea_Input.SCROLLBARS_NONE);
		this.textArea_Input.setBounds(numberInterval,0,width-numberInterval-1,4*height/5);
		this.textArea_Input.setBackground(Color.white);
		this.textArea_Input.addTextListener(this);
		this.textArea_Lines=new TextArea(this.getLines(100),0,0,textArea_Input.SCROLLBARS_NONE);
		this.textArea_Lines.setBounds(0,0,numberInterval,4*height/5);
		this.textArea_Lines.setBackground(Color.white);
		this.textArea_Lines.setEditable(false);
		this.textArea_Error=new TextArea("",0,0,textArea_Input.SCROLLBARS_VERTICAL_ONLY);
		this.textArea_Error.setBounds(0,4*height/5,width-1,height/5);
		this.textArea_Error.setBackground(Color.white);
		this.textArea_Error.setEditable(false);
		this.add(textArea_Input);
		this.add(textArea_Lines);
		this.add(textArea_Error);
		this.setBounds(x0,y0,width,height);
		this.textWindow_InputHint=new TextWindow();
		this.setFocusable(true);
	}
	private String getLines(int endLine)
	{
		String lines="";
		for(int i=1;i<=endLine;i++)lines+=i+"\r\n";
		return lines;
	}
	public void textValueChanged(TextEvent e)
	{
		String[] texts=this.getStringArray(this.textArea_Input.getText());
		int length=texts.length;
		int startX=x0+texts[length-1].length()*charInterval+numberInterval;
		int startY=y0+length*textInterval;
		String inputText=texts[length-1];
		this.textWindow_InputHint.setPositionAndTexts(startX,startY,this.getHintTexts(inputText));
	}
	String[] getHintTexts(String inputText)
	{
		String[] hintTexts=null;
		String[] allHintTexts=this.getAllHintTexts();
		int length=allHintTexts.length;
		StringQueue StringQueue1=new StringQueue();
		for(int i=0;i<length;i++)
		{
			if(!inputText.equals("")&&allHintTexts[i].startsWith(inputText))StringQueue1.enQueue(allHintTexts[i]);
			if(("this."+allHintTexts[i]).startsWith(inputText)&&inputText.length()>4)StringQueue1.enQueue(allHintTexts[i]);
		}
		hintTexts=StringQueue1.getStrings();
		return hintTexts;
	}
	public String[] getSQLcommand()
	{
		String[] texts=this.getStringArray(this.textArea_Input.getText());
		this.startRow=0;
		this.startColumn=0;
		this.row=0;
		this.column=0;
		int length=texts.length;
		String errors="";
		StringQueue SQLcommand=new StringQueue();
		for(int i=0;i<length;i++)
		{
			String sql=this.convertToSQL(texts[i]);
			if(sql.equals(""))errors+="There is an error at line:"+(i+1)+";"+this.errorMessage+"\r\n";
			else SQLcommand.enQueue(sql);
		}
		if(!errors.equals(""))
		{
			this.textArea_Error.setText(errors);
			return null;
		}
		this.textArea_Error.setText("Compiled Successfully...");
		return SQLcommand.getStrings();
	}
	private String[] getAllHintTexts()
	{
		return new String[]
		{
			"box[column][level]=merchandiseID;",
			"row=storehouseRow;",
			"column=storehouseColumn;",
			"setShelf(shelfColumn,shelfLevel);",
			"setBox(storehouseRow,storehouseColumn,shelfColumn,shelfLevel,merchandiseID);",
			"setRegion(regionName);"
		};
	}
	private String[] getStringArray(String text)
	{
		int l=text.length();
		int k=0;
		char c;
		for(int i=0;i<l;i++)if((c=text.charAt(i))=='\n')k++;
		String[] stringArray=new String[k+1];
		String s="";
		for(int i=0;i<=k;i++)stringArray[i]=s;
		int i=0;
		k=0;
		while(i<l)
		{
			c=text.charAt(i++);
			if(c=='\r')c=text.charAt(i++);
			if(c!='\n')s+=c;
			else
			{
				stringArray[k++]=s;
				s="";
			}
		}
		if(!s.equals(""))stringArray[k++]=s;
		return stringArray;
	}
	private String convertToSQL(String text)
	{
		String SQL="";
		this.errorMessage="";
		this.beginIndex=0;
		String substring=this.getSubstring(text,new char[]{'.'});
		if(!substring.equals("this"))this.beginIndex=0;
		substring=this.getSubstring(text,new char[]{'[','(','='});
		if(substring.equals("box"))
		{
			String shelfColumn=this.getSubstring(text,new char[]{']'});
			this.getSubstring(text,new char[]{'['});
			String shelfLevel=this.getSubstring(text,new char[]{']'});
			this.getSubstring(text,new char[]{'='});
			String merchandiseID=this.getSubstring(text,new char[]{';'});
			if(merchandiseID.equals("null"))SQL="Delete From MerchandiseLocationInfo Where storehouseRow="+this.row+" And storehouseColumn="+this.column+" And shelfColumn="+shelfColumn+" And shelfLevel="+shelfLevel;
			else SQL="Insert Into MerchandiseLocationInfo Values ("+merchandiseID+","+(this.startRow+this.row)+","+(this.startColumn+this.column)+","+shelfColumn+","+shelfLevel+")";
		}
		else if(substring.equals("row"))
		{
			String row=this.getSubstring(text,new char[]{';'});
			if(!isInteger(row))this.errorMessage="Row isn't an integer!";
			this.row=Integer.parseInt(row);
		}
		else if(substring.equals("column"))
		{
			String column=this.getSubstring(text,new char[]{';'});
			if(!isInteger(column))this.errorMessage="Column isn't an integer!";
			this.column=Integer.parseInt(column);
		}
		else if(substring.equals("setShelf"))
		{
			String row=this.getSubstring(text,new char[]{','});
			String column=this.getSubstring(text,new char[]{')'});
			if(!isInteger(row))this.errorMessage="Row isn't an integer!";
			this.row=Integer.parseInt(row);
			if(!isInteger(column))this.errorMessage+="\r\nColumn isn't an integer!";
			this.column=Integer.parseInt(column);
		}
		else if(substring.equals("setBox"))
		{
			String storehouseRow=this.getSubstring(text,new char[]{','});
			String storehouseColumn=this.getSubstring(text,new char[]{','});
			String shelfColumn=this.getSubstring(text,new char[]{','});
			String shelfLevel=this.getSubstring(text,new char[]{','});
			String merchandiseID=this.getSubstring(text,new char[]{')'});
			if(merchandiseID.equals("null"))SQL="Delete From MerchandiseLocationInfo Where storehouseRow="+this.row+" And storehouseColumn="+this.column+" And shelfColumn="+shelfColumn+" And shelfLevel="+shelfLevel;
			else SQL="Insert Into MerchandiseLocationInfo Values ("+merchandiseID+","+storehouseRow+","+storehouseColumn+","+shelfColumn+","+shelfLevel+")";
		}
		return SQL;
	}
	private boolean isInteger(String s)
	{
		int length=s.length();
		if(length==0)return false;
		for(int i=0;i<length;i++)
		{
			char c=s.charAt(i);
			if(c<'0'||c>'9')return false;
		}
		return true;
	}
	private String getSubstring(String string,char[] endChar)
	{
		int i=beginIndex;
		int l=string.length();
		if(i>=l)return "";
		String substring="";
		char Char=string.charAt(i++);
		while(Char==' '&&i<l)Char=string.charAt(i++);
		while(!isIn(Char,endChar))
		{
			substring+=Char;
			if(i>=l)break;
			Char=string.charAt(i++);
		}
		this.beginIndex=i;
		return substring;
	}
	private boolean isIn(char c,char[] a)
	{
		int l=a.length;
		for(int i=0;i<l;i++)if(a[i]==c)return true;
		return false;
	}
}
class TextWindow extends Window
{
	int textLength;
	int textInterval=11;
	int charInterval=8;
	String[] texts;
	int width;
	int height;
	public TextWindow()
	{
		super(null);
		this.setBackground(Color.white);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	public void setPositionAndTexts(int startX,int startY,String[] texts)
	{
		if(texts==null){this.reshape(startX,startY,0,0);return;}
		this.texts=texts;
		this.textLength=texts.length;
		this.width=this.maxLength(texts)*charInterval;
		this.height=(this.textLength+1)*textInterval;
		this.reshape(startX,startY,width,height);
		this.repaint();
	}
	private int maxLength(String[] texts)
	{
		int maxlen=0;
		for(int i=0;i<textLength;i++)
		{
			int len=texts[i].length();
			if(len>maxlen)maxlen=len;
		}
		return maxlen;
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.black);
		g.drawRect(0,0,width-1,height-1);
		for(int i=0;i<textLength;i++)g.drawString(texts[i],0,(i+1)*textInterval);
	}
}

class StringQueue
{
	private String stringQueue;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public void enQueue(String string)
	{
		this.stringQueue+=string+";";
	}
	public String deQueue()
	{
		String string="";
		if(stringQueue.length()==0)return string;
		int n=0;
		char c=stringQueue.charAt(n++);
		while(c!=';')
		{
			string+=c;
			c=stringQueue.charAt(n++);
		}
		this.stringQueue=stringQueue.substring(n,stringQueue.length());
		return string;
	}
	public int length()
	{
		int l=0;
		for(int i=0;i<stringQueue.length();i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
		}
		return l;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public String[] getStrings()
	{
		int l=this.length();
		String[] strings=new String[l];
		int n=0,i=0;
		String s="";
		char c;
		while(n<l)
		{
			c=stringQueue.charAt(i++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(i++);
			}
			strings[n++]=s;
			s="";
		}
		return strings;
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}