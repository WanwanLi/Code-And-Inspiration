#include "QOpenGLCube.h"
#include <QBasicTimer>
#include <QMouseEvent>
#include <QOpenGLWidget>
#include <QOpenGLFunctions>
#include <QOpenGLShaderProgram>

class QOpenGLWindow : public QOpenGLWidget, protected QOpenGLFunctions
{
	Q_OBJECT
	public:
	void initializeGL();
	void resizeGL(int width, int height);
	void paintGL();
	void teardownGL();
	~QOpenGLWindow();

	private:
	bool isMousePressed;
	QOpenGLCube*glCube;
	QBasicTimer basicTimer;
	void updateViewDistance();
	void updateViewDirection();
	QPoint mouseMove, mousePos;
	qreal mouseScroll, mouseDelta;
	qreal zNear=1, zFar=100, fov=45;
	void initializeOpenGLParameters();
	QOpenGLShaderProgram*glProgram;
	void timerEvent(QTimerEvent* event);
	void wheelEvent(QWheelEvent* event);
	QVector3D eye, center, right, up, forward;
	void mouseMoveEvent(QMouseEvent* event);
	void mousePressEvent(QMouseEvent* event);
	void mouseReleaseEvent(QMouseEvent* event);
	qreal clamp(qreal value, qreal min, qreal max);
	QVector3D rotate(QVector3D vector, QVector3D axis, qreal angle);
	QMatrix4x4 rotateMatrix, glProjectionMatrix, glModelMatrix, glViewMatrix;
	qreal rotateAngle, angularSpeed, rotateSpeed, translateSpeed, viewDistance;
	QOpenGLShaderProgram* newQOpenGLShaderProgram(QString vShader, QString fShader);
	void glBindAttributeBuffer(const char* name, QOpenGLBuffer* glBuffer, GLuint tupleSize);
};
