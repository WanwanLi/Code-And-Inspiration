#include <stdio.h>
#include <windows.h>
#include <WinSock2.h>
#include <mysql.h>
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
int parseInt(String string)
{
	int l=length(string);
	if(l==0)return 0;
	int i=0,k;
	int integer=0;
	char c=string[0];
	int isNegtive=0;
	if(c=='-'){isNegtive=1;l--;}
	for(i=0;i<l;i++)
	{
		c=string[i+isNegtive];
		k=(int)(c-'0');
		integer+=k*exponent(10,l-1-i);
	}
	return isNegtive?-integer:integer;
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


#define Connection MYSQL*
#define ResultSet MYSQL_RES*
MYSQL_ROW mySQLRow;
Connection connection;
void getConnection(String database,String user,String password)
{
	connection=mysql_init(NULL);
	if(!connection)printf("Init the connection to MySQL error...\n");
	connection=mysql_real_connect(connection,"localhost",user,password,database,0,NULL,0);
	if(!connection)printf("Fail to connection to MySQL ...\n");
}

void executeUpdate(String StructuredQueryLanguage)
{
	mysql_query(connection,StructuredQueryLanguage);
}

ResultSet executeQuery(String StructuredQueryLanguage)
{
	mysql_query(connection,StructuredQueryLanguage);
	ResultSet resultSet=mysql_store_result(connection);
	return resultSet;
}
int next(ResultSet resultSet)
{
	mySQLRow=mysql_fetch_row(resultSet);
	if(mySQLRow==NULL)return 0;
	return 1;
}

String getString(ResultSet resultSet,String fieldName)
{
	int field=-1;
	MYSQL_FIELD* mySQLField=mysql_fetch_field_direct(resultSet,++field);
	while(mySQLField!=NULL)
	{
		if(equals(mySQLField->name,fieldName))break;
		mySQLField=mysql_fetch_field_direct(resultSet,++field);
	}
	if(mySQLRow[field]==NULL)return "null";
	else return (String)mySQLRow[field];
}
void closeConnection()
{
	mysql_close(connection);
}



#define Node struct Node
Node
{
	String NO;
	Node* next;
};
Node* first;
Node* last;
int listLength=0;
Node* newNode(String NO)
{
	Node* node=(Node*)malloc(sizeof(Node));
	node->NO=NO;
	node->next=NULL;
	return node;
}
void addNodeIntoList(String NO)
{

	Node* node=newNode(NO);
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


void readNodesFromFile(String fileName)
{
	int length=0,i;
	String string0=newString(0);
	String string1=newString(0);
	FILE* file=fopen(fileName,"r");
	if(file==NULL)return;
	fscanf(file,"%d",&length);
	for(i=0;i<length;i++)
	{
		fscanf(file,"%s ",string0);
		fscanf(file,"%s ",string1);
		addNodeIntoList(add(string0,string1));
	}
	fclose(file);
}

int writeNodesIntoFile(String fileName)
{
	int i;
	FILE* file=fopen(fileName,"w");
	if(file==NULL)return 0;
	fprintf(file,"%d\n",listLength);
	Node* n=first;
	for(i=0;i<listLength;i++,n=n->next)
	{
		String NO=n->NO;
		fprintf(file,"%s\n",substring(NO,0,5));
		fprintf(file,"%s\n",substring(NO,5,10));
	}
	fclose(file);
	return 1;
}



int CHECK_SUCCESS=0;
int CHECK_NO_BALANCE=1;
int CHECK_ON_SERVICE=2;
int CHECK_NOT_EXIST=3;
String  cardUserInfoFile="CAndCardReader.userInfo";
void readCardUserInfo()
{
	readNodesFromFile(cardUserInfoFile);
}
void writeCardUserInfo()
{
	writeNodesIntoFile(cardUserInfoFile);
}
void printCardUserInfo()
{
	int i;
	Node* n=first;
	for(i=0;i<listLength;i++,n=n->next)
	{
		printf("user NO is : %s\n",n->NO);
	}
}
int checkCardUserNO(String NO)
{
	String StructuredQueryLanguage=add("Select * From UserInfoTable Where NO=",NO);
	ResultSet ResultSet1=executeQuery(StructuredQueryLanguage);
	if(next(ResultSet1))
	{
		String balance=getString(ResultSet1,"Balance");
		String name=getString(ResultSet1,"Name");
		printf("Balance=%s\tName=%s\n",balance,name);
		if(parseInt(balance)>0)return CHECK_SUCCESS;
		else return CHECK_NO_BALANCE;
	}
	return CHECK_NOT_EXIST;
}
int startServiceForNO(String NO)
{
	int checkResult=checkCardUserNO(NO);
	if(checkResult!=CHECK_SUCCESS)return checkResult;
	if(existInList(NO))return CHECK_ON_SERVICE;
	addNodeIntoList(NO);
	return CHECK_SUCCESS;
}
int stopServiceForNO(String NO)
{
	if(existInList(NO))
	{
		deleteNodeFromList(NO);
		return CHECK_ON_SERVICE;
	}
	return -1;
}
void reduceCardUserBalance()
{
	int i;
	Node* n=first;
	for(i=0;i<listLength;i++,n=n->next)
	{
		String StructuredQueryLanguage=add("Select Balance From UserInfoTable Where NO='",add(n->NO,"'"));
		ResultSet ResultSet1=executeQuery(StructuredQueryLanguage);
		if(next(ResultSet1))
		{
			int newBalance=parseInt(getString(ResultSet1,"Balance"))-1;
			StructuredQueryLanguage=add("Update UserInfoTable Set Balance=",toString(newBalance));
			StructuredQueryLanguage=add(StructuredQueryLanguage,add(" Where NO='",add(n->NO,"'")));
printf("%s\n",StructuredQueryLanguage);
			executeUpdate(StructuredQueryLanguage);
		}
	}
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


int counter=0;
String NO=NULL;
void runCardReader()
{

	String cardNO=getCardNO();
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
			Sleep(2000);
		}
	}
	else 
	{
		if(counter==1)
		{
			int result=startServiceForNO(NO);
			if(result==CHECK_SUCCESS)printf("The Service For User NO. : %s is start ...\n",NO);
			else
			{
				errorBeep();
				if(result==CHECK_ON_SERVICE)printf("The Service For User NO. : %s has already started ...\n",NO);
				else if(result==CHECK_NOT_EXIST)printf("The User NO. : %s is not in database ...\n",NO);
				else if(result==CHECK_NO_BALANCE)printf("The User NO. : %s has no balance ...\n",NO);
				else printf("The User NO. : %s is Not valid ...\n",NO);
				counter=0;
			}
		}
		else if(counter==2)
		{
			int result=stopServiceForNO(NO);
			if(result==CHECK_ON_SERVICE)printf("The Service For User NO. : %s is stoped \n",NO);
			else
			{
				errorBeep();
				printf("User NO. %s has already finished  ...\n",NO);
				counter=0;
			}
		}
		counter=0;
	}
}
int main()
{
	getConnection("CardReaderDatabase","root","11235813");
	readCardUserInfo();
	printCardUserInfo();
	while(1)
	{	
		runCardReader();
		Sleep(1000);
		timer();
		printTime();
		writeCardUserInfo();
		reduceCardUserBalance();
	}
	closeConnection();
}
