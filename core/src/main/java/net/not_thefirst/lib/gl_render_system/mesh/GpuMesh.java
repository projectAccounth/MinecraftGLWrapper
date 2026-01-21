package net.not_thefirst.lib.gl_render_system.mesh;

import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public interface GpuMesh extends AutoCloseable {
    void draw();
    VertexFormat format();

    @Override
    void close();
}
