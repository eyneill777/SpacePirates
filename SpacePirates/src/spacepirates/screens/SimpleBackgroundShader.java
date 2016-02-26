package spacepirates.screens;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class SimpleBackgroundShader {
		static String vertexShader = 
			  "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
			+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
			+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
			+ "uniform mat4 u_projTrans;\n" //
			+ "varying vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "\n" //
			+ "void main()\n" //
			+ "{\n" //
			+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
			+ "   v_color.a = v_color.a * (255.0/254.0);\n" //
			+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
			+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
			+ "}\n";
		static String fragmentShader = 
			  "#ifdef GL_ES\n"
			+ "#define LOWP lowp\n"
			+ "precision mediump float;\n"
			+ "#else\n"
			+ "#define LOWP \n"
			+ "#endif\n"
			+ "varying LOWP vec4 v_color;\n"
			+ "varying vec2 v_texCoords;\n"
			+ "uniform sampler2D u_texture;\n"
			+ "uniform float time;\n"
			+ "void main()\n"
			+ "{\n"
			//+ "  vec4 color0 = vec4(0.2, 0.6, 0.3, 1);\n"
			+ "  vec4 color1 = vec4(0.5, cos(floor(v_texCoords.y*time)/time * time/10), sin(floor(v_texCoords.x*time)/time * time/5), 1);\n"
			+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords) * color1;\n"
			+ "}";
		
		public static ShaderProgram buildShader(){
			ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);
			return program;
		}
}