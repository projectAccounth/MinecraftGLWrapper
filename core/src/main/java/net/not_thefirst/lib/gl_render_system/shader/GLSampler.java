package net.not_thefirst.lib.gl_render_system.shader;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL33;

/**
 * A wrapper for OpenGL texture samplers.
 */
public final class GLSampler implements AutoCloseable {

    /**
     * The OpenGL sampler object ID. This is generated when the sampler is created and is used to reference the sampler in OpenGL calls. Each sampler object encapsulates the sampling parameters for textures, such as filtering and wrapping modes, and can be bound to texture units to affect how textures are sampled during rendering.
     */
    private final int id;

    /**
     * Creates a new OpenGL sampler object.
     */
    public GLSampler() {
        this.id = GL33.glGenSamplers();
    }

    /**
     * Gets the OpenGL sampler object ID. This ID is used to reference the sampler in OpenGL calls, such as when setting sampler parameters or binding the sampler to a texture unit. It is important to manage this ID properly, ensuring that the sampler is deleted when it is no longer needed to free up GPU resources.
     * @return The OpenGL sampler object ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the minification filter for this sampler. The minification filter determines how the texture is sampled when it needs to be shrunk down (minified) during rendering. Common filter options include GL_NEAREST for nearest-neighbor filtering and GL_LINEAR for linear filtering, which can provide smoother results. This method allows you to configure the sampler's behavior when textures are rendered at smaller sizes.
     * @param filter The OpenGL filter mode to use for minification (e.g. GL_NEAREST, GL_LINEAR).
     * @return This sampler instance for chaining.
     */
    public GLSampler minFilter(int filter) {
        GL33.glSamplerParameteri(id, GL11.GL_TEXTURE_MIN_FILTER, filter);
        return this;
    }

    /**
     * Sets the magnification filter for this sampler. The magnification filter determines how the texture is sampled when it needs to be enlarged (magnified) during rendering. Similar to the minification filter, common options include GL_NEAREST for a pixelated look and GL_LINEAR for smoother results. This method allows you to configure the sampler's behavior when textures are rendered at larger sizes.
     * @param filter The OpenGL filter mode to use for magnification (e.g. GL_NEAREST, GL_LINEAR).
     * @return This sampler instance for chaining.
     */
    public GLSampler magFilter(int filter) {
        GL33.glSamplerParameteri(id, GL11.GL_TEXTURE_MAG_FILTER, filter);
        return this;
    }

    /**
     * Sets the wrapping mode for the S (U) texture coordinate. The wrapping mode determines how texture coordinates outside the [0, 1] range are handled when sampling a texture. Common wrapping modes include GL_REPEAT, which repeats the texture, and GL_CLAMP_TO_EDGE, which clamps the coordinates to the edge of the texture. This method allows you to configure how textures are sampled when texture coordinates exceed their normal range in the horizontal direction.
     * @param mode The OpenGL wrapping mode to use for the S (U) coordinate (e.g. GL_REPEAT, GL_CLAMP_TO_EDGE).
     * @return This sampler instance for chaining.
     */ 
    public GLSampler wrapS(int mode) {
        GL33.glSamplerParameteri(id, GL11.GL_TEXTURE_WRAP_S, mode);
        return this;
    }

    /**
     * Sets the wrapping mode for the T (V) texture coordinate. Similar to the S (U) coordinate, the wrapping mode for the T (V) coordinate determines how texture coordinates outside the [0, 1] range are handled when sampling a texture in the vertical direction. This method allows you to configure how textures are sampled when texture coordinates exceed their normal range in the vertical direction.
     * @param mode The OpenGL wrapping mode to use for the T (V) coordinate (e.g. GL_REPEAT, GL_CLAMP_TO_EDGE).
     * @return This sampler instance for chaining.
     */
    public GLSampler wrapT(int mode) {
        GL33.glSamplerParameteri(id, GL11.GL_TEXTURE_WRAP_T, mode);
        return this;
    }

    /**
    * Sets the wrapping mode for the R (W) texture coordinate. The wrapping mode for the R (W) coordinate determines how texture coordinates outside the [0, 1] range are handled when sampling a 3D texture or a cube map. This method allows you to configure how textures are sampled when texture coordinates exceed their normal range in the depth direction for 3D textures or in the third dimension for cube maps.
    * @param mode The OpenGL wrapping mode to use for the R (W) coordinate (e.g. GL_REPEAT, GL_CLAMP_TO_EDGE).
    * @return This sampler instance for chaining.
    */
    public GLSampler wrapR(int mode) {
        GL33.glSamplerParameteri(id, GL12.GL_TEXTURE_WRAP_R, mode);
        return this;
    }

    /**
     * Sets the maximum anisotropy level for this sampler. Anisotropic filtering improves the quality of texture sampling at oblique viewing angles, reducing blurriness and preserving detail. The value should be greater than or equal to 1.0, where 1.0 means no anisotropic filtering (equivalent to standard filtering), and higher values enable more aggressive anisotropic filtering. This method checks if the GL_EXT_texture_filter_anisotropic extension is supported before setting the parameter, ensuring compatibility with the current OpenGL context.
     * @param value The maximum anisotropy level to set for this sampler (e.g. 1.0 for no anisotropic filtering, higher values for more aggressive filtering).
     * @return This sampler instance for chaining.
     */
    public GLSampler anisotropy(float value) {
        if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            GL33.glSamplerParameterf(id,
                EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT,
                value);
        }
        return this;
    }

    public void bind(int unit) {
        GL33.glBindSampler(unit, id);
    }

    public static void unbind(int unit) {
        GL33.glBindSampler(unit, 0);
    }

    @Override
    public void close() {
        GL33.glDeleteSamplers(id);
    }
}