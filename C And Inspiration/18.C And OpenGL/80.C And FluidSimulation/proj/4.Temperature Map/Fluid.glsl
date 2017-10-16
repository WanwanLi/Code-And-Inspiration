	
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

-- AdvectFluid

out vec4 FragColor;

uniform sampler2D VelocityTexture;
uniform sampler2D SourceTexture;

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


-- AdvectConcentration

out vec4 FragColor;

uniform sampler2D Velocity;
uniform sampler2D Concentration;
uniform sampler2D Obstacles;
uniform sampler2D ObstVelocity;

uniform vec2 InverseSize;
uniform float TimeStep;
uniform float MassLoss;
uniform float VelocityScale;

bool isBetween(float x, float min, float max, float error)
{
	return (x>=min+error)&&(x<=max-error);
}

bool isObstacle(ivec2 fragCoord, ivec2 offset, float error)
{
	vec2 texCoord=InverseSize * vec2(fragCoord+offset);
	if(isBetween(texCoord.x, 0, 1, error)&&isBetween(texCoord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, texCoord).a;
		if(obstacle == 0)return false;
	}
	return true;
}


bool isObstacle(vec2 fragCoord, float error)
{
	vec2 texCoord=InverseSize * fragCoord;
	if(isBetween(texCoord.x, 0, 1, error)&&isBetween(texCoord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, texCoord).a;
		return obstacle != 0;
	}
	return false;
}

float getAntiAliasedColor(float frontColor, float groundColor, vec2 coord, float error)
{
	int Precision = 5;
	vec2 fragCoord = coord-vec2(0.5);
	float counter = 0, step = 1.0 / (Precision - 1);
	for (int i = 0; i < Precision; i++)
	{
		fragCoord.x = coord.x-0.5;
		for (int j = 0; j < Precision; j++)
		{
			counter += isObstacle(fragCoord, error) ? 1 : 0;
			fragCoord.x += step;
		}
		fragCoord.y += step;
	}
	float weight = counter / (Precision * Precision);
	return weight * groundColor + (1 - weight) * frontColor;
}

bool isNearToObstacle(ivec2 coord, float error)
{
	if(isObstacle(coord, ivec2(0, 0), error))return true;
	if(isObstacle(coord, ivec2(0, 1), error))return true;
	if(isObstacle(coord, ivec2(0, -1), error))return true;
	if(isObstacle(coord, ivec2(-1, 0), error))return true;
	if(isObstacle(coord, ivec2(1, 0), error))return true;
	return false;
}

void main()
{
	float error = 0.01;
	vec2 fragCoord = gl_FragCoord.xy;
	vec2 u =  texture(Velocity, InverseSize * fragCoord).xy;
	vec2 coord = fragCoord - TimeStep *u;
	FragColor = texture(Concentration,  InverseSize * coord);
	u =  texture(ObstVelocity, InverseSize * fragCoord).xy;
	coord = fragCoord - TimeStep *u * VelocityScale;
	if(isNearToObstacle(ivec2(coord), error))
	{
		float intensity = getAntiAliasedColor(0, 1-MassLoss, coord, error);
		FragColor += intensity * texture(Concentration, InverseSize * fragCoord);
		FragColor = clamp(FragColor, vec4(0), vec4(1));
	}
}

-- Jacobi

out vec4 FragColor;

uniform sampler2D Pressure;
uniform sampler2D Divergence;

uniform float Alpha;
uniform float InverseBeta;

void main()
{
	ivec2 coord = ivec2(gl_FragCoord.xy);
	float U = texelFetchOffset(Pressure, coord, 0, ivec2(0, 1)).x;
	float D = texelFetchOffset(Pressure, coord, 0, ivec2(0, -1)).x;
	float L = texelFetchOffset(Pressure, coord, 0, ivec2(-1, 0)).x;
	float R = texelFetchOffset(Pressure, coord, 0, ivec2(1, 0)).x;
	float div = texelFetch(Divergence, coord, 0).x;
	FragColor = vec4((U+D+L+R + Alpha * div) * InverseBeta);
}


