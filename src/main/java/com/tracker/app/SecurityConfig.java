package com.tracker.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig{

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
		http.authorizeHttpRequests(authorize -> authorize 
            .requestMatchers("/register","/ws/**","login").permitAll()
            .requestMatchers("/admin/**","/Priorities").hasRole("ADMIN")
            .requestMatchers("/manager/**").hasRole("MANAGER")
            .requestMatchers("/developer/**").hasRole("DEVELOPER")
            .requestMatchers("/tester/**").hasRole("TESTER")
            .requestMatchers("/support/**").hasRole("SUPPORT")
            .anyRequest().authenticated()
       );
        
        http.formLogin(formLogin -> formLogin
            .loginPage("/login")
            .successHandler(authenticationSuccessHandler())
            .permitAll()
        );


        return http.build();
        }


	    // Custom success handler
	    @Bean
	    public AuthenticationSuccessHandler authenticationSuccessHandler() {
	        return new CustomAuthenticationSuccessHandler();
	    }
    
	    @Bean
    	public PasswordEncoder passwordEncoder() {
    		return new BCryptPasswordEncoder();
    	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* 
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers("/register","/ws/**").permitAll()
                .requestMatchers("/admin/**","/Priorities").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasRole("MANAGER")
                .requestMatchers("/developer/**").hasRole("DEVELOPER")
                .requestMatchers("/tester/**").hasRole("TESTER")
                .requestMatchers("/support/**").hasRole("SUPPORT")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .successHandler(customSuccessHandler)
                .permitAll()
                .and()
            .logout()
                .permitAll()
        		.and()
        	.sessionManagement()
            .maximumSessions(1) // Allow only one session per user
                .maxSessionsPreventsLogin(false) // If true, a user cannot authenticate from multiple devices simultaneously
                .sessionRegistry(sessionRegistry())
                .and()
            .sessionAuthenticationStrategy(sessionAuthenticationStrategy());
    		}

    	@Bean
    	public SessionRegistry sessionRegistry() {
    		return new SessionRegistryImpl();
}

    	@Bean
    	public HttpSessionEventPublisher httpSessionEventPublisher() {
    		return new HttpSessionEventPublisher();
    	}

    	@Bean
    	public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    		return new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
}
    

    	@Bean
    	public PasswordEncoder passwordEncoder() {
    		return new BCryptPasswordEncoder();
    	}
}*/
