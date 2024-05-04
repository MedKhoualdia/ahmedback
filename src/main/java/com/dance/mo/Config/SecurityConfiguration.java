package com.dance.mo.Config;


import com.dance.mo.Entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration  {
    @Autowired
    private OAuth2LoginSuccessHandler OAuth2LoginSuccessHandler;

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;




   /* @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowCredentials(true)
                        .allowedHeaders("*");
            }
        };
    }*/
   @Bean
   public CorsFilter corsFilter() {
       CorsConfiguration config = new CorsConfiguration();
       config.setAllowCredentials(true); // Allow credentials (cookies)
       config.addAllowedOrigin("http://localhost:4200"); // Add the origin of your frontend application
       config.addAllowedHeader("*");
       config.addAllowedMethod("*");
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/**", config);

       return new CorsFilter(source);
   }




    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity//disable cross site req forgery
                .csrf(AbstractHttpConfigurer::disable)
                //my white list permitted
                .authorizeHttpRequests(req ->
                req.requestMatchers( "/user/**","/signUp/**","/admin/**","/succes/**","/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().authenticated())




                ///session stateless
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                ///execute filter before UsernamePasswordAuthentication
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2.successHandler(OAuth2LoginSuccessHandler)

                        .clientRegistrationRepository(clientRegistrationRepository())
                );
        return httpSecurity.build();
    }
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = new ArrayList<>();
        registrations.add(
                ClientRegistration.withRegistrationId("google")
                        .clientId("595644901644-l8e6vv6eg1d8t15svj7722gs1k1j5nvd.apps.googleusercontent.com")
                        .clientSecret("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")
                        .scope("email", "profile")
                        .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                        .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                        .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                        .clientName("Google")
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .userNameAttributeName("sub")
                        .redirectUri("http://localhost:8088/login/oauth2/code/google")
                        .build()
        );
        registrations.add(
                ClientRegistration.withRegistrationId("facebook")
                        .clientId("3627885030786171")
                        .clientSecret("0216027f5c4e2e7066a08ffe7b44b560")
                        .scope("email", "public_profile")
                        .authorizationUri("https://www.facebook.com/v19.0/dialog/oauth")
                        .tokenUri("https://graph.facebook.com/v19.0/oauth/access_token")
                        .userInfoUri("https://graph.facebook.com/me?fields=id,name,first_name,middle_name,last_name,email,birthday")
                        .clientName("Facebook")
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .userNameAttributeName("id")
                        .redirectUri("http://localhost:8088/login/oauth2/code/facebook")
                        .build()
        );

        return new InMemoryClientRegistrationRepository(registrations);
    }


}
