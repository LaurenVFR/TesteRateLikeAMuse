package ratelikeamuse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// controla o index, que é a pagina inicial com o login e criação de conta
@Controller
public class IndexController {

    @GetMapping("/")
    public String showIndexPage() {
        return "index"; 
    }

}
