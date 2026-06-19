package net.not_thefirst.gl_sys_test.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private int screenWidth;
    private int screenHeight;
    private float fov = 70.00f;

    private final Vector3f position = new Vector3f(0.0f, 0.0f, 3.0f);
    private final Vector3f front = new Vector3f(0.0f, 0.0f, -1.0f);
    private final Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
    
    // Auxiliary vectors kept as fields to prevent garbage collection allocations in the render loop
    private final Vector3f right = new Vector3f();
    private final Vector3f worldUp = new Vector3f(0.0f, 1.0f, 0.0f);

    private float yaw = -90.0f;
    private float pitch = 0.0f;

    private final Matrix4f viewMatrix = new Matrix4f();
    private final float[] matrixBuffer = new float[16];

    public Camera(int w, int h) {
        updateVectors();
        this.screenWidth = w;
        this.screenHeight = h;
    }

    public void updateScreenDimensions(int w, int h) {
        this.screenWidth = w;
        this.screenHeight = h;
    }

    private final Matrix4f projMatrix = new Matrix4f();
    private final float[] projBuffer = new float[16];

    /**
     * Generates a 3D Perspective Projection Matrix flattened into a 16-element float array.
     */
    public float[] getProjectionMatrixArray() {
        float aspectRatio = (float) screenWidth / (float) screenHeight;
        float zNear = 0.05f; // Minecraft style near plane clipping
        float zFar = 1000.0f; // Far plane view distance boundary

        projMatrix.identity().perspective((float) Math.toRadians(fov), aspectRatio, zNear, zFar);
        return projMatrix.get(projBuffer);
    }

    public Matrix4f getProjectionMatrix() {
        return projMatrix.set(getProjectionMatrixArray());
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix.set(getViewMatrixArray());
    }

    public void updateVectors() {
        // Calculate direction vector from angles
        float radYaw = (float) Math.toRadians(yaw);
        float radPitch = (float) Math.toRadians(pitch);

        front.x = (float) (Math.cos(radYaw) * Math.cos(radPitch));
        front.y = (float) Math.sin(radPitch);
        front.z = (float) (Math.sin(radYaw) * Math.cos(radPitch));
        front.normalize();

        // Re-calculate Right and Up vectors using JOML cross products
        front.cross(worldUp, right).normalize();
        right.cross(front, up).normalize();
    }

    /**
     * Generates a LookAt matrix and flattens it directly into a 16-element float array.
     */
    public float[] getViewMatrixArray() {
        // target position = current position + look direction vector
        Vector3f target = new Vector3f(position).add(front);
        
        viewMatrix.identity().lookAt(position, target, up);
        return viewMatrix.get(matrixBuffer); // Pours values directly into the flat array buffer
    }

    public void moveRelative(float dx, float dy, float dz) {
        this.position.add(dx, dy, dz);
    }

    public void rotate(float dYaw, float dPitch) {
        this.yaw += dYaw;
        this.pitch += dPitch;

        if (this.pitch > 89.0f) this.pitch = 89.0f;
        if (this.pitch < -89.0f) this.pitch = -89.0f;

        updateVectors();
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getZ() {
        return position.z;
    }

    // Getters for directional offsets
    public Vector3f getFront() { return front; }
    public Vector3f getRight() { return right; }
}
