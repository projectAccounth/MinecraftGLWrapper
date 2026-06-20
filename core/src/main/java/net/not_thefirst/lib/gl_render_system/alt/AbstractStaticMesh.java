package net.not_thefirst.lib.gl_render_system.alt;

import net.not_thefirst.lib.gl_render_system.mesh.utils.GLPrimitive;
import net.not_thefirst.lib.gl_render_system.vertex.VertexFormat;

public abstract class AbstractStaticMesh<P extends AbstractRenderPass<?>> implements AutoCloseable {
    protected final GLPrimitive primitive;
    protected final VertexFormat format;

    protected AbstractStaticMesh(
        final GLPrimitive primitive,
        final VertexFormat format) {
        this.primitive = primitive;
        this.format = format;
    }

    public abstract void setup(final P pass);
    public abstract void draw(P pass, RenderParams params);
    public abstract void cleanup(final P pass);

    public int getIndexCount() {
        return this.primitive.indicesPerPrimitive();
    }
    @Override
    public abstract void close();

    public static interface Builder<B extends Builder<B, R>, R extends AbstractStaticMesh<?>> {
        B addVertex(float x, float y, float z);
        /**
         * Sets the color of the latest vertex. Values are passed in the range of [0, 256), in the format of RGBA.
         * @param r Red channel.
         * @param g Green channel.
         * @param b Blue channel.
         * @param a Alpha channel.
         * @return A reference to the updated builder.
         */
        B setColor(int r, int g, int b, int a);

        /**
         * Sets the color of the latest vertex. A RGBA_8888-formatted color is passed in.
         * @param color The color.
         * @return A reference to the updated builder.
         */

        B setColor(int color);

        B setUv(float u, float v);
        B setUv1(int u, int v);
        B setUv2(int u, int v);
        B setNormal(float x, float y, float z);
        B setLineWidth(float width);

        default void addVertex(final float x, final float y, final float z, final int color, final float u, final float v, final int overlayCoords, final int lightCoords, final float nx, final float ny, final float nz) {
            this.addVertex(x, y, z);
            this.setColor(color);
            this.setUv(u, v);
            this.setOverlay(overlayCoords);
            this.setLight(lightCoords);
            this.setNormal(nx, ny, nz);
        }

        /**
         * Sets the color of the latest vertex. Values are passed in the normalized range of [0, 1], in the format of RGBA.
         * @param r Red channel.
         * @param g Green channel.
         * @param b Blue channel.
         * @param a Alpha channel.
         * @return A reference to the updated builder.
         */
        default B setColor(final float r, final float g, final float b, final float a) {
            return this.setColor(
                (int)(r * 255.0F), 
                (int)(g * 255.0F), 
                (int)(b * 255.0F), 
                (int)(a * 255.0F));
        }

        default B setLight(final int packedLightCoords) {
            return this.setUv2(packedLightCoords & Character.MAX_VALUE, packedLightCoords >> 16 & Character.MAX_VALUE);
        }

        default B setOverlay(final int packedOverlayCoords) {
            return this.setUv1(packedOverlayCoords & Character.MAX_VALUE, packedOverlayCoords >> 16 & Character.MAX_VALUE);
        }

        R build();
    }
}
