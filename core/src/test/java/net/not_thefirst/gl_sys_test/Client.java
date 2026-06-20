package net.not_thefirst.gl_sys_test;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;

import net.not_thefirst.gl_sys_test.camera.Camera;
import net.not_thefirst.gl_sys_test.input.InputHandler;
import net.not_thefirst.gl_sys_test.movement.MovementController;
import net.not_thefirst.gl_sys_test.renderer.MainRenderer;
import net.not_thefirst.gl_sys_test.renderer.objects.TestRenderer;
import net.not_thefirst.gl_sys_test.renderer.pipelines.GLPipelines;
import net.not_thefirst.gl_sys_test.resources.ResourceManager;
import net.not_thefirst.gl_sys_test.utils.Initializer;
import net.not_thefirst.gl_sys_test.window.WindowContext;

public class Client {
    private static int windowWidth = 800;
    private static int windowHeight = 600;
    private static boolean windowResized = false;

    private static GLFWFramebufferSizeCallback fbCallback; 

    private static MainRenderer renderer;
    private static ResourceManager resourceManager;
    private static WindowContext context;

    public static ResourceManager getMainResourceManager() {
        return resourceManager;
    }

    public static WindowContext getWindowContext() {
        return context;
    }

    public static MainRenderer getMainRenderer() {
        return renderer;
    }

    public static void main(String[] args) {

        context = new WindowContext(windowWidth, windowHeight, "");
        context.init();
        long window = context.getWindowHandle();
        InputHandler.init(window);

        resourceManager = new ResourceManager();
        renderer = new MainRenderer(windowWidth, windowHeight);

        GLPipelines.init();
        TestRenderer.test();
        
        Initializer.get().run();
        
        Camera camera = renderer.getCamera();
        MovementController controller = new MovementController();

        fbCallback = GLFWFramebufferSizeCallback.create((windowId, width, height) -> {
            windowWidth = width;
            windowHeight = height;
            windowResized = true;
        });
        GLFW.glfwSetFramebufferSizeCallback(window, fbCallback);

        GLFW.glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long windowId, int width, int height) {
                windowWidth = width;
                windowHeight = height;
                windowResized = true;
            }
        });

        long lastTime = System.nanoTime();

        while (!context.shouldClose()) {
            long now = System.nanoTime();
            float deltaTime = (now - lastTime) / 1_000_000_000.0f;
            lastTime = now;

            if (windowResized) {
                GL11.glViewport(0, 0, windowWidth, windowHeight);
                renderer.onWindowResize(windowWidth, windowHeight);
                windowResized = false;
            }

            context.pollEvents();

            controller.update(window, camera, deltaTime);

            renderer.clearFrame();
            renderer.renderScene();

            InputHandler.postUpdate();
            context.swapBuffers();
            context.postFrame();
        }

        context.destroy();
    }
}
