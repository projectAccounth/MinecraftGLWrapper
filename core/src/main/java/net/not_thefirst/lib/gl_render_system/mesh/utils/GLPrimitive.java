package net.not_thefirst.lib.gl_render_system.mesh.utils;

import org.lwjgl.opengl.GL11;

public enum GLPrimitive {

    TRIANGLES(3, 3) {
        @Override
        public int[] getIndexArray() {
            return new int[]{0, 1, 2};
        }

        @Override
        public int getGLConst() {
            return GL11.GL_TRIANGLES;
        }
    },

    QUADS(4, 6) {
        @Override
        public int[] getIndexArray() {
            return new int[]{0, 1, 3, 1, 2, 3};
        }

        @Override
        public int getGLConst() {
            return GL11.GL_TRIANGLES;
        }
    },

    LINES(2, 2) {
        @Override
        public int[] getIndexArray() {
            return new int[]{0, 1};
        }

        @Override
        public int getGLConst() {
            return GL11.GL_LINES;
        }
    },

    TEST(6, 6) {
        @Override
        public int[] getIndexArray() {
            return new int[]{0, 1, 2, 3, 4, 5};
        }

        @Override
        public int getGLConst() {
            return GL11.GL_LINES;
        }
    };

    private final int vertsPerPrim;
    private final int indsPerPrim;

    GLPrimitive(int vpp, int ipp) {
        this.vertsPerPrim = vpp;
        this.indsPerPrim  = ipp;
    }

    public int verticesPerPrimitive() {
        return vertsPerPrim;
    }

    public int indicesPerPrimitive() {
        return indsPerPrim;
    }

    public static GLPrimitive fromGL(int glEnum) {
        switch (glEnum) {
            case GL11.GL_LINES: return LINES;
            case GL11.GL_TRIANGLES: return TRIANGLES;
            case GL11.GL_QUADS: return QUADS;
            default: throw new IllegalArgumentException("Non-primitive passed");
        }
    }

    public abstract int getGLConst();
    public abstract int[] getIndexArray();
}
