package net.not_thefirst.lib.gl_render_system.state;

import java.util.Objects;

import net.not_thefirst.lib.gl_render_system.shader.GLProgram;

public final class ShaderState implements RenderState {

    private final GLProgram program;

    public ShaderState(GLProgram program) {
        this.program = program;
    }

    @Override
    public void apply() {
        program.use();
    }

    @Override
    public void clear() {
        program.stop();
    }

    public GLProgram program() {
        return program;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ShaderState)) return false;
        return ((ShaderState) o).program == this.program;
    }

    @Override
    public int hashCode() {
        return Objects.hash(program);
    }
}
