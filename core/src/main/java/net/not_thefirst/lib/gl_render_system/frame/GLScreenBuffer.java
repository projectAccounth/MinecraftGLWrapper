package net.not_thefirst.lib.gl_render_system.frame;

import static org.lwjgl.opengl.GL30.*;

import net.not_thefirst.lib.gl_render_system.target.GLRenderTarget;

// main framebuffer wrapper
public class GLScreenBuffer implements GLRenderTarget {
    private int width;
    private int height;

    public GLScreenBuffer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, width, height);
    }

    @Override
    public void unbind() {
        //
    }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }

    @Override
    public void close() {
        // No-op
    }
}

