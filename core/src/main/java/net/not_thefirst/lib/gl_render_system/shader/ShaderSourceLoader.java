package net.not_thefirst.lib.gl_render_system.shader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public final class ShaderSourceLoader {

    private ShaderSourceLoader() {}

    public static String load(InputStream in) {
        try {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load shader: ", e);
        }
    }
}
