#include "QOpenGLCube.h"

QOpenGLCube::QOpenGLCube(GLuint vertexLength, GLuint indexLength): QOpenGLMesh(vertexLength, indexLength)
{
	typedef QVector3D V;
	typedef GLvertex G;
	QVector3D p[]=
	{
		V(1.0f,  -1.0f, 1.0f), V(1.0f,  1.0f, 1.0f), V(-1.0f,  1.0f, 1.0f), V(-1.0f,  -1.0f, 1.0f), 
		V(1.0f,  -1.0f, -1.0f), V(1.0f,  1.0f, -1.0f), V(-1.0f,  1.0f, -1.0f), V(-1.0f,  -1.0f, -1.0f)
	};
	QVector3D c[]=
	{
		V(1.0f,  0.0f, 1.0f), V(1.0f,  1.0f, 1.0f), V(0.0f,  1.0f, 1.0f), V(0.0f,  0.0f, 1.0f), 
		V(1.0f,  0.0f, 0.0f), V(1.0f,  1.0f, 0.0f), V(0.0f,  1.0f, 0.0f), V(0.0f,  0.0f, 0.0f)
	};
	QVector3D n[]=
	{
		V(1.0f,  0.0f, 0.0f), V(0.0f,  1.0f, 0.0f), V(0.0f,  0.0f, 1.0f),
		V(-1.0f,  0.0f, 0.0f), V(0.0f,  -1.0f, 0.0f), V(0.0f,  0.0f, -1.0f)
	};
	GLvertex v[]=
	{
		G(p[0], n[2], c[0]), G(p[1], n[2], c[1]), G(p[2], n[2], c[2]), G(p[3], n[2], c[3]),
		G(p[7], n[5], c[7]), G(p[6], n[5], c[6]), G(p[5], n[5], c[5]), G(p[4], n[5], c[4]), 
		G(p[1], n[1], c[1]), G(p[5], n[1], c[5]), G(p[6], n[1], c[6]), G(p[2], n[1], c[2]), 
		G(p[0], n[4], c[0]), G(p[3], n[4], c[3]), G(p[7], n[4], c[7]), G(p[4], n[4], c[4]),
		G(p[0], n[0], c[0]), G(p[4], n[0], c[4]), G(p[5], n[0], c[5]), G(p[1], n[0], c[1]), 
		G(p[3], n[3], c[3]), G(p[2], n[3], c[2]), G(p[6], n[3], c[6]), G(p[7], n[3], c[7])
	};
	this->addQuad(v[0], v[1], v[2], v[3]);
	this->addQuad(v[4], v[5], v[6], v[7]);
	this->addQuad(v[8], v[9], v[10], v[11]);
	this->addQuad(v[12], v[13], v[14], v[15]);
	this->addQuad(v[16], v[17], v[18], v[19]);
	this->addQuad(v[20], v[21], v[22], v[23]);
	this->createBuffers();
}
