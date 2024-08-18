package lt.techin.bookreservationapp.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
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
    return http.csrf(
            (csrf) ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(
            authorize -> {
              authorize.anyRequest().authenticated();
            })
        .oauth2Login(
            oauth2 -> {
              oauth2.loginPage("http://localhost:5173/login").permitAll();
              oauth2.userInfoEndpoint(c -> c.userService(customOAuth2UserService));
              oauth2.successHandler(oAuth2LoginSuccessHandler);
            })
        .logout(
            (logout) -> logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()))
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // configuration.setAllowedOrigins(List.of(frontendUrl));
    configuration.addAllowedOrigin(frontendUrl);
    // configuration.addAllowedHeader("*");
    configuration.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN"));
    // configuration.addAllowedMethod("*");
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
        new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);

    return urlBasedCorsConfigurationSource;
  }
}
