package com.challenge.challenge.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private UserDetailsService uS;

    @Autowired
    private PasswordEncoder pE;

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception{
        build.userDetailsService(uS).passwordEncoder(pE);
    }

    @Bean
    CustomSuccessHandler successHandler(){
        return new CustomSuccessHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            .cors()
            .and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/operator/edit/**", "/operator/delete/**")
                .hasAnyRole("ADMIN","SUPER_ADMIN")
            .antMatchers("/operator/list")
                .hasAnyRole("USER","ADMIN","SUPER_ADMIN")
            .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                //.defaultSuccessUrl("/operator/list")
                .successHandler(successHandler());
            ;
    }
}
