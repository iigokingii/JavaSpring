package com.courseproject.courseproject.Configuration;

import com.courseproject.courseproject.Filter.JwtAuthenticationFilter;
import com.courseproject.courseproject.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthentificationFilter;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	@Bean
	public AuthenticationProvider authenticationProvider(){
		System.out.println("[SecurityConfig]-AuthentucationProvider");
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userService.userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		System.out.println("[SecurityConfig]-Authentucation Manager");
		return config.getAuthenticationManager();
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		System.out.println("[SecurityConfig]-Secutity Filter Chain");
		
		http
				.csrf(csrf->csrf.disable())
				.authorizeHttpRequests(request->request
								
								//.requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll()
								//.requestMatchers(HttpMethod.GET,"/api/v1/test/**").permitAll()
								//.anyRequest().authenticated()
						
								//авторизация/регистрация
								.requestMatchers(HttpMethod.GET,"/").permitAll()
								.requestMatchers(HttpMethod.GET,"/SignUp").permitAll()
								.requestMatchers(HttpMethod.GET,"/SignIn").permitAll()
								.requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll()
								.requestMatchers(HttpMethod.GET,"/api/v1/**").permitAll()
//								//статические ресурсы
								.requestMatchers(HttpMethod.GET,"/Admin/**").permitAll()
								.requestMatchers(HttpMethod.GET,"/Shared/**").permitAll()
								.requestMatchers(HttpMethod.GET,"/images/**").permitAll()
								.requestMatchers(HttpMethod.GET,"/MainPage/**").permitAll()
								.requestMatchers(HttpMethod.GET,"/SignInJS/**").permitAll()
								.requestMatchers(HttpMethod.GET,"/SignUpJS/**").permitAll()
								//на все остальное
								.anyRequest().authenticated()
						//.anyRequest().permitAll()
				)
				.sessionManagement(session->session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthentificationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	
}
