package ratelikeamuse.entity; 

import java.util.Objects;

// atributos
public class Rate {

    private Long id;        // ID da avaliação
    private Long userId;    
    private Long movieId;   
    private int score;      


    public Rate() {}

// Construtor
    public Rate(Long id, Long userId, Long movieId, int score) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.score = score;
    }

// Gets e Sets 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

// equal e hash para id unico e evitar duplicidade
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Objects.equals(id, rate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", userId=" + userId +
                ", movieId=" + movieId +
                ", score=" + score 
                + '}';
    }
}