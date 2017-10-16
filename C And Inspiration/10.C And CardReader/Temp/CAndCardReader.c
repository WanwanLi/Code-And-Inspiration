#include <windows.h>
#include <stdio.h>
#define BLOCK0 0x01
#define BLOCK1 0x02
#define BLOCK2 0x04
#define KEY_IDENTIFY 0x10

#define String char*
String newString(int length)
{
	String string=(String)(malloc((length+1)*sizeof(char)));
	string[length]='\0';
	return string;
}

int* newInt(int length)
{
	int* integers=(int*)(malloc(length*sizeof(int)));
	return integers;
}
int length(String string)
{
	int l=0;
	char c=string[l];
	while(c!='\0')
	{
		l++;
		c=string[l];
	}
	return l;
}
int equals(String string0,String string1)
{
	int i=0;
	int length0=length(string0);
	int length1=length(string1);
	if(length0!=length1)return 0;
	for(i=0;i<length0;i++)if(string0[i]!=string1[i])return 0;
	return 1;
}
int exponent(int base,int index)
{
	int i=0;
	int result=1;
	for(i=0;i<index;i++)result*=base;
	return result;
}
int toInteger(String string)
{
	int i,k;
	char c;
	int l=length(string);
	int integer=0;
	for(i=0;i<l;i++)
	{
		c=string[i];
		k=(int)(c-'0');
		integer+=k*exponent(10,l-1-i);
	}
	return integer;
}

String add(String string0,String string1)
{
	int i=0;
	int length0=length(string0);
	int length1=length(string1);
	String add=newString(length0+length1);
	for(i=0;i<length0;i++)add[i]=string0[i];
	for(i=0;i<length1;i++)add[length0+i]=string1[i];
	return add;
}
String toString(int integer)
{
	String zero="0";
	if(integer==0)return zero;
	int isNegative=0;
	if(integer<0)isNegative=1;
	int number=integer;
	if(isNegative)number=-integer;
	int length=0;
	int i=0;
	while(number>0)
	{
		number/=10;
		length++;
	}
	number=integer;
	if(isNegative)number=-integer;
	String string=newString(length);
	for(i=0;i<length;i++)
	{
		string[length-1-i]=(char)(number%10+'0');
		number/=10;
	}
	if(isNegative)return add("-",string);
	return string;
}
String substring(String string,int i0,int i1)
{
	String subString=newString(i1-i0);
	int i=i0;for(;i<i1;i++)subString[i-i0]=string[i];
	return subString;
}

String ADD(String integer1,String integer2)
{
	int length1=length(integer1);
	int length2=length(integer2);
	int length3=(length1>length2?length1:length2);
	String integer3=newString(length3);
	int c=0;
	int i=length1-1,j=length2-1,k=length3-1;
	for(;i>=0&&j>=0;i--,j--,k--)
	{
		int d1=integer1[i]-'0';
		int d2=integer2[j]-'0';
		int s=d1+d2+c;
		c=s/10;
		integer3[k]=s%10+'0';
	}
	for(;i>=0;i--,k--)
	{
		int d1=integer1[i]-'0';
		int s=d1+c;
		c=s/10;
		integer3[k]=s%10+'0';
	}
	for(;j>=0;j--,k--)
	{
		int d2=integer2[j]-'0';
		int s=d2+c;
		c=s/10;
		integer3[k]=s%10+'0';
	}
	if(c!=0)
	{
		String integer4=newString(length3+1);
		integer4[0]=c+'0';
		for(i=0;i<length3;i++)integer4[i+1]=integer3[i];
		return integer4;
	}
	return integer3;
}



#define Node struct Node
Node
{
	String NO;
	int balance;
	int isOnService;
	Node* next;
};
Node* first;
Node* last;
int listLength=0;
Node* newNode(String NO,int balance)
{
	Node* node=(Node*)malloc(sizeof(Node));
	node->NO=NO;
	node->balance=balance;
	node->isOnService=0;
	node->next=NULL;
	return node;
}
void addNodeIntoList(String NO,int balance)
{
	Node* node=newNode(NO,balance);
	if(first==NULL)
	{
		first=node;
		last=node;
	}
	else
	{
		last->next=node;
		last=node;
	}
	listLength++;
}
int deleteNodeFromList(String NO)
{
	int i;
	Node* n=first;
	Node* t=n;
	if(equals(first->NO,NO))
	{
		first=first->next;
		free(n);
		listLength--;
		return 0;
	}
	for(i=0;i<listLength;i++,t=n,n=n->next)
	{
		if(equals(n->NO,NO))
		{
			t->next=n->next;
			if(n==last)last=t;
			free(n);
			listLength--;
			return i;
		}	
	}
	return -1;
}
String removeNodeFromList(int index)
{
	if(index<0||index>=listLength)return 0;
	int i;
	Node* n=first;
	Node* t=n;
	if(index==0)first=first->next;
	else
	{
		for(i=0;i<index;i++,t=n,n=n->next);
		t->next=n->next;
		if(n==last)last=t;
	}
	String NO=n->NO;
	free(n);
	listLength--;
	return NO;
}
int existInList(String NO)
{
	int i;
	Node* n=first;
	Node* t=n;
	for(i=0;i<listLength;i++,t=n,n=n->next)
	{
		if(equals(n->NO,NO))return 1;
	}
	return 0;
}


