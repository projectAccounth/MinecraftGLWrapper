package net.not_thefirst.lib.gl_render_system.target;

public interface GLRenderTarget extends AutoCloseable {
    void bind();
    void unbind();
    int getWidth();
    int getHeight();
    @Override
    void close();
}
