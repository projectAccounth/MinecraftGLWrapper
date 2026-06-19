package net.not_thefirst.lib.gl_render_system.mesh;

import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public class CompiledMesh {
    final float[] positions;
    final float[] normals;
    final float[] uvs;
    final int[] colors;
    final int vertexCount;

    final VertexFormat format;
    final GLPrimitive primitive;

    CompiledMesh(
        float[] positions,
        float[] normals,
        float[] uvs,
        int[] colors,
        int vertexCount,
        VertexFormat format,
        GLPrimitive primitive
    ) {
        this.positions = positions;
        this.normals = normals;
        this.uvs = uvs;
        this.colors = colors;
        this.vertexCount = vertexCount;
        this.format = format;
        this.primitive = primitive;
    }

    public GLPrimitive primitive() { return this.primitive; }
    public VertexFormat format() { return this.format; }
    public float[] positions() { return this.positions; }
    public float[] normals() { return this.normals; }
    public float[] uvs() { return this.uvs; }
    public int[] colors() { return this.colors; }
    public int vertexCount() { return this.vertexCount; }
}