-- DiffuseConcentration

out float FragColor;

uniform float TimeStep;
uniform float CellSquare;
uniform float DiffusionScale;
uniform vec2 InverseSize;
uniform sampler2D Source;
uniform sampler2D Obstacles;

bool isBetween(float x, float min, float max, float error)
{
	return (x>=min+error)&&(x<=max-error);
}

bool isObstacle(ivec2 fragCoord, ivec2 offset, float error)
{
	vec2 texCoord=InverseSize * vec2(fragCoord+offset);
	if(isBetween(texCoord.x, 0, 1, error)&&isBetween(texCoord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, texCoord).a;
		if(obstacle == 0)return false;
	}
	return true;
}

bool isObstacle(vec2 fragCoord, float error)
{
	vec2 texCoord=InverseSize * fragCoord;
	if(isBetween(texCoord.x, 0, 1, error)&&isBetween(texCoord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, texCoord).a;
		if(obstacle == 0)return false;
	}
	return true;
}

float getAntiAliasedColor(float frontColor, float groundColor, vec2 coord, float error)
{
	int Precision = 5;
	vec2 fragCoord = coord-vec2(0.5);
	float counter = 0, step = 1.0 / (Precision - 1);
	for (int i = 0; i < Precision; i++)
	{
		fragCoord.x = coord.x-0.5;
		for (int j = 0; j < Precision; j++)
		{
			counter += isObstacle(fragCoord, error) ? 1 : 0;
			fragCoord.x += step;
		}
		fragCoord.y += step;
	}
	float weight = counter / (Precision * Precision);
	return weight * groundColor + (1 - weight) * frontColor;
}

bool isNearToObstacle(ivec2 coord, float error)
{
	if(isObstacle(coord, ivec2(0, 0), error))return true;
	if(isObstacle(coord, ivec2(0, 1), error))return true;
	if(isObstacle(coord, ivec2(0, -1), error))return true;
	if(isObstacle(coord, ivec2(-1, 0), error))return true;
	if(isObstacle(coord, ivec2(1, 0), error))return true;
	return false;
}

void main()
{
	float error = 0.01;
	ivec2 coord = ivec2(gl_FragCoord.xy);
	float sC = texelFetch(Source, coord, 0).x; 
	if(isNearToObstacle(coord, error))
	{
		FragColor = getAntiAliasedColor(sC, 0, gl_FragCoord.xy, error);
		return;
	}
	float sU = texelFetchOffset(Source, coord, 0, ivec2(0, 1)).x;
	float sD = texelFetchOffset(Source, coord, 0, ivec2(0, -1)).x;
	float sL = texelFetchOffset(Source, coord, 0, ivec2(-1, 0)).x;
	float sR = texelFetchOffset(Source, coord, 0, ivec2(1, 0)).x;
	float Laplace = ((sR -2*sC + sL) + (sU -2*sC + sD))/CellSquare;
	FragColor = sC + TimeStep * DiffusionScale * Laplace;
}


-- TransferHeat

out vec4 FragColor;

uniform float TimeStep;
uniform float CellSquare;
uniform float DiffusionScale;
uniform vec2 InverseSize;
uniform sampler2D Source;
uniform sampler2D FluidTemperature;


float texelFetchOffsetFluidTemperature(ivec2 coord, ivec2 offset)
{
	return texture(FluidTemperature, InverseSize * (coord + offset)).r;
}

float getAvgFluidTemperature(ivec2 coord, vec4 oC, vec4 oU, vec4 oD, vec4 oL, vec4 oR)
{
	float tC = 0, count = 0;
	float tU = texelFetchOffsetFluidTemperature(coord, ivec2(0, 1));
	float tD = texelFetchOffsetFluidTemperature(coord, ivec2(0, -1));
	float tL = texelFetchOffsetFluidTemperature(coord, ivec2(-1, 0));
	float tR = texelFetchOffsetFluidTemperature(coord, ivec2(1, 0));
	if(oU.a==0){tC+=tU; count++;}
	if(oD.a==0){tC+=tD; count++;}
	if(oL.a==0){tC+=tL; count++;}
	if(oR.a==0){tC+=tR; count++;}
	return count==0?oC.r:tC/count;
}

