// glsl was developed by Philip Rideout and released under the MIT License.

#include "glsl.h"
#include <sys/time.h>
#include <glxew.h>
#include <glew.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>
#include <signal.h>
#include <wchar.h>

typedef struct PlatformContextRec
{
    Display* MainDisplay;
    Window MainWindow;
} PlatformContext;

unsigned int GetMicroseconds()
{
    struct timeval tp;
    gettimeofday(&tp, NULL);
    return tp.tv_sec * 1000000 + tp.tv_usec;
}

int main(int argc, char** argv)
{
    int attrib[] = {
        GLX_RENDER_TYPE, GLX_RGBA_BIT,
        GLX_DRAWABLE_TYPE, GLX_WINDOW_BIT,
        GLX_DOUBLEBUFFER, True,
        GLX_RED_SIZE, 8,
        GLX_GREEN_SIZE, 8,
        GLX_BLUE_SIZE, 8,
        GLX_ALPHA_SIZE, 8,
        GLX_DEPTH_SIZE, 24,
#if GLSL_ENABLE_MULTISAMPLING
        GLX_SAMPLE_BUFFERS, 1,
        GLX_SAMPLES, 4,
#endif
        None
    };
    
    PlatformContext context;

    context.MainDisplay = XOpenDisplay(NULL);
    int screen = DefaultScreen(context.MainDisplay);
    Window root = RootWindow(context.MainDisplay, screen);

    int fbcount;
    PFNGLXCHOOSEFBCONFIGPROC glXChooseFBConfig = (PFNGLXCHOOSEFBCONFIGPROC)glXGetProcAddress((GLubyte*)"glXChooseFBConfig");
    GLXFBConfig *fbc = glXChooseFBConfig(context.MainDisplay, screen, attrib, &fbcount);
    if (!fbc)
        glslFatalError("Failed to retrieve a framebuffer config\n");;

    PFNGLXGETVISUALFROMFBCONFIGPROC glXGetVisualFromFBConfig = (PFNGLXGETVISUALFROMFBCONFIGPROC)glXGetProcAddress((GLubyte*)"glXGetVisualFromFBConfig");
    XVisualInfo *visinfo = glXGetVisualFromFBConfig(context.MainDisplay, fbc[0]);
    if (!visinfo)
        glslFatalError("Error: couldn't create OpenGL window with this pixel format.\n");

    XSetWindowAttributes attr;
    attr.background_pixel = 0;
    attr.border_pixel = 0;
    attr.colormap = XCreateColormap(context.MainDisplay, root, visinfo->visual, AllocNone);
    attr.event_mask = StructureNotifyMask | ExposureMask | KeyPressMask | KeyReleaseMask |
                      PointerMotionMask | ButtonPressMask | ButtonReleaseMask;

    context.MainWindow = XCreateWindow(
        context.MainDisplay,
        root,
        0, 0,
        GLSL_VIEWPORT_WIDTH, GLSL_VIEWPORT_HEIGHT, 0,
        visinfo->depth,
        InputOutput,
        visinfo->visual,
        CWBackPixel | CWBorderPixel | CWColormap | CWEventMask,
        &attr
    );
    XMapWindow(context.MainDisplay, context.MainWindow);

    GLXContext glcontext;
    if (GLSL_FORWARD_COMPATIBLE_GL) {
        GLXContext tempContext = glXCreateContext(context.MainDisplay, visinfo, NULL, True);
        PFNGLXCREATECONTEXTATTRIBSARBPROC glXCreateContextAttribs = (PFNGLXCREATECONTEXTATTRIBSARBPROC)glXGetProcAddress((GLubyte*)"glXCreateContextAttribsARB");
        if (!glXCreateContextAttribs) {
            glslFatalError("Your platform does not support OpenGL 4.0.\n"
                          "Try changing GLSL_FORWARD_COMPATIBLE_GL to 0.\n");
        }
        int fbcount = 0;
        GLXFBConfig *framebufferConfig = glXChooseFBConfig(context.MainDisplay, screen, 0, &fbcount);
        if (!framebufferConfig) {
            glslFatalError("Can't create a framebuffer for OpenGL 4.0.\n");
        } else {
            int attribs[] = {
                GLX_CONTEXT_MAJOR_VERSION_ARB, 4,
                GLX_CONTEXT_MINOR_VERSION_ARB, 0,
                GLX_CONTEXT_FLAGS_ARB, GLX_CONTEXT_FORWARD_COMPATIBLE_BIT_ARB,
                0
            }; 
            glcontext = glXCreateContextAttribs(context.MainDisplay, framebufferConfig[0], NULL, True, attribs);
            glXMakeCurrent(context.MainDisplay, 0, 0);
            glXDestroyContext(context.MainDisplay, tempContext);
        } 
    } else {
        glcontext = glXCreateContext(context.MainDisplay, visinfo, NULL, True);
    }

    glXMakeCurrent(context.MainDisplay, context.MainWindow, glcontext);
    
    GLenum err = glewInit();
    if (GLEW_OK != err)
        glslFatalError("GLEW Error: %s\n", glewGetErrorString(err));

    // Work around some GLEW issues:    
    #define glewGetProcAddress(name) (*glXGetProcAddressARB)(name)
    glPatchParameteri = (PFNGLPATCHPARAMETERIPROC)glewGetProcAddress((const GLubyte*)"glPatchParameteri");
    glBindVertexArray = (PFNGLBINDVERTEXARRAYPROC)glewGetProcAddress((const GLubyte*)"glBindVertexArray");
    glDeleteVertexArrays = (PFNGLDELETEVERTEXARRAYSPROC)glewGetProcAddress((const GLubyte*)"glDeleteVertexArrays");
    glGenVertexArrays = (PFNGLGENVERTEXARRAYSPROC)glewGetProcAddress((const GLubyte*)"glGenVertexArrays");
    glIsVertexArray = (PFNGLISVERTEXARRAYPROC)glewGetProcAddress((const GLubyte*)"glIsVertexArray");

    // Reset OpenGL error state:
    glGetError();

    glslDebugString("OpenGL Version: %s\n", glGetString(GL_VERSION));
    
    const char* windowTitle = glslInitialize(GLSL_VIEWPORT_WIDTH, GLSL_VIEWPORT_HEIGHT);
    XStoreName(context.MainDisplay, context.MainWindow, windowTitle);
    
    // -------------------
    // Start the Game Loop
    // -------------------

    unsigned int previousTime = GetMicroseconds();
    int done = 0;
    while (!done) {
        
        if (glGetError() != GL_NO_ERROR)
            glslFatalError("OpenGL error.\n");

        if (XPending(context.MainDisplay)) {
            XEvent event;
    
            XNextEvent(context.MainDisplay, &event);
            switch (event.type)
            {
                case Expose:
                    //redraw(display, event.xany.window);
                    break;
                
                case ConfigureNotify:
                    //resize(event.xconfigure.width, event.xconfigure.height);
                    break;
                
                case ButtonPress:
                    glslHandleMouse(event.xbutton.x, event.xbutton.y, GLSL_DOWN);
                    break;

                case ButtonRelease:
                    glslHandleMouse(event.xbutton.x, event.xbutton.y, GLSL_UP);
                    break;

                case MotionNotify:
                    glslHandleMouse(event.xmotion.x, event.xmotion.y, GLSL_MOVE);
                    break;

                case KeyRelease:
                case KeyPress: {
                    XComposeStatus composeStatus;
                    char asciiCode[32];
                    KeySym keySym;
                    int len;
                    
                    len = XLookupString(&event.xkey, asciiCode, sizeof(asciiCode), &keySym, &composeStatus);
                    switch (asciiCode[0]) {
                        case 'x': case 'X': case 'q': case 'Q':
                        case 0x1b:
                            done = 1;
                            break;
                    }
                }
            }
        }

        unsigned int currentTime = GetMicroseconds();
        unsigned int deltaTime = currentTime - previousTime;
        previousTime = currentTime;
        
        glslUpdate(deltaTime);

        glslRender(0);
        glXSwapBuffers(context.MainDisplay, context.MainWindow);
    }

    return 0;
}

