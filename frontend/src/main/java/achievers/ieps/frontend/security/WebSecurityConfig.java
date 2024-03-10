package achievers.ieps.frontend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers( new AntPathRequestMatcher("/css/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/assets/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/static/**")).permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/profile/view").permitAll()
                        .requestMatchers("/test").permitAll()

                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/validasi-vendor").hasAnyAuthority("PROCSTAFF", "PROCMANAGER")
                        .requestMatchers("/vendor-assessment/**").hasAnyAuthority("VENDOR", "VENDOR_NOT_VALID")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login-form")
                        .permitAll()
                )
                .logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .clearAuthentication(true));
        return http.build();
    }
}
