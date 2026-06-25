package net.not_thefirst.lib.gl_render_system.shader;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.joml.Matrix4f;
public final class Std140BufferBuilder implements Closeable {

    private final ByteBuffer buffer;
    private final float[] matrixCache = new float[16];
    private boolean closed = false;

    /**
     * Allocate a single scratchpad buffer once at startup.
     * Give it enough size to hold the largest single UBO structure in your engine (e.g., 1KB).
     */
    public Std140BufferBuilder(int initialSize) {
        this.buffer = ByteBuffer.allocateDirect(initialSize);
        this.buffer.order(ByteOrder.nativeOrder());
    }

    public Std140BufferBuilder reset() {
        buffer.clear();
        return this;
    }

    private void align(int align) {
        int pos = buffer.position();
        int aligned = (pos + align - 1) & ~(align - 1);
        buffer.position(aligned);
    }

    public Std140BufferBuilder putFloat(float v) {
        align(4);
        buffer.putFloat(v);
        return this;
    }

    public Std140BufferBuilder putInt(int v) {
        align(4);
        buffer.putInt(v);
        return this;
    }

    public Std140BufferBuilder putVec2(float x, float y) {
        align(8);
        buffer.putFloat(x);
        buffer.putFloat(y);
        return this;
    }

    public Std140BufferBuilder putVec3(float x, float y, float z) {
        align(16);
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
        buffer.putFloat(0.0f); // std140 pad
        return this;
    }

    public Std140BufferBuilder putVec4(float x, float y, float z, float w) {
        align(16);
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
        buffer.putFloat(w);
        return this;
    }

    public Std140BufferBuilder putIVec4(int x, int y, int z, int w) {
        align(16);
        buffer.putInt(x);
        buffer.putInt(y);
        buffer.putInt(z);
        buffer.putInt(w);
        return this;
    }

    public Std140BufferBuilder putMat4(Matrix4f mat) {
        align(16);
        mat.get(matrixCache);
        for (int i = 0; i < 16; i++) {
            buffer.putFloat(matrixCache[i]);
        }
        return this;
    }

    /**
     * Prepares the internal buffer to be read by the OpenGL driver.
     * @return The internal raw ByteBuffer, flipped and ready.
     */
    public ByteBuffer build() {
        buffer.flip();
        return buffer;
    }

    @Override
    public void close() {
        if (isClosed()) throw new IllegalStateException("Tried to close a closed buffer");
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }
}
