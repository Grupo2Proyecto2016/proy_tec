package com.springmvc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.springmvc", "com.springmvc.logic"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
		@Bean
		public static PropertySourcesPlaceholderConfigurer properties() {
		    final PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		    propertySourcesPlaceholderConfigurer.setLocation(new ClassPathResource("/application.properties"));
		    return propertySourcesPlaceholderConfigurer;
	    }
	
	 	@Autowired
	    private JwtAuthenticationEntryPoint unauthorizedHandler;

	    @Autowired
	    private UserDetailsService userDetailsService;

	    @Autowired
	    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
	        authenticationManagerBuilder
	                .userDetailsService(this.userDetailsService)
	                .passwordEncoder(passwordEncoder());
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }

	    @Bean
	    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
	        JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter();
	        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
	        return authenticationTokenFilter;
	    }

	    @Override
	    protected void configure(HttpSecurity httpSecurity) throws Exception {
	        httpSecurity
	                .csrf().disable()
	                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	                .authorizeRequests()
	                .antMatchers("/auth").permitAll()
	                .antMatchers("/*/auth/**").permitAll()
	                //.antMatchers("/**/auth/**").permitAll()
	                .antMatchers("/*/tenantExist/**").permitAll()
	                .anyRequest().authenticated();

	        // Custom JWT based security filter
	        httpSecurity
	                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

	        // disable page caching
	        httpSecurity.headers().cacheControl();
	    }
} 