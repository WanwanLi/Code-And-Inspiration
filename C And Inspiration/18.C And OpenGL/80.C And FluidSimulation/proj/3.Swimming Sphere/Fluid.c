#include "Fluid.h"
#include <math.h>

struct GLshader
{
	GLuint Advect;
	GLuint Jacobi;
	GLuint AddAir;
	GLuint SubtractGradient;
	GLuint ComputeDivergence;
	GLuint ApplyImpulse;
	GLuint ApplyBuoyancy;
	GLuint MoveObstacles;
	GLuint AdvectObstacles;
	GLuint AccelerateObstacles;

}
GLshaders;

void glInitShaders()
{
	GLshaders.Advect = glCreateShaders("Fluid.Vertex", 0, "Fluid.Advect");
	GLshaders.Jacobi = glCreateShaders("Fluid.Vertex", 0, "Fluid.Jacobi");
	GLshaders.SubtractGradient = glCreateShaders("Fluid.Vertex", 0, "Fluid.SubtractGradient");
	GLshaders.ComputeDivergence = glCreateShaders("Fluid.Vertex", 0, "Fluid.ComputeDivergence");
	GLshaders.AddAir = glCreateShaders("Fluid.Vertex", 0, "Fluid.AddAir");
	GLshaders.ApplyImpulse = glCreateShaders("Fluid.Vertex", 0, "Fluid.Splat");
	GLshaders.ApplyBuoyancy = glCreateShaders("Fluid.Vertex", 0, "Fluid.Buoyancy");
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

static FramebufferTexture Divergence;
static DataFramebuffer Obstacles, ObstaclesVelocity, ObstaclesPosition;
static DataFramebuffer Velocity, Density, Pressure, Temperature;
static GLuint QuadVertexArray, VisualizeProgram;

static const float CellSize = 1.25f;
static const int ViewportWidth = (GLSL_VIEWPORT_WIDTH);
static const int ViewportHeight = (GLSL_VIEWPORT_HEIGHT);
static const int GridWidth = (ViewportWidth / 4);
static const int GridHeight = (ViewportHeight / 4);
static const float SplatRadius = (float) (GridWidth / 6.0f);

static const float AmbientTemperature = 0.0f;
static const float ImpulseTemperature = 10.0f;
static const float ImpulseDensity = 1.0f;
static const int NumJacobiIterations = 40;
static const float TimeStep = 0.05f;
static const float SmokeBuoyancy = 1.0f;
static const float SmokeWeight = 0.05f;
static const float GradientScale = 1.125f / CellSize;
static const float TemperatureDissipation = 0.99f;
static const float VelocityDissipation = 0.99f;
static const float DensityDissipation = 1.0f;
static const float ElasticCoefficient = 2.0f;
static const float GravityAcceleration = 30.0f;
static const vec2 ImpulsePosition = { GridWidth / 2, - (int) SplatRadius / 2};
static const vec2 InitObstacleVelocity = { -500.0, 0.0};
static const vec2 ZeroVector2 = { 0, 0};
static int time=0;

void glAdvect(FramebufferTexture velocity, FramebufferTexture source, FramebufferTexture obstacles, FramebufferTexture dest, float dissipation)
{
	GLuint p = GLshaders.Advect;
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

void glJacobi(FramebufferTexture pressure, FramebufferTexture divergence, FramebufferTexture obstacles, FramebufferTexture dest)
{
	GLuint p = GLshaders.Jacobi;
	glUseProgram(p);

	GLint inverseSize = glGetUniformLocation(p, "InverseSize");
	glUniform2f(inverseSize, 1.0f / GridWidth, 1.0f / GridHeight);

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
	glActiveTexture(GL_TEXTURE2);
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
	glUniform1f(gradientScale, GradientScale);
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

	GLint inverseSize = glGetUniformLocation(p, "InverseSize");
	glUniform2f(inverseSize, 1.0f / GridWidth, 1.0f / GridHeight);
	GLint halfCell = glGetUniformLocation(p, "HalfInverseCellSize");
	glUniform1f(halfCell, 0.5f / CellSize);
	GLint sampler = glGetUniformLocation(p, "Obstacles");
	glUniform1i(sampler, 1);
	sampler = glGetUniformLocation(p, "ObstVelocity");
	glUniform1i(sampler, 2);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, velocity.texture);
	glActiveTexture(GL_TEXTURE1);
	glBindTexture(GL_TEXTURE_2D, obstacles.texture);
	glActiveTexture(GL_TEXTURE2);
	glBindTexture(GL_TEXTURE_2D, obstVelocity.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glAddAir(FramebufferTexture obstacles, float red, float green, float blue, float density, FramebufferTexture dest)
{
	GLuint p = GLshaders.AddAir;
	glUseProgram(p);

	GLint densityLoc = glGetUniformLocation(p, "Density");
	GLint fillColorLoc = glGetUniformLocation(p, "FillColor");
	GLint inverseSize = glGetUniformLocation(p, "InverseSize");

	glUniform1f(densityLoc, density);
	glUniform3f(fillColorLoc,  red, green, blue);
	glUniform2f(inverseSize, 1.0f / GridWidth, 1.0f / GridHeight);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, obstacles.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glApplyImpulse(FramebufferTexture dest, vec2 position, float value)
{
	GLuint p = GLshaders.ApplyImpulse;
	glUseProgram(p);

	GLint pointLoc = glGetUniformLocation(p, "Point");
	GLint radiusLoc = glGetUniformLocation(p, "Radius");
	GLint fillColorLoc = glGetUniformLocation(p, "FillColor");

	glUniform2f(pointLoc, (float) position.X, (float) position.Y);
	glUniform1f(radiusLoc, SplatRadius);
	glUniform3f(fillColorLoc, value, value, value);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glEnable(GL_BLEND);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glApplyBuoyancy(FramebufferTexture velocity, FramebufferTexture temperature, FramebufferTexture density, FramebufferTexture dest)
{
	GLuint p = GLshaders.ApplyBuoyancy;
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
	GLint eyePosition = glGetUniformLocation(program, "eyePosition");
	GLint lightDirection = glGetUniformLocation(program, "lightDirection");
	glUniform3f(eyePosition, 0,0,-1);
	glUniform3f(lightDirection, 0.383f, 0.604f, -0.698f);
	glUniform2f(scale, 1.0f / width, 1.0f / height);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glDeleteProgram(program);
	glDeleteVertexArrays(1, &vertexArray);
}

void glDrawBorder()
{
	float T=0.9999f, vertices[] = { -T,-T,  T,-T,  T,T,  -T,T,  -T,-T };
	GLuint vertexArray = glCreateVertexArray(vertices,  sizeof(vertices));
	GLuint program = glCreateShaders("Fluid.Vertex", 0, "Fluid.Fill");
	glUseProgram(program);
	glDrawArrays(GL_LINE_STRIP, 0, 5);
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

	GLint inverseSize = glGetUniformLocation(p, "InverseSize");
	GLint timeStep = glGetUniformLocation(p, "TimeStep");
	GLint sourceTexture = glGetUniformLocation(p, "SourceTexture");

	glUniform2f(inverseSize, 1.0f /(ViewportWidth * 2), 1.0f / (ViewportHeight * 2));
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


void glAccelerateObstacles(FramebufferTexture position, FramebufferTexture source, FramebufferTexture dest)
{
	GLuint p = GLshaders.AccelerateObstacles;
	glUseProgram(p);

	GLint Kappa = glGetUniformLocation(p, "Kappa");
	GLint Gravity = glGetUniformLocation(p, "Gravity");
	GLint timeStep = glGetUniformLocation(p, "TimeStep");
	GLint sourceTexture = glGetUniformLocation(p, "SourceTexture");
	GLint inverseSize = glGetUniformLocation(p, "InverseSize");


	glUniform2f(inverseSize, 1.0f /(ViewportWidth * 2), 1.0f / (ViewportHeight * 2));
	glUniform1f(Gravity, GravityAcceleration);
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

	glUniform2f(inverseSize, 1.0f /(ViewportWidth * 2), 1.0f / (ViewportHeight * 2));
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
	w = ViewportWidth * 2; h = ViewportHeight * 2;
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
	glViewport(0, 0, ViewportWidth * 2, ViewportHeight * 2);
	glAdvectObstacles(ObstaclesVelocity.Ping, Obstacles.Ping, Obstacles.Pong);
	glSwapFramebufferTextures(&Obstacles);
	glAccelerateObstacles(ObstaclesPosition.Ping, ObstaclesVelocity.Ping, ObstaclesVelocity.Pong);
	glSwapFramebufferTextures(&ObstaclesVelocity);
	glMoveObstacles(ObstaclesVelocity.Ping, ObstaclesPosition.Ping, ObstaclesPosition.Pong);
	glSwapFramebufferTextures(&ObstaclesPosition);
	glViewport(0, 0, GridWidth, GridHeight);
	glAdvect(Velocity.Ping, Velocity.Ping, Obstacles.Ping, Velocity.Pong, VelocityDissipation);
	glSwapFramebufferTextures(&Velocity);
	glAdvect(Velocity.Ping, Temperature.Ping, Obstacles.Ping, Temperature.Pong, TemperatureDissipation);
	glSwapFramebufferTextures(&Temperature);
	glAdvect(Velocity.Ping, Density.Ping, Obstacles.Ping, Density.Pong, DensityDissipation);
	glSwapFramebufferTextures(&Density);
	glApplyBuoyancy(Velocity.Ping, Temperature.Ping, Density.Ping, Velocity.Pong);
	glSwapFramebufferTextures(&Velocity);
	if(time==0)glAddAir(Obstacles.Ping,1.0,0,0,0.5, Density.Ping);time++;/*
	glApplyImpulse(Temperature.Ping, ImpulsePosition, ImpulseTemperature);
	glApplyImpulse(Density.Ping, ImpulsePosition, ImpulseDensity);*/
	glComputeDivergence(Velocity.Ping, Obstacles.Ping, ObstaclesVelocity.Ping, Divergence);
	glClearFramebufferTexture(Pressure.Ping, 0);
	for (int i = 0; i < NumJacobiIterations; ++i) 
	{
		glJacobi(Pressure.Ping, Divergence, Obstacles.Ping, Pressure.Pong);
		glSwapFramebufferTextures(&Pressure);
	}
	glSubtractGradient(Velocity.Ping, Pressure.Ping, Obstacles.Ping, ObstaclesVelocity.Ping, Velocity.Pong);
	glSwapFramebufferTextures(&Velocity);
}

void glslRender(GLuint windowFbo)
{
	glUseProgram(VisualizeProgram);
	GLint fillColor = glGetUniformLocation(VisualizeProgram, "FillColor");
	GLint scale = glGetUniformLocation(VisualizeProgram, "Scale");
	GLint isFluid = glGetUniformLocation(VisualizeProgram, "isFluid");
	glEnable(GL_BLEND);
	glViewport(0, 0, ViewportWidth, ViewportHeight);
	glBindFramebuffer(GL_FRAMEBUFFER, windowFbo);
	glClearColor(0, 0, 0, 1);
	glClear(GL_COLOR_BUFFER_BIT);
	glBindTexture(GL_TEXTURE_2D, Density.Ping.texture);
	glUniform3f(fillColor, 1, 1, 1);
	glUniform1i(isFluid,true);
	glUniform2f(scale, 1.0f / ViewportWidth, 1.0f / ViewportHeight);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glBindTexture(GL_TEXTURE_2D, Obstacles.Ping.texture);
	glUniform1i(isFluid,false);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glDisable(GL_BLEND);
}

void glslHandleMouse(int x, int y, int action)
{
}
