import java.util.Scanner;
public class $JunChenSha
{
	public static void main(String[] args)
	{
		boolean isChinese=false;
		TianShen tianShen=new TianShen(isChinese, 10);
		JunChenSha junChenSha = new JunChenSha(tianShen);
		junChenSha.show();
		while(true)
		{
			if(tianShen.nextInt(false))
			{
				int cmd=tianShen.getInt();
				if(cmd==1)junChenSha.next();
				else if(cmd==0)junChenSha.prev();
				else tianShen.printInputErrorMessage();
			}
			else tianShen.printInputErrorMessage();
		}
	}
}
class JunChenSha
{
	public final int ALL=0;
	public final int KING=1;
	public final int QUEEN=2;
	public final int SOLDIER=3;
	public final int GENERAL=4;
	public final int MINISTER=5;
	public final int VILLAGERS=6;
	public final int TRAITORS=7;
	public final int REBELS=8;
	public final int ERROR=-1;
	final int GAME_OVER=-1;
	final int GET_TOTAL_NUM=0;
	final int GET_REBEL_NUM=1;
	final int GET_TRAITOR_NUM=2;
	final int GET_GENERAL_INFO=3;
	final int GET_GUARDED_PLAYER=4;
	final int GET_REVENGED_PLAYER=5;
	final int GET_REBELS_INFO=6;
	final int GET_MURDERED_PLAYER=7;
	final int GET_TRAITORS_INFO=8;
	final int GET_TRAITOR_PLAYER=9;
	final int GET_RESCUED_PLAYER=10;
	final int GET_POISONED_PLAYER=11;
	final int GET_KING_INFO=12;
	final int GET_SOLDIER_INFO=13;
	final int GET_MINISTER_INFO=14;
	final int GET_CHECKED_PLAYER=15;
	final int GET_DIED_PLAYER=16;
	final int GET_NEW_KING_INFO=17;
	final int GET_SENTENCED_PLAYER=18;
	int status;
	int winner, king;
	TianShen tianShen;
	boolean[] isGuarded;
	int[]  players, medicines, poisons;
	int aliveRebel, aliveTraitor, aliveTotal;
	int rebelNum, traitorNum, totalNum, roleNum=8;
	int guardedPlayer, revengedPlayer, murderedPlayer;
	int traitorPlayer, rescuedPlayer, poisonedPlayer, checkedPlayer, sentencedPlayer;
	boolean isNotFirstNight, isSilentNight, isKingAlive;
	public JunChenSha(TianShen tianShen)
	{
		this.tianShen=tianShen;
		this.tianShen.junChenSha=this;
		this.status=GET_TOTAL_NUM;
		this.isNotFirstNight=false;
	}
	public void next()
	{
		if(this.nextInt())
		{
			if(status==GET_DIED_PLAYER)
			{
				if(isGameOver())
				{
					this.status=GAME_OVER;
					this.show();
					return;
				}
			}
			if(status==GET_SENTENCED_PLAYER)
			{
				if(isGameOver())this.status=GAME_OVER;
				else this.status=GET_GUARDED_PLAYER;
				this.isNotFirstNight=true;
			}
			else
			{
				this.status++;
				while(skip())this.status++;
			}
		}
		this.show();
	}
	public void prev()
	{
		if(this.status!=GET_TOTAL_NUM)
		{
			this.status--;
			while(skip())this.status--;
		}
		if(isNotFirstNight)
		{
			this.status=max(status, GET_GUARDED_PLAYER);
		}
		this.show();
	}
	public void show()
	{
		if(status==GAME_OVER)
		{
			tianShen.declareTheWinner(winner);
		}
		else if(status==GET_TOTAL_NUM)
		{
			tianShen.askPlayersTotalNumber(ALL);
		}
		else if(status==GET_REBEL_NUM)
		{
			tianShen.askPlayersTotalNumber(REBELS);
		}
		else if(status==GET_TRAITOR_NUM)
		{
			tianShen.askPlayersTotalNumber(TRAITORS);
		}
		else if(status==GET_GENERAL_INFO)
		{
			tianShen.askPlayersToCloseEyes(ALL);
			tianShen.askPlayersToOpenEyes(GENERAL);
			tianShen.askPlayersChairNumber(false);
		}
		else if(status==GET_GUARDED_PLAYER)
		{
			if(isNotFirstNight)
			{
				tianShen.askPlayersToCloseEyes(ALL);
				tianShen.askPlayersToOpenEyes(GENERAL);
			}
			tianShen.askPlayersToGuard();
		}
		else if(status==GET_REVENGED_PLAYER)
		{
			tianShen.askPlayersToRevenge();
		}
		else if(status==GET_REBELS_INFO)
		{
			tianShen.askPlayersToCloseEyes(GENERAL);
			tianShen.askPlayersToOpenEyes(REBELS);
			tianShen.askPlayersChairNumber(true);
		}
		else if(status==GET_MURDERED_PLAYER)
		{
			if(isNotFirstNight)
			{
				tianShen.askPlayersToCloseEyes(GENERAL);
				tianShen.askPlayersToOpenEyes(REBELS);
			}
			tianShen.askPlayersToMurder();
		}
		else if(status==GET_TRAITORS_INFO)
		{
			tianShen.askPlayersToCloseEyes(REBELS);
			tianShen.askPlayersToOpenEyes(TRAITORS);
			tianShen.askPlayersChairNumber(true);
		}
		else if(status==GET_TRAITOR_PLAYER)
		{
			if(isNotFirstNight)
			{
				tianShen.askPlayersToCloseEyes(REBELS);
				tianShen.askPlayersToOpenEyes(TRAITORS);
			}
			tianShen.askPlayersToFindRepresentitive();
		}
		else if(status==GET_RESCUED_PLAYER)
		{
			tianShen.askPlayersToRescue(murderedPlayer);
		}
		else if(status==GET_POISONED_PLAYER)
		{
			tianShen.askPlayersToPoison();
		}
		else if(status==GET_KING_INFO)
		{
			tianShen.askPlayersToCloseEyes(TRAITORS);
			tianShen.askPlayersToOpenEyes(KING);
			tianShen.askPlayersChairNumber(false);
		}
		else if(status==GET_SOLDIER_INFO)
		{
			tianShen.askPlayersToCloseEyes(KING);
			tianShen.askPlayersToOpenEyes(SOLDIER);
			tianShen.askPlayersChairNumber(false);
		}
		else if(status==GET_MINISTER_INFO)
		{
			tianShen.askPlayersToCloseEyes(SOLDIER);
			tianShen.askPlayersToOpenEyes(MINISTER);
			tianShen.askPlayersChairNumber(false);
		}
		else if(status==GET_CHECKED_PLAYER)
		{
			if(isNotFirstNight)
			{
				tianShen.askPlayersToCloseEyes(TRAITORS);
				tianShen.askPlayersToOpenEyes(MINISTER);
			}
			tianShen.askPlayersToCheck();
		}
		else if(status==GET_DIED_PLAYER)
		{
			this.getDiedPlayer(); this.countAlivePlayers();
			tianShen.declareTheIdentity(isLoyal(checkedPlayer));
			tianShen.askPlayersToCloseEyes(MINISTER);
			tianShen.askPlayersToOpenEyes(ALL);
			this.declareTheDeaths();
			this.showAllPlayers();
		}
		else if(status==GET_NEW_KING_INFO)
		{
			tianShen.askPlayersToFindNewKing(KING);
		}
		else if(status==GET_SENTENCED_PLAYER)
		{
			tianShen.askPlayersToSentence(KING);
		}
		tianShen.askPlayersToContinue(5);
	}
	boolean nextInt()
	{
		boolean isMultiple=isMultiplePlayers();
		tianShen.askPlayersToInputNumber(isMultiple);
		if(!tianShen.nextInt(isMultiple))
		{
			tianShen.printInputErrorMessage();
			return false;
		}
		if(status==GET_TOTAL_NUM)
		{
			this.totalNum = tianShen.getInt();
			this.initPlayers();
		}
		else if(status==GET_REBEL_NUM)
		{
			this.rebelNum = tianShen.getInt();
		}
		else if(status==GET_TRAITOR_NUM)
		{
			this.traitorNum = tianShen.getInt();
		}
		else if(status==GET_GENERAL_INFO)
		{
			if(this.getCharacter(GENERAL)==ERROR)
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_GUARDED_PLAYER)
		{
			this.guardedPlayer=tianShen.getInt();
			if(isInvalid(guardedPlayer))
			{
				tianShen.printInputErrorMessage();
				return false;
			}
			if(isGuarded[guardedPlayer])this.guardedPlayer=0;
		}
		else if(status==GET_REVENGED_PLAYER)
		{
			this.revengedPlayer=tianShen.getInt();
			if(isInvalid(revengedPlayer))
			{
				tianShen.printInputErrorMessage();
				return false;
			}
			if(isNotAlive(GENERAL))guardedPlayer=revengedPlayer=0;
		}
		else if(status==GET_REBELS_INFO)
		{
			if(!this.getCharacters(REBELS, rebelNum))
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_MURDERED_PLAYER)
		{
			this.murderedPlayer=tianShen.getInt();
			if(isInvalid(murderedPlayer))
			{
				tianShen.printInputErrorMessage();
				return false;
			}
			if(murderedPlayer==guardedPlayer)this.murderedPlayer=0;
			if(isNotAlive(REBELS))this.murderedPlayer=0;
		}
		else if(status==GET_TRAITORS_INFO)
		{
			if(!this.getCharacters(TRAITORS, traitorNum))
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_TRAITOR_PLAYER)
		{
			this.traitorPlayer=tianShen.getInt();
			if(isInvalid(traitorPlayer))
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_RESCUED_PLAYER)
		{
			this.rescuedPlayer=tianShen.getInt();
			if(rescuedPlayer>1)
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_POISONED_PLAYER)
		{
			this.poisonedPlayer=tianShen.getInt();
			if(isInvalid(poisonedPlayer))
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_KING_INFO)
		{
			this.king=this.getCharacter(KING);
			if(king==ERROR)
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_SOLDIER_INFO)
		{
			if(this.getCharacter(SOLDIER)==ERROR)
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_MINISTER_INFO)
		{
			if(this.getCharacter(MINISTER)==ERROR)
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_CHECKED_PLAYER)
		{
			this.checkedPlayer=tianShen.getInt();
			if(isInvalid(checkedPlayer))
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_NEW_KING_INFO)
		{
			this.king=tianShen.getInt();
			if(isInvalid(king))
			{
				tianShen.printInputErrorMessage();
				return false;
			}
		}
		else if(status==GET_SENTENCED_PLAYER)
		{
			this.sentencedPlayer=tianShen.getInt();
			if(isInvalid(sentencedPlayer))
			{
				tianShen.printInputErrorMessage();
				return false;
			}
			this.kill(sentencedPlayer);
			this.countAlivePlayers();
		}
		return true;
	}
	boolean skip()
	{
		if(isNotFirstNight)
		{
			if(status==GET_GENERAL_INFO)return true;
			else if(status==GET_REBELS_INFO)return true;
			else if(status==GET_TRAITORS_INFO)return true;
			else if(status==GET_KING_INFO)return true;
			else if(status==GET_SOLDIER_INFO)return true;
			else if(status==GET_MINISTER_INFO)return true;
		}
		if(isKingAlive&&status==GET_NEW_KING_INFO)return true;
		return false;
	}
	int getCharacter(int player)
	{
		int chairNumber=tianShen.getInt();
		if(isInvalid(chairNumber))return ERROR;
		this.players[chairNumber]=player;
		return chairNumber;
	}
	boolean getCharacters(int player, int number)
	{
		int[] chairNumbers=tianShen.getInts();
		if(chairNumbers.length!=number)
		{
			tianShen.printNotMatchErrorMessage();
			return false;
		}
		if(player==TRAITORS)
		{
			this.medicines=new int[traitorNum]; 
			this.poisons=new int[traitorNum];
		}
		for(int i=1; i<=number; i++)
		{
			int chairNumber=chairNumbers[i-1];
			if(isInvalid(chairNumber))return false;
			this.players[chairNumber]=player;
			if(player==TRAITORS)
			{
				this.medicines[i-1]=chairNumber; 
				this.poisons[i-1]=chairNumber;
			}
		}
		return true;
	}
	void initPlayers()
	{
		this.players=new int[totalNum+1]; 
		this.isGuarded=new boolean[totalNum+1]; 
		for(int i=1; i<=totalNum; i++)
		{
			this.players[i]=VILLAGERS; 
			this.isGuarded[i]=false;
		}
	}
	public void showAllPlayers()
	{
		tianShen.showAllPlayers(players, king);
	}
	int max(int a, int b){return a>b?a:b;}
	int getTraitorIndex(int[] medicine, int traitorPlayer)
	{
		for(int i=0; i<medicine.length; i++)
		if(medicine[i]==traitorPlayer)return i;
		return -1;
	}
	void kill(int player)
	{
		if(player>0&&players[player]>0)this.players[player]*=-1;
	}
	void rescue()
	{
		if(murderedPlayer!=0&&rescuedPlayer==1)
		{
			int traitorIndex=getTraitorIndex(medicines, traitorPlayer);
			if(traitorIndex>=0)
			{
				this.murderedPlayer=0; 
				this.medicines[traitorIndex]=0;
			}
			else this.rescuedPlayer=0;
		}
		else this.rescuedPlayer=0;
	}
	void poison()
	{
		if(rescuedPlayer==0)
		{
			int traitorIndex=this.getTraitorIndex(poisons, traitorPlayer);
			if(traitorIndex>=0)
			{
				if(poisonedPlayer==guardedPlayer)this.poisonedPlayer=0;
				if(isMinisterNotPoisoned(poisonedPlayer))this.poisonedPlayer=0;
				if(poisonedPlayer!=0)this.poisons[traitorIndex]=0;
			}
			else this.poisonedPlayer=0;
		}		
		else this.poisonedPlayer=0;
	}
	void getDiedPlayer()
	{
		if(guardedPlayer!=0)this.isGuarded[guardedPlayer]=true;
		if(players[traitorPlayer]==TRAITORS){rescue(); poison();}
		else this.rescuedPlayer=this.poisonedPlayer=0;
		if(isNotRevenge())this.revengedPlayer=0;
		this.kill(revengedPlayer); 
		this.kill(murderedPlayer); 
		this.kill(poisonedPlayer);
	}
	void countAlivePlayers()
	{
		this.aliveTotal=this.aliveRebel=this.aliveTraitor=0;
		for(int i=1; i<=totalNum; i++)
		{
			if(players[i]>0)this.aliveTotal++;
			if(players[i]==REBELS)this.aliveRebel++;
			if(players[i]==TRAITORS)this.aliveTraitor++;
		}
	}
	void declareTheDeath(int player)
	{
		if(player!=0)
		{
			tianShen.declareTheDeath(player);
			if(player==king)this.isKingAlive=false;
			this.isSilentNight=false;
		}
	}
	void declareTheDeaths()
	{
		this.isSilentNight=true; this.isKingAlive=true;
		this.declareTheDeath(revengedPlayer);
		this.declareTheDeath(murderedPlayer);
		this.declareTheDeath(poisonedPlayer);
		if(isSilentNight)tianShen.declareThePeace();
	}
	boolean isNotAlive(int player)
	{
		for(int i=1; i<=totalNum; i++)
		{
			if(players[i]==player)return false;
		}
		return true;
	}
	boolean isNotRevenge()
	{
		return players[murderedPlayer]!=GENERAL||players[revengedPlayer]==SOLDIER;
	}
	boolean isMinisterNotPoisoned(int poisonedPlayer)
	{
		return players[poisonedPlayer]==MINISTER&&players[king]!=MINISTER;
	}
	boolean isMultiplePlayers()
	{
		if(status==GET_REBELS_INFO)return true;
		else if(status==GET_TRAITORS_INFO)return true;
		else return false;
	}
	boolean isGameOver()
	{
		int aliveLoyal=aliveTotal-aliveRebel-aliveTraitor;
		if(aliveLoyal==aliveTotal){this.winner=KING;return true;}
		else if(aliveLoyal==0)
		{
			if(aliveRebel>0){this.winner=REBELS;return true;}
			else {this.winner=TRAITORS;return true;}
		}
		else return false;
	}
	boolean isLoyal(int player)
	{
		if(aliveRebel>0)return players[player]==REBELS?false:true;
		else return players[player]==TRAITORS?false:true;
	}
	public boolean isDied(int player)
	{
		return players[player]<0;
	}
	public boolean isInvalid(int chairNumber)
	{
		return chairNumber<0||chairNumber>totalNum?true:false;
	}
}
class TianShen
{
	int size;
	String players;
	Scanner scanner;
	boolean isChinese; 
	public JunChenSha junChenSha;
	public TianShen(boolean isChinese, int size)
	{
		this.size=size;
		this.isChinese=isChinese;
		this.scanner=new Scanner(System.in);
	}
	public void askPlayersToContinue(int space)
	{
		for(int i=0; i<space; i++)System.out.print("\t");
		if(isChinese)System.out.print("Qian jin 1,  hou tui 0:  ");
		else System.out.print("Go forward, press 1, or backward, press 0:  ");
	}
	public void askPlayersToInputNumber(boolean isMultiplePlayers)
	{
		if(isMultiplePlayers)
		{
			if(isChinese)
			{
				System.out.print("Qing shu ru yi zu shu zi : ");
				System.out.println("(yong dou hao fen kai)");
			}
			else
			{
				System.out.print("Please give me some integers: ");
				System.out.println("(Please use comma to seperate)");
			}
		}
		else
		{
			if(isChinese)System.out.print("Qing shu ru yi ge shu zi :  ");
			else System.out.print("Please give me an integer:  ");
		}
	}
	public void askPlayersToOpenEyes(int player)
	{
		this.players=getPlayersName(player);
		if(isChinese)System.out.println(players + ", qing zheng yan!");
		else System.out.println(players + ", Please open your eyes!");
	}
	public void askPlayersToCloseEyes(int player)
	{
		this.players=getPlayersName(player);
		if(isChinese)System.out.println(players + ", qing bi yan!");
		else System.out.println(players + ", Please close your eyes!");
	}
	public void askPlayersToGuard()
	{
		if(isChinese)System.out.println(players + ", qing que ding yi ming shou wei mu biao:");
		else System.out.println(players + ", please chose someone to be guarded:");
	}
	public void askPlayersToRevenge()
	{
		if(isChinese)System.out.println(players + ", qing que ding yi ming fu chou mu biao:");
		else System.out.println(players + ", please chose someone to fight back:");
	}
	public void askPlayersToMurder()
	{
		if(isChinese)System.out.println(players + ", qing que ding yi ming an sha mu biao:");
		else System.out.println(players + ", please chose someone to be murdered:");
	}
	public void askPlayersToFindRepresentitive()
	{
		if(isChinese)
		{
			System.out.println(players + ", qing que ding yi ming wan jia zuo wei dai biao.");
			System.out.println("Qi ta "+players+" qing bi yan.");
			System.out.println(players + " dai biao shi:");
		}
		else 
		{
			System.out.println(players + ", please chose someone to be your representitive.");
			System.out.println("Other "+players+", please close your eyes.");
			System.out.println(players + "s\' representitive is:");
		}
	}
	public void askPlayersToRescue(int murderedPlayer)
	{
		if(isChinese)
		{
			System.out.println(players + ", gang cai zhe wei wan jia ("+murderedPlayer+") shen wang.");
			System.out.println("Ni you yi ping jie yao shi yong ma (Yong yao shu ru 1, bu yong shu ru 0)?");
		}
		else 
		{
			System.out.println(players + ", the player in danger is this ("+murderedPlayer+").");
			System.out.println("You have one medicine, do you want to use it(save press 1, otherwise 0)?");
		}
	}
	public void askPlayersToPoison()
	{
		if(isChinese)
		{
			System.out.println(players + "Ni you yi ping du yao shi yong ma?(Ru guo bu yong yao, shu ru 0)");
			System.out.println("Ru guo shi yong, qing que ding yi ming mu biao:");
		}
		else 
		{
			System.out.println(players + " You have one poison, do you want to use it?(if don\'t use, press 0)");
			System.out.println("If use, please chose someone to be poisoned:");
		}
	}
	public void askPlayersToCheck()
	{
		if(isChinese)System.out.println(players + ", ni xiang yan na yi wei wan jia de shen fen? ");
		else System.out.println(players + ", whose identity do you want to check? ");
	}
	public void declareTheIdentity(boolean isLoyal)
	{
		if(isChinese)
		{
			System.out.println(players + ", hang ren xiang Shang, huai ren xiang Xia,  ");
			System.out.println("Ni yan de wan jia shi zhe ge:("+(isLoyal?"Shang)":"Xia)"));
		}
		else 
		{
			System.out.println(players + ", good person is Up,  bad person is Down, ");
			System.out.println("The perosn you checked is:("+(isLoyal?"Up)":"Down)"));
		}
	}
	public void declareTheDeath(int diedPlayer)
	{
		if(isChinese)System.out.println(players + ", Zuo wan "+diedPlayer+" hao wan jia si wang.");
		else System.out.println(players + ", No."+diedPlayer+" was killed during last night");
	}
	public void declareThePeace()
	{
		if(isChinese)System.out.println("Zuo wan shi yi ge ping an ye!");
		else System.out.println("Yesterday was a silent night!");
	}
	public void askPlayersToFindNewKing(int player)
	{
		this.players=getPlayersName(player);
		if(isChinese)System.out.println(players + ", ni yao shan rang wang wei gei shui? ");
		else System.out.println(players + ", you were killed last night, who could be the new king? ");
	}
	public void askPlayersToSentence(int player)
	{
		this.players=getPlayersName(player);
		if(isChinese)
		{
			System.out.println(players + ", qing zhi ding yi ming wan jia ka shi fa yan,");
			System.out.println("Bing zhi ding fa yan shun xu, zai quan bu wan jia fa yan jie shu hou,");
			System.out.println("Qing dian sha yi ming wan jia, ni suo dian sha de shi:");
		}
		else 
		{
			System.out.println(players + ", Please specify someone to start thier speakings one by one,");
			System.out.println("After all of them finished thier speakings, please sentence some one to death:");
		}
	}
	public void declareTheWinner(int player)
	{
		this.players=getPlayersName(player);
		if(isChinese)System.out.println("You xi jie shu! "+players + " hou sheng!");
		else System.out.println("Game is over! "+players + " is the winner!");
	}
	public void showAllPlayers(int[] players, int king)
	{
		if(isChinese)System.out.println( "Suo you wan jia de shen fen ru xia:");
		else System.out.println("The charaters of all the players are shown bellow:"); 
		for(int i=1; i<players.length; i++)
		{
			String isDied="", isKing, player;
			isKing=i==king?isChinese?"(Wang)":"(King)":"";
			if(players[i]<0)
			{
				player=getPlayersName(-players[i]);
				isDied=isChinese?"(Zhen Wang)":"(Is Died)";
				if(isChinese)System.out.println("Wan jia ["+i+"] de shen fen shi: "+player+isKing+isDied);
				else System.out.println("The charater of player ["+i+"] is: "+player+isKing+isDied);
			}
			else
			{
				player=getPlayersName(players[i]);
				if(isChinese)System.out.println("Wan jia ["+i+"] de shen fen shi: "+player+isKing+isDied);
				else System.out.println("The charater of player ["+i+"] is: "+player+isKing+isDied);
			}
		}
	}
	public String getPlayersName(int player)
	{
		if(player==junChenSha.ALL) return isChinese?"Suo You Ren":"All People";
		else if(player==junChenSha.KING) return isChinese?"Jun Wang":"King";
		else if(player==junChenSha.QUEEN) return isChinese?"Wang Hou":"Queen";
		else if(player==junChenSha.SOLDIER) return isChinese?"Guan Bing":"Soldier";
		else if(player==junChenSha.GENERAL) return isChinese?"Jiang Jun":"General";
		else if(player==junChenSha.MINISTER) return isChinese?"Zai Xiang":"Minister";
		else if(player==junChenSha.VILLAGERS) return isChinese?"Cun Min":"Villagers";
		else if(player==junChenSha.TRAITORS) return isChinese?"Nei Jian":"Traitors";
		else if(player==junChenSha.REBELS) return isChinese?"Fan Zei":"Rebels";
		else return "null";
	}
	public void askPlayersTotalNumber(int player)
	{
		this.players=getPlayersName(player);
		if(isChinese)System.out.println("You duo shao wei wan jia shi "+players+" ?");
		else System.out.println("How many players are "+players+" ?");
	}

