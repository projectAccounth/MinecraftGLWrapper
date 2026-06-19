package net.not_thefirst.lib.gl_render_system.alt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRenderPass<T extends AbstractPipeline> implements AutoCloseable {
    protected final String name;
    protected final List<T> pipelines;
    protected final Map<String, UniformValue> uniforms = new HashMap<>();
    protected AbstractStaticMesh<?, ?> meshData;
    protected int indexCount;
    protected boolean indexed;

    protected boolean open = true;

    protected AbstractRenderPass(String name, T[] pipelines) {
        this.name = name;
        this.pipelines = Arrays.asList(pipelines);
    }

    public String getName() {
        return name;
    }

    public List<T> getPipelines() {
        return pipelines;
    }

    public void setup() {
        if (isClosed()) {
            throw new IllegalStateException("Tried to setup a closed pass");
        }
    }
    
    public void render() {
        if (isClosed()) {
            throw new IllegalStateException("Tried to render a closed pass");
        }
    }

    public void cleanup() {
        if (isClosed()) {
            throw new IllegalStateException("Tried to close a closed pass");
        }
    }

    public abstract void setMesh(AbstractStaticMesh<?, ?> mesh, int indexCount);
    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public boolean isClosed() {
        return !open;
    }

    public AbstractStaticMesh<?, ?> getMesh() {
        return meshData;
    }

    public int getIndexCount() {
        return indexCount;
    }

    @Override
    public void close() {
        cleanup();
    }

    public void setUniform1i(String name, int value) {
        uniforms.put(name, new UniformValue(
            UniformType.INT1,
            value
        ));
    }

    public void setUniform2i(String name, int x, int y) {
        uniforms.put(name, new UniformValue(
            UniformType.INT2,
            new int[]{x, y}
        ));
    }

    public void setUniform3i(String name, int x, int y, int z) {
        uniforms.put(name, new UniformValue(
            UniformType.INT3,
            new int[]{x, y, z}
        ));
    }

    public void setUniform4i(String name, int x, int y, int z, int w) {
        uniforms.put(name, new UniformValue(
            UniformType.INT4,
            new int[]{x, y, z, w}
        ));
    }

    public void setUniform1f(String name, float value) {
        uniforms.put(name, new UniformValue(
            UniformType.FLOAT1,
            value
        ));
    }

    public void setUniform2f(String name, float x, float y) {
        uniforms.put(name, new UniformValue(
            UniformType.FLOAT2,
            new float[]{x, y}
        ));
    }

    public void setUniform3f(String name, float x, float y, float z) {
        uniforms.put(name, new UniformValue(
            UniformType.FLOAT3,
            new float[]{x, y, z}
        ));
    }

    public void setUniform4f(String name, float x, float y, float z, float w) {
        uniforms.put(name, new UniformValue(
            UniformType.FLOAT4,
            new float[]{x, y, z, w}
        ));
    }

    public void setUniformMatrix3f(String name, float[] matrix) {
        uniforms.put(name, new UniformValue(
            UniformType.MAT3,
            matrix
        ));
    }

    public void setUniformMatrix4f(String name, float[] matrix) {
        uniforms.put(name, new UniformValue(
            UniformType.MAT4,
            matrix
        ));
    }

    public abstract void bindTexture(String name, int textureId, int slot);
    public abstract void bindTextureArray(String name, int textureId, int slot);

    public abstract void bindUniformBlock(String name, AbstractUBODataBuffer<?, ?> ubo);
}