void main()
{
	ivec2 coord = ivec2(gl_FragCoord.xy);
	vec4 oC = texelFetch(Source, coord, 0); 
	if(oC.a==0){FragColor = oC;return;}
	vec4 oU = texelFetchOffset(Source, coord, 0, ivec2(0, 1));
	vec4 oD = texelFetchOffset(Source, coord, 0, ivec2(0, -1));
	vec4 oL = texelFetchOffset(Source, coord, 0, ivec2(-1, 0));
	vec4 oR = texelFetchOffset(Source, coord, 0, ivec2(1, 0));

	float heatTransfer = getAvgFluidTemperature(coord, oC, oU, oD, oL, oR) - oC.r, TransferRate = 0.8;
	float heat = oC.r + heatTransfer * TransferRate;
	FragColor = vec4(heat, heat, heat, oC.a);
}


-- DiffuseObstTemperature

out vec4 FragColor;

uniform float TimeStep;
uniform float CellSquare;
uniform float DiffusionScale;
uniform vec2 InverseSize;
uniform sampler2D Source;
uniform sampler2D FluidTemperature;


float texelFetchOffsetFluidTemperature(ivec2 coord, ivec2 offset)
{
	return texture(FluidTemperature, InverseSize * (coord + offset)).r;
}

float getAvgFluidTemperature1(ivec2 coord, vec4 oC, vec4 oU, vec4 oD, vec4 oL, vec4 oR)
{
	float tC = 0, count = 0;
	float tU = texelFetchOffsetFluidTemperature(coord, ivec2(0, 1));
	float tD = texelFetchOffsetFluidTemperature(coord, ivec2(0, -1));
	float tL = texelFetchOffsetFluidTemperature(coord, ivec2(-1, 0));
	float tR = texelFetchOffsetFluidTemperature(coord, ivec2(1, 0));
	if(oU.a==0){tC+=tU; count++;}
	if(oD.a==0){tC+=tD; count++;}
	if(oL.a==0){tC+=tL; count++;}
	if(oR.a==0){tC+=tR; count++;}
	return count==0?oC.r:tC/count;
}

float getAvgFluidTemperature(ivec2 coord, vec4 oC, vec4 oU, vec4 oD, vec4 oL, vec4 oR)
{
	float temperature = 0;
	int range = 5, counter = 0;
	for(int i=-range; i<=range; i++)
	{
		for(int j=-range; j<=range; j++)
		{
			float tU = texelFetchOffsetFluidTemperature(coord, ivec2(i, j));
			vec4 oT = texelFetch(Source, coord+ivec2(i, j), 0);
			if(oT.a==0){temperature+=tU; counter++;}
		}
	}
	return counter==0?oC.r:temperature/counter;
}

void main1()
{
	ivec2 coord = ivec2(gl_FragCoord.xy);
	vec4 oC = texelFetch(Source, coord, 0); 
	if(oC.a==0){FragColor = oC;return;}
	vec4 oU = texelFetchOffset(Source, coord, 0, ivec2(0, 1));
	vec4 oD = texelFetchOffset(Source, coord, 0, ivec2(0, -1));
	vec4 oL = texelFetchOffset(Source, coord, 0, ivec2(-1, 0));
	vec4 oR = texelFetchOffset(Source, coord, 0, ivec2(1, 0));

	float heatTransfer = getAvgFluidTemperature(coord, oC, oU, oD, oL, oR) - oC.r, TransferRate = 0.8;
	float sC = oC.r + heatTransfer * TransferRate;
	float sU = oU.a==0?sC:oU.r;
	float sD = oD.a==0?sC:oD.r;
	float sL = oL.a==0?sC:oL.r;
	float sR = oR.a==0?sC:oR.r;

	/*float Laplace = ((sR -2*sC + sL) + (sU -2*sC + sD))/CellSquare;
	float heat = sC +  clamp(TimeStep * 4*DiffusionScale *  Laplace, -sC, sC);*/

	float heat = sC +  ((sR -2*sC + sL) + (sU -2*sC + sD))/4;
	FragColor = vec4(heat, heat, heat, oC.a);
}



