package ru.project.forpopcorn.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title; // Название фильма

    @Column(columnDefinition = "text")
    private String textMini; // Описание

    @Column(columnDefinition = "text")
    private String textLarge; // Описание

    @OneToMany(mappedBy = "movieRate",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratingList = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonManagedReference
    @JsonIgnore
    private List<Genre> genres = new ArrayList<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public Movie(int id, String title, String textMini, String textLarge) {
        this.id = id;
        this.title = title;
        this.textMini = textMini;
        this.textLarge = textLarge;
    }

    public Movie(String title, String textMini, String textLarge) {
        this.title = title;
        this.textMini = textMini;
        this.textLarge = textLarge;
    }

    public Movie(int id, String title, String textMini, String textLarge, List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.textMini = textMini;
        this.textLarge = textLarge;
        this.genres = genres;
    }

    //Страна
    //Продолжительность


}
