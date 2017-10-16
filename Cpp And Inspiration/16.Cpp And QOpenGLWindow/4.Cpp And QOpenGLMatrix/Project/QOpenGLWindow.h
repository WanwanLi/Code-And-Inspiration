#include "QOpenGLMesh.h"
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
	QOpenGLMesh*glMesh;
	QBasicTimer basicTimer;
	void updateViewDistance();
	void updateViewDirection();
	QPoint mouseMove, mousePos;
	qreal mouseScroll, mouseDelta;
	qreal zNear=1, zFar=100, fov=45;
	void initializeOpenGLParameters();
	QOpenGLMesh*newQOpenGLMesh();
	QOpenGLShaderProgram*glProgram;
	void timerEvent(QTimerEvent* event);
	void wheelEvent(QWheelEvent* event);
	QVector3D eye, center, right, up, forward;
	void mouseMoveEvent(QMouseEvent* event);
	void mousePressEvent(QMouseEvent* event);
	void mouseReleaseEvent(QMouseEvent* event);
	qreal clamp(qreal value, qreal min, qreal max);
	qreal rotateSpeed, translateSpeed, viewDistance;
	QMatrix4x4 rotateMatrix, glProjectionMatrix, glModelviewMatrix;
	QVector3D rotate(QVector3D vector, QVector3D axis, qreal angle);
	QOpenGLShaderProgram* newQOpenGLShaderProgram(QString vShader, QString fShader);
	void glBindAttributeBuffer(const char* name, QOpenGLBuffer* glBuffer, GLuint tupleSize);
};
