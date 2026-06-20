package net.not_thefirst.gl_sys_test.renderer;

import org.lwjgl.opengl.GL11;

import net.not_thefirst.gl_sys_test.Client;
import net.not_thefirst.gl_sys_test.camera.Camera;
import net.not_thefirst.gl_sys_test.render.factories.GLRenderPassFactory;
import net.not_thefirst.gl_sys_test.render.factories.GLUBODataBufferFactory;
import net.not_thefirst.gl_sys_test.render.gl.GLResourceHandler;
import net.not_thefirst.gl_sys_test.resources.ResourceManager;
import net.not_thefirst.gl_sys_test.utils.Initializer;
import net.not_thefirst.gl_sys_test.utils.TaskRunner;
import net.not_thefirst.lib.gl_render_system.alt.PipelineManager;
import net.not_thefirst.lib.gl_render_system.alt.PipelineManager.PipelineProvider;
import net.not_thefirst.lib.gl_render_system.frame.GLDepthFramebuffer;
import net.not_thefirst.lib.gl_render_system.frame.GLScreenBuffer;
import net.not_thefirst.lib.gl_render_system.target.GLRenderTarget;
import net.not_thefirst.lib.utils.math.ARGB;

public class MainRenderer implements IRenderer {

    private Camera camera;
    private ResourceManager resourceManager;

    private TaskRunner preFrameTasks = new TaskRunner("pre-frame");
    private TaskRunner postFrameTasks = new TaskRunner("post-frame");
    
    private GLGraphicsContext graphicsContext;

    // currently unused
    private GLDepthFramebuffer depthPassBuffer;
    private GLScreenBuffer screenBuffer;

    public MainRenderer(int initialWidth, int initialHeight) {
        this.camera = new Camera(initialWidth, initialHeight);
        this.resourceManager = Client.getMainResourceManager();

        GLResourceHandler.init(resourceManager);
        
        this.graphicsContext = new GLGraphicsContext();
        this.depthPassBuffer = new GLDepthFramebuffer(initialWidth, initialHeight);
        this.screenBuffer    = new GLScreenBuffer(initialWidth, initialHeight);
    }

    public void addPreFrameTask(String name, Runnable task) {
        preFrameTasks.registerTask(name, task);
    }

    public void addPostFrameTask(String name, Runnable task) {
        postFrameTasks.registerTask(name, task);
    }

    public void clearFrame() {
        graphicsContext.clearColor(ARGB.BLACK);
        graphicsContext.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void renderScene() {
        preFrameTasks.run();

        graphicsContext.processPasses();
        graphicsContext.flushPasses();

        postFrameTasks.run();
    }

    public void onWindowResize(int newWidth, int newHeight) {
        if (depthPassBuffer != null) {
            depthPassBuffer.close();
        }
        if (screenBuffer != null) {
            screenBuffer.close();
        }
        depthPassBuffer = new GLDepthFramebuffer(newWidth, newHeight);
        screenBuffer = new GLScreenBuffer(newWidth, newHeight);
        camera.updateScreenDimensions(newWidth, newHeight);
    }

    public GLRenderTarget getDepthRenderTarget() {
        return this.depthPassBuffer;
    }

    public GLRenderTarget getMainRenderTarget() {
        return this.screenBuffer;
    }

    public GLGraphicsContext getCurrentContext() {
        return this.graphicsContext;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }
}
