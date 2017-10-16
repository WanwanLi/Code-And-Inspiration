package com.android.project;
import java.util.LinkedList;
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
	public final int NONE=-2;
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
	LinkedList stack;
	TianShen tianShen;
	boolean[] isGuarded;
	int[]  players, medicines, poisons;
	int status, image, winner, king, traitor;
	int aliveRebel, aliveTraitor, aliveTotal;
	int rebelNum, traitorNum, totalNum;
	int guardedPlayer, revengedPlayer, murderedPlayer;
	int rescuedPlayer, poisonedPlayer, checkedPlayer, sentencedPlayer;
	boolean isNotFirstNight, isSilentNight, isKingAlive, isShowingAllPlayers;
	public JunChenSha(TianShen tianShen)
	{
		this.initialize();
		this.tianShen=tianShen;
		this.tianShen.junChenSha=this;
	}
	public void next()
	{
		if(status==GAME_OVER)
		{
			this.initialize(); 
			this.show();
			return;
		}
		if(isShowingAllPlayers)showAllPlayers();
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
				if(isCloseToEnd()||isGameOver())
				{
					this.status=GAME_OVER;
					this.show();
					return;
				}
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
		if(isShowingAllPlayers)showAllPlayers();
		if(status==GAME_OVER)this.status=GET_SENTENCED_PLAYER;
		else if(status!=GET_TOTAL_NUM)
		{
			this.status--;
			while(skip())this.status--;
			if(isGetCharacters())this.recover();
		}
		if(isNotFirstNight)this.status=max(status, GET_GUARDED_PLAYER);
		this.show();
	}
	public void show()
	{
		tianShen.clear();
		if(status==GAME_OVER)
		{
			tianShen.declareTheWinner(winner);
		}
		else if(status==GET_TOTAL_NUM)
		{
			tianShen.askPlayersTotalNumber(ALL);
			tianShen.updateImage(ALL);
		}
		else if(status==GET_REBEL_NUM)
		{
			tianShen.askPlayersTotalNumber(REBELS);
			tianShen.updateImage(REBELS);
		}
		else if(status==GET_TRAITOR_NUM)
		{
			tianShen.askPlayersTotalNumber(TRAITORS);
			tianShen.updateImage(TRAITORS);
		}
		else if(status==GET_GENERAL_INFO)
		{
			tianShen.askPlayersToCloseEyes(ALL);
			tianShen.askPlayersToOpenEyes(GENERAL);
			tianShen.updateImage(GENERAL);
			tianShen.askPlayersChairNumber(GENERAL, 1);
		}
		else if(status==GET_GUARDED_PLAYER)
		{
			if(isNotFirstNight)
			{
				tianShen.askPlayersToCloseEyes(ALL);
				tianShen.askPlayersToOpenEyes(GENERAL);
				tianShen.updateImage(GENERAL);
			}
			tianShen.askPlayersToGuard(GENERAL);
		}
		else if(status==GET_REVENGED_PLAYER)
		{
			tianShen.askPlayersToRevenge(GENERAL);
			tianShen.updateImage(GENERAL);
		}
		else if(status==GET_REBELS_INFO)
		{
			tianShen.askPlayersToCloseEyes(GENERAL);
			tianShen.askPlayersToOpenEyes(REBELS);
			tianShen.updateImage(REBELS);
			tianShen.askPlayersChairNumber(REBELS, rebelNum);
		}
		else if(status==GET_MURDERED_PLAYER)
		{
			if(isNotFirstNight)
			{
				tianShen.askPlayersToCloseEyes(GENERAL);
				tianShen.askPlayersToOpenEyes(REBELS);
			}
			tianShen.askPlayersToMurder(REBELS);
			tianShen.updateImage(REBELS);
		}
		else if(status==GET_TRAITORS_INFO)
		{
			tianShen.askPlayersToCloseEyes(REBELS);
			tianShen.askPlayersToOpenEyes(TRAITORS);
			tianShen.updateImage(TRAITORS);
			tianShen.askPlayersChairNumber(TRAITORS, traitorNum);
		}
		else if(status==GET_TRAITOR_PLAYER)
		{
			if(isNotFirstNight)
			{
				tianShen.askPlayersToCloseEyes(REBELS);
				tianShen.askPlayersToOpenEyes(TRAITORS);
				tianShen.updateImage(TRAITORS);
			}
			tianShen.askPlayersToFindPrime(TRAITORS);
		}
		else if(status==GET_RESCUED_PLAYER)
		{
			tianShen.askPlayersToRescue(TRAITORS, murderedPlayer);
		}
		else if(status==GET_POISONED_PLAYER)
		{
			tianShen.askPlayersToPoison(TRAITORS);
			tianShen.updateImage(TRAITORS);
		}
		else if(status==GET_KING_INFO)
		{
			tianShen.askPlayersToCloseEyes(TRAITORS);
			tianShen.askPlayersToOpenEyes(KING);
			tianShen.updateImage(KING);
			tianShen.askPlayersChairNumber(KING, 1);
		}
		else if(status==GET_SOLDIER_INFO)
		{
			tianShen.askPlayersToCloseEyes(KING);
			tianShen.askPlayersToOpenEyes(SOLDIER);
			tianShen.updateImage(SOLDIER);
			tianShen.askPlayersChairNumber(SOLDIER, 1);
		}
		else if(status==GET_MINISTER_INFO)
		{
			tianShen.askPlayersToCloseEyes(SOLDIER);
			tianShen.askPlayersToOpenEyes(MINISTER);
			tianShen.updateImage(MINISTER);
			tianShen.askPlayersChairNumber(MINISTER, 1);
		}
		else if(status==GET_CHECKED_PLAYER)
		{
			if(isNotFirstNight)
			{
				tianShen.askPlayersToCloseEyes(TRAITORS);
				tianShen.askPlayersToOpenEyes(MINISTER);
				tianShen.updateImage(MINISTER);
			}
			tianShen.askPlayersToCheck(MINISTER);
		}
		else if(status==GET_DIED_PLAYER)
		{
			this.getDiedPlayer(); this.countAlivePlayers();
			tianShen.declareTheIdentity(MINISTER, isLoyal(checkedPlayer));
			tianShen.askPlayersToCloseEyes(MINISTER);
			tianShen.askPlayersToOpenEyes(ALL);
			tianShen.updateImage(ALL);
			this.declareTheDeaths();
		}
		else if(status==GET_NEW_KING_INFO)
		{
			tianShen.askPlayersToFindNewKing(KING);
			tianShen.updateImage(KING);
		}
		else if(status==GET_SENTENCED_PLAYER)
		{
			tianShen.askPlayersToSentence(KING);
			tianShen.updateImage(KING);
		}
	}
	boolean nextInt()
	{
		int playersNum=this.getPlayersNumber();
		if(!tianShen.nextInt(playersNum))return false;
		if(status==GET_TOTAL_NUM)
		{
			this.totalNum = this.aliveTotal = tianShen.getInt();
			if(totalNum==10)this.setPlayersNumber(2, 3);
			else if(totalNum==13)this.setPlayersNumber(3, 4);
			this.initPlayers();
		}
		else if(status==GET_REBEL_NUM)
		{
			this.rebelNum = this.aliveRebel = tianShen.getInt();
		}
		else if(status==GET_TRAITOR_NUM)
		{
			this.traitorNum =  this.aliveTraitor = tianShen.getInt();
			this.medicines=new int[traitorNum]; 
			this.poisons=new int[traitorNum];
		}
		else if(status==GET_GENERAL_INFO)
		{
			this.getCharacter(GENERAL);
		}
		else if(status==GET_GUARDED_PLAYER)
		{
			this.guardedPlayer=tianShen.getInt();
			if(isGuarded[guardedPlayer])this.guardedPlayer=0;
		}
		else if(status==GET_REVENGED_PLAYER)
		{
			this.revengedPlayer=tianShen.getInt();
			if(isNotAlive(GENERAL))guardedPlayer=revengedPlayer=0;
		}
		else if(status==GET_REBELS_INFO)
		{
			this.getCharacters(REBELS);
		}
		else if(status==GET_MURDERED_PLAYER)
		{
			this.murderedPlayer=tianShen.getInt();
			if(murderedPlayer==guardedPlayer)this.murderedPlayer=0;
			if(isNotAlive(REBELS))this.murderedPlayer=0;
		}
		else if(status==GET_TRAITORS_INFO)
		{
			this.getCharacters(TRAITORS);
		}
		else if(status==GET_TRAITOR_PLAYER)
		{
			if(aliveTraitor==1)traitor=getTraitor();
			else if(aliveTraitor==0)this.traitor=0;
			else this.traitor=tianShen.getInt();
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
		}
		else if(status==GET_KING_INFO)
		{
			this.king=this.getCharacter(KING);
		}
		else if(status==GET_SOLDIER_INFO)
		{
			this.getCharacter(SOLDIER);
		}
		else if(status==GET_MINISTER_INFO)
		{
			this.getCharacter(MINISTER);
		}
		else if(status==GET_CHECKED_PLAYER)
		{
			this.checkedPlayer=tianShen.getInt();
		}
		else if(status==GET_NEW_KING_INFO)
		{
			this.king=tianShen.getInt();
		}
		else if(status==GET_SENTENCED_PLAYER)
		{
			this.sentencedPlayer=tianShen.getInt();
			this.kill(sentencedPlayer);
			this.countAlivePlayers();
		}
		return true;
	}
	void initialize()
	{
		this.isNotFirstNight=false;
		this.isShowingAllPlayers=false;
		this.status=GET_TOTAL_NUM;
		this.stack=new LinkedList();
		this.players=null;
		this.traitor=0;
		this.king=0;
	}
	void setPlayersNumber(int traitorNum, int rebelNum)
	{
		this.traitorNum= this.aliveTraitor=traitorNum;
		this.rebelNum=this.aliveRebel=rebelNum;
		this.medicines=new int[traitorNum]; 
		this.poisons=new int[traitorNum];
		this.status=GET_TRAITOR_NUM;
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
	boolean skip()
	{
		if(isNotFirstNight&&isGetCharacters())return true;
		else if(isKingAlive&&status==GET_NEW_KING_INFO)return true;
		else return false;
	}
	boolean isGetCharacters()
	{
		if(status==GET_GENERAL_INFO)return true;
		else if(status==GET_REBELS_INFO)return true;
		else if(status==GET_TRAITORS_INFO)return true;
		else if(status==GET_KING_INFO)return true;
		else if(status==GET_SOLDIER_INFO)return true;
		else if(status==GET_MINISTER_INFO)return true;
		else return false;
	}
	int getCharacter(int player)
	{
		int chairNumber=tianShen.getInt();
		this.stack.push(this.players.clone());
		this.players[chairNumber]=player;
		return chairNumber;
	}
	void getCharacters(int player)
	{
		int[] chairNumbers=tianShen.getInts();
		int length=chairNumbers.length;
		int[] players=this.players.clone();
		for(int i=1; i<=length; i++)
		{
			int chairNumber=chairNumbers[i-1];
			this.players[chairNumber]=player;
			if(player==TRAITORS)
			{
				this.medicines[i-1]=chairNumber; 
				this.poisons[i-1]=chairNumber;
			}
		}
		this.stack.push(players);
	}
	void recover()
	{
		this.players=(int[])this.stack.pop();
	}
	public void showAllPlayers()
	{
		if(players==null)return;
		this.isShowingAllPlayers=!isShowingAllPlayers;
		if(isShowingAllPlayers)
		{
			tianShen.showAllPlayers(players, king, traitor);
			this.image=tianShen.updateImage(NONE);
		}
		else
		{
			tianShen.setImage(image);
			tianShen.setText("");
		}
	}
	int max(int a, int b){return a>b?a:b;}
	int getTraitor()
	{
		for(int i=1; i<=totalNum; i++)
		{
			if(players[i]==TRAITORS)return i;
		}
		return 0;
	}
	int getTraitorIndex(int[] medicine, int traitorPlayer)
	{
		for(int i=0; i<medicine.length; i++)
		if(medicine[i]==traitorPlayer)return i;
		return -1;
	}
	int getTraitorNumber(int[] medicine)
	{
		int number=0;
		for(int i=0; i<medicine.length; i++)
		if(medicine[i]>0)number++;
		return number;
	}
	void kill(int player)
	{
		if(player>0&&players[player]>0)
		{
			if(players[player]==TRAITORS)
			{
				int traitorIndex=getTraitorIndex(medicines, player);
				if(traitorIndex>=0)this.medicines[traitorIndex]=0;
				traitorIndex=getTraitorIndex(poisons, player);
				if(traitorIndex>=0)this.poisons[traitorIndex]=0;
			}
			this.players[player]*=-1;
		}

	}
	void rescue()
	{
		if(murderedPlayer!=0&&rescuedPlayer==1)
		{
			int traitorIndex=getTraitorIndex(medicines, traitor);
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
			int traitorIndex=this.getTraitorIndex(poisons, traitor);
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
		if(players[traitor]==TRAITORS){rescue(); poison();}
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
	boolean isCloseToEnd()
	{
		int aliveLoyal=aliveTotal-aliveRebel-aliveTraitor;
		if(aliveTraitor==0)
		{
			if(aliveRebel>=aliveLoyal){this.winner=REBELS;return true;}
		}
		if(aliveRebel==0)
		{
			if(getTraitorNumber(poisons)>=aliveLoyal){this.winner=TRAITORS;return true;}
		}
		return false;
	}
	boolean isLoyal(int player)
	{
		if(aliveRebel>0)return players[player]==REBELS?false:true;
		else return players[player]==TRAITORS?false:true;
	}
	public boolean isDied(int player)
	{
		return players==null?false:players[player]<0;
	}
	public boolean isInvalid(int chairNumber)
	{
		return players==null?false:chairNumber<0||chairNumber>totalNum?true:false;
	}
	public int getPlayersNumber()
	{
		if(status==GET_REBELS_INFO)return rebelNum;
		else if(status==GET_TRAITORS_INFO)return traitorNum;
		else return 1;
	}
	public int getPlayersName(int number)
	{
		if(number==rebelNum)return REBELS;
		else if(number==traitorNum)return TRAITORS;
		else return NONE;
	}
}