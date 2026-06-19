package net.not_thefirst.lib.gl_render_system.state;

import java.util.Objects;

import org.lwjgl.opengl.GL11;

public final class FaceCullState implements RenderState {

    private final int side;

    public static final FaceCullState FRONT = new FaceCullState(GL11.GL_FRONT);
    public static final FaceCullState BACK = new FaceCullState(GL11.GL_BACK);

    private FaceCullState(int side) {
        this.side = side;
    }

    @Override
    public void apply() {
        GL11.glCullFace(side);
    }

    @Override
    public void clear() {
        GL11.glCullFace(GL11.GL_BACK);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CullState)) return false;
        return ((FaceCullState) o).side == this.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(side);
    }
}
