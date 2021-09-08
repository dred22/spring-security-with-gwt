package com.eazybytes.config;

import com.eazybytes.filter.JWTTokenGeneratorFilter;
import com.eazybytes.filter.JWTTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * /myAccount - Secured /myBalance - Secured /myLoans - Secured /myCards -
	 * Secured /notices - Not Secured /contact - Not Secured
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
		cors().configurationSource(request -> {
		CorsConfiguration config = new CorsConfiguration();
			config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
			//config.setAllowedOrigins(Collections.singletonList("*"));
			config.setAllowedMethods(Collections.singletonList("*"));
			config.setAllowCredentials(true);
			config.setAllowedHeaders(Collections.singletonList("*"));
				config.setExposedHeaders(Arrays.asList("Authorization"));
			config.setMaxAge(3600L);
			return config;
		}).and().csrf().disable()
/*				.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
				.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)*/
				.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
				.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
/*				.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)*/
				.authorizeRequests()
				.antMatchers("/myAccount").hasAnyRole("USER")
				.antMatchers("/myBalance").hasAnyRole("USER","ADMIN")
				.antMatchers("/myLoans").hasRole("ROOT")
				.antMatchers("/myCards").authenticated()
				.antMatchers("/user").authenticated()
				.antMatchers("/notices").permitAll()
				.antMatchers("/contact").permitAll().and().httpBasic();
		//http.headers().frameOptions().disable();
	}
	
	/*
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception {
	 * auth.inMemoryAuthentication().withUser("admin").password("12345").authorities
	 * ("admin").and(). withUser("user").password("12345").authorities("read").and()
	 * .passwordEncoder(NoOpPasswordEncoder.getInstance()); }
	 */

	/*
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception { InMemoryUserDetailsManager userDetailsService = new
	 * InMemoryUserDetailsManager(); UserDetails user =
	 * User.withUsername("admin").password("12345").authorities("admin").build();
	 * UserDetails user1 =
	 * User.withUsername("user").password("12345").authorities("read").build();
	 * userDetailsService.createUser(user); userDetailsService.createUser(user1);
	 * auth.userDetailsService(userDetailsService); }
	 */

	/*
	 * @Bean public UserDetailsService userDetailsService(DataSource dataSource) {
	 * return new JdbcUserDetailsManager(dataSource); }
	 */

	@Bean
	public PasswordEncoder passwordEncoder() {
		//return new BCryptPasswordEncoder();
		return NoOpPasswordEncoder.getInstance();
	}
}
