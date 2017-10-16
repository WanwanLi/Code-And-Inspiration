#include "Fish.h"

typedef struct MeshRec
{
    GLuint Positions;
    GLuint Normals;
    GLuint Faces;
    GLsizei FaceCount;
    GLsizei VertexCount;
} Mesh;

typedef struct TextureRec
{
    GLuint Handle;
    GLsizei Width;
    GLsizei Height;
} Texture;


Point3 granny_path(float t)
{
    t = 2 * Pi * t;
    float x = -0.22 * cos(t) - 1.28 * sin(t) - 0.44 * cos(3 * t) - 0.78 * sin(3 * t);
    float y = -0.1 * cos(2 * t) - 0.27 * sin(2 * t) + 0.38 * cos(4 * t) + 0.46 * sin(4 * t);
    float z = 0.7 * cos(3 * t) - 0.4 * sin(3 * t);
    return P3MakeFromElems(x, y, z);
}

Vector3 V3Perp(Vector3 u)
{
    Vector3 v = V3MakeFromElems(0, 0, 1);
    Vector3 u_prime = V3Cross(u, v);
    if (V3LengthSqr(u_prime) < 0.01f) {
        v = V3MakeFromElems(0, 1, 0);
        u_prime = V3Cross(u, v);
        if (V3LengthSqr(u_prime) < 0.01f) {
            v = V3MakeFromElems(1, 0, 0);
            u_prime = V3Cross(u, v);
        }
    }
    return V3Normalize(u_prime);
}

void RedistributePath(const float* pSrc, float* pDest, float length, int width)
{
    int sourceCount = width;
    int destCount = width;
        
    float sourceCursor = 0;
    float destCursor = 0;
    float destSegment = length / (destCount - 1);
    int sourceIndex = 0;
        
    for (int i = 0; i < destCount; i++) {
        
        Point3 s0 = *((Point3*) (pSrc + sourceIndex * 3));
        Point3 s1 = *((Point3*) (pSrc + ((sourceIndex+1) % sourceCount) * 3));

        float nextDestCursor = destCursor + destSegment;
        float sourceSegment = P3Dist(s0, s1);
        float nextSourceCursor = sourceCursor + sourceSegment;
            
        while (nextSourceCursor < nextDestCursor && sourceIndex < sourceCount - 2) {
            sourceCursor = nextSourceCursor;
            sourceIndex = sourceIndex + 1;
                
            s0 = *((Point3*) (pSrc + sourceIndex * 3));
            s1 = *((Point3*) (pSrc + ((sourceIndex+1) % sourceCount) * 3));
            sourceSegment = P3Dist(s0, s1);
                
            nextSourceCursor = sourceCursor + sourceSegment;
        }
            
        float t = (sourceSegment - nextSourceCursor + nextDestCursor) / sourceSegment;
        Point3 d = P3Lerp(t, s0, s1);
        *pDest++ = d.x; *pDest++ = d.y; *pDest++ = d.z;
    
        destCursor = nextDestCursor;
        sourceCursor = nextSourceCursor - sourceSegment;
    }
}



