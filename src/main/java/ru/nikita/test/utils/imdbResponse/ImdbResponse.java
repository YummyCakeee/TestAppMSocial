package ru.nikita.test.utils.imdbResponse;

import java.util.List;

public class ImdbResponse {
    private List<Results> results;

    public ImdbResponse() {}

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }
}
