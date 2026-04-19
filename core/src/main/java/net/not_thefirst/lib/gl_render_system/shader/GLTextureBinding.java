package net.not_thefirst.lib.gl_render_system.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class GLTextureBinding {

    private final int textureId;
    private final int target;
    private final GLSampler sampler;

    public GLTextureBinding(int target, int textureId, GLSampler sampler) {
        this.target = target;
        this.textureId = textureId;
        this.sampler = sampler;
    }

    public void bind(int unit) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
        GL11.glBindTexture(target, textureId);
        if (sampler != null) {
            sampler.bind(unit);
        }
    }
}
