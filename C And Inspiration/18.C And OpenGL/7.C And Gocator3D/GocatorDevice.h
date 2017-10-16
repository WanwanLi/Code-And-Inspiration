#include <Go2.h>
#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#define GocatorDevice_h 0
#define String char*
#define RECEIVE_TIMEOUT 20000 
#define ERROR_VALUE -32768
#define String char*
#define null NULL
#define DEVICE_COUNT 3
Go2System devices[DEVICE_COUNT];
Go2Mode deviceModes[DEVICE_COUNT];
unsigned int* deviceIDs;
unsigned int deviceCount;
Go2AddressInfo* deviceIPAddresses;
int nextInt()
{
	int d=0;
	scanf("%d",&d);
	return d;
}
void checkStatus(int status,String msg)
{
	if(status!=GO2_OK)
	{
		printf("Status=%d\n",status);
		printf("%s failed...\n",msg);
	//	printf("Press any key to continue...");
	}
	else printf("%s is: OK\n",msg);
}
void checkStatus(int status,String msg,int id)
{
	if(status!=GO2_OK)
	{
		printf("Device %d:",id);
		printf("Status=%d ",status);
		printf("%s failed...\n",msg);
	//	printf("Press any key to continue...");
	}
	else 
	{
		printf("Device %d: ",id);
		printf("%s is: OK\n",msg);
	}
}
void initDevices()
{
	int status=Go2Api_Initialize();
	checkStatus(status,"Initialize Device");
	status=Go2System_Discover(&deviceIDs,&deviceIPAddresses,&deviceCount);
	checkStatus(status,"System Discover");
	if(deviceCount<DEVICE_COUNT)printf("There will be only %d devices are applicable",deviceCount);
	for(int i=0;i<deviceCount;i++)
	{
		status=Go2System_Construct(&devices[i]);
		checkStatus(status,"System Construct",i);
	}
}

