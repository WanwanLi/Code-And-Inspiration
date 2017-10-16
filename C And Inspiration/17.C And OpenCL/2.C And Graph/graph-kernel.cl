inline float getDistance(__global float* input,int i,int j,int dim)
{
	float d=0.0;
	for(int k=0;k<dim;k++)
	{
		float x=input[i*dim+k];
		float y=input[j*dim+k];
		d+=(x-y)*(x-y);
	}
	return sqrt(d);
}
__kernel void main(__global float* input,__global float* output,int row, int col, int dim)
{
	int i=get_global_id(0),j=get_global_id(1);
	if(i<row&&j<col)output[i*col+j]=getDistance(input,i,j,dim);
}