import java.io.*;
import java.util.*;

public class JavaAndPasswordPrologSystem
{
	public static void main(String[] args)
	{
		PasswordPrologSystem passwordPrologSystem=new PasswordPrologSystem(args[0]);
		while(!passwordPrologSystem.resetPassword())
		{
			passwordPrologSystem.printQuestion();
			passwordPrologSystem.scanAnswer();
		}
	}
}
class PasswordPrologSystem
{
	int time=0;
	String category;
	String question;
	Scanner scanner;
	int score, minScore=6;
	LinkedList<int[]> indexList;
	LinkedList<Integer> scoreList;
	Hashtable<String, Boolean> questionTable;
	LinkedList<String> categoryList, questionList, answerList;
	Hashtable<String, Hashtable<String, Integer>> scoreTable;
	Hashtable<String, Hashtable<String, String>> answerTable;
	public PasswordPrologSystem(String fileName)
	{
		this.loadFile(fileName);
		this.scanner=new Scanner(System.in);
		this.questionTable=new Hashtable<String, Boolean>();
		this.scoreTable=new Hashtable<String, Hashtable<String, Integer>>();
		this.answerTable=new Hashtable<String, Hashtable<String, String>>();
		for(int i=0; i<categoryList.size(); i++)
		{
			this.scoreTable.put(categoryList.get(i), this.createScoreTable(indexList.get(i)));
			this.answerTable.put(categoryList.get(i), this.createAnswerTable(indexList.get(i)));
		}
	}
	void loadFile(String fileName)
	{
		this.indexList=new LinkedList<int[]>();
		this.scoreList=new LinkedList<Integer>();
		this.answerList=new LinkedList<String>();
		this.questionList=new LinkedList<String>();
		this.categoryList=new LinkedList<String>();
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName)); int line=0, lines;
			for(String string; (string=BufferedReader1.readLine())!=null; line+=lines)
			{
				String[] s=string.split("\t");
				this.categoryList.add(s[0]);
				lines=Integer.parseInt(s[1]);
				this.indexList.add(new int[]{line, line+lines});
				for(int i=0; i<lines; i++)
				{
					s=BufferedReader1.readLine().split("\t");
					this.questionList.add(s[0]);
					this.answerList.add(s[1]);
					this.scoreList.add(Integer.parseInt(s[2]));
				}
			}
			BufferedReader1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	void getQuestion()
	{
		int i=random(categoryList.size());
		this.category=categoryList.get(i);
		this.question=questionList.get(random(indexList.get(i)));
		if(questionTable.get(question)==null)
		{
			this.questionTable.put(question, true);
		}
		else if(questionTable.get(question))this.getQuestion();
	}
	public void printQuestion()
	{
		this.getQuestion();
		System.out.print("What is: "+question+"? Your answer is: ");
	}
	public void scanAnswer()
	{
		String answer=this.scanner.nextLine();
		if(answer.equals(answerTable.get(category).get(question)))
		{
			int score=scoreTable.get(category).get(question).intValue();
			System.out.println("Correct! You have got "+score+" score!");
			this.score+=score;
		}
		else System.out.println("Sorry! Your answer is incorrect!");
	}
	public boolean resetPassword()
	{
		if(++this.time>questionList.size())
		{
			System.out.println("You could not be able to reset your password!"); return true;
		}
		if(score>=minScore)
		{
			System.out.println("You could be able to reset your password!"); return true;
		}
		return false;
	}
	Hashtable<String, String> createAnswerTable(int[] index)
	{	
		Hashtable<String, String> hashtable1=new Hashtable<String, String>();
		for(int i=index[0]; i<index[1]; i++)
		{
			hashtable1.put(questionList.get(i), answerList.get(i)); 
		}
		return hashtable1;
	}
	Hashtable<String, Integer> createScoreTable(int[] index)
	{	
		Hashtable<String, Integer> hashtable1=new Hashtable<String, Integer>();
		for(int i=index[0]; i<index[1]; i++)
		{
			hashtable1.put(questionList.get(i), scoreList.get(i)); 
		}
		return hashtable1;
	}
	int random(int max)
	{
		return random(0, max);
	}
	int random(int[] range)
	{
		return random(range[0], range[1]);
	}
	int random(int min, int max)
	{
		return min+(int)((max-min)*Math.random());
	}
}