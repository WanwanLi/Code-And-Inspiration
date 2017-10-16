#include <QLabel>
#include <QSlider>
#include <QWidget>
#include <QFileDialog>
#include <QBoxLayout>
#include <QApplication>
#include <QPushButton>
#include "QCameraWidget.h"

class QWindow : public QWidget
{
	Q_OBJECT
	public:	
	QWindow(QWidget* widget=0);

	private slots:
	void play();

	private:
	QLabel* label;
	QSlider* slider;
	QPushButton* playButton;
	QPushButton* captureButton;
	QCameraWidget* cameraWidget;
	QBoxLayout* newQBoxLayout();
};
