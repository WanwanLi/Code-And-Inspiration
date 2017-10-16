attribute vec3 position;
attribute vec3 color;
varying highp vec4 vColor;
uniform mat4 projectionMatrix;
uniform mat4 modelviewMatrix;
 
void main()
{
	gl_Position = projectionMatrix * modelviewMatrix * vec4(position, 1.0);
	vColor = vec4(color, 1.0);
}