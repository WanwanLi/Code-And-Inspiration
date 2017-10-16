attribute vec3 position;
attribute vec3 color;
varying highp vec4 vColor;
 
void main()
{
	gl_Position = vec4(position, 1.0);
	vColor = vec4(color, 1.0);
}