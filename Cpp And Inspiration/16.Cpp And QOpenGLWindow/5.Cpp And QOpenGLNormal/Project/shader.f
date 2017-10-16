precision highp float;
varying vec3 vPosition;
varying vec3 vNormal;
varying vec3 vColor;
uniform float shininess;
uniform vec3 lightColor;
uniform vec3 eyePosition;
uniform vec3 lightPosition;
uniform vec3 decayVector;

float lightDecay(float distance)
{
	float  a=decayVector.x, b=decayVector.y, c=decayVector.z;
	return 1.0/(a+b*distance+c*distance*distance);
}
vec3 lightIntensity()
{
	vec3 N=normalize(vNormal);
	vec3 V=eyePosition-vPosition;
	vec3 L=lightPosition-vPosition;
	float decay=lightDecay(length(V));
	decay*=lightDecay(length(L));
	V=normalize(V); L=normalize(L);
	vec3 R=normalize(reflect(-L, N));
	float diffuse=max(0.0, dot(N, L));
	float specular=max(0.0, dot(V, R));
	specular=pow(specular, shininess);
	specular=diffuse==0.0?0.0:specular;
	return vec3(diffuse, specular, decay);
}
void main()
{
	vec3 specularColor=lightColor, diffuseColor=vColor;
	vec3 light=lightIntensity(), color=diffuseColor*light.x;
	color=(color+specularColor*light.y)*lightColor*light.z;
	gl_FragColor=vec4(color, 1.0);
}
