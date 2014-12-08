uniform mat4 u_Matrix;
attribute vec3 a_Position;
varying vec3 v_Position;
void main()
{
    v_Position = a_Position;
    v_Position = -v_Position.z;
    gl_Positon = u_Matrix*vec4(a_Position,1.0);
    gl_Posiotn = gl_Position.xyww;
}