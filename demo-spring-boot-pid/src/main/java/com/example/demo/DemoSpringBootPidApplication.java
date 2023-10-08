package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileInputStream;

@SpringBootApplication
public class DemoSpringBootPidApplication {
    public static void main(String[] args) {
        var application = new SpringApplication(DemoSpringBootPidApplication.class);

        // 通过入参配置(Program Arguments)
        // --spring.pid.file=C:\Users\Administrator\Desktop\application.pid

        // 通过-D参数配置(VM Options)
        //-Dspring.pid.file=C:\Users\Administrator\Desktop\application.pid

        // 通过环境变量配置(待测试)
        // @rem windows
        // @set SPRING_PID_FILE=C:\Users\Administrator\Desktop\application.pid

        // 通过application.properties配置
        // spring.pid.file=C:\Users\Administrator\Desktop\application.pid

        // 通过代码配置(同`参数配置`)
        System.setProperty("spring.pid.file", "${user.dir}" + File.separator + "application.pid");

        // 通过代码配置2
        var myArgs = new String[]{"--spring.pid.file=${user.dir}" + File.separator + "application.pid"};
        //  application.run(myArgs);

        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
    }

    private static final Logger logger = LoggerFactory.getLogger(DemoSpringBootPidApplication.class);

    @Bean
    ApplicationRunner runner(@Value("${user.dir}") String userDir, @Value("${spring.pid.file}") String pidFilePath) {
        return args -> {
            logger.info("user.dir={}", userDir);
            logger.info("spring.pid.file={}", pidFilePath);

            var pidFile = new File(pidFilePath);
            try (var in = new FileInputStream(pidFile);) {
                byte[] bytes = in.readAllBytes();

                var currentProjectPid = new String(bytes);
                logger.info("currentProjectPid={}", currentProjectPid);
            }
        };
    }


}
