#include <QColor>
#include <QImage>
#include <QWidget>
#include <QPainter>
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
	QPen pen;
	QPoint point;
	QColor bgColor;
	QImage* image;
	QWidget* widget;
	int minLineWidth;
	int maxLineWidth;
	bool isTabletPressed;
	bool isMousePressed;
	bool isImageModified;
	void drawLine(QPoint point);
	void resizeWindow(int margin);
	int lineWidth(QTabletEvent* event);
	void paintEvent(QPaintEvent* event);
	void tabletEvent(QTabletEvent* event);
	void tabletMoveEvent(QTabletEvent* event);
	void tabletPressEvent(QTabletEvent* event);
	void tabletReleaseEvent(QTabletEvent* event);
	void mouseMoveEvent(QMouseEvent* event);
	void mousePressEvent(QMouseEvent* event);
	void mouseReleaseEvent(QMouseEvent* event);
};
