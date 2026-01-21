package net.not_thefirst.lib.gl_render_system.mesh;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public abstract class AbstractGpuMesh implements GpuMesh {

    protected final int vao;
    protected final int vbo;
    protected final int drawMode;
    protected final VertexFormat format;

    protected AbstractGpuMesh(
        int vao,
        int vbo,
        int drawMode,
        VertexFormat format
    ) {
        this.vao = vao;
        this.vbo = vbo;
        this.drawMode = drawMode;
        this.format = format;
    }

    @Override
    public VertexFormat format() {
        return format;
    }
    
    @Override
    public void close() {
        GL15.glDeleteBuffers(vbo);
        GL30.glDeleteVertexArrays(vao);
    }
}
