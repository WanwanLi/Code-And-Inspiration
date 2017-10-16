	
-- Vertex

in vec4 Position;
void main()
{
	gl_Position = Position;
}

-- Fill

out vec4 FragColor;

void main()
{
	FragColor = vec4(0, 0, 0, 1);
}

-- Advect

out vec4 FragColor;

uniform sampler2D VelocityTexture;
uniform sampler2D SourceTexture;
uniform sampler2D Obstacles;

uniform vec2 InverseSize;
uniform float TimeStep;
uniform float Dissipation;

void main()
{
	vec2 fragCoord = gl_FragCoord.xy;
	vec2 u = texture(VelocityTexture, InverseSize * fragCoord).xy;
	vec2 coord = InverseSize * (fragCoord - TimeStep * u);
	FragColor = Dissipation * texture(SourceTexture, coord);
}

-- Jacobi

out vec4 FragColor;

uniform sampler2D Pressure;
uniform sampler2D Divergence;
uniform sampler2D Obstacles;

uniform float Alpha;
uniform float InverseBeta;
uniform vec2 InverseSize;

bool isBetween(float x, float min, float max, float error)
{
	return (x>=min+error)&&(x<=max-error);
}

bool isObstacle(vec2 fragCoord, float error)
{
	vec2 coord=InverseSize * fragCoord;
	if(isBetween(coord.x, 0, 1, error)&&isBetween(coord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, coord).a;
		if(obstacle == 0)return false;
	}
	return true;
}

vec4 texelFetchOffsetPressure(ivec2 coord, ivec2 offset)
{
	vec2 fragCoord=vec2(coord+offset);
	if(isObstacle(fragCoord, 0.01))return texelFetch(Pressure, coord, 0);
	else return  texelFetchOffset(Pressure, coord, 0, offset);
}

void main()
{
	ivec2 coord = ivec2(gl_FragCoord.xy);
	vec4 U = texelFetchOffsetPressure(coord, ivec2(0, 1));
	vec4 D = texelFetchOffsetPressure(coord, ivec2(0, -1));
	vec4 L = texelFetchOffsetPressure(coord, ivec2(-1, 0));
	vec4 R = texelFetchOffsetPressure(coord, ivec2(1, 0));
	vec4 div = texelFetch(Divergence, coord, 0);
	FragColor = (U+D+L+R + Alpha * div) * InverseBeta;
}

-- SubtractGradient

out vec2 FragColor;

uniform vec2 InverseSize;
uniform float GradientScale;
uniform sampler2D Velocity;
uniform sampler2D Pressure;
uniform sampler2D Obstacles;

bool isBetween(float x, float min, float max, float error)
{
	return (x>=min+error)&&(x<=max-error);
}

bool isObstacle(vec2 fragCoord, float error)
{
	vec2 coord=InverseSize * fragCoord;
	if(isBetween(coord.x, 0, 1, error)&&isBetween(coord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, coord).a;
		if(obstacle == 0)return false;
	}
	return true;
}

float texelFetchOffsetPressure(ivec2 coord, ivec2 offset)
{
	vec2 fragCoord=vec2(coord+offset);
	if(isObstacle(fragCoord, 0.01))return texelFetch(Pressure, coord, 0).x;
	else return  texelFetchOffset(Pressure, coord, 0, offset).x;
}

void main()
{
	if(isObstacle(gl_FragCoord.xy, 0.01)){ FragColor = vec2(0); return; }
	ivec2 coord = ivec2(gl_FragCoord.xy);
	float U = texelFetchOffsetPressure(coord, ivec2(0, 1));
	float D = texelFetchOffsetPressure(coord, ivec2(0, -1));
	float L = texelFetchOffsetPressure(coord, ivec2(-1, 0));
	float R = texelFetchOffsetPressure(coord, ivec2(1, 0));
	vec2 grad = vec2(R - L, U - D) * GradientScale;
	FragColor = texelFetch(Velocity, coord, 0).xy - grad;
}

-- ComputeDivergence

out float FragColor;

uniform vec2 InverseSize;
uniform sampler2D Velocity;
uniform sampler2D Obstacles;
uniform float HalfInverseCellSize;

