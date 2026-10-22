/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.data.jpa.repository.config.EnableJpaAuditing
 */
package com.healthsure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HealthSureApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthSureApplication.class, (String[])args);
    }
}
