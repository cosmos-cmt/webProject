package com.cuda.gym.security;

import com.cuda.gym.domain.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService customUserDetailsService;
    private final AuthenticationSuccessHandler loginSuccessHandler;
    private final PersistentTokenRepository persistentTokenRepository;

        private static final String REMEMBER_ME_KEY = "gym-remember-me-key";

    public SecurityConfig(UserDetailsService customUserDetailsService,
                          AuthenticationSuccessHandler loginSuccessHandler,
                          PersistentTokenRepository persistentTokenRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.persistentTokenRepository = persistentTokenRepository;
    }

       @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                "/",                                                 "/index",                                           "/classes",
                                "/membership",
                                "/trainers",
                                "/contact",
                                "/register",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**"
                        ).permitAll()
                                                .requestMatchers("/admin/**")
                        .hasRole(User.ADMIN.name())
                                               .requestMatchers("/member/**")
                        .hasRole(UserRole.MEMBER.name())
                                                .anyRequest()
                        .authenticated()
                )

                               .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")                              .passwordParameter("password")
                        .successHandler(loginSuccessHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                               .rememberMe(remember -> remember
                        .rememberMeParameter("remember-me")                        .userDetailsService(customUserDetailsService)
                        .tokenRepository(persistentTokenRepository)
                        .key(REMEMBER_ME_KEY)
                        .tokenValiditySeconds(7 * 24 * 60 * 60)
                )

                                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID", "REMEMBER_ME")
                        .invalidateHttpSession(true)
                        .permitAll()
                )

                             .csrf(Customizer.withDefaults());

        return http.build();
    }
}