bool isBetween(float x, float min, float max, float error)
{
	return (x>=min+error)&&(x<=max-error);
}

bool isObstacle(vec2 fragCoord, float error)
{
	vec2 coord=InverseSize * fragCoord;
	if(isBetween(coord.x, 0, 1, error)&&isBetween(coord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, coord).a;
		if(obstacle == 0)return false;
	}
	return true;
}

vec2 texelFetchOffsetVelocity(ivec2 coord, ivec2 offset)
{
	vec2 fragCoord=vec2(coord+offset);
	if(isObstacle(fragCoord, 0.01))
	{
		vec2 reflect=-vec2(abs(offset.x),abs(offset.y));
		return reflect*texelFetch(Velocity, coord, 0).xy;
	}
	return  texelFetchOffset(Velocity, coord, 0, offset).xy;
}

void main()
{
	ivec2 coord = ivec2(gl_FragCoord.xy);
	vec2 U = texelFetchOffsetVelocity(coord, ivec2(0, 1));
	vec2 D = texelFetchOffsetVelocity(coord, ivec2(0, -1));
	vec2 L = texelFetchOffsetVelocity(coord, ivec2(-1, 0));
	vec2 R = texelFetchOffsetVelocity(coord, ivec2(1, 0));
	FragColor = HalfInverseCellSize * (R.x - L.x + U.y - D.y);
}

-- Splat

out vec4 FragColor;

uniform vec2 Point;
uniform float Radius;
uniform vec3 FillColor;

void main()
{
	float d = distance(Point, gl_FragCoord.xy);
	if (d < Radius) 
	{
		float a = (Radius - d) * 0.5;
		a = min(a, 1.0);
		FragColor = vec4(FillColor, a);
	}
	else FragColor = vec4(0);
}

-- Buoyancy

out vec2 FragColor;
uniform sampler2D Velocity;
uniform sampler2D Temperature;
uniform sampler2D Density;
uniform float AmbientTemperature;
uniform float TimeStep;
uniform float Sigma;
uniform float Kappa;

void main()
{
	ivec2 TC = ivec2(gl_FragCoord.xy);
	float T = texelFetch(Temperature, TC, 0).r;
	vec2 V = texelFetch(Velocity, TC, 0).xy;
	FragColor = V;
	if (T > AmbientTemperature) 
	{
		float D = texelFetch(Density, TC, 0).x;
		FragColor += (TimeStep * (T - AmbientTemperature) * Sigma - D * Kappa ) * vec2(0, 1);
	}
}

-- Visualize

out vec4 FragColor;
uniform sampler2D Sampler;
uniform vec3 FillColor;
uniform vec2 Scale;
uniform bool isFluid;
void main()
{
	vec4 texColor= texture(Sampler, gl_FragCoord.xy * Scale);
	if(isFluid)FragColor = vec4(FillColor, texColor.r);
	else FragColor = texColor;
}


-- RayTracing

