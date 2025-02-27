package org.anildeveloper.SpringBoot01.security;

import org.anildeveloper.SpringBoot01.util.constants.Authorities;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String WHITEPAGE[] = {
            "/",
            "/home",
            "/db-console/**",
            "/login",
            "/css/**",
            "/assets/**",
            "/register",
            "/posts/**",
            "/**"
    };

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(WHITEPAGE)
                        .permitAll()
                        .requestMatchers("/profile/**").authenticated()
                        .requestMatchers("/post/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/editor/**").hasAnyRole("EDITOR", "ADMIN")
                        .requestMatchers("/admin/**").hasAuthority(Authorities.ACCESS_ADMIN_PANEL.getAuthorityName()))
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
                .rememberMe((remember) -> remember
                        .rememberMeParameter("remember-me"))
                // remove these after H2 database
                .csrf((csrf) -> csrf.disable())
                .headers((headers) -> headers.frameOptions((options) -> options.disable()));

        return http.build();
    }

}
