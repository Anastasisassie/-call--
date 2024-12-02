package com.app.model;

import com.app.model.enums.ApplicationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Application implements Serializable {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private int number;
    private String date;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(length = 5000)
    private String description;

    @ManyToOne
    private Category category;
    @ManyToOne
    private User client;
    @ManyToOne
    private User employee;
    @ManyToOne
    private User manager;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    private List<ApplicationComment> comments = new ArrayList<>();

    public Application(int number, String date, ApplicationStatus status, String description, Category category, User client, User employee, User manager) {
        this.number = number;
        this.date = date;
        this.status = status;
        this.description = description;
        this.category = category;
        this.client = client;
        this.employee = employee;
        this.manager = manager;
    }

    public List<ApplicationComment> getComments() {
        comments.sort(Comparator.comparing(ApplicationComment::getId));
        Collections.reverse(comments);
        return comments;
    }
}
