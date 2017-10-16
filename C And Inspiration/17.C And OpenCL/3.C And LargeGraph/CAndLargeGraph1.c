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
void clComputeFunc(cl_mem inputBuffer1, cl_mem inputBuffer2, cl_mem outputBuffer, float* inputData1, float* inputData2, float* outputData, int row, int col, int dim, int x, int y)
{
	int i,j;
	for(i=0;i<row;i++)
	{
		for(j=0;j<dim;j++)
		{
			inputData1[i*dim+j]=(x+i)*dim+j;
			inputData2[i*dim+j]=(y+i)*dim+j;
		}
	}
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
	for(i=0;i<row;i++)
	{
		for(j=0;j<col;j++)
		{
			printf("%.0f\t", outputData[i*col+j]);
		}
		printf("\n");
	}
}
int main()
{
	clInitDevices();
	char* kernelSource=clGetKernelSource("./graph-kernel.cl");
	int i, j, row=10, col=10, dim=3;
	float* inputData1=newFloat32Array(row*dim);
	float* inputData2=newFloat32Array(row*dim);
	float* outputData=newFloat32Array(row*col);
	cl_mem inputBuffer1=clCreateKernelBuffer(NULL, row*dim);
	cl_mem inputBuffer2=clCreateKernelBuffer(NULL, row*dim);
	cl_mem outputBuffer=clCreateKernelBuffer(NULL, row*col);
	clComputeFunc
	(
		inputBuffer1, inputBuffer2, outputBuffer,
		inputData1, inputData2, outputData,
		row, col, dim, 0, 0
	);
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
