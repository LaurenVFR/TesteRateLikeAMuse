package ratelikeamuse.repository;

import ratelikeamuse.entity.User;
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
public class UserRepository {

    private final File userJsonFile;
    private final ObjectMapper objectMapper;
    private final AtomicLong idCounter;

    public UserRepository() throws IOException {
        this.objectMapper = new ObjectMapper();
        
// Localiza o arquivo que vai ser o nosso "banco de dados" (bem entre aspas)
        this.userJsonFile = ResourceUtils.getFile("classpath:users.json");

// Lê todos os usuários existentes para descobrir qual o maior ID.
        List<User> users = findAllInternal();
        long maxId = users.stream()
                         .mapToLong(User::getId)
                         .max()
                         .orElse(0L);
        
// Já começa o contador com o maior id que tiver para não duplicar.
        this.idCounter = new AtomicLong(maxId);
    }

    private List<User> findAllInternal() {
        try {
            return objectMapper.readValue(userJsonFile, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

// Método para salvar a lista completa de usuários no arquivo.

    private synchronized void saveAllInternal(List<User> users) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(userJsonFile, users);
    }

// busca de usuario por id
    public Optional<User> findByUsername(String username) {
        return findAllInternal().stream()
                // ALTERAÇÃO AQUI: .equalsIgnoreCase() em vez de .equals()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

// verifica se o nick já existe
    public boolean existsByUsername(String username) {
        // ALTERAÇÃO AQUI: .equalsIgnoreCase() em vez de .equals()
        return findAllInternal().stream().anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

// salva o usuario
    public User save(User user) {
        try {
            List<User> users = new ArrayList<>(findAllInternal());

// gera o ID
            if (user.getId() == null) {
                user.setId(idCounter.incrementAndGet());
            }
            users.removeIf(u -> u.getId().equals(user.getId()));          
            users.add(user);
// Salva a lista inteira de volta no arquivo.
            saveAllInternal(users);
            
            return user;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar usuário", e);
        }
    }
}