#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "matlib/engine.h"
 
#pragma comment( lib, "libeng.lib" )
#pragma comment( lib, "libmx.lib" )
#pragma comment( lib, "libmat.lib" )
 
int main()
{
Engine *ep;
if (!(ep = engOpen("\0")))
{
fprintf(stderr, "\nCan't start MATLAB engine\n");
return EXIT_FAILURE;
}
 
int Nsample = 50;
const double PI = 3.1415926;
double *t = new double[Nsample] ;
 
for(int i = 0; i < Nsample; i++)
{
t[i] = i * 2 * PI / Nsample;
}
 
 
mxArray *T = NULL, *result = NULL;
T = mxCreateDoubleMatrix(1, Nsample, mxREAL);
memcpy((void *)mxGetPr(T), (void *)t, Nsample*sizeof(t[0]));
 
engPutVariable(ep, "T", T);
engEvalString(ep, "Y=sin(T);");
engEvalString(ep, "plot(T,Y);");
engEvalString(ep, "title('y=sin(t)');");
engEvalString(ep, "xlabel('t');");
engEvalString(ep, "ylabel('y');");
 
printf("Hit return to continue\n\n");
fgetc(stdin);
 
mxDestroyArray(T);
engEvalString(ep, "close;");
 
engClose(ep);
return EXIT_SUCCESS;
}
