package com.mlh.mlh.service;

import com.mlh.mlh.model.WikiData;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WikiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String WIKI_API_URL = "https://en.wikipedia.org/w/api.php";

    public WikiService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public WikiData getContent(String term) {
        WikiData wikiData = new WikiData();
        wikiData.setTerm(term);

        try {
            String url = buildUrl(term);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                processResponse(wikiData, response.getBody());
            } else {
                handleError(wikiData, "HTTP Error: " + response.getStatusCodeValue());
            }
        } catch (HttpClientErrorException e) {
            handleError(wikiData, "API Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            handleError(wikiData, "General Error: " + e.getMessage());
        }

        return wikiData;
    }

    private String buildUrl(String term) {
        return UriComponentsBuilder.fromHttpUrl(WIKI_API_URL)
                .queryParam("action", "query")
                .queryParam("format", "json")
                .queryParam("prop", "extracts")
                .queryParam("exintro", "")
                .queryParam("explaintext", "")
                .queryParam("titles", term)
                .encode()
                .toUriString();
    }

    private void processResponse(WikiData wikiData, String jsonResponse) throws Exception {
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode pages = root.path("query").path("pages");
        JsonNode page = pages.elements().hasNext() ? pages.elements().next() : null;

        if (page == null || page.has("missing")) {
            wikiData.setStatus("error");
            wikiData.setApiMessage("Page does not exist");
        } else {
            wikiData.setTitle(page.path("title").asText());
            wikiData.setContent(page.path("extract").asText());
            wikiData.setStatus("found");
        }
    }

    private void handleError(WikiData wikiData, String errorMessage) {
        wikiData.setStatus("error");
        wikiData.setError(errorMessage);
        wikiData.setApiMessage("Failed to fetch data from Wikipedia API");
    }
}