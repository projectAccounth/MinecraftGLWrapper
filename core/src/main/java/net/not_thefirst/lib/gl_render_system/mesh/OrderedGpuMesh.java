package net.not_thefirst.lib.gl_render_system.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class OrderedGpuMesh extends AbstractGpuMesh {

    private final int vertexCount;

    public OrderedGpuMesh(
        int vao,
        int vbo,
        int vertexCount,
        int drawMode,
        VertexFormat format
    ) {
        super(vao, vbo, drawMode, format);
        this.vertexCount = vertexCount;
    }

    @Override
    public void draw() {
        GL30.glBindVertexArray(vao);
        GL11.glDrawArrays(drawMode, 0, vertexCount);
    }
}
