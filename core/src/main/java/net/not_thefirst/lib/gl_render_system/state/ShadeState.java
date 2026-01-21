package net.not_thefirst.lib.gl_render_system.state;

import org.lwjgl.opengl.GL11;

/**
 * Shade state for OpenGL.
 * @deprecated Removed in core profile. Shouldn't be used.
 */
@Deprecated(forRemoval = true)
public class ShadeState implements RenderState {

    public static final ShadeState SMOOTH = new ShadeState(GL11.GL_SMOOTH);
    public static final ShadeState FLAT = new ShadeState(GL11.GL_FLAT);

    private final int type;

    @Deprecated
    public ShadeState(int type) {
        this.type = type;
    }

    @Override
    public void apply() {
        // no-op
    }

    @Override
    public void clear() {
        // no-op
    }
}
