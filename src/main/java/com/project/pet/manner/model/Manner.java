package com.project.pet.manner.model;

import com.project.pet.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity @Getter
public class Manner {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long MannerId;

    @JoinColumn(name = "applicant_id")
    @ManyToOne(fetch = LAZY)
    private User applicant;

    @JoinColumn(name = "evaluator_id")
    @ManyToOne(fetch = LAZY)
    private User evaluator;

    @Column(nullable = false)
    private double score;

    public void settingAppicant(User user){
        this.applicant = user;
        user.getManners().add(this);
    }
    public void settingEvaluator(User user){
        this.evaluator = user;
    }

}
