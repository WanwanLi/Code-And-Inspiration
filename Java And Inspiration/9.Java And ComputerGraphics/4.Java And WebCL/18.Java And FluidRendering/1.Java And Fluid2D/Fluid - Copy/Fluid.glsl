-- RayTracing

			varying vec3 fragPosition;
			uniform vec3 eyePosition;
			uniform vec3 lightDirection;
			float shininess=20.0;
			float sphereRadius1=0.15;
			float sphereRadius2=0.4;
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
				b1=intersectSphere(sphereCenter1,sphereRadius1,origin,direction,d1);
				b2=intersectCube(sphereCenter2,sphereRadius2,origin,direction,d2);
				b3=intersectPlane(plane1,sphereRadius3,origin,direction,d3);
				if(b1&&d1<d2&&d1<d3)
				{
					position=origin+d1*direction;
					normal=normalize(position-sphereCenter1);
					color=sphereColor1;
				}
				else if(b2&&d2<d3)
				{
					position=origin+d2*direction;
					normal=normalCube(position,sphereCenter2);
					color=sphereColor2;
				}
				else if(b3)
				{
					position=origin+d3*direction;
					normal=normalize(plane1.xyz);
					color=sphereColor3;
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
			void main(void)
			{
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
				}
				gl_FragColor=vec4((viewColor+reflectColor)*shadowDecay,1.0);
			}

-- Vertex

in vec4 Position;

void main()
{
    gl_Position = Position;
}

-- Fill

out vec3 FragColor;

void main()
{
    FragColor = vec3(1, 0, 0);
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
    float solid = texture(Obstacles, InverseSize * fragCoord).x;
    if (solid > 0) {
        FragColor = vec4(0);
        return;
    }

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
    vec3 oN = texelFetchOffset(Obstacles, T, 0, ivec2(0, 1)).xyz;
    vec3 oS = texelFetchOffset(Obstacles, T, 0, ivec2(0, -1)).xyz;
    vec3 oE = texelFetchOffset(Obstacles, T, 0, ivec2(1, 0)).xyz;
    vec3 oW = texelFetchOffset(Obstacles, T, 0, ivec2(-1, 0)).xyz;

    // Use center pressure for solid cells:
    if (oN.x > 0) pN = pC;
    if (oS.x > 0) pS = pC;
    if (oE.x > 0) pE = pC;
    if (oW.x > 0) pW = pC;

    vec4 bC = texelFetch(Divergence, T, 0);
    FragColor = (pW + pE + pS + pN + Alpha * bC) * InverseBeta;
}

-- SubtractGradient

out vec2 FragColor;

uniform sampler2D Velocity;
uniform sampler2D Pressure;
uniform sampler2D Obstacles;
uniform float GradientScale;

void main()
{
    ivec2 T = ivec2(gl_FragCoord.xy);

    vec3 oC = texelFetch(Obstacles, T, 0).xyz;
    if (oC.x > 0) {
        FragColor = oC.yz;
        return;
    }

    // Find neighboring pressure:
    float pN = texelFetchOffset(Pressure, T, 0, ivec2(0, 1)).r;
    float pS = texelFetchOffset(Pressure, T, 0, ivec2(0, -1)).r;
    float pE = texelFetchOffset(Pressure, T, 0, ivec2(1, 0)).r;
    float pW = texelFetchOffset(Pressure, T, 0, ivec2(-1, 0)).r;
    float pC = texelFetch(Pressure, T, 0).r;

    // Find neighboring obstacles:
    vec3 oN = texelFetchOffset(Obstacles, T, 0, ivec2(0, 1)).xyz;
    vec3 oS = texelFetchOffset(Obstacles, T, 0, ivec2(0, -1)).xyz;
    vec3 oE = texelFetchOffset(Obstacles, T, 0, ivec2(1, 0)).xyz;
    vec3 oW = texelFetchOffset(Obstacles, T, 0, ivec2(-1, 0)).xyz;

    // Use center pressure for solid cells:
    vec2 obstV = vec2(0);
    vec2 vMask = vec2(1);

    if (oN.x > 0) { pN = pC; obstV.y = oN.z; vMask.y = 0; }
    if (oS.x > 0) { pS = pC; obstV.y = oS.z; vMask.y = 0; }
    if (oE.x > 0) { pE = pC; obstV.x = oE.y; vMask.x = 0; }
    if (oW.x > 0) { pW = pC; obstV.x = oW.y; vMask.x = 0; }

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
    vec3 oN = texelFetchOffset(Obstacles, T, 0, ivec2(0, 1)).xyz;
    vec3 oS = texelFetchOffset(Obstacles, T, 0, ivec2(0, -1)).xyz;
    vec3 oE = texelFetchOffset(Obstacles, T, 0, ivec2(1, 0)).xyz;
    vec3 oW = texelFetchOffset(Obstacles, T, 0, ivec2(-1, 0)).xyz;

    // Use obstacle velocities for solid cells:
    if (oN.x > 0) vN = oN.yz;
    if (oS.x > 0) vS = oS.yz;
    if (oE.x > 0) vE = oE.yz;
    if (oW.x > 0) vW = oW.yz;

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

-- Visualize

out vec4 FragColor;
uniform sampler2D Sampler;
uniform vec3 FillColor;
uniform vec2 Scale;

void main()
{
    float L = texture(Sampler, gl_FragCoord.xy * Scale).r;
    FragColor = vec4(FillColor, L);
}
