#include "graphics3d.h"
int main()
{
	Vector* i=newVector(1,-1,1);
	Vector* n=newVector(0,1,0);
	Vector* r=reflectedVector(i,n);
	printf("r=(%f,%f,%f)\n",r->x,r->y,r->z);
}