void glslDebugStringW(const wchar_t* pStr, ...)
{
    va_list a;
    va_start(a, pStr);

    wchar_t msg[1024] = {0};
    vswprintf(msg, countof(msg), pStr, a);
    fputws(msg, stderr);
}

void glslDebugString(const char* pStr, ...)
{
    va_list a;
    va_start(a, pStr);

    char msg[1024] = {0};
    vsnprintf(msg, countof(msg), pStr, a);
    fputs(msg, stderr);
}

void glslFatalErrorW(const wchar_t* pStr, ...)
{
    fwide(stderr, 1);

    va_list a;
    va_start(a, pStr);

    wchar_t msg[1024] = {0};
    vswprintf(msg, countof(msg), pStr, a);
    fputws(msg, stderr);
    exit(1);
}

void _glslFatalError(const char* pStr, va_list a)
{
    char msg[1024] = {0};
    vsnprintf(msg, countof(msg), pStr, a);
    fputs(msg, stderr);
    exit(1);
}

void glslFatalError(const char* pStr, ...)
{
    va_list a;
    va_start(a, pStr);
    _glslFatalError(pStr, a);
}
void glslCheckCondition(int condition, ...)
{
    va_list a;
    const char* pStr;

    if (condition)
        return;

    va_start(a, condition);
    pStr = va_arg(a, const char*);
    _glslFatalError(pStr, a);
}

int glslIsPressing(char key)
{
    return 0;
}

const char* glslResourcePath()
{
    return "demo";
}

