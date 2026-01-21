package net.not_thefirst.lib.gl_render_system.shader;

import java.io.InputStream;

public interface ToStreamProvider {
    InputStream toStream(String path);
}
