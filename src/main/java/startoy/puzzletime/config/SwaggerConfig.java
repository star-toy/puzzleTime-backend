// src/main/java/com/startoy/polling/config/SwaggerConfig.java
package startoy.puzzletime.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {


        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("cookieAuth", // 쿠키 인증 정의
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY) // API Key 방식
                                        .name("token") // 쿠키 이름
                                        .in(SecurityScheme.In.COOKIE))) // 쿠키에서 전달
                .addServersItem(new Server().url("/").description("https설정"))
                .info(new Info()
                        .title("puzzle API")
                        .version("1.0.0")
                        .description("puzzle API Document"));
    }
}