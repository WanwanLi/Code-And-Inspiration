/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*
 * StegoExtract.c: A program for manipulating images                           *
 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#include <string.h>
#include "image.h"

unsigned char getlsbs(unsigned char *p);

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
  int y =0; //a temp variable for b.size
  unsigned char A;
  int a = 0; //a temp variable for b.size
  int getGrayC = 0; //a counter for GetGray()
  int getRedC = 0; //a counter for GetRed()
  int getGreenC = 0; //a counter for GetGreen()
  int getBlueC = 0; //a counter for GetBlue()
  int getColorCounter = 0; //a counter to keep tack of what color you should be getting
  unsigned char arr[8]; //storage for a byte
  for(int i=3; i>=0; i--){ //for each byte 
    for(int x=0; x<8; x++){ //for each of those 8 bits
      if(GetColor){ //if the image is color
        if(getColorCounter%3 == 0){ //red is first, and should happen 1/3 times
          arr[x] = GetRed(getRedC); //fills position x of arr with a red byte (pixel)
          getRedC++; //incriments the position of the next time you get a red byte
        }
        if(getColorCounter%3 == 1){ //green is second, and should happen 1/3 times
          arr[x] = GetGreen(getGreenC); //fills position x of arr with a green byte (pixel)
          getGreenC++; //incriments the position of the next time you get a green byte (pixel)
        }
        if(getColorCounter%3 == 2){ //blue is third, and should happen 1/3 times
          arr[x] = GetBlue(getBlueC); //fills position x of arr with a blue byte (pixel)
          getBlueC++; //incriments the position of the next time you get a blue byte (pixel)
        }
        getColorCounter++; //keeps track of the color that was just gotten and moves to the next color
      }
      else{ //the image is not color
        arr[x] = GetGray(getGrayC); //fills position x of arr with a gray byte (pixel)
        getGrayC++; //incriments the position of the next time you get a gray byte (pixel)
      }
    }
    A = getlsbs(arr); //gets the lsbs from that array
    a=0; //sets a to 0
    a = a|A; //combines the byte A with the int a
    a = a<<24; //shifts the byte you got all the way to the left
    y = a|y; //combines the int a with the int y
    if (i != 0) //makes sure you only shift 3 times so bits arent lost
      y = ((unsigned int)y) >>8; //here you cast to unsigned int because for gcc if the left most digit is a 1 and you with shift instead of 0s you will get 1s
  }
  b.size = y; 
  printf("\nb.size=%d\n", b.size); //prints b.size
  b.data = malloc(b.size); // Allocates room for the output data file
  if (b.data == NULL){ //if there is an error allocating memory, print error and exit
    printf("ERROR ALLOCATING MEMORY");
    exit(1);
  }
  // extract the eight digits of your G# using 4 bits per digit
  int gnum[8] = {0,0,0,0,0,0,0,0};
  for(int i=0; i<4; i++){ //for each byte
    for(int x=0; x<8; x++){ //for each of those 8 bits
      if(GetColor){ //if the image is color
        if(getColorCounter%3 == 0){ //red is first, and should happen 1/3 times
          arr[x] = GetRed(getRedC); //fills position x of arr with a red byte (pixel)
          getRedC++; //incriments the position of the next time you get a red byte
        }
        if(getColorCounter%3 == 1){ //green is second, and should happen 1/3 times
          arr[x] = GetGreen(getGreenC); //fills position x of arr with a green byte (pixel)
          getGreenC++; //incriments the position of the next time you get a green byte (pixel)
        }
        if(getColorCounter%3 == 2){ //blue is third, and should happen 1/3 times
          arr[x] = GetBlue(getBlueC); //fills position x of arr with a blue byte (pixel)
          getBlueC++; //incriments the position of the next time you get a blue byte (pixel)
        }
        getColorCounter++; //keeps track of the color that was just gotten and moves to the next color
      }
      else{ //the image is not color
        arr[x] = GetGray(getGrayC); //fills position x of arr with a gray byte (pixel)
        getGrayC++; //incriments the position of the next time you get a gray byte (pixel)
      }
    }
    A = getlsbs(arr); //gets the lsbs from that array
    gnum[i*2]= (A & 0xF0) | gnum[i*2]; //sets the number of the gnum to the first 4 bits of the byte
    gnum[i*2] = gnum[i*2] >> 4; 
    gnum[(i*2)+1]= (A & 0x0F) | gnum[(i*2)+1]; //sets the number of the gnum to the last 4 bits of the byte
  }
  printf("GNUM=");
  for(int x=0; x<8;x++){ //prints the gnum
    printf("%d",gnum[x]);
  }
 
  for (i=0; i<b.size; i++){
  // here you extract information from the image one byte at the time
  // note that you should extract only the least significant bits of the image
    for(int x=0; x<8; x++){ //for each of those 8 bits
      if(GetColor){ //if the image is color
        if(getColorCounter%3 == 0){ //red is first, and should happen 1/3 times
          arr[x] = GetRed(getRedC); //fills position x of arr with a red byte (pixel)
          getRedC++; //incriments the position of the next time you get a red byte
        }
        if(getColorCounter%3 == 1){ //green is second, and should happen 1/3 times
          arr[x] = GetGreen(getGreenC); //fills position x of arr with a green byte (pixel)
          getGreenC++; //incriments the position of the next time you get a green byte (pixel)
        }
        if(getColorCounter%3 == 2){ //blue is third, and should happen 1/3 times
          arr[x] = GetBlue(getBlueC); //fills position x of arr with a blue byte (pixel)
          getBlueC++; //incriments the position of the next time you get a blue byte (pixel)
        }
        getColorCounter++; //keeps track of the color that was just gotten and moves to the next color
      }
      else{ //if the image is not color
        arr[x] = GetGray(getGrayC); //fills position x of arr with a gray byte (pixel)
        getGrayC++; //incriments the position of the next time you get a gray byte (pixel)
      }
    }
    A = getlsbs(arr); //gets the lsbs from that array
    SetByte(i,A); //sets the byte of the payload
  }

  WriteBinaryFile(argv[2],b);  // output payload file
  free(b.data); //frees the allocated memory
}

//This function gets the last bit of each byte of an array of bytes
//It returns 1 byte composed of all the least significant bits
unsigned char getlsbs(unsigned char *p){
  unsigned char b0 = 0x0;
  for(int x=7; x>=0;x--){
    b0 = (p[x]&1) | (b0 <<= 1); //uses masking to only take the last bit of a byte from the array
  }
  return b0;
}






