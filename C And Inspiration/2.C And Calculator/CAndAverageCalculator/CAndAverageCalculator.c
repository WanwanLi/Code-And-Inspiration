#include<stdio.h>
int getGPA(int s)
{
	if(s>=85)return 4;
	else if(s>=75)return 3;
	else if(s>=70)return 2;
	else if(s>=60)return 1;
	else return 0;
}
int main()
{
	int i=0;
	float w=1.0f;
	float sum=0.0f,n=0.0f;
	while(1)
	{
		scanf("%d",&i);
		if(i==-1)break;
		i=getGPA(i);
		scanf("%f",&w);
		sum+=i*w;
		n+=w;
	}
	float average=sum/n;
	printf("GPA is : %f/4.0\n",average);
	scanf("%d",&i);
}

