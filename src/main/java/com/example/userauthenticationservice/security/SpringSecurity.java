//package com.example.userauthenticationservice.security;
//
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.MacAlgorithm;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.crypto.SecretKey;
//
//@Configuration
//public class SpringSecurity {
//
//@Bean
//    SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
// httpSecurity.cors().disable();
// httpSecurity.csrf().disable();
// httpSecurity.authorizeHttpRequests(authorize-> authorize.anyRequest().permitAll());
// return httpSecurity.build();
//
//
//    }
//
//    @Bean
//    BCryptPasswordEncoder bCryptPasswordEncoder() {
//return new BCryptPasswordEncoder();
//}
//
//@Bean
//public SecretKey secretKey() {
//    MacAlgorithm algorithm = Jwts.SIG.HS256;
////    return algorithm.key().build();
//    SecretKey secretKey = algorithm.key().build();
//    return secretKey;
//}
//}

//
//package com.example.userauthenticationservice.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.MacAlgorithm;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.crypto.SecretKey;
//
//@Configuration
//@Import(SecurityConfig.class)
//public class SpringSecurity {
//
//    @Bean
//    @Order(2) // Ensures this security filter applies after OAuth2 filter
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(Customizer.withDefaults()) // Enable CORS with default settings
//                .csrf().disable() // Disable CSRF for simplicity (enable in production)
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/public/**").permitAll()  // Allow public endpoints
//                        .anyRequest().authenticated()
//                )
//                .formLogin(Customizer.withDefaults()) // Enable Form Login
//                .httpBasic(Customizer.withDefaults()); // New recommended approach for HTTP Basic Authentication
//
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password(bCryptPasswordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecretKey secretKey() {
//        MacAlgorithm algorithm = Jwts.SIG.HS256;
//        return algorithm.key().build();
//    }
//}


package com.example.userauthenticationservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import javax.crypto.SecretKey;
import java.util.UUID;

@Configuration
public class SpringSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/actuator/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults())
//                .httpBasic(httpBasic -> httpBasic.disable()); // Updated to avoid deprecated method

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey secretKey() {
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        return algorithm.key().build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .build();

        return new InMemoryRegisteredClientRepository(oidcClient);
    }
}
