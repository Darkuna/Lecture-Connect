package com.lecture.coordinator.configs;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring configuration for web security.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.headers().frameOptions().disable(); // needed for H2 console

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login.xhtml");

        http.authorizeHttpRequests()
                // Permit access to the H2 console
                .antMatchers("/h2-console/**").permitAll()
                // Permit access for all to error pages
                .antMatchers("/error/**")
                .permitAll()
                // Permit access to register
                .antMatchers("/register.xhtml").permitAll()
                // Only access with admin role
                .antMatchers("/admin/**")
                .hasAnyAuthority("ADMIN")
                // Permit access only for some roles
                .antMatchers("/secured/**")
                .hasAnyAuthority("ADMIN", "USER")
                .and().formLogin()
                .loginPage("/login.xhtml")
                .failureUrl("/login.xhtml?error")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/secured/welcome.xhtml");

        http.exceptionHandling().accessDeniedPage("/error/access_denied.xhtml");
        http.sessionManagement().invalidSessionUrl("/error/invalid_session.xhtml");

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Configure roles and passwords via datasource
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from userx where username=?")
                .authoritiesByUsernameQuery(
                        "select userx_username, roles from userx_userx_role where userx_username=?");
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
