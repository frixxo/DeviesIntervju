package DeviesIntervju.API.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Book {
    public int id;
    public String title;
    public int publishYear;
    public String author;
    public Genre genre;
    public int pageCount;

    public Book(int id, String title, int publishYear, String author, Genre genre, int pageCount) {
        this.id = id;
        this.title = title;
        this.publishYear = publishYear;
        this.author = author;
        this.genre = genre;
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}

