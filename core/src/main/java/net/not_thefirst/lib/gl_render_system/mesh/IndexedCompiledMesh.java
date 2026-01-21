package net.not_thefirst.lib.gl_render_system.mesh;

import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class IndexedCompiledMesh extends CompiledMesh {
    private final int[] indices;

    public IndexedCompiledMesh(
        float[] positions,
        float[] normals,
        float[] uvs,
        int[] colors,
        int[] indices,
        int vertexCount,
        VertexFormat format,
        int mode
    ) {
        super(positions, normals, uvs, colors, vertexCount, format, mode);
        this.indices   = indices;
    }

    public int[] indices()     { return indices; }
    public int indexCount()    { return indices.length; }
}
