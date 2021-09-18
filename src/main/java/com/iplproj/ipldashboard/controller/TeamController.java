package com.iplproj.ipldashboard.controller;

import com.iplproj.ipldashboard.model.Match;
import com.iplproj.ipldashboard.model.Team;
import com.iplproj.ipldashboard.repository.MatchRepository;
import com.iplproj.ipldashboard.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by sridharrajagopal on 9/17/21.
 */
@RestController
@CrossOrigin(origins = "*")
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MatchRepository matchRepository;

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable("teamName") String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.findLatestMatchesByName(team.getTeamName(), 4));
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatches(@PathVariable("teamName") String teamName, @RequestParam int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
        //return matchRepository.findByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(teamName, startDate, endDate, teamName, startDate, endDate);
        return matchRepository.getMatchesByTeamBetweenDates(teamName, startDate, endDate);
    }
}
