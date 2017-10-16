	
-- Vertex

in vec4 Position;
void main()
{
	gl_Position = Position;
}

-- Fill

out vec4 FragColor;
uniform vec3 FillColor;
void main()
{
	FragColor = vec4(FillColor, 1);
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
	float solid = texture(Obstacles, InverseSize * fragCoord).a;
	if (solid > 0.500){ FragColor = vec4(0); return; }
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

void main()
{
	ivec2 T = ivec2(gl_FragCoord.xy);

	// Find neighboring pressure:
	vec4 pN = texelFetchOffset(Pressure, T, 0, ivec2(0, 1));
	vec4 pS = texelFetchOffset(Pressure, T, 0, ivec2(0, -1));
	vec4 pE = texelFetchOffset(Pressure, T, 0, ivec2(1, 0));
	vec4 pW = texelFetchOffset(Pressure, T, 0, ivec2(-1, 0));
	vec4 pC = texelFetch(Pressure, T, 0);

	// Find neighboring obstacles:
	vec4 oN = texelFetchOffset(Obstacles, T, 0, ivec2(0, 1));
	vec4 oS = texelFetchOffset(Obstacles, T, 0, ivec2(0, -1));
	vec4 oE = texelFetchOffset(Obstacles, T, 0, ivec2(1, 0));
	vec4 oW = texelFetchOffset(Obstacles, T, 0, ivec2(-1, 0));

	// Use center pressure for solid cells:
	if (oN.a > 0.500) pN = pC;
	if (oS.a > 0.500) pS = pC;
	if (oE.a > 0.500) pE = pC;
	if (oW.a > 0.500) pW = pC;

	vec4 bC = texelFetch(Divergence, T, 0);
	FragColor = (pW + pE + pS + pN + Alpha * bC) * InverseBeta;
}

-- SubtractGradient

out vec2 FragColor;

uniform sampler2D Velocity;
uniform sampler2D Pressure;
uniform sampler2D Obstacles;
uniform sampler2D ObstVelocity;
uniform float GradientScale;

void main()
{
	ivec2 T = ivec2(gl_FragCoord.xy);

	float oC = texelFetch(Obstacles, T, 0).a;
	vec2 oV = texelFetch(ObstVelocity, T, 0).xy;
	if (oC > 0.500) { FragColor = oV; return; }

	// Find neighboring pressure:
	float pN = texelFetchOffset(Pressure, T, 0, ivec2(0, 1)).r;
	float pS = texelFetchOffset(Pressure, T, 0, ivec2(0, -1)).r;
	float pE = texelFetchOffset(Pressure, T, 0, ivec2(1, 0)).r;
	float pW = texelFetchOffset(Pressure, T, 0, ivec2(-1, 0)).r;
	float pC = texelFetch(Pressure, T, 0).r;

	// Find neighboring obstacles:
	float oN = texelFetchOffset(Obstacles, T, 0, ivec2(0, 1)).a;
	float oS = texelFetchOffset(Obstacles, T, 0, ivec2(0, -1)).a;
	float oE = texelFetchOffset(Obstacles, T, 0, ivec2(1, 0)).a;
	float oW = texelFetchOffset(Obstacles, T, 0, ivec2(-1, 0)).a;

	// Find neighboring obstacles velocity:
	vec4 ovN = texelFetchOffset(ObstVelocity, T, 0, ivec2(0, 1));
	vec4 ovS = texelFetchOffset(ObstVelocity, T, 0, ivec2(0, -1));
	vec4 ovE = texelFetchOffset(ObstVelocity, T, 0, ivec2(1, 0));
	vec4 ovW = texelFetchOffset(ObstVelocity, T, 0, ivec2(-1, 0));

	// Use center pressure for solid cells:
	vec2 obstV = vec2(0);
	vec2 vMask = vec2(1);

	if (oN > 0.500) { pN = pC; obstV.y = ovN.y; vMask.y = 0; }
	if (oS > 0.500) { pS = pC; obstV.y = ovS.y; vMask.y = 0; }
	if (oE > 0.500) { pE = pC; obstV.x = ovE.x; vMask.x = 0; }
	if (oW > 0.500) { pW = pC; obstV.x = ovW.x; vMask.x = 0; }

	// Enforce the free-slip boundary condition:
	vec2 oldV = texelFetch(Velocity, T, 0).xy;
	vec2 grad = vec2(pE - pW, pN - pS) * GradientScale;
	vec2 newV = oldV - grad;
	FragColor = (vMask * newV) + obstV;  
}

-- ComputeDivergence

out float FragColor;

uniform sampler2D Velocity;
uniform sampler2D Obstacles;
uniform sampler2D ObstVelocity;
uniform float HalfInverseCellSize;

void main()
{
	ivec2 T = ivec2(gl_FragCoord.xy);

	// Find neighboring velocities:
	vec2 vN = texelFetchOffset(Velocity, T, 0, ivec2(0, 1)).xy;
	vec2 vS = texelFetchOffset(Velocity, T, 0, ivec2(0, -1)).xy;
	vec2 vE = texelFetchOffset(Velocity, T, 0, ivec2(1, 0)).xy;
	vec2 vW = texelFetchOffset(Velocity, T, 0, ivec2(-1, 0)).xy;

	// Find neighboring obstacles:
	float oN = texelFetchOffset(Obstacles, T, 0, ivec2(0, 1)).a;
	float oS = texelFetchOffset(Obstacles, T, 0, ivec2(0, -1)).a;
	float oE = texelFetchOffset(Obstacles, T, 0, ivec2(1, 0)).a;
	float oW = texelFetchOffset(Obstacles, T, 0, ivec2(-1, 0)).a;

	// Find neighboring obstacles velocity:
	vec2 ovN = texelFetchOffset(ObstVelocity, T, 0, ivec2(0, 1)).xy;
	vec2 ovS = texelFetchOffset(ObstVelocity, T, 0, ivec2(0, -1)).xy;
	vec2 ovE = texelFetchOffset(ObstVelocity, T, 0, ivec2(1, 0)).xy;
	vec2 ovW = texelFetchOffset(ObstVelocity, T, 0, ivec2(-1, 0)).xy;

	// Use obstacle velocities for solid cells:
	if (oN > 0.500) vN = ovN;
	if (oS > 0.500) vS = ovS;
	if (oE > 0.500) vE = ovE;
	if (oW > 0.500) vW = ovW;

	FragColor = HalfInverseCellSize * (vE.x - vW.x + vN.y - vS.y);
}

-- Splat

out vec4 FragColor;

uniform vec2 Point;
uniform float Radius;
uniform vec3 FillColor;

void main()
{
	float d = distance(Point, gl_FragCoord.xy);
	if (d < Radius) {
		float a = (Radius - d) * 0.5;
		a = min(a, 1.0);
		FragColor = vec4(FillColor, a);
	} else {
		FragColor = vec4(0);
	}
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

	if (T > AmbientTemperature) {
		float D = texelFetch(Density, TC, 0).x;
		FragColor += (TimeStep * (T - AmbientTemperature) * Sigma - D * Kappa ) * vec2(0, 1);
	}
}

-- AdvectObstacles

out vec4 FragColor;

uniform sampler2D VelocityTexture;
uniform sampler2D SourceTexture;

uniform vec2 InverseSize;
uniform float TimeStep;

void main()
{
	vec2 fragCoord = gl_FragCoord.xy;
	vec2 u = texture(VelocityTexture, InverseSize * fragCoord).xy; 
	vec2 coord = InverseSize * (fragCoord - TimeStep * u);
	FragColor = texture(SourceTexture, coord);
}

-- AccelerateObstacles

out vec4 FragColor;

uniform sampler2D PositionTexture;
uniform sampler2D SourceTexture;

uniform vec2 InverseSize;
uniform float TimeStep;
uniform float Kappa;

void main()
{
	vec2 fragCoord = InverseSize * gl_FragCoord.xy; 
	vec2 v = texture(SourceTexture, fragCoord).xy;
	vec2 a = - Kappa * texture(PositionTexture,  fragCoord).xy; 
	FragColor = vec4( v + TimeStep * a , 0, 1);
}

-- MoveObstacles

out vec4 FragColor;

uniform sampler2D VelocityTexture;
uniform sampler2D SourceTexture;

uniform vec2 InverseSize;
uniform float TimeStep;

void main()
{
	vec2 fragCoord = InverseSize * gl_FragCoord.xy;
	vec2 v = texture(VelocityTexture,  fragCoord).xy; 
	vec2 p = texture(SourceTexture, fragCoord).xy;
	FragColor = vec4( p + TimeStep * v , 0, 1);
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
uniform vec3 lightPosition;
uniform vec3 lightDirection;
uniform bool isBackground;
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
float CornelBoxSize=1.75;
vec3 CornelBoxCenter=vec3(0.0,0.0,0.0);
vec3 CornelBoxColor3=vec3(0.63, 0.065, 0.05);
vec3 CornelBoxColor2=vec3(0.14, 0.45, 0.091);
vec3 CornelBoxColor1=vec3(0.625, 0.61, 0.48);
vec3 lightColor=vec3(1.0,1.0,1.0);
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
	if(tFar<MaxError){distance=MaxDistance;return false;}
	distance=tFar;
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
bool intersectObstacle(vec3 origin,vec3 direction,out vec3 position,out vec3 normal, out vec3 color)
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
bool intersectBackground(vec3 origin,vec3 direction,out vec3 position,out vec3 normal, out vec3 color)
{
	float distance=0;
	bool b=intersectCube(CornelBoxCenter, CornelBoxSize, origin, direction, distance);
	if(b)
	{
		position=origin+direction*distance;
		normal=-normalCube(position,CornelBoxCenter);
		color=CornelBoxColor1;
		if(normal.x<-0.5)color=CornelBoxColor2;
		else if(normal.x>0.5)color=CornelBoxColor3;
	}
	return b;
}
vec3 directionalLightColor(vec3 normalDirection,vec3 viewDirection,vec3 color)
{
	vec3 L=lightDirection,N=normalDirection;
	vec3 R=reflect(-L,N),V=viewDirection;
	float L_N=dot(L,N),R_V=max(dot(R,-V),0.0);
	float reflectance=0.5*pow(R_V,shininess);
	return clamp(L_N*color*lightColor+reflectance*lightColor,0.0,1.0);
}
vec3 positionalLightColor(vec3 position, vec3 normalDirection,vec3 viewDirection,vec3 color)
{
	vec3 D=lightPosition-position; 
	float d=length(D),I=7.5,d2=5+(d*d);
	vec3 L=normalize(D), N=normalDirection;
	vec3 R=reflect(-L,N), V=viewDirection;
	float L_N=dot(L,N), R_V=max(dot(R,-V),0.0);
	float reflectance=0.125*pow(R_V,shininess);
	return clamp(I*L_N*color*lightColor/d2+reflectance*lightColor/d2,0.0,1.0);
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
	if(isBackground)
	{
		if(intersectBackground(eyePosition,viewDirection,position,normal,color))
		{
			viewColor=positionalLightColor(position,normal,viewDirection,color);
			FragColor=vec4(viewColor,1);
		}
		else FragColor=vec4(0);
	}
	else
	{
		if(intersectObstacle(eyePosition,viewDirection,position,normal,color))
		{
			viewColor=directionalLightColor(normal,viewDirection,color);
			FragColor=vec4(viewColor,1);
		}
		else FragColor=vec4(0);
	}

}
