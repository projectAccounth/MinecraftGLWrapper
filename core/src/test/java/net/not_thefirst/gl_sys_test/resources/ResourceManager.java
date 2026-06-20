package net.not_thefirst.gl_sys_test.resources;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ResourceManager {

    public ResourceManager() {
        //
    }

    /**
     * Opens an InputStream for a file embedded inside the program resources.
     * 
     * @param resourcePath Path relative to the resources folder (e.g., "config.properties" or "assets/image.png")
     * @return An active, buffered InputStream
     * @throws IOException If the resource cannot be found or opened
     */
    public InputStream getResourceStream(String resourcePath) throws IOException {
        String normalizedPath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
        
        InputStream rawStream = ResourceManager.class.getClassLoader().getResourceAsStream(normalizedPath);
        
        if (rawStream == null) {
            throw new FileNotFoundException("Local program resource not found: " + resourcePath);
        }
        
        return new BufferedInputStream(rawStream);
    }
}
