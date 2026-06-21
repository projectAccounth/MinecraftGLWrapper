package net.not_thefirst.lib.gl_render_system.shader;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public final class UniformBufferObject
        implements AutoCloseable {

    private static int boundUniformBuffer;

    private final int bufferId;
    private final int binding;
    private final int size;

    private boolean closed;

    public UniformBufferObject(
        int binding,
        int size
    ) {

        if (size <= 0) {
            throw new IllegalArgumentException(
                "UBO size must be > 0"
            );
        }

        this.binding = binding;
        this.size = size;

        bufferId = GL15.glGenBuffers();

        GL15.glBindBuffer(
            GL31.GL_UNIFORM_BUFFER,
            bufferId
        );

        GL15.glBufferData(
            GL31.GL_UNIFORM_BUFFER,
            size,
            GL15.GL_DYNAMIC_DRAW
        );

        GL30.glBindBufferBase(
            GL31.GL_UNIFORM_BUFFER,
            binding,
            bufferId
        );
    }

    private void checkAlive() {
        if (closed) {
            throw new IllegalStateException(
                "UBO already closed"
            );
        }
    }

    private void bindForUpdate() {

        if (boundUniformBuffer != bufferId) {

            GL15.glBindBuffer(
                GL31.GL_UNIFORM_BUFFER,
                bufferId
            );

            boundUniformBuffer = bufferId;
        }
    }

    public int size() {
        return size;
    }

    public void update(ByteBuffer data) {
        update(0, data);
    }

    public void update(
        int offset,
        ByteBuffer data
    ) {

        checkAlive();

        int bytes = data.remaining();

        if (offset + bytes > size) {
            throw new IllegalArgumentException(
                "UBO overflow. capacity=" +
                size +
                ", writeEnd=" +
                (offset + bytes)
            );
        }

        bindForUpdate();

        GL15.glBufferSubData(
            GL31.GL_UNIFORM_BUFFER,
            offset,
            data
        );
    }

    @Override
    public void close() {

        if (closed) {
            return;
        }

        if (boundUniformBuffer == bufferId) {
            boundUniformBuffer = 0;
        }

        GL15.glDeleteBuffers(bufferId);

        closed = true;
    }
}