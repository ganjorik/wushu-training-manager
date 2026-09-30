package com.wushu.config;

import com.wushu.security.CustomAuthenticationSuccessHandler;
import com.wushu.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
	private final CustomAuthenticationSuccessHandler successHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration configuration)
			throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http)
		throws Exception {

		http
				.csrf(csrf -> csrf.disable())

				.userDetailsService(userDetailsService)

				.authorizeHttpRequests(auth -> auth

						.requestMatchers("/login")
						.permitAll()

						.requestMatchers("/coaches/**")
						.hasRole("ADMIN")

						.requestMatchers("/students/**")
						.hasRole("ADMIN")

						.requestMatchers("/exercises/**")
						.hasRole("ADMIN")

						.requestMatchers(HttpMethod.GET, "/groups")
						.hasAnyRole("ADMIN", "COACH")

						.requestMatchers(HttpMethod.GET, "/groups/{id}")
						.hasAnyRole("ADMIN", "COACH")

						.requestMatchers(HttpMethod.POST, "/groups")
						.hasRole("ADMIN")

						.requestMatchers(HttpMethod.POST, "/groups/*/students")
						.hasRole("ADMIN")

						.requestMatchers(HttpMethod.POST, "/groups/*/students/*/delete")
						.hasRole("ADMIN")

						.requestMatchers("/groups/new", "/groups/edit/**", "/groups/delete/**")
						.hasRole("ADMIN")

						.requestMatchers("/trainings/**")
						.hasAnyRole("ADMIN", "COACH")

						.requestMatchers(HttpMethod.GET, "/api/v1/students/**")
						.permitAll()

						.anyRequest()
						.authenticated()
				)

				.formLogin(login -> login

						.loginPage("/login")

						.successHandler(successHandler)

						.permitAll()
				)

				.exceptionHandling(exception -> exception

						.accessDeniedPage("/403")
				)

				.logout(logout -> logout

						.logoutUrl("/logout")

						.logoutSuccessUrl("/login?logout")

						.permitAll()

				);

		return http.build();
	}
}
