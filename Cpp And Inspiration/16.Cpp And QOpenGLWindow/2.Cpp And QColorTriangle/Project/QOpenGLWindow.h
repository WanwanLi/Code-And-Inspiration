#include <QDebug>
#include <QOpenGLBuffer>
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
	QOpenGLShaderProgram* glProgram;
	QOpenGLBuffer *glColorBuffer, *glPositionBuffer;
	QOpenGLBuffer* newQOpenGLBuffer(GLfloat* buffer, GLuint tupleSize, GLuint length);
	QOpenGLShaderProgram* newQOpenGLShaderProgram(QString vShader, QString fShader);
	void glBindAttributeBuffer(const char* name, QOpenGLBuffer* glBuffer, GLuint tupleSize);
};