// Computes a cylinder whose spine aligns with the X axis
// Y and Z diameters are 1.0
// The entire cylinder is centered at (+0.5, +0.5, +0.5).
Mesh CreateCylinder(float height, float radius, int stacks, int slices)
{
    // Compute a 2D circle for the sweep shape:
    float* circle = (float*) malloc(sizeof(float) * slices * 2);
    {
        float theta = 0;
        const float dtheta = 2 * Pi / (float) slices;
        float* pDest = circle;
        for (int slice = 0; slice < slices; ++slice, theta += dtheta) {
            *pDest++ = cos(theta);
            *pDest++ = sin(theta);
        }
    }

    // Compute the cylinder vertex normals:
    int vertCount = slices * stacks;
    float* tubeNormals = (float*) malloc(sizeof(float) * vertCount * 3);
    {
        float* pDest = tubeNormals;
        for (int stack = 0; stack < stacks; ++stack) {
            const float* pSrc = circle;
            for (int slice = 0; slice < slices; ++slice) {
                *pDest++ = 0;
                *pDest++ = *pSrc++;
                *pDest++ = *pSrc++;
            }
        }
    }
    
    // Scale the circle:
    {
        float* pDest = circle;
        for (int slice = 0; slice < slices; ++slice) {
            float x = *pDest; *pDest = radius * x; ++pDest;
            float y = *pDest; *pDest = radius * y; ++pDest;
        }
    }

    // Compute the cylinder vertex positions:
    float* tubeVerts = (float*) malloc(sizeof(float) * vertCount * 3);
    {
        float x = 0.5 - height / 2;
        float dx = height / (float) (stacks - 1);
        float* pDest = tubeVerts;
        for (int stack = 0; stack < stacks; ++stack, x += dx) {
            const float* pSrc = circle;
            for (int slice = 0; slice < slices; ++slice) {
                *pDest++ = x;
                *pDest++ = *pSrc++;
                *pDest++ = *pSrc++;
            }
        }
    }

    free(circle);
    
    // Compute triangle connectivity:
    int faceCount = slices * (stacks - 1) * 2;
    int* tubeFaces = (int*) malloc(sizeof(int) * faceCount * 3);
    {
        int* pDest = tubeFaces;
        for (int stack = 0; stack < stacks - 1; ++stack) {
            int n = stack * slices;
            for (int slice = 0; slice < slices; ++slice) {
                int a = slice;
                int b = slice + slices;
                int c = ((slice + 1) % slices) + slices;
                int d = ((slice + 1) % slices);
                *pDest++ = n+c;
                *pDest++ = n+b;
                *pDest++ = n+a;
                *pDest++ = n+a;
                *pDest++ = n+d;
                *pDest++ = n+c;
            }
        }
    }
    
    // Create OpenGL VBOs:

    Mesh m;
    m.FaceCount = faceCount;
    m.VertexCount = vertCount;
    
    glGenBuffers(1, &m.Positions);
    glBindBuffer(GL_ARRAY_BUFFER, m.Positions);
    glBufferData(GL_ARRAY_BUFFER, sizeof(float) * vertCount * 3, tubeVerts, GL_STATIC_DRAW);

    glGenBuffers(1, &m.Normals);
    glBindBuffer(GL_ARRAY_BUFFER, m.Normals);
    glBufferData(GL_ARRAY_BUFFER, sizeof(float) * vertCount * 3, tubeNormals, GL_STATIC_DRAW);

    glGenBuffers(1, &m.Faces);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.Faces);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(int) * faceCount * 3, tubeFaces, GL_STATIC_DRAW);

    free(tubeVerts);
    free(tubeNormals);
    free(tubeFaces);

    return m;
}

