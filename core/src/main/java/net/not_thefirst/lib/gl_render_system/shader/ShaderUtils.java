package net.not_thefirst.lib.gl_render_system.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public final class ShaderUtils {

    private ShaderUtils() {}

    public static final class CompilationFailException extends Exception {

        public CompilationFailException(String message) {
            super(message);
        }

        public CompilationFailException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static int compile(int type, String source) throws CompilationFailException {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            String log = GL20.glGetShaderInfoLog(shader);
            GL20.glDeleteShader(shader);
            throw new CompilationFailException("Shader compile failed:\n" + log);
        }

        return shader;
    }

    public static GLProgram create(String vertexSrc, String fragmentSrc) throws CompilationFailException {
        int vert;
        int frag;
        try {
            vert = compile(GL20.GL_VERTEX_SHADER, vertexSrc);
            frag = compile(GL20.GL_FRAGMENT_SHADER, fragmentSrc);
        }
        catch (CompilationFailException exception) {
            exception.printStackTrace();
            return null;
        }

        int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vert);
        GL20.glAttachShader(program, frag);
        GL20.glLinkProgram(program);

        GL20.glDeleteShader(vert);
        GL20.glDeleteShader(frag);

        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            String log = GL20.glGetProgramInfoLog(program);
            GL20.glDeleteProgram(program);
            throw new CompilationFailException("Program link failed:\n" + log);
        }

        return new GLProgram(program);
    }

    public static GLProgram create(
        ToStreamProvider provider,
        String vertexPath,
        String fragmentPath) {

        String vertSrc = ShaderSourceLoader.load(provider.toStream(vertexPath));
        String fragSrc = ShaderSourceLoader.load(provider.toStream(fragmentPath));

        try {
            return create(vertSrc, fragSrc);
        }
        catch (CompilationFailException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
