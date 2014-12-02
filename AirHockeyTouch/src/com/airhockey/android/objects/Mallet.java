package com.airhockey.android.objects;
import java.util.List;

import com.airhockey.android.data.VertexArray;
import com.airhockey.android.objects.ObjectBuilder.DrawCommand;
import com.airhockey.android.objects.ObjectBuilder.GeneratedData;
import com.airhockey.android.programs.ColorShaderProgram;
import com.airhockey.android.util.Geometry.Point;
public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 3;
    public final float radius;
    public final float height;
    private final VertexArray vertexArray;
    private final List<DrawCommand> drawList;
    public Mallet (float radius,float height, int numPointsArountMallet) {
        GeneratedData generatedData = ObjectBuilder.createMallet(new Point(0f,
            0f, 0f), radius, height, numPointsArountMallet);
        this.radius = radius;
        this.height = height;
        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }
    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(0,
            colorProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            0);
    }
    public void draw() {
        for(DrawCommand drawCommand:drawList){
            drawCommand.draw();
        }
    }
}
