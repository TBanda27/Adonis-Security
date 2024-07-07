package com.email_verification.adonis.registration;

import com.email_verification.adonis.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import static org.springframework.security.config.Customizer.withDefaults;


@Service
@AllArgsConstructor
public class UserRegistrationDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserRegistrationDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("User with email: "+ email + " not found"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/registration/**").permitAll()
                        .requestMatchers("/api/v1/users/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
//                .formLogin(withDefaults())
                .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/perform-login")
                        .defaultSuccessUrl("/home",  true)
                        .failureUrl("/login?error=true")
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler()).permitAll())
                .logout(logout -> logout.logoutUrl("/perform-logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .build();

    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return ((request, response, exception) -> response.sendRedirect("/login?error=true"));
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> response.sendRedirect("/home");
    }
}
