package app.web.pavelk.read2.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;

@Slf4j(topic = "static-controller")
@Controller
@AllArgsConstructor
public class StaticController {

    @PostConstruct
    public void pathsLog() {
        log.info("http://localhost:8080/api/read2/");
    }

    @GetMapping("/")
    public String main() {
        return "redirect:/main";
    }

    @GetMapping({"/main"})
    public String homePage() {
        return "/main/index.html";
    }

}

