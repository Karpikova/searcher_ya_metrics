package com.searcher.searcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class SearcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearcherApplication.class, args);
    }
}