void main()
{
	ivec2 coord = ivec2(gl_FragCoord.xy);
	vec4 oC = texelFetch(Source, coord, 0); 
	if(oC.a==0){FragColor = oC;return;}


	vec4 oU = texelFetchOffset(Source, coord, 0, ivec2(0, 1));
	vec4 oD = texelFetchOffset(Source, coord, 0, ivec2(0, -1));
	vec4 oL = texelFetchOffset(Source, coord, 0, ivec2(-1, 0));
	vec4 oR = texelFetchOffset(Source, coord, 0, ivec2(1, 0));

	float heatTransfer = getAvgFluidTemperature(coord, oC, oU, oD, oL, oR) - oC.r, TransferRate = 1;
	float sC = oC.r + heatTransfer * TransferRate;

	float Laplace = 0;
	int range = 3, counter = 1;
	for(int i=-range; i<=range; i++)
	{
		for(int j=-range; j<=range; j++)
		{
			vec4 oT = texelFetch(Source, coord+ivec2(i, j), 0);
			if(oT.a!=0)
			{
				counter++;
				Laplace += oT.r - sC;
			}
		}
	}

	float heat = sC + Laplace /counter;
	FragColor = vec4(heat, heat, heat, oC.a);
}


-- DiffuseTemperature

out float FragColor;

uniform float TimeStep;
uniform float CellSquare;
uniform float ObstacleValue;
uniform float DiffusionScale;
uniform vec2 InverseSize;
uniform sampler2D Source;
uniform sampler2D Obstacles;

bool isBetween(float x, float min, float max, float error)
{
	return (x>=min+error)&&(x<=max-error);
}

bool isObstacle(ivec2 fragCoord, ivec2 offset, float error)
{
	vec2 texCoord=InverseSize * vec2(fragCoord+offset);
	if(isBetween(texCoord.x, 0, 1, error)&&isBetween(texCoord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, texCoord).a;
		if(obstacle == 0)return false;
	}
	return true;
}

bool isObstacle(vec2 fragCoord, float error)
{
	vec2 texCoord=InverseSize * fragCoord;
	if(isBetween(texCoord.x, 0, 1, error)&&isBetween(texCoord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, texCoord).a;
		if(obstacle == 0)return false;
	}
	return true;
}

float getAntiAliasedColor(float frontColor, float groundColor, vec2 coord, float error)
{
	int Precision = 5;
	vec2 fragCoord = coord-vec2(0.5);
	float counter = 0, step = 1.0 / (Precision - 1);
	for (int i = 0; i < Precision; i++)
	{
		fragCoord.x = coord.x-0.5;
		for (int j = 0; j < Precision; j++)
		{
			counter += isObstacle(fragCoord, error) ? 1 : 0;
			fragCoord.x += step;
		}
		fragCoord.y += step;
	}
	float weight = counter / (Precision * Precision);
	return weight * groundColor + (1 - weight) * frontColor;
}

bool isNearToObstacle(bool oC, bool oU, bool oD, bool oL, bool oR)
{
	if(oC)return true;
	if(oU)return true;
	if(oD)return true;
	if(oL)return true;
	if(oR)return true;
	return false;
}

bool isInsideObstacle(bool oC, bool oU, bool oD, bool oL, bool oR)
{
	if(!oC)return false;
	if(!oU)return true;
	if(!oD)return true;
	if(!oL)return true;
	if(!oR)return true;
	return true;
}

