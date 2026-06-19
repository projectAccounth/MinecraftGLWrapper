package net.not_thefirst.lib.gl_render_system.mesh;

import java.util.ArrayDeque;

import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public final class MeshPool {

    private final ArrayDeque<BuildingMesh> pool = new ArrayDeque<>();

    private final VertexFormat format;
    private final GLPrimitive primitive;

    MeshPool(VertexFormat format, GLPrimitive primitive) {
        this.format = format;
        this.primitive = primitive;
    }

    public BuildingMesh acquire() {
        BuildingMesh buildingMesh = pool.pollFirst();
        if (buildingMesh != null) {
            buildingMesh.reset();
            return buildingMesh;
        }
        return new BuildingMesh(format, primitive);
    }

    public void release(BuildingMesh buildingMesh) {
        pool.addFirst(buildingMesh);
    }
}
