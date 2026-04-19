package net.not_thefirst.lib.gl_render_system.mesh;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public abstract class AbstractGpuMesh implements GpuMesh {

    protected final int vao;
    protected final int vbo;
    protected final int drawMode;
    protected final VertexFormat format;

    /**
     * Creates a new GPU mesh with the given parameters. The VAO and VBO should already be set up with the appropriate vertex data.
     * @param vao The vertex array object ID.
     * @param vbo The vertex buffer object ID.
     * @param drawMode The OpenGL draw mode (e.g. GL_TRIANGLES).
     * @param format The vertex format describing the layout of the vertex data (refer to {@link net.not_thefirst.lib.gl_render_system.vertex.VertexFormat} for details).
     */
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
