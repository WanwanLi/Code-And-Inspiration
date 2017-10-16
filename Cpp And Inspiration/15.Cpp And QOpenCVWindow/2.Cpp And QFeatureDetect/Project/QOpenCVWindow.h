#include <QLabel>
#include <QTimer>
#include <QWidget>
#include <QPainter>
#include <QGridLayout>
#include <QApplication>
#include <QPushButton>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#define QVideoCapture cv::VideoCapture
#define QVector4i std::vector<cv::Vec4i>
#define QFormat QImage::Format
#define QMatrix cv::Mat

class QOpenCVWindow : public QWidget
{
	Q_OBJECT
	public:	
	QOpenCVWindow(QWidget* widget=0);

	private slots:
	void play();
	void timeout();

	private:
	QTimer* timer;
	QVector4i lines;
	QLabel* statusLabel;
	QLabel* imageLabel[4];
	QPushButton* playButton;
	QVideoCapture* videoCapture;
	QGridLayout* newQGridLayout();
	QMatrix originalPixels, processedPixels;
	QImage scaledImage(QImage image, QSize size);
	QVideoCapture* newQVideoCapture(QLabel* label);
	void setPixmap(QLabel* label, QMatrix pixels, QFormat format);
};
