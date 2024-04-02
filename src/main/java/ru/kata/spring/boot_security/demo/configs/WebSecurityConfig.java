package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserDetailsService securityUserService;


    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserDetailsService securityUserService) {
        this.successUserHandler = successUserHandler;
        this.securityUserService = securityUserService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String admin = "ADMIN";
        String user = "USER";
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole(admin)
                .antMatchers("/user/**").hasAnyRole(user, admin)
                .antMatchers("/api/admin/**").hasRole(admin)
                .antMatchers("/api/user/**").hasAnyRole(user, admin)
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler).permitAll()
                .and()
                .logout().permitAll();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(securityUserService);
        return authenticationProvider;
    }
}