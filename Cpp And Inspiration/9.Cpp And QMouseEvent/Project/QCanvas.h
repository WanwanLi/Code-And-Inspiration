#include <QColor>
#include <QImage>
#include <QWidget>
#include <QPaintEvent>

class QCanvas : public QWidget
{
	Q_OBJECT
	public:
	QCanvas(QWidget* widget=0);
	bool loadImage(QString fileName);
	bool saveImage(QString fileName, const char* fileFormat);

	public slots:
	bool isModified();
	void clearImage();
	void resizeImage();

	private:
	int lineWidth;
	QPoint point;
	QColor color;
	QColor bgColor;
	QImage* image;
	QWidget* widget;
	bool isMousePressed;
	bool isImageModified;
	void drawLine(QPoint point);
	void resizeWindow(int margin);
	void paintEvent(QPaintEvent* event);
	void mouseMoveEvent(QMouseEvent* event);
	void mousePressEvent(QMouseEvent* event);
	void mouseReleaseEvent(QMouseEvent* event);
};
