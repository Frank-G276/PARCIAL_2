package Parcial.pw.Guilombo.Web.Configuration;

import Parcial.pw.Guilombo.Service.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, "/user").hasAnyRole("RECTOR");
                    auth.requestMatchers("/error", "/login", "/logout").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/user/new").hasAnyRole("RECTOR");
                    auth.requestMatchers(HttpMethod.POST, "/user/new").hasAnyRole("RECTOR");
                    auth.requestMatchers(HttpMethod.GET, "/user/editar/**").hasAnyRole("RECTOR");
                    auth.requestMatchers(HttpMethod.PUT, "/user/editar/**").hasAnyRole("RECTOR");
                    auth.requestMatchers(HttpMethod.POST, "/user/delete/**").hasRole("RECTOR");
                    auth.requestMatchers(HttpMethod.GET, "/asignaturas").hasAnyRole("RECTOR", "DOCENTE","ESTUDIANTE");
                    auth.requestMatchers(HttpMethod.GET, "/asignaturas/nuevo").hasAnyRole("RECTOR");
                    auth.requestMatchers(HttpMethod.GET, "/asignaturas/editar/**").hasAnyRole("RECTOR");
                    auth.requestMatchers(HttpMethod.GET, "/asignaturas/eliminar/**").hasAnyRole("RECTOR");
                    auth.requestMatchers(HttpMethod.POST, "/asignaturas/guardar").hasAnyRole("RECTOR");
                    auth.requestMatchers(HttpMethod.GET, "/asignaturas/mis-asignaturas").hasAnyRole("RECTOR", "DOCENTE");
                    auth.requestMatchers(HttpMethod.POST, "/asignaturas/actualizar-horario/").hasAnyRole("RECTOR");
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    login.loginPage("/login");
                    login.successHandler(successHandler());
                    login.failureUrl("/login?error=true");
                    login.permitAll();
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login?logout=true");
                    logout.invalidateHttpSession(true);
                    logout.clearAuthentication(true);
                    logout.deleteCookies("JSESSIONID");
                    logout.permitAll();
                })
                .exceptionHandling(exception -> {
                    exception.accessDeniedHandler(deniedHandler());
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                    session.maximumSessions(1);
                    session.sessionFixation().migrateSession();
                })

                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler deniedHandler() {
        return (request, response, auth) -> {
            response.sendRedirect("/403");
        };
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, auth) -> {
            response.sendRedirect("/asignaturas");
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImp userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
