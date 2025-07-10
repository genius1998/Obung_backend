package com.lion.CalPick.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "bungs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String store;

    @Column(nullable = false)
    private Integer totalParticipants;

    @Column(nullable = false)
    private String organizerUserId; // 주최자의 userId
}
