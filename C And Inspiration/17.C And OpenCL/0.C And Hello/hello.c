#include <stdio.h>
#include <stdlib.h>
#include <CL/cl.h>
 
#define MEM_SIZE (128)
#define MAX_SOURCE_SIZE (0x100000)

cl_context clContext=NULL;
cl_device_id clDeviceID=NULL;
cl_platform_id clPlatformID=NULL;
cl_command_queue clCommandQueue=NULL;
cl_uint clDevicesLength=0;
cl_uint clPlatformsLength=0;
cl_program clProgram=NULL;
cl_kernel clKernel=NULL;
cl_int clErrorCode=0;

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
cl_mem clCreateKernelBuffer(size_t length)
{
	return clCreateBuffer(clContext, CL_MEM_READ_WRITE, length*sizeof(char), NULL, &clErrorCode);
}
void clSetKernelArgBuffer(cl_int id, cl_mem kernelBuffer)
{
	clSetKernelArg(clKernel, id, sizeof(cl_mem), (void *)&kernelBuffer);
}
void clReadKernelBuffer(char* outputData, cl_mem kernelBuffer, size_t length)
{
	clEnqueueReadBuffer(clCommandQueue, kernelBuffer, CL_TRUE, 0, length*sizeof(char), outputData, 0, NULL, NULL);
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
	clInitDevices();
	char* kernelSource=clGetKernelSource("./hello.cl");
	cl_mem kernelBuffer=clCreateKernelBuffer(MEM_SIZE);
	clSetKernelArgBuffer(0, kernelBuffer);
	clExecuteKernelProgram();
	char string[MEM_SIZE];
	clReadKernelBuffer(string, kernelBuffer, MEM_SIZE);
	printf("%s\n",string);
 	free(kernelSource);
	clReleaseMemObject(kernelBuffer);
	clReleaseResources();
	return 0;
}
