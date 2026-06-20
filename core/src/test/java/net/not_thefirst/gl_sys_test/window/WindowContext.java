package net.not_thefirst.gl_sys_test.window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryUtil;

public class WindowContext {
    private final int width;
    private final int height;
    private final String title;
    private long windowHandle = MemoryUtil.NULL;

    public WindowContext(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public long getWindowHandle() {
        return this.windowHandle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        windowHandle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle == MemoryUtil.NULL) {
            GLFW.glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();

        GL11.glEnable(GL43.GL_DEBUG_OUTPUT);
        GL11.glEnable(GL43.GL_DEBUG_OUTPUT_SYNCHRONOUS);

        GL43.glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
            if (type == GL43.GL_DEBUG_TYPE_ERROR) {
                String msg = GLDebugMessageCallback.getMessage(length, message);
                System.err.println("OpenGL MSG");
                System.err.println("Message: " + msg);
                
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                
                for (int i = 2; i < stackTrace.length; i++) {
                    System.err.println("\tat " + stackTrace[i]);
                }
            }
        }, 0L);
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowHandle);
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(windowHandle);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    public void postFrame() {
        // ..
    }

    public void destroy() {
        if (windowHandle != MemoryUtil.NULL) {
            GLFW.glfwDestroyWindow(windowHandle);
        }
        GLFW.glfwTerminate();
    }
}
