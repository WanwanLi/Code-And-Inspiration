package com.android.project;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageSwitcher;
import android.content.res.Resources;

class TianShen
{
	int image;
	int[] integers;
	EditText editText;
	TextView textView;
	boolean isChinese;
	Resources resources;
	MainActivity mainActivity;
	ImageSwitcher imageSwitcher;
	public JunChenSha junChenSha;
	public TianShen(boolean isChinese, EditText editText, TextView textView,Resources resources, ImageSwitcher imageSwitcher, MainActivity mainActivity)
	{
		this.imageSwitcher=imageSwitcher;
		this.mainActivity=mainActivity;
		this.resources=resources;
		this.isChinese=isChinese;
		this.textView=textView;
		this.editText=editText;
	}
	public void askPlayersTotalNumber(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)
		{
			if(player==junChenSha.ALL)print(resources.getString(R.string.total_num));
			else print(resources.getString(R.string.how_many)+players+"?");
		}
		else println("How many players are "+players+" ?");
	}
	public void askPlayersChairNumber(int player, int number)
	{
		String players=getPlayersName(player);
		if(number>1)
		{
			if(isChinese)print(number+" "+players+resources.getString(R.string.chair_nums));
			else print("What are the chair No.s of "+number+" "+players);
		}
		else
		{
			if(isChinese)print(players+resources.getString(R.string.chair_num));
			else print("What is the chair No. of "+players);
		}
	}
	public void askPlayersToInputSomeNumbers()
	{
		if(isChinese)makeText(resources.getString(R.string.input_some));
		else
		{
			makeText
			(
				"Give me some integers: \n"+
				"(use comma to seperate)"
			);
		}
	}
	public void askPlayersToOpenEyes(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)println(players+resources.getString(R.string.open_eyes));
		else println(players + ", Please open your eyes!");
	}
	public void askPlayersToCloseEyes(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)print(players + resources.getString(R.string.close_eyes));
		else print(players + ", Please close your eyes!");
	}
	public void askPlayersToGuard(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)print(players + resources.getString(R.string.guard));
		else print(players + ", please chose someone to be guarded:");
	}
	public void askPlayersToRevenge(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)print(players + resources.getString(R.string.revenge));
		else print(players + ", please chose someone to fight back:");
	}
	public void askPlayersToMurder(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)print(players + resources.getString(R.string.murder));
		else print(players + ", please chose someone to be murdered:");
	}
	public void askPlayersToFindPrime(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)
		{
			println(players + resources.getString(R.string.find_prime));
			print
			(
				resources.getString(R.string.other)+players+
				resources.getString(R.string.close_eyes)
			);
		}
		else 
		{
			println(players + ", please chose someone to be your prime.");
			print("Other "+players+", please close your eyes.");
		}
	}
	public void askPlayersToRescue(int player, int murderedPlayer)
	{
		String players=getPlayersName(player);
		if(isChinese)
		{
			println(players +resources.getString(R.string.murdered)+murderedPlayer);
			print(resources.getString(R.string.rescue));
		}
		else 
		{
			println(players + ", the player in danger is this ("+murderedPlayer+").");
			print("You have one medicine, do you want to use it(save press 1, otherwise 0)?");
		}
	}
	public void askPlayersToPoison(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)print(players + resources.getString(R.string.poison));
		else 
		{
			println(players + " You have one poison, do you want to use it?(if don\'t use, press 0)");
			print("If use, please chose someone to be poisoned:");
		}
	}
	public void askPlayersToCheck(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)print(players + resources.getString(R.string.check));
		else print(players + ", whose identity do you want to check? ");
	}
	public void declareTheIdentity(int player, boolean isLoyal)
	{
		String players=getPlayersName(player);
		if(isChinese)
		{
			println
			(
				players + resources.getString(R.string.identity)+
				(
					isLoyal?resources.getString(R.string.up):
					resources.getString(R.string.down)
				)
			);
		}
		else 
		{
			println(players + ", good person is Up,  bad person is Down, ");
			println("the perosn you checked is:("+(isLoyal?"Up)":"Down)"));
		}
	}
	public void declareTheDeath(int diedPlayer)
	{
		if(isChinese)print(diedPlayer+resources.getString(R.string.died));
		else print("No."+diedPlayer+" was killed. ");
	}
	public void declareThePeace()
	{
		if(isChinese)print(resources.getString(R.string.peace));
		else print("Yesterday was a silent night!");
	}
	public void askPlayersToFindNewKing(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)print(players + resources.getString(R.string.new_king));
		else print(players + ", you were killed last night, who could be the new king? ");
	}
	public void askPlayersToSentence(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)print(players + resources.getString(R.string.sentence));
		else 
		{
			print(players + ", Please specify someone to start thier speakings one by one,");
			print("After all of them finished thier speakings, please sentence some one to death:");
		}
	}
	public void declareTheWinner(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)print(resources.getString(R.string.winner)+players);
		else print("Game is over! "+players + " is the winner!");
	}
	public void showAllPlayers(int[] players, int king, int prime)
	{
		String text=isChinese?resources.getString(R.string.all_players):
		"The charaters of all the players are:"; 
		String King=isChinese?resources.getString(R.string.king):"King";
		String Prime=isChinese?resources.getString(R.string.prime):"Prime";
		for(int i=1; i<players.length; i++)
		{
			text+="\n";
			String isDied="", isKing, isPrime, player;
			isKing=i==king?("("+King+")"):"";
			isPrime=i==prime?("("+Prime+")"):"";
			if(players[i]<0)
			{
				player=getPlayersName(-players[i]);
				isDied=isChinese?resources.getString(R.string.is_died):"(Is Died)";
			}
			else player=getPlayersName(players[i]);	
			if(isChinese)text+="["+i+resources.getString(R.string.player)+player+isKing+isPrime+isDied;
			else text+=("The charater of player ["+i+"] is: "+player+isKing+isPrime+isDied);
		}
		this.setText(text);
	}
	public int updateImage(int player)
	{
		int image=this.image;
		if(player==junChenSha.ALL)this.image=R.drawable.tian_shen;
		else if(player==junChenSha.KING)this.image=R.drawable.king;
		else if(player==junChenSha.SOLDIER)this.image=R.drawable.soldier;
		else if(player==junChenSha.GENERAL)this.image=R.drawable.general;
		else if(player==junChenSha.MINISTER)this.image=R.drawable.minister;
		else if(player==junChenSha.TRAITORS)this.image=R.drawable.traitor;
		else if(player==junChenSha.REBELS)this.image=R.drawable.rebel;
		else if(player==junChenSha.NONE)this.image=R.drawable.none;
		this.setImage(this.image);
		return image;
	}
	public int getInt()
	{
		return this.integers[0];
	}
	public int[] getInts()
	{
		return this.integers;
	}
	public String getText()
	{
		String text=this.editText.getText().toString();
		if(text.length()==0)text="0";
		this.editText.setText("");
		return text;
	}
	public void setText(String string)
	{
		this.editText.setText(string);
	}
	public void makeText(String string)
	{
		this.mainActivity.makeText(string);
	}
	public void print(String string)
	{
		this.textView.setText(textView.getText()+string);
	}
	public void println(String string)
	{
		this.textView.setText(textView.getText()+string+"\n");
	}
	public void clear()
	{
		this.textView.setText("");
	}
	public void setImage(int image)
	{
		this.image=image;
		this.imageSwitcher.setImageResource(image);
		this.imageSwitcher.setImageResource(image);
	}
	public boolean nextInt(int length)
	{
		String[] values=getValues();
		if(values.length!=length)
		{
			int player=junChenSha.getPlayersName(length);
			printNotMatchErrorMessage(player); 
			askPlayersToInputSomeNumbers();
			return false;
		}
		this.integers=new int[length];
		for(int i=0; i<length; i++)
		{
			try{this.integers[i]=Integer.parseInt(values[i]);}
			catch(Exception e){printInputErrorMessage();return false;}
			if(isInvalidPlayer(integers[i]))return false;
		}
		return true;
	}
	String[] getValues()
	{
		String values=this.getText();
		if(values.length()==0)values="0";
		values=values.replace(" ", "");
		values=values.replace(".", ",");
		return values.split(",");
	}
	boolean isInvalidPlayer(int player)
	{
		if(junChenSha.isInvalid(player))
		{
			this.printInputErrorMessage();
			return true;
		}
		else if(junChenSha.isDied(player))
		{
			this.printInputAgainMessage(player);
			return true;
		}
		else return false;
	}
	void printInputErrorMessage()
	{
		if(isChinese)makeText(resources.getString(R.string.input_error));
		else makeText("Input error, please input again!");
	}
	void printInputAgainMessage(int player)
	{
		if(isChinese)makeText(player+resources.getString(R.string.input_again));
		else makeText("The player " +player+ " is died, please input again!");
	}
	void printNotMatchErrorMessage(int player)
	{
		String players=getPlayersName(player);
		if(isChinese)makeText(players+resources.getString(R.string.not_match));
		else makeText("The number of "+players+" deosn\'t macth the numbers of integers input");
	}
	String getPlayersName(int player)
	{
		if(player==junChenSha.ALL) return isChinese?resources.getString(R.string.all):"All People";
		else if(player==junChenSha.KING) return isChinese?resources.getString(R.string.king):"King";
		else if(player==junChenSha.QUEEN) return isChinese?resources.getString(R.string.queen):"Queen";
		else if(player==junChenSha.SOLDIER) return isChinese?resources.getString(R.string.soldier):"Soldier";
		else if(player==junChenSha.GENERAL) return isChinese?resources.getString(R.string.general):"General";
		else if(player==junChenSha.MINISTER) return isChinese?resources.getString(R.string.minister):"Minister";
		else if(player==junChenSha.VILLAGERS) return isChinese?resources.getString(R.string.villagers):"Villagers";
		else if(player==junChenSha.TRAITORS) return isChinese?resources.getString(R.string.traitors):"Traitors";
		else if(player==junChenSha.REBELS) return isChinese?resources.getString(R.string.rebels):"Rebels";
		else return "null";
	}
}