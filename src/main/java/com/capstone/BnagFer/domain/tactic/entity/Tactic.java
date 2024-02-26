package com.capstone.BnagFer.domain.tactic.entity;


import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.global.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tactic_tactic")
public class Tactic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tactic_id")
    private Long tacticId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "tactic_name", nullable = false, length = 20)
    private String tacticName;

    @Column(name = "anonymous")
    private boolean anonymous = Boolean.TRUE;

    @Column(name = "famous_coach_name", length = 20)
    private String famousCoachName;

    @Column(name = "main_formation", length = 10)
    private String mainFormation;

    @Lob
    @Column(name = "attack_formation")
    private byte[] attackFormation;

    @Lob
    @Column(name = "defense_formation")
    private byte[] defenseFormation;

    @Column(name = "tactic_details", columnDefinition = "TEXT")
    private String tacticDetails;

    @Column(name = "attack_details", columnDefinition = "TEXT")
    private String attackDetails;

    @Column(name = "defense_details", columnDefinition = "TEXT")
    private String defenseDetails;

    @OneToMany(mappedBy = "tactic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TacticComment> comments = new ArrayList<TacticComment>();

    @OneToMany(mappedBy = "tactic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TacticLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "tactic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Team> teams; // Team 관련 내용을 pull 받지 않은 상태
}
