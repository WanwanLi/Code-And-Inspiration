#include <QPen>
#include <QColor>
#include <QPoint>
#include <QImage>
#include <QWidget>
#include "QSketch.h"
#include <QVector3D>
#include <QMatrix4x4>
#include <QPaintEvent>
#include <QBasicTimer>

class QCanvas : public QWidget
{
	Q_OBJECT
	public:
	QCanvas(QWidget* widget=0);
	bool loadImage(QString fileName);
	bool saveImage(QString fileName, const char* fileFormat);

	public slots:
	void clear();
	bool isModified();
	void resizeImage();
	void perturbPlanes();
	void minimizeEnergy();
	void createRelations();
	void setCameraUpward();

	private:
	QPen pen;
	QPoint point;
	QPen marker;
	bool isUpdated;
	QColor bgColor;
	QImage image;
	QSketch sketch;
	QWidget* widget;
	void clearImage();
	void drawSketch();
	int matchIndex=0;
	bool isMousePressed;
	void updateViewInfo();
	QColor color(int index);
	QVector<QColor> colors;
	QBasicTimer basicTimer;
	QMatrix4x4 rotateMatrix;
	void rotateViewDirection();
	void updateViewDistance();
	void updateViewDirection();
	bool isCameraUpward=false;
	QVector3D right, up, forward;
	void resizeWindow(int margin);
	QPoint mouseMove, mousePos;
	qreal mouseScroll, mouseDelta;
	void paintEvent(QPaintEvent* event);
	void timerEvent(QTimerEvent* event);
	void wheelEvent(QWheelEvent* event);
	void keyPressEvent(QKeyEvent* event);
	void mouseMoveEvent(QMouseEvent* event);
	void mousePressEvent(QMouseEvent* event);
	void mouseReleaseEvent(QMouseEvent* event);
	qreal clamp(qreal value, qreal min, qreal max);
	qreal rotateSpeed, translateSpeed, viewDistance;
	QVector3D rotate(QVector3D vector, QVector3D axis, qreal angle);
};
