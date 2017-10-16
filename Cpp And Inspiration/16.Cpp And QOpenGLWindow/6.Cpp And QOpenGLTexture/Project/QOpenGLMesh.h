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
	void addTexcoord(GLfloat x, GLfloat y);
	void addTexcoord(GLfloat* texcoord);
	void addPosition(GLfloat* position);
	void addNormal(GLfloat* normal);
	void addVertex(GLvertex vertex);
	QOpenGLBuffer* texcoordBuffer;
	QOpenGLBuffer* positionBuffer;
	QOpenGLBuffer* normalBuffer;
	QOpenGLBuffer* indexBuffer;
	GLuint vertexLength; 
	GLuint indexLength;
	void createBuffers();
	~QOpenGLMesh();

	private:
	GLuint index;
	GLushort* indices;
	GLfloat* normals;
	GLfloat* positions;
	GLfloat* texcoords;
	GLuint texcoordIndex;
	GLuint positionIndex;
	GLuint normalIndex;
	GLuint vertexIndex;
	GLfloat* newGLfloat(GLuint length);
	GLushort* newGLushort(GLuint length);
	QOpenGLBuffer* newQOpenGLBuffer(QOpenGLBuffer::Type type, GLvoid* buffer, GLuint size, GLuint length);
};
