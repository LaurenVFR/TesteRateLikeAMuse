package ratelikeamuse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity // Habilita a configuração de segurança web do Spring
public class SecurityConfig {

    // Essa função criptografa as senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cadeia de filtros de segurança do Spring Security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
// Permite acesso à página inicial, registro, e arquivos estáticos 
                .requestMatchers("/", "/register", "/css/**", "/images/**").permitAll()
                
// aqui so quem tiver a função adm pode entrar nos links que tiver isso para poder editar os filmes
                .requestMatchers("/movies/new", "/movies/save", "/movies/edit/**", "/movies/delete/**").hasRole("ADMIN")
                
// pra entrar em qualquer pagina que não seja a de login vc tem que ta logado
                .anyRequest().authenticated()
            )
// Habilita e configura o formulário de login gerenciado pelo Spring Security
            .formLogin(form -> form
// usa o "/" como parte da construção da pagina
                .loginPage("/")
// "/login" libera o spring para processar o login 
                .loginProcessingUrl("/login")
// Se o login der certo, carrega o "/movies"
                .defaultSuccessUrl("/movies", true)
// Se o login der erro, volta para a tela de login com msg de erro
                .failureUrl("/?error=true")
                .permitAll() // libera a pag de login para publico 
            )
// logout
            .logout(logout -> logout
// o link vai ter o "/logout"
                .logoutUrl("/logout")
// depois de sair do perfil volta pro login 
                .logoutSuccessUrl("/?logout")
                .permitAll()
            );

        return http.build();
    }
}