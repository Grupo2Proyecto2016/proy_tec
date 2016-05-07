//package com.springmvc.configuration;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//	
////	public SecurityConfig() {
////	    super(false);
////	}
////
////	@Override
////    protected void configure(HttpSecurity http) throws Exception {
////       
////      http.authorizeRequests()
////        .antMatchers("/").authenticated()
////        //.antMatchers("/admin/**").access("hasRole('ADMIN')")
////        //.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
////        .and().formLogin().loginPage("/login")
////        .usernameParameter("username").passwordParameter("password")
////        .and().csrf();
////        //.and().exceptionHandling().accessDeniedPage("/Access_Denied");
////    }
//
//	    @Override
//	    protected void configure(HttpSecurity httpSecurity) throws Exception {
//	        httpSecurity
//	                // we don't need CSRF because our token is invulnerable
//	                .csrf().disable()
//
//	                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//
//	                // don't create session
//	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//
//	                .authorizeRequests()
//	                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//	                // allow anonymous resource requests
//	                .antMatchers(
//	                        HttpMethod.GET,
//	                        "/",
//	                        "/index",
//	                        "/pages/*",
//	                        "/login",
//	                        "/*/login",
//	                        //"/*.html",
//	                        "/favicon.ico",
//	                        //"/**/*.html",
//	                        "/**/*.css",
//	                        "/**/*.js",
//	                        "/**/*.jpg"
//	                ).permitAll()
//	                .antMatchers("/auth/**").permitAll()
//	                .anyRequest().authenticated();
//
//	        // Custom JWT based security filter
//	        httpSecurity
//	                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
//
//	        // disable page caching
//	        httpSecurity.headers().cacheControl();
//	    }
//} 