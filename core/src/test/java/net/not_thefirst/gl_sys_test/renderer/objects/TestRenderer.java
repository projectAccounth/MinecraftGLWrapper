package net.not_thefirst.gl_sys_test.renderer.objects;

import net.not_thefirst.gl_sys_test.Client;
import net.not_thefirst.gl_sys_test.camera.Camera;
import net.not_thefirst.gl_sys_test.render.passes.GLMesh;
import net.not_thefirst.gl_sys_test.render.passes.GLRenderPass;
import net.not_thefirst.gl_sys_test.renderer.GLGraphicsContext;
import net.not_thefirst.gl_sys_test.renderer.MainRenderer;
import net.not_thefirst.gl_sys_test.renderer.pipelines.GLPipelines;
import net.not_thefirst.gl_sys_test.utils.Initializer;
import net.not_thefirst.lib.gl_render_system.alt.AbstractStaticMesh;
import net.not_thefirst.lib.gl_render_system.alt.AbstractUBODataBuffer;
import net.not_thefirst.lib.gl_render_system.alt.PipelineManager;
import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.shader.Std140SizeCalculator;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;
import net.not_thefirst.lib.utils.math.ARGB;

public class TestRenderer {
    private static GLMesh mesh = null;
    private static GLRenderPass pass;

    static int faceColor1 = ARGB.colorFromFloat(1, 0.5f, 0.5f, 0.5f);
    static int faceColor2 = ARGB.colorFromFloat(1, 0.6f, 0.5f, 0.5f);
    static int faceColor3 = ARGB.colorFromFloat(1, 0.5f, 0.7f, 0.5f);
    static int faceColor4 = ARGB.colorFromFloat(1, 0.5f, 0.5f, 0.8f);
    static int faceColor5 = ARGB.colorFromFloat(1, 0.5f, 0.1f, 0.5f);
    static int faceColor6 = ARGB.colorFromFloat(1, 0.0f, 0.5f, 0.5f);

    private TestRenderer() {}

    public static void test() {
        AbstractStaticMesh.Builder<?, ?> builder = 
            new GLMesh.Builder(
                VertexFormat.POSITION_COLOR, 
                GLPrimitive.TRIANGLES);

        float s = 1;

        builder.addVertex(0, 0, s).setColor(faceColor1);
        builder.addVertex(s, 0, s).setColor(faceColor1);
        builder.addVertex(s, s, s).setColor(faceColor1);
        builder.addVertex(0, s, s).setColor(faceColor1);

        builder.addVertex(s, 0, 0).setColor(faceColor2);
        builder.addVertex(0, 0, 0).setColor(faceColor2);
        builder.addVertex(0, s, 0).setColor(faceColor2);
        builder.addVertex(s, s, 0).setColor(faceColor2);

        builder.addVertex(0, s, s).setColor(faceColor3);
        builder.addVertex(s, s, s).setColor(faceColor3);
        builder.addVertex(s, s, 0).setColor(faceColor3);
        builder.addVertex(0, s, 0).setColor(faceColor3);

        builder.addVertex(0, 0, 0).setColor(faceColor4);
        builder.addVertex(s, 0, 0).setColor(faceColor4);
        builder.addVertex(s, 0, s).setColor(faceColor4);
        builder.addVertex(0, 0, s).setColor(faceColor4);

        builder.addVertex(s, 0, s).setColor(faceColor5);
        builder.addVertex(s, 0, 0).setColor(faceColor5);
        builder.addVertex(s, s, 0).setColor(faceColor5);
        builder.addVertex(s, s, s).setColor(faceColor5);

        builder.addVertex(0, 0, 0).setColor(faceColor6);
        builder.addVertex(0, 0, s).setColor(faceColor6);
        builder.addVertex(0, s, s).setColor(faceColor6);
        builder.addVertex(0, s, 0).setColor(faceColor6);
                
        mesh = (GLMesh) builder.build();

        int bufSize = new Std140SizeCalculator().addMat4().addMat4().finish().offset();

        Initializer.get().registerTask("bind", () -> {
            MainRenderer renderer = Client.getMainRenderer();
            renderer.addPreFrameTask("push pass", () -> {
                AbstractUBODataBuffer<?, ?> buf = 
                    PipelineManager.getInstance().createDataBuffer("Transforms", bufSize);
                Camera cam = Client.getMainRenderer().getCamera();

                pass = new GLRenderPass("test", GLPipelines.UNNAMED);
                pass.setMesh(mesh, mesh.getIndexCount());

                buf.putMat4(cam.getProjectionMatrix());
                buf.putMat4(cam.getViewMatrix());

                pass.bindUniformBlock("Transforms", buf);

                GLGraphicsContext ctx = renderer.getCurrentContext();
                ctx.submit(pass);
            });

            renderer.addPostFrameTask("nuke pass", () -> {
                pass.close();
            });
        });
    }
}
