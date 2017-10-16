#include "Fluid.h"
#include <math.h>

struct GLshader
{
	GLuint Jacobi;
	GLuint AddSmoke;
	GLuint AdvectFluid;
	GLuint AddDiffusion;
	GLuint SubtractGradient;
	GLuint ComputeDivergence;
	GLuint AddExternalForce;
	GLuint MoveObstacles;
	GLuint AdvectObstacles;
	GLuint AccelerateObstacles;

}
GLshaders;

void glInitShaders()
{
	GLshaders.Jacobi = glCreateShaders("Fluid.Vertex", 0, "Fluid.Jacobi");
	GLshaders.AddSmoke = glCreateShaders("Fluid.Vertex", 0, "Fluid.AddSmoke");
	GLshaders.AdvectFluid = glCreateShaders("Fluid.Vertex", 0, "Fluid.AdvectFluid");
	GLshaders.AddDiffusion = glCreateShaders("Fluid.Vertex", 0, "Fluid.AddDiffusion");
	GLshaders.SubtractGradient = glCreateShaders("Fluid.Vertex", 0, "Fluid.SubtractGradient");
	GLshaders.ComputeDivergence = glCreateShaders("Fluid.Vertex", 0, "Fluid.ComputeDivergence");
	GLshaders.AddExternalForce = glCreateShaders("Fluid.Vertex", 0, "Fluid.AddExternalForce");
	GLshaders.MoveObstacles = glCreateShaders("Fluid.Vertex", 0, "Fluid.MoveObstacles");
	GLshaders.AdvectObstacles = glCreateShaders("Fluid.Vertex", 0, "Fluid.AdvectObstacles");
	GLshaders.AccelerateObstacles = glCreateShaders("Fluid.Vertex", 0, "Fluid.AccelerateObstacles");

}

static void glResetState()
{
	glActiveTexture(GL_TEXTURE2); glBindTexture(GL_TEXTURE_2D, 0);
	glActiveTexture(GL_TEXTURE1); glBindTexture(GL_TEXTURE_2D, 0);
	glActiveTexture(GL_TEXTURE0); glBindTexture(GL_TEXTURE_2D, 0);
	glBindFramebuffer(GL_FRAMEBUFFER, 0);
	glDisable(GL_BLEND);
}

#define HEAT_MAP 1
#define DENCITY_MAP 2
#define VELOCITY_MAP 3
#define RAY_TRACING_MAP 4

static const float CellSize = 1.25f;
static const int ViewSize = 2, GridSize = 4;
static const float ObstacleThreshold1 = 0.1f;
static const int ViewportWidth = (GLSL_VIEWPORT_WIDTH);
static const int ViewportHeight = (GLSL_VIEWPORT_HEIGHT);
static const int GridWidth = (ViewportWidth / GridSize);
static const int GridHeight = (ViewportHeight / GridSize);
static const float SplatRadius = (float) (GridWidth / 20.0f);
static const int renderMode = RAY_TRACING_MAP ;

static const float AmbientTemperature = 300.0f;
static const float ImpulseTemperature =  5800.0f;
static const float ImpulseDensity = 1.0f;
static const float VelocityWidth = 0.00125f;
static const float GridNum = 120;
static const int JacobiIterationTime = 40;
static const float TimeStep = 0.08f;
static const float SmokeBuoyancy = 0.005f;
static const float SmokeWeight = 0.05f;
static const float DiffusionScale = 1.99f;
static const float GradientScale = 1.125f / CellSize;
static const float TemperatureDissipation = 0.99f;
static const float VelocityDissipation = 0.99f;
static const float DensityDissipation = 1.0f;
static const float ElasticCoefficient = 1.0f;
static const vec2 ImpulsePosition = { GridWidth / 2,  SplatRadius*2 };
static const vec2 InitObstacleVelocity = { 0, -100.0};
static const vec2 ZeroVector2 = { 0, 0};
static const vec3 FluidColor = { 0, 1, 1};

