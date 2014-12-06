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
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.multiplyMV;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.security.auth.PrivateCredentialPermission;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.airhockey.android.objects.Mallet;
import com.airhockey.android.objects.Puck;
import com.airhockey.android.objects.Table;
import com.airhockey.android.programs.ColorShaderProgram;
import com.airhockey.android.programs.TextureShaderProgram;
import com.airhockey.android.util.Geometry;
import com.airhockey.android.util.Geometry.Plane;
import com.airhockey.android.util.Geometry.Point;
import com.airhockey.android.util.Geometry.Ray;
import com.airhockey.android.util.Geometry.Sphere;
import com.airhockey.android.util.Geometry.Vector;
import com.airhockey.android.util.MatrixHelper;
import com.airhockey.android.util.TextureHelper;

public class AirHockeyRenderer implements Renderer {
    private boolean malletPressed = false;
    private Point blueMalletPosition;
    private final Context context;
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] invertedViewProjectionMatrix = new float[16];
    private Table table;
    private Mallet mallet;
    private Puck puck;
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
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
            / (float) height, 1f, 10f);
        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub
        glClearColor(0f, 0f, 0f, 0f);
        table = new Table();
        mallet = new Mallet(0.08f, 0.15f, 32);
        puck = new Puck(0.06f, 0.02f, 32);
        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);
        texture =
            TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
        blueMalletPosition = new Point(0f, mallet.height / 2f, 0.4f);
    }

    private void positionTableInScene() {
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0,
            modelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z) {
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0,
            modelMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub
        glClear(GL_COLOR_BUFFER_BIT);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        invertM(invertedViewProjectionMatrix,0,viewProjectionMatrix,0);
        positionTableInScene();
        textureProgram.useProgram();
        textureProgram.setUniforms(modelViewProjectionMatrix, texture);
        table.bindData(textureProgram);
        table.draw();
        positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
        mallet.bindData(colorProgram);
        mallet.draw();
        positionObjectInScene(0f, mallet.height / 2f, 0.4f);
        colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
        mallet.draw();
        positionObjectInScene(blueMalletPosition.x,blueMalletPosition.y,blueMalletPosition.z);
        colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        puck.bindData(colorProgram);
        puck.draw();
    }
    private Ray convertNormalized2DPointToRay(float normalizedX,float normalizedY) {
        final float[] nearPointNdc = {normalizedX,normalizedY,-1,1};
        final float[] farPointNdc = {normalizedX,normalizedY,1,1};
        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];
        multiplyMV(
            nearPointWorld,0,invertedViewProjectionMatrix,0,nearPointNdc,0);
        multiplyMV(
            farPointWorld,0,invertedViewProjectionMatrix,0,farPointNdc,0);
        divideByW(nearPointWorld);
        divideByW(farPointWorld);
        Point nearPointRay =
            new Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Point farPointRay =
            new Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);
        return new Ray(nearPointRay,Geometry.vectorBetween(nearPointRay,farPointRay));
    }
    public void handleTouchPress(float normalizedX, float normalizedY) {
        Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
        Sphere malletBoundingSphere = 
            new Sphere(new Point(blueMalletPosition.x, blueMalletPosition.y,
                blueMalletPosition.z), mallet.height / 2f);
        malletPressed = Geometry.intersects(malletBoundingSphere,ray);
    }
    private void divideByW(float [] vector) {
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }
    public void handleTouchDrag(float normalizedX, float normalizedY) {
        if (malletPressed) {
            Ray ray = convertNormalized2DPointToRay(normalizedX, normalizedY);
            Plane plane = new Plane(new Point(0, 0, 0), new Vector(0, 1, 0));
            Point touchedPoint = Geometry.intersectionPoint(ray, plane);
            blueMalletPosition = new Point(touchedPoint.x, mallet.height/2f, touchedPoint.z);
        }
    }
}
