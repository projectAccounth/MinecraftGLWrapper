package net.not_thefirst.lib.gl_render_system.mesh;

import java.util.Arrays;

import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class IndexedBuildingMesh extends BuildingMesh {

    private int[] indices;
    private int indexCount;

    private final IndexPattern pattern;
    private int vertsInPrimitive;

    public IndexedBuildingMesh(
        VertexFormat format,
        int mode,
        IndexPattern pattern
    ) {
        super(format, mode);
        this.pattern = pattern;
        this.indices = new int[1024];
        this.vertsInPrimitive = 0;
    }

    private void ensureIndexCapacity(int additional) {
        int required = indexCount + additional;
        if (required <= indices.length)
            return;

        int newCapacity = indices.length;
        while (newCapacity < required) {
            newCapacity = newCapacity + (newCapacity >> 1);
        }
        indices = Arrays.copyOf(indices, newCapacity);
    }

    @Override
    public IndexedBuildingMesh endVertex() {
        super.endVertex();

        vertsInPrimitive++;

        if (vertsInPrimitive == pattern.verticesPerPrimitive()) {
            int base =
                vertexCount() - pattern.verticesPerPrimitive();

            ensureIndexCapacity(pattern.indicesPerPrimitive());
            pattern.emit(base, indices, indexCount);
            indexCount += pattern.indicesPerPrimitive();

            vertsInPrimitive = 0;
        }

        return this;
    }

    @Override
    public IndexedCompiledMesh compile() {
        if (vertsInPrimitive != 0) {
            throw new IllegalStateException(
                "Incomplete primitive: " + vertsInPrimitive +
                " vertices written, expected " +
                pattern.verticesPerPrimitive()
            );
        }

        CompiledMesh base = super.compile();
        return new IndexedCompiledMesh(
            base.positions(),
            base.normals(),
            base.uvs(),
            base.colors(),
            Arrays.copyOf(indices, indexCount),
            base.vertexCount(),
            base.format(),
            base.mode()
        );
    }

    public enum IndexPattern {

        TRIANGLES(3, 3) {
            @Override
            public void emit(int base, int[] out, int o) {
                out[o]     = base;
                out[o + 1] = base + 1;
                out[o + 2] = base + 2;
            }
        },

        QUADS(4, 6) {
            @Override
            public void emit(int base, int[] out, int o) {
                out[o]     = base;
                out[o + 1] = base + 1;
                out[o + 2] = base + 2;

                out[o + 3] = base;
                out[o + 4] = base + 2;
                out[o + 5] = base + 3;
            }
        },

        LINES(2, 2) {
            @Override
            public void emit(int base, int[] out, int o) {
                out[o]     = base;
                out[o + 1] = base + 1;
            }
        };

        private final int vertsPerPrim;
        private final int indsPerPrim;

        IndexPattern(int vpp, int ipp) {
            this.vertsPerPrim = vpp;
            this.indsPerPrim  = ipp;
        }

        public int verticesPerPrimitive() {
            return vertsPerPrim;
        }

        public int indicesPerPrimitive() {
            return indsPerPrim;
        }

        public abstract void emit(int baseVertex, int[] out, int offset);
    }
}

