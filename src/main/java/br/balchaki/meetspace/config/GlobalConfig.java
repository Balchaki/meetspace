package br.balchaki.meetspace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class GlobalConfig {

    @Bean
    public TimeZone timeZone() {
        TimeZone defaultTimeZone = TimeZone.getTimeZone("America/Sao_Paulo");
        TimeZone.setDefault(defaultTimeZone);
        return defaultTimeZone;
    }
}
