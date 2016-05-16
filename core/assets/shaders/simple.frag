#version 150

#ifdef GL_ES
precision mediump float;
#endif

//input from vertex shader
in vec4 vColor;

void main() {
    gl_FragColor = vColor;
}