package net.not_thefirst.lib.gl_render_system.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class IndexedGpuMesh extends AbstractGpuMesh {

    private final int ebo;
    private final int indexCount;
    private final int indexType;

    public IndexedGpuMesh(
        int vao,
        int vbo,
        int ebo,
        int indexCount,
        int indexType,
        GLPrimitive primitive,
        VertexFormat format
    ) {
        super(vao, vbo, primitive, format);
        this.ebo = ebo;
        this.indexCount = indexCount;
        this.indexType = indexType;
    }

    @Override
    public void draw() {
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL11.glDrawElements(primitive.getGLConst(), indexCount, indexType, 0);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void close() {
        GL15.glDeleteBuffers(ebo);
        super.close();
    }
}