/*
Mesh CreateMesh(const char* ctmFile, float totalScale, float lengthScale)
{
    Mesh mesh = {0};
    
    char qualifiedPath[256] = {0};
    strcpy(qualifiedPath, glslResourcePath());
    strcat(qualifiedPath, "/\0");
    strcat(qualifiedPath, ctmFile);
    
    // Open the CTM file:
    CTMcontext ctmContext = ctmNewContext(CTM_IMPORT);
    ctmLoad(ctmContext, qualifiedPath);
    glslCheckCondition(ctmGetError(ctmContext) == CTM_NONE, "OpenCTM issue with loading %s", qualifiedPath);
    CTMuint vertexCount = ctmGetInteger(ctmContext, CTM_VERTEX_COUNT);
    CTMuint faceCount = ctmGetInteger(ctmContext, CTM_TRIANGLE_COUNT);

    // Create the VBO for positions:
    const CTMfloat* positions = ctmGetFloatArray(ctmContext, CTM_VERTICES);
    if (positions) {
    
        // Find bounding box
        float m = 99999.0f;
        Point3 minCorner = P3MakeFromElems(m, m, m);
        Point3 maxCorner = P3MakeFromElems(-m, -m, -m);
        const CTMfloat* pSrc = positions;
        CTMuint remainingVerts = vertexCount;
        while (remainingVerts--)
        {
            float x = *pSrc++;
            float y = *pSrc++;
            float z = *pSrc++;
            Point3 p = P3MakeFromElems(x, y, z);
            minCorner = P3MinPerElem(p, minCorner);
            maxCorner = P3MaxPerElem(p, maxCorner);
        }
		
        // Scale such that the Z extent is 'scale'
        // The X and Y scales are computed according to the aspect ratio.
        // The model is centered at (+0.5, +0.5, +0.5).
        float xratio = (maxCorner.x - minCorner.x) / (maxCorner.z - minCorner.z);
        float yratio = (maxCorner.y - minCorner.y) / (maxCorner.z - minCorner.z);

        float sx = lengthScale * totalScale * xratio / (maxCorner.x - minCorner.x);
        float sy = totalScale * yratio / (maxCorner.y - minCorner.y);
        float sz = totalScale / (maxCorner.z - minCorner.z);
        pSrc = positions;
        remainingVerts = vertexCount;
        CTMfloat* pDest = (CTMfloat*) positions;
        while (remainingVerts--)
        {
            float x = *pSrc++;
            float y = *pSrc++;
            float z = *pSrc++;
            *pDest++ = (x - minCorner.x) * sx - totalScale * xratio / 2;
            *pDest++ = (y - minCorner.y) * sy - totalScale * yratio / 2;
            *pDest++ = (z - minCorner.z) * sz - totalScale / 2;
        }

        GLuint handle;
        GLsizeiptr size = vertexCount * sizeof(float) * 3;
        glGenBuffers(1, &handle);
        glBindBuffer(GL_ARRAY_BUFFER, handle);
        glBufferData(GL_ARRAY_BUFFER, size, positions, GL_STATIC_DRAW);
        mesh.Positions = handle;
    }
    
    // Create the VBO for normals:
    const CTMfloat* normals = ctmGetFloatArray(ctmContext, CTM_NORMALS);
    if (normals) {
        GLuint handle;
        GLsizeiptr size = vertexCount * sizeof(float) * 3;
        glGenBuffers(1, &handle);
        glBindBuffer(GL_ARRAY_BUFFER, handle);
        glBufferData(GL_ARRAY_BUFFER, size, normals, GL_STATIC_DRAW);
        mesh.Normals = handle;
    }
    
    // Create the VBO for indices:
    const CTMuint* indices = ctmGetIntegerArray(ctmContext, CTM_INDICES);
    if (indices) {
        
        GLsizeiptr bufferSize = faceCount * 3 * sizeof(GLuint);
        
        GLuint handle;
        glGenBuffers(1, &handle);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, handle);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferSize, indices, GL_STATIC_DRAW);
        mesh.Faces = handle;
    }
    
    ctmFreeContext(ctmContext);

    mesh.FaceCount = faceCount;
    mesh.VertexCount = vertexCount;

    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    return mesh;
}

*/

