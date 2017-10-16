#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <CL/cl.h>

#define MAX_SOURCE_SIZE (0x100000)

cl_context clContext=NULL;
cl_device_id clDeviceID=NULL;
cl_platform_id clPlatformID=NULL;
cl_command_queue clCommandQueue=NULL;
cl_uint clDevicesLength=0;
cl_uint clPlatformsLength=0;
cl_program clProgram=NULL;
cl_kernel clKernel=NULL;
cl_int clErrorCode=0, clArgID=0;
float* newFloat32Array(int length)
{
	return (float*)malloc(sizeof(float)*length);
}
float** newFloat32Matrix(int length)
{
	float** matrix=(float**)malloc(sizeof(float*)*length);int i;
	for(i=0;i<length;i++)matrix[i]=NULL;
	return matrix;
}
void clInitDevices()
{
	clErrorCode=clGetPlatformIDs(1, &clPlatformID, &clPlatformsLength);
	clErrorCode=clGetDeviceIDs(clPlatformID, CL_DEVICE_TYPE_DEFAULT, 1, &clDeviceID, &clDevicesLength);
	clContext=clCreateContext(NULL, 1, &clDeviceID, NULL, NULL, &clErrorCode);
	clCommandQueue=clCreateCommandQueueWithProperties(clContext, clDeviceID, 0, &clErrorCode);
}
char* clGetKernelSource(char* fileName)
{
	FILE* file=fopen(fileName,"r");
	if(!file){printf("Failed to load kernel.\n");exit(1);}
	char* kernelSource=(char*)malloc(MAX_SOURCE_SIZE);
	size_t kernelSourceSize=fread(kernelSource, 1, MAX_SOURCE_SIZE, file);
	fclose(file);
	clProgram=clCreateProgramWithSource(clContext, 1, (const char**)&kernelSource, (const size_t *)&kernelSourceSize, &clErrorCode);
	clErrorCode=clBuildProgram(clProgram, 1, &clDeviceID, NULL, NULL, NULL);
	clKernel=clCreateKernel(clProgram, "main", &clErrorCode);
	return kernelSource;
}
cl_mem clCreateKernelBuffer(float* data, size_t length)
{
	if(data==NULL)return clCreateBuffer(clContext, CL_MEM_READ_WRITE, length*sizeof(float), NULL, &clErrorCode);
	else return clCreateBuffer(clContext, CL_MEM_READ_WRITE|CL_MEM_COPY_HOST_PTR, length*sizeof(float), data, &clErrorCode);
}
void clSetKernelArgBuffer(cl_mem kernelBuffer)
{
	clSetKernelArg(clKernel, clArgID++, sizeof(cl_mem), (void *)&kernelBuffer);
}
void clSetKernelArgInteger(cl_int integer)
{
	clSetKernelArg(clKernel, clArgID++, sizeof(cl_int), (void *)&integer);
}
void clReadKernelBuffer(float* outputData, cl_mem kernelBuffer, size_t length)
{
	clEnqueueReadBuffer(clCommandQueue, kernelBuffer, CL_TRUE, 0, length*sizeof(float), outputData, 0, NULL, NULL);
}
void clWriteKernelBuffer(float* inputData, cl_mem kernelBuffer, size_t length)
{
	clEnqueueWriteBuffer(clCommandQueue, kernelBuffer, CL_TRUE, 0, length*sizeof(float), inputData, 0, NULL, NULL);
}
void clExecuteKernelProgram()
{
	size_t workDim=2;
	size_t localWorkSize[2];
	size_t globalWorkSize[2];
	localWorkSize[0]=16;
	localWorkSize[1]=16;
	globalWorkSize[0]=1024;
	globalWorkSize[1]=1024;
	clEnqueueNDRangeKernel(clCommandQueue, clKernel, workDim, NULL, globalWorkSize, localWorkSize, 0, NULL, NULL);
}
void clReleaseResources()
{
	clFlush(clCommandQueue);
	clFinish(clCommandQueue);
	clReleaseKernel(clKernel);
	clReleaseProgram(clProgram);
	clReleaseCommandQueue(clCommandQueue);
	clReleaseContext(clContext);
}
void clComputeFunc(cl_mem inputBuffer1, cl_mem inputBuffer2, cl_mem outputBuffer, float* inputData1, float* inputData2, float* outputData, int row, int col, int dim)
{
	clWriteKernelBuffer(inputData1, inputBuffer1, row*dim);
	clWriteKernelBuffer(inputData2, inputBuffer2, row*dim);
	clSetKernelArgBuffer(inputBuffer1);
	clSetKernelArgBuffer(inputBuffer2);
	clSetKernelArgBuffer(outputBuffer);
	clSetKernelArgInteger(row);
	clSetKernelArgInteger(col);
	clSetKernelArgInteger(dim);
	clExecuteKernelProgram();
	clReadKernelBuffer(outputData, outputBuffer, row*col);
}
float* clGetInputData(FILE* file, float** inputMatrix, int row, int dim, int d, int x)
{
	float* inputData=inputMatrix[d];
	if(inputData==NULL)
	{
		char string[1024];
		int i,j; inputData=newFloat32Array(row*dim);
		for(i=0;i<row&&!feof(file);i++)
		{
			fgets(string,sizeof(string),file);
			printf("%s", string);
			char* token = strtok(string, ",");
			printf("line %s: \n", token);
			for(j=0;j<dim;j++)
			{
				inputData[i*dim+j]=atof(strtok(NULL, ","));
				printf("%f:\n ", inputData[i*dim+j]);
			}
		}
		inputMatrix[d]=inputData;
	}
	return inputData;
}
void clExecuteFunc(FILE* file, float** inputMatrix, float** outputMatrix, int size, int row, int col, int dim)
{
	int x,y,i,j;
	cl_mem inputBuffer1=clCreateKernelBuffer(NULL, row*dim);
	cl_mem inputBuffer2=clCreateKernelBuffer(NULL, row*dim);
	cl_mem outputBuffer=clCreateKernelBuffer(NULL, row*col);
	for(i=0,x=0;i<size;x+=row,i++)
	{
		for(j=i,y=x;j<size;y+=col,j++)
		{
			float* inputData1=clGetInputData(file, inputMatrix, row, dim, i, x);
			float* inputData2=clGetInputData(file, inputMatrix, col, dim, j, y);
			float* outputData=newFloat32Array(row*col);
			clComputeFunc
			(
				inputBuffer1, inputBuffer2, outputBuffer,
				inputData1, inputData2, outputData,
				row, col, dim
			);
			outputMatrix[i*size+j]=outputData;
			printf("Submatrix [%d][%d] finished...\n",i,j);
		}
	}
	clReleaseMemObject(inputBuffer1);
	clReleaseMemObject(inputBuffer2);
	clReleaseMemObject(outputBuffer);
}
void clPrintFloat32Matrix(float* matrix, int row, int col)
{
	int i,j;
	if(matrix==NULL)
	{
		for(i=0;i<row;i++)
		{
			for(j=0;j<col;j++)
			{
				printf("0\t");
			}
			printf("\n");
		}
		return;
	}
	for(i=0;i<row;i++)
	{
		for(j=0;j<col;j++)
		{
			printf("%f\t", matrix[i*col+j]);
		}
		printf("\n");
	}
}
float* clMergeFloat32Matrix(int sizeX, int sizeY, float** subMatrix, int row, int col)
{
	int x,y,i,j;
	int height=sizeX*row;
	int width=sizeY*col;
	float* largeMatrix=newFloat32Array(height*width);
	for(x=0;x<sizeX;x++)
	{
		for(y=x;y<sizeY;y++)
		{
			float* matrix=subMatrix[x*sizeY+y];
			for(i=0;i<row;i++)
			{
				int r=x*row+i;
				for(j=0;j<col;j++)
				{
					int c=y*col+j;
					largeMatrix[r*width+c]=matrix[i*col+j];
				}
			}
			free(matrix);
		}
	}
	for(x=0;x<height;x++)
	{
		for(y=0;y<x;y++)
		{
			largeMatrix[x*width+y]=largeMatrix[y*width+x];
		}
	}
	return largeMatrix;
}
int main()
{
	clInitDevices();
	char* kernelSource=clGetKernelSource("./graph-kernel.cl");
	int i, j, row=3, col=row, dim=-1;
	char string[1024];
	char* fileName="CSVFile.csv";
	FILE* file=fopen(fileName, "r");
	if(file==NULL)
	{
		printf("File does not exists... \n");
		return 0;
	}
	fgets(string,sizeof(string),file);
	char* token = strtok(string, ",");
	while(token)
	{
		printf("%s, ", token);
		token=strtok(NULL, ",");
		dim++;
	}
	int dataLength=9, size=dataLength/row;
	float** inputMatrix=newFloat32Matrix(size);
	float** outputMatrix=newFloat32Matrix(size*size);
	clExecuteFunc(file, inputMatrix, outputMatrix, size, row, col, dim);
	for(i=0;i<size;i++)
	{
		for(j=0;j<size;j++)
		{
			printf("Submatrix [%d][%d] is:\n",i,j);
			float* matrix=outputMatrix[i*size+j];
			clPrintFloat32Matrix(matrix, row, col);
			printf("\n");
		}
	}
	float* resultMatrix=clMergeFloat32Matrix(size, size, outputMatrix, row, col);
	clPrintFloat32Matrix(resultMatrix, dataLength, dataLength);	
	fclose(file);
	free(resultMatrix);
 	free(kernelSource);
	clReleaseResources();
	return 1;
}
