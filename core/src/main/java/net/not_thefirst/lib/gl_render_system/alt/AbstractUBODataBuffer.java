package net.not_thefirst.lib.gl_render_system.alt;

import org.joml.Matrix4f;


public abstract class AbstractUBODataBuffer<T extends AbstractUBODataBuffer<T, R>, R> implements AutoCloseable {
    protected final String name;
    protected int size;
    protected boolean closed = false;

    protected AbstractUBODataBuffer(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }
    
    public int getSize() {
        return size;
    }
    
    public abstract T putFloat(float v);
    public abstract T putInt(int v);
    public abstract T putVec2(float x, float y);
    public abstract T putVec3(float x, float y, float z);
    public abstract T putVec4(float x, float y, float z, float w);
    public abstract T putIVec4(int x, int y, int z, int w);
    public abstract T putMat4(Matrix4f mat);

    public abstract R build();

    public abstract void reset();

    protected void assertClosed() { if (isClosed()) throw new IllegalStateException("Tried to close a closed buffer"); }
    public boolean isClosed() {
        return closed;
    }
    
    @Override
    public void close() {
        assertClosed();
        closed = true;
    }
}
