package net.not_thefirst.lib.gl_render_system.alt;

public class UniformValue {
    private final UniformType type;
    private final Object value;

    public UniformValue(UniformType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public UniformType type() { return this.type; }
    public Object value() { return this.value; }
}