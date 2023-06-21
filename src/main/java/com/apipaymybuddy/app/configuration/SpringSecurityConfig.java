package com.apipaymybuddy.app.configuration;



import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception {
	    auth.jdbcAuthentication()
	      .dataSource(dataSource)
	      .usersByUsernameQuery("select mail,password,enabled "
	    	        + "from user "
	    	        + "where mail = ?")
	    	      .authoritiesByUsernameQuery("select mail,authority "
	    	        + "from authorities "
	    	        + "where mail = ?");
		
		
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
//		http.authorizeHttpRequests()
//			//.antMatchers("/admin").hasRole("ADMIN")
//			.antMatchers("/user/**").hasRole("USER")
//			//.antMatchers("/user/**").hasRole("ROLE_USER")
//			.anyRequest().authenticated();
		
//		http.authorizeHttpRequests()                 //retourne 401 -> utilisateur non authenfifié
//		//.antMatchers(HttpMethod.OPTIONS, "/**")
//		.antMatchers("/user/**").hasRole("USER")
//		.anyRequest().authenticated().and().httpBasic();
		
//		http.authorizeHttpRequests()                 //retourne 403 -> utilisateur authentifié mais non autorisé à consulté
//		.antMatchers("/user/**").hasRole("USER")
//		.anyRequest().authenticated();
		
		
//		http.authorizeHttpRequests()                 //retourne l'information qq soit les identifiants
//		.antMatchers("/user/**").permitAll()
//		.anyRequest().authenticated();	
		
		
//		http.authorizeHttpRequests()                 //retourne l'information qq soit les identifiants
//		.antMatchers("/user/**").permitAll()
//		.anyRequest().authenticated().and().httpBasic();
		
//				http.authorizeRequests().antMatchers("/user/**")                 //retourne 401 -> utilisateur non authenfifié
//		.hasRole("USER").and().httpBasic();
		
//		http.authorizeRequests().antMatchers(HttpMethod.GET, "/user/**").permitAll().anyRequest().authenticated()
//		.and().httpBasic();
		
//		http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.GET, "/user/**").permitAll().anyRequest().authenticated()
//		.and().httpBasic();
		
		
//		http.httpBasic().and().authorizeRequests().antMatchers("/user/**").hasRole("USER") // ne retourne rien
//		.anyRequest().authenticated();
//	
		
//		http.httpBasic().and().authorizeRequests().antMatchers("/user/**") // ne retourne rien et 401
//		.permitAll().anyRequest().authenticated();
	
		
//		http.httpBasic().and().authorizeHttpRequests().antMatchers("/user/**") // ne retourne rien et 401
//		.permitAll().anyRequest().authenticated();
		
//		http.csrf().disable().httpBasic().and().authorizeHttpRequests().antMatchers("/user/**") // ne retourne rien et 401
//		.permitAll().anyRequest().authenticated();
		
//		http.httpBasic().and().authorizeHttpRequests().antMatchers("/user/**") // ne retourne rien et 401
//		.permitAll().anyRequest().authenticated();
//		http.csrf().disable();
		
//		http.httpBasic().and().authorizeHttpRequests()
//		.antMatchers("/user/**").hasRole("USER")
//		.anyRequest().authenticated();
		
		
//		http.authorizeRequests().antMatchers("/user/**")
//		.permitAll().anyRequest().authenticated().and().httpBasic();
		
        http
        .csrf().disable()
        .authorizeRequests().anyRequest().authenticated()
        .and()
        .httpBasic();
	}
}
