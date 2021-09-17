package com.iplproj.ipldashboard.data;

import com.iplproj.ipldashboard.model.Match;
import com.iplproj.ipldashboard.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  private EntityManager em;

  @Autowired
  public JobCompletionNotificationListener(EntityManager em) {
    this.em = em;
  }

//  private final JdbcTemplate jdbcTemplate;

 /* @Autowired
  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
*/
  @Override
  @Transactional
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");
      Map<String, Team> teamData = new HashMap<>();
 /*    List<Object[]> results = em.createQuery("select m.team1, count(*) from Match m group by m.team1")
              .getResultList();*/
      em.createQuery("select m.team1, count(*) from Match m group by m.team1", Object[].class)
              .getResultList()
              .stream()
              .map(e -> new Team((String) e[0], (long) e[1]))
              .forEach(team -> teamData.put(team.getTeamName(), team));

      em.createQuery("select m.team2, count(*) from Match m group by m.team2", Object[].class)
              .getResultList()
              .stream()
              .forEach(e -> {
                Team team = teamData.get((String) e[0]);
                team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
              });
      em.createQuery("select m.matchWinner, count(*) from Match m group by m.matchWinner", Object[].class)
              .getResultList()
              .stream()
              .forEach(e -> {
                Team team = teamData.get((String) e[0]);
                if(team != null) {
                  team.setTotalWins((long) e[1]);
                }
              });
      teamData.values().forEach(team -> em.persist(team));
      teamData.values().forEach(team -> log.info(team.toString()));
    /*  jdbcTemplate.query("SELECT id,city,date,player_of_match,venue,team1,team2,toss_winner,toss_decision,match_winner,result,result_margin,umpire1,umpire2 FROM match limit 5",
        (rs, row) -> Match.builder()
                .id(rs.getLong("id"))
                .city(rs.getString("city"))
                .date(LocalDate.parse(rs.getString("date")))
                .playerOfMatch(rs.getString("player_of_match"))
                .venue(rs.getString("venue"))
                .team1(rs.getString("team1"))
                .team2(rs.getString("team2"))
                .tossWinner(rs.getString("toss_winner"))
                .tossDecision(rs.getString("toss_decision"))
                .matchWinner(rs.getString("match_winner"))
                .result(rs.getString("result"))
                .resultMargin(rs.getString("result_margin"))
                .umpire1(rs.getString("umpire1"))
                .umpire2(rs.getString("umpire2"))
                .build()
      ).forEach(match -> log.info("Found < {} > in the database.", match));*/
    }
  }
}