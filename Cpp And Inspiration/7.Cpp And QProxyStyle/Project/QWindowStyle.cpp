#include "QWindowStyle.h"
#include <QStyleFactory>
#include <QPushButton>
#include <QComboBox>
#include <QPainter>
#define isQComboBox qobject_cast<QComboBox*>
#define isQPushButton qobject_cast<QPushButton*>

QWindowStyle::QWindowStyle(QString styleName, QString imageType, QVector<QColor> colorSet) : QProxyStyle(QStyleFactory::create("windows"))
{
	this->styleName=styleName; this->imageType=imageType; this->colorSet=colorSet;
	this->buttonImageName=tr("images/%1/button.%2").arg(styleName).arg(imageType);
	this->backgroundImageName=tr("images/%1/background.%2").arg(styleName).arg(imageType);
}
void QWindowStyle::polish(QPalette& palette)
{
	this->setBrush(palette);
	this->setTexture(palette);
}
void QWindowStyle::polish(QWidget* widget)
{
	if(isQComboBox(widget)||isQPushButton(widget))
	{
		 widget->setAttribute(Qt::WA_Hover, true);
	}
}
void QWindowStyle::unpolish(QWidget* widget)
{
	if(isQComboBox(widget)||isQPushButton(widget))
	{
		 widget->setAttribute(Qt::WA_Hover, false);
	}
}
void QWindowStyle::setBrush(QPalette& palette)
{
	palette=QPalette(colorSet[Palette]);
	palette.setBrush(QPalette::Base, colorSet[Base]);
	palette.setBrush(QPalette::Text, colorSet[Foreground]);
	palette.setBrush(QPalette::Highlight, colorSet[Highlight]);
	palette.setBrush(QPalette::ButtonText, colorSet[Foreground]);
	palette.setBrush(QPalette::WindowText, colorSet[Foreground]);
}
void QWindowStyle::setTexture(QPalette& palette)
{
	QPixmap buttonImage(buttonImageName);
	QPixmap backgroundImage(backgroundImageName);
	this->setPixmap(palette, QPalette::Button, buttonImage);
	this->setPixmap(palette, QPalette::Window, backgroundImage);
}
void QWindowStyle::setPixmap(QPalette& palette, QPalette::ColorRole colorRole, QPixmap pixmap)
{
	for (int i=0; i<QPalette::NColorGroups; ++i) 
	{
		QColor color=palette.brush(QPalette::ColorGroup(i), colorRole).color();
		palette.setBrush(QPalette::ColorGroup(i), colorRole, QBrush(color, pixmap));
	}
}
