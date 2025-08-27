package ratelikeamuse.entity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

// define que o usuario tem esses atributos e implementa UserDetails do Spring
public class User implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Funcao funcao;

    public User() {
    }

//construtor
    public User(Long id, String username, String password, Funcao funcao) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.funcao = funcao;
    }
    
// Gets e Sets
    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    @Override 
    public String getUsername() { 
        return this.username; 
    }

    public void setUsername(String username) {
         this.username = username; 
    }


    @Override 
    public String getPassword() { 
        return this.password; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public Funcao getFuncao() { 
        return funcao; 
    }

    public void setFuncao(Funcao funcao) { 
        this.funcao = funcao; 
    }

// libera as permicoes do que a pessoa pode fazer de acordo com a função
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (funcao == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + funcao.name()));
    }
// não desloga sozinho
    @Override 
    @JsonIgnore 
    public boolean isAccountNonExpired() { 
        return true; 
    }
// não bloqueia
    @Override 
    @JsonIgnore 
    public boolean isAccountNonLocked() { 
        return true; 
    }
// senha eterna
    @Override 
    @JsonIgnore 
    public boolean isCredentialsNonExpired() { 
        return true; 
    }
// sempre vai poder avaliar
    @Override 
    @JsonIgnore 
    public boolean isEnabled() { 
        return true; 
    }
// Equals e has para comparar
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
// cria a string 
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", funcao=" + funcao + '}';
    }
}