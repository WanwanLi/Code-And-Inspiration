attribute vec3 position;
attribute vec3 normal;
attribute vec3 color;
varying vec3 vColor;
varying vec3 vNormal;
varying vec3 vPosition;
uniform mat4 projectionMatrix;
uniform mat3 normalMatrix;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
 
void main()
{
	vPosition=(modelMatrix*vec4(position, 1)).xyz;
	vNormal=normalMatrix*normal; vColor=color; 
	gl_Position=projectionMatrix*viewMatrix*vec4(vPosition, 1.0);
}