void main()
{
	float error = 0.01;
	float scale = DiffusionScale;
	ivec2 coord = ivec2(gl_FragCoord.xy);
	bool oC = isObstacle(coord, ivec2(0, 0), error);
	bool oU = isObstacle(coord, ivec2(0, 1), error);
	bool oD = isObstacle(coord, ivec2(0, -1), error);
	bool oL = isObstacle(coord, ivec2(-1, 0), error);
	bool oR = isObstacle(coord, ivec2(1, 0), error);
	float sC = texelFetch(Source, coord, 0).x; 
	float sU = texelFetchOffset(Source, coord, 0, ivec2(0, 1)).x;
	float sD = texelFetchOffset(Source, coord, 0, ivec2(0, -1)).x;
	float sL = texelFetchOffset(Source, coord, 0, ivec2(-1, 0)).x;
	float sR = texelFetchOffset(Source, coord, 0, ivec2(1, 0)).x;
	float heatTransfer = 0, TransferRate = 0.8;
	/*if(isNearToObstacle(oC, oU, oD, oL, oR))
	{
		sC = getAntiAliasedColor(sC, ObstacleValue, gl_FragCoord.xy, error);
		heatTransfer = ObstacleValue - sC;
		TransferRate *= -1;
	}*/
	float Laplace = ((sR -2*sC + sL) + (sU -2*sC + sD))/CellSquare;
	FragColor = sC + TimeStep * scale *  Laplace + heatTransfer * TransferRate ;
}


-- AddObstaclesVelocity

out vec2 FragColor;

uniform vec2 InverseSize;
uniform float VelocityScale;
uniform float GradientScale;
uniform sampler2D Velocity;
uniform sampler2D Pressure;
uniform sampler2D Obstacles;
uniform sampler2D ObstVelocity;

bool isBetween(float x, float min, float max, float error)
{
	return (x>=min+error)&&(x<=max-error);
}

bool isObstacle(vec2 fragCoord, float error)
{
	vec2 texCoord=InverseSize * fragCoord;
	if(isBetween(texCoord.x, 0, 1, error)&&isBetween(texCoord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, texCoord).a;
		if(obstacle == 0)return false;
	}
	return true;
}

vec4 texelFetchObstVelocity(ivec2 fragCoord)
{
	vec2 texCoord=InverseSize * vec2(fragCoord);
	return  texture(ObstVelocity, texCoord) * VelocityScale;
}

vec2 getAntiAliasedColor(vec2 frontColor, vec2 groundColor, vec2 coord, float error)
{
	int Precision = 5;
	vec2 fragCoord = coord-vec2(0.5);
	float counter = 0, step = 1.0 / (Precision - 1);
	for (int i = 0; i < Precision; i++)
	{
		fragCoord.x = coord.x-0.5;
		for (int j = 0; j < Precision; j++)
		{
			counter += isObstacle(fragCoord, error) ? 1 : 0;
			fragCoord.x += step;
		}
		fragCoord.y += step;
	}
	float weight = counter / (Precision * Precision);
	return weight * groundColor + (1 - weight) * frontColor;
}

bool isObstacle(ivec2 fragCoord, ivec2 offset, float error)
{
	vec2 texCoord=InverseSize * vec2(fragCoord+offset);
	if(isBetween(texCoord.x, 0, 1, error)&&isBetween(texCoord.y, 0, 1, error))
	{
		float obstacle = texture(Obstacles, texCoord).a;
		if(obstacle == 0)return false;
	}
	return true;
}

bool isNearToObstacle(ivec2 coord, float error)
{
	if(isObstacle(coord, ivec2(0, 0), error))return true;
	if(isObstacle(coord, ivec2(0, 1), error))return true;
	if(isObstacle(coord, ivec2(0, -1), error))return true;
	if(isObstacle(coord, ivec2(-1, 0), error))return true;
	if(isObstacle(coord, ivec2(1, 0), error))return true;
	return false;
}

