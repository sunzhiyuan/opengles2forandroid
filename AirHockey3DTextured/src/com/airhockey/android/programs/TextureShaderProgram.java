package com.airhockey.android.programs;

import com.airhockey.android.R;
import com.airhockey.android.R.raw;

import android.R.integer;
import android.content.Context;
import static android.opengl.GLES20.*;
public class TextureShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    private final int aPostionLocation;
    private final int aTextureCoodinatesLocation;
    
    public TextureShaderProgram(Context context) {
        // TODO Auto-generated constructor stub
        super(context, R.raw.texture_vertex_shader, R.raw.simple_fragment_shader);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetAttribLocation(program, U_TEXTURE_UNIT);
        aPostionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoodinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }
    public void setUniforms(float [] matrix, int textureId) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTextureUnitLocation, 0);
    }
    public int getPositionAttributeLocation() {
        return aPostionLocation;
    }
    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoodinatesLocation;
    }
}
