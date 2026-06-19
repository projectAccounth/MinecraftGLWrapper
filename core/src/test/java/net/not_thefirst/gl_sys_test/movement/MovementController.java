package net.not_thefirst.gl_sys_test.movement;

import org.lwjgl.glfw.GLFW;

import net.not_thefirst.gl_sys_test.camera.Camera;
import net.not_thefirst.gl_sys_test.input.InputHandler;

import org.joml.Vector3f;

public class MovementController {
    private boolean mouseGrabbed = false;
    private final float baseSpeed = 4.0f;
    private final float mouseSensitivity = 0.12f;

    private final Vector3f moveDirection = new Vector3f();

    public void update(long windowHandle, Camera camera, float deltaTime) {
        if (InputHandler.isKeyJustPressed(GLFW.GLFW_KEY_P)) {
            mouseGrabbed = !mouseGrabbed;
            GLFW.glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR, 
                mouseGrabbed ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
        }

        if (mouseGrabbed) {
            float dYaw = (float) (InputHandler.getMouseDeltaX() * mouseSensitivity);
            float dPitch = (float) (-InputHandler.getMouseDeltaY() * mouseSensitivity);
            camera.rotate(dYaw, dPitch);
        }

        float speedMultiplier = InputHandler.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) ? 2.5f : 1.0f;
        float currentSpeed = baseSpeed * speedMultiplier * deltaTime;

        moveDirection.set(0, 0, 0);

        Vector3f horizontalFront = new Vector3f(camera.getFront().x, 0.0f, camera.getFront().z).normalize();
        Vector3f horizontalRight = new Vector3f(camera.getRight().x, 0.0f, camera.getRight().z).normalize();

        if (InputHandler.isKeyDown(GLFW.GLFW_KEY_W)) moveDirection.add(horizontalFront);
        if (InputHandler.isKeyDown(GLFW.GLFW_KEY_S)) moveDirection.sub(horizontalFront);
        if (InputHandler.isKeyDown(GLFW.GLFW_KEY_A)) moveDirection.sub(horizontalRight);
        if (InputHandler.isKeyDown(GLFW.GLFW_KEY_D)) moveDirection.add(horizontalRight);

        if (moveDirection.lengthSquared() > 0) {
            moveDirection.normalize().mul(currentSpeed);
            camera.moveRelative(moveDirection.x, moveDirection.y, moveDirection.z);
        }

        if (InputHandler.isKeyDown(GLFW.GLFW_KEY_SPACE))      camera.moveRelative(0, currentSpeed, 0);
        if (InputHandler.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) camera.moveRelative(0, -currentSpeed, 0);
    }
}
