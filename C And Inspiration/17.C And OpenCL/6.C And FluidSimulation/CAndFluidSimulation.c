#include <stdio.h>
#include <stdlib.h>
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
int main()
{
	int i,j,row=10,col=5,length=row*col;
	float* inputData1=newFloat32Array(row*col);
	float* inputData2=newFloat32Array(row*col);
	float* outputData=newFloat32Array(row*col);
	for(i=0;i<row;i++)
	{
		for(j=0;j<col;j++)
		{
			inputData1[i*col+j]=i*col+j;
			inputData2[i*col+j]=-2*(i*col+j);
		}
	}
	clInitDevices();
	char* kernelSource=clGetKernelSource("./fluid-kernel.cl");
	cl_mem inputBuffer1=clCreateKernelBuffer(inputData1, row*col);
	cl_mem inputBuffer2=clCreateKernelBuffer(inputData2, row*col);
	cl_mem outputBuffer=clCreateKernelBuffer(NULL, row*col);
	clSetKernelArgBuffer(inputBuffer1);
	clSetKernelArgBuffer(inputBuffer2);
	clSetKernelArgBuffer(outputBuffer);
	clSetKernelArgInteger(row);
	clSetKernelArgInteger(col);
	clExecuteKernelProgram();
	clReadKernelBuffer(outputData, outputBuffer, row*col);
	for(i=0;i<row;i++)
	{
		for(j=0;j<col;j++)
		{
			printf("%.0f\t", outputData[i*col+j]);
		}
		printf("\n");
	}
 	free(kernelSource);
	free(inputData1);
	free(inputData2);
	free(outputData);
	clReleaseMemObject(inputBuffer1);
	clReleaseMemObject(inputBuffer2);
	clReleaseMemObject(outputBuffer);
	clReleaseResources();
	return 0;
}
