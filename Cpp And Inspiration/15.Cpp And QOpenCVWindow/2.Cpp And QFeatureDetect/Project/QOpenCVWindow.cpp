#include <QStyle>
#include "QOpenCVWindow.h"

QOpenCVWindow::QOpenCVWindow(QWidget* widget) : QWidget(widget)
{
 	this->statusLabel=new QLabel();
 	this->imageLabel[0]=new QLabel();
 	this->imageLabel[1]=new QLabel();
 	this->imageLabel[2]=new QLabel();
 	this->imageLabel[3]=new QLabel();
	this->videoCapture=newQVideoCapture(statusLabel);
 	this->playButton=new QPushButton();
 	this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPause));
	this->timer=new QTimer(this);
	this->timer->start(200);
	this->setLayout(newQGridLayout());
	connect(timer, &QTimer::timeout, this, &QOpenCVWindow::timeout);
	connect(playButton, &QPushButton::clicked, this, &QOpenCVWindow::play);
}
QVideoCapture* QOpenCVWindow::newQVideoCapture(QLabel* label)
{
	QVideoCapture* videoCapture=new QVideoCapture(); videoCapture->open(0);
	if(videoCapture->isOpened())label->setText("Camera is opened.");
	else label->setText("ERROR: Can not open camera."); 
	return videoCapture;
}
QGridLayout* QOpenCVWindow::newQGridLayout()
{
	QGridLayout* gridLayout=new QGridLayout();
	gridLayout->addWidget(imageLabel[0], 0, 0, 3, 2);
	gridLayout->addWidget(imageLabel[1], 0, 2, 1, 1);
	gridLayout->addWidget(imageLabel[2], 1, 2, 1, 1);
	gridLayout->addWidget(imageLabel[3], 2, 2, 1, 1);
	gridLayout->addWidget(playButton, 3, 0, 1, 1);
	gridLayout->addWidget(statusLabel, 3, 1, 1, 2);
	gridLayout->setColumnStretch(0, 1);
	gridLayout->setColumnStretch(1, 40);
	gridLayout->setColumnStretch(2, 10);
	gridLayout->setRowStretch(0, 1);
	gridLayout->setRowStretch(1, 1);
	gridLayout->setRowStretch(2, 1);
	gridLayout->setRowStretch(3, 3);
	return gridLayout;
}
void QOpenCVWindow::setPixmap(QLabel* label, QMatrix pixels, QFormat format)
{
	QImage image((uchar*)pixels.data, pixels.cols, pixels.rows, pixels.step, format);
	label->setPixmap(QPixmap::fromImage(scaledImage(image, label->size())));
}
QImage QOpenCVWindow::scaledImage(QImage image, QSize size)
{
	QImage* newImage=new QImage(size, QImage::Format_RGB32); QPainter painter(newImage); 
	QImage scaledImage=image.scaled(size,Qt::IgnoreAspectRatio, Qt::SmoothTransformation);
	painter.drawImage(QPoint(0, 0), scaledImage); return *newImage;
}
void QOpenCVWindow::play()
{
	if(this->timer->isActive())
	{
		this->timer->stop();
		this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPlay));
	}
	else
	{
		this->timer->start();
		this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPause));
	}
}
void QOpenCVWindow::timeout()
{
	this->videoCapture->read(originalPixels); if(originalPixels.empty())return;
	cv::Canny(originalPixels, processedPixels, 50, 200, 3);
	this->setPixmap(imageLabel[1], processedPixels, QImage::Format_Indexed8);
	//cv::cvtColor(processedPixels, processedPixels, CV_GRAY2BGR);
	//this->setPixmap(imageLabel[2], processedPixels, QImage::Format_RGB888);
	if(!lines.empty())lines.erase(lines.begin(), lines.end());
	cv::HoughLinesP(processedPixels, lines, 1, CV_PI/180, 1, 1, 100);
	for(int i=0; i<5&&i<lines.size(); i++)
	{
		cv::line(originalPixels, cv::Point(lines[i][0], lines[i][1]),
		cv::Point(lines[i][2], lines[i][3]), cv::Scalar(255, 0, 0), 3, 8);
	}
	this->setPixmap(imageLabel[0], originalPixels, QImage::Format_RGB888);
/*
	cv::cvtColor(originalPixels, originalPixels, CV_BGR2RGB);
	this->setPixmap(imageLabel[1], originalPixels, QImage::Format_RGB888);
	cv::cvtColor(originalPixels, processedPixels, CV_BGR2GRAY);
	this->setPixmap(imageLabel[2], processedPixels, QImage::Format_Indexed8);

	this->setPixmap(imageLabel[3], processedPixels, QImage::Format_Indexed8);
	cv::GaussianBlur(processedPixels, processedPixels, cv::Size(9, 9), 2, 2);
	lines.erase(lines.begin(), lines.end());
	cv::HoughLinesP(processedPixels, lines, 1, CV_PI/180, 80, 30, 10);
*/
}
