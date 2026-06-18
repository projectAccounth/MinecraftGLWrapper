package net.not_thefirst.lib.gl_render_system.state;

import java.util.Objects;

import org.lwjgl.opengl.GL11;

public final class DepthTestState implements RenderState {

    private final int func;

    public static final DepthTestState LEQUAL =
        new DepthTestState(GL11.GL_LEQUAL);

    public static final DepthTestState GEQUAL =
        new DepthTestState(GL11.GL_GEQUAL);

    public static final DepthTestState ALWAYS =
        new DepthTestState(GL11.GL_ALWAYS);

    public static final DepthTestState NEVER =
        new DepthTestState(GL11.GL_NEVER);

    public static final DepthTestState GREATER =
        new DepthTestState(GL11.GL_GREATER);

    public static final DepthTestState LESS =
        new DepthTestState(GL11.GL_LESS);
    
    public static final DepthTestState EQUALS =
        new DepthTestState(GL11.GL_EQUAL);

    public DepthTestState(int func) {
        this.func = func;
    }

    @Override
    public void apply() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(func);
    }

    @Override
    public void clear() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DepthTestState)) return false;
        return ((DepthTestState) o).func == this.func;
    }

    @Override
    public int hashCode() {
        return Objects.hash(func);
    }
}