Texture CreatePathTexture()
{
    GLuint handle;
    glGenTextures(1, &handle);
    glBindTexture(GL_TEXTURE_2D, handle);
    
    int width = 128;
    int height = 128;
    float* pData = (float*) malloc(sizeof(float) * 3 * width * height);
    float* pDest = pData;

    int row = 0;

    // Ellipse
    {
        float theta = 0;
        const float dtheta = 2 * Pi / (float) width;
        float* pTemp = (float*) malloc(width * 3 * sizeof(float));
        pDest = pTemp;
        float length = 0;

        // Path Centers:
        float px, py;
        for (int slice = 0; slice < width; ++slice, theta += dtheta) {
            float x = 2 * cos(theta);
            float y = 1 * sin(theta);
            *pDest++ = x;
            *pDest++ = 0;
            *pDest++ = y;
            if (slice > 1) {
                length += sqrt((x - px) * (x - px) + (y - py) * (y - py));
            }
            px = x; py = y;
        }

        // Restribute:
        pDest = pData;
        const float* pSrc = pTemp;
        RedistributePath(pSrc, pDest, length, width);
        pDest += width * 3;
        free(pTemp);
    
        // Path Normals:
        for (int slice = 0; slice < width; ++slice) {
            *pDest++ = 0;
            *pDest++ = 1;
            *pDest++ = 0;
        }
        row++;
    }
    
    // Upside-down Pacman Ghost
    {
        float dt = 1.0f / (float) (width - 1);
        float t = 0;
        float length = 0;
        float px, py;
        float* pTemp = (float*) malloc(sizeof(float) * 3 * width);
        float* pStart = pDest;
        pDest = pTemp;
        for (int slice = 0; slice < width; ++slice, t += dt) {
            float tt = (fmod(t, 1.0f) * 8.0f);
            int stage = (int) tt;
            float fraction = tt - (float) stage;
            float x, y;
            switch (stage) {
            case 0:
                x = -6 + fraction * 12.0f;
                y = -7;
                break;
            case 1:
                tt = (1 - fraction) * Pi * 0.5f;
                x = +6 + 6 * cos(tt);
                y = -1 - 6 * sin(tt);
                break;
            case 2:
                x = 12;
                y = -1 + 6 * fraction;
                break;
            case 3:
            case 4:
            case 5:
                fraction = ((stage - 3) + fraction) / 3.0f;
                x = 12 - 24 * fraction;
                y = 5 + 3 * sin(fraction * 4.9f * Pi);
                break;
            case 6:
                x = -12;
                y = 5 - 6 * fraction;
                break;
            case 7:
                tt = fraction * Pi * 0.5f;
                x = -6 - 6 * cos(tt);
                y = -1 - 6 * sin(tt);
                break;
            }
            x /= 4.0f; y /= 5.0;
            *pDest++ = x;
            *pDest++ = 0;
            *pDest++ = y;
            if (slice > 1) {
                length += sqrt((x - px) * (x - px) + (y - py) * (y - py));
            }
            px = x; py = y;
        }

        // Redistribute the path nodes:
        pDest = pStart;
        const float* pSrc = pTemp;
        RedistributePath(pSrc, pDest, length, width);
        free(pTemp);
        pDest += width * 3;
        
        // Orientation vectors:
        dt = 1.0f / (float) (width - 1);
        t = 0;
        for (int slice = 0; slice < width; ++slice, t += dt) {
            float tt = (fmod(t, 1.0f) * 8.0f);
            int stage = (int) tt;
            float fraction = tt - (float) stage;
            float x, y;
            switch (stage) {
            case 0:
                *pDest++ = 0;
                *pDest++ = cos(fraction * Pi);
                *pDest++ = sin(fraction * Pi);
                break;
            case 1: case 2: case 3:
            case 4: case 5: case 6:
                *pDest++ = 0;
                *pDest++ = -1;
                *pDest++ = 0;
                break;
            case 7:
                *pDest++ = 0;
                *pDest++ = cos(Pi + fraction * Pi);
                *pDest++ = sin(Pi + fraction * Pi);
                break;
            }
        }
        row++;
    }

    // Granny Knot
    for (; row < height / 2; row++)
    {
        float initial = (float) rand() / RAND_MAX;
        float scale = 1.0f + (float) rand() / RAND_MAX;

        float ax = -1.0f + 2.0f * (float) rand() / RAND_MAX;
        float ay = -1.0f + 2.0f * (float) rand() / RAND_MAX;
        float az = -1.0f + 2.0f * (float) rand() / RAND_MAX;
        Vector3 axis = V3Normalize(V3MakeFromElems(ax, ay, az));
        float theta = Pi * (float) rand() / RAND_MAX;
        Transform3 rotation = T3MakeRotationAxis(theta, axis);

        float t = initial;
        const float dt = 1.0f / (float) width;
        
        // Path Centers:
        for (int slice = 0; slice < width; ++slice, t += dt) {
            Point3 p = T3MulP3(rotation, granny_path(t));
            *pDest++ = p.x * scale;
            *pDest++ = p.y * scale;
            *pDest++ = p.z * scale;
        }
    
        // Path Normals:
        t = initial;
        for (int slice = 0; slice < width; ++slice, t += dt) {
            Point3 p0 = granny_path(t);
            Point3 p1 = granny_path(t + dt / 2);
            Vector3 a = V3Normalize(P3Sub(p1, p0));
            Vector3 b = V3Normalize(V3Perp(a));
            Vector3 n = T3MulV3(rotation, b);
            *pDest++ = n.x;
            *pDest++ = n.y;
            *pDest++ = n.z;
        }
    }
    
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGB, GL_FLOAT, pData);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    
    free(pData);
    
    Texture texture;
    texture.Handle = handle;
    texture.Width = width;
    texture.Height = height;
    return texture;
}

