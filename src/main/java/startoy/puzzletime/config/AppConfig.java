package startoy.puzzletime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     * RestTemplate Bean 생성
     * 이 Bean은 애플리케이션 전역에서 HTTP 요청을 위해 사용.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
