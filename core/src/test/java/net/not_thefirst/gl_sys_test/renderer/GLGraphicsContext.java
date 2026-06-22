package net.not_thefirst.gl_sys_test.renderer;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import net.not_thefirst.gl_sys_test.render.gl.GLRenderPass;
import net.not_thefirst.lib.gl_render_system.target.GLRenderTarget;
import net.not_thefirst.lib.utils.math.ARGB;

public class GLGraphicsContext {
    private List<GLRenderPass> passes = new ArrayList<>();

    GLGraphicsContext() {
        //
    }

    void beginTarget(GLRenderTarget target) {
        target.bind();
    }

    public void clear(int mask) {
        glClear(mask);
    }

    public void clearColor(int colorARGB) {
        glClearColor(ARGB.redFloat(colorARGB), ARGB.greenFloat(colorARGB), ARGB.blueFloat(colorARGB), ARGB.alphaFloat(colorARGB));
    }

    public void submit(GLRenderPass pass) {
        if (pass.isClosed()) {
            System.out.println("[ERROR]: Attempted to submit a closed pass");
        }
        passes.add(pass);
    }

    void processPasses() {
        for (GLRenderPass pass : passes) {
            pass.setup();
            pass.render();
            pass.cleanup();
        }
    }

    void endTarget(GLRenderTarget target) {
        target.unbind();
    }

    void flushPasses() {
        passes.clear();
    }
}
