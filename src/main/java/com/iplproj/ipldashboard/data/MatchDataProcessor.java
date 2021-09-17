package com.iplproj.ipldashboard.data;

import com.iplproj.ipldashboard.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

/**
 * Created by sridharrajagopal on 9/15/21.
 */
public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) throws Exception {
        Match match = Match.builder()
                .id(Long.parseLong(matchInput.getId()))
                .city(matchInput.getCity())
                .date(LocalDate.parse(matchInput.getDate()))
                .playerOfMatch(matchInput.getPlayer_of_match())
                .venue(matchInput.getVenue())
                .tossWinner(matchInput.getToss_winner())
                .tossDecision(matchInput.getToss_decision())
                .matchWinner(matchInput.getWinner())
                .result(matchInput.getResult())
                .resultMargin(matchInput.getResult_margin())
                .umpire1(matchInput.getUmpire1())
                .umpire2(matchInput.getUmpire2())
                .build();

        //Set team 1 and team 2 depending on innings order
        String firstInningsTeam;
        String secondInningsTeam;
        if("bat".equals(matchInput.getToss_decision())) {
            firstInningsTeam = matchInput.getToss_winner();
            secondInningsTeam = matchInput.getToss_winner().equals(matchInput.getTeam1()) ?
                    matchInput.getTeam2() : matchInput.getTeam1();
        } else {
            secondInningsTeam = matchInput.getToss_winner();
            firstInningsTeam = matchInput.getToss_winner().equals(matchInput.getTeam1()) ?
                    matchInput.getTeam2() : matchInput.getTeam1();
        }
        match.setTeam1(firstInningsTeam);
        match.setTeam2(secondInningsTeam);
        return match;
    }

}
