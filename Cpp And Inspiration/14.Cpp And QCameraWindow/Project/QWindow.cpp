#include <QStyle>
#include "QWindow.h"

QWindow::QWindow(QWidget* widget) : QWidget(widget)
{
 	this->setMinimumSize(500, 400);
 	this->label = new QLabel();
 	this->label->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Maximum);
 	this->playButton = new QPushButton();
 	this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPause));
 	this->captureButton = new QPushButton("Capture");
	this->slider=new QSlider();
	this->slider->setMinimum(-4);
	this->slider->setMaximum(4);
	this->slider->setPageStep(2);
	this->slider->setOrientation(Qt::Horizontal);
	this->slider->setTickPosition(QSlider::TicksAbove);
	this->cameraWidget=new QCameraWidget(this);
	this->cameraWidget->setButton(captureButton);
	this->cameraWidget->setLabel(label);
	this->setLayout(newQBoxLayout());
	connect(playButton, &QPushButton::clicked, this, &QWindow::play);
	connect(captureButton, &QPushButton::clicked, cameraWidget, &QCameraWidget::captureImage);
	connect(slider, &QSlider::sliderMoved, cameraWidget, &QCameraWidget::setExposureCompensation);
}
QBoxLayout* QWindow::newQBoxLayout()
{
	QBoxLayout* HBoxLayout=new QHBoxLayout();
	HBoxLayout->setMargin(0);
	HBoxLayout->addWidget(captureButton);
	HBoxLayout->addWidget(playButton);
	HBoxLayout->addWidget(slider);
	QBoxLayout* VBoxLayout=new QVBoxLayout();
	VBoxLayout->addWidget(cameraWidget);
	VBoxLayout->addLayout(HBoxLayout);
	VBoxLayout->addWidget(label);
	return VBoxLayout;
}
void QWindow::play()
{
	if(cameraWidget->camera->state()==QCamera::ActiveState)
	{
		this->cameraWidget->camera->stop();
		this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPlay));
	}
	else 
	{
		this->cameraWidget->camera->start();
		this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPause));
	}
}
