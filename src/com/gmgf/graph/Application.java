package com.gmgf.graph;

import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {
    public Application() {
        packages("com.gmgf.services");

    }
}
