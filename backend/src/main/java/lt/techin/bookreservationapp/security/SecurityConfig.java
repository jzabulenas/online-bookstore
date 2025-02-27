package lt.techin.bookreservationapp.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lt.techin.bookreservationapp.user.User;
import lt.techin.bookreservationapp.user.UserRepository;

@Configuration
@EnableWebSecurity
class SecurityConfig {

  // private final CustomOAuth2UserService customOAuth2UserService;
  // private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  private final String frontendUrl;
  private final UserRepository userRepository;

  SecurityConfig(
      // CustomOAuth2UserService customOAuth2UserService,
      // OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
      @Value("${frontend.url}") String frontendUrl, UserRepository userRepository) {

    // this.customOAuth2UserService = customOAuth2UserService;
    // this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    this.frontendUrl = frontendUrl;
    this.userRepository = userRepository;
  }

  // @Bean
  // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
  // Exception {
  // http.authorizeHttpRequests(
  // authorize ->
  // authorize
  // .requestMatchers("/signup")
  // .permitAll()
  // .requestMatchers("/login")
  // .permitAll()
  // .requestMatchers("/h2-console/**")
  // .permitAll()
  // .requestMatchers(HttpMethod.POST, "/categories")
  // .hasRole("ADMIN")
  // .requestMatchers(HttpMethod.PUT, "/categories/**")
  // .hasRole("ADMIN")
  // .requestMatchers(HttpMethod.DELETE, "/categories/**")
  // .hasRole("ADMIN")
  // .anyRequest()
  // .authenticated())
  // .headers(headers -> headers.frameOptions(f -> f.disable()))
  // .csrf(csrf -> csrf.disable())
  // .httpBasic(Customizer.withDefaults());
  //
  // return http.build();
  // }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

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
        .formLogin(Customizer.withDefaults())
        .oneTimeTokenLogin(
            configurer -> {
              configurer.tokenGenerationSuccessHandler(
                  (request, response, oneTimeToken) -> {
                    System.out.println(oneTimeToken.getUsername());

                    var token = oneTimeToken.getTokenValue();

                    var msg = "Please go to http://localhost:8080/login/ott?token=" + token;
                    System.out.println(msg);

                    response.setContentType(MediaType.TEXT_HTML_VALUE);
                    response.getWriter().write("You've got console mail!");

                    if (!this.userRepository.existsByEmail(oneTimeToken.getUsername())) {
                      User user = new User();
                      user.setEmail(oneTimeToken.getUsername());
                      user.setRole("ROLE_USER");
                      this.userRepository.save(user);
                    }
                  });
              // configurer.tokenService(
              // new OneTimeTokenService() {
              //
              // @Override
              // public OneTimeToken generate(GenerateOneTimeTokenRequest
              // request) {
              // // TODO Auto-generated method stub
              // return null;
              // }
              //
              // @Override
              // public OneTimeToken consume(
              // OneTimeTokenAuthenticationToken authenticationToken) {
              // // TODO Auto-generated method stub
              // return null;
              // }
              // });
              configurer.authenticationSuccessHandler(
                  (request, response, authentication) -> {
                    response.sendRedirect(this.frontendUrl + "/oauth2/redirect");
                  });
            })

        // .oauth2Login(
        // oauth2 -> {
        // oauth2.loginPage("http://localhost:5173/login").permitAll();
        // oauth2.userInfoEndpoint(c -> c.userService(customOAuth2UserService));
        // oauth2.successHandler(oAuth2LoginSuccessHandler);
        // })
        // .logout(
        // (logout) -> logout.logoutSuccessHandler(new
        // HttpStatusReturningLogoutSuccessHandler()))
        .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // configuration.setAllowedOrigins(List.of(frontendUrl));
    // configuration.addAllowedHeader("*");
    // configuration.addAllowedMethod("*");

    configuration.addAllowedOrigin(frontendUrl);
    configuration.setAllowedHeaders(List.of("Content-Type", "X-XSRF-TOKEN"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
        new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);

    return urlBasedCorsConfigurationSource;
  }
}
