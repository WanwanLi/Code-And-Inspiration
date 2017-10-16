-- Vertex
in vec3 normal;
in vec3 position;
out vec3 glNormal;
uniform mat3 normalMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelviewMatrix;
void main()
{
	glNormal = normalMatrix * normal;
	gl_Position = projectionMatrix * modelviewMatrix * vec4(position, 1.0);
	gl_Position = vec4(position, 1.0);
}

-- Fragment
in vec3 glNormal;
uniform vec3 eyeDirection;
uniform vec3 lightDirection;
uniform vec3 ambientColor;
uniform vec3 specularColor;
uniform vec3 diffuseColor;
uniform float shininess;
void main()
{
	/*vec3 N = normalize(glNormal);
	vec3 E = normalize(eyeDirection);
	vec3 L = normalize(lightDirection);
	vec3 H = normalize(L + E);
	float D = max(0.0, dot(N, L));
	float S = max(0.0, dot(N, H));
	S = pow(S, Shininess);
	vec3 color = ambientColor; 
	color += diffuseColor * D;
	color += specularColor * S;
	FragColor = vec4(color, 1.0);*/
	gl_FragColor = vec4(1);
}