	public void askPlayersChairNumber(boolean isMultiplePlayers)
	{
		if(isMultiplePlayers)
		{
			if(isChinese)System.out.println(players+" de zuo wei bian hao fen bie shi shen me?");
			else System.out.println("What are the chair No.s of "+players);
		}
		else
		{
			if(isChinese)System.out.println(players+" de zuo wei bian hao shi shen me?");
			else System.out.println("What is the chair No. of "+players);
		}
	}
	public void printInputErrorMessage()
	{
		if(isChinese)System.out.println("Shu ru cuo wu, qing chong xin shu ru: ");
		else System.out.println("Error input, please input again: ");
	}
	public void printInputAgainMessage()
	{
		if(isChinese)System.out.print("Ci wan jia yi si wang, shi fou cong xin shu ru? (An 1 chong shu)");
		else System.out.print("This player is died, would you like to input again? (Press 1 to input again)");
	}
	public void printNotMatchErrorMessage()
	{
		if(isChinese)System.out.println(players+" de ge shu yu shu ru shu zi de ge shu bu fu");
		else System.out.println("The number of "+players+" deosn\'t macth the numbers of integers input");
	}
	public void printMenu()
	{
		this.println();
		System.out.println("Jun Chen Sha");
		this.println();
	}
	public void println()
	{
		for(int i=0; i<size; i++)System.out.print('_');
		System.out.println();
		System.out.println();
	}
	int integer;
	int[] integers;
	public int getInt()
	{
		return this.integer;
	}
	public int[] getInts()
	{
		return this.integers;
	}
	public void printInts()
	{
		for(int i=0; i<integers.length; i++)
		{
			System.out.printf("integers[%d]=%d\n", i, integers[i]);
		}
	}
	public boolean nextInt(boolean isMultiple)
	{
		if(isMultiple)
		{
			String line=this.scanner.nextLine();
			String[] values=line.replace(" ", "").split(",");
			this.integers=new int[values.length];
			for(int i=0; i<values.length; i++)
			{
				try{this.integers[i]=Integer.parseInt(values[i]);}
				catch(Exception e){return false;}
			}
			return true;
		}
		else
		{
			try
			{
				String value=this.scanner.nextLine();
				this.integer=Integer.parseInt(value);
				return true;
			}
			catch(Exception e){return false;}
		}
	}

}

