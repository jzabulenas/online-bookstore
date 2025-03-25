package lt.techin.bookreservationapp.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lt.techin.bookreservationapp.role.RoleRepository;
import lt.techin.bookreservationapp.user.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final String frontendUrl;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  SecurityConfig(
      @Value("${frontend.url}") String frontendUrl,
      UserRepository userRepository,
      RoleRepository roleRepository) {
    this.frontendUrl = frontendUrl;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

    return http.build();

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
    //        .csrf(csrf -> csrf.disable())
    //        .httpBasic();
    //
    //    return http.build();
  }

  //  @Bean
  //  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  //
  //    return http.csrf(
  //            (csrf) ->
  //                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
  //                    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()))
  //        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
  //        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
  //        .authorizeHttpRequests(
  //            authorize -> {
  //              authorize.anyRequest().authenticated();
  //            })
  //        .formLogin(Customizer.withDefaults())
  //        .oneTimeTokenLogin(
  //            configurer -> {
  //              configurer.tokenGenerationSuccessHandler(
  //                  (request, response, oneTimeToken) -> {
  //                    System.out.println(oneTimeToken.getUsername());
  //
  //                    var token = oneTimeToken.getTokenValue();
  //
  //                    var msg = "Please go to http://localhost:8080/login/ott?token=" + token;
  //                    System.out.println(msg);
  //
  //                    response.setContentType(MediaType.TEXT_HTML_VALUE);
  //                    response.getWriter().write("You've got console mail!");
  //
  //                    if (!this.userRepository.existsByEmail(oneTimeToken.getUsername())) {
  //                      Optional<Role> role = this.roleRepository.findByName("ROLE_USER");
  //
  //                      User user = new User();
  //                      user.setEmail(oneTimeToken.getUsername());
  //                      user.setRoles(List.of(role.get()));
  //                      this.userRepository.save(user);
  //                    }
  //                  });
  //              // configurer.tokenService(
  //              // new OneTimeTokenService() {
  //              //
  //              // @Override
  //              // public OneTimeToken generate(GenerateOneTimeTokenRequest
  //              // request) {
  //              //
  //              // return null;
  //              // }
  //              //
  //              // @Override
  //              // public OneTimeToken consume(
  //              // OneTimeTokenAuthenticationToken authenticationToken) {
  //              //
  //              // return null;
  //              // }
  //              // });
  //              configurer.authenticationSuccessHandler(
  //                  (request, response, authentication) -> {
  //                    response.sendRedirect(this.frontendUrl + "/oauth2/redirect");
  //                  });
  //            })
  //
  //        // .oauth2Login(
  //        // oauth2 -> {
  //        // oauth2.loginPage("http://localhost:5173/login").permitAll();
  //        // oauth2.userInfoEndpoint(c -> c.userService(customOAuth2UserService));
  //        // oauth2.successHandler(oAuth2LoginSuccessHandler);
  //        // })
  //        // .logout(
  //        // (logout) -> logout.logoutSuccessHandler(new
  //        // HttpStatusReturningLogoutSuccessHandler()))
  //        .build();
  //  }

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

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
