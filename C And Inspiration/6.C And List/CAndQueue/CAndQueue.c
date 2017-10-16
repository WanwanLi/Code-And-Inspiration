int* newInt(int length)
{
	int* integers=(int*)(malloc(length*sizeof(int)));
	return integers;
}


int* queue;
int queueLength=0;
void newQueue(int integer)
{
	queueLength++;
	queue=newInt(1);
	queue[0]=integer;
}
void enQueue(int integer)
{
	if(queueLength==0){newQueue(integer);return;}
	int i;
	int* queue1=newInt(queueLength+1);
	for(i=0;i<queueLength;i++)queue1[i]=queue[i];
	queue1[queueLength]=integer;
	queueLength++;
	queue=queue1;
}
int deQueue()
{
	int i;
	int* queue1=newInt(queueLength-1);
	int result=queue[0];
	for(i=1;i<queueLength;i++)queue1[i-1]=queue[i];
	queueLength--;
	queue=queue1;
	return result;
}
void displayQueue()
{
	int i;
	printf("Queue is:");
	for(i=0;i<queueLength;i++)
	{
		printf("%d ",queue[i]);
	}
	printf("\n");
}
int existInQueue(int integer)
{
	int i;
	for(i=0;i<queueLength;i++)
	{
		if(queue[i]==integer)return 1;
	}
	return 0;
}
int deleteFromQueue(int integer)
{
	int i;
	for(i=0;i<queueLength;i++)if(integer==queue[i])break;
	if(i==queueLength)return -1;
	int index=i;
	int* queue1=newInt(queueLength-1);
	for(i=0;i<queueLength-1;i++)
	{
		if(i<index)queue1[i]=queue[i];
		else queue1[i]=queue[i+1];
	}
	queueLength--;
	queue=queue1;
	return index;
}

int removeFromQueue(int index)
{
	int result=queue[index],i;
	int* queue1=newInt(queueLength-1);
	for(i=0;i<queueLength-1;i++)
	{
		if(i<index)queue1[i]=queue[i];
		else queue1[i]=queue[i+1];
	}
	queueLength--;
	queue=queue1;
	return result;
}

int main()
{
	int i;
	for(i=0;i<10;i++)enQueue(i);
	displayQueue();
	int index=deleteFromQueue(5);
	printf("index=%d\n",index);
	displayQueue();
	printf("result=%d\n",removeFromQueue(index));
	while(queueLength>0){printf("deQueue is %d  ",deQueue());displayQueue();}
	return 0;
}
