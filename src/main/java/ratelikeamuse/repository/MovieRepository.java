package ratelikeamuse.repository;

import ratelikeamuse.entity.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MovieRepository {

    private final File movieJsonFile;
    private final ObjectMapper objectMapper;
    private final AtomicLong idCounter;

    public MovieRepository() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.movieJsonFile = ResourceUtils.getFile("classpath:movie.json");

        List<Movie> movies = findAllInternal();
        long maxId = movies.stream()
                           .mapToLong(Movie::getId)
                           .max()
                           .orElse(0L);
        this.idCounter = new AtomicLong(maxId);
    }
// pega o json e transforma em lista
    private List<Movie> findAllInternal() {
        try {
            return objectMapper.readValue(movieJsonFile, new TypeReference<List<Movie>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
// salva a lista no json
    private synchronized void saveAllInternal(List<Movie> movies) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(movieJsonFile, movies);
    }

// metodo que me da uma lista com todos os filmes
    public List<Movie> findAll() {
        return findAllInternal();
    }

// filme por id
    public Optional<Movie> findById(Long id) {
        return findAllInternal().stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst();
    }

// salva/atualiza o filme (se for novo vai criar o id mas se o id já existir eu com a permissao de adm vou atualizar)
    public Movie save(Movie movie) {
        try {
            List<Movie> movies = new ArrayList<>(findAllInternal());
            if (movie.getId() == null) {
                movie.setId(idCounter.incrementAndGet());
            }
            movies.removeIf(m -> m.getId().equals(movie.getId()));
            movies.add(movie);
            saveAllInternal(movies);
            return movie;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar filme", e);
        }
    }
//função para não ficar repetindo a lista de filmes

    public long count() {
        return findAllInternal().size();
    }
    
// o nome da função é autoexplicativo
    public void deleteById(Long id) {
        try {
            List<Movie> movies = new ArrayList<>(findAllInternal());
            boolean removed = movies.removeIf(m -> m.getId().equals(id));
            if (removed) {
                saveAllInternal(movies);
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao deletar filme", e);
        }
    }
}
