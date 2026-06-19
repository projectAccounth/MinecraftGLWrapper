package net.not_thefirst.gl_sys_test.input;

import java.util.Arrays;

import org.lwjgl.glfw.GLFW;

public class InputHandler {
    private static final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private static final boolean[] keysJustPressed = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private static final boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
    
    private static double mouseX, mouseY;
    private static double mouseDeltaX, mouseDeltaY;
    private static boolean firstMouse = true;

    public static void init(long windowHandle) {
        // Keyboard tracking
        GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key >= 0 && key < keys.length) {
                boolean isPressed = (action != GLFW.GLFW_RELEASE);
                keysJustPressed[key] = (isPressed && !keys[key]);
                keys[key] = isPressed;
            }
        });

        // Mouse button tracking
        GLFW.glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
            if (button >= 0 && button < mouseButtons.length) {
                mouseButtons[button] = (action != GLFW.GLFW_RELEASE);
            }
        });

        // Framerate-independent mouse delta movement tracking
        GLFW.glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> {
            if (firstMouse) {
                mouseX = xpos;
                mouseY = ypos;
                firstMouse = false;
            }
            mouseDeltaX = xpos - mouseX;
            mouseDeltaY = ypos - mouseY;
            mouseX = xpos;
            mouseY = ypos;
        });

        // Reset state if window loses focus to prevent stuck keys
        GLFW.glfwSetWindowFocusCallback(windowHandle, (window, focused) -> {
            if (!focused) {
                Arrays.fill(keys, false);
                Arrays.fill(keysJustPressed, false);
                Arrays.fill(mouseButtons, false);
                firstMouse = true;
            }
        });
    }

    public static void postUpdate() {
        Arrays.fill(keysJustPressed, false);
        
        mouseDeltaX = 0;
        mouseDeltaY = 0;
    }

    public static boolean isKeyDown(int keyCode) { return keys[keyCode]; }
    public static boolean isKeyJustPressed(int keyCode) { return keysJustPressed[keyCode]; }
    public static boolean isMouseButtonDown(int buttonCode) { return mouseButtons[buttonCode]; }
    public static double getMouseDeltaX() { return mouseDeltaX; }
    public static double getMouseDeltaY() { return mouseDeltaY; }
}