out vec4 FragColor;
uniform vec2 Scale;
uniform vec3 eyePosition;
uniform vec3 lightDirection;
float shininess=20.0;
float sphereRadius1=0.15;
float sphereRadius2=0.25;
float sphereRadius3=20.0;
vec3 sphereCenter1=vec3(-1.0,0.0,0.0);
vec3 sphereCenter2=vec3(0.0,0.0,0.0);
vec3 sphereCenter3=vec3(1.0,0.0,0.0);
vec3 sphereColor1=vec3(1.0,0.0,0.0);
vec3 sphereColor2=vec3(0.0,1.0,0.0);
vec3 sphereColor3=vec3(0.0,0.0,1.0);
vec4 plane1=vec4(0.0,1.0,-0.0,-0.45);
vec3 directionalLightColor=vec3(1.0,1.0,1.0);
float MaxError=0.001,MaxDistance=10000.0;
bool intersectSphere(vec3 center,float radius,vec3 origin,vec3 direction,out float distance)
{
	vec3 connection=center-origin;
	float b=dot(connection,direction);
	float c=dot(connection,connection);
	float delta=b*b-c+radius*radius;
	if(delta<0.0){distance=MaxDistance;return false;}
	distance=b-sqrt(delta);
	if(distance<=MaxError){distance=MaxDistance;return false;}
	return true;
}
bool intersectCube(vec3 center,float radius,vec3 origin,vec3 direction,out float distance)
{
	vec3 cubeMin=center-vec3(radius);
	vec3 cubeMax=center+vec3(radius);
	vec3 tMin=(cubeMin-origin)/direction;
	vec3 tMax=(cubeMax-origin)/direction;
	vec3 t1=min(tMin,tMax),t2=max(tMin,tMax);
	float tNear=max(max(t1.x,t1.y),t1.z);
	float tFar=min(min(t2.x,t2.y),t2.z);
	if(tNear>tFar){distance=MaxDistance;return false;}
	if(tNear<MaxError){distance=MaxDistance;return false;}
	else distance=tNear;
	return true;
}
float signal(float x){return x>=0.0?1.0:-1.0;}
vec3 normalCube(vec3 position,vec3 center)
{
	vec3 normal=normalize(position-center);
	float x=normal.x,y=normal.y,z=normal.z;
	float absX=abs(x),absY=abs(y),absZ=abs(z);
	if(absX>absY&&absX>absZ){x=signal(x);y=0.0;z=0.0;}
	else if(absY>absZ){x=0.0;y=signal(y);z=0.0;}
	else {x=0.0;y=0.0,z=signal(z);}
	return vec3(x,y,z);
}
bool intersectPlane(vec4 plane,float radius,vec3 origin,vec3 direction,out float distance)
{
	float delta=sign(plane.w)*0.000001;
	vec3 normal=normalize(plane.xyz);
	vec3 center=vec3(0.0,plane.w,0.0);
	vec3 connection=center-origin;
	distance=(dot(normal,connection)+delta)/(dot(normal,direction)+delta);
	if(distance<=MaxError||distance>radius){distance=MaxDistance;return false;}
	return true;	
}
bool intersectScene(vec3 origin,vec3 direction,out vec3 position,out vec3 normal, out vec3 color)
{
	float d1,d2,d3;bool b1,b2,b3;
	b2=intersectSphere(sphereCenter2,sphereRadius2,origin,direction,d2);
	if(b2)
	{
		position=origin+d2*direction;
		normal=normalize(position-sphereCenter2);
		color=sphereColor2;
	}
	else return false;
	return true;
}
bool isOnShadow(vec3 position)
{
	vec3 newPosition,normal,color;
	return  intersectScene(position,lightDirection,newPosition,normal,color);
}
vec3 lightColor(vec3 normalDirection,vec3 viewDirection,vec3 color)
{
	vec3 L=lightDirection,N=normalDirection;
	vec3 R=reflect(-L,N),V=viewDirection;
	float L_N=dot(L,N),R_V=max(dot(R,-V),0.0);
	float reflectance=0.8*pow(R_V,shininess);
	return clamp(L_N*color+reflectance*directionalLightColor,0.0,1.0);
}
void main()
{
	vec2 fragCoord=(gl_FragCoord.xy * Scale-vec2(0.5))*2.0;
	fragCoord.y*=Scale.x/Scale.y;
	vec3 fragPosition=vec3(fragCoord.xy,0);
	vec3 viewDirection=normalize(fragPosition-eyePosition);
	vec3 position,normal,color;
	vec3 viewColor=vec3(0.0);
	vec3 reflectColor=vec3(0.0);
	float shadowDecay=1.0;
	if(intersectScene(eyePosition,viewDirection,position,normal,color))
	{
		if(isOnShadow(position))shadowDecay=0.6;
		viewColor=lightColor(normal,viewDirection,color);
		vec3 reflectedDirection=reflect(viewDirection,normal),reflectedPosition;
		if(intersectScene(position,reflectedDirection,reflectedPosition,normal,color))
		{
			reflectColor=lightColor(normal,reflectedDirection,color);
		}
		FragColor=vec4((viewColor+reflectColor)*shadowDecay,1);
	}
	else FragColor=vec4(0);
}
