#version 150

in vec2 a_position;
in vec4 a_color;

//our camera matrix
uniform mat4 u_projTrans;

//send the color out to the fragment shader
out vec4 vColor;

void main() {
    vColor = a_color;
    gl_Position = u_projTrans * vec4(a_position.xy, 0.0, 1.0);
}