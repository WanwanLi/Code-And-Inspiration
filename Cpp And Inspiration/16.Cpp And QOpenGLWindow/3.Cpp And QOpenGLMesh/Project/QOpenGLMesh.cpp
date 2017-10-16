#include "QOpenGLMesh.h"

QOpenGLMesh::QOpenGLMesh(GLuint vertexLength, GLuint indexLength)
{
	this->positions=newGLfloat(vertexLength*3);
	this->colors=newGLfloat(vertexLength*3);
	this->indices=newGLushort(indexLength);
	this->vertexLength=vertexLength;
	this->indexLength=indexLength;
	this->positionIndex=0;
	this->colorIndex=0;
	this->index=0;
}
void QOpenGLMesh::addPosition(GLfloat x, GLfloat y, GLfloat z)
{
	this->positions[positionIndex++]=x;
	this->positions[positionIndex++]=y;
	this->positions[positionIndex++]=z;
}
void QOpenGLMesh::addColor(GLfloat r, GLfloat g, GLfloat b)
{
	this->colors[colorIndex++]=r;
	this->colors[colorIndex++]=g;
	this->colors[colorIndex++]=b;
}
void QOpenGLMesh::addIndices(GLushort i, GLushort j, GLushort k)
{
	this->indices[index++]=i;
	this->indices[index++]=j;
	this->indices[index++]=k;
}
void QOpenGLMesh::createBuffers()
{
	this->positionBuffer=newQOpenGLBuffer(QOpenGLBuffer::VertexBuffer, positions, sizeof(GLfloat), vertexLength*3);
	this->colorBuffer=newQOpenGLBuffer(QOpenGLBuffer::VertexBuffer, colors, sizeof(GLfloat), vertexLength*3);
	this->indexBuffer=newQOpenGLBuffer(QOpenGLBuffer::IndexBuffer, indices, sizeof(GLushort), indexLength);
}
GLfloat* QOpenGLMesh::newGLfloat(GLuint length)
{
	return (GLfloat*)malloc(sizeof(GLfloat)*length);
}
GLushort* QOpenGLMesh::newGLushort(GLuint length)
{
	return (GLushort*)malloc(sizeof(GLushort)*length);
}
QOpenGLBuffer* QOpenGLMesh::newQOpenGLBuffer(QOpenGLBuffer::Type type, GLvoid* buffer, GLuint size, GLuint length)
{
	QOpenGLBuffer* openGLBuffer=new QOpenGLBuffer(type); 
	openGLBuffer->setUsagePattern(QOpenGLBuffer::StaticDraw); 
	openGLBuffer->create(); openGLBuffer->bind();
	openGLBuffer->allocate(buffer, size*length);
	openGLBuffer->release(); return openGLBuffer;
}
QOpenGLMesh::~QOpenGLMesh()
{
	delete positionBuffer;
	delete colorBuffer;
	delete indexBuffer;
	free(positions);
	free(colors);
	free(indices);
}
