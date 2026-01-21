package net.not_thefirst.lib.gl_render_system.shader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ProgramManager<K> implements AutoCloseable {

    /**
     * Program definition. Serves as an unique identifier for shader programs (not really I guess)
     */
    public static final class ProgramDefinition {
        /**
         * The vertex shader path.
         */
        private final String vertex;
        /**
         * The fragment shader path.
         */
        private final String fragment;

        /**
         * Constructor.
         * @param vert The vertex shader path. 
         * @param frag The fragment shader path.
         */
        public ProgramDefinition(String vert, String frag) {
            this.vertex = vert;
            this.fragment = frag;
        }

        /**
         * Gets the vertex shader path.
         * @return The vertex shader path.
         */
        public String vertex() { return this.vertex; }

        /**
         * Gets the fragment shader path.
         * @return The fragment shader path.
         */
        public String fragment() { return this.fragment; }
    }

    /**
     * The resource provider for shaders.
     */
    private ToStreamProvider provider;

    /**
     * Hashmap for definitions.
     */
    private final Map<K, ProgramDefinition> definitions = new HashMap<>();
    
    /**
     * Hashmap for programs.
     */
    private final Map<K, GLProgram> programs = new HashMap<>();

    public ProgramManager(ToStreamProvider provider) {
        this.provider = provider;
    }

    public void setProvider(ToStreamProvider provider) {
        this.provider = Objects.requireNonNull(
            provider,
            "Attempted to set a null shader source provider on ProgramManager"
        );
    }

    public void register(K id, String vertex, String fragment) {
        Objects.requireNonNull(id, "Program id must not be null");
        Objects.requireNonNull(vertex, "Vertex shader path must not be null");
        Objects.requireNonNull(fragment, "Fragment shader path must not be null");

        definitions.put(id, new ProgramDefinition(vertex, fragment));
    }

    /**
     * Gets the program with the corresponding ID. Lazily creates if not found. Throws on compilation fail.
     * @param id The specified ID.
     * @return The program.
     */
    public GLProgram get(K id) {
        Objects.requireNonNull(id, "Program id must not be null");

        GLProgram existing = programs.get(id);
        if (existing != null) {
            return existing;
        }

        ProgramDefinition def = definitions.get(id);
        if (def == null) {
            throw new IllegalStateException(
                "No shader program registered with id: " + id
            );
        }

        if (provider == null) {
            throw new IllegalStateException(
                "Cannot create shader program '" + id + "': Shader source provider is not available"
            );
        }

        try {
            GLProgram program = ShaderUtils.create(
                provider,
                def.vertex(),
                def.fragment()
            );

            programs.put(id, program);
            return program;

        } catch (RuntimeException e) {
            throw new IllegalStateException(
                "Failed to create shader program '" + id + "'\n" +
                "  Vertex:   " + def.vertex() + "\n" +
                "  Fragment: " + def.fragment(),
                e
            );
        }
    }

    /**
     * Reloads a program with the specified ID.
     * @param id The specified ID.
     * @return The reloaded program.
     */
    public GLProgram reload(K id) {
        Objects.requireNonNull(id, "Program id must not be null");

        GLProgram old = programs.remove(id);
        if (old != null) {
            old.close();
        }

        return get(id);
    }

    /**
     * Reloads all program in the manager.
     */
    public void reloadAll() {
        programs.values().forEach(GLProgram::close);
        programs.clear();
    }

    @Override
    public void close() {
        reloadAll();
        definitions.clear();
    }
}
