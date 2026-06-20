package net.not_thefirst.gl_sys_test.utils;

public class Initializer extends TaskRunner {
    private boolean ran = false;

    private Initializer() {
        super("Initializer");
    }

    private static final Initializer INSTANCE = new Initializer();

    public static Initializer get() {
        return INSTANCE;
    }

    @Override
    public void run() {
        if (this.ran) {
            throw new RuntimeException("Attempted to call the initializer more than once.");
        }
        super.run();
        ran = true;
        super.flushTasks();
    }
}
