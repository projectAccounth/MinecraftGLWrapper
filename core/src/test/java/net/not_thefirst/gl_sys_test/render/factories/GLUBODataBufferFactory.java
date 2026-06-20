package net.not_thefirst.gl_sys_test.render.factories;

import net.not_thefirst.gl_sys_test.render.passes.GLUBODataBuffer;
import net.not_thefirst.lib.gl_render_system.alt.PipelineManager.DataBufferFactory;

import java.nio.ByteBuffer;

public class GLUBODataBufferFactory implements DataBufferFactory<GLUBODataBuffer, ByteBuffer> {
    @Override
    public GLUBODataBuffer create(String name, int size) {
        return new GLUBODataBuffer(name, size);
    }
}
