#include <stdlib.h>
#include <stdio.h>
#include "glutlib.h"
#define GeometryArray struct GeometryArray
GeometryArray
{
	GeometryInfo* first;
	GeometryInfo* last;
	int length;
};
Public GeometryArray* getGeometryArray(GeometryInfo* geometryInfo)
{
	GeometryArray* geometryArray=(GeometryArray*)malloc(sizeof(GeometryArray));
	geometryArray->first=geometryInfo;
	geometryArray->last=geometryInfo;
	geometryArray->length=1;
	return geometryArray;
}
Public void addGeometry(GeometryArray* geometryArray,GeometryInfo* geometryInfo)
{
	geometryArray->last->next=geometryInfo;
	geometryArray->last=geometryInfo;
	geometryArray->length++;
}
Public void generateNormals(GeometryInfo* geometryInfo)
{
	switch(geometryInfo->geometryArrayType)
	{
		case TRIANGLE_ARRAY:generateTriangleArrayNormals(geometryInfo);break;
		case QUAD_ARRAY:generateQuadArrayNormals(geometryInfo);break;
		case TRIANGLE_STRIP_ARRAY:generateGeometryStripArrayNormals(geometryInfo);break;
		case QUAD_STRIP_ARRAY:generateGeometryStripArrayNormals(geometryInfo);break;
	}
}
#define PolygonAttributes struct PolygonAttributes
#define POLYGON_POINT 1
#define POLYGON_LINE 2
#define POLYGON_FILL 3
#define CULL_FRONT 4
#define CULL_BACK 5
#define CULL_NONE 6
PolygonAttributes
{
	int polygonMode;
	int cullFace;
};
PolygonAttributes* newPolygonAttributes()
{
	PolygonAttributes* polygonAttributes=(PolygonAttributes*)malloc(sizeof(PolygonAttributes));
	polygonAttributes->polygonMode=POLYGON_FILL;
	polygonAttributes->cullFace=CULL_BACK ;
	return polygonAttributes;
}
Public void setPolygonMode(PolygonAttributes* polygonAttributes,int polygonMode)
{
	polygonAttributes->polygonMode=polygonMode;
}
Public void setCullFace(PolygonAttributes* polygonAttributes,int cullFace)
{
	polygonAttributes->cullFace=cullFace;
}
#define PointAttributes struct PointAttributes
PointAttributes
{
	float pointSize;
	int pointAntialiasingEnable;
};
Public PointAttributes* newPointAttributes()
{
	PointAttributes* pointAttributes=(PointAttributes*)malloc(sizeof(PointAttributes));
	pointAttributes->pointSize=1.0f;
	pointAttributes->pointAntialiasingEnable=false;
	return pointAttributes;
}
Public void setPointSize(PointAttributes* pointAttributes,float pointSize)
{
	pointAttributes->pointSize=pointSize;
}
Public void setPointAntialiasingEnable(PointAttributes* pointAttributes,int enable)
{
	pointAttributes->pointAntialiasingEnable=enable;
}
#define LineAttributes struct LineAttributes
#define PATTERN_SOLID 6
#define PATTERN_DASH 7
#define PATTERN_DOT 8
#define PATTERN_DASH_DOT 9
LineAttributes
{
	float lineWidth;
	int linePattern;
	int lineAntialiasingEnable;
};
Public LineAttributes* newLineAttributes()
{
	LineAttributes* lineAttributes=(LineAttributes*)malloc(sizeof(LineAttributes));
	lineAttributes->lineWidth=1.0f;
	lineAttributes->linePattern=PATTERN_SOLID;
	lineAttributes->lineAntialiasingEnable=false;
	return lineAttributes;
}
Public void setLineWidth(LineAttributes* lineAttributes,float lineWidth)
{
	lineAttributes->lineWidth=lineWidth;
}
Public void setLinePattern(LineAttributes* lineAttributes,int linePattern)
{
	lineAttributes->linePattern=linePattern;
}
Public void setLineAntialiasingEnable(LineAttributes* lineAttributes,int enable)
{
	lineAttributes->lineAntialiasingEnable=enable;
}
#define BLENDED 1
#define TransparencyAttributes struct TransparencyAttributes
TransparencyAttributes
{
	int blendMode;
	float alpha;
};
Public TransparencyAttributes* newTransparencyAttributes(int blendMode,float transparency)
{
	TransparencyAttributes* transparencyAttributes=(TransparencyAttributes*)malloc(sizeof(TransparencyAttributes));
	transparencyAttributes->blendMode=blendMode;
	transparencyAttributes->alpha=1.0f-transparency;
	return transparencyAttributes;
}
Public void setTransparency(TransparencyAttributes* transparencyAttributes,float transparency)
{
	transparencyAttributes->alpha=1.0f-transparency;
}
#define Material struct Material
Material
{
	float diffuseColor[4];
	float ambientColor[4];
	float emissiveColor[4];
	float specularColor[4];
	float shininess;
};
Public Material* newMaterial()
{
	Material* material=(Material*)malloc(sizeof(Material));
	material->diffuseColor[0]=1;
	material->diffuseColor[1]=1;
	material->diffuseColor[2]=1;
	material->diffuseColor[3]=1;
	material->ambientColor[0]=0;
	material->ambientColor[1]=0;
	material->ambientColor[2]=0;
	material->ambientColor[3]=1;
	material->emissiveColor[0]=0;
	material->emissiveColor[1]=0;
	material->emissiveColor[2]=0;
	material->emissiveColor[3]=1;
	material->specularColor[0]=0;
	material->specularColor[1]=0;
	material->specularColor[2]=0;
	material->specularColor[3]=1;
	material->shininess=50.0f;
	return material;
}
Public void setDiffuseColor(Material* material,Color3f* color)
{
	material->diffuseColor[0]=color->red;
	material->diffuseColor[1]=color->green;
	material->diffuseColor[2]=color->blue;
}
Public void setAmbientColor(Material* material,Color3f* color)
{
	material->ambientColor[0]=color->red;
	material->ambientColor[1]=color->green;
	material->ambientColor[2]=color->blue;
}
Public void setEmissiveColor(Material* material,Color3f* color)
{
	material->emissiveColor[0]=color->red;
	material->emissiveColor[1]=color->green;
	material->emissiveColor[2]=color->blue;
}
Public void setSpecularColor(Material* material,Color3f* color)
{
	material->specularColor[0]=color->red;
	material->specularColor[1]=color->green;
	material->specularColor[2]=color->blue;
}
Public void setShininess(Material* material,float shininess)
{
	material->shininess=shininess*100;
}
#define GL_TEXTURE_BASE_LEVEL 0
#define GL_TEXTURE_NO_BORDER 0
#define Texture2D struct Texture2D
Texture2D
{
	byte* imagePixels;
	int imageWidth;
	int imageHeight;
	Texture imageTexture;
};
Public Texture2D* newTexture2D(String imageName,int imageWidth,int imageHeight)
{
	Texture2D* texture2D=(Texture2D*)malloc(sizeof(Texture2D));
	texture2D->imagePixels=readImage(imageName,imageWidth,imageHeight);
	texture2D->imageWidth=imageWidth;
	texture2D->imageHeight=imageHeight;
	return texture2D;
}
#define Appearance struct Appearance
Appearance
{
	PolygonAttributes* polygonAttributes;
	PointAttributes* pointAttributes;
	LineAttributes* lineAttributes;
	TransparencyAttributes* transparencyAttributes;
	Material* material;
	Texture2D* texture;
};
Public Appearance* newAppearance()
{
	Appearance* appearance=(Appearance*)malloc(sizeof(Appearance));
	appearance->polygonAttributes=null;
	appearance->pointAttributes=null;
	appearance->lineAttributes=null;
	appearance->transparencyAttributes=null;
	appearance->material=null;
	appearance->texture=null;
	return appearance;
}
Public void setPolygonAttributes(Appearance* appearance,PolygonAttributes* polygonAttributes)
{
	appearance->polygonAttributes=polygonAttributes;
}
Public void setPointAttributes(Appearance* appearance,PointAttributes* pointAttributes)
{
	appearance->pointAttributes=pointAttributes;
}
Public void setLineAttributes(Appearance* appearance,LineAttributes* lineAttributes)
{
	appearance->lineAttributes=lineAttributes;
}
Public void setTransparencyAttributes(Appearance* appearance,TransparencyAttributes* transparencyAttributes)
{
	appearance->transparencyAttributes=transparencyAttributes;
}
Public void setMaterial(Appearance* appearance,Material* material)
{
	appearance->material=material;
}
Public void setTexture(Appearance* appearance,Texture2D* texture)
{
	appearance->texture=texture;
}
#define Shape3D struct Shape3D
Shape3D
{
	GeometryArray* geometryArray;
	Appearance* appearance;
	Shape3D* next;
};
Public Shape3D* newShape3D()
{
	Shape3D* shape3D=(Shape3D*)malloc(sizeof(Shape3D));
	shape3D->geometryArray=null;
	shape3D->appearance=null;
	shape3D->next=null;
	return shape3D;
}
Public void setGeometry(Shape3D* shape3D,GeometryArray* geometryArray)
{
	shape3D->geometryArray=geometryArray;
}
Public void setAppearance(Shape3D* shape3D,Appearance* appearance)
{
	shape3D->appearance=appearance;
}
#define Primitive struct Primitive
#define CONE 1
#define CYLINDER 2
#define SPHERE 3
#define BOX 4
#define COLOR_CUBE 5
Primitive
{
	int type;
	float radius;
	float length;
	float width;
	float height;
	Appearance* appearance;
	Primitive* next;
};
Public Primitive* newCone(float radius,float height,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=CONE;
	primitive->radius=radius;
	primitive->height=height;
	primitive->appearance=appearance;
	primitive->next=null;
	return primitive;
}
Public Primitive* newCylinder(float radius,float height,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=CYLINDER;
	primitive->radius=radius;
	primitive->height=height;
	primitive->appearance=appearance;
	primitive->next=null;
	return primitive;
}
Public Primitive* newSphere(float radius,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=SPHERE;
	primitive->radius=radius;
	primitive->appearance=appearance;
	primitive->next=null;
	return primitive;
}
Public Primitive* newBox(float length,float width,float height,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=BOX;
	primitive->length=length;
	primitive->width=width;
	primitive->height=height;
	primitive->appearance=appearance;
	primitive->next=null;
	return primitive;
}
Public Primitive* newColorCube(float size,Appearance* appearance)
{
	Primitive* primitive=(Primitive*)malloc(sizeof(Primitive));
	primitive->type=COLOR_CUBE;
	primitive->length=size;
	primitive->width=size;
	primitive->height=size;
	primitive->appearance=appearance;
	primitive->next=null;
	return primitive;
}
#define LIGHT_SPOT 3
#define LIGHT_AMBIENT 2
#define LIGHT_POSITION 1
#define LIGHT_DIRECTION 0
#define Light struct Light
Light
{
	int index;
	float color[4];
	float position[4];
	Light* next;
};
Public Light* newPositionalLight(Color3f* color,Point3f* position)
{
	Light* light=(Light*)malloc(sizeof(Light));
	light->index=0;
	light->color[0]=color->red;
	light->color[1]=color->green;
	light->color[2]=color->blue;
	light->color[3]=1.0f;
	light->position[0]=position->x;
	light->position[1]=position->y;
	light->position[2]=position->z;
	light->position[3]=LIGHT_POSITION;
	light->next=null;
	return light;
}
Public Light* newDirectionalLight(Color3f* color,Vector3f* direction)
{
	Light* light=(Light*)malloc(sizeof(Light));
	light->index=0;
	light->color[0]=color->red;
	light->color[1]=color->green;
	light->color[2]=color->blue;
	light->color[3]=1.0f;
	light->position[0]=-direction->x;
	light->position[1]=-direction->y;
	light->position[2]=-direction->z;
	light->position[3]=LIGHT_DIRECTION;
	light->next=null;
	return light;
}
Public Light* newPointLight(Color3f* color,Point3f* startPoint,Point3f* endPoint)
{
	Light* light=(Light*)malloc(sizeof(Light));
	light->index=0;
	light->color[0]=color->red;
	light->color[1]=color->green;
	light->color[2]=color->blue;
	light->color[3]=1.0f;
	light->position[0]=endPoint->x-startPoint->x;
	light->position[1]=endPoint->y-startPoint->y;
	light->position[2]=endPoint->z-startPoint->z;
	light->position[3]=LIGHT_SPOT;
	light->next=null;
	return light;
}
Public Light* newAmbientLight(Color3f* color)
{
	Light* light=(Light*)malloc(sizeof(Light));
	light->index=0;
	light->color[0]=color->red;
	light->color[1]=color->green;
	light->color[2]=color->blue;
	light->color[3]=1.0f;
	light->position[3]=LIGHT_AMBIENT;
	light->next=null;
	return light;
}
#define Transform3D struct Transform3D
Transform3D
{
	Vector3d* translation;
	Vector3d* scale;
	AxisAngle4d* rotation;
};
Public Transform3D* newTransform3D()
{
	Transform3D* transform3D=(Transform3D*)malloc(sizeof(Transform3D));
	transform3D->translation=null;
	transform3D->scale=null;
	transform3D->rotation=null;
	return transform3D;
}
Public void setTranslation(Transform3D* transform3D,Vector3d* translation)
{
	transform3D->translation=translation;
}
Public void setScale(Transform3D* transform3D,Vector3d* scale)
{
	transform3D->scale=scale;
}
Public void setRotation(Transform3D* transform3D,AxisAngle4d* rotation)
{
	transform3D->rotation=rotation;
}
Public void rotX(Transform3D* transform3D,double a)
{
	if(transform3D->rotation!=null)free(transform3D->rotation);
	transform3D->rotation=newAxisAngle4d(1,0,0,a);
}
Public void rotY(Transform3D* transform3D,double a)
{
	if(transform3D->rotation!=null)free(transform3D->rotation);
	transform3D->rotation=newAxisAngle4d(0,1,0,a);
}
Public void rotZ(Transform3D* transform3D,double a)
{
	if(transform3D->rotation!=null)free(transform3D->rotation);
	transform3D->rotation=newAxisAngle4d(0,0,1,a);
}
#define TransformGroup struct TransformGroup
TransformGroup
{
	Transform3D* transform3D;
	Primitive* firstPrimitive;
	Primitive* lastPrimitive;
	int primitiveLength;
	Shape3D* firstShape3D;
	Shape3D* lastShape3D;
	int shape3DLength;
	TransformGroup* firstTransformGroup;
	TransformGroup* lastTransformGroup;
	int transformGroupLength;
	TransformGroup* next;
};
Public TransformGroup* newTransformGroup(Transform3D* transform3D)
{
	TransformGroup* transformGroup=(TransformGroup*)malloc(sizeof(TransformGroup));
	transformGroup->transform3D=transform3D;
	transformGroup->firstPrimitive=null;
	transformGroup->lastPrimitive=null;
	transformGroup->primitiveLength=0;
	transformGroup->firstShape3D=null;
	transformGroup->lastShape3D=null;
	transformGroup->shape3DLength=0;
	transformGroup->firstTransformGroup=null;
	transformGroup->lastTransformGroup=null;
	transformGroup->transformGroupLength=0;
	transformGroup->next=null;
	return transformGroup;
}
Public void addChild_TT(TransformGroup* transformGroup,TransformGroup* childTransformGroup)
{
	if(transformGroup->firstTransformGroup==null)
	{
		transformGroup->firstTransformGroup=childTransformGroup;
		transformGroup->lastTransformGroup=childTransformGroup;
	}
	else
	{
		transformGroup->lastTransformGroup->next=childTransformGroup;
		transformGroup->lastTransformGroup=childTransformGroup;
	}
	transformGroup->transformGroupLength++;
}
Public void addChild_TP(TransformGroup* transformGroup,Primitive* primitive)
{
	if(transformGroup->firstPrimitive==null)
	{
		transformGroup->firstPrimitive=primitive;
		transformGroup->lastPrimitive=primitive;
	}
	else
	{
		transformGroup->lastPrimitive->next=primitive;
		transformGroup->lastPrimitive=primitive;
	}
	transformGroup->primitiveLength++;
}
Public void addChild_TS(TransformGroup* transformGroup,Shape3D* shape3D)
{
	if(transformGroup->firstShape3D==null)
	{
		transformGroup->firstShape3D=shape3D;
		transformGroup->lastShape3D=shape3D;
	}
	else
	{
		transformGroup->lastShape3D->next=shape3D;
		transformGroup->lastShape3D=shape3D;
	}
	transformGroup->shape3DLength++;
}
#define BranchGroup struct BranchGroup
BranchGroup
{
	Primitive* firstPrimitive;
	Primitive* lastPrimitive;
	int primitiveLength;
	Shape3D* firstShape3D;
	Shape3D* lastShape3D;
	int shape3DLength;
	Light* firstLight;
	Light* lastLight;
	int lightLength;
	TransformGroup* firstTransformGroup;
	TransformGroup* lastTransformGroup;
	int transformGroupLength;
	BranchGroup* next;
};
Public BranchGroup* newBranchGroup()
{
	BranchGroup* branchGroup=(BranchGroup*)malloc(sizeof(BranchGroup));
	branchGroup->firstPrimitive=null;
	branchGroup->lastPrimitive=null;
	branchGroup->primitiveLength=0;
	branchGroup->firstShape3D=null;
	branchGroup->lastShape3D=null;
	branchGroup->shape3DLength=0;
	branchGroup->firstLight=null;
	branchGroup->lastLight=null;
	branchGroup->lightLength=0;
	branchGroup->firstTransformGroup=null;
	branchGroup->lastTransformGroup=null;
	branchGroup->transformGroupLength=0;
	return branchGroup;
};
Public void addChild_BT(BranchGroup* branchGroup,TransformGroup* transformGroup)
{
	if(branchGroup->firstTransformGroup==null)
	{
		branchGroup->firstTransformGroup=transformGroup;
		branchGroup->lastTransformGroup=transformGroup;
	}
	else
	{
		branchGroup->lastTransformGroup->next=transformGroup;
		branchGroup->lastTransformGroup=transformGroup;
	}
	branchGroup->transformGroupLength++;
}
Public void addChild_BP(BranchGroup* branchGroup,Primitive* primitive)
{
	if(branchGroup->firstPrimitive==null)
	{
		branchGroup->firstPrimitive=primitive;
		branchGroup->lastPrimitive=primitive;
	}
	else
	{
		branchGroup->lastPrimitive->next=primitive;
		branchGroup->lastPrimitive=primitive;
	}
	branchGroup->primitiveLength++;
}
Public void addChild_BS(BranchGroup* branchGroup,Shape3D* shape3D)
{
	if(branchGroup->firstShape3D==null)
	{
		branchGroup->firstShape3D=shape3D;
		branchGroup->lastShape3D=shape3D;
	}
	else
	{
		branchGroup->lastShape3D->next=shape3D;
		branchGroup->lastShape3D=shape3D;
	}
	branchGroup->shape3DLength++;
}
int lightCount=0;
Public void addChild_BL(BranchGroup* branchGroup,Light* light)
{
	light->index=lightCount++;
	if(branchGroup->firstLight==null)
	{
		branchGroup->firstLight=light;
		branchGroup->lastLight=light;
	}
	else
	{
		branchGroup->lastLight->next=light;
		branchGroup->lastLight=light;
	}
	branchGroup->lightLength++;
}
#define SimpleUniverse struct SimpleUniverse
SimpleUniverse
{
	BranchGroup* firstBranchGroup;
	BranchGroup* lastBranchGroup;
	int branchGroupLength;
};
Public SimpleUniverse* newSimpleUniverse()
{
	SimpleUniverse* simpleUniverse=(SimpleUniverse*)malloc(sizeof(SimpleUniverse));
	simpleUniverse->firstBranchGroup=null;
	simpleUniverse->lastBranchGroup=null;
	simpleUniverse->branchGroupLength=0;
	return simpleUniverse;
}
Public void addBranchGraph(SimpleUniverse* simpleUniverse,BranchGroup* branchGroup)
{
	if(simpleUniverse->firstBranchGroup==null)
	{
		simpleUniverse->firstBranchGroup=branchGroup;
		simpleUniverse->lastBranchGroup=branchGroup;
	}
	else
	{
		simpleUniverse->lastBranchGroup->next=branchGroup;
		simpleUniverse->lastBranchGroup=branchGroup;
	}
	simpleUniverse->branchGroupLength++;
}
void glEnableAppearance(Appearance* appearance)
{
	if(appearance!=null)
	{
		TransparencyAttributes_alpha=-1;
		TransparencyAttributes* transparencyAttributes=appearance->transparencyAttributes;
		Material* material=appearance->material;
		Texture2D* texture2D=appearance->texture;
		if(transparencyAttributes!=null)
		{
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			if(material!=null)
			{
				material->ambientColor[3]=transparencyAttributes->alpha;
				material->diffuseColor[3]=transparencyAttributes->alpha;
				material->specularColor[3]=transparencyAttributes->alpha;
			}
			TransparencyAttributes_alpha=transparencyAttributes->alpha;
		}
		if(material!=null)
		{
			glMaterialfv(GL_FRONT,GL_DIFFUSE,material->diffuseColor);
			glMaterialfv(GL_FRONT,GL_AMBIENT,material->ambientColor);
			glMaterialfv(GL_FRONT,GL_EMISSION,material->emissiveColor);
			glMaterialfv(GL_FRONT,GL_SPECULAR,material->specularColor);
			glMaterialf(GL_FRONT,GL_SHININESS,material->shininess);
		}
		if(texture2D!=null)
		{
			glEnable(GL_TEXTURE_2D);
			glPixelStorei(GL_UNPACK_ALIGNMENT,1);
			glGenTextures(1,&texture2D->imageTexture);
			glBindTexture(GL_TEXTURE_2D,texture2D->imageTexture);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
			glTexImage2D(GL_TEXTURE_2D,GL_TEXTURE_BASE_LEVEL,GL_RGBA,texture2D->imageWidth,texture2D->imageHeight,GL_TEXTURE_NO_BORDER,GL_RGBA,GL_UNSIGNED_BYTE,texture2D->imagePixels);
			glTexEnvf(GL_TEXTURE_ENV,GL_TEXTURE_ENV_MODE,GL_MODULATE);
			glBindTexture(GL_TEXTURE_2D,texture2D->imageTexture);
		}
	}
}
void glDisableAll()
{
	glDisable(GL_COLOR_MATERIAL);
	glDisable(GL_TEXTURE_2D);
	glDisable(GL_BLEND);
}
void glDisplayPrimitive(Primitive* primitive)
{
	glEnableAppearance(primitive->appearance);
	switch(primitive->type)
	{
		case CONE:
		{
			double radius=primitive->radius+0.0;
			double height=primitive->height+0.0;
			int slices=100;
			glutCone(radius,height,slices);
			break;
		}
		case CYLINDER:
		{
			double baseRadius=primitive->radius+0.0;
			double topRadius=baseRadius;
			double height=primitive->height+0.0;
			int slices=100;
			glutCylinder(baseRadius,topRadius,height,slices);
			break;
		}
		case SPHERE:
		{
			double radius=primitive->radius+0.0;
			int slices=200,stacks=100;
			//glutSolidSphere(radius,slices,stacks);
			glutSphere(radius,slices,stacks);
			break;
		}
		case BOX:
		{
			double length=primitive->length;
			double width=primitive->width;
			double height=primitive->height;
			glutBox(length,width,height);
			break;
		}
		case COLOR_CUBE:
		{
			double length=primitive->length;
			double width=primitive->width;
			double height=primitive->height;
			glutColorCube(length,width,height);
			break;
		}
	}
	glDisableAll();
}
void glDisplayShape3D(Shape3D* shape3D)
{
	GeometryArray* geometryArray=shape3D->geometryArray;
	if(geometryArray==null)return;
	glEnableAppearance(shape3D->appearance);
	GeometryInfo* geometryInfo=geometryArray->first;
	for(int i=0;i<geometryArray->length;i++)
	{
		int type=geometryInfo->geometryArrayType;
		if(type>=GEOMETRY_STRIP_ARRAY)glutGeometryStripArray(geometryInfo);
		else glutGeometryArray(geometryInfo);
		geometryInfo=geometryInfo->next;
	}
	glDisableAll();
}
void glDisplayTransformGroup(TransformGroup* transformGroup)
{
	Transform3D* transform3D=transformGroup->transform3D;
	int glTranslationIsNull=1;
	glPushMatrix();
	if(transform3D!=null)
	{
		Vector3d* translation=transform3D->translation;
		Vector3d* scale=transform3D->scale;
		AxisAngle4d* rotation=transform3D->rotation;
		if(translation!=null)
		{
			glTranslatef(translation->x,translation->y,translation->z);
			glPushMatrix();
			glTranslationIsNull=0;
		}
		if(rotation!=null)glRotatef(rotation->a*180.0/PI,rotation->x,rotation->y,rotation->z);
		if(scale!=null)glScalef(scale->x,scale->y,scale->z);
	}
	Primitive* primitive=transformGroup->firstPrimitive;
	int l=transformGroup->primitiveLength;
	for(int i=0;i<l;i++,primitive=primitive->next)glDisplayPrimitive(primitive);
	Shape3D* shape3D=transformGroup->firstShape3D;
	l=transformGroup->shape3DLength;
	for(int i=0;i<l;i++,shape3D=shape3D->next)glDisplayShape3D(shape3D);
	TransformGroup* childTransformGroup=transformGroup->firstTransformGroup;
	l=transformGroup->transformGroupLength;
	for(int i=0;i<l;i++,childTransformGroup=childTransformGroup->next)glDisplayTransformGroup(childTransformGroup);
	if(!glTranslationIsNull)glPopMatrix();
	glPopMatrix();
}
void glDisplayBranchGroup(BranchGroup* branchGroup)
{
	Primitive* primitive=branchGroup->firstPrimitive;
	int l=branchGroup->primitiveLength;
	for(int i=0;i<l;i++,primitive=primitive->next)glDisplayPrimitive(primitive);
	Shape3D* shape3D=branchGroup->firstShape3D;
	l=branchGroup->shape3DLength;
	for(int i=0;i<l;i++,shape3D=shape3D->next)glDisplayShape3D(shape3D);
	TransformGroup* transformGroup=branchGroup->firstTransformGroup;
	l=branchGroup->transformGroupLength;
	for(int i=0;i<l;i++,transformGroup=transformGroup->next)glDisplayTransformGroup(transformGroup);
}
int GL_LIGHTS[]={GL_LIGHT0,GL_LIGHT1,GL_LIGHT2,GL_LIGHT3,GL_LIGHT4,GL_LIGHT5,GL_LIGHT6,GL_LIGHT7};
void glDisplayLight(Light* light)
{
	int i=light->index;if(i>=8)return;
	int lightType=(int)light->position[3];
	glEnable(GL_LIGHTS[i]);
	if(lightType==LIGHT_AMBIENT)glLightModelfv(GL_LIGHT_MODEL_AMBIENT,light->color);
	else 
	{
		glLightfv(GL_LIGHTS[i],GL_AMBIENT,light->color);
		glLightfv(GL_LIGHTS[i],GL_SPECULAR,light->color);
		glLightfv(GL_LIGHTS[i],GL_DIFFUSE,light->color);
		if(lightType==LIGHT_SPOT)
		{
			float direction[]={light->position[0],light->position[1],light->position[2]};
			glLightf(GL_LIGHTS[i],GL_SPOT_CUTOFF,90.0);
			glLightfv(GL_LIGHTS[i],GL_SPOT_DIRECTION,direction);
		}
		else glLightfv(GL_LIGHTS[i],GL_POSITION,light->position);
	}
}
SimpleUniverse* viewingPlatform=null;
CallList branchGroupLists=null;
CallList lightList=null;
void glutInitViewingPlatform()
{
	if(viewingPlatform==null)return;
	int l=viewingPlatform->branchGroupLength;
	branchGroupLists=glGenLists(l);
	BranchGroup* branchGroup=viewingPlatform->firstBranchGroup;
	for(int i=0;i<l;i++,branchGroup=branchGroup->next)
	{
		glNewList(branchGroupLists+i,GL_COMPILE);
		glDisplayBranchGroup(branchGroup);
		glEndList();
	}
	lightList=glGenLists(1);
	glNewList(lightList,GL_COMPILE);
	branchGroup=viewingPlatform->firstBranchGroup;
	for(int i=0;i<l;i++,branchGroup=branchGroup->next)
	{
		Light* light=branchGroup->firstLight;
		int k=branchGroup->lightLength;
		for(int j=0;j<k;j++,light=light->next)glDisplayLight(light);
	}
	glEndList();
}
int isOrbitBehavior=0;
Vector3f* viewingPlatformDirection=newVector3f(0,0,1);
Vector3f* viewingPlatformUpDirection=newVector3f(0,1,0);
Vector3f* viewingPlatformRightDirection=newVector3f(1,0,0);
int translateMouseX=0,translateMouseY=0,translateMouseZ=0;
float mouseRotateUp=0,mouseRotateRight=0,mouseScale=0.5;
void glTranformViewingPlatform()
{
	glPopMatrix();
	double rot_Up=mouseRotateUp*180/PI;
	double rot_Right=mouseRotateRight*180/PI;
	double x=viewingPlatformUpDirection->x;
	double y=viewingPlatformUpDirection->y;
	double z=viewingPlatformUpDirection->z;
	glRotatef(rot_Up,x,y,z);
	rotate(viewingPlatformRightDirection,viewingPlatformUpDirection,-mouseRotateUp);
	x=viewingPlatformRightDirection->x;
	y=viewingPlatformRightDirection->y;
	z=viewingPlatformRightDirection->z;
	glRotatef(rot_Right,x,y,z);
	rotate(viewingPlatformUpDirection,viewingPlatformRightDirection,-mouseRotateRight);
	mouseRotateUp=0;
	mouseRotateRight=0;
	float k=0.01;
	mouseScale+=k*translateMouseZ;
	glPushMatrix();
	glScalef(exp(mouseScale),exp(mouseScale),exp(mouseScale));
}
void glDisplayViewingPlatform()
{
	if(viewingPlatform==null)return;
	glCallList(lightList);
	if(!isOrbitBehavior)glTranformViewingPlatform();
	int l=viewingPlatform->branchGroupLength;
	for(int i=0;i<l;i++)glCallList(branchGroupLists+i);
}
float viewDistance=2.0;
Vector3f* viewDirection=newVector3f(0,0,-1);
Vector3f* viewUpDirection=newVector3f(0,1,0);
Vector3f* viewRightDirection=newVector3f(1,0,0);
int currentMouseX=0,currentMouseY=0,currentMouseZ=0,currentMouseButton=0;
float eyeX=0.0,eyeY=0.0,eyeZ=viewDistance,centerX=0.0,centerY=0.0,centerZ=0.0,upX=0,upY=1,upZ=0;
float viewTranslateX=0,viewTranslateY=0,viewTranslateZ=0;
void glTransformViewPoint()
{
	if(isOrbitBehavior)
	{
		float k=-0.01;
		eyeX+=viewDirection->x*k*translateMouseZ;
		eyeY+=viewDirection->y*k*translateMouseZ;
		eyeZ+=viewDirection->z*k*translateMouseZ;
		centerX=eyeX+viewDirection->x*viewDistance;
		centerY=eyeY+viewDirection->y*viewDistance;
		centerZ=eyeZ+viewDirection->z*viewDistance;
	}
}
void glRotateViewDirection()
{
	float k=0.02;
	if(isOrbitBehavior)
	{
		float rotUp=-k*translateMouseX;
		float rotRight=-k*translateMouseY;
		rotate(viewDirection,viewUpDirection,rotUp);
		rotate(viewRightDirection,viewUpDirection,rotUp);
		rotate(viewDirection,viewRightDirection,rotRight);
		rotate(viewUpDirection,viewRightDirection,rotRight);
		upX=viewUpDirection->x;
		upY=viewUpDirection->y;
		upZ=viewUpDirection->z;
	}
	else 
	{
		mouseRotateUp=k*translateMouseX;
		mouseRotateRight=k*translateMouseY;
	}
}
float backgroundColor[]={0.0,0.0,0.0,0.0};
float lightPosition[]={2.0,2.0,2.0,1};
float lightDirection[]={0.0,0.0,2.0,0};
float lightColor[]={1.0,1.0,1.0,1.0};
void displayFunc()
{
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glTransformViewPoint();
	gluLookAt(eyeX,eyeY,eyeZ,centerX,centerY,centerZ,upX,upY,upZ);
	glClearColor(backgroundColor[0],backgroundColor[1],backgroundColor[2],backgroundColor[3]);
	glCullFace(GL_BACK);
	glFrontFace(GL_CCW);
	glEnable(GL_CULL_FACE);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_LIGHTING);
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	glDisplayViewingPlatform();
	glutSwapBuffers();
}

