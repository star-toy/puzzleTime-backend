package startoy.puzzletime.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity(debug = true) // 디버깅 활성화
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {
            "/",
            "/api/session/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/v*/**", // v1, v2, v3 ...
            "/error",
            "/images/**",
            "/favicon.ico",
            "/actuator/**",
            "/api/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_URLS).permitAll()  // PUBLIC_URLS에 포함된 경로들 허용
                        .requestMatchers("/api/auth/**", "/oauth2/**").permitAll()  // OAuth2 관련 경로 허용
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())  // REST API에서 CSRF 비활성화
                .cors(Customizer.withDefaults()); // CORS 설정 추가

        return http.build();
    }

}