package net.not_thefirst.lib.gl_render_system.state;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

/**
 * A utility class that captures the current OpenGL state upon instantiation and restores it when closed.
 * This is particularly useful for ensuring that temporary changes to the OpenGL state do not affect other parts of the application.
 * <p>
 * Usage example:
 * <pre>
 * try (GLStateGuard guard = new GLStateGuard()) {
 *     // Perform OpenGL operations that modify the state
 * }
 * // After the try block, the OpenGL state is automatically restored to its previous configuration.
 * </pre>
 * <p>
 * Note: This class captures a subset of the OpenGL state. Depending on the application's needs, additional state variables may need to be captured and restored.
 */
public final class GLStateGuard implements AutoCloseable {

    private final int program;

    private final int vao;
    private final int arrayBuffer;
    private final int elementArrayBuffer;

    private final boolean depthTest;
    private final int depthFunc;
    private final boolean depthMask;

    private final boolean cullFace;
    private final int cullMode;
    private final int frontFace;

    private final boolean blend;
    private final int blendSrcRGB;
    private final int blendDstRGB;
    private final int blendSrcAlpha;
    private final int blendDstAlpha;

    private final boolean colorR;
    private final boolean colorG;
    private final boolean colorB;
    private final boolean colorA;
    
    private final int[] viewport = new int[4];

    private final boolean scissorTest;
    private final int[] scissorBox = new int[4];

    private final int activeTexture;
    private final int boundTexture2D;
    private final int samplerBinding;

    public GLStateGuard() {

        // Program
        program = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);

        // VAO && buffers
        vao = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        arrayBuffer = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
        elementArrayBuffer = GL11.glGetInteger(GL15.GL_ELEMENT_ARRAY_BUFFER_BINDING);

        // Depth
        depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        depthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
        depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);

        // Culling
        cullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        cullMode = GL11.glGetInteger(GL11.GL_CULL_FACE_MODE);
        frontFace = GL11.glGetInteger(GL11.GL_FRONT_FACE);

        // Blending
        blend = GL11.glIsEnabled(GL11.GL_BLEND);
        blendSrcRGB = GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB);
        blendDstRGB = GL11.glGetInteger(GL14.GL_BLEND_DST_RGB);
        blendSrcAlpha = GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA);
        blendDstAlpha = GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA);

        // Color mask
        colorR = GL11.glGetBoolean(GL11.GL_COLOR_WRITEMASK);
        colorG = GL11.glGetBoolean(GL11.GL_COLOR_WRITEMASK + 1);
        colorB = GL11.glGetBoolean(GL11.GL_COLOR_WRITEMASK + 2);
        colorA = GL11.glGetBoolean(GL11.GL_COLOR_WRITEMASK + 3);

        // Viewport
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);

        // Scissor
        scissorTest = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
        GL11.glGetIntegerv(GL11.GL_SCISSOR_BOX, scissorBox);

        // Texture && sampler
        activeTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        boundTexture2D = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        samplerBinding = GL11.glGetInteger(GL33.GL_SAMPLER_BINDING);
    }

    @Override
    public void close() {

        // Program
        GL20.glUseProgram(program);

        // VAO && buffers
        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, arrayBuffer);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer);

        // Depth
        set(GL11.GL_DEPTH_TEST, depthTest);
        GL11.glDepthFunc(depthFunc);
        GL11.glDepthMask(depthMask);

        // Culling
        set(GL11.GL_CULL_FACE, cullFace);
        GL11.glCullFace(cullMode);
        GL11.glFrontFace(frontFace);

        // Blending
        set(GL11.GL_BLEND, blend);
        GL14.glBlendFuncSeparate(
            blendSrcRGB, blendDstRGB,
            blendSrcAlpha, blendDstAlpha
        );

        // Color mask
        GL11.glColorMask(colorR, colorG, colorB, colorA);

        // Viewport
        GL11.glViewport(viewport[0], viewport[1], viewport[2], viewport[3]);

        // Scissor
        set(GL11.GL_SCISSOR_TEST, scissorTest);
        GL11.glScissor(
            scissorBox[0],
            scissorBox[1],
            scissorBox[2],
            scissorBox[3]
        );

        // Texture / sampler
        GL13.glActiveTexture(activeTexture);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, boundTexture2D);
        GL33.glBindSampler(0, samplerBinding);
    }

    private static void set(int cap, boolean enabled) {
        if (enabled) GL11.glEnable(cap);
        else GL11.glDisable(cap);
    }
}