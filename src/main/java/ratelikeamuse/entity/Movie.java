package ratelikeamuse.entity;

import java.util.Objects;

public class Movie {
    private Long id;
    private String nome;
    private String genero;
    private int ano;
    private String posterUrl;

// precisa desse construtor vazio pq é aqui que o json vai criar o objeto que vai pra lista    
    public Movie() {}

// construtor com os parametros
    public Movie(Long id, String nome, String genero, int ano, String posterUrl) {
        this.id = id;
        this.nome = nome;
        this.genero = genero;
        this.ano = ano;
        this.posterUrl = posterUrl;
    }

// Gets e Sets
    public Long getId() {
        return id; 
    }

    public void setId(Long id) {
         this.id = id; 
    }

    public String getNome() { 
        return nome; 
    }

    public void setNome(String nome) { 
        this.nome = nome; 
    }

    public String getGenero() { 
        return genero; 
    }

    public void setGenero(String genero) { 
        this.genero = genero; 
    }

    public int getAno() { 
        return ano; 
    }

    public void setAno(int ano) {
         this.ano = ano; 
    }

    public String getPosterUrl() { 
        return posterUrl; 
    }

    public void setPosterUrl(String posterUrl) {
         this.posterUrl = posterUrl; 
    }

// compara os filmes pra nao repetir filme 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Movie) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}