package com.github.nullptr7.conf;

import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.feel.CamundaFeelEnginePlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessEngineConfiguration {

    @Bean
    public ProcessEnginePlugin feelScalaPlugin() {
        return new CamundaFeelEnginePlugin();
    }
}
