precision highp float;
varying vec2 vTexcoord;
varying vec3 vPosition;
varying vec3 vNormal;
uniform float shininess;
uniform vec3 lightColor;
uniform vec3 eyePosition;
uniform vec3 lightPosition;
uniform vec3 decayVector;
uniform sampler2D texture;

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
	vec3 diffuseColor=texture2D(texture, vTexcoord).rgb;
	vec3 specularColor=lightColor, light=lightIntensity();
	vec3 color=diffuseColor*light.x+specularColor*light.y;
	gl_FragColor=vec4(color*lightColor*light.z, 1.0);
}
