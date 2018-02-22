/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*
 * Stego.c: A program for manipulating images                           *
 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include "image.h"

void setlsbs(unsigned char *p, unsigned char b0)
{
unsigned char c=b0;
for(int i=0;i<8;i++)
	{
	p[i]=(p[i] & 0xfe) | c%2;
        c=c>>1;
	}
}

int main(int argc, char *argv[])
{  
  int i, j, k, cover_bits, bits;
  struct Buffer b = {NULL, 0, 0};
  struct Image img = {0, NULL, NULL, NULL, NULL, 0, 0};
  byte b0;
   
  if (argc != 4) 
    {
      printf("\n%s <cover_file> <stego_file> <file_to_hide> \n", argv[0]);
      exit(1);
    }
  ReadImage(argv[1],&img);       // read image file into the image buffer img
                                 // the image is an array of unsigned chars (bytes) of NofR rows
                                 // NofC columns, it should be accessed using provided macros
  ReadBinaryFile(argv[3],&b);    // Read binary data
 

  // hidden information 
  // first four bytes is the size of the hidden file
  // next 4 bytes is the G number (4 bits per digit)
  if (!GetColor)
    cover_bits = img.NofC*img.NofR;
  else 
    cover_bits = 3*img.NofC*img.NofR;    
  bits = (8 + b.size)*8;
  if (bits > cover_bits)
    {
      printf("Cover file is not large enough %d (bits) > %d (cover_bits)\n",bits,cover_bits);
      exit(1);
    }

  // embed four size bytes for the Buffer's size field
  // 
  //find least sig bit of 32 bytes 
  //set least sig bit of those 32 bytes to the 4 bytes for the size
 
  unsigned char p[8];
  unsigned char c;
  int cnt=0;
  for(int i=0;i<4;i++)
	{
	for (int j=0;j<8;j++)
		{
		p[j]=GetGray(cnt);
		cnt++;
		c=((b.size>>8*i)&0xff);
		}
		setlsbs(p,c);
	cnt=cnt-8;
	for(int i=0;i<8;i++)
		{
		SetGray(cnt,p[i]);
		cnt++;
		}
	}

  // embed the eight digits of your G# using 4 bits per digit
  // 
  //gray at 32
  //4 bits per digit of gnumber 01001452
  int cnt2=0;

  int v[8]={0,1,0,0,1,4,5,2};
  unsigned char t=0x0;
  for(int i=0;i<4;i++)
	{
	t=0;
	for(int j=0;j<2;j++)
		{
		t=t|v[cnt2];
		cnt2++;
		if(j==1)
		{
			break;
		}
		t=t<<4;
		}
	for(int j=0;j<8;j++)
		{
		p[i]=GetGray(cnt);
		cnt++;
		}
	setlsbs(p,t);
	cnt=cnt-8;
	for(int j=7;j>=0;j--)
	{
	SetGray(cnt,p[j]);
	cnt++;
	}
	}


  for (i=0; i<b.size; i++)
    {
	for(int j=0;j<8;j++)
	{
	p[j]=GetGray(cnt);
	cnt++;
	}
	setlsbs(p,GetByte(i));
	cnt=cnt-8;
	for(int j=7;j>=0;j--)
	{
	SetGray(cnt,p[j]);
	cnt++;
	}
      // here you embed information into the image one byte at the time
      // note that you should change only the least significant bits of the image
    }

  WriteImage(argv[2],img);  // output stego file (cover_file + file_to_hide)
}