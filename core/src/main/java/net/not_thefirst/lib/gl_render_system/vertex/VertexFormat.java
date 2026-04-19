package net.not_thefirst.lib.gl_render_system.vertex;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.not_thefirst.lib.gl_render_system.mesh.CompiledMesh;

public final class VertexFormat {

    /**
     * Defines the type of vertex attribute. This is used to identify the purpose of each attribute in the vertex data, such as position, color, normal, etc. The rendering system uses this information to correctly interpret the vertex data when rendering meshes.
     */
    public enum VertexAttribute {
        POSITION,
        COLOR,
        NORMAL,
        UV0,
        UV1,
        TANGENT
    }

    /**
     * Represents a single vertex attribute in the vertex format. It contains information about the attribute type (e.g. position, color), the number of components (e.g. 3 for position), the OpenGL data type (e.g. GL_FLOAT), and whether the data should be normalized (e.g. for colors). This class is used to define the layout of vertex data in memory and how it should be interpreted by the GPU when rendering.
     */
    public static final class Element {

        
        public final VertexAttribute attribute;
        public final int componentCount;
        public final int glType;
        public final boolean normalized;

        public Element(
            VertexAttribute attribute,
            int componentCount,
            int glType,
            boolean normalized
        ) {
            this.attribute = attribute;
            this.componentCount = componentCount;
            this.glType = glType;
            this.normalized = normalized;
        }

        public int sizeBytes() {
            return componentCount * glTypeSize(glType);
        }

        private static int glTypeSize(int glType) {

            if (glType == GL11.GL_FLOAT) return Float.BYTES;
            if (glType == GL11.GL_UNSIGNED_BYTE || glType == GL11.GL_BYTE) return Byte.BYTES;
            if (glType == GL11.GL_UNSIGNED_SHORT || glType == GL11.GL_SHORT) return Short.BYTES;
            if (glType == GL11.GL_INT) return Integer.BYTES;
            
            throw new IllegalArgumentException("Unsupported GL type: " + glType);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Element)) return false;
            Element e = (Element) o;
            return attribute == e.attribute &&
                   componentCount == e.componentCount &&
                   glType == e.glType &&
                   normalized == e.normalized;
        }

        @Override
        public int hashCode() {
            return Objects.hash(attribute, componentCount, glType, normalized);
        }
    }

    private final List<Element> elements;
    private final int strideBytes;

    private final int[] offsets;
    private final int[] locations;

    public VertexFormat(List<Element> elements) {
        this.elements = Collections.unmodifiableList(new ArrayList<>(elements));

        this.offsets = new int[elements.size()];
        this.locations = new int[elements.size()];

        int offset = 0;
        for (int i = 0; i < elements.size(); i++) {
            offsets[i] = offset;
            locations[i] = i;
            offset += elements.get(i).sizeBytes();
        }

        this.strideBytes = offset;
    }

    public int strideBytes() {
        return strideBytes;
    }

    public List<Element> elements() {
        return elements;
    }


    public void enable() {
        for (int i = 0; i < elements.size(); i++) {
            Element e = elements.get(i);
            
            GL20.glVertexAttribPointer(
                locations[i],
                e.componentCount,
                e.glType,
                e.normalized,
                strideBytes,
                offsets[i]
            );
            GL20.glEnableVertexAttribArray(locations[i]);
        }
    }

    public void disable() {
        for (int loc : locations) {
            GL20.glDisableVertexAttribArray(loc);
        }
    }

    public void putVertex(
        ByteBuffer buf,
        int vertexIndex,
        CompiledMesh mesh,
        float offX, float offY, float offZ
    ) {
        final float[] positions = mesh.positions();
        final float[] normals   = mesh.normals();
        final float[] uvs       = mesh.uvs();
        final int[]   colors    = mesh.colors();
        for (Element e : elements) {

            if (e.attribute == VertexAttribute.POSITION && positions == null) {
                throw new IllegalStateException(
                    "Mesh is missing positions but format requires them"
                );
            }
            if (e.attribute == VertexAttribute.NORMAL && normals == null) {
                throw new IllegalStateException(
                    "Mesh is missing normals but format requires them"
                );
            }
            if (e.attribute == VertexAttribute.UV0 && uvs == null) {
                throw new IllegalStateException(
                    "Mesh is missing UVs but format requires them"
                );
            }
            if (e.attribute == VertexAttribute.COLOR && colors == null) {
                throw new IllegalStateException(
                    "Mesh is missing colors but format requires them"
                );
            }

            int p;

            switch(e.attribute) {
                case POSITION:
                    p = vertexIndex * 3;
                    buf.putFloat(positions[p]     + offX);
                    buf.putFloat(positions[p + 1] + offY);
                    buf.putFloat(positions[p + 2] + offZ);
                    break;
                case NORMAL:
                    p = vertexIndex * 3;
                    buf.putFloat(normals[p]);
                    buf.putFloat(normals[p + 1]);
                    buf.putFloat(normals[p + 2]);
                    break;
                case UV0:
                    p = vertexIndex * 2;
                    buf.putFloat(uvs[p]);
                    buf.putFloat(uvs[p + 1]);
                    break;
                case COLOR:
                    int c = colors[vertexIndex];
                    buf.put((byte)((c >> 16) & 0xFF)); // R
                    buf.put((byte)((c >> 8) & 0xFF));  // G
                    buf.put((byte)(c & 0xFF));         // B
                    buf.put((byte)((c >> 24) & 0xFF)); // A
                    break;
                default:
                    throw new IllegalStateException(
                        "Unhandled attribute: " + e.attribute
                    );
            }
        }
    }
    public static final Element POSITION =
        new Element(VertexAttribute.POSITION, 3, GL11.GL_FLOAT, false);

    public static final Element COLOR =
        new Element(VertexAttribute.COLOR, 4, GL11.GL_UNSIGNED_BYTE, true);

    public static final Element NORMAL =
        new Element(VertexAttribute.NORMAL, 3, GL11.GL_FLOAT, false);

    public static final Element UV0 =
        new Element(VertexAttribute.UV0, 2, GL11.GL_FLOAT, false);

    public static final Element UV1 =
        new Element(VertexAttribute.UV1, 2, GL11.GL_FLOAT, false);

    public static final VertexFormat POSITION_COLOR =
        new VertexFormat(Arrays.asList(POSITION, COLOR));

    public static final VertexFormat POSITION_COLOR_TEX =
        new VertexFormat(Arrays.asList(POSITION, COLOR, UV0));

    public static final VertexFormat POSITION_COLOR_NORMAL =
        new VertexFormat(Arrays.asList(POSITION, COLOR, NORMAL));

    public static final VertexFormat POSITION_COLOR_NORMAL_TEX =
        new VertexFormat(Arrays.asList(POSITION, COLOR, NORMAL, UV0));
}
