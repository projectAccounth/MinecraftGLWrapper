package net.not_thefirst.lib.gl_render_system.state;

import org.lwjgl.opengl.GL11;

/**
 * Shade state for OpenGL.
 * @deprecated Removed in core profile. Shouldn't be used.
 */
@Deprecated
public class ShadeState implements RenderState {

    public static final ShadeState SMOOTH = new ShadeState(GL11.GL_SMOOTH);
    public static final ShadeState FLAT = new ShadeState(GL11.GL_FLAT);
    private static boolean isUsed = false;

    @SuppressWarnings("unused")
    private final int type;

    @Deprecated
    public ShadeState(int type) {
        this.type = type;
    }

    @Deprecated
    public static void markUsed() {
        isUsed = true;
    }

    @Deprecated
    public static void markUnused() {
        isUsed = false;
    }

    @Deprecated
    public static boolean isUsed() {
        return isUsed;
    }

    @Deprecated
    @Override
    public void apply() {
        if (isUsed) {
            GL11.glShadeModel(type);
        }
    }

    @Deprecated
    @Override
    public void clear() {
        // no-op
    }
}
