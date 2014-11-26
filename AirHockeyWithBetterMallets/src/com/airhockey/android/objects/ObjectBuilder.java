package com.airhockey.android.objects;

import com.airhockey.android.util.Geometry.Cylinder;

import android.R.integer;

public class ObjectBuilder {
    private static final int FLOATS_PER_VERTEX = 3;
    private final float[] vertexData;
    private int offset = 0;

    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }

    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }
}
