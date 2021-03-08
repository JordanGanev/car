package com.alphateam6.carinsurance.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);
        return jdbcUserDetailsManager;
    }

    // identity provider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(encoder());
    }

    // fluent api configure
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/**", "/users/**", "/js/**", "/admin", "/admin/requests-filter").permitAll()
                .antMatchers("/assets/img/**", "/css/**").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/requests/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/", "/register", "/registrationConfirm").permitAll()
                .antMatchers("/index/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/v2/api-docs").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/authenticate")
                .defaultSuccessUrl("/requests/new")
                //.successForwardUrl("/beers")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/requests/new")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
