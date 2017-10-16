#include <glsl.h>
#include <glsw.h>
#include <math.h>
#include <string.h>
#define String char*

typedef struct Vector2
{
	float x;
	float y;
} 
vec2;

typedef struct Vector3
{
	float x;
	float y;
	float z;
} 
vec3;

GLuint glslCreateShader(GLuint GL_TYPE_SHADER, const String glShaderName, const String glSource)
{
	GLuint glShader=0;
	GLint compileSuccess;
	GLchar compilerSpew[256];
	if (glShaderName) 
	{
		glShader = glCreateShader(GL_TYPE_SHADER);
		glShaderSource(glShader, 1, &glSource, 0);
		glCompileShader(glShader);
		glGetShaderiv(glShader, GL_COMPILE_STATUS, &compileSuccess);
		glGetShaderInfoLog(glShader, sizeof(compilerSpew), 0, compilerSpew);
		glslCheckCondition(compileSuccess, "Can't compile %s:\n%s", glShaderName, compilerSpew);
	}
	return glShader;
}

void glslAttachShader(GLuint GL_TYPE_SHADER, const String glShaderName, const String glMessage, GLuint glProgram)
{
	const String glSource = glswGetShader(glShaderName);
	glslCheckCondition(glShaderName == 0 || glSource != 0, glMessage, glShaderName, glShaderName);
	GLuint glShader = glslCreateShader(GL_TYPE_SHADER, glShaderName, glSource);
	if(glShaderName)glAttachShader(glProgram, glShader);
}

void glslLinkProgram(GLuint glProgram, const String vShader, const String gShader, const String fShader)
{
	glLinkProgram(glProgram);
	GLint linkSuccess; 	
	GLchar glDebugString[256];
	glGetProgramiv(glProgram, GL_LINK_STATUS, &linkSuccess);
	glGetProgramInfoLog(glProgram, sizeof(glDebugString), 0, glDebugString);
	if (!linkSuccess) 
	{
		glslDebugString("Link error.\n");
		if(vShader)glslDebugString("Vertex Shader: %s\n", vShader);
		if(gShader)glslDebugString("Geometry Shader: %s\n", gShader);
		if(fShader)glslDebugString("Fragment Shader: %s\n", fShader);
		glslDebugString("%s\n", glDebugString);
	}
}

GLuint glShader;

void glUseShader(GLuint shader)
{
	glShader = shader;
	glUseProgram(shader);
}

void glAddShadersPath()
{
	glswInit();
	glswAddPath("../", ".glsl");
	glswAddPath("./", ".glsl");
	char resourcePath[128];
	strcpy(resourcePath, glslResourcePath());
	strcat(resourcePath, "/");
	glswAddPath(resourcePath, ".glsl");
	glswAddDirective("*", "#version 150");
}

GLuint glCreateShaders(const String vShader, const String gShader, const String fShader)
{
	static int first = 1;
	if (first) {glAddShadersPath(); first = 0;}
	GLuint glProgram = glCreateProgram();
	const String glMessage = "Can't find %s shader: '%s'.\n";
	glslAttachShader(GL_VERTEX_SHADER, vShader, glMessage, glProgram);
	glslAttachShader(GL_GEOMETRY_SHADER, gShader, glMessage, glProgram);
	glslAttachShader(GL_FRAGMENT_SHADER, fShader, glMessage, glProgram);
	glslLinkProgram(glProgram, vShader, gShader, fShader);
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

GLuint glCreateArrayBuffer(float* array, int length)
{
	GLuint arrayBuffer = glCreateBuffer();
	glBindBuffer(GL_ARRAY_BUFFER, arrayBuffer);
	glBufferData(GL_ARRAY_BUFFER, length*sizeof(float), array, GL_STATIC_DRAW);
	return arrayBuffer;
}

GLuint glCreateIndexBuffer(int* indices, int length)
{
	GLuint indexBuffer = glCreateBuffer();
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, length*sizeof(int), indices, GL_STATIC_DRAW);
	return indexBuffer;
}

void glEnableVertexAttribute(String attributeName, GLuint verticesBuffer, int size)
{
	GLint attributeLocation = glGetAttribLocation(glShader, attributeName);
	glEnableVertexAttribArray(attributeLocation);
	glBindBuffer(GL_ARRAY_BUFFER, verticesBuffer);
	glVertexAttribPointer(attributeLocation, size, GL_FLOAT, GL_FALSE, size*sizeof(float), 0);
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

void glBindActiveTexture(GLuint GL_TEXTUREX, GLuint texture)
{
	glActiveTexture(GL_TEXTUREX);
	glBindTexture(GL_TEXTURE_2D, texture);
}

void glUniform1v(String uniformName, float uniformValue)
{
	GLuint uniformLocation = glGetUniformLocation(glShader, uniformName);
	glUniform1f(uniformLocation, uniformValue);
}

void glUniform2v(String uniformName, float uniformValue1, float uniformValue2)
{
	GLuint uniformLocation = glGetUniformLocation(glShader, uniformName);
	glUniform2f(uniformLocation, uniformValue1, uniformValue2);
}

void glUniform3v(String uniformName, float uniformValue1, float uniformValue2, float uniformValue3)
{
	GLuint uniformLocation = glGetUniformLocation(glShader, uniformName);
	glUniform3f(uniformLocation, uniformValue1, uniformValue2, uniformValue3);
}
