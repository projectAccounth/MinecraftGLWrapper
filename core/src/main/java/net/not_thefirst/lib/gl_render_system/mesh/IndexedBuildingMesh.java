package net.not_thefirst.lib.gl_render_system.mesh;

import java.util.Arrays;

import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class IndexedBuildingMesh extends BuildingMesh {

    private int vertsInPrimitive;

    public IndexedBuildingMesh(
        VertexFormat format,
        GLPrimitive primitive
    ) {
        super(format, primitive);
        this.vertsInPrimitive = 0;
    }

    @Override
    public IndexedBuildingMesh endVertex() {
        super.endVertex();

        vertsInPrimitive++;
        vertsInPrimitive %= primitive.verticesPerPrimitive();

        return this;
    }

    @Override
    public IndexedCompiledMesh compile() {
        if (vertsInPrimitive != 0) {
            throw new IllegalStateException(
                "Incomplete primitive: " + vertsInPrimitive +
                " vertices written, expected " +
                primitive.verticesPerPrimitive()
            );
        }
        beginVert();
        
        int count = currentIndex;

        float[] finalPositions = Arrays.copyOf(positions, count * 3);
        float[] finalNormals   = Arrays.copyOf(normals,   count * 3);
        float[] finalUvs       = Arrays.copyOf(uvs,       count * 2);
        int[]   finalColors    = Arrays.copyOf(colors,    count);
        return new IndexedCompiledMesh(
            finalPositions,
            finalNormals,
            finalUvs,
            finalColors,
            count,
            format,
            primitive
        );
    }
}
