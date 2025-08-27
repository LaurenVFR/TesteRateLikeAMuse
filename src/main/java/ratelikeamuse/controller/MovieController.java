package ratelikeamuse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ratelikeamuse.entity.Movie;
import ratelikeamuse.service.MovieService;
import ratelikeamuse.service.RateService;

@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final RateService rateService;

    public MovieController(MovieService movieService, RateService rateService) {
        this.movieService = movieService;
        this.rateService = rateService;
    }
// lista de todos os filmes
@GetMapping
	public String listMovies(Model model) {
// Pega todos os filmes
    	List<Movie> movies = movieService.getAllMovies();
    	
// Cria um mapa para armazenar a média de avaliação de cada filme
    	Map<Long, Double> averageRates = new HashMap<>();
    	for (Movie movie : movies) {
    		double averageRate = rateService.movieRate(movie.getId());
    		averageRates.put(movie.getId(), averageRate);
    	}
    	
// Adiciona os filmes e as médias ao modelo para o HTML
    	model.addAttribute("movies", movies);
    	model.addAttribute("averageRates", averageRates);
    	
    	return "movies/list";
	}

// Retorna formulário para criar filme
    @GetMapping("/new")
    public String showCreateForm(Model model) {
// Envia um objeto movie vazio para o formulário usar
        model.addAttribute("movie", new Movie());
        return "movies/form";
    }
// salva o filme novo com as infos
	@PostMapping("/save")
	public String saveMovie(@ModelAttribute Movie movie) {
	    // O Spring automaticamente preenche o objeto 'movie'
	    // A lógica de saveOrUpdate no Service fará o resto
 	movieService.saveMovie(movie);
 	return "redirect:/movies";
	}

// edita o filme
    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Movie> movieOpt = movieService.getMovieById(id);
        if (movieOpt.isPresent()) {
            model.addAttribute("movie", movieOpt.get());
            return "movies/form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Filme não encontrado!");
            return "redirect:/movies";
        }
    }

// apaga o filme da lista
    @PostMapping("/delete")
    public String deleteMovie(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            movieService.deleteMovie(id);
            redirectAttributes.addFlashAttribute("successMessage", "Filme excluído com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir filme!");
        }
        return "redirect:/movies";
    }
}