Node* readNodesFromFile(String fileName)
{
	int length,i;
	String string=newString(0);
	String integer=newString(0);
	FILE* file=fopen(fileName,"r");
	if(file==NULL)return NULL;
	fscanf(file,"%s",integer);
	length=toInteger(integer);
	for(i=0;i<length;i++)
	{
		string=newString(0);
		integer=newString(0);
		fscanf(file,"%s",string);
		fscanf(file,"%s",integer);
		addNodeIntoList(string,toInteger(integer));
	}
	fclose(file);
	return first;
}

int writeNodesIntoFile(String fileName)
{
	int i;
	FILE* file=fopen(fileName,"w");
	if(file==NULL)return 0;
	fprintf(file,"%d ",listLength);
	Node* n=first;
	for(i=0;i<listLength;i++,n=n->next)
	{
		fprintf(file,"%s ",n->NO);
		fprintf(file,"%d ",n->balance);
	}
	fclose(file);
	return 1;
}



Node* cardUserInfo;
int cardUserNumber=0;
String  cardUserInfoFile="CAndCardReader.userInfo";
void readCardUserInfo()
{
	cardUserInfo=readNodesFromFile(cardUserInfoFile);
	cardUserNumber=listLength;
}
void writeCardUserInfo()
{
	writeNodesIntoFile(cardUserInfoFile);
}
void printCardUserInfo()
{
	int i;
	Node* n=cardUserInfo;
	for(i=0;i<cardUserNumber;i++,n=n->next)
	{
		printf("use NO is : %s\t",n->NO);
		printf("Balance is : %d\n",n->balance);
	}
}
int checkCardUserNO(String NO)
{
	int i;
	Node* n=cardUserInfo;
	for(i=0;i<cardUserNumber;i++,n=n->next)
	{
		if(equals(n->NO,NO))
		{
			if(n->balance>0)return i;
			else return -1;
		}
	}
	return -2;
}
void reduceCardUserBalance()
{
	int i;
	Node* n=cardUserInfo;
	for(i=0;i<cardUserNumber;i++,n=n->next)
	{
		if(n->isOnService)n->balance--;
	}
}
int isOnService(int checkResult)
{
	int i;
	Node* n=cardUserInfo;
	for(i=0;i<checkResult;i++,n=n->next);
	return n->isOnService;
}
void startService(int checkResult)
{
	int i;
	Node* n=cardUserInfo;
	for(i=0;i<checkResult;i++,n=n->next);
	n->isOnService=1;
}
int stopService(int checkResult)
{
	int i;
	Node* n=cardUserInfo;
	for(i=0;i<checkResult;i++,n=n->next);
	n->isOnService=0;
	return n->balance;
}
String getCardNO()
{
	HINSTANCE hinstance=LoadLibrary("OUR_MIFARE.dll");
	typedef char (*ReadICCard)(unsigned char ctrlword,unsigned char *serial,unsigned char area,unsigned char keyA1B0,unsigned char *picckey,unsigned char *piccdata0_2);
	ReadICCard readICCard=(ReadICCard)GetProcAddress(hinstance,"piccreadex");
	unsigned char ctrlWord=BLOCK0+BLOCK1+BLOCK2+KEY_IDENTIFY;
	unsigned String serial=newString(4);
	unsigned char areaNO=1;
	unsigned char key=1;
	unsigned char* password=(char*)malloc(sizeof(char)*6);
	int i=0;
	for(i=0;i<6;i++)password[i]=0xFF;
	String result=newString(48);
	char returnValue=readICCard(ctrlWord,serial,areaNO,key,password,result);
	if(returnValue!=0)return 0;
	String cardNO="0";
	String adder="0";
	int sum=0;
	for(i=0;i<3;i++)sum+=(serial[i]<<i*8);
	adder=toString(serial[i]<<2*8);
	for(i=0;i<8;i++)adder=ADD(adder,adder);
	cardNO=ADD(toString(sum),adder);
	return cardNO;
}



String readCard()
{
	HINSTANCE hinstance=LoadLibrary("OUR_MIFARE.dll");
	typedef char (*ReadICCard)(unsigned char ctrlword,unsigned char *serial,unsigned char area,unsigned char keyA1B0,unsigned char *picckey,unsigned char *piccdata0_2);
	ReadICCard readICCard=(ReadICCard)GetProcAddress(hinstance,"piccreadex");
	unsigned char ctrlWord=BLOCK0+BLOCK1+BLOCK2+KEY_IDENTIFY;
	String serial=newString(4);
	unsigned char areaNO=1;
	unsigned char key=1;
	unsigned char* password=(char*)malloc(sizeof(char)*6);
	int i=0;
	for(i=0;i<6;i++)password[i]=0xFF;
	String result=newString(48);
	char returnValue=readICCard(ctrlWord,serial,areaNO,key,password,result);
	if(returnValue!=0)return NULL;
	return result;
}


