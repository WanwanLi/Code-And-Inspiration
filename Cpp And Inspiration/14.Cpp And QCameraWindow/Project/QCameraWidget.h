#include <QLabel>
#include <QWidget>
#include <QCamera>
#include <QPushButton>
#include <QCameraImageCapture>

class QCameraWidget : public QWidget
{
	Q_OBJECT
	public:	
	QCamera* camera;
	QCameraWidget(QWidget* widget=0);
	void setButton(QPushButton* button);
	void setLabel(QLabel* label);
	void captureImage();

	public slots:
	void readyForCaptureChanged(bool ready);
	void imageSaved(int id, QString fileName);
	void setExposureCompensation(int index);

	private:
	QLabel* label;
	QPushButton* button;
	QCameraImageCapture* cameraImageCapture;
};
