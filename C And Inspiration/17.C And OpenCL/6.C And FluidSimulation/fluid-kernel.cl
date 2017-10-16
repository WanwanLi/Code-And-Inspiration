__kernel void main(__global float* input1, __global float* input2, __global float* output ,int row, int col)
{
	int i=get_global_id(0),j=get_global_id(1);
	if(i<row&&j<col)output[i*col+j]=input1[i*col+j]+input2[i*col+j];
}