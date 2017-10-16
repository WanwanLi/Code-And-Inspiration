#include <QOpenGLBuffer>

class QOpenGLMesh
{
	public:
	QOpenGLMesh(GLuint vertexLength, GLuint indexLength);
	void addIndices(GLushort i, GLushort j, GLushort k);
	void addPosition(GLfloat x, GLfloat y, GLfloat z);
	void addColor(GLfloat r, GLfloat g, GLfloat b);
	QOpenGLBuffer* positionBuffer;
	QOpenGLBuffer* colorBuffer;
	QOpenGLBuffer* indexBuffer;
	GLuint vertexLength; 
	GLuint indexLength;
	void createBuffers();
	~QOpenGLMesh();

	private:
	GLuint index;
	GLfloat* colors;
	GLuint colorIndex;
	GLushort* indices;
	GLfloat* positions;
	GLuint positionIndex;
	GLfloat* newGLfloat(GLuint length);
	GLushort* newGLushort(GLuint length);
	QOpenGLBuffer* newQOpenGLBuffer(QOpenGLBuffer::Type type, GLvoid* buffer, GLuint size, GLuint length);
};