int writeCard(String string)
{
	HINSTANCE hinstance=LoadLibrary("OUR_MIFARE.dll");
	typedef char (*WriteICCard)(unsigned char ctrlword,unsigned char *serial,unsigned char area,unsigned char keyA1B0,unsigned char *picckey,unsigned char *piccdata0_2);
	WriteICCard writeICCard=(WriteICCard)GetProcAddress(hinstance,"piccwriteex");
	unsigned char ctrlWord=BLOCK0+BLOCK1+BLOCK2+KEY_IDENTIFY;
	int i=0;
	String serial=newString(4);
	unsigned char areaNO=1;
	unsigned char key=1;
	unsigned char* password=(char*)malloc(sizeof(char)*6);
	for(i=0;i<6;i++)password[i]=0xFF;
	if(writeICCard(ctrlWord,serial,areaNO,key,password,string)==0)
	{
		writeICCard(ctrlWord,serial,areaNO,key,password,string);
		return 1;
	}
	return 0;
}


void beepCard(int time)
{
	HINSTANCE hinstance=LoadLibrary("OUR_MIFARE.dll");
	typedef char (*BeepICCard)(unsigned long xms);
	BeepICCard beepICCard=(BeepICCard)GetProcAddress(hinstance,"pcdbeep");
	unsigned char ctrlWord=BLOCK0+BLOCK1+BLOCK2+KEY_IDENTIFY;
	beepICCard(time);
}

void errorBeep()
{
	beepCard(80);
	beepCard(80);
	beepCard(80);
	beepCard(80);
	printf("error!!!\n");
}


void sleep(int time)
{
	int i=0,j=0,k=0;
	for(i=0;i<time*1000;i++)
	{
		for(j=0;j<1000;j++)
		{
			for(k=0;k<400;k++)
			{
			}
		}
	}	
}

int day=0;
int hour=0;
int minute=0;
int second=0;
void clearTime()
{
	day=0;
	hour=0;
	minute=0;
	second=0;
}
void timer()
{
	second++;
	if(second==60){second=0;minute++;}
	if(minute==60){minute=0;hour++;}
	if(hour==24){hour=0;day++;}
	if(day==365)day=0;
}
void printTime()
{
	printf("Day : %d\t",day);
	printf("hour : %d\t",hour);
	printf("minute : %d\t",minute);
	printf("second : %d\n",second);
}
int startServiceForNO(String NO)
{
	int checkResult=checkCardUserNO(NO);
	if(checkResult<0)return 0;
	if(isOnService(checkResult))return 0;
	startService(checkResult);
	return 1;
}
int stopServiceForNO(String NO)
{
	int checkResult=checkCardUserNO(NO);
	if(checkResult<0)return 0;
	if(isOnService(checkResult)==0)return 0;
	writeCardUserInfo();
	return stopService(checkResult);
}
int counter=0;
void runCardReader()
{

	String cardNO=getCardNO();
	String NO=NULL;
	if(cardNO!=NULL)
	{
		NO=cardNO;
		printf("user NO is : %s\n",NO);
		if(counter==0||counter==1)
		{
			counter++;
			beepCard(100);
		}
		else 
		{
			errorBeep();
			counter=0;
			sleep(2);
		}
	}
	else 
	{
		if(counter==1)
		{
printf("b1\n");
			int b=startServiceForNO(NO);
printf("b2\n");
			if(b==1)printf("The Service For User NO. : %d is start ...\n",NO);
			else
			{
				errorBeep();
				printf("The Service For User NO. : %d has already started ...\n",NO);
				counter=0;
			}
		}
		else if(counter==2)
		{
			int b=stopServiceForNO(NO);
			if(b>=0)printf("The Service For User NO. : %d is stoped (total  balance is %d Yuan )\n",NO,b);
			else
			{
				errorBeep();
				printf("User NO. %d has already finished  ...\n",NO);
				counter=0;
			}
		}
		else counter=0;
	}
}

void startCardReader()
{
	readCardUserInfo();
	printCardUserInfo();
	while(1)
	{
		runCardReader();
		sleep(1);
		timer();
		printTime();
	}
}

int main()
{
	printf("C And Card Reader: \n");
	printf("Functions:\n");
	printf("1.Start the Card Reader \n");
	printf("2.Add Account \n");
	printf("3.Change Card ID\n");
	printf("4.Add Money\n");
	printf("Input command here:");
	int d=0;
	scanf("%d",&d);
	if(d==1)startCardReader();
	else
	{
		int length=48,i=0;
		String id=toString(d);
		String string=newString(length);
		for(i=0;i<length;i++)string[i]='0';
		for(i=0;i<4;i++)string[i]=id[i];
		int b=writeCard(string);
		if(b==1)printf("Card have already been written :\n %s\n",readCard());
		else printf("Write card error ! ! ! \n");
	}
	return 0;
}
