package com.mlh.mlh.model;

public class WikiData {
    private String term;
    private String title;
    private String content;
    private String status;
    private String error;
    private String apiMessage;

    public WikiData() {}

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getApiMessage() { return apiMessage; }
    public void setApiMessage(String apiMessage) { this.apiMessage = apiMessage; }
}