package net.not_thefirst.lib.gl_render_system.mesh;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class MeshUploader {

    private MeshUploader() {}

    public static OrderedGpuMesh uploadOrdered(CompiledMesh mesh) {
        return uploadOrdered(mesh, 0.0f, 0.0f, 0.0f);
    }

    public static OrderedGpuMesh uploadOrdered(
        CompiledMesh mesh,
        float offX, float offY, float offZ
    ) {
        VertexFormat format = mesh.format();
        int vertexCount = mesh.vertexCount();

        int stride = format.strideBytes();
        int totalBytes = vertexCount * stride;

        ByteBuffer buffer = BufferUtils.createByteBuffer(totalBytes);

        for (int i = 0; i < vertexCount; i++) {
            format.putVertex(buffer, i, mesh, offX, offY, offZ);
        }

        buffer.flip();

        int vao = GL30.glGenVertexArrays();
        int vbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        format.enable();

        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        return new OrderedGpuMesh(
            vao,
            vbo,
            vertexCount,
            mesh.mode(),
            format
        );
    }

    public static IndexedGpuMesh uploadIndexed(
        IndexedCompiledMesh mesh
    ) {
        return uploadIndexed(mesh, 0.0f, 0.0f, 0.0f);
    }

    public static IndexedGpuMesh uploadIndexed(
        IndexedCompiledMesh mesh,
        float offX, float offY, float offZ
    ) {
        VertexFormat format = mesh.format();

        int vertexCount = mesh.vertexCount();
        int indexCount  = mesh.indexCount();

        int stride = format.strideBytes();
        ByteBuffer vertexBuffer =
            BufferUtils.createByteBuffer(vertexCount * stride);

        for (int i = 0; i < vertexCount; i++) {
            format.putVertex(vertexBuffer, i, mesh, offX, offY, offZ);
        }

        vertexBuffer.flip();

        IntBuffer indexBuffer =
            BufferUtils.createIntBuffer(indexCount);
        indexBuffer.put(mesh.indices());
        indexBuffer.flip();

        int vao = GL30.glGenVertexArrays();
        int vbo = GL15.glGenBuffers();
        int ebo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(
            GL15.GL_ARRAY_BUFFER,
            vertexBuffer,
            GL15.GL_STATIC_DRAW
        );

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(
            GL15.GL_ELEMENT_ARRAY_BUFFER,
            indexBuffer,
            GL15.GL_STATIC_DRAW
        );

        format.enable();

        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        return new IndexedGpuMesh(
            vao,
            vbo,
            ebo,
            indexCount,
            GL11.GL_UNSIGNED_INT,
            mesh.mode(),
            format
        );
    }
}
