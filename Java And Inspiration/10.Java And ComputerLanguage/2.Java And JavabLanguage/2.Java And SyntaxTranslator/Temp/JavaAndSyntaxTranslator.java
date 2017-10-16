import java.io.*;
public class JavaAndSyntaxTranslator
{
	public static void main(String[] args)
	{
		String code=getCode("test.javab");
		LexicalAnalyser LexicalAnalyser1=new LexicalAnalyser(code);
		String[] keyWordList=LexicalAnalyser1.getKeyWordList();
		int[] wordTable=LexicalAnalyser1.getWordTable();
		Identifier[] identifierTable=LexicalAnalyser1.getIdentifierTable();
		//LexicalAnalyser1.printWordTable();
		SyntaxTranslator SyntaxTranslator1=new SyntaxTranslator(keyWordList,wordTable,identifierTable);
		String[] instructions=SyntaxTranslator1.getInstructions();
		for(int i=0;i<instructions.length;i++)System.out.println(i+": "+instructions[i]);
	}
	private static String getCode(String fileName)
	{
		String code="";
		try
		{
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
class SyntaxTranslator
{
	private StringQueue instructionsQueue;
	private IdentifierList identifierList;
	private String[] instructions;
	private Identifier[] identifiers;
	private int instructionPointer,beginInstructionPointer;
	private int identifierNumber;
	private String[] keyWordList;
	private int[] wordTable;
	private int w0,w1;
	private int index;
	private int length;
	private Identifier[] identifierTable;
	public SyntaxTranslator(String[] keyWordList,int[] wordTable,Identifier[] identifierTable)
	{
		this.keyWordList=keyWordList;
		this.wordTable=wordTable;
		this.identifierTable=identifierTable;
		this.length=wordTable.length/2;
		this.initializeIdentifierList(identifierTable);
		this.instructionsQueue=new StringQueue();
		this.getJavaFlatVirtualMachineInstructions();
		this.instructions=this.instructionsQueue.getStrings();
		this.identifierTable=this.identifierList.getIdentifierTable();
	}
	private void initializeIdentifierList(Identifier[] identifierTable)
	{
		this.identifierList=new IdentifierList();
		for(int i=0;i<identifierTable.length;i++)
		{
			this.identifierList.add(identifierTable[i]);
		}
		this.identifierNumber=identifierTable.length;
	}
	private void getJavaFlatVirtualMachineInstructions()
	{
		while(index<length)
		this.getMethodsInstructions();
	}
	private void getMethodsInstructions()
	{
		this.nextWord();
		if(w0==Word.KEY&&(w1==WordList.Key_int||w1==WordList.Key_public))
		{
			boolean isPublic=(w1==WordList.Key_public)?true:false;
			this.nextWord();
			if(w0==Word.ID)
			{
				this.identifierTable[w1].Type=WordList.Key_public;
				this.identifierTable[w1].Pointer=instructionsQueue.length();
				if(isPublic)this.beginInstructionPointer=identifierTable[w1].Pointer;
				this.nextWord();
				if(w0==Word.MRK&&w1==Word.L_PARENTHESE)
				{
					this.getParametersInstructions();
					this.nextWord();
					if(w0==Word.MRK&&w1==Word.R_PARENTHESE)
					{
						this.nextWord();
						if(w0==Word.MRK&&w1==Word.L_BRACE)
						{
							this.getCodeBlockInstructions();
							if(isPublic)this.instructionsQueue.enQueue("EXIT");
							this.nextWord();
							if(w0==Word.MRK&&w1==Word.R_BRACE){}
							else System.out.println("error");
						}
						else System.out.println("error");
					}
					else System.out.println("error");
				}
				else System.out.println("error");
			}
			else System.out.println("error");
		}
		else System.out.println("error");
	}
	private void getParametersInstructions()
	{	
		this.nextWord();
		if(w0==Word.KEY&&w1==WordList.Key_int)
		{
			this.nextWord();
			if(w0==Word.ID)
			{
				this.instructionsQueue.enQueue("INT &"+w1);
				this.instructionsQueue.enQueue("DEQI &"+w1);
			}
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.COMMA)this.getParametersInstructions();
			else if(w0==Word.MRK&&w1==Word.R_PARENTHESE){this.index--;return;}
			else System.out.println("error");
		}
		else if(w0==Word.MRK&&w1==Word.R_PARENTHESE){this.index--;return;}
	}
	private void getCodeBlockInstructions()
	{
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.R_BRACE){this.index--;return;}
		else if(w0==Word.MRK&&w1==Word.L_BRACE)
		{
			this.getCodeBlockInstructions();
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.R_BRACE){}
			else System.out.println("error");
		}
		else
		{
			this.getDeclarationOrAssignmentExpressionInstructions();
			if(w0==Word.KEY&&w1==WordList.Key_if){this.index--;this.getIfElseExpressionInstructions();}
			else if(w0==Word.KEY&&w1==WordList.Key_while)this.getWhileExpressionInstructions();
			else if(w0==Word.KEY&&w1==WordList.Key_for)this.getForExpressionInstructions();
			else if(w0==Word.KEY&&w1==WordList.Key_return)this.getReturnExpressionInstructions();
			else if(w0==Word.KEY&&w1==WordList.Key_this)this.getThisExpressionInstructions();
		}
		this.getCodeBlockInstructions();
	}
	private void getDeclarationOrAssignmentExpressionInstructions()
	{
		if(w0==Word.KEY&&w1==WordList.Key_int)this.getIntDeclarationInstructions();
		else if(w0==Word.ID&&identifierTable[w1].Type==WordList.Key_int)
		{
			int t0=wordTable[index*2+0];
			int t1=wordTable[index*2+1];
			if(t0==Word.ARI&&(t1==Word.INC||t1==Word.DEC))
			{
				if(t1==Word.INC)this.instructionsQueue.enQueue("INC &"+w1);
				else this.instructionsQueue.enQueue("DEC &"+w1);
				this.index++;
				return;
			}
			if(t0==Word.ARI&&(t1==Word.ACC||t1==Word.DEL))
			{
				String instruction="";
				if(t1==Word.ACC)instruction+="ACC ";
				else instruction+="DEL ";
				instruction+="&"+w1+",";
				this.nextWord();
				String id=this.getAddExpressionInstructions();
				instruction+=id;
				this.instructionsQueue.enQueue(instruction);
				return;
			}
			int k0=wordTable[(index+1)*2+0];
			int k1=wordTable[(index+1)*2+1];
			this.index--;
			if(k0==Word.ID&&identifierTable[k1].Type==WordList.Key_public)this.getMethodAssignmentInstructions();
			else this.getArithmeticAssignmentInstructions();
		}
	}
	private void getIfElseExpressionInstructions()
	{
		this.nextWord();
		if(w0==Word.KEY&&w1==WordList.Key_if)
		{
			this.nextWord();
			String id=this.getAndExpressionInstructions();
			int ip_if=instructionsQueue.length();
			this.index++;
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.L_BRACE)
			{
				this.getCodeBlockInstructions();
				this.nextWord();
				if(w0==Word.MRK&&w1==Word.R_BRACE)
				{
					this.nextWord();
					int ip_else=instructionsQueue.length();
					if(w0==Word.KEY&&w1==WordList.Key_else)
					{
						this.nextWord();
						if(w0==Word.MRK&&w1==Word.L_BRACE)
						{
							this.getCodeBlockInstructions();
							this.nextWord();
							if(w0==Word.MRK&&w1==Word.R_BRACE)
							{
								int ip_end=instructionsQueue.length();
								this.instructionsQueue.insert("JZ "+id+","+(ip_else+2),ip_if);
								this.instructionsQueue.insert("JMP "+(ip_end+2),ip_else+1);
							}
						}
						else if(w0==Word.KEY&&w1==WordList.Key_if)
						{
							this.index--;
							this.instructionsQueue.insert("JZ "+id+","+(ip_else+2),ip_if);
							int ip_begin=instructionsQueue.length();
							this.instructionsQueue.enQueue("NOP");
							this.getIfElseExpressionInstructions();
							int ip_end=instructionsQueue.length();
							this.instructionsQueue.set(ip_begin,"JMP "+ip_end);
						}
					}
					else
					{
						this.instructionsQueue.insert("JZ "+id+","+(ip_else+1),ip_if);
						this.index--;
					}
				}
				else System.out.println("error");
			}
			else System.out.println("error");
		}
	}
	private void getWhileExpressionInstructions()
	{
		int ip_while=instructionsQueue.length();
		this.nextWord();
		String id=this.getAndExpressionInstructions();
		int ip_begin=instructionsQueue.length();
		this.index++;
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.L_BRACE)
		{
			this.getCodeBlockInstructions();
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.R_BRACE)
			{
				int ip_end=instructionsQueue.length();
				String instruction="JZ "+id+","+(ip_end+2);
				this.instructionsQueue.insert(instruction,ip_begin);
				instruction="JMP "+(ip_while+1);
				this.instructionsQueue.enQueue(instruction);
			}
			else System.out.println("error");
		}
		else System.out.println("error");
	}
	private void getForExpressionInstructions()
	{
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.L_PARENTHESE)
		{
			this.nextWord();
			this.getDeclarationOrAssignmentExpressionInstructions();
			String id=this.getAndExpressionInstructions();
			int ip_begin=instructionsQueue.length();
			this.instructionsQueue.enQueue("NOP");
			int l0=instructionsQueue.length();
			this.index++;
			this.nextWord();
			this.getDeclarationOrAssignmentExpressionInstructions();
			int l1=instructionsQueue.length()-1;
			StringQueue q=this.instructionsQueue.deStringQueue(l0,l1);
			this.index++;
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.L_BRACE)
			{
				this.getCodeBlockInstructions();
				this.nextWord();
				if(w0==Word.MRK&&w1==Word.R_BRACE)
				{
					this.instructionsQueue.enStringQueue(q);
					this.instructionsQueue.enQueue("JMP "+(ip_begin-1));
					int ip_end=instructionsQueue.length();
					this.instructionsQueue.set(ip_begin,"JZ "+id+","+ip_end);
				}
				else System.out.println("error");
			}
			else System.out.println("error");
		}
		
	}
	private void getReturnExpressionInstructions()
	{
		String id=this.getAddExpressionInstructions();
		this.instructionsQueue.enQueue("ENQI "+id);
		this.instructionsQueue.enQueue("RET");
		this.index++;
	}
	private void getThisExpressionInstructions()
	{
		this.nextWord();
		if(w0==Word.MRK&&w1==Word.DOT)
		{
			this.nextWord();
			if(w0==Word.ID)
			{
				String thisMethod=identifierTable[w1].Name;
				if(thisMethod.equals("println"))
				{
					this.nextWord();
					String id=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("PRINT "+id);
					this.index++;
				}
				else if(thisMethod.equals("nextInt"))
				{
					this.index++;
					this.nextWord();
					this.instructionsQueue.enQueue("READI &"+w1);
					this.index+=2;
				}
				else if(thisMethod.equals("setColor"))
				{
					this.index++;
					String r=this.getAddExpressionInstructions();
					this.index++;
					String g=this.getAddExpressionInstructions();
					this.index++;
					String b=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("COLOR "+r+","+g+","+b);
					this.index+=2;
				}
				else if(thisMethod.equals("drawLine"))
				{
					this.index++;
					String x0=this.getAddExpressionInstructions();
					this.index++;
					String y0=this.getAddExpressionInstructions();
					this.index++;
					String x1=this.getAddExpressionInstructions();
					this.index++;
					String y1=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("LINE "+x0+","+y0+","+x1+","+y1);
					this.index+=2;
				}
				else if(thisMethod.equals("drawRect"))
				{
					this.index++;
					String x=this.getAddExpressionInstructions();
					this.index++;
					String y=this.getAddExpressionInstructions();
					this.index++;
					String width=this.getAddExpressionInstructions();
					this.index++;
					String height=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("DRECT "+x+","+y+","+width+","+height);
					this.index+=2;
				}
				else if(thisMethod.equals("fillRect"))
				{
					this.index++;
					String x=this.getAddExpressionInstructions();
					this.index++;
					String y=this.getAddExpressionInstructions();
					this.index++;
					String width=this.getAddExpressionInstructions();
					this.index++;
					String height=this.getAddExpressionInstructions();
					this.instructionsQueue.enQueue("FRECT "+x+","+y+","+width+","+height);
					this.index+=2;
				}
			}
		}
	}
	private void getIntDeclarationInstructions()
	{
		int w0=this.wordTable[(index+1)*2+0];
		int w1=this.wordTable[(index+1)*2+1];
		if(w1==Word.COMMA)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_int;
			this.instructionsQueue.enQueue("INT &"+w1);
			this.index+=2;
			this.getIntDeclarationInstructions();
		}
		else if(w1==Word.MOV)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_int;
			this.instructionsQueue.enQueue("INT &"+w1);
			w0=this.wordTable[(index+2)*2+0];
			w1=this.wordTable[(index+2)*2+1];
			if(w0==Word.ID&&identifierTable[w1].Type==WordList.Key_public){this.index--;this.getMethodAssignmentInstructions();}
			else this.getArithmeticAssignmentInstructions();
			this.index++;
			this.getIntDeclarationInstructions();
		}
		else if(w1==Word.SEMICOLON)
		{
			w1=this.wordTable[index*2+1];
			this.identifierTable[w1].Type=WordList.Key_int;
			this.instructionsQueue.enQueue("INT &"+w1);
			this.index+=2;
		}
		else System.out.println("error");
	}
	private void getStringDeclarationInstructions()
	{
		
	}
	private void getMethodAssignmentInstructions()
	{
		String id0="&"+this.wordTable[index*2+1];
		this.index+=2;
		this.w1=this.wordTable[index*2+1];
		int ip=this.identifierTable[w1].Pointer;
		this.index+=2;
		while(true)
		{
			String id=this.getAddExpressionInstructions();
			this.instructionsQueue.enQueue("ENQI "+id);
			this.nextWord();
			if(w0==Word.MRK&&w1==Word.COMMA){}
			else if(w0==Word.MRK&&w1==Word.R_PARENTHESE)break;
			else System.out.println("error");
		}
		this.instructionsQueue.enQueue("CALL "+ip);
		this.instructionsQueue.enQueue("DEQI "+id0);
	}
	private void getArithmeticAssignmentInstructions()
	{
		String id0="&"+this.wordTable[index*2+1];
		index+=2;
		String id1=this.getAddExpressionInstructions();
		String instruction="MOV "+id0+","+id1;
		this.instructionsQueue.enQueue(instruction);
	}
	private String getAddExpressionInstructions()
	{
		String id=this.getMulExpressionInstructions();
		if(wordTable[index*2+1]==Word.ADD||wordTable[index*2+1]==Word.SUB)
		{
			String newID="&"+(identifierNumber++);
			String instruction="ADD "+newID+","+id;
			while(index<length&&wordTable[index*2+1]==Word.ADD||wordTable[index*2+1]==Word.SUB)
			{
				String sgn=(wordTable[index*2+1]==Word.ADD)?"+":"-";
				this.index++;
				id=this.getMulExpressionInstructions();
				instruction+=","+sgn+id;

			}
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getMulExpressionInstructions()
	{
		String id=this.getArithmeticIdentifierValue();
		if(wordTable[index*2+1]==Word.MUL||wordTable[index*2+1]==Word.DIV)
		{
			String newID="&"+(identifierNumber++);
			String instruction="MUL "+newID+","+id;
			while(index<length&&wordTable[index*2+1]==Word.MUL||wordTable[index*2+1]==Word.DIV)
			{
				String sgn=(wordTable[index*2+1]==Word.MUL)?"*":"/";
				this.index++;
				id=this.getArithmeticIdentifierValue();
				instruction+=","+sgn+id;
			}
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getArithmeticIdentifierValue()
	{
		if(wordTable[index*2+0]==Word.ID)
		{
			String id="&"+wordTable[index*2+1];
			this.index++;
			return id;
		}
		else if(wordTable[index*2+0]==Word.INT)
		{
			String id=" "+wordTable[index*2+1];
			this.index++;
			return id;
		}
		else if(wordTable[index*2+1]==Word.L_PARENTHESE)
		{
			this.index++;
			String id=this.getAddExpressionInstructions();
			if(wordTable[index*2+1]==Word.R_PARENTHESE)
			{
				this.index++;
				return id;
			}
			else return "&-1";
		}
		else return "&-1";
	}
	private void getLogicAssignmentInstructions()
	{
		String id0="&"+this.wordTable[index*2+1];
		index+=2;
		String id1=this.getAndExpressionInstructions();
		String instruction="MOV "+id0+","+id1;
		this.instructionsQueue.enQueue(instruction);
	}
	private String getAndExpressionInstructions()
	{
		String id=this.getCmpExpressionInstructions();
		if(wordTable[index*2+1]==Word.AND||wordTable[index*2+1]==Word.OR)
		{
			String newID="&"+(identifierNumber++);
			String instruction="AND "+newID+","+id;
			while(index<length&&wordTable[index*2+1]==Word.AND||wordTable[index*2+1]==Word.OR)
			{
				String sgn=(wordTable[index*2+1]==Word.AND)?"*":"+";
				this.index++;
				id=this.getCmpExpressionInstructions();
				instruction+=","+sgn+id;

			}
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getCmpExpressionInstructions()
	{
		String id=this.getNotExpressionInstructions();
		int OP=wordTable[index*2+1];
		if(OP==Word.EQ||OP==Word.NE||OP==Word.LE||OP==Word.LT||OP==Word.GE||OP==Word.GT)
		{
			String newID="&"+(identifierNumber++);
			this.index++;
			String CMP="";
			switch(OP)
			{
				case Word.EQ:CMP="EQ ";break;
				case Word.NE:CMP="NE ";break;
				case Word.LE:CMP="LE ";break;
				case Word.LT:CMP="LT ";break;
				case Word.GE:CMP="GE ";break;
				case Word.GT:CMP="GT ";break;
			}
			String instruction=CMP+newID+","+id;
			id=this.getNotExpressionInstructions();
			instruction+=","+id;
			this.instructionsQueue.enQueue("INT "+newID);
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return id;
	}
	private String getNotExpressionInstructions()
	{
		if(wordTable[index*2+1]==Word.NOT)
		{
			this.index++;
			String newID="&"+(identifierNumber++);
			String id=this.getLogicIdentifierValue();
			this.instructionsQueue.enQueue("INT "+newID);
			String instruction="NOT "+newID+","+id;
			this.instructionsQueue.enQueue(instruction);
			return newID;
		}
		else return this.getAddExpressionInstructions();
	}
	private String getLogicIdentifierValue()
	{
		if(wordTable[index*2+0]==Word.ID)
		{
			String id="&"+wordTable[index*2+1];
			this.index++;
			return id;
		}
		else if(wordTable[index*2+0]==Word.INT)
		{
			String id=" "+wordTable[index*2+1];
			this.index++;
			return id;
		}
		else if(wordTable[index*2+1]==Word.L_PARENTHESE)
		{
			this.index++;
			String id=this.getAndExpressionInstructions();
			if(wordTable[index*2+1]==Word.R_PARENTHESE)
			{
				this.index++;
				return id;
			}
			else return "&-1";
		}
		else return "&-1";
	}
	private void nextWord()
	{
		this.w0=this.wordTable[index*2+0];
		this.w1=this.wordTable[index*2+1];
		this.index++;
	}
	public String[] getInstructions()
	{
		return this.instructions;
	}
}









class LexicalAnalyser
{
	private int index;
	private String code;
	private boolean isCode;
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
		this.isCode=true;
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
			if(isCode)wordList.addWord(word);
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
	public char[] getCharTable()
	{
		return this.charTable;
	}
	public String[] getStringTable()
	{
		return this.stringTable;
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
			case Word.SEMICOLON:return ";";
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
			while(isAlpha(c)||isDigit(c))
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
				else if(c=='=')return new Word(Word.ARI,Word.ACC);
				else{this.index--;return new Word(Word.ARI,Word.ADD);}
				case '-':
				c=code.charAt(index++);
				if(c=='-')return new Word(Word.ARI,Word.DEC);
				else if(c=='=')return new Word(Word.ARI,Word.DEL);
				else{this.index--;return new Word(Word.ARI,Word.SUB);}
				case '*':
				c=code.charAt(index++);
				if(c=='/'){this.isCode=true;break;}
				else{this.index--;return new Word(Word.ARI,Word.MUL);}
				case '/':
				if(index>=length)break;
				c=code.charAt(index++);
				if(c=='*'){this.isCode=false;break;}
				else{this.index--;return new Word(Word.ARI,Word.DIV);}
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
				case ';':return new Word(Word.MRK,Word.SEMICOLON);
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
	public int Pointer;
	public CodeBlock ParentBlock;
	public Identifier(String name)
	{
		this.Name=name;
	}
	public Identifier(int type)
	{
		this.Name="null";
		this.Type=type;
	}
}
class IdentifierList
{
	private Identifier firstIdentifier;
	private Identifier lastIdentifier;
	private int length;
	public void add(Identifier identifier)
	{
		this.length++;
		if(firstIdentifier==null)
		{
			this.firstIdentifier=identifier;
			this.lastIdentifier=identifier;
		}
		else
		{
			this.lastIdentifier.Next=identifier;
			this.lastIdentifier=identifier;
		}
	}
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
	public static final int ACC=47;
	public static final int DEL=48;
	public static final int L_PARENTHESE=50;
	public static final int R_PARENTHESE=51;
	public static final int L_SQRBRACKET=52;
	public static final int R_SQRBRACKET=53;
	public static final int L_BRACE=54;
	public static final int R_BRACE=55;
	public static final int DOT=56;
	public static final int COMMA=57;
	public static final int SEMICOLON=58;
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
	public static final int Key_int=1;
	public static final int Key_char=2;
	public static final int Key_String=3;
	public static final int Key_void=4;
	public static final int Key_public=5;
	public static final int Key_new=6;
	public static final int Key_for=7;
	public static final int Key_while=8;
	public static final int Key_if=9;
	public static final int Key_else=10;
	public static final int Key_return=11;
	public static final int Key_this=12;
	public static String[] getKeyWordList()
	{
		return new String[]
		{
			"null",
			"int",
			"char",
			"String",
			"void",
			"public",
			"new",
			"for",
			"while",
			"if",
			"else",
			"return",
			"this"
		};
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
	public StringQueue(String queue)
	{
		this.stringQueue=queue;
		this.length=this.getLength();
		if(length==0)
		{
			this.stringQueue+=";";
			this.length=1;
		}
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
	public void insert(String string,int index)
	{
		this.length++;
		if(index>=length)
		{
			this.stringQueue+=string+";";
			return;
		}
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(i,Length);
		this.stringQueue=s0+";"+string+s1;
	}
	public void set(int index,String string)
	{
		if(index>=length)return;
		int i=0,j=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==index)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringQueue.charAt(j);
			if(c==';')break;
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(j,Length);
		this.stringQueue=s0+";"+string+s1;
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
	private int getLength()
	{
		int i=0,l=0,Length=stringQueue.length();
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')l++;
		}
		return l;
	}
	public int length()
	{
		return this.length;
	}
	public void show()
	{
		System.out.println(stringQueue);
	}
	public String getStringQueue()
	{
		String queue="";
		queue+=stringQueue;
		return queue;
	}
	public void enStringQueue(StringQueue queue)
	{
		this.stringQueue+=queue.getStringQueue();
		this.length+=queue.length();
	}
	public StringQueue deStringQueue(int begin,int end)
	{
		int i=0,j=0,l=0,Length=stringQueue.length();
		String queue="";
		for(i=0;i<Length;i++)
		{
			char c=stringQueue.charAt(i);
			if(c==';')
			{
				l++;
				if(l==begin)break;
			}
		}
		for(j=i+1;j<Length;j++)
		{
			char c=stringQueue.charAt(j);
			queue+=c;
			if(c==';')
			{
				l++;
				if(l>end)break;
			}
		}
		String s0=stringQueue.substring(0,i);
		String s1=stringQueue.substring(j,Length);
		this.stringQueue=s0+s1;
		this.length-=(end-begin+1);
		return new StringQueue(queue);
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