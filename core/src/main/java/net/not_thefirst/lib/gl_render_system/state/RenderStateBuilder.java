package net.not_thefirst.lib.gl_render_system.state;

import java.util.ArrayList;
import java.util.List;

public final class RenderStateBuilder {

    private DepthTestState depth = DepthTestState.LEQUAL;
    private BlendState blend = BlendState.NONE;
    private CullState cull = CullState.CULL;
    private MaskState mask = MaskState.COLOR_DEPTH;

    private List<RenderState> extras = new ArrayList<>();

    public RenderStateBuilder depthTest(DepthTestState state) {
        this.depth = state;
        return this;
    }

    public RenderStateBuilder mask(MaskState state) {
        this.mask = state;
        return this;
    }

    public RenderStateBuilder blend(BlendState state) {
        this.blend = state;
        return this;
    }

    public RenderStateBuilder cull(CullState state) {
        this.cull = state;
        return this;
    }

    public RenderStateBuilder addCustom(RenderState state) {
        this.extras.add(state);
        return this;
    }

    public CompositeRenderState build() {
        List<RenderState> all = new ArrayList<>(this.extras);
        all.add(depth);
        all.add(blend);
        all.add(cull);
        all.add(mask);
        return new CompositeRenderState(
            all.toArray(new RenderState[0])
        );
    }
}
