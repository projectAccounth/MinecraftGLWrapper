package net.not_thefirst.lib.gl_render_system.alt;

public final class RenderParams {
    private final int instanceCount;
    private final int vertexOffset;
    private final int firstIndex;
    private final int firstInstance;

    public RenderParams(int instanceCount, int vertexOffset, int firstIndex, int firstInstance) {
        this.instanceCount = instanceCount;
        this.vertexOffset = vertexOffset;
        this.firstIndex = firstIndex;
        this.firstInstance = firstInstance;
    }

    public RenderParams() {
        this(1, 0, 0, 0);
    }

    public int instanceCount() {
        return this.instanceCount;
    }

    public int vertexOffset() {
        return this.vertexOffset;
    }

    public int firstIndex() {
        return this.firstIndex;
    }

    public int firstInstance() {
        return this.firstInstance;
    }
}

