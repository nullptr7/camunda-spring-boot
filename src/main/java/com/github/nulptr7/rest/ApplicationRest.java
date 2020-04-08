package com.github.nulptr7.rest;

import org.camunda.bpm.engine.rest.impl.CamundaRestResources;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class ApplicationRest extends Application {
    // We need to create our own custom endpoints using similar overriding
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.addAll(CamundaRestResources.getResourceClasses());
        classes.addAll(CamundaRestResources.getConfigurationClasses());
        return classes;
    }
}
