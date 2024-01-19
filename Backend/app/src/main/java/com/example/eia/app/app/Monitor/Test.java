package com.example.eia.app.app.Monitor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
// JPA entity class to create a table: "test" with columns in MySQL 
@Table(name="test") 
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    private String type;
    @Column(name = "history")
    private String history;
    @Column(name = "content", nullable = false)
    private String content;
}
