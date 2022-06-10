package app.web.pavelk.read2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

@Slf4j(topic = "static-controller")
@Controller
@RequiredArgsConstructor
public class StaticController {

    @Value("${app.host:}")
    private String host;

    @PostConstruct
    public void setBaseUrlStaticFile() throws URISyntaxException, IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("static/main/");
        if (resource != null) {
            Path path = Paths.get(resource.toURI());
            try (Stream<Path> walk = Files.walk(path)) {
                walk.filter(Files::isRegularFile)
                        .filter(f -> f.getFileName().toString().contains("main"))
                        .forEach(f -> {
                            try {
                                String replace = Files.readString(f).replaceFirst("https://localhost:8081", host);
                                Files.write(f, replace.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.SYNC, StandardOpenOption.TRUNCATE_EXISTING);
                                log.info("{}/api/read2/", host);
                            } catch (IOException e) {
                                throw new IllegalArgumentException(e);
                            }
                        });
            }
        }
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

