#include <QCoreApplication>
#include "QOptimizer.h"
using namespace std;

int main(int argc, char *argv[])
{
	QCoreApplication coreApplication(argc, argv);
	QString fileName="energy.fsk";
	QOptimizer optimizer(1e-6, 100);
	if(optimizer.minimize(fileName))
	{
		cerr<<optimizer.iterations<<" iterations"<<endl;
		cerr<<"f(x)="<<optimizer.minEnergy<<endl;
		cerr<<"x="<<endl<<optimizer.sketchPlanes<<endl;
	}
	else cerr<<"Error: in load file."<<endl;
	return coreApplication.exec();
}
