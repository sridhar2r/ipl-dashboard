package com.iplproj.ipldashboard.repository;

import com.iplproj.ipldashboard.model.Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by sridharrajagopal on 9/17/21.
 */
public interface MatchRepository extends CrudRepository<Match, Long> {
    List<Match> findByTeam1OrTeam2OrderByDateDesc(String teamName1, String teamName2, Pageable pageable);


    // Date has to be passed after both team 1 and team2 due to precedence issue
    /*List<Match> findByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(String teamName1, LocalDate startDate1, LocalDate endDate1,
                                                                              String teamName2, LocalDate startDate, LocalDate endDate);*/

    @Query("select m from Match m where (m.team1=:teamName or m.team2=:teamName) and m.date between :startDate and :endDate order by m.date desc  ")
    List<Match> getMatchesByTeamBetweenDates(@Param("teamName") String teamName,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    default List<Match> findLatestMatchesByName(String teamName, int count) {
        Pageable pageable = PageRequest.of(0, count);
        return findByTeam1OrTeam2OrderByDateDesc(teamName, teamName, pageable);
    }
}
