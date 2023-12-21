package lt.techin.bookreservationapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) 
			throws Exception {
		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/signup").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/h2-console/**").permitAll()
						.anyRequest().authenticated())
				.headers(headers -> headers.frameOptions(f -> f.disable()))
				.csrf(csrf -> csrf.disable())
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
