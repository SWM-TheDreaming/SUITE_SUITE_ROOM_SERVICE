package com.suite.suite_user_service.member.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration                        // 빈 등록
@RequiredArgsConstructor
@PropertySource("classpath:application.yml") 		// classpath는 src/main/resource/ 입니다.
public class ConfigUtil {
    private final Environment env;

    public String getProperty(String key) {
        return env.getProperty(key);
    }
}
