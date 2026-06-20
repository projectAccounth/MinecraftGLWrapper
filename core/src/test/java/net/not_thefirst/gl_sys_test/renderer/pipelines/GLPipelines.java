package net.not_thefirst.gl_sys_test.renderer.pipelines;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.not_thefirst.gl_sys_test.render.gl.GLPipeline;
import net.not_thefirst.gl_sys_test.utils.Initializer;
import net.not_thefirst.lib.gl_render_system.state.BlendState;
import net.not_thefirst.lib.gl_render_system.state.CullState;
import net.not_thefirst.lib.gl_render_system.state.DepthTestState;
import net.not_thefirst.lib.gl_render_system.state.FaceCullState;
import net.not_thefirst.lib.gl_render_system.state.MaskState;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public class GLPipelines {
    private GLPipelines() {}

    public static final GLPipeline UNNAMED = 
        new GLPipeline.Builder("UNNAMED")
            .withBlendState(BlendState.TRANSLUCENT)
            .withCullState(CullState.CULL)
            .withDepthTestState(DepthTestState.LEQUAL)
            .withFragmentShader("default.frag")
            .withVertexShader("default.vert")
            .withId("default")
            .withVertexFormat(VertexFormat.POSITION_COLOR)
            .withFaceCull(FaceCullState.BACK)
            .withUniformBlocks(Stream.of(
                new AbstractMap.SimpleEntry<>("Transforms", 0)
            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
            .build();

    public static final GLPipeline UNNAMED_ND = 
        new GLPipeline.Builder("UNNAMED_ND")
            .withBlendState(BlendState.TRANSLUCENT)
            .withCullState(CullState.CULL)
            .withDepthTestState(DepthTestState.LEQUAL)
            .withMaskState(MaskState.DEPTH_ONLY)
            .withFragmentShader("default.frag")
            .withVertexShader("default.vert")
            .withId("default_nd")
            .withVertexFormat(VertexFormat.POSITION_COLOR)
            .withFaceCull(FaceCullState.BACK)
            .withUniformBlocks(Stream.of(
                new AbstractMap.SimpleEntry<>("Transforms", 0)
            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
            .build();

    public static void init() {
        Initializer.get().registerTask("register pipelines", () -> {
            UNNAMED.setup();
            UNNAMED_ND.setup();
            System.out.println("initialized pipelines");
        });
    }
}
