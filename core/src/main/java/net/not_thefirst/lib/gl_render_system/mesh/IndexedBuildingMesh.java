package net.not_thefirst.lib.gl_render_system.mesh;

import java.util.Arrays;

import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class IndexedBuildingMesh extends BuildingMesh {

    private int vertsInPrimitive;
    private int[] indices;
    private int currentIndexCount;

    public IndexedBuildingMesh(
        VertexFormat format,
        GLPrimitive primitive
    ) {
        super(format, primitive);
        this.vertsInPrimitive = 0;
        this.indices = new int[32 * primitive.indicesPerPrimitive()];
        this.currentIndexCount = 0;
    }

    @Override
    protected void endLastVertex() {
        boolean completedVertex = vertexOpen;

        super.endLastVertex();

        if (!completedVertex)
            return;

        vertsInPrimitive++;

        if (vertsInPrimitive == primitive.verticesPerPrimitive()) {
            int primitiveBaseVertex =
                vertexCount() - primitive.verticesPerPrimitive();

            int[] pattern = primitive.getSequentialIndexArray();
            ensureIndexCapacity(pattern.length);

            for (int i = 0; i < pattern.length; i++) {
                indices[currentIndexCount++] =
                    primitiveBaseVertex + pattern[i];
            }

            vertsInPrimitive = 0;
        }
    }

    @Override
    public IndexedCompiledMesh compile() {
        endLastVertex();
        if (vertsInPrimitive != 0) {
            throw new IllegalStateException(
                "Incomplete primitive: " + vertsInPrimitive +
                " vertices written, expected " +
                primitive.verticesPerPrimitive()
            );
        }

        int count = currentIndex;

        float[] finalPositions = Arrays.copyOf(positions, count * 3);
        float[] finalNormals   = Arrays.copyOf(normals,   count * 3);
        float[] finalUvs       = Arrays.copyOf(uvs,       count * 2);
        int[]   finalColors    = Arrays.copyOf(colors,    count);
        int[]   finalIndices   = Arrays.copyOf(indices, currentIndexCount);

        return new IndexedCompiledMesh(
            finalPositions,
            finalNormals,
            finalUvs,
            finalColors,
            finalIndices,
            count,
            format,
            primitive,
            currentIndexCount
        );
    }

    @Override
    public void reset() {
        super.reset();
        this.vertsInPrimitive = 0;
        this.currentIndexCount = 0;
    }

    private void ensureIndexCapacity(int additionalElements) {
        if (currentIndexCount + additionalElements > indices.length) {
            int newLength = Math.max(indices.length * 2, currentIndexCount + additionalElements);
            indices = Arrays.copyOf(indices, newLength);
        }
    }
}
