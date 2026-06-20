package net.not_thefirst.lib.gl_render_system.mesh.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public enum GLPrimitive {

    TRIANGLES(3, 3, new int[]{0, 1, 2}, GL11.GL_TRIANGLES),
    QUADS(4, 6, new int[]{0, 1, 3, 1, 2, 3}, GL11.GL_TRIANGLES),
    LINES(2, 2, new int[]{0, 1}, GL11.GL_LINES),
    TEST(6, 6, new int[]{0, 1, 2, 3, 4, 5}, GL11.GL_LINES);

    private final int vertsPerPrim;
    private final int indsPerPrim;
    private final int[] indexArray;
    private final int glConst;

    GLPrimitive(int vpp, int ipp, int[] indexArray, int glConst) {
        if (!isValidCorePrimitive(glConst)) 
            throw new IllegalArgumentException("Invalid Core Profile primitive passed");
        this.vertsPerPrim = vpp;
        this.indsPerPrim  = ipp;
        this.indexArray = indexArray;
        this.glConst = glConst;
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
            default: throw new IllegalArgumentException("Invalid Core Profile primitive passed");
        }
    }

    public static boolean isValidCorePrimitive(int mode) {
        switch (mode) {
            case GL11.GL_POINTS:
            case GL11.GL_LINES:
            case GL11.GL_LINE_LOOP:
            case GL11.GL_LINE_STRIP:
            case GL11.GL_TRIANGLES:
            case GL11.GL_TRIANGLE_STRIP:
            case GL11.GL_TRIANGLE_FAN:
            
            case GL32.GL_LINES_ADJACENCY:
            case GL32.GL_LINE_STRIP_ADJACENCY:
            case GL32.GL_TRIANGLES_ADJACENCY:
            case GL32.GL_TRIANGLE_STRIP_ADJACENCY:
            
            case GL40.GL_PATCHES:
                return true;
                
            default:
                return false;
        }
    }
    public int getGLConst() { return glConst; }
    public int[] getSequentialIndexArray() { return indexArray; }
}
