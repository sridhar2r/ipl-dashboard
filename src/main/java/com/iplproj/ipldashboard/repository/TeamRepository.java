package com.iplproj.ipldashboard.repository;

import com.iplproj.ipldashboard.model.Team;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by sridharrajagopal on 9/17/21.
 */
public interface TeamRepository extends CrudRepository<Team, Long> {
    Team findByTeamName(String teamName);
}
