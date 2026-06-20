package net.not_thefirst.gl_sys_test.render.gl;

import java.io.InputStream;

import net.not_thefirst.gl_sys_test.resources.ResourceManager;
import net.not_thefirst.lib.gl_render_system.shader.ToStreamProvider;

class GLShaderProvider implements ToStreamProvider {
    private ResourceManager manager;

    public GLShaderProvider(ResourceManager manager) {
        this.manager = manager;
    }

    @Override
    public InputStream toStream(String path) {
        try {
            return manager.getResourceStream(path);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
}
