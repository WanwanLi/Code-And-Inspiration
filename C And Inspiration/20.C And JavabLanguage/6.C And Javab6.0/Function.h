#define FUNC_MAX_PARAM_NUM 21
#define DEFAULT_FUNC_STEP 100
#define Function struct Function
Function
{
	int* paramPointers;
	int beginPointer;
	int endPointer;
	int doublePointer;
	int vectorPointer;
	Function* next;
};
Function* newFunction(int* paramPointers)
{
	Function* function=(Function*)malloc(sizeof(Function));
	function->paramPointers=paramPointers;
	function->beginPointer=-1;
	function->endPointer=-1;
	function->doublePointer=-1;
	function->vectorPointer=-1;
	function->next=null;
	return function;
}
#define FunctionList struct FunctionList
FunctionList
{
	Function* first;
	Function* last;
	int length;
};
FunctionList* newFunctionList()
{
	FunctionList* functionList=(FunctionList*)malloc(sizeof(FunctionList));
	functionList->first=null;
	functionList->last=null;
	functionList->length=0;
	return functionList;
}
void addFunction(FunctionList* functionList,Function* function)
{
	if(functionList->length==0)
	{
		functionList->first=function;
		functionList->last=function;
	}
	else
	{
		functionList->last->next=function;
		functionList->last=function;
	}
	functionList->length++;
}
Function* getFunction(FunctionList* functionList,int index)
{
	Function* function=functionList->first;int i;
	for(i=0;i<functionList->length&&i<index;i++)function=function->next;
	return function;
}




