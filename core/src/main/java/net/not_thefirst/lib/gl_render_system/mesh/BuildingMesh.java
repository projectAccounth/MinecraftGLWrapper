package net.not_thefirst.lib.gl_render_system.mesh;

import java.util.Arrays;

import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;
import net.not_thefirst.lib.utils.math.ARGB;

public class BuildingMesh {

    protected float[] positions;
    protected float[] normals;
    protected float[] uvs;
    protected int[] colors;

    protected int capacity;
    protected int currentIndex;

    protected boolean vertexOpen = false;

    protected final VertexFormat format;
    protected final GLPrimitive primitive;

    public BuildingMesh(VertexFormat format, GLPrimitive primitive) {
        this.format = format;
        this.primitive = primitive;

        this.capacity = 1024;

        this.positions = new float[capacity * 3];
        this.normals   = new float[capacity * 3];
        this.uvs       = new float[capacity * 2];
        this.colors    = new int[capacity];
    }

    private void ensureCapacity(int additionalVertices) {
        int required = currentIndex + additionalVertices;
        if (required <= capacity)
            return;

        int newCapacity = capacity;
        while (newCapacity < required) {
            newCapacity = newCapacity + (newCapacity >> 1);
        }

        positions = Arrays.copyOf(positions, newCapacity * 3);
        normals   = Arrays.copyOf(normals,   newCapacity * 3);
        uvs       = Arrays.copyOf(uvs,       newCapacity * 2);
        colors    = Arrays.copyOf(colors,    newCapacity);

        capacity = newCapacity;
    }

    protected void endLastVertex() {
        if (vertexOpen) {
            currentIndex++;
            vertexOpen = false;
        }
    }

    public BuildingMesh addVertex(float x, float y, float z) {
        endLastVertex();
        ensureCapacity(1);

        int p = currentIndex * 3;
        positions[p]     = x;
        positions[p + 1] = y;
        positions[p + 2] = z;

        vertexOpen = true;
        return this;
    }

    public BuildingMesh setNormal(float nx, float ny, float nz) {
        if (!vertexOpen) throw new IllegalStateException("Cannot set normal without starting a vertex");
        int p = currentIndex * 3;
        normals[p]     = nx;
        normals[p + 1] = ny;
        normals[p + 2] = nz;
        return this;
    }

    public BuildingMesh setUv(float u, float v) {
        if (!vertexOpen) throw new IllegalStateException("Cannot set UV without starting a vertex");
        int p = currentIndex * 2;
        uvs[p]     = u;
        uvs[p + 1] = v;
        return this;
    }

    public BuildingMesh setColor(int color) {
        if (!vertexOpen) throw new IllegalStateException("Cannot set color without starting a vertex");
        colors[currentIndex] = color;
        return this;
    }

    public BuildingMesh setColor(float r, float g, float b, float a) {
        int ir = Math.min(255, Math.max(0, (int)(r * 255.0f)));
        int ig = Math.min(255, Math.max(0, (int)(g * 255.0f)));
        int ib = Math.min(255, Math.max(0, (int)(b * 255.0f)));
        int ia = Math.min(255, Math.max(0, (int)(a * 255.0f)));

        int color = ARGB.color(ia, ir, ig, ib);
        return setColor(color);
    }

    public BuildingMesh endVertex() {
        endLastVertex();
        return this;
    }

    public void reset() {
        currentIndex = 0;
        vertexOpen = false;
    }

    public int capacity() {
        return capacity;
    }

    public int vertexCount() {
        return currentIndex + (vertexOpen ? 1 : 0);
    }

    public CompiledMesh compile() {
        endLastVertex();

        int count = currentIndex;

        float[] finalPositions = Arrays.copyOf(positions, count * 3);
        float[] finalNormals   = Arrays.copyOf(normals,   count * 3);
        float[] finalUvs       = Arrays.copyOf(uvs,       count * 2);
        int[]   finalColors    = Arrays.copyOf(colors,    count);

        return new CompiledMesh(
            finalPositions,
            finalNormals,
            finalUvs,
            finalColors,
            count,
            format,
            primitive
        );
    }
}
