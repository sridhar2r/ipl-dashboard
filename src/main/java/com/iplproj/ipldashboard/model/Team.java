package com.iplproj.ipldashboard.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sridharrajagopal on 9/16/21.
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String teamName;
    private long totalMatches;
    private long totalWins;
    @Transient
    private List<Match> matches;

    public Team(String teamName, long totalMatches) {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }
}