static GLuint BentProgram;
static Matrix4 ProjectionMatrix;
static Matrix4 ModelviewMatrix;
static Mesh SquidMesh;
static Mesh TunaMesh;
static Mesh DolphinMesh;
static Mesh CylinderMesh;
static Texture PathTexture;
static int ShowPaths = 1;
static float PathOffset;
static float PathScale;

void glslHandleMouse(int x, int y, int action)
{
    if (action == GLSL_UP) {
        ShowPaths = 1 - ShowPaths;
    }
}

static void LoadProgram(GLuint program)
{
    glUseProgram(program);

    GLint pathScale = glGetUniformLocation(program, "PathScale");
    glUniform1f(pathScale, PathScale);
    
    GLint pathOffset = glGetUniformLocation(program, "PathOffset");
    glUniform1f(pathOffset, PathOffset);

    GLint inverseWidth = glGetUniformLocation(program, "InverseWidth");
    glUniform1f(inverseWidth, 1.0f / PathTexture.Width);

    GLint inverseHeight = glGetUniformLocation(program, "InverseHeight");
    glUniform1f(inverseHeight, 1.0f / PathTexture.Height);

    GLint modelviewProjection = glGetUniformLocation(program, "ModelviewProjection");
    Matrix4 mvp = M4Mul(ProjectionMatrix, ModelviewMatrix);
    glUniformMatrix4fv(modelviewProjection, 1, 0, &mvp.col0.x);

    GLint normalMatrix = glGetUniformLocation(program, "NormalMatrix");
    Matrix3 nm = M3Transpose(M4GetUpper3x3(ModelviewMatrix));
    float packed[9] = {
        nm.col0.x, nm.col1.x, nm.col2.x,
        nm.col0.y, nm.col1.y, nm.col2.y,
        nm.col0.z, nm.col1.z, nm.col2.z };
    glUniformMatrix3fv(normalMatrix, 1, 0, &packed[0]);

    GLint projection = glGetUniformLocation(program, "Projection");
    glUniformMatrix4fv(projection, 1, 0, &ProjectionMatrix.col0.x);

    GLint size = glGetUniformLocation(program, "Size");
    glUniform2f(size, GLSL_VIEWPORT_WIDTH, GLSL_VIEWPORT_HEIGHT);

    GLint specularMaterial = glGetUniformLocation(program, "SpecularMaterial");
    glUniform3f(specularMaterial, 0.5f, 0.5f, 0.5f);

    GLint shininess = glGetUniformLocation(program, "Shininess");
    glUniform1f(shininess, 50);

    GLint ambientMaterial = glGetUniformLocation(program, "AmbientMaterial");
    glUniform3f(ambientMaterial, 0.04f, 0.04f, 0.04f);

    GLint lightPosition = glGetUniformLocation(program, "LightPosition");
    glUniform3f(lightPosition, 0.25, 0.25, 1);
}

static void BindMesh(Mesh m)
{
    GLuint programHandle;
    glGetIntegerv(GL_CURRENT_PROGRAM, (GLint*) &programHandle);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.Faces);

    glBindBuffer(GL_ARRAY_BUFFER, m.Positions);
    GLint positionSlot = glGetAttribLocation(programHandle, "Position");
    glVertexAttribPointer(positionSlot, 3, GL_FLOAT, GL_FALSE, sizeof(float) * 3, 0);
    glEnableVertexAttribArray(positionSlot);

    GLint normalSlot = glGetAttribLocation(programHandle, "Normal");
    if (normalSlot > -1) {
        glBindBuffer(GL_ARRAY_BUFFER, m.Normals);
        glVertexAttribPointer(normalSlot, 3, GL_FLOAT, GL_FALSE, sizeof(float) * 3, 0);
        glEnableVertexAttribArray(normalSlot);
    }
}

