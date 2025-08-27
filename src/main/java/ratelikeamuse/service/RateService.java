package ratelikeamuse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ratelikeamuse.entity.Rate;
import ratelikeamuse.repository.RateRepository;


@Service
public class RateService {
    private final RateRepository rateRepository;
    
    public RateService(RateRepository rateRepository ) {
        this.rateRepository = rateRepository;
    }

    /**
     * Essa função recebe como parametro a ID do user e do movie e uma avaliação(score)
     * Salva a avaliação de um usuário. Se já existir uma avaliação para o mesmo filme e usuário, ela é atualizada
     * Caso contrário, uma nova avaliação é criada
     */
    public Rate saveOrUpdateRate(Long userId, Long movieId, int score) {
       Optional<Rate> existingRateOpt = rateRepository.findByUserIdAndMovieId(userId, movieId);

       if (existingRateOpt.isPresent()) {
          Rate existingRate = existingRateOpt.get();
          existingRate.setScore(score);
          return rateRepository.save(existingRate);
       } else {
         Rate newRate = new Rate(null, userId, movieId, score);
         return rateRepository.save(newRate);
       }
    }
    // Recbe como parametro a ID do user e retorna todas as avaliações feitas por ele
    public List<Rate> myRates (Long userId) {
        return rateRepository.findByUserId(userId);
    }
    
    // Essa função recebe como parametro o ID de um filme e retorna a média de avaliações do filme 
    public int movieRate(Long movieId) {
        List<Rate> movieRates = rateRepository.findByMovieId(movieId);
        if (movieRates.isEmpty()) {
            return 0; 
        }
        int rates = 0;
        for(int i = 0; i < movieRates.size() ; i++) {
            rates += movieRates.get(i).getScore();
        }
        int averageRate = rates / movieRates.size();
        return averageRate;
    }
    // Quando chamada recebe o ID de uma avaliação e deleta ela 
    public void deleteRate(Long id) {
        rateRepository.deleteById(id);
    }
}