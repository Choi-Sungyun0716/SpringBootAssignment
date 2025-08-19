package com.rookies4.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AssignmentApplication {

	public static void main(String[] args) {
        //SpringApplication.run(MySpringBoot3ProjectApplication.class, args);
        SpringApplication application = new SpringApplication(AssignmentApplication.class);
        //Application 타입을 변경하기
        application.setWebApplicationType(WebApplicationType.SERVLET);
        application.run(args);

    }

}
