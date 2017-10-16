import java.io.*;
public class JavaAndLexicalAnalyser
{
	public static void main(String[] args)
	{
		String code=getCode("test.javab");
		LexicalAnalyser LexicalAnalyser1=new LexicalAnalyser(code);
		int[] wordTable=LexicalAnalyser1.getWordTable();
		LexicalAnalyser1.printWordTable();
	}
	private static String getCode(String fileName)
	{
		String code="";
		try{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName));
			String s=BufferedReader1.readLine();
			while(s!=null)
			{
				code+=s;
				s=BufferedReader1.readLine();
			}
			BufferedReader1.close();

		}
		catch(Exception e){e.printStackTrace();}
		return code;
	}
}
class LexicalAnalyser
{
	private int index;
	private String code;
	private int length;
	private int[] wordTable;
	private char[] charTable;
	private String[] stringTable;
	private String[] keyWordList;
	private IdentifierList identifierList;
	private Identifier[] identifierTable;
	private StringQueue charQueue;
	private StringQueue stringQueue;
	public LexicalAnalyser(String code)
	{
		this.index=0;
		this.code=code;
		this.length=code.length();
		this.charQueue=new StringQueue();
		this.stringQueue=new StringQueue();
		this.keyWordList=WordList.getKeyWordList();
		this.identifierList=new IdentifierList();
		this.createWordTable();
		this.createCharTable();
		this.createStringTable();
		this.createIdentifierTable();
	}
	private void createWordTable()
	{
		WordList wordList=new WordList();
		this.index=0;
		while(index<length)
		{
			Word word=this.scanNextWord();
			wordList.addWord(word);
		}
		this.wordTable=wordList.getWordTable();
	}
	private void createCharTable()
	{
		this.charTable=this.charQueue.toCharArray();
	}
	private void createStringTable()
	{
		this.stringTable=this.stringQueue.getStrings();
	}
	private void createIdentifierTable()
	{
		this.identifierTable=this.identifierList.getIdentifierTable();
	}
	public String[] getKeyWordList()
	{
		return this.keyWordList;
	}
	public int[] getWordTable()
	{
		return this.wordTable;
	}
	public Identifier[] getIdentifierTable()
	{
		return this.identifierTable;
	}
	public void printWordTable()
	{
		for(int i=0;i<wordTable.length/2;i++)
		{
			int c=wordTable[i*2+0];
			int o=wordTable[i*2+1];
			switch(c)
			{
				case Word.ID:System.out.println(identifierTable[o].Name);break;
				case Word.KEY:System.out.println(keyWordList[o]);break;
				case Word.INT:System.out.println(o);break;
				case Word.CHR:System.out.println(charTable[o]);break;
				case Word.STR:System.out.println(stringTable[o]);break;
				case Word.LOG:System.out.println(getLogicString(o));break;
				case Word.ARI:System.out.println(getArithmeticString(o));break;
				case Word.MRK:System.out.println(getMarkString(o));break;
			}
		}	
	}
	private String getLogicString(int operationType)
	{
		switch(operationType)
		{
			case Word.EQ:return "==";
			case Word.NE:return "!=";
			case Word.LE:return "<=";
			case Word.LT:return "<";
			case Word.GE:return ">=";
			case Word.GT:return ">";
			case Word.AND:return "&&";
			case Word.OR:return "||";
			case Word.NOT:return "!";
		}
		return "null";
	}
	private String getArithmeticString(int operationType)
	{	
		switch(operationType)
		{
			case Word.MOV:return "=";
			case Word.INC:return "++";
			case Word.ADD:return "+";
			case Word.DEC:return "--";
			case Word.SUB:return "-";
			case Word.MUL:return "*";
			case Word.DIV:return "/";
			case Word.AND:return "&";
			case Word.OR:return "|";
		}
		return "null";
	}
	private String getMarkString(int operationType)
	{	
		switch(operationType)
		{	
			case Word.L_PARENTHESE:return "(";
			case Word.R_PARENTHESE:return ")";
			case Word.L_SQRBRACKET:return "[";
			case Word.R_SQRBRACKET:return "]";
			case Word.L_BRACE:return "{";
			case Word.R_BRACE:return "}";
			case Word.DOT:return ".";
			case Word.COMMA:return ",";
			case Word.SEMMIC:return ";";
		}
		return "null";
	}
	private Word scanNextWord()
	{
		String s="";
		char c=code.charAt(index++);
		while(c==' '||c=='\t'||c=='\n'||c=='\r')c=code.charAt(index++);
		if(isAlpha(c))
		{
			while(isAlpha(c))
			{
				s+=c;
				c=code.charAt(index++);
				if(index>=length)break;
			}
			this.index--;
			int n=this.getKeyWordIndex(s);
			if(n==-1){n=this.getIdentifierIndex(s);return new Word(Word.ID,n);}
			else return new Word(Word.KEY,n);
		}
		else if(isDigit(c))
		{
			while(isDigit(c))
			{
				s+=c;
				c=code.charAt(index++);
				if(index>=length)break;
			}
			this.index--;
			return new Word(Word.INT,Integer.parseInt(s));
		}
		else
		{
			switch(c)
			{
				case '=':
				c=code.charAt(index++);
				if(c=='=')return new Word(Word.LOG,Word.EQ);
				else{this.index--;return new Word(Word.ARI,Word.MOV);}
				case '!':
				c=code.charAt(index++);
				if(c=='=')return new Word(Word.LOG,Word.NE);
				else{this.index--;return new Word(Word.LOG,Word.NOT);}
				case '<':
				c=code.charAt(index++);
				if(c=='=')return new Word(Word.LOG,Word.LE);
				else{this.index--;return new Word(Word.LOG,Word.LT);}
				case '>':
				c=code.charAt(index++);
				if(c=='=')return new Word(Word.LOG,Word.GE);
				else{this.index--;return new Word(Word.LOG,Word.GT);}
				case '+':
				c=code.charAt(index++);
				if(c=='+')return new Word(Word.ARI,Word.INC);
				else{this.index--;return new Word(Word.ARI,Word.ADD);}
				case '-':
				c=code.charAt(index++);
				if(c=='-')return new Word(Word.ARI,Word.DEC);
				else{this.index--;return new Word(Word.ARI,Word.SUB);}
				case '*':return new Word(Word.ARI,Word.MUL);
				case '/':return new Word(Word.ARI,Word.DIV);
				case '&':
				c=code.charAt(index++);
				if(c=='&')return new Word(Word.LOG,Word.AND);
				else{this.index--;return new Word(Word.ARI,Word.AND);}
				case '|':
				c=code.charAt(index++);
				if(c=='|')return new Word(Word.LOG,Word.OR);
				else{this.index--;return new Word(Word.ARI,Word.OR);}
				case '(':return new Word(Word.MRK,Word.L_PARENTHESE);
				case ')':return new Word(Word.MRK,Word.R_PARENTHESE);
				case '[':return new Word(Word.MRK,Word.L_SQRBRACKET);
				case ']':return new Word(Word.MRK,Word.R_SQRBRACKET);
				case '{':return new Word(Word.MRK,Word.L_BRACE);
				case '}':return new Word(Word.MRK,Word.R_BRACE);
				case '\'':
				c=code.charAt(index++);
				this.charQueue.enQueue(c);
				c=code.charAt(index++);
				return new Word(Word.CHR,charQueue.length()-1);
				case '\"':
				c=code.charAt(index++);
				while(c!='\"')
				{
					if(c!=';')s+=c;
					c=code.charAt(index++);
					if(index>=length)break;
				}
				this.stringQueue.enQueue(s);
				return new Word(Word.STR,stringQueue.length()-1);
				case '.':return new Word(Word.MRK,Word.DOT);
				case ',':return new Word(Word.MRK,Word.COMMA);
				case ';':return new Word(Word.MRK,Word.SEMMIC);
			}
		}
		return new Word(-1,-1);
	}
	private int getKeyWordIndex(String word)
	{
		int l=this.keyWordList.length;
		for(int i=0;i<l;i++)if(word.equals(keyWordList[i]))return i;
		return -1;
	}
	private int getIdentifierIndex(String word)
	{
		return this.identifierList.getIdentifierIndex(word);
	}
	private boolean isAlpha(char c)
	{
		return ('A'<=c&&c<='Z')||('a'<=c&&c<='z');
	}
	private boolean isDigit(char c)
	{
		return ('0'<=c&&c<='9');
	}
}
class CodeBlock
{
}
class Identifier
{
	public String Name;
	public Identifier Next;
	public int Type;
	public CodeBlock ParentBlock;
	public Identifier(String name)
	{
		this.Name=name;
	}
}
class IdentifierList
{
	private Identifier firstIdentifier;
	private Identifier lastIdentifier;
	private int length;
	public void addIdentifier(String name)
	{
		this.length++;
		if(firstIdentifier==null)
		{
			this.firstIdentifier=new Identifier(name);
			this.lastIdentifier=firstIdentifier;
		}
		else
		{
			if(notExist(name))
			{
				Identifier id=new Identifier(name);
				this.lastIdentifier.Next=id;
				this.lastIdentifier=id;
			}
		}
	}
	public int length()
	{
		return length;
	}
	public Identifier[] getIdentifierTable()
	{
		Identifier[] table=new Identifier[length];
		Identifier id=firstIdentifier;
		for(int i=0;i<length;i++,id=id.Next)table[i]=id;
		return table;
	}
	public int getIdentifierIndex(String name)
	{
		int index=0;
		for(Identifier id=firstIdentifier;id!=null;id=id.Next,index++)
		{
			if(id.Name.equals(name))return index;
		}
		this.addIdentifier(name);
		return this.length-1;
	}
	private boolean notExist(String name)
	{
		for(Identifier id=firstIdentifier;id!=null;id=id.Next)
		{
			if(id.Name.equals(name))return false;
		}
		return true;
	}
}
class Word
{
	public Word Next;
	public int Classification;
	public int OperationType;
	public static final int ID=0;
	public static final int KEY=1;
	public static final int INT=2;
	public static final int LOG=3;
	public static final int ARI=4;
	public static final int MRK=5;
	public static final int CHR=6;
	public static final int STR=7;
	public static final int EQ=30;
	public static final int NE=31;
	public static final int LE=32;
	public static final int LT=33;
	public static final int GE=34;
	public static final int GT=35;
	public static final int AND=36;
	public static final int OR=37;
	public static final int NOT=38;
	public static final int MOV=40;
	public static final int ADD=41;
	public static final int SUB=42;
	public static final int MUL=43;
	public static final int DIV=44;
	public static final int INC=45;
	public static final int DEC=46;
	public static final int L_PARENTHESE=50;
	public static final int R_PARENTHESE=51;
	public static final int L_SQRBRACKET=52;
	public static final int R_SQRBRACKET=53;
	public static final int L_BRACE=54;
	public static final int R_BRACE=55;
	public static final int DOT=56;
	public static final int COMMA=57;
	public static final int SEMMIC=58;
	public Word(int classification,int operationType)
	{
		this.Classification=classification;
		this.OperationType=operationType;
	}
}
class WordList
{
	private Word firstWord;
	private Word lastWord;
	private int length;
	public void addWord(Word word)
	{
		this.length++;
		Word newWord=new Word(word.Classification,word.OperationType);
		if(firstWord==null)
		{

			this.firstWord=newWord;
			this.lastWord=newWord;
		}
		else
		{
			this.lastWord.Next=newWord;
			this.lastWord=newWord;
		}
	}
	public int[] getWordTable()
	{
		int[] table=new int[2*length];
		Word word=firstWord;
		for(int i=0;i<length;i++,word=word.Next)
		{
			table[i*2+0]=word.Classification;
			table[i*2+1]=word.OperationType;
		}
		return table;
	}
	public int length()
	{
		return length;
	}
	public static String[] getKeyWordList()
	{
		StringQueue queue=new StringQueue();
		try{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader("KeyWordList.txt"));
			String s=BufferedReader1.readLine();
			while(s!=null)
			{
				queue.enQueue(s);
				s=BufferedReader1.readLine();
			}
			BufferedReader1.close();

		}
		catch(Exception e){e.printStackTrace();}
		return queue.getStrings();
	}
}
class StringQueue
{
	private String stringQueue;
	private int length;
	public StringQueue()
	{
		this.stringQueue="";
	}
	public void enQueue(char character)
	{
		this.stringQueue+=character;
		this.length++;
	}
	public void enQueue(String string)
	{
		this.stringQueue+=string+";";
		this.length++;
	}
	public void enQueue(int[] array)
	{
		int l=array.length;
		for(int i=0;i<l;i++)this.stringQueue+=array[i]+";";
		this.length+=l;
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
		this.length--;
		return string;
	}
	public int length()
	{
		return this.length;
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
	public char[] toCharArray()
	{
		char[] array=new char[length];
		for(int i=0;i<length;i++)array[i]=stringQueue.charAt(i);
		return array;
	}
	public int[] toIntArray()
	{
		String s="";
		int n=0;
		int[] array=new int[length];
		for(int i=0;i<length;i++)
		{
			char c=stringQueue.charAt(n++);
			while(c!=';')
			{
				s+=c;
				c=stringQueue.charAt(n++);
			}
			array[i]=Integer.parseInt(s);
			s="";
		}
		return array;
	}
	public boolean isNotEmpty()
	{
		return (this.stringQueue.length()>0);
	}
}
