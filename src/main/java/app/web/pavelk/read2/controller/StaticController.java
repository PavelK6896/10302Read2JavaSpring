package app.web.pavelk.read2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j(topic = "static-controller")
@Controller
@RequiredArgsConstructor
public class StaticController {

    @GetMapping("/")
    public String main() {
        return "redirect:/main";
    }

    @GetMapping({"/main"})
    public String homePage() {
        return "/main/index.html";
    }

}

