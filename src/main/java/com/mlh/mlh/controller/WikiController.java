package com.mlh.mlh.controller;

import com.mlh.mlh.model.WikiData;
import com.mlh.mlh.service.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WikiController {

    @Autowired
    private WikiService wikiService;

    @GetMapping("/api/search")
    public Map<String, Object> search(@RequestParam String term) {
        WikiData wikiData = wikiService.getContent(term);
        Map<String, Object> response = new HashMap<>();

        response.put("term", term);
        response.put("title", wikiData.getTitle());
        response.put("content", wikiData.getContent());
        response.put("status", wikiData.getStatus());
        response.put("error", wikiData.getError());
        response.put("apiMessage", wikiData.getApiMessage());

        return response;
    }
}