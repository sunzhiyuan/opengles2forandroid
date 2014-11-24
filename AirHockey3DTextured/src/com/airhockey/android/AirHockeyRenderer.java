/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.airhockey.android;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.*;
import static android.opengl.Matrix.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.R.integer;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockey.android.objects.Mallet;
import com.airhockey.android.objects.Table;
import com.airhockey.android.programs.ColorShaderProgram;
import com.airhockey.android.programs.TextureShaderProgram;
import com.airhockey.android.util.LoggerConfig;
import com.airhockey.android.util.MatrixHelper;
import com.airhockey.android.util.ShaderHelper;
import com.airhockey.android.util.TextResourceReader;
import com.airhockey.android.util.TextureHelper;

public class AirHockeyRenderer implements Renderer {
    private final Context context;
    private final float [] projectionMatrix  = new float[16];
    private final float [] modelMatrix = new float[16];
    private Table table;
    private Mallet mallet;
    
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;
    public AirHockeyRenderer(Context context) {
        this.context = context;
    }
    /**
     * onSurfaceChanged is called whenever the surface has changed. This is
     * called at least once when the surface is initialized. Keep in mind that
     * Android normally restarts an Activity on rotation, and in that case, the
     * renderer will be destroyed and a new one created.
     * 
     * @param width
     *            The new width, in pixels.
     * @param height
     *            The new height, in pixels.
     */
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width/(float)height, 1f, 10f);
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix,0,0f,0f,-2.5f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
        final float [] temp = new float[16];
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix,0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub
        glClearColor(0f, 0f, 0f, 0f);
        table = new Table();
        mallet = new Mallet();
        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
        
    }
    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub
        glClear(GL_COLOR_BUFFER_BIT);
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix, texture);
        table.bindData(textureProgram);
        table.draw();
        
        colorProgram.useProgram();
        colorProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();
    }
}
