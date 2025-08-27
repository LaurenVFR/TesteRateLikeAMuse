package ratelikeamuse.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import ratelikeamuse.entity.Rate;

@SpringBootTest
class RateRepositoryTest {

    private RateRepository rateRepository;

    @BeforeEach
    void setUp() throws IOException {
// cria uma copia vazia do json pra ficar testando 
        File testJsonFile = ResourceUtils.getFile("classpath:rate.json");
        
// Escreve um array JSON vazio
        Files.write(testJsonFile.toPath(), "[]".getBytes());

// cria um repositorio novo
        rateRepository = new RateRepository();
    }

    @Test
    void save_ShouldAssignIdToFirstRate() {

//inicia a lista com esses dados
        Rate newRate = new Rate(null, 101L, 1L, 5);

// salva o novo dado
        Rate savedRate = rateRepository.save(newRate);

// ve se ta tudo certo
        assertNotNull(savedRate);
        assertEquals(1L, savedRate.getId(), "ID tem que começar com 1");
        assertEquals(5, savedRate.getScore());
    }

    @Test
    void save_ShouldIncrementIdForSubsequentRates() {

// primeiro registro
        rateRepository.save(new Rate(null, 101L, 1L, 5)); 
        Rate secondRate = new Rate(null, 102L, 2L, 4);

        Rate savedSecondRate = rateRepository.save(secondRate);

        assertEquals(2L, savedSecondRate.getId(), "O ID do segundo registro deve ser 2");
    }
    
    @Test
    void findByMovieId_ShouldReturnCorrectRates() {
// Adiciona dados de teste
        rateRepository.save(new Rate(null, 101L, 1L, 5));
        rateRepository.save(new Rate(null, 102L, 1L, 4)); 
        rateRepository.save(new Rate(null, 101L, 2L, 3)); 

// Busca as avaliações para o filme de ID 1.
        List<Rate> rates = rateRepository.findByMovieId(1L);

// testa se as avaliações são as certas
        assertEquals(2, rates.size());
// testa se as infos são do movieId certo
        assertTrue(rates.stream().allMatch(r -> r.getMovieId().equals(1L)));
    }

    @Test
    void findByUserId_ShouldReturnCorrectRates() {
        rateRepository.save(new Rate(null, 101L, 1L, 5)); // User 101
        rateRepository.save(new Rate(null, 102L, 1L, 4)); // User 102
        rateRepository.save(new Rate(null, 101L, 2L, 3)); // User 101

        List<Rate> rates = rateRepository.findByUserId(101L);

        assertEquals(2, rates.size());
        assertTrue(rates.stream().allMatch(r -> r.getUserId().equals(101L)));
    }
    
    @Test
    void deleteById_ShouldRemoveTheCorrectRate() {

        Rate rate1 = rateRepository.save(new Rate(null, 101L, 1L, 5)); // Terá ID 1
        rateRepository.save(new Rate(null, 102L, 1L, 4)); // Terá ID 2

        rateRepository.deleteById(rate1.getId()); // Deleta a avaliação de ID 1

// pega todas as avaliações do filme
        List<Rate> rates = rateRepository.findByMovieId(1L);
        assertEquals(1, rates.size());
// entra em loop com os ids que faltam
        assertEquals(2L, rates.get(0).getId());
    }

    @Test
    void findByMovieId_ShouldReturnEmptyListWhenNoRatesExist() {

        List<Rate> rates = rateRepository.findByMovieId(999L);

        assertNotNull(rates);
        assertTrue(rates.isEmpty(), "A lista tem que ta vazia");
    }
}