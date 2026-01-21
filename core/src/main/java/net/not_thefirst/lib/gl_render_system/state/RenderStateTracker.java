package net.not_thefirst.lib.gl_render_system.state;

import java.util.HashSet;
import java.util.Set;

public final class RenderStateTracker {

    private static final Set<Class<? extends RenderState>> ACTIVE =
        new HashSet<>();

    private RenderStateTracker() {}

    public static void apply(RenderState state) {
        Class<? extends RenderState> type = state.getClass();
        if (ACTIVE.add(type)) {
            state.apply();
        }
    }

    public static void clear(RenderState state) {
        Class<? extends RenderState> type = state.getClass();
        if (ACTIVE.remove(type)) {
            state.clear();
        }
    }

    public static void reset() {
        ACTIVE.clear();
    }
}