void main()
{
	float error = 0.01;
	vec2 fragCoord=gl_FragCoord.xy;
	vec2 texCoord = InverseSize * fragCoord;
	vec2 fluidVelocity = texture(Velocity, texCoord).xy;
	vec2 obstVelocity = texture(ObstVelocity, texCoord).xy *VelocityScale;
	vec2 coord = fragCoord-obstVelocity*0.08; 
	if(isNearToObstacle(ivec2(coord), error))
	{
		FragColor = getAntiAliasedColor(fluidVelocity, obstVelocity, coord, error);
	}
	else FragColor = fluidVelocity;
}


-- SubtractGradient

out vec2 FragColor;

uniform float GradientScale;
uniform sampler2D Velocity;
uniform sampler2D Pressure;


void main()
{
	ivec2 coord = ivec2(gl_FragCoord.xy); 
	float pU = texelFetchOffset(Pressure, coord, 0, ivec2(0, 1)).x;
	float pD = texelFetchOffset(Pressure, coord, 0, ivec2(0, -1)).x;
	float pL = texelFetchOffset(Pressure, coord, 0, ivec2(-1, 0)).x;
	float pR = texelFetchOffset(Pressure, coord, 0, ivec2(1, 0)).x;
	float pC = texelFetch(Pressure, coord, 0).x;
	vec2 grad = vec2(pR - pL, pU - pD) * GradientScale;
	FragColor = texelFetch(Velocity, coord, 0).xy - grad;
}

-- ComputeDivergence

out float FragColor;

uniform float VelocityScale;
uniform sampler2D Velocity;
uniform float HalfInverseCellSize;

void main()
{
	ivec2 coord = ivec2(gl_FragCoord.xy);
	vec4 vU = texelFetchOffset(Velocity, coord,  0, ivec2(0, 1));
	vec4 vD = texelFetchOffset(Velocity, coord,  0, ivec2(0, -1));
	vec4 vL = texelFetchOffset(Velocity, coord,  0, ivec2(-1, 0));
	vec4 vR = texelFetchOffset(Velocity, coord,  0, ivec2(1, 0));
	FragColor = HalfInverseCellSize * (vR.x - vL.x + vU.y - vD.y);
}

-- AddSmoke

out vec4 FragColor;

uniform vec2 Scale;
uniform vec2 Center;
uniform float Radius;
uniform vec3 FillColor;

vec2 scaleCenter(vec2 coord, vec2 scale, vec2 center)
{
	return (coord-center)*scale+center;
}
void main()
{
	vec2 fragCoord = scaleCenter(gl_FragCoord.xy, Scale, Center);
	float d = distance(fragCoord , Center);
	if (d < Radius) 
	{
		float a =  min((Radius - d) * 0.5, 1.0);
		FragColor = vec4(FillColor, a);
	}
	else FragColor = vec4(0);
}


-- AddExternalForce

out vec2 FragColor;
uniform sampler2D Velocity;
uniform sampler2D Temperature;
uniform sampler2D Concentration;
uniform float AmbientTemperature;
uniform float SmokeDensity;
uniform float AirDensity;
uniform float TimeStep;
uniform float Buoyancy;

void main()
{
	ivec2 coord = ivec2(gl_FragCoord.xy);
	float T = texelFetch(Temperature, coord, 0).r;
	FragColor = texelFetch(Velocity, coord, 0).xy;
	if (T > AmbientTemperature) 
	{
		float C = texelFetch(Concentration, coord, 0).x;
		float D =  C * SmokeDensity + (1-C) * AirDensity;
		FragColor += (TimeStep * (T - AmbientTemperature) * Buoyancy - D) * vec2(0, 1);
	}
}

-- AdvectObstacles

out vec4 FragColor;

uniform sampler2D VelocityTexture;
uniform sampler2D SourceTexture;
uniform float ObstacleThreshold;

uniform vec2 InverseSize;
uniform float TimeStep;

