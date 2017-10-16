attribute vec2 texcoord;
attribute vec3 position;
attribute vec3 normal;
varying vec3 vNormal;
varying vec3 vPosition;
varying vec2 vTexcoord;
uniform mat4 projectionMatrix;
uniform mat3 normalMatrix;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
 
void main()
{
	vPosition=(modelMatrix*vec4(position, 1)).xyz;
	vNormal=normalMatrix*normal; vTexcoord=texcoord; 
	gl_Position=projectionMatrix*viewMatrix*vec4(vPosition, 1.0);
}
