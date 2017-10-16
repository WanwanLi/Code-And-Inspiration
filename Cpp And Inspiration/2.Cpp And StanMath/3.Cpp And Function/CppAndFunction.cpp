#include <stan/math.hpp>
using namespace std;
using namespace stan::math;

char** newChar(int row, int col)
{
	char** array=(char**)malloc(row*sizeof(char*));
	for(int i=0; i<row; i++)array[i]=(char*)malloc(col*sizeof(char));
	return array;
}
class Function
{
	private:
	int row;
	int col;
	char** graph;
	int toInt(double x);

	public:
	Function(int row, int col);
	void draw(char c, var (*func)(var x), double argv[]);
	void println();
	~Function();
};
Function::Function(int row, int col)
{
	int i, j;
	this->col=col;
	this->row=row; 
	this->graph=newChar(row, col);
	for(i=0; i<row; i++)
	{
		for(j=0; j<col; j++)
		{
			this->graph[i][j]=' ';
		}
	}
	for(i=row/2, j=0; j<col; j++)
	{
		this->graph[i][j]='-';
	}
	for(i=0, j=col/2; i<row; i++)
	{
		this->graph[i][j]='|';
	}
}
int Function::toInt(double x)
{
	return x >= 0?(int)(x + 0.5):(int)(x - 0.5);
}
void Function::draw(char c, var (*func)(var x), double argv[])
{
	double x0 = argv[0];
	double x1 = argv[1];
	double y0 = argv[2];
	double y1 = argv[3];
	int op = (int)argv[4];
	for(int j=0; j<col; j++)
	{
		var x=x0+j*(x1-x0)/(col-1);
		var f=(*func)(x); f.grad();
		double y=op==0?f.val():x.adj();
		if(y0<=y&&y<=y1)
		{
			int i=toInt((row-1)*(y-y0)/(y1-y0));
			this->graph[row-1-i][j]=c;
		}
	}
}
void Function::println()
{
	system("cls");
	for(int i=0; i<row; i++)
	{
		for(int j=0; j<col; j++)
		{
			printf("%c", graph[i][j]);
		}
		printf("\n");
	}
}

Function::~Function()
{
	for(int i=0; i<row; i++)free(graph[i]);
	free(graph);
}
var sin3x(var x)
{
	return sin(3*x);
}
var x2(var x)
{
	return  x*x;
}
var cosx2(var x)
{
	return   cos(x*x);
}
var expx2(var x)
{
	return   x==0?0:exp(-1.0/(x*x));
}
int main(int argc, char* argv[]) 
{
	int rangeX = 3;
	int rangeY = 2;
	int row=rangeY*20+1;
	int col=rangeX*20+1;
	double argv1[5]={-2.0, 2.0, -2, 2, 0};
	double argv2[5]={-1.5, 1.5, -2, 2, 0};
	double argv3[5]={-3.0, 3.0, -4, 4, 0};
	double argv4[5]={-2.0, 2.0, -1, 1, 0};
	Function function(row, col);
	//function.draw('o', &sin3x, argv1);
	function.draw('o', &x2, argv2); argv2[4]=1;
	function.draw('*', &x2, argv2); argv2[4]=0;
	//function.draw('o', &cosx2, argv3);
	//function.draw('o', &expx2, argv4);
	function.println();
	return 0;
}