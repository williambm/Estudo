package com.wbm.mykeycloack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Classe padrão de configuração de Security do spring
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeConfig -> {
                    authorizeConfig.requestMatchers("/public").permitAll(); // Rotas públicas liberadas
                    authorizeConfig.requestMatchers("/logout").permitAll(); // Libera o endpoint de logout
                    authorizeConfig.requestMatchers("/api").authenticated(); // Rotas privadas exigem autenticação
                    authorizeConfig.anyRequest().authenticated(); // Todas as outras rotas exigem autenticação
                })
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/home", true) // Redireciona para /home após o login
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Configura o endpoint de logout
                        .addLogoutHandler(keycloakLogoutHandler()) // Adiciona o logout no Keycloak
                        .logoutSuccessUrl("/public") // Redireciona para /public após o logout
                )
                .build();
    }

    /**
     * Configura um LogoutHandler para fazer logout no Keycloak.
     */
    private LogoutHandler keycloakLogoutHandler() {
        return (request, response, authentication) -> {
            try {
                // Redireciona para o endpoint de logout do Keycloak
                response.sendRedirect("http://localhost:8080/realms/myrealm/protocol/openid-connect/logout");
            } catch (Exception e) {
                throw new RuntimeException("Erro ao fazer logout no Keycloak", e);
            }
        };
    }
}
