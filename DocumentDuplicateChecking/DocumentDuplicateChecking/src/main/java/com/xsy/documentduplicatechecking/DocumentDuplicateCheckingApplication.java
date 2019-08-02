package com.xsy.documentduplicatechecking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xushiyue
 * @date 2019年7月30日11:25:48
 */
@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class DocumentDuplicateCheckingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentDuplicateCheckingApplication.class, args);
    }

}
