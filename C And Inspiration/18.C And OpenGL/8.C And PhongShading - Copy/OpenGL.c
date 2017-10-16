#include "OpenGL.h"

int ViewportWidth = GLSL_VIEWPORT_WIDTH;
int ViewportHeight = GLSL_VIEWPORT_HEIGHT;
GLuint glPositionArray;

const String glslInitialize(int width, int height)
{
	glShader = glCreateShaders("OpenGL.Vertex", 0, "OpenGL.Fragment");
	glUseShader(glShader);
	glEnable(GL_DEPTH_TEST);
	float T = 0.5, positions[] = { -T,-T, 0,  T,-T,0,   -T,T,0,   T,T,0 };
	glPositionArray = glCreateArrayBuffer(positions, 12);
	glEnableVertexAttribute("position", glPositionArray, 3);
	return "OpenGL";
}

void glslRender(GLuint windowFramebuffer)
{
	glBindFramebuffer(GL_FRAMEBUFFER, windowFramebuffer);
	glViewport(0, 0, ViewportWidth, ViewportHeight);
	glClearColor(0, 0, 0, 1);
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

void glslUpdate(unsigned int elapsedMicroseconds)
{
	
}

void glslHandleMouse(int x, int y, int action)
{
}
