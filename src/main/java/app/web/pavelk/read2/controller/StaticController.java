package app.web.pavelk.read2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;

@Slf4j(topic = "static-controller")
@Controller
@RequiredArgsConstructor
public class StaticController {

    @Value("${app.host:}")
    private String host;

    @PostConstruct
    public void pathsLog() {
        log.info("{}/api/read2/", host);
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

