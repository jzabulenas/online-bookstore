package lt.techin.bookreservationapp.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CustomOAuth2UserService customOAuth2UserService;
  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  private final String frontendUrl;

  public SecurityConfig(
      CustomOAuth2UserService customOAuth2UserService,
      OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
      @Value("${frontend.url}") String frontendUrl) {
    this.customOAuth2UserService = customOAuth2UserService;
    this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    this.frontendUrl = frontendUrl;
  }

  //  @Bean
  //  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  //    http.authorizeHttpRequests(
  //            authorize ->
  //                authorize
  //                    .requestMatchers("/signup")
  //                    .permitAll()
  //                    .requestMatchers("/login")
  //                    .permitAll()
  //                    .requestMatchers("/h2-console/**")
  //                    .permitAll()
  //                    .requestMatchers(HttpMethod.POST, "/categories")
  //                    .hasRole("ADMIN")
  //                    .requestMatchers(HttpMethod.PUT, "/categories/**")
  //                    .hasRole("ADMIN")
  //                    .requestMatchers(HttpMethod.DELETE, "/categories/**")
  //                    .hasRole("ADMIN")
  //                    .anyRequest()
  //                    .authenticated())
  //        .headers(headers -> headers.frameOptions(f -> f.disable()))
  //        .csrf(csrf -> csrf.disable())
  //        .httpBasic(Customizer.withDefaults());
  //
  //    return http.build();
  //  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(Customizer.withDefaults())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(
            auth -> {
              auth.anyRequest().authenticated();
            })
        .oauth2Login(
            oath2 -> {
              // oath2.loginPage("http://localhost:5173/login").permitAll();
              oath2.userInfoEndpoint(c -> c.userService(customOAuth2UserService));
              oath2.successHandler(oAuth2LoginSuccessHandler);
            })
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(frontendUrl));
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
        new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);

    return urlBasedCorsConfigurationSource;
  }
}
