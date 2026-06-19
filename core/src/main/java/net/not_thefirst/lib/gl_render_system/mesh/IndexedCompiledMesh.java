package net.not_thefirst.lib.gl_render_system.mesh;

import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class IndexedCompiledMesh extends CompiledMesh {
    private final int[] indices;
    private final int indexCount;

    public IndexedCompiledMesh(
        float[] positions,
        float[] normals,
        float[] uvs,
        int[] colors,
        int[] indices,
        int vertexCount,
        int indexCount,
        VertexFormat format,
        GLPrimitive primitive
    ) {
        super(positions, normals, uvs, colors, vertexCount, format, primitive);
        this.indices   = indices;
        this.indexCount = indexCount;
    }

    public int[] indices()     { return indices; }
    public int indexCount()    { return indexCount; }
}