void timerFunc(int value)
{
	glutPostRedisplay();
	glutTimerFunc(3,timerFunc,1);
}
float fieldOfView=45.0,near=1.0,far=10.0;
void reshapeFunc(int w,int h)
{
	glViewport(0,0,w,h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(fieldOfView,(w+0.0)/h,near,far);
	glutPostRedisplay();
}
void mouseFunc(int button,int state,int x,int y)
{
	if(state==GLUT_DOWN)
	{
		currentMouseX=x;
		currentMouseY=y;
		currentMouseZ=y;
		currentMouseButton=button;
	}
}
void motionFunc(int x,int y)
{
	if(currentMouseButton==GLUT_RIGHT_BUTTON)
	{
		translateMouseX=x-currentMouseX;
		translateMouseY=y-currentMouseY;
		translateMouseZ=0;
		currentMouseX=x;
		currentMouseY=y;
		glRotateViewDirection();
	}
	else
	{
		translateMouseX=0;
		translateMouseY=0;
		translateMouseZ=y-currentMouseZ;
		currentMouseZ=y;
	}
	glutPostRedisplay();
}
void specialFunc(int k,int x,int y)
{
	float d=-0.1;
	switch(k)
	{
		case GLUT_KEY_UP:
		{
			viewTranslateX+=d*viewUpDirection->x;
			viewTranslateY+=d*viewUpDirection->y;
			viewTranslateZ+=d*viewUpDirection->z;
			break;
		}
		case GLUT_KEY_DOWN:
		{
			viewTranslateX-=d*viewUpDirection->x;
			viewTranslateY-=d*viewUpDirection->y;
			viewTranslateZ-=d*viewUpDirection->z;
			break;
		}
		case GLUT_KEY_LEFT:
		{
			viewTranslateX-=d*viewRightDirection->x;
			viewTranslateY-=d*viewRightDirection->y;
			viewTranslateZ-=d*viewRightDirection->z;
			break;
		}
		case GLUT_KEY_RIGHT:
		{
			viewTranslateX+=d*viewRightDirection->x;
			viewTranslateY+=d*viewRightDirection->y;
			viewTranslateZ+=d*viewRightDirection->z;
			break;
		}
	}
	glutPostRedisplay();
}
Public void setViewPlatformBehavior(int orbitBehavior)
{
	isOrbitBehavior=orbitBehavior==1?1:0;
}
Public void getViewingPlatform(SimpleUniverse* simpleUniverse)
{
	viewingPlatform=simpleUniverse;
	glutInitDisplayMode(GLUT_DOUBLE|GLUT_RGB|GLUT_DEPTH);
	glutInitWindowSize(500,500);
	glutInitWindowPosition(0,0);
	glutCreateWindow("");
	glutInitViewingPlatform();
	glutDisplayFunc(displayFunc);
	glutReshapeFunc(reshapeFunc);
	glutMouseFunc(mouseFunc);
	glutMotionFunc(motionFunc);
	glutSpecialFunc(specialFunc);
//	glutTimerFunc(33,timerFunc,1);
	glutMainLoop();
}
