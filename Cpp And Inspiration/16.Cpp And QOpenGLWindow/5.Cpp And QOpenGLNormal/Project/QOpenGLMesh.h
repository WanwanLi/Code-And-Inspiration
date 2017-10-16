#include "QOpenGLVertex.h"
#include <QOpenGLBuffer>

class QOpenGLMesh
{
	public:
	QOpenGLMesh(GLuint vertexLength, GLuint indexLength);
	void addQuad(GLvertex v0, GLvertex v1, GLvertex v2, GLvertex v3);
	void addIndices(GLushort i, GLushort j, GLushort k);
	void addPosition(GLfloat x, GLfloat y, GLfloat z);
	void addNormal(GLfloat x, GLfloat y, GLfloat z);
	void addColor(GLfloat r, GLfloat g, GLfloat b);
	void addPosition(GLfloat* position);
	void addNormal(GLfloat* normal);
	void addColor(GLfloat* color);
	void addVertex(GLvertex vertex);
	QOpenGLBuffer* positionBuffer;
	QOpenGLBuffer* normalBuffer;
	QOpenGLBuffer* colorBuffer;
	QOpenGLBuffer* indexBuffer;
	GLuint vertexLength; 
	GLuint indexLength;
	void createBuffers();
	~QOpenGLMesh();

	private:
	GLuint index;
	GLfloat* colors;
	GLfloat* normals;
	GLfloat* positions;
	GLushort* indices;
	GLuint positionIndex;
	GLuint normalIndex;
	GLuint colorIndex;
	GLuint vertexIndex;
	GLfloat* newGLfloat(GLuint length);
	GLushort* newGLushort(GLuint length);
	QOpenGLBuffer* newQOpenGLBuffer(QOpenGLBuffer::Type type, GLvoid* buffer, GLuint size, GLuint length);
};