void glslRender(GLuint windowFbo)
{
    LoadProgram(BentProgram);

    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glBindTexture(GL_TEXTURE_2D, PathTexture.Handle);

    GLint instanceOffset = glGetUniformLocation(BentProgram, "InstanceOffset");
    GLint diffuseMaterial = glGetUniformLocation(BentProgram, "DiffuseMaterial");

    if (ShowPaths) {
        glUniform3f(diffuseMaterial, 255/255.0f, 167/255.0f, 178/255.0f);
        BindMesh(CylinderMesh);
        
        glUniform1i(instanceOffset, 2);
        glDrawElements(GL_TRIANGLES, CylinderMesh.FaceCount * 3, GL_UNSIGNED_INT, 0);

        glUniform1i(instanceOffset, 1);
        glDrawElements(GL_TRIANGLES, CylinderMesh.FaceCount * 3, GL_UNSIGNED_INT, 0);
    }

    glClearColor(143/255.0f, 188/255.0f, 204/255.0f, 1);

    glUniform3f(diffuseMaterial, 0.5f, 0.5f, 0.5f);
    glUniform1i(instanceOffset, 2);
    BindMesh(DolphinMesh);
    glDrawElementsInstanced(GL_TRIANGLES, DolphinMesh.FaceCount * 3, GL_UNSIGNED_INT, 0, 1);

    glUniform3f(diffuseMaterial, 255/255.0f, 250/255.0f, 179/255.0f);
    glUniform1i(instanceOffset, 1);
    BindMesh(SquidMesh);
    glDrawElementsInstanced(GL_TRIANGLES, SquidMesh.FaceCount * 3, GL_UNSIGNED_INT, 0, 1);

    /*glUniform3f(diffuseMaterial, 0.9, 0.9, 1.0);
    glUniform1i(instanceOffset, 2);
    BindMesh(TunaMesh);
    glDrawElementsInstanced(GL_TRIANGLES, TunaMesh.FaceCount * 3, GL_UNSIGNED_INT, 0, 126);*/
}

const char* glslInitialize(int width, int height)
{
    int slices = 8;
    int stacks = 128;
    float radius = 0.05f;
    CylinderMesh = CreateCylinder(20.0f, radius, stacks, slices);
    DolphinMesh = CreateCylinder(2.0f, radius*3, stacks, slices);
    SquidMesh = CreateCylinder(1.5f, radius*4, stacks, slices);
    TunaMesh = CreateCylinder(1.0f, radius, stacks, slices);
   // DolphinMesh = CreateMesh("Dolphin.ctm", 1, 1.25f);
  //  SquidMesh = CreateMesh("Squid.ctm", 1, 1);
  //  TunaMesh = CreateMesh("Tuna.ctm", 0.25, 0.25);
    PathTexture = CreatePathTexture();
    BentProgram = glCreateShaders("Fish.Vertex.Bent", 0, "Fish.Fragment");

    // Set up the projection matrix:
    const float HalfWidth = 0.75f;
    const float HalfHeight = HalfWidth * GLSL_VIEWPORT_HEIGHT / GLSL_VIEWPORT_WIDTH;
    ProjectionMatrix = M4MakeFrustum(-HalfWidth, +HalfWidth, -HalfHeight, +HalfHeight, 2, 15);

    // Initialize various GL state:
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_CULL_FACE);
    
    return "Fish Demo";
}

void glslUpdate(unsigned int elapsedMicroseconds)
{
    const float RadiansPerMicrosecond = 0.0000005f;
    static float Theta = 0.0f;
    //Theta += elapsedMicroseconds * RadiansPerMicrosecond;
    PathOffset += elapsedMicroseconds * 0.0000002f;
    PathScale = 0.05f;

    Vector3 offset = V3MakeFromElems(0, 0, 0);
    Matrix4 model = M4MakeRotationX(Theta);
    model = M4Mul(M4MakeTranslation(offset), model);
    model = M4Mul(model, M4MakeTranslation(V3Neg(offset)));
    Point3 eyePosition = P3MakeFromElems(0, 10, 0.1f);
    Point3 targetPosition = P3MakeFromElems(0, 0, 0.1f);
    Vector3 upVector = V3MakeFromElems(0, 0, 1);
    Matrix4 view = M4MakeLookAt(eyePosition, targetPosition, upVector);
    ModelviewMatrix = M4Mul(view, model);
}
