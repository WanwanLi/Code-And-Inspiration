#include "QWindow.h"
#include "QRunnable.h"
#include <QMessageBox>

QWindow::QWindow(QWidget* widget) : QWidget(widget)
{
 	this->counter=0;
	this->thread=new QThread();
 	this->setFixedSize(500, 400);
	this->basicTimer=new QBasicTimer();
	this->basicTimer->start(500, this);
	this->runnable=new QRunnable(thread);
 	this->pushButton=newQPushButton
	(
		tr("Button"), tr("Tooltip"), 
		this, QPixmap("button.png"), 
		QFont("Courier", 20, 1, true), QIcon("icon.gif")
	);
 	this->pushButton->setGeometry(175, 150, 150, 45);
 	this->pushButton->setCheckable(true);
	this->progressBar=new QProgressBar(this); 
	this->progressBar->setGeometry(100, 250, 320, 20);
	connect(pushButton, &QPushButton::clicked, this, &QWindow::clicked);
	connect(this, &QWindow::quit, QApplication::instance(), &QApplication::quit);
	connect(runnable, &QRunnable::valueChanged, progressBar, &QProgressBar::setValue);
}
QPushButton* QWindow::newQPushButton(QString& name, QString& tooltip, QWidget* widget, QPixmap& pixmap, QFont& font, QIcon& icon)
{
	QPushButton* pushButton=new QPushButton(name, widget); QPalette palette; 
	palette.setBrush(pushButton->backgroundRole(), QBrush(pixmap));
	pushButton->setAutoFillBackground(true);
	pushButton->setPalette(palette);
	pushButton->setToolTip(tooltip);
	pushButton->setFont(font);
	pushButton->setIcon(icon);
	pushButton->setFlat(true);
	return pushButton;
}
void QWindow::clicked(bool clicked)
{
	if(thread->isRunning())
	{
		QString message="QProgressBar is running!";
		QMessageBox::critical(this, "Error", message); 
	}
	else thread->start();
}
void QWindow::timerEvent(QTimerEvent* event)
{
	this->pushButton->setText(tr("Time:%1").arg(++this->counter));
	if(counter>=100)emit quit();
}
