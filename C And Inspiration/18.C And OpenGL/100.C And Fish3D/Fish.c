#include "Fish.h"

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

// Computes a cylinder whose spine aligns with the X axis
// Y and Z diameters are 1.0
// The entire cylinder is centered at (+0.5, +0.5, +0.5).
Shape3D CreateCylinder(float height, float radius, int stacks, int slices)
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
    Shape3D m =newShape3D(tubeVerts, tubeNormals, tubeFaces, vertCount , faceCount );

    free(tubeVerts);
    free(tubeNormals);
    free(tubeFaces);

    return m;
}

Texture2D CreatePathTexture()
{
    int width = 128;
    int height = 128;
    float* pData = (float*) malloc(sizeof(float) * 3 * width * height);
    float* pDest = pData;

    
    // Granny Knot
    for (int row = 0; row < height / 2; row++)
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
    
    Texture2D texture2D=newTexture2D(pData, width, height);
    free(pData);
    return texture2D;
}

static GLuint BentProgram;
static Matrix4 ProjectionMatrix;
static Matrix4 ModelviewMatrix;
static Shape3D SquidShape3D;
static Shape3D TunaShape3D;
static Shape3D DolphinShape3D;
static Shape3D CylinderShape3D;
static Texture2D PathTexture;
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
    glUniform1f(inverseWidth, 1.0f / PathTexture.width);

    GLint inverseHeight = glGetUniformLocation(program, "InverseHeight");
    glUniform1f(inverseHeight, 1.0f / PathTexture.height);

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

static void BindShape3D(Shape3D m)
{
    GLuint programHandle;
    glGetIntegerv(GL_CURRENT_PROGRAM, (GLint*) &programHandle);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m.indices);

    glBindBuffer(GL_ARRAY_BUFFER, m.coordinates);
    GLint positionSlot = glGetAttribLocation(programHandle, "Position");
    glVertexAttribPointer(positionSlot, 3, GL_FLOAT, GL_FALSE, sizeof(float) * 3, 0);
    glEnableVertexAttribArray(positionSlot);

    GLint normalSlot = glGetAttribLocation(programHandle, "Normal");
    if (normalSlot > -1) {
        glBindBuffer(GL_ARRAY_BUFFER, m.normals);
        glVertexAttribPointer(normalSlot, 3, GL_FLOAT, GL_FALSE, sizeof(float) * 3, 0);
        glEnableVertexAttribArray(normalSlot);
    }
}

void glslRender(GLuint windowFbo)
{
    LoadProgram(BentProgram);

    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glBindTexture(GL_TEXTURE_2D, PathTexture.texture);

    GLint diffuseMaterial = glGetUniformLocation(BentProgram, "DiffuseMaterial");

    if (ShowPaths) {
        glUniform3f(diffuseMaterial, 255/255.0f, 167/255.0f, 178/255.0f);
        BindShape3D(CylinderShape3D);
        glDrawElements(GL_TRIANGLES, CylinderShape3D.indicesLength * 3, GL_UNSIGNED_INT, 0);

    }

    glClearColor(143/255.0f, 188/255.0f, 204/255.0f, 1);

    glUniform3f(diffuseMaterial, 0.5f, 0.5f, 0.5f);
    BindShape3D(DolphinShape3D);
    glDrawElements(GL_TRIANGLES, DolphinShape3D.indicesLength * 3, GL_UNSIGNED_INT, 0);

}

const char* glslInitialize(int width, int height)
{
    int slices = 8;
    int stacks = 128;
    float radius = 0.05f;
    CylinderShape3D = CreateCylinder(20.0f, radius, stacks, slices);
    DolphinShape3D = CreateCylinder(2.0f, radius*3, stacks, slices);
    SquidShape3D = CreateCylinder(1.5f, radius*4, stacks, slices);
    TunaShape3D = CreateCylinder(1.0f, radius, stacks, slices);
   // DolphinShape3D = CreateShape3D("Dolphin.ctm", 1, 1.25f);
  //  SquidShape3D = CreateShape3D("Squid.ctm", 1, 1);
  //  TunaShape3D = CreateShape3D("Tuna.ctm", 0.25, 0.25);
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
