#include "Fluid.h"
#include <math.h>

struct GLshader
{
	GLuint Fill;
	GLuint Jacobi;
	GLuint AddSmoke;
	GLuint AdvectFluid;
	GLuint AddDiffusion;
	GLuint SubtractGradient;
	GLuint ComputeDivergence;
	GLuint AdvectConcentration;
	GLuint AddExternalForce;
	GLuint AddObstaclesVelocity;
	GLuint MoveObstacles;
	GLuint AdvectObstacles;
	GLuint AccelerateObstacles;
	GLuint RayTracing;
	GLuint Visualize;
}
GLshaders;

void glInitShaders()
{
	GLshaders.Fill = glCreateShaders("Fluid.Vertex", 0, "Fluid.Fill");
	GLshaders.Jacobi = glCreateShaders("Fluid.Vertex", 0, "Fluid.Jacobi");
	GLshaders.AddSmoke = glCreateShaders("Fluid.Vertex", 0, "Fluid.AddSmoke");
	GLshaders.AdvectFluid = glCreateShaders("Fluid.Vertex", 0, "Fluid.AdvectFluid");
	GLshaders.AddDiffusion = glCreateShaders("Fluid.Vertex", 0, "Fluid.AddDiffusion");
	GLshaders.SubtractGradient = glCreateShaders("Fluid.Vertex", 0, "Fluid.SubtractGradient");
	GLshaders.ComputeDivergence = glCreateShaders("Fluid.Vertex", 0, "Fluid.ComputeDivergence");
	GLshaders.AdvectConcentration = glCreateShaders("Fluid.Vertex", 0, "Fluid.AdvectConcentration");
	GLshaders.AddExternalForce = glCreateShaders("Fluid.Vertex", 0, "Fluid.AddExternalForce");
	GLshaders.AddObstaclesVelocity = glCreateShaders("Fluid.Vertex", 0, "Fluid.AddObstaclesVelocity");
	GLshaders.MoveObstacles = glCreateShaders("Fluid.Vertex", 0, "Fluid.MoveObstacles");
	GLshaders.AdvectObstacles = glCreateShaders("Fluid.Vertex", 0, "Fluid.AdvectObstacles");
	GLshaders.AccelerateObstacles = glCreateShaders("Fluid.Vertex", 0, "Fluid.AccelerateObstacles");
	GLshaders.RayTracing = glCreateShaders("Fluid.Vertex", 0, "Fluid.RayTracing");
	GLshaders.Visualize = glCreateShaders("Fluid.Vertex", 0, "Fluid.Visualize");

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
#define DENSITY_MAP 2
#define VELOCITY_MAP 3
#define RAY_TRACING_MAP 4

static const float CellSize = 1.25f;
static const int ViewSize = 2, GridSize = 4;
static const int ObstaclePrecision = 1;
static const float ObstacleThreshold = 0.1f;
static const int ViewportWidth = (GLSL_VIEWPORT_WIDTH);
static const int ViewportHeight = (GLSL_VIEWPORT_HEIGHT);
static const int GridWidth = (ViewportWidth / GridSize);
static const int GridHeight = (ViewportHeight / GridSize);
static const float SplatRadius = (float)(GridWidth / 20.0f);
static const int renderMode = RAY_TRACING_MAP ;

static const float AmbientTemperature = 300.0f;
static const float ObstacleTemperature = 1000.0f;
static const float CenterTemperature = 5800.0f;
static const float AmbientConcentration = 0.80f;
static const float CenterConcentration = 0.8f;

static const float VelocityWidth = 0.00125f;
static const float GridNum = 120;
static const int JacobiIterationTime = 40;
static const float TimeStep = 0.08f;
static const float SmokeBuoyancy = 0.005f;
static const float SmokeDensity = 0.05f;
static const float AirDensity = 0.001f;
static const float MassLoss = 0.01f;
static const float DiffusionScale = 1.2f;
static const float GradientScale = 1.125f / CellSize;
static const float TemperatureDissipation = 0.99f;
static const float VelocityDissipation = 0.99f;
static const float ElasticCoefficient = 1.5f;
static const float GravityAcceleration = 0;
static const vec2 CenterPosition = { GridWidth / 2,  SplatRadius * 2 };
static const vec2 InitObstacleVelocity = {0, -150 };
static const vec2 ZeroVector2 = { 0, 0 };
static const vec3 FluidColor = { 1, 1, 1 };

static int time = 0;
static GLuint QuadVertexArray;
static FramebufferTexture Background, Divergence;
static DataFramebuffer Obstacles, ObstaclesVelocity, ObstaclesPosition;
static DataFramebuffer Velocity, Concentration, Pressure, Temperature;


void glAdvectFluid(FramebufferTexture velocity, FramebufferTexture source, FramebufferTexture dest, float dissipation)
{
	glUseShader(GLshaders.AdvectFluid);
	glUniform2F("InverseSize", 1.0f / GridWidth, 1.0f / GridHeight);
	glUniform1F("TimeStep", TimeStep);
	glUniform1F("Dissipation", dissipation);
	glUniform1I("SourceTexture", 1);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, velocity.texture);
	glBindActiveTexture(GL_TEXTURE1, source.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}


void glJacobi(FramebufferTexture pressure, FramebufferTexture divergence, FramebufferTexture dest)
{
	glUseShader(GLshaders.Jacobi);
	glUniform1F("Alpha", -CellSize * CellSize);
	glUniform1F("InverseBeta", 0.25f);
	glUniform1I("Divergence", 1);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, pressure.texture);
	glBindActiveTexture(GL_TEXTURE1, divergence.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glAdvectConcentration(FramebufferTexture velocity, FramebufferTexture source, FramebufferTexture obstacles, FramebufferTexture obstVelocity, FramebufferTexture dest)
{
	glUseShader(GLshaders.AdvectConcentration);
	glUniform2F("InverseSize", 1.0f / GridWidth, 1.0f / GridHeight);
	glUniform1F("TimeStep", TimeStep);
	glUniform1F("MassLoss", MassLoss);
	glUniform1F("VelocityScale", 1.0f / (ViewSize * GridSize));
	glUniform1I("Concentration", 1);
	glUniform1I("Obstacles", 2);
	glUniform1I("ObstVelocity", 3);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, velocity.texture);
	glBindActiveTexture(GL_TEXTURE1, source.texture);
	glBindActiveTexture(GL_TEXTURE2, obstacles.texture);
	glBindActiveTexture(GL_TEXTURE3, obstVelocity.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glAddDiffusion(FramebufferTexture source, FramebufferTexture obstacles, float obstacleValue, FramebufferTexture dest)
{
	glUseShader(GLshaders.AddDiffusion);
	glUniform1I("Obstacles", 1);
	glUniform1F("TimeStep", TimeStep);
	glUniform1F("ObstacleValue", obstacleValue);
	glUniform1F("Precision", ObstaclePrecision);
	glUniform1F("CellSquare", CellSize*CellSize);
	glUniform1F("DiffusionScale", DiffusionScale);
	glUniform2F("InverseSize", 1.0f / GridWidth, 1.0f / GridHeight);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, source.texture);
	glBindActiveTexture(GL_TEXTURE1, obstacles.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}


void glAddObstaclesVelocity(FramebufferTexture velocity, FramebufferTexture obstacles, FramebufferTexture obstVelocity, FramebufferTexture dest)
{
	glUseShader(GLshaders.AddObstaclesVelocity);
	glUniform1I("Obstacles", 1);
	glUniform1I("ObstVelocity", 2);
	glUniform1F("VelocityScale", 1.0f / (ViewSize * GridSize));
	glUniform2F("InverseSize", 1.0f / GridWidth, 1.0f / GridHeight);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, velocity.texture);
	glBindActiveTexture(GL_TEXTURE1, obstacles.texture);
	glBindActiveTexture(GL_TEXTURE2, obstVelocity.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}


void glSubtractGradient(FramebufferTexture velocity, FramebufferTexture pressure, FramebufferTexture dest)
{
	glUseShader(GLshaders.SubtractGradient);
	glUniform1F("GradientScale", GradientScale);
	glUniform1I("Pressure", 1);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, velocity.texture);
	glBindActiveTexture(GL_TEXTURE1, pressure.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glComputeDivergence(FramebufferTexture velocity, FramebufferTexture dest)
{
	glUseShader(GLshaders.ComputeDivergence);
	glUniform1F("VelocityScale", 1.0f / (ViewSize * GridSize));
	glUniform1F("HalfInverseCellSize", 0.5f / CellSize);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, velocity.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glAddSmoke(FramebufferTexture dest, vec2 position, float value)
{
	glUseShader(GLshaders.AddSmoke);
	glUniform2F("Scale", 0.5f, 1.0f);
	glUniform2F("Center", position.X, position.Y);
	glUniform1F("Radius", SplatRadius);
	glUniform3F("FillColor", value, value, value);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glEnable(GL_BLEND);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glAddExternalForce(FramebufferTexture velocity, FramebufferTexture temperature, FramebufferTexture concentration, FramebufferTexture dest)
{
	glUseShader(GLshaders.AddExternalForce);
	glUniform1I("Temperature", 1);
	glUniform1I("Concentration", 2);
	glUniform1F("AmbientTemperature", AmbientTemperature);
	glUniform1F("SmokeDensity", SmokeDensity);
	glUniform1F("AirDensity", AirDensity);
	glUniform1F("TimeStep", TimeStep);
	glUniform1F("Buoyancy", SmokeBuoyancy);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, velocity.texture);
	glBindActiveTexture(GL_TEXTURE1, temperature.texture);
	glBindActiveTexture(GL_TEXTURE2, concentration.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

void glDrawObstacles(int width, int height)
{
	float T = 1, vertices[] = { -T,-T,  T,-T,  -T,T,  T,T };
	GLuint vertexArray = glCreateVertexArray(vertices, sizeof(vertices));
	glUseShader(GLshaders.RayTracing);
	glUniform1I("isObstacleBox", 0);
	glUniform3F("eyePosition", 0, 0, -1);
	glUniform3F("lightDirection", 0.383f, 0.604f, -0.698f);
	glUniform2F("Scale", 1.0f / width, 1.0f / height);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glDeleteVertexArrays(1, &vertexArray);
}

void glDrawQuad(float T, float red, float green, float blue)
{
	float vertices[] = { -T,-T,  T,-T,  -T,T,  T,T };
	GLuint vertexArray = glCreateVertexArray(vertices, sizeof(vertices));
	glUseShader(GLshaders.Fill);
	glUniform3F("FillColor", red, green, blue);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glDeleteVertexArrays(1, &vertexArray);
}

void glCreateBackground(FramebufferTexture dest, int width, int height)
{
	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glViewport(0, 0, width, height);
	glClearColor(0, 0, 0, 0);
	glClear(GL_COLOR_BUFFER_BIT);
	float T = 1, vertices[] = { -2 * T,-T,  2 * T,-T,  -2 * T,T,  2 * T,T };
	GLuint vertexArray = glCreateVertexArray(vertices, sizeof(vertices));
	GLuint shader = glCreateShaders("Fluid.Vertex", 0, "Fluid.RayTracing");
	glUseShader(shader);
	glUniform3F("eyePosition", 0, 0, -1);
	glUniform3F("lightPosition", 0, 1.2f, 1.0f);
	glUniform2F("Scale", 1.0f / width, 1.0f / height);
	glUniform1I("isBackground", 1);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glDeleteVertexArrays(1, &vertexArray);
	glDeleteProgram(shader);
}

void glCreateObstacles(FramebufferTexture dest, int width, int height, int isVelocity, vec2 velocity)
{
	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glEnable(GL_BLEND);
	glViewport(0, 0, width, height);
	glClearColor(0, 0, 0, 0);
	glClear(GL_COLOR_BUFFER_BIT);
	if (isVelocity == 0)glDrawObstacles(width, height);
	else glDrawQuad(0.95f, velocity.X, velocity.Y, 0);
	glDisable(GL_BLEND);
}

void glAdvectObstacles(FramebufferTexture velocity, FramebufferTexture source, FramebufferTexture dest)
{
	glUseShader(GLshaders.AdvectObstacles);
	glUniform2F("InverseSize", 1.0f / (ViewportWidth * ViewSize), 1.0f / (ViewportHeight * ViewSize));
	glUniform1F("TimeStep", TimeStep);
	glUniform1F("ObstacleThreshold", ObstacleThreshold);
	glUniform1I("SourceTexture", 1);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, velocity.texture);
	glBindActiveTexture(GL_TEXTURE1, source.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}


void glAccelerateObstacles(FramebufferTexture position, FramebufferTexture source, FramebufferTexture dest)
{
	glUseShader(GLshaders.AccelerateObstacles);
	glUniform2F("InverseSize", 1.0f / (ViewportWidth * ViewSize), 1.0f / (ViewportHeight * ViewSize));
	glUniform1F("Gravity", GravityAcceleration);
	glUniform1F("Kappa", ElasticCoefficient);
	glUniform1F("TimeStep", TimeStep);
	glUniform1I("SourceTexture", 1);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, position.texture);
	glBindActiveTexture(GL_TEXTURE1, source.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}


void glMoveObstacles(FramebufferTexture velocity, FramebufferTexture source, FramebufferTexture dest)
{
	glUseShader(GLshaders.MoveObstacles);
	glUniform2F("InverseSize", 1.0f / (ViewportWidth * ViewSize), 1.0f / (ViewportHeight * ViewSize));
	glUniform1F("TimeStep", TimeStep);
	glUniform1I("SourceTexture", 1);

	glBindFramebuffer(GL_FRAMEBUFFER, dest.framebuffer);
	glBindActiveTexture(GL_TEXTURE0, velocity.texture);
	glBindActiveTexture(GL_TEXTURE1, source.texture);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glResetState();
}

const char* glslInitialize(int width, int height)
{
	int w = GridWidth, h = GridHeight;
	Velocity = glCreateDataFramebuffer(w, h, 2);
	Concentration = glCreateDataFramebuffer(w, h, 1);
	Pressure = glCreateDataFramebuffer(w, h, 1);
	Temperature = glCreateDataFramebuffer(w, h, 1);
	Divergence = glCreateFramebufferTexture(w, h, 3);
	glInitShaders();
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	w = ViewportWidth * ViewSize; h = ViewportHeight * ViewSize;
	Background = glCreateFramebufferTexture(w, h, 4);
	glCreateBackground(Background, w, h);
	Obstacles = glCreateDataFramebuffer(w, h, 4);
	glCreateObstacles(Obstacles.Ping, w, h, 0, ZeroVector2);
	ObstaclesPosition = glCreateDataFramebuffer(w, h, 4);
	glCreateObstacles(ObstaclesPosition.Ping, w, h, 1, ZeroVector2);
	ObstaclesVelocity = glCreateDataFramebuffer(w, h, 4);
	glCreateObstacles(ObstaclesVelocity.Ping, w, h, 1, InitObstacleVelocity);
	float T = 1, vertices[] = { -T,-T,  T,-T,  -T,T,  T,T };
	QuadVertexArray = glCreateVertexArray(vertices, sizeof(vertices));
	glClearFramebufferTexture(Temperature.Ping, AmbientTemperature);
	return "C And Fluid2D";
}


void glslUpdate(unsigned int elapsedMicroseconds)
{
	glViewport(0, 0, ViewportWidth * ViewSize, ViewportHeight * ViewSize);
	glMoveObstacles(ObstaclesVelocity.Ping, ObstaclesPosition.Ping, ObstaclesPosition.Pong);
	glSwapFramebufferTextures(&ObstaclesPosition);
	glAccelerateObstacles(ObstaclesPosition.Ping, ObstaclesVelocity.Ping, ObstaclesVelocity.Pong);
	glSwapFramebufferTextures(&ObstaclesVelocity);
	glAdvectObstacles(ObstaclesVelocity.Ping, Obstacles.Ping, Obstacles.Pong);
	glSwapFramebufferTextures(&Obstacles);

	glViewport(0, 0, GridWidth, GridHeight);
	glAddSmoke(Concentration.Ping, CenterPosition, CenterConcentration);
	glAddSmoke(Temperature.Ping, CenterPosition, CenterTemperature);
	glAddExternalForce(Velocity.Ping, Temperature.Ping, Concentration.Ping, Velocity.Pong);
	glSwapFramebufferTextures(&Velocity);
	glAddObstaclesVelocity(Velocity.Ping, Obstacles.Ping, ObstaclesVelocity.Ping, Velocity.Pong);
	glSwapFramebufferTextures(&Velocity);
	glAdvectFluid(Velocity.Ping, Velocity.Ping, Velocity.Pong, VelocityDissipation);
	glSwapFramebufferTextures(&Velocity);
	glComputeDivergence(Velocity.Ping, Divergence);
	glClearFramebufferTexture(Pressure.Ping, 0);
	for (int i = 0; i < JacobiIterationTime; i++)
	{
		glJacobi(Pressure.Ping, Divergence, Pressure.Pong);
		glSwapFramebufferTextures(&Pressure);
	}
	glSubtractGradient(Velocity.Ping, Pressure.Ping, Velocity.Pong);
	glSwapFramebufferTextures(&Velocity);
	glAddDiffusion(Temperature.Ping, Obstacles.Ping, ObstacleTemperature, Temperature.Pong);
	glSwapFramebufferTextures(&Temperature);
	glAdvectConcentration(Velocity.Ping, Concentration.Ping, Obstacles.Ping, ObstaclesVelocity.Ping, Concentration.Pong);
	glSwapFramebufferTextures(&Concentration);
	glAddDiffusion(Concentration.Ping, Obstacles.Ping, 0, Concentration.Pong);
	glSwapFramebufferTextures(&Concentration);
	glAdvectFluid(Velocity.Ping, Temperature.Ping, Temperature.Pong, TemperatureDissipation);
	glSwapFramebufferTextures(&Temperature);


}

void glslRender(GLuint windowFbo)
{
	glUseShader(GLshaders.Visualize);
	glEnable(GL_BLEND);
	glViewport(0, 0, ViewportWidth, ViewportHeight);
	glBindFramebuffer(GL_FRAMEBUFFER, windowFbo);
	glClearColor(0, 0, 0, 1);
	glClear(GL_COLOR_BUFFER_BIT);
	glUniform3F("FillColor", 1, 1, 1);
	glUniform1I("dSampler", 1);
	switch (renderMode)
	{
		case HEAT_MAP:
		glUniform1I( "isHeat", true);
		glUniform1F("minValue", AmbientTemperature);
		glUniform1F("maxValue", CenterTemperature);
		glBindTexture(GL_TEXTURE_2D, Temperature.Ping.texture); break;
		case DENSITY_MAP:
		glUniform1I("isDensity", true);
		glUniform1F("minValue", AirDensity);
		glUniform1F("maxValue", SmokeDensity);
		glBindTexture(GL_TEXTURE_2D, Concentration.Ping.texture); break;
		case VELOCITY_MAP:
		glUniform1I("isVelocity", true);
		glUniform1F("velocityWidth", VelocityWidth);
		glUniform2F("velocityGridSize", 1.0 / GridNum, 1.0 / GridNum);
		glBindTexture(GL_TEXTURE_2D, Velocity.Ping.texture);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, Concentration.Ping.texture);
		glActiveTexture(GL_TEXTURE0); break;
		case RAY_TRACING_MAP:
		glBindTexture(GL_TEXTURE_2D, Background.texture);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		glUniform1I("isFluid", true);
		glBindTexture(GL_TEXTURE_2D, Concentration.Ping.texture); break;
	}
	glUniform2F("Scale", 1.0f / ViewportWidth, 1.0f / ViewportHeight);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glBindTexture(GL_TEXTURE_2D, Obstacles.Ping.texture);
	glUniform1I("isFluid", false);
	glUniform1I( "isHeat", false);
	glUniform1I("isDensity", false);
	glUniform1I("isVelocity", false);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
	glDisable(GL_BLEND);
}

void glslHandleMouse(int x, int y, int action)
{
}
