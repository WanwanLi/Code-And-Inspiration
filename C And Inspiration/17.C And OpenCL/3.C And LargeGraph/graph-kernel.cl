inline float getDistance(__global float* input1,__global float* input2,int i,int j,int dim)
{
	float d=0.0;
	for(int k=0;k<dim;k++)
	{
		float x=input1[i*dim+k];
		float y=input2[j*dim+k];
		d+=(x-y)*(x-y);
	}
	return sqrt(d);
}
__kernel void main(__global float* input1, __global float* input2,__global float* output, int row, int col, int dim)
{
	int i=get_global_id(0),j=get_global_id(1);
	if(i<row&&j<col)output[i*col+j]=getDistance(input1,input2,i,j,dim);
}
