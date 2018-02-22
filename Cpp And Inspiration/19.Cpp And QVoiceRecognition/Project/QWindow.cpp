#include "QWindow.h"
#include <QStyle>
#include <QDebug>
#include <QStandardPaths>

QWindow::QWindow(QWidget* widget) : QWidget(widget)
{
 	this->setMinimumSize(500, 400);
 	this->label = new QLabel();
	this->audioRecorder = new QAudioRecorder(this);
	this->audioRecorder->setOutputLocation(QUrl::fromLocalFile(QDir::currentPath()+"/Audio"));
 	this->label->setSizePolicy(QSizePolicy::Preferred, QSizePolicy::Maximum);
 	this->playButton = new QPushButton();
 	this->playButton->setEnabled(false);
 	this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPlay));
 	this->openButton = new QPushButton("Open");
 	this->saveButton = new QPushButton("Save");
 	this->slider = new QSlider(Qt::Horizontal);
 	this->slider->setRange(0, 0);
	this->videoWidget=new QVideoWidget();
	this->setLayout(newQBoxLayout());
	this->mediaPlayer=new QMediaPlayer();
	this->mediaPlayer->setVideoOutput(videoWidget);
	typedef void (QMediaPlayer::*error)(QMediaPlayer::Error);
	connect(slider, &QSlider::sliderMoved, this, &QWindow::sliderMoved);
	connect(openButton, &QPushButton::clicked, this, &QWindow::open);
	connect(saveButton, &QPushButton::clicked, this, &QWindow::save);
	connect(playButton, &QPushButton::clicked, this, &QWindow::play);
	connect(mediaPlayer, static_cast<error>(&QMediaPlayer::error), this, &QWindow::error);
	connect(mediaPlayer, &QMediaPlayer::positionChanged, this, &QWindow::positionChanged);
	connect(mediaPlayer, &QMediaPlayer::durationChanged, this, &QWindow::durationChanged);
}
QBoxLayout* QWindow::newQBoxLayout()
{
	QBoxLayout* HBoxLayout=new QHBoxLayout();
	HBoxLayout->setMargin(0);
	HBoxLayout->addWidget(openButton);
	HBoxLayout->addWidget(saveButton);
	HBoxLayout->addWidget(playButton);
	HBoxLayout->addWidget(slider);
	QBoxLayout* VBoxLayout=new QVBoxLayout();
	VBoxLayout->addWidget(videoWidget);
	VBoxLayout->addLayout(HBoxLayout);
	VBoxLayout->addWidget(label);
	return VBoxLayout;
}
void QWindow::open()
{
	this->playButton->setEnabled(false); this->label->setText("");
	this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPlay));
	QFileDialog* fileDialog=newQFileDialog("Open Vedio", "Vedio: (*.avi *.mp4 *.wmv *.flv *.txt)"); 
	fileDialog->setDirectory(QStandardPaths::standardLocations(QStandardPaths::MoviesLocation).value(0, QDir::homePath()));
	if(fileDialog->exec()!=QDialog::Accepted){this->label->setText("Error: Selected File is not Acceptable."); return;}
	this->mediaPlayer->setMedia(fileDialog->selectedUrls().constFirst());
	this->playButton->setEnabled(true); this->label->setText(""); 
}
void QWindow::save()
{
	QString fileName=QFileDialog::getSaveFileName();
	this->audioRecorder->setOutputLocation(QUrl::fromLocalFile(fileName));
}
void QWindow::play()
{
	if(audioRecorder->state()==QMediaRecorder::StoppedState)
	{
qDebug()<<"record";
		this->audioRecorder->record();
this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPlay));
	}
	else
	{
		this->audioRecorder->stop();
	this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPause));
}
	/*
	if(mediaPlayer->state()==QMediaPlayer::PlayingState)
	{
		this->mediaPlayer->pause();
		this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPlay));
	}
	else 
	{
		this->mediaPlayer->play();
		this->playButton->setIcon(style()->standardIcon(QStyle::SP_MediaPause));
	}
	*/
}
void QWindow::error()
{
	QString errorString=mediaPlayer->errorString();
	QString errorCode=QString::number(int(mediaPlayer->error()));
	this->label->setText("Error: "+errorString+"(Code #"+errorCode+").");
	this->playButton->setEnabled(false);
}
void QWindow::sliderMoved(int sliderPosition)
{
	this->mediaPlayer->setPosition(sliderPosition);
}
void QWindow::positionChanged(qint64 position)
{
	this->slider->setValue(position);
}
void QWindow::durationChanged(qint64 duration)
{
	this->slider->setRange(0, duration);
}
QFileDialog* QWindow::newQFileDialog(QString windowTitle, QString nameFilter)
{
	QFileDialog* fileDialog=new QFileDialog(this);
	fileDialog->setAcceptMode(QFileDialog::AcceptOpen);
	fileDialog->setWindowTitle(windowTitle);
	fileDialog->setNameFilter(nameFilter);
	return fileDialog;
}
