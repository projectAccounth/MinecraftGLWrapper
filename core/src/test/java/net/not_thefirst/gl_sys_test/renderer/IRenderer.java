package net.not_thefirst.gl_sys_test.renderer;

import net.not_thefirst.gl_sys_test.camera.Camera;

public interface IRenderer {
    void renderScene();
    Camera getCamera();
}
