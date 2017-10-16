#include "QMainWindow.h"

int main(int argc, char *argv[])
{
	QApplication application(argc, argv);
	QMainWindow window;
	window.resize(900, 600);
	window.show();
	return application.exec();
}