void main()
{
	vec2 fragCoord = gl_FragCoord.xy;
	vec2 u = texture(VelocityTexture, InverseSize * fragCoord).xy; 
	vec2 coord = InverseSize * (fragCoord - TimeStep * u);
	vec4 obstacleColor = texture(SourceTexture, coord);
	if(obstacleColor.a<ObstacleThreshold)obstacleColor.a=0;
	else if(obstacleColor.a>1-ObstacleThreshold)obstacleColor.a=1;
	FragColor = obstacleColor;
}

-- AccelerateObstacles

out vec4 FragColor;

uniform sampler2D PositionTexture;
uniform sampler2D SourceTexture;

uniform vec2 InverseSize;
uniform float TimeStep;
uniform float Gravity;
uniform float Kappa;

bool isBetween(float x, float min, float max, float error)
{
	return (x>=min+error)&&(x<=max-error);
}

bool isBorder(vec2 texCoord, float error)
{
	return !(isBetween(texCoord.x, 0, 1, error)&&isBetween(texCoord.y, 0, 1, error));
}

void main()
{
	vec2 fragCoord = InverseSize * gl_FragCoord.xy; 
	vec2 v = texture(SourceTexture, fragCoord).xy;
	if(isBorder(fragCoord, 0.01)){FragColor = vec4(v,0,1);return;}
	vec2 d = texture(PositionTexture,  fragCoord).xy;
	vec2 a =  - Kappa * d - vec2(0, Gravity);
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
uniform sampler2D dSampler;
uniform vec3 FillColor;
uniform vec2 Scale;
uniform bool isFluid;
uniform bool isHeat;
uniform bool isDensity;
uniform bool isVelocity;
uniform bool isObstHeat;
uniform float minValue;
uniform float maxValue;
uniform float velocityWidth;
uniform vec2 velocityGridSize;

vec3 heatColor(float value, float min, float max)
{
	float ratio=(value-min)/(max-min);
	float hue=clamp(1.0-ratio,0.0,1.0);
	float saturation=1, brightness=1;
	float red=0,green=0,blue=0;
	float range=saturation*brightness;
	float high=brightness;
	float low=brightness-range;
	float H=4.5*hue;
	int n=int(H);
	float h=H-n;
	float mid=(n%2==0?low+h*range:low+(1-h)*range);
	switch(n)
	{
		case 0:red=high;green=mid;blue=low;break;
		case 1:red=mid;green=high;blue=low;break;
		case 2:red=low;green=high;blue=mid;break;
		case 3:red=low;green=mid;blue=high;break;
		case 4:red=mid;green=low;blue=high;break;
		case 5:red=high;green=low;blue=mid;break;
	}
	return vec3(red,green,blue);
}

vec3 densityColor(float value, float min, float max)
{
	float mid = (1-value)*min+value*max;
	return vec3((mid-min)/(max-min));
}

float distanceToRay(vec2 position,vec2 origin,vec2 direction)
{
	vec3 a=vec3(position-origin, 0);
	vec3 b=vec3(direction, 0);
	return length(cross(a, b));
}

vec4 velocityColor(float brightness, vec2 texCoord)
{
	vec2 gridSize=velocityGridSize;
	vec2 coord=texCoord/gridSize;
	vec2 gridCoord = floor(coord);
	vec2 center=gridCoord*gridSize+gridSize/2;
	vec2 velocity=texture(Sampler, center).xy;
	float intensity=length(velocity);
	if(intensity==0)return vec4(0);
	vec2 direction=normalize(velocity);
	float distance=distanceToRay(texCoord,center,direction);
	float decay=clamp(1-distance/velocityWidth,0,1);
	return vec4(FillColor, brightness*decay);
}

void main()
{
	vec2 texCoord = gl_FragCoord.xy*Scale;
	vec4 texColor = texture(Sampler, texCoord);
	if(isFluid)FragColor = vec4(FillColor, texColor.r);
	else if(isHeat)FragColor = vec4(heatColor(texColor.r, minValue, maxValue), 1);
	else if(isDensity)FragColor = vec4(densityColor(texColor.r, minValue, maxValue), 1);
	else if(isVelocity)FragColor = vec4(velocityColor(texture(dSampler, texCoord).r, texCoord));
	else if(isObstHeat)FragColor = vec4(heatColor(texColor.r, minValue, maxValue), texColor.a);
	else FragColor = texColor;
}


-- RayTracing

out vec4 FragColor;
uniform vec2 Scale;
uniform vec3 eyePosition;
uniform vec3 lightPosition;
uniform vec3 lightDirection;
uniform bool isBackground;
uniform bool isObstacleBox;
uniform bool isScaler;
uniform float scaler;
float shininess=20.0;
float sphereRadius=0.2;
vec3 sphereCenter=vec3(0.0,0.1,0.0);
vec3 sphereColor=vec3(0.0,1.0,0.0);
float boxRadius=0.175;
vec3 boxCenter=vec3(0.0,0.0,0.0);
vec3 boxColor=vec3(1.0,0.0,0.0);
float CornelBoxSize=1.75;
vec3 CornelBoxCenter=vec3(0.0,0.0,0.0);
vec3 CornelBoxColor3=vec3(0.63, 0.065, 0.05);
vec3 CornelBoxColor2=vec3(0.14, 0.45, 0.091);
vec3 CornelBoxColor1=vec3(0.625, 0.61, 0.48);
vec3 axis=vec3(1,0,1); float angle= 3.14 /4; 
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
bool intersectBox(vec3 center,float radius,vec3 origin,vec3 direction,out float distance)
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
	distance=tNear;
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
bool intersectObstacleSphere(vec3 origin,vec3 direction,out vec3 position,out vec3 normal, out vec3 color)
{
	float distance; bool intersect=intersectSphere(sphereCenter,sphereRadius,origin,direction,distance);
	if(intersect)
	{
		position=origin+distance*direction;
		normal=normalize(position-sphereCenter);
		color=sphereColor;
	}
	else return false;
	return true;
}
bool intersectObstacleBox(vec3 origin,vec3 direction,out vec3 position,out vec3 normal, out vec3 color)
{
	float distance; bool intersect=intersectBox(boxCenter,boxRadius,origin,direction,distance);
	if(intersect)
	{
		position=origin+distance*direction;
		normal=normalCube(position,boxCenter);
		color=boxColor;
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
	if(isScaler)return vec3(scaler);
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
vec3 rotate(vec3 vector,vec3 axis,float angle)
{
	if(angle==0)return vector;
	vec3 v=vector, u=normalize(axis);
	float uv=dot(u,v); vec3 n=cross(u,v);
	float cosA=cos(angle), sinA=sin(angle);
	float vecX=cosA*v.x+(1-cosA)*uv*u.x+sinA*n.x;
	float vecY=cosA*v.y+(1-cosA)*uv*u.y+sinA*n.y;
	float vecZ=cosA*v.z+(1-cosA)*uv*u.z+sinA*n.z;
	return vec3(vecX, vecY, vecZ);
}
void rotateViewDirection(vec3 center, vec3 axis,float angle, vec3 eyePosition,vec3 viewDirection, out vec3 newPosition, out vec3 newDirection)
{
	vec3 eyeDistance=eyePosition-center;
	vec3 newDistance=rotate(eyeDistance,axis,angle);
	newPosition=center+newDistance;
	newDirection=rotate(viewDirection,axis,angle);
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
	else if(isObstacleBox)
	{
		vec3 newPosition,newDirection;
		rotateViewDirection(boxCenter, axis, angle,eyePosition,viewDirection,newPosition,newDirection);
		if(intersectObstacleBox(newPosition,newDirection,position,normal,color))
		{
			viewColor=directionalLightColor(normal,viewDirection,color);
			FragColor=vec4(viewColor,1);
		}
		else FragColor=vec4(0);
	}
	else
	{
		if(intersectObstacleSphere(eyePosition,viewDirection,position,normal,color))
		{
			viewColor=directionalLightColor(normal,viewDirection,color);
			FragColor=vec4(viewColor,1);
		}
		else FragColor=vec4(0);
	}
}
