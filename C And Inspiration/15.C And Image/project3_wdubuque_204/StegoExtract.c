/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*
 * StegoExtract.c: A program for manipulating images                           *
 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "image.h"

unsigned char getlsbs(unsigned char* p)
	{
	unsigned char c=0x0;
	for (int i=0;i<8;i++)
		{
		unsigned char z=p[i]%2;
		c=c|z;
		if(i==7)
			{
			break;
			}
		c=c<<1;
		}
	return c;
	}
int main(int argc, char *argv[])
{  
  int i, j, k, cover_bits, bits;
  struct Buffer b = {NULL, 0, 0};
  struct Image img = {0, NULL, NULL, NULL, NULL, 0, 0};
  byte b0;
   
  if (argc != 3) 
    {
      printf("\n%s <stego_file> <file_to_extract> \n", argv[0]);
      exit(1);
    }
  ReadImage(argv[1],&img);       // read image file into the image buffer img
                                 // the image is an array of unsigned chars (bytes) of NofR rows
                                 // NofC columns, it should be accessed using provided macros

  // hidden information 
  // first four bytes is the size of the hidden file
  // next 4 bytes is the G number (4 bits per digit)
  if (!GetColor)
    cover_bits = img.NofC*img.NofR;
  else 
    cover_bits = 3*img.NofC*img.NofR;    

  b.size = 0;
  // extract four size bytes for the Buffer's size field
  // Set this to b.size
int cnt=31;
unsigned char p[8];
for(int i=0;i<4;i++)
	{
	for(int j=0;j<8;j++)
		{
		p[j]=GetGray(cnt);
		cnt--;
		}
	b.size=b.size|getlsbs(p);
	if(i==3)
		{
		break;
		}
	b.size=b.size<<8;
	}

  cnt=32;
  printf("\nsize: \n");
  printf("%d\n",b.size);
  b.data = malloc(b.size); // Allocates room for the output data file
  
  // extract the eight digits of your G# using 4 bits per digit

  unsigned char l=0;
  int d=0;
  for(int i=0;i<4;i++)
	{
	for(int j=0;j<8;j++)
		{
		p[j]=GetGray(cnt);
		cnt++;
		}
	l=getlsbs(p);
	printf("%d%d",(l>>4)&0x0f,(l&0x0f));
	
	}
unsigned char t=0;
cnt=64;
  for (i=0; i<b.size; i++)
    {
	for(int i=0;i<8;i++)
	{
	t=t|GetGray(cnt)%2;
	cnt++;
	if(i==7)
	{break;}
	t=t<<1;
      	}
	SetByte(i,t);
      // here you extract information from the image one byte at the time
      // note that you should extract only the least significant bits of the image
    }

  WriteBinaryFile(argv[2],b);  // output payload file
}