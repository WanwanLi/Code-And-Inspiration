#include "QWindow.h"

QWindow::QWindow(QWidget* widget) : QWidget(widget)
{
 	this->counter=0;
 	this->setFixedSize(500, 400);
 	this->pushButton=newQPushButton(tr("Button"), tr("Tooltip"), this, QPixmap("button.png"), QFont("Courier", 20, 1, true), QIcon("icon.gif"));
 	this->pushButton->setGeometry(175, 150, 150, 45);
 	this->pushButton->setCheckable(true);
	connect(pushButton, &QPushButton::clicked, this, &QWindow::clicked);
	connect(this, &QWindow::quit, QApplication::instance(), &QApplication::quit);
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
	if(clicked)this->pushButton->setText("Clicked");
	else this->pushButton->setText("Button");
	if(++this->counter>=4)emit quit();
}
