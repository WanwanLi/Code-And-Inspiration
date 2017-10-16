#include "Fluid.h"
#include <math.h>

void CreateObstacles(Surface dest, int width, int height)
{


    glBindFramebuffer(GL_FRAMEBUFFER, dest.FboHandle);
    glEnable(GL_BLEND);
    glViewport(0, 0, width, height);
    glClearColor(0, 0, 0, 0);
    glClear(GL_COLOR_BUFFER_BIT);

    const int DrawObstacle = 1;
    if (DrawObstacle) {
    GLuint vao=CreateQuad();
    GLuint program = CreateProgram("Fluid.Vertex", 0, "Fluid.RayTracing");
    glUseProgram(program);
    GLint scale = glGetUniformLocation(program, "Scale");
    GLint eyePosition = glGetUniformLocation(program, "eyePosition");
    GLint lightDirection = glGetUniformLocation(program, "lightDirection");

    glUniform3f(eyePosition , 0,0,-1);
    glUniform3f(lightDirection , 0.383f,0.604f,-0.698f);
    glUniform2f(scale, 1.0f / width, 1.0f / height);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    // Cleanup
    glDeleteProgram(program);
    glDeleteVertexArrays(1, &vao);
}


    const int DrawBorder = 1;
    if (DrawBorder) {
    GLuint vao;
    glGenVertexArrays(1, &vao);
    glBindVertexArray(vao);
    GLuint program = CreateProgram("Fluid.Vertex", 0, "Fluid.Fill");
    glUseProgram(program);
        #define T 0.9999f
        float positions[] = { -T, -T, T, -T, T,  T, -T,  T, -T, -T };
        #undef T
        GLuint vbo;
        GLsizeiptr size = sizeof(positions);
        glGenBuffers(1, &vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, size, positions, GL_STATIC_DRAW);
        GLsizeiptr stride = 2 * sizeof(positions[0]);
        glEnableVertexAttribArray(PositionSlot);
        glVertexAttribPointer(PositionSlot, 2, GL_FLOAT, GL_FALSE, stride, 0);
        glDrawArrays(GL_LINE_STRIP, 0, 5);
        glDeleteBuffers(1, &vbo);
    // Cleanup
    glDeleteProgram(program);
    glDeleteVertexArrays(1, &vao);
    }

    const int DrawCircle = 0;
    if (DrawCircle) {
        const int slices = 64;
        float positions[slices*2*3];
        float twopi = 8*atan(1.0f);
        float theta = 0;
        float dtheta = twopi / (float) (slices - 1);
        float* pPositions = &positions[0];
        for (int i = 0; i < slices; i++) {
            *pPositions++ = 0;
            *pPositions++ = 0;

            *pPositions++ = 0.25f * cos(theta) * height / width;
            *pPositions++ = 0.25f * sin(theta);
            theta += dtheta;

            *pPositions++ = 0.25f * cos(theta) * height / width;
            *pPositions++ = 0.25f * sin(theta);
        }

        GLuint vbo;
        GLsizeiptr size = sizeof(positions);
        glGenBuffers(1, &vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, size, positions, GL_STATIC_DRAW);
        GLsizeiptr stride = 2 * sizeof(positions[0]);
        glEnableVertexAttribArray(PositionSlot);
        glVertexAttribPointer(PositionSlot, 2, GL_FLOAT, GL_FALSE, stride, 0);
        glDrawArrays(GL_TRIANGLES, 0, slices * 3);
        glDeleteBuffers(1, &vbo);
    }
    glDisable(GL_BLEND);
}
