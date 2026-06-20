package net.not_thefirst.lib.gl_render_system.mesh;

import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class IndexedCompiledMesh extends CompiledMesh {
    public IndexedCompiledMesh(
        float[] positions,
        float[] normals,
        float[] uvs,
        int[] colors,
        int vertexCount,
        VertexFormat format,
        GLPrimitive primitive
    ) {
        super(positions, normals, uvs, colors, vertexCount, format, primitive);
    }

    public int[] indices()     { return primitive.getIndexArray(); }
    public int indexCount()    { return indices().length; }
}
