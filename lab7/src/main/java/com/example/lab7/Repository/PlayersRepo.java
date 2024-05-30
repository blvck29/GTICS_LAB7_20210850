package com.example.lab7.Repository;

import com.example.lab7.Entity.Players;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlayersRepo extends JpaRepository<Players, Integer> {

    @Query("SELECT p FROM Players p WHERE p.region = :region ORDER BY p.position ASC")
    List<Players> findAllByOrderByPositionAscAndByRegion(@Param("region") String region);

    @Query("SELECT p FROM Players p WHERE p.region = :region ORDER BY p.mmr DESC")
    List<Players> playersSortedDescByMMR(@Param("region") String region);

}
