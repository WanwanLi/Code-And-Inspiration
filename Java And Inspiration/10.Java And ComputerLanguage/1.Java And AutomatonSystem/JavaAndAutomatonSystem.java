import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
public class JavaAndAutomatonSystem
{
	public static void main(String[] args)
	{
		new AutomatonSystem();
	}
}
class AutomatonSystem extends Frame implements ActionListener
{
	private int screenWidth;
	private int screenHeight;
	private int unitWidth;
	private int unitHeight;
	private Automaton automaton;
	private int automatonType;
	private String[] internalStates;
	private String[] inputAlphabet;
	private String[] stackAlphabet;
	private String[][] transitionFunction;
	private int initialState;
	private int stackStartSymbol;
	private int[] finalStates;
	private String[] testInstructions;
	private int testPointer;
	private int beginIndex;
	private int startX,startY,width,height;
	private TextArea textArea_AutomatonScript;
	private TextArea textArea_TestInstructions;
	private Button button_GenerateAutomaton;
	private Button button_Test;
	private Button button_NextState;
	private Button button_InitializeState;
	private String[] automatonScripts;
	private String errorMessage;
	public AutomatonSystem()
	{
		this.screenWidth=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		this.screenHeight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.unitWidth=screenWidth/20;
		this.unitHeight=screenHeight/20;
		this.setSize(screenWidth,screenHeight);
		this.setLayout(null);
		this.addControls();
		this.testPointer=-1;
		this.stackStartSymbol=-1;
		this.getAutomatonFromFile();
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	private void addControls()
	{
		int startX=unitWidth;
		int startY=unitHeight;
		int width=5*unitWidth;
		int height=unitHeight;
		Font font=new Font("",Font.BOLD,15);
		Label Label1=new Label("Input The Script of Automaton Here:");
		Label1.setBounds(startX,startY,width,height);
		Label1.setFont(font);
		this.add(Label1);
		startX+=(width+unitWidth);
		startY+=unitHeight;
		width=12*unitWidth;
		height=17*unitHeight;
		this.startX=startX;
		this.startY=startY;
		this.width=width;
		this.height=height;
		if(automaton!=null)this.automaton.setBounds(startX,startY,width,height);
		startX=unitWidth;
		width=5*unitWidth;
		height=10*unitHeight;
		this.textArea_AutomatonScript=new TextArea("",0,0,TextArea.SCROLLBARS_HORIZONTAL_ONLY);
		this.textArea_AutomatonScript.setBounds(startX,startY,width,height);
		this.textArea_AutomatonScript.setFont(new Font("",Font.BOLD,20));
		this.add(textArea_AutomatonScript);
		startY+=height+unitHeight;
		width=3*unitWidth+unitWidth/2;
		height=unitHeight;
		this.button_GenerateAutomaton=new Button("Generate Automaton");
		this.button_GenerateAutomaton.setBounds(startX,startY,width,height);
		this.button_GenerateAutomaton.addActionListener(this);
		this.button_GenerateAutomaton.setFont(font);
		this.add(button_GenerateAutomaton);
		startX+=width+unitWidth/2;
		width=unitWidth;
		height=unitHeight;
		this.button_Test=new Button("Test");
		this.button_Test.setBounds(startX,startY,width,height);
		this.button_Test.addActionListener(this);
		this.button_Test.setFont(font);
		this.add(button_Test);
		startX=unitWidth;
		startY+=unitHeight;
		width=5*unitWidth;
		height=unitHeight;
		Label Label2=new Label("The Test Instructions is Displayed Here:");
		Label2.setBounds(startX,startY,width,height);
		Label2.setFont(font);
		this.add(Label2);
		startX=unitWidth;
		startY+=unitHeight;
		width=5*unitWidth;
		height=2*unitHeight+unitHeight/2;
		this.textArea_TestInstructions=new TextArea("",0,0,TextArea.SCROLLBARS_HORIZONTAL_ONLY);
		this.textArea_TestInstructions.setBounds(startX,startY,width,height);
		this.textArea_TestInstructions.setFont(new Font("",Font.BOLD,20));
		this.textArea_TestInstructions.setEditable(false);
		this.textArea_TestInstructions.setBackground(Color.white);
		this.add(textArea_TestInstructions);
		startY+=height+unitHeight/2;
		width=2*unitWidth+unitWidth/2;
		height=unitHeight;
		this.button_InitializeState=new Button("Initialize State");
		this.button_InitializeState.setBounds(startX,startY,width,height);
		this.button_InitializeState.addActionListener(this);
		this.button_InitializeState.setFont(font);
		this.add(button_InitializeState);
		startX+=width+unitWidth/2;
		width=2*unitWidth;
		height=unitHeight;
		this.button_NextState=new Button("Next State");
		this.button_NextState.setBounds(startX,startY,width,height);
		this.button_NextState.addActionListener(this);
		this.button_NextState.setFont(font);
		this.add(button_NextState);
	}
	public void actionPerformed(ActionEvent e)
	{
		Object eSource=e.getSource();
		if(eSource.equals(button_GenerateAutomaton))
		{
			this.testPointer=-1;
			this.textArea_TestInstructions.setText("");
			this.generateAutomaton(textArea_AutomatonScript.getText());
		}
		else if(eSource.equals(button_InitializeState))
		{
			if(automaton!=null)
			{
				this.automaton.initializeState();
				this.showCurrentTapes();
				this.repaint();
			}
		}
		else if(eSource.equals(button_NextState))
		{
			if(automaton==null)return;
			if(automatonType==Automaton.TURING_MACHINE)
			{
				this.automaton.transitIntoNextState("");
				this.showCurrentTapes();
				this.repaint();
			}
			else if(testPointer!=-1)
			{
				this.automaton.transitIntoNextState(testInstructions[testPointer]);
				this.testPointer++;
				if(testPointer>=testInstructions.length)
				{
					this.button_Test.setLabel("Test");
					this.testPointer=-1;
				}
				else this.button_Test.setLabel(testInstructions[testPointer]);
				this.setTestInstructions();
				this.repaint();
			}
		}
		else if(eSource.equals(button_Test))
		{
			if(automatonType==Automaton.TURING_MACHINE)return;
			if(automaton!=null&&testInstructions!=null)
			{
				this.testPointer=0;
				this.button_Test.setLabel(testInstructions[testPointer]);
				this.setTestInstructions();
				this.repaint();
			}
		}
	}
	private void showCurrentTapes()
	{
		if(automatonType==Automaton.TURING_MACHINE)
		{
			this.textArea_TestInstructions.setText(automaton.getAllCurrentTapes());
		}
	}
	private void setTestInstructions()
	{
		int l=testInstructions.length;
		String text="";
		for(int i=0;i<l;i++)
		{
			if(i==testPointer)text+=" ["+testInstructions[i]+"] ";
			else text+=" "+testInstructions[i]+" ";
		}
		this.textArea_TestInstructions.setText(text);
	}
	private void getAutomatonFromFile()
	{
		String text="";
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader("Automaton.a"));
			String line=BufferedReader1.readLine();
			while(line!=null)
			{
				text+=line+"\r\n";
				line=BufferedReader1.readLine();
			}
			BufferedReader1.close();
		}
		catch(Exception e){System.err.println(e);}
		if(text==null)
		{
			
			this.internalStates=new String[]{"q0","q1","q2"};
			this.inputAlphabet=new String[]{"a","b","c"};
			this.transitionFunction=new String[][]
			{
				{"a;","b;","c;"},
				{"c;","a;","b;"},
				{"b;","c;","a;"}
			};
			this.initialState=1;
			this.finalStates=new int[]{1,2};
			this.automaton=new Automaton(internalStates,inputAlphabet,transitionFunction,initialState,finalStates);
		}
		else
		{
			this.textArea_AutomatonScript.setText(text);
			this.generateAutomaton(text);
		}
	}
	private void initializeAutomaton()
	{
		this.automatonType=0;
		this.internalStates=null;
		this.inputAlphabet=null;
		this.stackAlphabet=null;
		this.transitionFunction=null;
		this.initialState=-1;
		this.stackStartSymbol=-1;
		this.finalStates=null;
		this.testInstructions=null;
	}
	private void generateAutomaton(String text)
	{
		this.initializeAutomaton();
		this.automatonScripts=this.getStringArray(text);
		int length=automatonScripts.length;
		String errorMessages="";
		for(int i=0;i<length;i++)
		{
			if(automatonScripts[i].equals(""))continue;
			this.beginIndex=0;
			this.errorMessage="";
			String substring=this.getSubstring(automatonScripts[i],'=');
			if(substring.equals("type"))
			{
				String type=this.getSubstring(automatonScripts[i],';');
				if(type.equals("FA"))this.automatonType=Automaton.FINITE_AUTOMATON;
				else if(type.equals("PDA"))this.automatonType=Automaton.PUSHDOWN_AUTOMATON;
				else if(type.equals("TM"))this.automatonType=Automaton.TURING_MACHINE;
				else this.errorMessage+="There is no type of automaton called '"+type+"' in this system...";
			}
			else if(substring.equals("state"))this.internalStates=this.getParameters(automatonScripts[i]);
			else if(substring.equals("input"))this.inputAlphabet=this.getParameters(automatonScripts[i]);
			else if(substring.equals("stack"))this.stackAlphabet=this.getParameters(automatonScripts[i]);
			else if(substring.equals("init"))
			{
				if(internalStates==null)continue;
				String initialState=this.getSubstring(automatonScripts[i],';');
				int init=this.getIndex(initialState,internalStates);
				if(init==-1)this.sendNoStateError(initialState,i+1);
				else this.initialState=init;
			}
			else if(substring.equals("start"))
			{
				if(stackAlphabet==null)continue;
				String startSymbol=this.getSubstring(automatonScripts[i],';');
				int start=this.getIndex(startSymbol,stackAlphabet);
				if(start==-1)this.sendInvalidStackSymbolError(startSymbol,i+1);
				else this.stackStartSymbol=start;
			}
			else if(substring.equals("final"))
			{
				if(internalStates==null)continue;
				String[] finals=this.getParameters(automatonScripts[i]);
				int l=finals.length;
				this.finalStates=new int[l];
				for(int j=0;j<l;j++)
				{
					int finalState=this.getIndex(finals[j],internalStates);
					if(finalState==-1)this.sendNoStateError(finals[j],i+1);
					finalStates[j]=finalState;
				}
			}
			else if(substring.equals("transit"))
			{
				if(internalStates==null||inputAlphabet==null)break;
				if(this.automatonType==Automaton.PUSHDOWN_AUTOMATON)if(stackAlphabet==null)break;
				i++;
				String transit=automatonScripts[i++];
				if(!transit.equals("{"))
				{
					this.setLineNumberForAutomatonScripts(i);
					this.errorMessage+="ERROR At Line "+i+" :There is syntax error that it is supposed to be '{' here...\r\n";
				}
				int l=internalStates.length;
				this.transitionFunction=new String[l][l];
				for(int j=0;j<l;j++)for(int k=0;k<l;k++)this.transitionFunction[j][k]="";
				while(i<length)
				{
					transit=automatonScripts[i++];
					if(transit.equals(""))continue;
					if(transit.equals("}")){i--;break;}
					this.getTransitionFunction(transit,i);
				}
			}
			else if(substring.equals("test"))
			{
				this.testInstructions=this.getParameters(automatonScripts[i]);
				int l=testInstructions.length;
				for(int j=0;j<l;j++)
				{
					int index=this.getIndex(testInstructions[j],inputAlphabet);
					if(index==-1)this.sendNoStateError(testInstructions[j],i+1);
				}
			}
			else this.errorMessage+="ERROR At Line "+(i+1)+" :There is no command like '"+substring+"' in this system...\r\n";
			errorMessages+=errorMessage;
		}
		if(!errorMessages.equals(""))
		{
			this.resetTextArea_AutomatonScript();
			this.textArea_TestInstructions.setText(errorMessages);
			return;
		}
		if(this.automatonType==Automaton.FINITE_AUTOMATON)
		{
			if(internalStates==null)this.sendNoParameterError("Internal States","FA");
			if(inputAlphabet==null)this.sendNoParameterError("Input Alphabet","FA");
			if(transitionFunction==null)this.sendNoParameterError("Transition Function","FA");
			if(initialState==-1)this.sendNoParameterError("Initial State","FA");
			if(finalStates==null)this.sendNoParameterError("Final States","FA");
			if(!errorMessage.equals("")){this.textArea_TestInstructions.setText(errorMessage);return;}
			this.automaton=new Automaton(internalStates,inputAlphabet,transitionFunction,initialState,finalStates);
		}
		else if(this.automatonType==Automaton.PUSHDOWN_AUTOMATON)
		{
			if(internalStates==null)this.sendNoParameterError("Internal States","PDA");
			if(inputAlphabet==null)this.sendNoParameterError("Input Alphabet","PDA");
			if(stackAlphabet==null)this.sendNoParameterError("Stack Alphabet","PDA");
			if(transitionFunction==null)this.sendNoParameterError("Transition Function","PDA");
			if(initialState==-1)this.sendNoParameterError("Initial State","PDA");
			if(stackStartSymbol==-1)this.sendNoParameterError("Stack Start Symbol","PDA");
			if(finalStates==null)this.sendNoParameterError("Final States","PDA");
			if(!errorMessage.equals("")){this.textArea_TestInstructions.setText(errorMessage);return;}
			this.automaton=new Automaton(internalStates,inputAlphabet,stackAlphabet,transitionFunction,initialState,stackStartSymbol,finalStates);
		}
		else if(this.automatonType==Automaton.TURING_MACHINE)
		{
			if(internalStates==null)this.sendNoParameterError("Internal States","TM");
			if(inputAlphabet==null)this.sendNoParameterError("Tape Alphabet","TM");
			if(transitionFunction==null)this.sendNoParameterError("Transition Function","TM");
			if(initialState==-1)this.sendNoParameterError("Initial State","TM");
			if(finalStates==null)this.sendNoParameterError("Final States","TM");
			if(!errorMessage.equals("")){this.textArea_TestInstructions.setText(errorMessage);return;}
			this.automaton=new Automaton(internalStates,inputAlphabet,transitionFunction,initialState,finalStates,this.getOriginalTape());
		}
		else this.textArea_TestInstructions.setText("The type of this automaton is not identified...");
		this.resetTextArea_AutomatonScript();
		this.automaton.setBounds(startX,startY,width,height);
		this.repaint();
	}
	private void resetTextArea_AutomatonScript()
	{
		String text="";
		int l=automatonScripts.length;
		for(int i=0;i<l;i++)text+=automatonScripts[i]+"\r\n";
		this.textArea_AutomatonScript.setText(text);
	}
	private String getOriginalTape()
	{
		if(this.testInstructions==null)return "";
		int l=testInstructions.length;
		String originalTape="";
		for(int i=0;i<l;i++)originalTape+=testInstructions[i]+".";
		return originalTape;
	}
	private void getTransitionFunction(String transit,int line)
	{
		if(this.automatonType==Automaton.FINITE_AUTOMATON)
		{
			this.beginIndex=0;
			this.getSubstring(transit,'(');
			String currentState=this.getSubstring(transit,',');
			int current=this.getIndex(currentState,internalStates);
			if(current==-1){this.sendNoStateError(currentState,line);return;}
			String inputString=this.getSubstring(transit,')');
			int input=this.getIndex(inputString,inputAlphabet);
			if(input==-1){this.sendInvalidInputError(inputString,line);return;}
			this.getSubstring(transit,'=');
			String[] nextStates=this.getParameters(transit);
			int l=nextStates.length;
			for(int i=0;i<l;i++)
			{
				int next=this.getIndex(nextStates[i],internalStates);
				if(next==-1){this.sendNoStateError(nextStates[i],line);return;}
				this.transitionFunction[current][next]+=inputAlphabet[input]+";";
			}
		}
		else if(this.automatonType==Automaton.PUSHDOWN_AUTOMATON)
		{
			this.beginIndex=0;
			this.getSubstring(transit,'(');
			String currentState=this.getSubstring(transit,',');
			int current=this.getIndex(currentState,internalStates);
			if(current==-1){this.sendNoStateError(currentState,line);return;}
			String inputString=this.getSubstring(transit,',');
			int input=this.getIndex(inputString,inputAlphabet);
			if(input==-1){this.sendInvalidInputError(inputString,line);return;}
			String stackTop=this.getSubstring(transit,')');
			int top=this.getIndex(stackTop,stackAlphabet);
			if(top==-1){this.sendInvalidStackSymbolError(stackTop,line);return;}
			this.getSubstring(transit,'=');
			String[] nextStatesAndNewStackTops=this.getParameters(transit);
			int l=nextStatesAndNewStackTops.length;
			for(int i=0;i<l;i++)
			{
				this.beginIndex=0;
				String nextState=this.getSubstring(nextStatesAndNewStackTops[i],'/');
				int next=this.getIndex(nextState,internalStates);
				if(next==-1){this.sendNoStateError(nextState,line);return;}
				String[] newStackTops=this.getDividedStrings(nextStatesAndNewStackTops[i],'.');
				this.transitionFunction[current][next]+=inputAlphabet[input]+","+stackTop+"/"+newStackTops[0];
				if(newStackTops[0].equals("null")){this.transitionFunction[current][next]+=";";continue;}
				else
				{
					int newTop0=this.getIndex(newStackTops[0],stackAlphabet);
					if(newTop0==-1){this.sendInvalidStackSymbolError(newStackTops[0],line);return;}
				}
				for(int j=1;j<newStackTops.length;j++)
				{
					int newTop=this.getIndex(newStackTops[j],stackAlphabet);
					if(newTop==-1){this.sendInvalidStackSymbolError(newStackTops[j],line);return;}
					this.transitionFunction[current][next]+="."+newStackTops[j];
				}
				this.transitionFunction[current][next]+=";";
			}
		}
		else if(this.automatonType==Automaton.TURING_MACHINE)
		{
			this.beginIndex=0;
			this.getSubstring(transit,'(');
			String currentState=this.getSubstring(transit,',');
			int current=this.getIndex(currentState,internalStates);
			if(current==-1){this.sendNoStateError(currentState,line);return;}
			String readString=this.getSubstring(transit,')');
			int read=this.getIndex(readString,inputAlphabet);
			if(read==-1){this.sendInvalidInputError(readString,line);return;}
			this.getSubstring(transit,'=');
			String nextState=this.getSubstring(transit,'/');
			int next=this.getIndex(nextState,internalStates);
			if(next==-1){this.sendNoStateError(nextState,line);return;}
			String writeString=this.getSubstring(transit,',');
			int write=this.getIndex(writeString,inputAlphabet);
			if(write==-1){this.sendInvalidInputError(writeString,line);return;}
			String direction=this.getSubstring(transit,';');
			if(!(direction.equals("->")||direction.equals("<-"))){this.sendInvalidDirectionError(direction,line);return;}
			this.transitionFunction[current][next]+=inputAlphabet[read]+"/"+inputAlphabet[write]+","+direction+";";
		}
	}
	private void setLineNumberForAutomatonScripts(int line)
	{
		this.automatonScripts[line-1]=line+":"+this.automatonScripts[line-1];
	}
	private void sendNoStateError(String state,int line)
	{
		this.setLineNumberForAutomatonScripts(line);
		this.errorMessage+="ERROR At Line "+line+" :There is no state '"+state+"' declared before ...\r\n";
	}
	private void sendInvalidInputError(String inputString,int line)
	{
		this.setLineNumberForAutomatonScripts(line);
		this.errorMessage+="ERROR At Line "+line+" :The Input '"+inputString+"' is invalid becuase it is not declared before...\r\n";
	}
	private void sendInvalidDirectionError(String inputString,int line)
	{
		this.setLineNumberForAutomatonScripts(line);
		this.errorMessage+="ERROR At Line "+line+" :The Direction '"+inputString+"' should be '->' for right and '<-' for left..\r\n";
	}
	private void sendInvalidStackSymbolError(String stackSymbol,int line)
	{
		this.setLineNumberForAutomatonScripts(line);
		this.errorMessage+="ERROR At Line "+line+" :The Stack Symbol '"+stackSymbol+"' is invalid becuase it is not declared before...\r\n";
	}
	private void sendNoParameterError(String parameter,String automatonType)
	{
		this.errorMessage+="ERROR :There is no parameter :"+parameter+" in the "+automatonType+" defined before...\r\n";
	}
	public void paint(Graphics g)
	{
		if(automaton!=null)this.automaton.paint(g);
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
			if(c==':')
			{
				s="";
				c=text.charAt(i++);
			}
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
	private String getSubstring(String string,char endChar)
	{
		int i=beginIndex;
		int l=string.length();
		if(i>=l)return "";
		String substring="";
		char Char=string.charAt(i++);
		while((Char==' '||Char=='\t')&&i<l)Char=string.charAt(i++);
		while(Char!=endChar)
		{
			substring+=Char;
			if(i>=l)break;
			Char=string.charAt(i++);
		}
		this.beginIndex=i;
		return substring;
	}
	private String getSubstring(String string,char[] endChar)
	{
		int i=beginIndex;
		int l=string.length();
		if(i>=l)return "";
		String substring="";
		char Char=string.charAt(i++);
		while((Char==' '||Char=='\t')&&i<l)Char=string.charAt(i++);
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
	private static String[] getSeparatedStrings(String string,char separator)
	{
		int i=0;
		int l=string.length();
		if(i>=l)return null;
		String[] separatedStrings=new String[2];
		separatedStrings[0]=separatedStrings[1]="";
		char Char=string.charAt(i++);
		while(Char!=separator)
		{
			separatedStrings[0]+=Char;
			if(i>=l)break;
			Char=string.charAt(i++);
		}
		while(i<l)
		{
			Char=string.charAt(i++);
			separatedStrings[1]+=Char;
		}
		return separatedStrings;
	}
	private String[] getDividedStrings(String string,char divideChar)
	{
		int l=string.length();
		int i=beginIndex;
		int n=0;
		while(i<l)
		{
			char c=string.charAt(i++);
			if(c==divideChar)n++;
		}
		String[] parameters=new String[n+1];
		i=beginIndex;
		n=0;
		String s="";
		while(i<l)
		{
			char c=string.charAt(i++);
			if(c==divideChar)
			{
				parameters[n++]=s;
				s="";
			}
			else s+=c;
		}
		parameters[n++]=s;
		return parameters;
	}
	private String[] getParameters(String string)
	{
		int l=string.length();
		int i=beginIndex;
		int n=0;
		while(i<l)
		{
			char c=string.charAt(i++);
			if(c==',')n++;
		}
		String[] parameters=new String[n+1];
		i=beginIndex;
		n=0;
		String s="";
		while(i<l)
		{
			char c=string.charAt(i++);
			if(c==',')
			{
				parameters[n++]=s;
				s="";
			}
			else if(c==';')
			{
				parameters[n++]=s;
				s="";
				break;
			}
			else s+=c;
		}
		if(!s.equals(""))parameters[n++]=s;
		return parameters;
	}
	private int getIndex(String string,String[] array)
	{
		int l=array.length;
		for(int i=0;i<l;i++)if(string.equals(array[i]))return i;
		return -1;
	}
}
class Automaton
{
	public static final int FINITE_AUTOMATON=1;
	public static final int PUSHDOWN_AUTOMATON=2;
	public static final int TURING_MACHINE=3;
	private int automatonType;
	private String[] internalStates;
	private String[] inputAlphabet;
	private String[] stackAlphabet;
	private String[][] transitionFunction;
	private int initialState;
	private int stackStartSymbol;
	private int[] finalStates;
	private int[] currentStates;
	private String[] currentStacks;
	private String[] currentTapes;
	private int[] currentPointers;
	private String originalTape;
	private int beginIndex;
	private int startX;
	private int startY;
	private int width;
	private int height;
	private int interval;
	private int diameter;
	private int statesLength;
	private int alphabetLength;
	public Automaton(String[] internalStates,String[] inputAlphabet,String[][] transitionFunction,int initialState,int[] finalStates)
	{
		this.automatonType=FINITE_AUTOMATON;
		this.internalStates=internalStates;
		this.inputAlphabet=inputAlphabet;
		this.transitionFunction=transitionFunction;
		this.initialState=initialState;
		this.finalStates=finalStates;
		this.statesLength=internalStates.length;
		this.alphabetLength=inputAlphabet.length;
		this.currentStates=null;
	}
	public Automaton(String[] internalStates,String[] inputAlphabet,String[] stackAlphabet,String[][] transitionFunction,int initialState,int stackStartSymbol,int[] finalStates)
	{
		this.automatonType=PUSHDOWN_AUTOMATON;
		this.internalStates=internalStates;
		this.inputAlphabet=inputAlphabet;
		this.stackAlphabet=stackAlphabet;
		this.transitionFunction=transitionFunction;
		this.initialState=initialState;
		this.stackStartSymbol=stackStartSymbol;
		this.finalStates=finalStates;
		this.statesLength=internalStates.length;
		this.alphabetLength=inputAlphabet.length;
		this.currentStates=null;
		this.currentStacks=null;
	}
	public Automaton(String[] internalStates,String[] inputAlphabet,String[][] transitionFunction,int initialState,int[] finalStates,String originalTape)
	{
		this.automatonType=TURING_MACHINE;
		this.internalStates=internalStates;
		this.inputAlphabet=inputAlphabet;
		this.transitionFunction=transitionFunction;
		this.initialState=initialState;
		this.finalStates=finalStates;
		this.statesLength=internalStates.length;
		this.alphabetLength=inputAlphabet.length;
		this.originalTape=originalTape;
		this.currentStates=null;
		this.currentTapes=null;
		this.currentPointers=null;
	}
	public void setBounds(int startX,int startY,int width,int height)
	{
		this.startX=startX;
		this.startY=startY;
		this.width=width;
		this.height=height;
		this.diameter=width/(statesLength)/3;
		this.interval=diameter*2;
	}
	public void paint(Graphics g)
	{
		g.drawRect(startX,startY,width,height);
		this.drawAutomaton(g);
	}
	private void drawAutomaton(Graphics g)
	{
		g.setFont(new Font("",Font.BOLD,15));
		int h=height/3/internalStates.length;
		int charWidth=3;
		for(int i=0;i<statesLength;i++)
		{
			int x0=startX+diameter+i*(diameter+interval);
			int y0=startY+height/2-diameter/2;
			if(isInCurrentStates(i))this.drawSphere(g,x0,y0,diameter,isInFinalStates(i));
			g.drawOval(x0,y0,diameter,diameter);
			if(i==initialState)
			{
				if(i==0)
				{
					g.drawString("Start",startX+20,startY+height/2-10);
					this.drawArrow(g,startX+10,startY+height/2,diameter-10,0);
				}
				else
				{
					g.drawString("Start",x0+diameter/2-20,y0+2*diameter-5);
					this.drawArrow(g,x0+diameter/2,y0+2*diameter-20,-(diameter-20),Integer.MAX_VALUE);
				}
			}
			if(isInFinalStates(i))g.drawOval(x0+diameter/8,y0+diameter/8,3*diameter/4,3*diameter/4);
			int l=internalStates[i].length();
			g.drawString(internalStates[i],x0+diameter/2-charWidth*l,y0+diameter/2);
		}
		for(int i=0;i<statesLength;i++)
		{
			for(int j=0;j<statesLength;j++)
			{
				int transitLength=transitionFunction[i][j].length();
				if(transitLength!=0)
				{
					int x0=startX+diameter+i*(diameter+interval);
					int y0=startY+height/2-diameter/2;
					int d=j-i;
					if(d>0)
					{
						int length=interval+(d-1)*(interval+diameter);
						if(d==1)d=0;
						int offset=d*h;
						this.drawArrow(g,x0+diameter,y0+diameter/2,length,offset);
						g.drawString(transitionFunction[i][j],x0+diameter+length/2-charWidth*transitLength,y0+diameter/2-offset-diameter/10);
					}
					else if(d<0)
					{
						int length=-interval+(d+1)*(interval+diameter);
						int offset=d*h;
						this.drawArrow(g,x0,y0+diameter/2,length,offset);
						g.drawString(transitionFunction[i][j],x0+length/2-charWidth*transitLength,y0+diameter/2-offset+diameter/4);
					}
					else
					{
						this.drawArrow(g,x0+diameter,y0+diameter/2,0,0);
						g.drawString(transitionFunction[i][j],x0+diameter/2-charWidth*transitLength,y0-diameter/2-diameter/10);
					}
				}
			}
		}
		if(automatonType==Automaton.PUSHDOWN_AUTOMATON&&currentStacks!=null)
		{
			for(int i=0;i<currentStacks.length;i++)
			{
				int startX=this.startX+50;
				int startY=this.startY+50+i*30;
				g.setFont(new Font("",Font.BOLD,20));
				g.drawString(internalStates[currentStates[i]]+".Stack=[ "+currentStacks[i]+" ]",startX,startY);
			}
		}
	}
	private void drawSphere(Graphics g,int x0,int y0,int diameter,boolean isFinalState)
	{
		int r=diameter/2;
		int x=x0+r;
		int y=y0+r;
		g.setColor(Color.blue);
		for(int i=-r;i<=r;i++)
		{
			for(int j=-r;j<=r;j++)
			{
				double t=(i*i+j*j+0.0)/(r*r);
				if(t<1.0)
				{
					double k=1.0-t;
					int c=(int)(255*k);
					if(isFinalState)
					{
						int b=c+50;
						if(b>255)b=255;
						g.setColor(new Color(c,c,b));
					}
					else g.setColor(new Color(c,c,c));
					g.drawLine(x+i,y+j,x+i,y+j);
				}
			}
		}
		g.setColor(Color.black);
	}
	private void drawArrow(Graphics g,int x0,int y0,int length,int offset)
	{
		int x1=x0+length;
		int xl=sgn(length)*diameter/8;
		int yl=sgn(length)*diameter/20;
		if(abs(xl)<8)xl=8*sgn(xl);
		if(abs(yl)<3)yl=3*sgn(yl);
		int T=10;
		if(offset==Integer.MAX_VALUE)
		{
			g.drawLine(x0,y0,x0,y0+length);
			g.drawLine(x0-yl,y0+length-xl,x0,y0+length);
			g.drawLine(x0+yl,y0+length-xl,x0,y0+length);
		}
		else if(length==0)
		{
			double angle0=-30.0;
			double dAngle=240.0;
			Arc2D arc2D=new Arc2D.Double(x0-diameter,y0-diameter,diameter,diameter,angle0,dAngle,Arc2D.OPEN);
			((Graphics2D)g).draw(arc2D);
			int x=x0-diameter/2;
			int y=y0-diameter;
			g.drawLine(x,y,x+xl,y);
			g.drawLine(x,y-yl,x+xl,y);
			g.drawLine(x,y+yl,x+xl,y);
		}
		else if(offset==0)
		{
			g.drawLine(x0,y0,x1,y0);
			g.drawLine(x1-xl,y0-yl,x1,y0);
			g.drawLine(x1-xl,y0+yl,x1,y0);
		}
		else
		{
			for(int t=-T;t<T;t++)
			{
				double k=(t+0.0)/T;
				int x=x0+length/2+(int)(k*length/2);
				int y=y0-offset+(int)(k*k*offset);
				double K=(t+1.0)/T;
				int X=x0+length/2+(int)(K*length/2);
				int Y=y0-offset+(int)(K*K*offset);
				g.drawLine(x,y,X,Y);
				if(t==0)
				{
					g.drawLine(x,y0-offset,x+xl,y0-offset);
					g.drawLine(x,y0-offset-yl,x+xl,y0-offset);
					g.drawLine(x,y0-offset+yl,x+xl,y0-offset);
				}
			}
		}
	}
	public void initializeState()
	{
		this.currentStates=new int[]{initialState};
		if(automatonType==PUSHDOWN_AUTOMATON)this.currentStacks=new String[]{stackAlphabet[stackStartSymbol]};
		if(automatonType==TURING_MACHINE)
		{
			this.currentTapes=new String[]{originalTape};
			this.currentPointers=new int[]{0};
			return;
		}
		this.addNullTransition();
	}
	public void transitIntoNextState(String transitCondition)
	{
		if(currentStates==null)return;
		int l=currentStates.length;
		StringQueue queue_States=new StringQueue();
		StringQueue queue_Stacks=new StringQueue();
		StringQueue queue_Tapes=new StringQueue();
		StringQueue queue_Pointers=new StringQueue();
		for(int i=0;i<l;i++)
		{
			for(int j=0;j<statesLength;j++)
			{
				String[] transitConditions=this.getTransitConditions(currentStates[i],j);
				if(transitConditions==null)continue;
				if(automatonType==FINITE_AUTOMATON)
				{
					if(isInStringArray(transitCondition,transitConditions))
					{
						if(!queue_States.contain(j+""))queue_States.enQueue(j+"");
					}
				}
				else if(automatonType==PUSHDOWN_AUTOMATON)
				{
					for(int k=0;k<transitConditions.length;k++)
					{
						this.beginIndex=0;
						String inputCondition=this.getSubstring(transitConditions[k],',');
						if(!transitCondition.equals(inputCondition))continue;
						String stackTop=this.getSubstring(transitConditions[k],'/');
						if(this.stackStartWith(currentStacks[i],stackTop))
						{
							String newStackTop=this.getSubstring(transitConditions[k],';');
							String newStack=this.pop(currentStacks[i]);
							if(!newStackTop.equals("null"))newStack=this.push(newStack,newStackTop);
							if(!(queue_Stacks.contain(newStack)&&queue_States.contain(j+"")))
							{
								queue_Stacks.enQueue(newStack);
								queue_States.enQueue(j+"");
							}
						}
					}
				}
				else if(automatonType==TURING_MACHINE)
				{
					for(int k=0;k<transitConditions.length;k++)
					{
						this.beginIndex=0;
						String readString=this.getSubstring(transitConditions[k],'/');
						if(!readString.equals(this.readTape(i)))continue;
						String writeString=this.getSubstring(transitConditions[k],',');
						String newTape=this.writeTape(i,writeString);
						queue_Tapes.enQueue(newTape);
						queue_States.enQueue(j+"");
						String direction=this.getSubstring(transitConditions[k],';');
						int currentPointer=currentPointers[i];
						if(direction.equals("->"))queue_Pointers.enQueue((currentPointer+1+""));
						else if(direction.equals("<-"))queue_Pointers.enQueue((currentPointer-1+""));
					}
				}
			}
		}
		this.currentStates=queue_States.toIntArray();
		this.currentStacks=queue_Stacks.toStringArray();
		this.currentTapes=queue_Tapes.toStringArray();
		this.currentPointers=queue_Pointers.toIntArray();
		this.addNullTransition();
	}
	private void addNullTransition()
	{
		if(currentStates==null)return;
		int l=currentStates.length;
		StringQueue queue_States=new StringQueue();
		StringQueue queue_Stacks=new StringQueue();
		for(int i=0;i<l;i++)
		{
			queue_States.enQueue(currentStates[i]+"");
			if(automatonType==PUSHDOWN_AUTOMATON)queue_Stacks.enQueue(currentStacks[i]);			
			for(int j=0;j<statesLength;j++)
			{
				String[] transitConditions=this.getTransitConditions(currentStates[i],j);
				if(transitConditions==null)continue;
				if(automatonType==FINITE_AUTOMATON)
				{
					if(isInStringArray("null",transitConditions))
					{
						if(!queue_States.contain(j+""))queue_States.enQueue(j+"");
					}
				}
				else if(automatonType==PUSHDOWN_AUTOMATON)
				{
					for(int k=0;k<transitConditions.length;k++)
					{
						this.beginIndex=0;
						String inputCondition=this.getSubstring(transitConditions[k],',');
						if(!inputCondition.equals("null"))continue;
						String stackTop=this.getSubstring(transitConditions[k],'/');
						if(this.stackStartWith(currentStacks[i],stackTop))
						{
							String newStackTop=this.getSubstring(transitConditions[k],';');
							String newStack=this.pop(currentStacks[i]);
							if(!newStackTop.equals("null"))newStack=this.push(newStack,newStackTop);
							if(!(queue_Stacks.contain(newStack)&&queue_States.contain(j+"")))
							{
								queue_Stacks.enQueue(newStack);
								queue_States.enQueue(j+"");
							}
						}
					}
				}	
			}
		}
		this.currentStates=queue_States.toIntArray();
		this.currentStacks=queue_Stacks.toStringArray();
	}
	private boolean stackStartWith(String currentStack,String stackTop)
	{
		int l=currentStack.length();
		int i=0;
		String currentStackTop="";
		while(i<l)
		{
			char c=currentStack.charAt(i++);
			if(c=='.')break;
			currentStackTop+=c;
		}
		return (currentStackTop.equals(stackTop));
	}
	private String pop(String currentStack)
	{
		int l=currentStack.length();
		int i=0;
		while(i<l)
		{
			char c=currentStack.charAt(i++);
			if(c=='.')break;
		}
		if(i==l)return "";
		else return (currentStack.substring(i,l));
	}
	private String push(String currentStack,String newStackTop)
	{
		if(currentStack.equals(""))return newStackTop;
		else return newStackTop+"."+currentStack;
	}
	public String getAllCurrentTapes()
	{
		int l=currentTapes.length;
		String allCurrentTapes="";
		for(int i=0;i<l;i++)
		{
			String state=internalStates[currentStates[i]];
			String[] tapes=this.getStringArrayFromTape(currentTapes[i]);
			int n=tapes.length;
			String currentTape="";
			int pointer=currentPointers[i];
			if(pointer==-1)currentTape+=" [] ";
			for(int j=0;j<n;j++)
			{
				if(j==pointer)currentTape+=" ["+tapes[j]+"] ";
				else currentTape+=" "+tapes[j]+" ";
			}
			if(pointer==n)currentTape+=" [] ";
			allCurrentTapes+=state+".Tape is: "+currentTape+"\r\n";
		}
		return allCurrentTapes;
	}
	private String readTape(int index)
	{
		int pointer=currentPointers[index];
		String tape=currentTapes[index];
		String[] tapeArray=this.getStringArrayFromTape(tape);
		if(pointer==-1||pointer==tapeArray.length)return "[]";
		else return tapeArray[pointer];
	}
	private String writeTape(int index,String writeString)
	{
		int pointer=currentPointers[index];
		String newTape="";
		String tape=currentTapes[index];
		if(pointer==-1)
		{
			currentPointers[index]++;
			return (writeString+"."+tape);
		}
		String[] tapeArray=this.getStringArrayFromTape(tape);
		if(pointer==tapeArray.length)newTape=tape+writeString+".";
		else
		{
			tapeArray[pointer]=writeString;
			newTape=this.getTapeFromStringArray(tapeArray);
		}
		return newTape;
	}
	private String[] getStringArrayFromTape(String tape)
	{
		int l=tape.length();
		int n=0;
		for(int i=0;i<l;i++)
		{
			char c=tape.charAt(i);
			if(c=='.')n++;
		}
		String[] stringArray=new String[n];
		String s="";
		n=0;
		for(int i=0;i<l;i++)
		{
			char c=tape.charAt(i);
			if(c=='.')
			{
				stringArray[n++]=s;
				s="";
			}
			else s+=c;
		}
		return stringArray;
	}
	private String getTapeFromStringArray(String[] array)
	{
		String tape="";
		int l=array.length;
		for(int i=0;i<l;i++) tape+=array[i]+".";
		return tape;
	}
	private String[] getTransitConditions(int from,int to)
	{
		String transitionString=this.transitionFunction[from][to];
		int l=transitionString.length();
		if(l==0)return null;
		int n=0;
		for(int i=0;i<l;i++)
		{
			char c=transitionString.charAt(i);
			if(c==';')n++;
		}
		String[] transitConditions=new String[n];
		String s="";
		n=0;
		for(int i=0;i<l;i++)
		{
			char c=transitionString.charAt(i);
			if(c==';')
			{
				transitConditions[n++]=s;
				s="";
			}
			else s+=c;
		}
		return transitConditions;
	}
	private String getSubstring(String string,char endChar)
	{
		int i=beginIndex;
		int l=string.length();
		if(i>=l)return "";
		String substring="";
		char Char=string.charAt(i++);
		while((Char==' '||Char=='\t')&&i<l)Char=string.charAt(i++);
		while(Char!=endChar)
		{
			substring+=Char;
			if(i>=l)break;
			Char=string.charAt(i++);
		}
		this.beginIndex=i;
		return substring;
	}
	private boolean isInStringArray(String string,String[] array)
	{
		if(string==null||array==null)return false;
		int l=array.length;
		for(int i=0;i<l;i++)if(string.equals(array[i]))return true;
		return false;
	}
	private boolean isInCurrentStates(int q)
	{
		if(currentStates==null)return false;
		int l=currentStates.length;
		for(int i=0;i<l;i++)if(q==currentStates[i])return true;
		return false;
	}
	private boolean isInFinalStates(int q)
	{
		if(finalStates==null)return false;
		int l=finalStates.length;
		for(int i=0;i<l;i++)if(q==finalStates[i])return true;
		return false;
	}
	private int abs(int x)
	{
		return (x<0?-x:x);
	}
	private int sgn(int x)
	{
		return (x>0?1:-1);
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
	public boolean contain(String string)
	{
		int l=this.length();
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
			if(s.equals(string))return true;
			n++;
			s="";
		}
		return false;
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
	public String[] toStringArray()
	{
		int l=this.length();
		String[] array=new String[l];
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
			array[n++]=s;
			s="";
		}
		return array;
	}
	public int[] toIntArray()
	{
		if(length==0)return null;
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