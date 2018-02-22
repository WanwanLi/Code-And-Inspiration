/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*
 * Stego.c: A program for manipulating images                           *
 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#include <string.h>
#include "image.h"

void setlsbs(unsigned char *p, unsigned char b0);

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
  // 
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
  printf("b.size=%d \n", b.size);
  int getGrayC = 0; //a counter for GetGray()
  int setGrayC = 0; //a counter for SetGray()
  int getRedC = 0; //a counter for GetRed()
  int getGreenC = 0; //a counter for GetGreen()
  int getBlueC = 0; //a counter for GetBlue()
  int setRedC = 0; //a counter for SetRed()
  int setGreenC = 0; //a counter for SetGreen()
  int setBlueC = 0; //a counter for SetBlue()
  int getColorCounter = 0; //a counter to tell which pixel (rgb) to get
  int setColorCounter = 0; //a counter to tell which pixel (rgb) to set
  unsigned char arr[8]; //storage for a byte
  unsigned char A;
  for(int i=0; i<4; i++){ //for each byte
    A = (unsigned char)((b.size >> (i*8)) & 0xFF); //takes 8 bits from b.size
    for(int x=0; x<8; x++){ //for each of those 8 bits, take a byte of the image
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
    setlsbs(arr,A); //sets the lsbs for those bytes
    for(int x=0; x<8; x++){ //for each of those 8 bits, take a byte of the image
      if(GetColor){ //if the image is color
        if(setColorCounter%3 == 0){ //red is first, and should happen 1/3 times
          SetRed(setRedC,arr[x]); //sets the red byte (pixel) of position setRedC of the new image with the red byte (pixel) of arr
          setRedC++; //incriments the position of the next time you set a red byte
        }
        if(setColorCounter%3 == 1){ //green is second, and should happen 1/3 times
          SetGreen(setGreenC,arr[x]); //sets the green byte (pixel) of position setGreenC of the new image with the green byte (pixel) of arr
          setGreenC++; //incriments the position of the next time you set a green byte 
        }
        if(setColorCounter%3 == 2){ //blue is third, and should happen 1/3 times
          SetBlue(setBlueC,arr[x]); //sets the blue byte (pixel) of position setBlueC of the new image with the blue byte (pixel) of arr
          setBlueC++; //incriments the position of the next time you set a blue byte
        }
        setColorCounter++; //keeps track of the color that was just gotten and moves to the next color
      }
      else{ //if the image is not color
        SetGray(setGrayC, arr[x]); //sets the gray byte (pixel) of position setGrayC of the new image with the gray byte (pixel) of arr
        setGrayC++; //incriments the position of the next time you set a gray byte (pixel)
      }
    }
  }
  

  // embed the eight digits of your G# using 4 bits per digit
  unsigned char gnum[8] = {0,1,0,1,3,3,2,3};
  printf("GNUM=");
  for(int x=0; x<8;x++){ //prints out the gnumber
    printf("%d",gnum[x]);
  }
  printf("\n");

  for(int i=0; i<4; i++){ //for each byte
    A = (unsigned char)(gnum[i*2] <<= 4) | gnum[(i*2)+1]; //this combines 2 of the numbers that are next to each other into 1 byte of data
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
    setlsbs(arr, A); //sets the lsbs for those bytes
    for(int x=0; x<8; x++){ //for each of those 8 bits, take a byte of the image
      if(GetColor){ //if the image is color
        if(setColorCounter%3 == 0){ //red is first, and should happen 1/3 times
          SetRed(setRedC,arr[x]); //sets the red byte (pixel) of position setRedC of the new image with the red byte (pixel) of arr
          setRedC++; //incriments the position of the next time you set a red byte
        }
        if(setColorCounter%3 == 1){ //green is second, and should happen 1/3 times
          SetGreen(setGreenC,arr[x]); //sets the green byte (pixel) of position setGreenC of the new image with the green byte (pixel) of arr
          setGreenC++; //incriments the position of the next time you set a green byte
        }
        if(setColorCounter%3 == 2){ //blue is third, and should happen 1/3 times
          SetBlue(setBlueC,arr[x]); //sets the blue byte (pixel) of position setBlueC of the new image with the blue byte (pixel) of arr
          setBlueC++; //incriments the position of the next time you set a blue byte
        }
        setColorCounter++; //keeps track of the color that was just gotten and moves to the next color
      }
      else{ //if the image is not color
        SetGray(setGrayC, arr[x]); //sets the gray byte (pixel) of position setGrayC of the new image with the gray byte (pixel) of arr
        setGrayC++; //incriments the position of the next time you set a gray byte
      }
    }
  }	   
  for (i=0; i<b.size; i++){
  // here you embed information into the image one byte at the time
  // note that you should change only the least significant bits of the image   
    A = GetByte(i); //gets a byte of the payload
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
    setlsbs(arr, A); //sets the lsbs for those bytes
    for(int x=0; x<8; x++){ //for each of those 8 bits, take a byte of the image
      if(GetColor){ //if the image is color
        if(setColorCounter%3 == 0){ //red is first, and should happen 1/3 times
          SetRed(setRedC,arr[x]); //sets the red byte (pixel) of position setRedC of the new image with the red byte (pixel) of arr
          setRedC++; //incriments the position of the next time you set a red byte
        }
        if(setColorCounter%3 == 1){ //green is second, and should happen 1/3 times
          SetGreen(setGreenC,arr[x]); //sets the green byte (pixel) of position setGreenC of the new image with the green byte (pixel) of arr
          setGreenC++; //incriments the position of the next time you set a green byte
        }
        if(setColorCounter%3 == 2){ //blue is third, and should happen 1/3 times
          SetBlue(setBlueC,arr[x]); //sets the blue byte (pixel) of position setBlueC of the new image with the blue byte (pixel) of arr
          setBlueC++; //incriments the position of the next time you set a blue byte
        }
        setColorCounter++; //keeps track of the color that was just gotten and moves to the next color
      }
      else{ //if the image is not color
        SetGray(setGrayC, arr[x]); //sets the gray byte (pixel) of position setGrayC of the new image with the gray byte (pixel) of arr
        setGrayC++; //incriments the position of the next time you set a gray byte
      }
    }
  }

  WriteImage(argv[2],img);  // output stego file (cover_file + file_to_hide)
}

//This function sets the least significant bit of a byte to whatever the bit of b0 is for each bit of b0 
//Takes in an array of bytes(unsigned char array) and a byte(unsigned char)
void setlsbs(unsigned char *p, unsigned char b0){
  for(int i=0; i<8; i++){ //for each bit of b0
    p[i] = (p[i] & 0xFE) | ((b0 >> i)&1); //mask the byte from the array and ors it with the bit of b0
  }
}

