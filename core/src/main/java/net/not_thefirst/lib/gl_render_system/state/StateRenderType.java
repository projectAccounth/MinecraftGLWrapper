package net.not_thefirst.lib.gl_render_system.state;

public final class StateRenderType extends RenderType {

    public StateRenderType(String name, CompositeRenderState state) {
        super(name, state);
    }

    public void run(Runnable drawCall) {
        setup();
        drawCall.run();
        clear();
    }
}
