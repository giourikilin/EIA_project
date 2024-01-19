package com.example.eia.app.app.Monitor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

// Spring Data JPA repository interface for MySQL
@EnableJpaRepositories
@Repository
public interface LogRepo extends JpaRepository<Log, Long> {
}
