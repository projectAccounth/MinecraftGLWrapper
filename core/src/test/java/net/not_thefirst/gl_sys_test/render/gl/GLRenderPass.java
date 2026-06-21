package net.not_thefirst.gl_sys_test.render.gl;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import net.not_thefirst.lib.gl_render_system.alt.AbstractRenderPass;
import net.not_thefirst.lib.gl_render_system.alt.AbstractStaticMesh;
import net.not_thefirst.lib.gl_render_system.alt.AbstractUBODataBuffer;
import net.not_thefirst.lib.gl_render_system.alt.RenderParams;
import net.not_thefirst.lib.gl_render_system.alt.UniformValue;
import net.not_thefirst.lib.gl_render_system.shader.GLProgram;
import net.not_thefirst.lib.gl_render_system.shader.UniformBufferObject;
import net.not_thefirst.lib.gl_render_system.state.GLStateGuard;

public class GLRenderPass extends AbstractRenderPass<GLPipeline> {

    private final Map<String, ByteBuffer> uniformBlocks =
        new HashMap<>();

    private GLStateGuard stateGuard;

    public GLRenderPass(String name, GLPipeline... pipelines) {
        super(name, pipelines);
    }

    @Override
    public void setup() {
        super.setup();
        stateGuard = new GLStateGuard();
    }

    @Override
    public void setMesh(AbstractStaticMesh<?> mesh, int indexCount) {
        if (mesh == null) {
            throw new IllegalArgumentException("Mesh cannot be null");
        }
        if (!(mesh instanceof GLMesh)) {
            throw new IllegalArgumentException("Mesh must be a GLMesh instance");
        }
        this.meshData = mesh;
    }

    public void validateBeforeRender() {
        if (pipelines.isEmpty())
            throw new IllegalStateException("Rendering pass with no pipelines, pass name: " + name);
    }

    @Override
    public void render() {
        super.render();

        validateBeforeRender();

        for (GLPipeline pipeline : pipelines) {
            pipeline.bind();

            uploadUniformBlocks(pipeline);
            bindUniforms(pipeline);

            ((GLMesh) meshData).draw(
                this,
                new RenderParams()
            );

            pipeline.unbind();
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        stateGuard.close();
    }

    private void bindUniforms(GLPipeline pipeline) {
        GLProgram program = pipeline.getProgram();
        for (Entry<String, UniformValue> entry : uniforms.entrySet()) {
            String name = entry.getKey();
            UniformValue value = entry.getValue();

            setUniform(program, name, value);
        }
    }

    private void setUniform(GLProgram program, String name, UniformValue value) {
        switch (value.type()) {
            case INT1: program.setInt(name, (int) value.value()); break;
            case INT2 : {
                int[] arr = (int[]) value.value();
                program.setVec2(name, arr[0], arr[1]);
                break;
            }
            case INT3 : {
                int[] arr = (int[]) value.value();
                program.setVec3(name, arr[0], arr[1], arr[2]);
                break;
                
            }
            case INT4 : {
                int[] arr = (int[]) value.value();
                program.setVec4(name, arr[0], arr[1], arr[2], arr[3]);
                break;
            }
            case FLOAT1 : program.setFloat(name, (float) value.value()); break;
            case FLOAT2 : {
                float[] arr = (float[]) value.value();
                program.setVec2(name, arr[0], arr[1]);
                break;
            }
            case FLOAT3 : {
                float[] arr = (float[]) value.value();
                program.setVec3(name, arr[0], arr[1], arr[2]);
                break;
            }
            case FLOAT4 : {
                float[] arr = (float[]) value.value();
                program.setVec4(name, arr[0], arr[1], arr[2], arr[3]);
                break;
            }
            case MAT4 : program.setMat4(name, new Matrix4f().set((float[]) value.value())); break;
            case MAT3 : program.setMat3(name, new Matrix3f().set((float[]) value.value())); break;
            default : throw new IllegalArgumentException("Unknown uniform type: " + value.type());
        }
    }

    private void uploadUniformBlocks(
        GLPipeline pipeline
    ) {

        for (Entry<String, ByteBuffer> entry :
                uniformBlocks.entrySet()) {

            UniformBufferObject ubo =
                pipeline.getUbos()
                        .get(entry.getKey());

            if (ubo == null) {
                continue;
            }

            ByteBuffer data = entry.getValue();

            if (data.remaining() != ubo.size()) {

                throw new IllegalStateException(
                    "UBO size mismatch for block '" +
                    entry.getKey() +
                    "' (shader=" +
                    ubo.size() +
                    ", buffer=" +
                    data.remaining() +
                    ")"
                );
            }

            ubo.update(data);
        }
    }

    @Override
    public void bindUniformBlock(
            String name,
            AbstractUBODataBuffer<?, ?> buffer) {

        if (!(buffer instanceof GLUBODataBuffer)) {
            throw new IllegalArgumentException(
                "UBO must be an instance of GLUBODataBuffer"
            );
        }

        uniformBlocks.put(
            name,
            ((GLUBODataBuffer) buffer).build()
        );
    }

    @Override
    public void bindTexture(String name, int textureId, int slot) {
        throw new UnsupportedOperationException("Unimplemented method 'bindTexture'");
    }

    @Override
    public void bindTextureArray(String name, int textureId, int slot) {
        throw new UnsupportedOperationException("Unimplemented method 'bindTextureArray'");
    }
}