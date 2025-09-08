package br.com.gerenciador.pedidos.core.config;

import br.com.gerenciador.pedidos.core.filter.JwtAuthenticationFilter;
import br.com.gerenciador.pedidos.service.UserDetailsServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final UserDetailsServiceImp userDetailsServiceImp;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs*/**", "/v3/api-docs.yaml");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("api/user/register/**", "api/auth/login/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/order/**")
                                .hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/product/**")
                                .hasAnyRole("USER", "ADMIN")
                                .anyRequest().hasRole("ADMIN")
                )
                .userDetailsService(userDetailsServiceImp)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        e -> e.accessDeniedHandler(
                                        (request,
                                         response,
                                         accessDeniedException) -> response.setStatus(403)
                                )
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .build();

    }
}
