#include <string.h>
#include <glsl.h>
#include <glsw.h>

typedef struct Vector2_ 
{
	int X;
	int Y;
} 
Vector2;

GLuint glslCreateShader(GLuint GL_TYPE_SHADER, const char* glKey, const char* glSource)
{
	GLuint glShader=0;
	GLint compileSuccess;
	GLchar compilerSpew[256];
	if (glKey) 
	{
		glShader = glCreateShader(GL_TYPE_SHADER);
		glShaderSource(glShader, 1, &glSource, 0);
		glCompileShader(glShader);
		glGetShaderiv(glShader, GL_COMPILE_STATUS, &compileSuccess);
		glGetShaderInfoLog(glShader, sizeof(compilerSpew), 0, compilerSpew);
		glslCheckCondition(compileSuccess, "Can't compile %s:\n%s", glKey, compilerSpew);
	}
	return glShader;
}

GLuint glCreateShaders(const char* vsKey, const char* gsKey, const char* fsKey)
{
	static int first = 1;
	if (first) 
	{
		glswInit();
		glswAddPath("../", ".glsl");
		glswAddPath("./", ".glsl");
		char qualifiedPath[128];
		strcpy(qualifiedPath, glslResourcePath());
		strcat(qualifiedPath, "/");
		glswAddPath(qualifiedPath, ".glsl");
		glswAddDirective("*", "#version 150");
		first = 0;
	}

	const char* vsSource = glswGetShader(vsKey);
	const char* gsSource = glswGetShader(gsKey);
	const char* fsSource = glswGetShader(fsKey);
	const char* msg = "Can't find %s shader: '%s'.\n";
	glslCheckCondition(vsSource != 0, msg, "vertex", vsKey);
	glslCheckCondition(gsKey == 0 || gsSource != 0, msg, "geometry", gsKey);
	glslCheckCondition(fsKey == 0 || fsSource != 0, msg, "fragment", fsKey);

	GLchar compilerSpew[256];
	GLuint glProgram = glCreateProgram();
	GLuint vsShader = glslCreateShader(GL_VERTEX_SHADER, vsKey, vsSource);
	GLuint gsShader = glslCreateShader(GL_GEOMETRY_SHADER, gsKey, gsSource);
	GLuint fsShader = glslCreateShader(GL_FRAGMENT_SHADER, fsKey, fsSource);
	if(vsKey)glAttachShader(glProgram, vsShader);
	if(gsKey)glAttachShader(glProgram, gsShader);
	if(fsKey)glAttachShader(glProgram, fsShader);
	glLinkProgram(glProgram);
	
	GLint linkSuccess;
	glGetProgramiv(glProgram, GL_LINK_STATUS, &linkSuccess);
	glGetProgramInfoLog(glProgram, sizeof(compilerSpew), 0, compilerSpew);
	if (!linkSuccess) 
	{
		glslDebugString("Link error.\n");
		if(vsKey)glslDebugString("Vertex Shader: %s\n", vsKey);
		if(gsKey)glslDebugString("Geometry Shader: %s\n", gsKey);
		if(fsKey)glslDebugString("Fragment Shader: %s\n", fsKey);
		glslDebugString("%s\n", compilerSpew);
	}
	return glProgram;
}

GLuint glCreateArray()
{
	GLuint array;
	glGenVertexArrays(1, &array);
	return array;
}

GLuint glCreateBuffer()
{
	GLuint buffer;
	glGenBuffers(1, &buffer);
	return buffer;
}

GLuint glCreateTexture()
{
	GLuint texture;
	glGenTextures(1, &texture);
	return texture;
}

GLuint glCreateFramebuffer()
{
	GLuint framebuffer;
	glGenFramebuffers(1, &framebuffer);
	return framebuffer;
}

GLuint glCreateRenderbuffer()
{
	GLuint renderbuffer;
	glGenRenderbuffers(1, &renderbuffer);
	return renderbuffer;
}

GLuint glCreateQuadObject(float* vertices, int size)
{
	GLuint vertexArray=glCreateArray();
	glBindVertexArray(vertexArray);
	GLuint vertexBuffer=glCreateBuffer();
	glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
	glBufferData(GL_ARRAY_BUFFER, size, vertices, GL_STATIC_DRAW);
	int PositionSlot=0;
	glEnableVertexAttribArray(PositionSlot);
	glVertexAttribPointer(PositionSlot, 2, GL_FLOAT, GL_FALSE, 2*sizeof(float), 0);
	return vertexArray;
}

typedef struct FramebufferTexture_ 
{
	GLuint framebuffer;
	GLuint texture;
	int NumComponents;
} 
FramebufferTexture;

FramebufferTexture glCreateFramebufferTexture(GLsizei width, GLsizei height, int numComponents)
{
	GLuint framebuffer=glCreateFramebuffer();
	glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);

	GLuint texture=glCreateTexture();
	glBindTexture(GL_TEXTURE_2D, texture);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

	switch(numComponents)
	{
		case 1: glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, width, height, 0, GL_RED, GL_FLOAT, 0); break;
		case 2: glTexImage2D(GL_TEXTURE_2D, 0, GL_RG32F, width, height, 0, GL_RG, GL_FLOAT, 0); break;
		case 3: glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGB, GL_FLOAT, 0); break;
		case 4: glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, width, height, 0, GL_RGBA, GL_FLOAT, 0); break;
		default: glslFatalError("Illegal slab format.");
	}
	glslCheckCondition(GL_NO_ERROR == glGetError(), "Unable to create normals texture");

	GLuint renderbuffer=glCreateRenderbuffer();
	glBindRenderbuffer(GL_RENDERBUFFER, renderbuffer);
	glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);
	glslCheckCondition(GL_NO_ERROR == glGetError(), "Unable to attach render buffer");
	glslCheckCondition(GL_FRAMEBUFFER_COMPLETE == glCheckFramebufferStatus(GL_FRAMEBUFFER), "Unable to create frame buffer");
	FramebufferTexture framebufferTexture = { framebuffer, texture, numComponents };

	glClearColor(0, 0, 0, 0);
	glClear(GL_COLOR_BUFFER_BIT);
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
	return framebufferTexture;
}

typedef struct DataFramebuffer_ 
{
	FramebufferTexture Ping;
	FramebufferTexture Pong;
} 
DataFramebuffer;


DataFramebuffer glCreateDataFramebuffer(GLsizei width, GLsizei height, int numComponents)
{
	DataFramebuffer dataFramebuffer;
	dataFramebuffer.Ping = glCreateFramebufferTexture(width, height, numComponents);
	dataFramebuffer.Pong = glCreateFramebufferTexture(width, height, numComponents);
	return dataFramebuffer;
}

void glSwapFramebufferTextures(DataFramebuffer* dataFramebuffer)
{
	FramebufferTexture temp = dataFramebuffer->Ping;
	dataFramebuffer->Ping = dataFramebuffer->Pong;
	dataFramebuffer->Pong = temp;
}

void glClearFramebufferTexture(FramebufferTexture s, float v)
{
	glBindFramebuffer(GL_FRAMEBUFFER, s.framebuffer);
	glClearColor(v, v, v, v);
	glClear(GL_COLOR_BUFFER_BIT);
}
