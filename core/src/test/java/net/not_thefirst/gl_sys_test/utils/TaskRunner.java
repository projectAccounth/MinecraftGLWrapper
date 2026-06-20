package net.not_thefirst.gl_sys_test.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class TaskRunner {
    private final String name;
    private final Map<String, Runnable> tasks = new LinkedHashMap<>();

    public TaskRunner(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public synchronized void registerTask(String name, Runnable task) {
        Objects.requireNonNull(name, "Task name cannot be null");
        Objects.requireNonNull(task, "Task cannot be null");
        tasks.put(name, task);
    }

    public synchronized boolean hasTask(String name) {
        return tasks.containsKey(name);
    }

    public synchronized int getTaskCount() {
        return tasks.size();
    }

    public String getName() {
        return this.name;
    }

    public void run() {
        Map<String, Runnable> tasksToRun;
        
        synchronized (this) {
            tasksToRun = new LinkedHashMap<>(tasks);
        }

        for (Map.Entry<String, Runnable> entry : tasksToRun.entrySet()) {
            String taskName = entry.getKey();
            Runnable task = entry.getValue();
            
            try {
                task.run();
            } catch (Exception e) {
                // System.err.println("[" + name + "] Task '" + taskName + "' failed, aborting sequence.");
                throw new IllegalStateException("Execution failed at task: " + taskName, e);
            }
        }
        
        // System.out.println("[" + name + "] All " + tasksToRun.size() + " tasks executed successfully.");
    }

    public synchronized void flushTasks() {
        tasks.clear();
    }
}
