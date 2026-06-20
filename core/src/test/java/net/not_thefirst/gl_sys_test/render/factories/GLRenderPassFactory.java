package net.not_thefirst.gl_sys_test.render.factories;

import net.not_thefirst.gl_sys_test.render.passes.GLPipeline;
import net.not_thefirst.gl_sys_test.render.passes.GLRenderPass;
import net.not_thefirst.lib.gl_render_system.alt.AbstractPipeline;
import net.not_thefirst.lib.gl_render_system.alt.PipelineManager.RenderPassFactory;

public class GLRenderPassFactory implements RenderPassFactory<GLPipeline, GLRenderPass> {
    public GLRenderPassFactory() { /* */}
    
    @Override
    public Class<GLPipeline> getPipelineClass() {
        return GLPipeline.class;
    }
    
    @Override
    public GLRenderPass create(String name, AbstractPipeline[] pipelines) {
        verifyPipelines(pipelines);
        return new GLRenderPass(name, (GLPipeline[]) pipelines);
    }
}