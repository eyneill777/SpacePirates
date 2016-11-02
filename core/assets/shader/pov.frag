#ifdef GL_ES
    precision lowp float;
    #define MED mediump
#else
    #define MED
#endif

varying vec4 v_color;
void main(){
    gl_FragColor = vec4(0, 0, 0, 1);
}