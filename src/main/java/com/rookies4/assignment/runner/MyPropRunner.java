package com.rookies4.assignment.runner;

import com.rookies4.assignment.MyPropProperties;
import com.rookies4.assignment.config.MyEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {

    @Value("${myprop.username}")
    private String name;

    @Value("${myprop.port}")
    private int port;

    @Autowired
    private MyPropProperties properties;

    @Autowired
    private MyEnvironment myEnvironment;

    private Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    public void run(ApplicationArguments args) throws Exception {

        logger.info("현재 활성호된 CustomerVo Bean = {}" , myEnvironment);

        System.out.println(("====Value 사용===="));
        logger.info("username = {}" , name);
        logger.info("port = {}" , port);
        logger.debug("username = {}" , name);
        logger.debug("port = {}" , port);

        System.out.println(("====properties 사용===="));
        logger.info("username = {}" , properties.getUsername());
        logger.info("port = {}" , properties.getPort());
        logger.debug("username = {}" , properties.getUsername());
        logger.debug("port = {}" , properties.getPort());




    }

}
