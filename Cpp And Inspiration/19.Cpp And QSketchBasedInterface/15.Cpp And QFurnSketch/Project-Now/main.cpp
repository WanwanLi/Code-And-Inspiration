#include "QWindow.h"
#include <exception>

class QExceptionApplication : public QApplication 
{
	public:
	QExceptionApplication(int& argc, char*argv[]):QApplication(argc, argv){}
	bool notify(QObject* receiver, QEvent* event); 
};
bool QExceptionApplication::notify(QObject* receiver, QEvent* event) 
{
	bool isNotified=true;
	try
	{
		isNotified=QApplication::notify(receiver, event);
	}
	catch(const std::exception& e)
	{
		qDebug()<<"Exception";
	}
	return isNotified;
} 
int main(int argc, char *argv[])
{
	QExceptionApplication application(argc, argv);
	QWindow window; window.show();
	return application.exec();
}
