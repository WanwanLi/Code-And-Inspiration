#define NOP 0
#define EXIT 1
#define MOV 2
#define ADD 3
#define MUL 4
#define NEG 5
#define INC 6
#define DEC 7
#define ACC 8
#define PRO 9
#define ENQ 10
#define DEQ 11
#define CALL 12
#define AND 13
#define NOT 14
#define EQ 15
#define NE 16
#define LE 17
#define LT 18
#define GE 19
#define GT 20
#define JZ 21
#define JNZ 22
#define JMP 23
#define RET 24
#define CMOV 25
#define SWITCH 26
#define PUSH 27
#define POP 28
#define IN 29
#define OUT 30
#define OPEN 31
#define READ 32
#define WRITE 33
#define CLOSE 34
#define RUN 35
#define NEW 36
#define INVOKE 37
#define PI 38
#define SIN 39
#define COS 40
#define TAN 41
#define COT 42
#define SEC 43
#define CSC 44
#define ASIN 45
#define ACOS 46
#define ATAN 47
#define ACOT 48
#define ASEC 49
#define ACSC 50
#define ABS 51
#define POW 52
#define EXP 53
#define LOG 54
#define LG 55
#define LN 56
#define SQRT 57
#define RAND 58
#define FACT 59
#define PERM 60
#define COMB 61
#define SQU 62
#define SAW 63
#define SH 64
#define CH 65
#define TH 66
#define ARSH 67
#define ARCH 68
#define ARTH 69
#define GAU 70
#define TAY 71
#define NUM 72
#define END 2147483646
void printInstruction_NOP(FILE* file,int* instruction)
{
	fprintf(file,"NOP\n");
}
void printInstruction_EXIT(FILE* file,int* instruction)
{
	fprintf(file,"EXIT\n");
}
void printInstruction_MOV(FILE* file,int* instruction)
{
	fprintf(file,"MOV %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_NEG(FILE* file,int* instruction)
{
	fprintf(file,"NEG %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ADD(FILE* file,int* instruction)
{
	fprintf(file,"ADD %c%d,%c%d",instruction[1],instruction[2],instruction[3],instruction[4]);
	int i=5,sgn=instruction[i];
	while(sgn!=END)
	{
		fprintf(file,",%c%c%d",sgn,instruction[i+1],instruction[i+2]);
		i+=3;sgn=instruction[i];
	}
	fprintf(file,"\n");
}
void printInstruction_MUL(FILE* file,int* instruction)
{
	fprintf(file,"MUL %c%d,%c%d",instruction[1],instruction[2],instruction[3],instruction[4]);
	int i=5,sgn=instruction[i];
	while(sgn!=END)
	{
		fprintf(file,",%c%c%d",sgn,instruction[i+1],instruction[i+2]);
		i+=3;sgn=instruction[i];
	}
	fprintf(file,"\n");
}
void printInstruction_INC(FILE* file,int* instruction)
{
	fprintf(file,"INC %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_DEC(FILE* file,int* instruction)
{
	fprintf(file,"DEC %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_ACC(FILE* file,int* instruction)
{
	fprintf(file,"ACC %c%d,%c%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5]);
}
void printInstruction_PRO(FILE* file,int* instruction)
{
	fprintf(file,"PRO %c%d,%c%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5]);
}
void printInstruction_ENQ(FILE* file,int* instruction)
{
	fprintf(file,"ENQ %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_DEQ(FILE* file,int* instruction)
{
	fprintf(file,"DEQ %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_CALL(FILE* file,int* instruction)
{
	fprintf(file,"CALL %d\n",instruction[1]);
}
void printInstruction_AND(FILE* file,int* instruction)
{
	fprintf(file,"AND %c%d,%c%d",instruction[1],instruction[2],instruction[3],instruction[4]);
	int i=5,sgn=instruction[i];
	while(sgn!=END)
	{
		fprintf(file,",%c%c%d",sgn,instruction[i+1],instruction[i+2]);
		i+=3;sgn=instruction[i];
	}
	fprintf(file,"\n");
}
void printInstruction_NOT(FILE* file,int* instruction)
{
	fprintf(file,"NOT %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_EQ(FILE* file,int* instruction)
{
	fprintf(file,"EQ %c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6]);
}
void printInstruction_NE(FILE* file,int* instruction)
{
	fprintf(file,"NE %c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6]);
}
void printInstruction_LE(FILE* file,int* instruction)
{
	fprintf(file,"LE %c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6]);
}
void printInstruction_LT(FILE* file,int* instruction)
{
	fprintf(file,"LT %c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6]);
}
void printInstruction_GE(FILE* file,int* instruction)
{
	fprintf(file,"GE %c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6]);
}
void printInstruction_GT(FILE* file,int* instruction)
{
	fprintf(file,"GT %c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6]);
}
void printInstruction_JZ(FILE* file,int* instruction)
{
	fprintf(file,"JZ %c%d,%d\n",instruction[1],instruction[2],instruction[3]);
}
void printInstruction_JNZ(FILE* file,int* instruction)
{
	fprintf(file,"JNZ %c%d,%d\n",instruction[1],instruction[2],instruction[3]);
}
void printInstruction_JMP(FILE* file,int* instruction)
{
	fprintf(file,"JMP %d\n",instruction[1]);
}
void printInstruction_RET(FILE* file,int* instruction)
{
	fprintf(file,"RET\n");
}
void printInstruction_CMOV(FILE* file,int* instruction)
{
	fprintf(file,"CMOV %c%d,%c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6],instruction[7],instruction[8]);
}
void printInstruction_SWITCH(FILE* file,int* instruction)
{
	fprintf(file,"SWITCH %c%d",instruction[1],instruction[2]);
	int i=3,sgn=instruction[i];
	while(1)
	{
		if(sgn==END-1){fprintf(file,",~%d",instruction[i+1]);break;}
		if(sgn==END-2){fprintf(file,",->%d",instruction[i+1]);break;}
		fprintf(file,",%d,%d,%c%d",sgn,instruction[i+1],instruction[i+2],instruction[i+3]);
		i+=4;sgn=instruction[i];
	}
	fprintf(file,"\n");
}
void printInstruction_PUSH(FILE* file,int* instruction)
{
	fprintf(file,"PUSH %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_POP(FILE* file,int* instruction)
{
	fprintf(file,"POP %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_IN(FILE* file,int* instruction)
{
	fprintf(file,"IN %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_OUT(FILE* file,int* instruction)
{
	fprintf(file,"OUT %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_OPEN(FILE* file,int* instruction)
{
	fprintf(file,"OPEN %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_READ(FILE* file,int* instruction)
{
	fprintf(file,"READ %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_WRITE(FILE* file,int* instruction)
{
	fprintf(file,"WRITE %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_CLOSE(FILE* file,int* instruction)
{
	fprintf(file,"CLOSE\n");
}
void printInstruction_RUN(FILE* file,int* instruction)
{
	fprintf(file,"RUN %d\n",instruction[1]);
}
void printInstruction_NEW(FILE* file,int* instruction)
{
	fprintf(file,"NEW %d,%d\n",instruction[1],instruction[2]);
}
void printInstruction_INVOKE(FILE* file,int* instruction)
{
	fprintf(file,"INVOKE %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_PI(FILE* file,int* instruction)
{
	fprintf(file,"PI %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_SIN(FILE* file,int* instruction)
{
	fprintf(file,"SIN %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_COS(FILE* file,int* instruction)
{
	fprintf(file,"COS %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_TAN(FILE* file,int* instruction)
{
	fprintf(file,"TAN %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_COT(FILE* file,int* instruction)
{
	fprintf(file,"COT %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_SEC(FILE* file,int* instruction)
{
	fprintf(file,"SEC %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_CSC(FILE* file,int* instruction)
{
	fprintf(file,"CSC %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ASIN(FILE* file,int* instruction)
{
	fprintf(file,"ASIN %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ACOS(FILE* file,int* instruction)
{
	fprintf(file,"ACOS %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ATAN(FILE* file,int* instruction)
{
	fprintf(file,"ATAN %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ACOT(FILE* file,int* instruction)
{
	fprintf(file,"ACOT %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ASEC(FILE* file,int* instruction)
{
	fprintf(file,"ASEC %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ACSC(FILE* file,int* instruction)
{
	fprintf(file,"ACSC %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ABS(FILE* file,int* instruction)
{
	fprintf(file,"ABS %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_POW(FILE* file,int* instruction)
{
	fprintf(file,"POW %c%d,%c%d",instruction[1],instruction[2],instruction[3],instruction[4]);
	int i=5,sgn=instruction[i];
	while(sgn!=END)
	{
		fprintf(file,",%c%c%d",sgn,instruction[i+1],instruction[i+2]);
		i+=3;sgn=instruction[i];
	}
	fprintf(file,"\n");
}
void printInstruction_EXP(FILE* file,int* instruction)
{
	fprintf(file,"EXP %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_LOG(FILE* file,int* instruction)
{
	fprintf(file,"LOG %c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6]);
}
void printInstruction_LG(FILE* file,int* instruction)
{
	fprintf(file,"LG %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_LN(FILE* file,int* instruction)
{
	fprintf(file,"LN %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_SQRT(FILE* file,int* instruction)
{
	fprintf(file,"SQRT %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_RAND(FILE* file,int* instruction)
{
	fprintf(file,"RAND %c%d\n",instruction[1],instruction[2]);
}
void printInstruction_FACT(FILE* file,int* instruction)
{
	fprintf(file,"FACT %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_PERM(FILE* file,int* instruction)
{
	fprintf(file,"PERM %c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6]);
}
void printInstruction_COMB(FILE* file,int* instruction)
{
	fprintf(file,"COMB %c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6]);
}
void printInstruction_SQU(FILE* file,int* instruction)
{
	fprintf(file,"SQU %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_SAW(FILE* file,int* instruction)
{
	fprintf(file,"SAW %c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6]);
}
void printInstruction_SH(FILE* file,int* instruction)
{
	fprintf(file,"SH %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_CH(FILE* file,int* instruction)
{
	fprintf(file,"CH %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_TH(FILE* file,int* instruction)
{
	fprintf(file,"TH %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ARSH(FILE* file,int* instruction)
{
	fprintf(file,"ARSH %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ARCH(FILE* file,int* instruction)
{
	fprintf(file,"ARCH %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_ARTH(FILE* file,int* instruction)
{
	fprintf(file,"ARTH %c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4]);
}
void printInstruction_GAU(FILE* file,int* instruction)
{
	fprintf(file,"GAU %c%d,%c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6],instruction[7],instruction[8]);
}
void printInstruction_TAY(FILE* file,int* instruction)
{
	fprintf(file,"TAY %c%d,%c%d,%c%d,%c%d\n",instruction[1],instruction[2],instruction[3],instruction[4],instruction[5],instruction[6],instruction[7],instruction[8]);
}
void (*printInstruction[NUM])(FILE* file,int* instruction);
void initPrintInstruction()
{
	printInstruction[NOP]=printInstruction_NOP;
	printInstruction[EXIT]=printInstruction_EXIT;
	printInstruction[MOV]=printInstruction_MOV;
	printInstruction[ADD]=printInstruction_ADD;
	printInstruction[MUL]=printInstruction_MUL;
	printInstruction[NEG]=printInstruction_NEG;
	printInstruction[INC]=printInstruction_INC;
	printInstruction[DEC]=printInstruction_DEC;
	printInstruction[ACC]=printInstruction_ACC;
	printInstruction[PRO]=printInstruction_PRO;
	printInstruction[ENQ]=printInstruction_ENQ;
	printInstruction[DEQ]=printInstruction_DEQ;
	printInstruction[CALL]=printInstruction_CALL;
	printInstruction[AND]=printInstruction_AND;
	printInstruction[NOT]=printInstruction_NOT;
	printInstruction[EQ]=printInstruction_EQ;
	printInstruction[NE]=printInstruction_NE;
	printInstruction[LE]=printInstruction_LE;
	printInstruction[LT]=printInstruction_LT;
	printInstruction[GE]=printInstruction_GE;
	printInstruction[GT]=printInstruction_GT;
	printInstruction[JZ]=printInstruction_JZ;
	printInstruction[JNZ]=printInstruction_JNZ;
	printInstruction[JMP]=printInstruction_JMP;
	printInstruction[RET]=printInstruction_RET;
	printInstruction[CMOV]=printInstruction_CMOV;
	printInstruction[SWITCH]=printInstruction_SWITCH;
	printInstruction[PUSH]=printInstruction_PUSH;
	printInstruction[POP]=printInstruction_POP;
	printInstruction[IN]=printInstruction_IN;
	printInstruction[OUT]=printInstruction_OUT;
	printInstruction[OPEN]=printInstruction_OPEN;
	printInstruction[READ]=printInstruction_READ;
	printInstruction[WRITE]=printInstruction_WRITE;
	printInstruction[CLOSE]=printInstruction_CLOSE;
	printInstruction[RUN]=printInstruction_RUN;
	printInstruction[NEW]=printInstruction_NEW;
	printInstruction[INVOKE]=printInstruction_INVOKE;
	printInstruction[PI]=printInstruction_PI;
	printInstruction[SIN]=printInstruction_SIN;
	printInstruction[COS]=printInstruction_COS;
	printInstruction[TAN]=printInstruction_TAN;
	printInstruction[COT]=printInstruction_COT;
	printInstruction[SEC]=printInstruction_SEC;
	printInstruction[CSC]=printInstruction_CSC;
	printInstruction[ASIN]=printInstruction_ASIN;
	printInstruction[ACOS]=printInstruction_ACOS;
	printInstruction[ATAN]=printInstruction_ATAN;
	printInstruction[ACOT]=printInstruction_ACOT;
	printInstruction[ASEC]=printInstruction_ASEC;
	printInstruction[ACSC]=printInstruction_ACSC;
	printInstruction[ABS]=printInstruction_ABS;
	printInstruction[POW]=printInstruction_POW;
	printInstruction[EXP]=printInstruction_EXP;
	printInstruction[LOG]=printInstruction_LOG;
	printInstruction[LG]=printInstruction_LG;
	printInstruction[LN]=printInstruction_LN;
	printInstruction[SQRT]=printInstruction_SQRT;
	printInstruction[RAND]=printInstruction_RAND;
	printInstruction[FACT]=printInstruction_FACT;
	printInstruction[PERM]=printInstruction_PERM;
	printInstruction[COMB]=printInstruction_COMB;
	printInstruction[SQU]=printInstruction_SQU;
	printInstruction[SAW]=printInstruction_SAW;
	printInstruction[SH]=printInstruction_SH;
	printInstruction[CH]=printInstruction_CH;
	printInstruction[TH]=printInstruction_TH;
	printInstruction[ARSH]=printInstruction_ARSH;
	printInstruction[ARCH]=printInstruction_ARCH;
	printInstruction[ARTH]=printInstruction_ARTH;
	printInstruction[GAU]=printInstruction_GAU;
	printInstruction[TAY]=printInstruction_TAY;
}
void printInstructions(FILE* file,int** instructions,int instructionsLength)
{
	initPrintInstruction();
	int i;for(i=0;i<instructionsLength;i++)
	{
		fprintf(file,"%d: ",i);
		printInstruction[instructions[i][0]](file,instructions[i]);
	}
}
int** toInstructions(List* instructionsList,int instructionsLength)
{
	int i;List* list;Node* n=instructionsList->first;boolean addEND=false;
	int** instructions=(int**)malloc(instructionsLength*sizeof(int*));
	for(i=0;i<instructionsLength;i++)
	{
		list=newList();
		int d=n->integer;
		if(d==ADD||d==MUL||d==POW||d==AND)addEND=true;
		while(d!=END)
		{
			addInteger(list,d);
			n=n->next;
			d=n->integer;
		}
		if(addEND)addInteger(list,END);
		instructions[i]=toIntegers(list);
		n=n->next;
	}
	return instructions;
}
void replaceInstruction(List* instructionsList,int instructionPointer,int int0,...)
{
	int ip=0;Node* n=instructionsList->first,*t;
	while(true)
	{
		if(n==null)return;
		int d=n->integer;
		while(d!=END)
		{
			n=n->next;
			d=n->integer;
		}
		if(++ip==instructionPointer)
		{
			t=n->next;
			d=t->integer;
			while(d!=END)
			{
				t=t->next;
				d=t->integer;
				instructionsList->length--;
			}
			break;
		}
		n=n->next;
	}
	if(n==null)return;
	int i,int_i;
	va_list argList;
	va_start(argList,int0);
	for(i=0,int_i=int0;int_i!=LastInteger;int_i=va_arg(argList,int))
	{
		n->next=newNode(int_i,0,null);
		n=n->next;
		instructionsList->length++;
	}
	va_end(argList);
	n->next=t;
}
void replaceInstructionList(List* instructionsList,int instructionPointer,List* newInstructionsList)
{
	if(newInstructionsList->length==0)return;
	int ip=0;Node* n=instructionsList->first,*t;
	while(true)
	{
		if(n==null)return;
		int d=n->integer;
		while(d!=END)
		{
			n=n->next;
			d=n->integer;
		}
		if(++ip==instructionPointer)
		{
			t=n->next;
			d=t->integer;
			while(d!=END)
			{
				t=t->next;
				d=t->integer;
				instructionsList->length--;
			}
			break;
		}
		n=n->next;
	}
	if(n==null)return;
	n->next=newInstructionsList->first;
	newInstructionsList->last->next=t;
	instructionsList->length+=newInstructionsList->length;
}
