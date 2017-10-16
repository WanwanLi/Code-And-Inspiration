#pragma once

#ifdef __cplusplus
extern "C" {
#endif

#include <glew.h>

#define TwoPi (6.28318531f)
#define Pi (3.14159265f)
#define countof(A) (sizeof(A) / sizeof(A[0]))

// Implemented by the demo:
const char* glslInitialize(int width, int height); // receive window size and return window title
void glslRender(unsigned int fbo);                 // draw scene (glsl swaps the backbuffer for you)
void glslUpdate(unsigned int microseconds);        // receive elapsed time (e.g., update physics)
void glslHandleMouse(int x, int y, int action);    // handle mouse action specified by bitfield

enum MouseFlag
{
    GLSL_DOWN  = 1 << 0,
    GLSL_UP    = 1 << 1,
    GLSL_MOVE  = 1 << 2,
    GLSL_LEFT  = 1 << 3,
    GLSL_RIGHT = 1 << 4,
};

// Implemented by the platform layer:
const char* glslResourcePath();
void glslDebugString(const char* pStr, ...);
void glslDebugStringW(const wchar_t* pStr, ...);
void glslFatalError(const char* pStr, ...);
void glslFatalErrorW(const wchar_t* pStr, ...);
void glslCheckCondition(int condition, ...);
void glslCheckConditionW(int condition, ...);
int glslIsPressing(char key);

// Configuration:
#define GLSL_VIEWPORT_WIDTH (680)
#define GLSL_VIEWPORT_HEIGHT (650)
#define GLSL_ENABLE_MULTISAMPLING 0
#define GLSL_VERTICAL_SYNC 0
#define GLSL_FORWARD_COMPATIBLE_GL 0

#ifdef __cplusplus
}
#endif