static FramebufferTexture Background, Divergence;
static DataFramebuffer Obstacles, ObstaclesVelocity, ObstaclesPosition;
static DataFramebuffer Velocity, Density, Pressure, Temperature;
static GLuint QuadVertexArray, VisualizeProgram;

void glAdvectFluid(FramebufferTexture velocity, FramebufferTexture source, FramebufferTexture obstacles, FramebufferTexture dest, float dissipation)
{
	GLuint p = GLshaders.AdvectFluid;
	glUseProgram(p);

	GLint inverseSize = glGetUniformLocation(p, "InverseSize");
	GLint timeStep = glGetUniformLocation(p, "TimeStep");
	GLint dissLoc = glGetUniformLocation(p, "Dissipation");
	GLint sourceTexture = glGetUniformLocation(p, "SourceTexture");
	GLint obstaclesTexture = glGetUniformLocation(p, "Obstacles");

	glUniform2f(inverseSize, 1.0f / GridWidth, 1.0f / GridHeight);
	glUniform1f(timeStep, TimeStep);
	glUniform1f(dissLoc, dissipation);
	glUniform1i(sourceTexture, 1);
	glUniform1i(obstaclesTexture, 2);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, velocity.texture);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, source.texture);
	glActiveTexture(GL_TEXTURE2);
	glBindTexture(GL_TEXTURE_2D, obstacles.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glJacobi(FramebufferTexture pressure, FramebufferTexture divergence, FramebufferTexture dest)
{
	GLuint p = GLshaders.Jacobi;
	glUseProgram(p);

	GLint alpha = glGetUniformLocation(p, "Alpha");
	GLint inverseBeta = glGetUniformLocation(p, "InverseBeta");
	GLint dSampler = glGetUniformLocation(p, "Divergence");
	GLint oSampler = glGetUniformLocation(p, "Obstacles");

	glUniform1f(alpha, -CellSize * CellSize);
	glUniform1f(inverseBeta, 0.25f);
	glUniform1i(dSampler, 1);
	glUniform1i(oSampler, 2);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, pressure.texture);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, divergence.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glAddDiffusion(FramebufferTexture source, FramebufferTexture obstacles, FramebufferTexture dest)
{
	GLuint p = GLshaders.AddDiffusion;
	glUseProgram(p);

	GLint sampler = glGetUniformLocation(p, "Obstacles");
	GLint timeStep = glGetUniformLocation(p, "TimeStep");
	GLint cellSquare = glGetUniformLocation(p, "CellSquare");
	GLint inverseSize = glGetUniformLocation(p, "InverseSize");
	GLint diffusionScale = glGetUniformLocation(p, "DiffusionScale");

	glUniform1i(sampler, 1);
	glUniform1f(timeStep, TimeStep);
	glUniform1f(cellSquare, CellSize*CellSize);
	glUniform1f(diffusionScale, DiffusionScale);
	glUniform2f(inverseSize, 1.0f / GridWidth, 1.0f / GridHeight);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, source.texture);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, obstacles.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}


