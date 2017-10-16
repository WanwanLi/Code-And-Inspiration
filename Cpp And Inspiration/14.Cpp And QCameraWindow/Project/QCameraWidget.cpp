#include "QCameraWidget.h"
#include <QCameraInfo>
#include <QBoxLayout>
#include <QCameraViewfinder>

QCameraWidget::QCameraWidget(QWidget* widget) : QWidget(widget)
{
	this->camera=new QCamera(QCameraInfo::defaultCamera());
	this->cameraImageCapture=new QCameraImageCapture(camera);
	QCameraViewfinder* viewfinder=new QCameraViewfinder();
	this->camera->setViewfinder(viewfinder);
	QBoxLayout* HBoxLayout=new QHBoxLayout();
	HBoxLayout->addWidget(viewfinder);
	HBoxLayout->setMargin(0);
	this->setLayout(HBoxLayout);
	this->camera->start();
	connect(cameraImageCapture, SIGNAL(readyForCaptureChanged(bool)), this, SLOT(readyForCaptureChanged(bool)));
	connect(cameraImageCapture, SIGNAL(imageSaved(int,QString)), this, SLOT(imageSaved(int,QString)));
}
void QCameraWidget::setLabel(QLabel* label)
{
	this->label=label;
}
void QCameraWidget::setButton(QPushButton* button)
{
	this->button=button;
}
void QCameraWidget::captureImage()
{
	this->cameraImageCapture->capture();
	this->label->setText("Image is cpaturing...");
}
void QCameraWidget::readyForCaptureChanged(bool ready)
{
	this->button->setEnabled(ready);
}
void QCameraWidget::imageSaved(int id, QString fileName)
{
	this->label->setText(tr("Cpatured image [%1] saved as: %2").arg(id).arg(fileName));
}
void QCameraWidget::setExposureCompensation(int index)
{
	this->camera->exposure()->setExposureCompensation(index*0.5);
	this->label->setText(tr("The exposure compensation is seted as [%1].").arg(index*0.5));
}
