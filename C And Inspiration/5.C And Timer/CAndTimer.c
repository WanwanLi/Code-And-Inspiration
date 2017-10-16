#include <stdio.h>
#include <time.h>
void delay(unsigned int interval)
{
	time_t time1=time(NULL),time2;
	for
	(
		time2=time1; 
		time2-time1<interval; 
		time2=time(NULL)
	);
}
int main()
{
	unsigned int interval=2;
 	time_t time1=time(NULL);
	delay(interval);
 	time_t time2=time(NULL);
	printf("The time delay is: %d seconds\n", time2-time1);
	return 0;
}
