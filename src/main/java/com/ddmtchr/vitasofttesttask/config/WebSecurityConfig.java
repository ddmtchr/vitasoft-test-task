package com.ddmtchr.vitasofttesttask.config;

import com.ddmtchr.vitasofttesttask.security.jwt.AccessDeniedHandlerJwt;
import com.ddmtchr.vitasofttesttask.security.jwt.AuthenticationEntryPointJwt;
import com.ddmtchr.vitasofttesttask.security.jwt.JwtAuthenticationFilter;
import com.ddmtchr.vitasofttesttask.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private UserService userDetailsService;

    @Autowired
    private AuthenticationEntryPointJwt unauthorizedHandler;

    @Autowired
    private AccessDeniedHandlerJwt accessDeniedHandlerJwt;

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(accessDeniedHandlerJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                                auth
                                        .antMatchers(HttpMethod.POST, "/api/tickets").hasAuthority("USER")
                                        .antMatchers(HttpMethod.PUT, "/api/tickets/{\\d+}").hasAuthority("USER")
                                        .antMatchers(HttpMethod.PUT, "/api/tickets/{\\d+}/send").hasAuthority("USER")
                                        .antMatchers(HttpMethod.GET, "/api/tickets/my").hasAuthority("USER")
                                        .antMatchers(HttpMethod.GET, "/api/tickets/by-name").hasAuthority("OPERATOR")
                                        .antMatchers(HttpMethod.GET, "/api/tickets").hasAuthority("OPERATOR")
                                        .antMatchers(HttpMethod.PUT, "/api/tickets/{\\d+}/accept").hasAuthority("OPERATOR")
                                        .antMatchers(HttpMethod.PUT, "/api/tickets/{\\d+}/reject").hasAuthority("OPERATOR")
                                        .antMatchers(HttpMethod.GET, "/api/users").hasAuthority("ADMIN")
                                        .antMatchers(HttpMethod.GET, "/api/users/by-name").hasAuthority("ADMIN")
                                        .antMatchers(HttpMethod.PUT, "/api/users/{//d+}/promote").hasAuthority("ADMIN")
                                        .antMatchers("/api/auth/**").permitAll()
                                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
