package net.not_thefirst.lib.gl_render_system.alt;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import net.not_thefirst.lib.utils.DynamicEnum;


public class PipelineManager {
    private static final PipelineManager INSTANCE = new PipelineManager();
    private static PipelineProvider CURRENT_PROVIDER = PipelineProvider.DEFAULT;

    // short: from 26.2 onwards, a reversed depth buffer is used
    private static DepthBufferImplType DEPTH_BUFFER_IMPL = DepthBufferImplType.STANDARD;

    /**
     * Table structure:
     * - Row key: pipeline name, String
     * - Column key: Pipeline provider of type {@link PipelineProvider}
     * - Value: Corresponding {@link AbstractPipeline} instance
     */
    private final Table<String, PipelineProvider, AbstractPipeline> pipelineTable = HashBasedTable.create();
    private final Map<PipelineProvider, Supplier<RenderPassFactory<?, ?>>> renderPassFactories = new HashMap<>();
    private final Map<PipelineProvider, Supplier<DataBufferFactory<?, ?>>> dataBufferFactories = new HashMap<>();;

    private PipelineManager() {}

    public void init() {
        // setup all abstract pipelines
        for (PipelineProvider provider : DynamicEnum.values(PipelineProvider.class)) {
            for (AbstractPipeline pipeline : INSTANCE.pipelineTable.column(provider).values()) {
                pipeline.setup();
            }
        }
    }

    public static PipelineManager getInstance() {
        return INSTANCE;
    }

    public List<AbstractPipeline> get() {
        return get(CURRENT_PROVIDER);
    }

    public List<AbstractPipeline> get(PipelineProvider provider, String... names) {
        List<AbstractPipeline> pipelines = new ArrayList<>();
        for (String name : names) {
            AbstractPipeline pipeline = pipelineTable.get(name, provider);
            if (pipeline == null) {
                throw new IllegalArgumentException("No pipeline found with name '" + name + "' for provider " + provider);
            }
            pipelines.add(pipeline);
        }
        return pipelines;
    }

    public List<AbstractPipeline> get(PipelineProvider provider) {
        return new ArrayList<>(pipelineTable.column(provider).values());
    }

    public void registerPipeline(AbstractPipeline pipeline) {
        registerPipeline(pipeline, CURRENT_PROVIDER);
    }

    public void registerPipeline(AbstractPipeline pipeline, PipelineProvider provider) {
        pipelineTable.put(pipeline.getName(), provider, pipeline);
    }

    public void registerPipelines(AbstractPipeline... pipelines) {
        for (AbstractPipeline pipeline : pipelines) {
            registerPipeline(pipeline);
        }
    }

    public void registerPipelines(PipelineProvider provider, AbstractPipeline... pipelines) {
        for (AbstractPipeline pipeline : pipelines) {
            registerPipeline(pipeline, provider);
        }
    }

    public AbstractRenderPass<?> createRenderPass(String name, AbstractPipeline[] pipelines) {
        return createRenderPass(name, CURRENT_PROVIDER, pipelines);
    }

    public AbstractRenderPass<?> createRenderPass(String name, PipelineProvider provider, AbstractPipeline[] pipelines) {
        return getRenderPassFactory(provider).create(name, pipelines);
    }

    public static DepthBufferImplType getSetDepthBufferImplType() {
        return DEPTH_BUFFER_IMPL;
    }

    public static void setDepthBufferImplType(DepthBufferImplType type) {
        DEPTH_BUFFER_IMPL = type;
    }

    public static PipelineProvider getPipelineProvider() {
        return CURRENT_PROVIDER;
    }

    public static void setPipelineProvider(PipelineProvider type) {
        CURRENT_PROVIDER = type;
    }

    public boolean hasRenderPassFactoryForProvider(PipelineProvider provider) {
        return renderPassFactories.containsKey(provider);
    }

    public void registerRenderPassFactory(PipelineProvider provider, Supplier<RenderPassFactory<?, ?>> factory) {
        renderPassFactories.put(provider, factory);
    }

    private RenderPassFactory<?, ?> getRenderPassFactory(PipelineProvider provider) {
        if (!hasRenderPassFactoryForProvider(provider))
            throw new InvalidParameterException("Attempted to get factory for unregistered provider " + provider);
        return renderPassFactories.get(provider).get();
    }

    public AbstractUBODataBuffer<?, ?> createDataBuffer(String name, int size) {
        return createDataBuffer(name, size, CURRENT_PROVIDER);
    }

    public AbstractUBODataBuffer<?, ?> createDataBuffer(String name, int size, PipelineProvider provider) {
        return getDataBufferFactory(provider).create(name, size);
    }

    public boolean hasDataBufferFactoryForProvider(PipelineProvider provider) {
        return dataBufferFactories.containsKey(provider);
    }

    public void registerDataBufferFactory(PipelineProvider provider, Supplier<DataBufferFactory<?, ?>> factory) {
        dataBufferFactories.put(provider, factory);
    }

    private DataBufferFactory<?, ?> getDataBufferFactory(PipelineProvider provider) {
        if (!hasDataBufferFactoryForProvider(provider))
            throw new InvalidParameterException("Attempted to get factory for unregistered provider " + provider);
        return dataBufferFactories.get(provider).get();
    }


    public AbstractPipeline getPipeline(String name) {
        return pipelineTable.get(name, CURRENT_PROVIDER);
    }

    public static class PipelineProvider extends DynamicEnum<PipelineProvider> {
        private PipelineProvider(String name, int ord) {
            super(name, ord);
        } 

        public static final PipelineProvider DEFAULT = register("DEFAULT");

        public static PipelineProvider register(String name) {
            return DynamicEnum.register(PipelineProvider.class, name, PipelineProvider::new);
        }
    }

    public enum DepthBufferImplType {
        STANDARD, REVERSED_Z
    }

    public static interface RenderPassFactory<T extends AbstractPipeline, R extends AbstractRenderPass<T>> {
        
        public Class<T> getPipelineClass();

        public R create(String name, AbstractPipeline[] pipelines);

        public default void verifyPipelines(AbstractPipeline[] pipelines) {
            if (pipelines.length == 0) {
                throw new IllegalArgumentException("At least one pipeline must be provided");
            }
            for (AbstractPipeline pipeline : pipelines) {
                if (!getPipelineClass().isInstance(pipeline)) {
                    throw new IllegalArgumentException(
                        "Pipeline " + pipeline.getName() + " is not of type " + getPipelineClass().getSimpleName()
                    );
                }
            }
        }
    }

    public static interface DataBufferFactory<T extends AbstractUBODataBuffer<T, R>, R> {
        public T create(String name, int size);
    }
}