void connectDevices()
{
	for(int i=0;i<deviceCount;i++)
	{
		int status=Go2System_Connect(devices[i],deviceIPAddresses[i].address);
		checkStatus(status,"System Connect",i);
		status=Go2System_ConnectData(devices[i],GO2_NULL,GO2_NULL);
		checkStatus(status,"System Connect Data",i);
		status=Go2System_Start(devices[i]);
		checkStatus(status,"System Start",i);
	}
	Go2Free(deviceIDs);
	Go2Free(deviceIPAddresses);
}
void configureDevices()
{
	for(int i=0;i<deviceCount;i++)
	{
		deviceModes[i]= Go2System_Mode(devices[i]);
		int status=Go2System_SetMode(devices[i],GO2_MODE_PROFILE_MEASUREMENT);
		checkStatus(status,"System Set Mode",i);
		status=Go2System_SetTriggerSource(devices[i],GO2_TRIGGER_SOURCE_TIME);
		checkStatus(status,"System Set Trigger Source",i);
		status=Go2System_ClearCalibration(devices[i]);
		checkStatus(status,"System Clear Calibration",i);
		status=Go2System_EnableFullFrameRate(devices[i],GO2_FALSE);
		checkStatus(status,"System Set Trigger Source",i);
		Go2Sensor sensor=Go2System_SensorAt(devices[i],0);
		/*
		unsigned int exposureDelay=Go2Sensor_Exposure(sensor);
		status=Go2Sensor_SetExposureDelay(sensor,exposureDelay);
		checkStatus(status,"System Set Exposure Delay",i);
		Go2Ethernet ethernet=Go2Output_Ethernet(Go2System_Output(devices[i]));
		status=Go2Ethernet_ClearSources(ethernet,GO2_OUTPUT_DATA_SOURCE_TYPE_PROFILE);
		checkStatus(status,"System Clear Sources",i);
		status=Go2Ethernet_AddSource(ethernet,GO2_OUTPUT_DATA_SOURCE_TYPE_PROFILE,GO2_PROFILE_SOURCE_MAIN);
		checkStatus(status,"System Add Source",i);
		*/
	}
}
void stopDevices()
{
	for(int i=0;i<deviceCount;i++)
	{
		int status=Go2System_Stop(devices[i]);
		checkStatus(status,"System Stop",i);
		status=Go2System_Disconnect(devices[i]);
		checkStatus(status,"System Disconnect",i);
		status=Go2System_DisconnectData(devices[i]);
		checkStatus(status,"System Disconnect Data",i);
		status=Go2System_Destroy(devices[i]);
		checkStatus(status,"System Destroy",i);
	}
	
	int status=Go2Api_Terminate();
	checkStatus(status,"Terminate Device");
}
int received=0;
int dataLength=0;
double xResolution[DEVICE_COUNT];
double zResolution[DEVICE_COUNT];
double xOffset[DEVICE_COUNT];
double zOffset[DEVICE_COUNT];
int dataWidth[DEVICE_COUNT];
Go2Int16* dataValues[DEVICE_COUNT];
double startZValue=0,startZ,zoomZ=0.005;
void getValidDataValues(int);
void getProfleDataValues(double*,int);
int min(int a,int b)
{
	return a<b?a:b;
}
void receiveDataFromDevice(int id)
{
	Go2Data data=GO2_NULL;
	if(Go2System_ReceiveData(devices[id],RECEIVE_TIMEOUT,&data)==GO2_OK)
	{
		for(int i=0;i<Go2Data_ItemCount(data);i++)
		{
			Go2Data dataItem=Go2Data_ItemAt(data,i);
			if(Go2Object_Type(dataItem)==GO2_TYPE_PROFILE_DATA)
			{	
				dataWidth[id]=Go2ProfileData_Width(dataItem);
				zResolution[id]=Go2ProfileData_ZResolution(dataItem);
				zOffset[id]=Go2ProfileData_ZOffset(dataItem);
				dataValues[id]=Go2ProfileData_Ranges(dataItem);
				getValidDataValues(id);
				return;
			}
			else Go2Data_Destroy(data);
		}
	}
}
void receiveDataFromDevice(int id,double* profile,int profilePointCount)
{
	for(int i=0;i<deviceCount;i++)dataWidth[i]=0;
	receiveDataFromDevice(id);
	dataLength=dataWidth[id];
	if(dataLength==0)return;
	getProfleDataValues(profile,profilePointCount);
}
void receiveDataFromDevices(double* profile,int profilePointCount)
{
	dataLength=0;
	for(int i=0;i<deviceCount;i++)
	{
		dataWidth[i]=0;
		receiveDataFromDevice(i);
		dataLength+=dataWidth[i];
	}
	getProfleDataValues(profile,profilePointCount);
}
Go2Int16 getValidDataValue(Go2Int16 a,Go2Int16 b)
{
	return a==ERROR_VALUE?b:b==ERROR_VALUE?a:a<b?a:b;
}
void getValidDataValues(int id)
{
	int i0=0;
	for(int i=1;i<dataWidth[id];i++)
	{
		if(dataValues[id][i]==ERROR_VALUE)
		{
			i0=i-1;
			while(i<dataWidth[id]&&dataValues[id][i]==ERROR_VALUE)i++;
			double validDataValue=i==dataWidth[id]?dataValues[id][i0]:getValidDataValue(dataValues[id][i0],dataValues[id][i]);
			for(int j=i0;j<i;j++)dataValues[id][j]=validDataValue;
		}
	}
}
double getZValue(int i)
{
	int id=0,w=0;
	for(;w<=i;w+=dataWidth[id++]);
	w-=dataWidth[--id];
	return (zOffset[id]+dataValues[id][i-w]*zResolution[id])*zoomZ;
}
double getProfileData(double p)
{
	double d=(dataLength-1.0)*p;
	int i0=(int)d,i1=i0+1;
	double z0=getZValue(i0);
	if(i0==dataLength-1)return z0;
	double z1=getZValue(i1);
	return z0*(i1-d)+z1*(d-i0);
}
void getProfleDataValues(double* profile,int profilePointCount)
{
	for(int i=0;i<profilePointCount;i++)
	{
		double p=(i+0.0)/(profilePointCount-1);
		profile[i]=getProfileData(p)-1;
	}
}
