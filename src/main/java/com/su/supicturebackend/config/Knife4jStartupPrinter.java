package com.su.supicturebackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动时在控制台输出 Knife4j 接口文档访问地址
 */
@Slf4j
@Component
public class Knife4jStartupPrinter implements CommandLineRunner {

    @Value("${server.port:8123}")
    private int port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Override
    public void run(String... args) {
        String path = contextPath + "/doc.html";
        log.info("==========================================");
        log.info("  Knife4j 接口文档地址: http://localhost:{}{}", port, path);
        log.info("==========================================");
    }
}
