#include <QVector3D>
#include <QOpenGLFunctions>

class QOpenGLVertex
{
	public:
	QOpenGLVertex();
	QOpenGLVertex(QVector3D position, QVector3D normal, QVector3D color);
	void setPosition(GLfloat x, GLfloat y, GLfloat z);
	void setNormal(GLfloat x, GLfloat y, GLfloat z);
	void setColor(GLfloat r, GLfloat g, GLfloat b);
	GLfloat* position;
	GLfloat* normal;
	GLfloat* color;

	private:
	GLfloat* newGLvertex3f(GLfloat f0, GLfloat f1, GLfloat f2);
};
typedef QOpenGLVertex GLvertex;