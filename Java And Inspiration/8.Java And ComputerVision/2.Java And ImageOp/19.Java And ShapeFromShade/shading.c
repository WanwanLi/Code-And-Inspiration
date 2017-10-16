/********************************************************/
/* This is the source code for the simple SFS algorithm */
/* It will read the UCF format image, and output the    */
/* estimated depth maps as a raw data file.             */
/* It can be complied as follow:			*/
/*  	cc -g -o shading shading.c -lm			*/
/********************************************************/

#include <stdio.h>
#include <math.h>

#define Size 128        /* image size 128 x 128 */
#define MAX(a,b)        (((a) < (b)) ? (b) : (a))

typedef unsigned char BYTE;

typedef struct
{
        int type;
        unsigned int    maxX,
                        maxY;
        unsigned char   *image;
}       PIC;

PIC UCFReadPic(infile)
FILE *infile;
{
   PIC temp;

   /* getting Type from image data */
   fread(&temp.type,sizeof(temp.type),1,infile);
   switch (temp.type)
   {
      case 0xF10F:
      case 0xF200:
      case 0xF201:
      case 0xF204:
      case 0x0000:
      {  
         fread(&temp.maxX,sizeof(temp.maxX),1,infile);
         fread(&temp.maxY,sizeof(temp.maxY),1,infile);
         break;
      }
      case 0x8000:
      case 0x8001:
      case 0xB003:
      default  :
      {
         fread(&temp.maxX,sizeof(temp.maxX),1,infile);
         fread(&temp.maxY,sizeof(temp.maxY),1,infile);
         break;
      }
   }
   if((temp.image=(BYTE*)calloc(temp.maxX*temp.maxY,sizeof(BYTE)))==NULL)
   {
      temp.maxX = temp.maxY = 0;
      temp.image = NULL;
      return(temp);
   }

   fread(temp.image,sizeof(BYTE),temp.maxX * temp.maxY,infile);
   return(temp);
}

main()
{
 char   filename[80];
 FILE   *outfile,*infile;
 int i,j,I,iter;
 float Ps,Qs,p,q,pq,PQs,fZ,dfZ,Eij,Wn=0.0001*0.0001,Y,K;
 float Zn[Size][Size],Zn1[Size][Size],Si1[Size][Size],Si[Size][Size];
 PIC pic1;

/* assume the initial estimate zero at time n-1 */
 for(i=0;i<Size;i++)
  for(j=0;j<Size;j++){
   Zn1[i][j] = 0.0;
   Si1[i][j] = 0.01; }
 printf("Input number of iterations : ");
 scanf("%d",&iter);
 printf("\nInput the image filename : ");
 scanf("%s",filename);
 infile = fopen(filename,"r");
 pic1 = UCFReadPic(infile);
 printf("\nInput the light source direction : ");
 printf("\nPs = ");
 scanf("%f",&Ps);
 printf("\nQs = ");
 scanf("%f",&Qs);

/************************************************************************/
 for(I=1;I<=iter;I++){
  for(i=0;i<Size;i++)
   for(j=0;j<Size;j++){ /* calculate -f(Zij) & df(Zij) */
    if(j-1 < 0 || i-1 < 0) /* take care boundary */
      p = q = 0.0;
    else {
          p = Zn1[i][j] - Zn1[i][(j-1)];
          q = Zn1[i][j] - Zn1[i-1][j]; }
    pq = 1.0 + p*p + q*q;
    PQs = 1.0 + Ps*Ps + Qs*Qs;
    Eij = pic1.image[i*pic1.maxX+j]/255.0;
    fZ = -1.0*(Eij - MAX(0.0,(1+p*Ps+q*Qs)/(sqrt(pq)*sqrt(PQs))));
    dfZ = -1.0*((Ps+Qs)/(sqrt(pq)*sqrt(PQs))-(p+q)*(1.0+p*Ps+q*Qs)/
                       (sqrt(pq*pq*pq)*sqrt(PQs))) ;
    Y = fZ + dfZ*Zn1[i][j];
    K = Si1[i][j]*dfZ/(Wn+dfZ*Si1[i][j]*dfZ);
    Si[i][j] = (1.0 - K*dfZ)*Si1[i][j]; 
    Zn[i][j] = Zn1[i][j] + K*(Y-dfZ*Zn1[i][j]);}

  printf("\nOutput depth map !\n");
  sprintf(filename,"tmap%d.out",I);
  outfile = fopen(filename,"w");
  for(i=0;i<Size;i++)
   for(j=0;j<Size;j++){
    fprintf(outfile,"%f\n",Zn[i][j]);
    Zn1[i][j] = Zn[i][j];
    Si1[i][j] = Si[i][j];}
  fclose(outfile); }
} /* end of main */
