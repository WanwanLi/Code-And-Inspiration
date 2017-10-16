#include "QOpenGLMesh.h"
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
	QOpenGLMesh* glMesh;
	void glDrawTriangleArrays();
	void glDrawTriangleElements();
	QOpenGLMesh* newQOpenGLMesh();
	QOpenGLShaderProgram* glProgram;
	QOpenGLShaderProgram* newQOpenGLShaderProgram(QString vShader, QString fShader);
	void glBindAttributeBuffer(const char* name, QOpenGLBuffer* glBuffer, GLuint tupleSize);
};