void glSubtractGradient(FramebufferTexture velocity, FramebufferTexture pressure, FramebufferTexture obstacles, FramebufferTexture obstVelocity,  FramebufferTexture dest)
{
	GLuint p = GLshaders.SubtractGradient;
	glUseProgram(p);

	GLint inverseSize = glGetUniformLocation(p, "InverseSize");
	glUniform2f(inverseSize, 1.0f / GridWidth, 1.0f / GridHeight);
	GLint gradientScale = glGetUniformLocation(p, "GradientScale");
	GLint velocityScale = glGetUniformLocation(p, "VelocityScale");
	glUniform1f(gradientScale, GradientScale);
	glUniform1f(velocityScale, 1.0f/(ViewSize * GridSize));
	GLint halfCell = glGetUniformLocation(p, "HalfInverseCellSize");
	glUniform1f(halfCell, 0.5f / CellSize);

	GLint sampler = glGetUniformLocation(p, "Pressure");
	glUniform1i(sampler, 1);
	sampler = glGetUniformLocation(p, "Obstacles");
	glUniform1i(sampler, 2);
	sampler = glGetUniformLocation(p, "ObstVelocity");
	glUniform1i(sampler, 3);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, velocity.texture);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, pressure.texture);
	glActiveTexture(GL_TEXTURE2);
	glBindTexture(GL_TEXTURE_2D, obstacles.texture);
	glActiveTexture(GL_TEXTURE3);
	glBindTexture(GL_TEXTURE_2D, obstVelocity.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glComputeDivergence(FramebufferTexture velocity, FramebufferTexture obstacles, FramebufferTexture obstVelocity,  FramebufferTexture dest)
{
	GLuint p = GLshaders.ComputeDivergence;
	glUseProgram(p);

	GLint velocityScale = glGetUniformLocation(p, "VelocityScale");
	glUniform1f(velocityScale, 1.0f/(ViewSize * GridSize));
	GLint halfCell = glGetUniformLocation(p, "HalfInverseCellSize");
	glUniform1f(halfCell, 0.5f / CellSize);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, velocity.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glAddSmoke(FramebufferTexture dest, vec2 position, float value)
{
	GLuint p = GLshaders.AddSmoke;
	glUseProgram(p);

	GLint scale = glGetUniformLocation(p, "Scale");
	GLint center = glGetUniformLocation(p, "Center");
	GLint radius = glGetUniformLocation(p, "Radius");
	GLint fillColor = glGetUniformLocation(p, "FillColor");

	glUniform2f(scale, 0.5f, 1.0f);
	glUniform2f(center, position.X, position.Y);
	glUniform1f(radius, SplatRadius);
	glUniform3f(fillColor, value, value, value);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glEnable(GL_BLEND);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glAddExternalForce(FramebufferTexture velocity, FramebufferTexture temperature, FramebufferTexture density, FramebufferTexture dest)
{
	GLuint p = GLshaders.AddExternalForce;
	glUseProgram(p);

	GLint tempSampler = glGetUniformLocation(p, "Temperature");
	GLint inkSampler = glGetUniformLocation(p, "Density");
	GLint ambTemp = glGetUniformLocation(p, "AmbientTemperature");
	GLint timeStep = glGetUniformLocation(p, "TimeStep");
	GLint sigma = glGetUniformLocation(p, "Sigma");
	GLint kappa = glGetUniformLocation(p, "Kappa");

	glUniform1i(tempSampler, 1);
	glUniform1i(inkSampler, 2);
	glUniform1f(ambTemp, AmbientTemperature);
	glUniform1f(timeStep, TimeStep);
	glUniform1f(sigma, SmokeBuoyancy);
	glUniform1f(kappa, SmokeWeight);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, velocity.texture);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, temperature.texture);
	glActiveTexture(GL_TEXTURE2);
	glBindTexture(GL_TEXTURE_2D, density.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glDrawObstacles(int width, int height)
{
	float T=1, vertices[] = {-T,-T,  T,-T,  -T,T,  T,T };
	GLuint vertexArray=glCreateVertexArray(vertices,  sizeof(vertices));
	GLuint program = glCreateShaders("Fluid.Vertex", 0, "Fluid.RayTracing");
	glUseProgram(program);
	GLint scale = glGetUniformLocation(program, "Scale");
	GLint isBox = glGetUniformLocation(program, "isObstacleBox");
	GLint eyePosition = glGetUniformLocation(program, "eyePosition");
	GLint lightDirection = glGetUniformLocation(program, "lightDirection");
	glUniform1i(isBox, 0);
	glUniform3f(eyePosition, 0,0,-1);
	glUniform3f(lightDirection, 0.383f, 0.604f, -0.698f);
	glUniform2f(scale, 1.0f / width, 1.0f / height);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glDeleteProgram(program);
	glDeleteVertexArrays(1, &vertexArray);
}

void glDrawQuad(float T,  float red, float green, float blue)
{
	float vertices[] = {-T,-T,  T,-T,  -T,T,  T,T };
	GLuint vertexArray = glCreateVertexArray(vertices,  sizeof(vertices));
	GLuint program = glCreateShaders("Fluid.Vertex", 0, "Fluid.Fill");
	glUseProgram(program);
	GLint fillColorLoc = glGetUniformLocation(program, "FillColor");
	glUniform3f(fillColorLoc, red, green, blue);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glDeleteProgram(program);
	glDeleteVertexArrays(1, &vertexArray);
}

void glCreateBackground(FramebufferTexture dest, int width, int height)
{
	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glViewport(0, 0, width, height);
	glClearColor(0, 0, 0, 0);
	glClear(GL_COLOR_BUFFER_BIT);
	float T=1, vertices[] = {-2*T,-T,  2*T,-T,  -2*T,T,  2*T,T };
	GLuint vertexArray=glCreateVertexArray(vertices,  sizeof(vertices));
	GLuint program = glCreateShaders("Fluid.Vertex", 0, "Fluid.RayTracing");
	glUseProgram(program);
	GLint scale = glGetUniformLocation(program, "Scale");
	GLint eyePosition = glGetUniformLocation(program, "eyePosition");
	GLint lightPosition = glGetUniformLocation(program, "lightPosition");
	GLint isBackground = glGetUniformLocation(program, "isBackground");
	glUniform3f(eyePosition, 0,0,-1);
	glUniform3f(lightPosition, 0, 1.2f, 1.0f);
	glUniform2f(scale, 1.0f / width, 1.0f / height);
	glUniform1i(isBackground, 1);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glDeleteProgram(program);
	glDeleteVertexArrays(1, &vertexArray);
}

void glCreateObstacles(FramebufferTexture dest, int width, int height, int isVelocity, vec2 velocity)
{
	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glEnable(GL_BLEND);
	glViewport(0, 0, width, height);
	glClearColor(0, 0, 0, 0);
	glClear(GL_COLOR_BUFFER_BIT);
	if(isVelocity==0)glDrawObstacles(width, height);
	else glDrawQuad(0.95f, velocity.X, velocity.Y, 0);
	glDisable(GL_BLEND);
}

void glAdvectObstacles(FramebufferTexture velocity, FramebufferTexture source, FramebufferTexture dest)
{
	GLuint p = GLshaders.AdvectObstacles;
	glUseProgram(p);

	GLint timeStep = glGetUniformLocation(p, "TimeStep");
	GLint inverseSize = glGetUniformLocation(p, "InverseSize");
	GLint threshold= glGetUniformLocation(p, "ObstacleThreshold");
	GLint sourceTexture = glGetUniformLocation(p, "SourceTexture");

	glUniform2f(inverseSize, 1.0f /(ViewportWidth * ViewSize), 1.0f / (ViewportHeight * ViewSize));
	glUniform1f(timeStep, TimeStep);
	glUniform1f(threshold, ObstacleThreshold1);
	glUniform1i(sourceTexture, 1);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, velocity.texture);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, source.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}


void glAccelerateObstacles(FramebufferTexture position, FramebufferTexture source, FramebufferTexture dest)
{
	GLuint p = GLshaders.AccelerateObstacles;
	glUseProgram(p);

	GLint inverseSize = glGetUniformLocation(p, "InverseSize");
	GLint Kappa = glGetUniformLocation(p, "Kappa");
	GLint timeStep = glGetUniformLocation(p, "TimeStep");
	GLint sourceTexture = glGetUniformLocation(p, "SourceTexture");


	glUniform2f(inverseSize, 1.0f /(ViewportWidth * ViewSize), 1.0f / (ViewportHeight * ViewSize));
	glUniform1f(Kappa, ElasticCoefficient); 
	glUniform1f(timeStep, TimeStep);
	glUniform1i(sourceTexture, 1);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, position.texture);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, source.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}


void glMoveObstacles(FramebufferTexture velocity, FramebufferTexture source, FramebufferTexture dest)
{
	GLuint p = GLshaders.MoveObstacles;
	glUseProgram(p);

	GLint inverseSize = glGetUniformLocation(p, "InverseSize");
	GLint timeStep = glGetUniformLocation(p, "TimeStep");
	GLint sourceTexture = glGetUniformLocation(p, "SourceTexture");

	glUniform2f(inverseSize, 1.0f /(ViewportWidth * ViewSize), 1.0f / (ViewportHeight * ViewSize));
	glUniform1f(timeStep, TimeStep);
	glUniform1i(sourceTexture, 1);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, velocity.texture);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, source.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

const char* glslInitialize(int width, int height)
{
	int w = GridWidth, h = GridHeight;
	Velocity = glCreateDataFramebuffer(w, h, 2);
	Density = glCreateDataFramebuffer(w, h, 1);
	Pressure = glCreateDataFramebuffer(w, h, 1);
	Temperature = glCreateDataFramebuffer(w, h, 1);
	Divergence = glCreateFramebufferTexture(w, h, 3);
	glInitShaders();
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	VisualizeProgram = glCreateShaders("Fluid.Vertex", 0, "Fluid.Visualize");
	w = ViewportWidth * ViewSize; h = ViewportHeight * ViewSize;
	Background = glCreateFramebufferTexture(w, h, 4);
	glCreateBackground(Background, w, h);
	Obstacles = glCreateDataFramebuffer(w, h, 4);
	glCreateObstacles(Obstacles.Ping, w, h, 0, ZeroVector2);
	ObstaclesPosition = glCreateDataFramebuffer(w, h, 4);
	glCreateObstacles(ObstaclesPosition.Ping, w, h, 1, ZeroVector2 );
	ObstaclesVelocity = glCreateDataFramebuffer(w, h, 4);
	glCreateObstacles(ObstaclesVelocity.Ping, w, h, 1, InitObstacleVelocity );
	float T=1, vertices[] = {-T,-T,  T,-T,  -T,T,  T,T };
	QuadVertexArray = glCreateVertexArray(vertices,  sizeof(vertices));
	glClearFramebufferTexture(Temperature.Ping, AmbientTemperature);
	return "C And Fluid2D";
}

void glslUpdate(unsigned int elapsedMicroseconds)
{
	glViewport(0, 0, ViewportWidth * ViewSize, ViewportHeight * ViewSize);
	glAdvectObstacles(ObstaclesVelocity.Ping, Obstacles.Ping, Obstacles.Pong);
	glSwapFramebufferTextures(&Obstacles);
	glAccelerateObstacles(ObstaclesPosition.Ping, ObstaclesVelocity.Ping, ObstaclesVelocity.Pong);
	glSwapFramebufferTextures(&ObstaclesVelocity);
	glMoveObstacles(ObstaclesVelocity.Ping, ObstaclesPosition.Ping, ObstaclesPosition.Pong);
	glSwapFramebufferTextures(&ObstaclesPosition);
	glViewport(0, 0, GridWidth, GridHeight);
	glAddSmoke(Density.Ping, ImpulsePosition, ImpulseDensity);
	glAddSmoke(Temperature.Ping, ImpulsePosition, ImpulseTemperature);
	glAdvectFluid(Velocity.Ping, Velocity.Ping, Obstacles.Ping, Velocity.Pong, VelocityDissipation);
	glSwapFramebufferTextures(&Velocity);
	glAdvectFluid(Velocity.Ping, Temperature.Ping, Obstacles.Ping, Temperature.Pong, TemperatureDissipation);
	glSwapFramebufferTextures(&Temperature);
	glAddDiffusion(Temperature.Ping, Obstacles.Ping, Temperature.Pong);
	glSwapFramebufferTextures(&Temperature); 
	glAdvectFluid(Velocity.Ping, Density.Ping, Obstacles.Ping, Density.Pong, DensityDissipation);
	glSwapFramebufferTextures(&Density);
	glAddDiffusion(Density.Ping, Obstacles.Ping, Density.Pong);
	glSwapFramebufferTextures(&Density);
	glAddExternalForce(Velocity.Ping, Temperature.Ping, Density.Ping, Velocity.Pong);
	glSwapFramebufferTextures(&Velocity);
	glComputeDivergence(Velocity.Ping, Obstacles.Ping, ObstaclesVelocity.Ping, Divergence);
	glClearFramebufferTexture(Pressure.Ping, 0);
	for (int i = 0; i < JacobiIterationTime; ++i) 
	{
		glJacobi(Pressure.Ping, Divergence, Pressure.Pong);
		glSwapFramebufferTextures(&Pressure);
	}
	glSubtractGradient(Velocity.Ping, Pressure.Ping, Obstacles.Ping, ObstaclesVelocity.Ping, Velocity.Pong);
	glSwapFramebufferTextures(&Velocity);
}

void glslRender(GLuint windowFbo)
{
	glUseProgram(VisualizeProgram);
	GLint fillColor = glGetUniformLocation(VisualizeProgram, "FillColor");
	GLint dSampler = glGetUniformLocation(VisualizeProgram, "dSampler");
	GLint scale = glGetUniformLocation(VisualizeProgram, "Scale");
	GLint isFluid = glGetUniformLocation(VisualizeProgram, "isFluid");
	GLint isHeat = glGetUniformLocation(VisualizeProgram, "isHeat");
	GLint isVelocity = glGetUniformLocation(VisualizeProgram, "isVelocity");
	GLint maxHeat = glGetUniformLocation(VisualizeProgram, "maxHeat");
	GLint minHeat = glGetUniformLocation(VisualizeProgram, "minHeat");
	GLint velocityWidth = glGetUniformLocation(VisualizeProgram, "velocityWidth");
	GLint velocityGridSize = glGetUniformLocation(VisualizeProgram, "velocityGridSize");
	glEnable(GL_BLEND);
	glViewport(0, 0, ViewportWidth, ViewportHeight);
	glBindFramebuffer(GL_FRAMEBUFFER, windowFbo);
	glClearColor(0, 0, 0, 1);
	glClear(GL_COLOR_BUFFER_BIT);
	glUniform3f(fillColor, 1, 1, 1);
	glUniform1i(dSampler, 1); 
	switch(renderMode)
	{
		case HEAT_MAP:
		glUniform1i(isHeat,true); 
		glUniform1f(maxHeat, ImpulseTemperature);
		glUniform1f(minHeat, AmbientTemperature);
		glBindTexture(GL_TEXTURE_2D, Temperature.Ping.texture); break;
		case DENCITY_MAP: 
		glUniform1i(isFluid,true); 
		glUniform3f(fillColor, FluidColor.X, FluidColor.Y, FluidColor.Z);
		glBindTexture(GL_TEXTURE_2D, Density.Ping.texture); break;
		case VELOCITY_MAP: 
		glUniform1i(isVelocity,true); 
		glUniform1f(velocityWidth, VelocityWidth);
		glUniform2f(velocityGridSize, 1.0/GridNum, 1.0/GridNum);
		glBindTexture(GL_TEXTURE_2D, Velocity.Ping.texture); 
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, Density.Ping.texture);
		glActiveTexture(GL_TEXTURE0); break;
		case RAY_TRACING_MAP:
		glBindTexture(GL_TEXTURE_2D, Background.texture);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		glUniform1i(isFluid,true); 
		glBindTexture(GL_TEXTURE_2D, Density.Ping.texture); break;
	}
	glUniform2f(scale, 1.0f / ViewportWidth, 1.0f / ViewportHeight);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glBindTexture(GL_TEXTURE_2D, Obstacles.Ping.texture);
	glUniform1i(isFluid,false);
	glUniform1i(isHeat,false);
	glUniform1i(isVelocity,false);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glDisable(GL_BLEND);
}

void glslHandleMouse(int x, int y, int action)
{
}
