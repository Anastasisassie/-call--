package com.app.model;

import com.app.controller.global.Global;
import com.app.model.enums.ApplicationStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ApplicationComment implements Serializable {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String dateAndTime = Global.getDateAndTimeNow();

    @Column(length = 5000)
    private String text;

    @ManyToOne
    private Application application;
    @ManyToOne
    private User owner;

    public ApplicationComment(String text, Application application, User owner) {
        this.text = text;
        this.application = application;
        this.owner = owner;
    }
}
