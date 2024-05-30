package com.example.lab7.Repository;

import com.example.lab7.Entity.Players;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayersRepo extends JpaRepository<Players, Integer> {
}
