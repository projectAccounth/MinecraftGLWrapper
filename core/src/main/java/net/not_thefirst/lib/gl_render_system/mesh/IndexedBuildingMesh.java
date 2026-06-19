package net.not_thefirst.lib.gl_render_system.mesh;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class IndexedBuildingMesh extends BuildingMesh {

    private int[] vertexTracker;
    private int trackerCount;

    private final IndexPattern pattern;
    private int vertsInPrimitive;

    public IndexedBuildingMesh(
        VertexFormat format,
        GLPrimitive primitive
    ) {
        super(format, primitive);
        this.pattern = IndexPattern.fromType(primitive);
        this.vertexTracker = new int[1024];
        this.vertsInPrimitive = 0;
        this.trackerCount = 0;
    }

    private void ensureTrackerCapacity(int additional) {
        int required = trackerCount + additional;
        if (required <= vertexTracker.length)
            return;

        int newCapacity = vertexTracker.length;
        while (newCapacity < required) {
            newCapacity = newCapacity + (newCapacity >> 1);
        }
        vertexTracker = Arrays.copyOf(vertexTracker, newCapacity);
    }

    @Override
    public IndexedBuildingMesh endVertex() {
        super.endVertex();

        vertsInPrimitive++;

        if (vertsInPrimitive == pattern.verticesPerPrimitive()) {
            int base = vertexCount() - pattern.verticesPerPrimitive();

            ensureTrackerCapacity(pattern.indicesPerPrimitive());
            pattern.emit(base, vertexTracker, trackerCount);
            trackerCount += pattern.indicesPerPrimitive();

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
            primitive.getIndexArray(),
            pattern.indsPerPrim,
            base.vertexCount(),
            base.format(),
            base.primitive()
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

                out[o + 3] = base + 2;
                out[o + 4] = base + 3;
                out[o + 5] = base;
            }
        },

        LINES(2, 2) {
            @Override
            public void emit(int base, int[] out, int o) {
                out[o]     = base;
                out[o + 1] = base + 1;
            }
        },

        TEST(6, 6) {
            @Override
            public void emit(int base, int[] out, int o) {
                out[o]     = base;
                out[o + 1] = base + 1;
                out[o + 2] = base + 2;
                out[o + 3] = base + 3;
                out[o + 4] = base + 4;
                out[o + 5] = base + 5;
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

        public static IndexPattern fromGL(int glEnum) {
            switch (glEnum) {
                case GL11.GL_LINES: return LINES;
                case GL11.GL_TRIANGLES: return TRIANGLES;
                case GL11.GL_QUADS: return QUADS;
                default: throw new IllegalArgumentException("Non-primitive passed");
            }
        }

        public static IndexPattern fromType(GLPrimitive primitive) {
            switch (primitive) {
                case LINES: return LINES;
                case TRIANGLES: return TRIANGLES;
                case QUADS: return QUADS;
                case TEST: return TEST;
                default: throw new IllegalArgumentException("Unknown passed");
            }
        }

        public abstract void emit(int baseVertex, int[] out, int offset);
    }
}
