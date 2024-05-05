package com.capstone.BnagFer.domain.tactic.entity;


import com.capstone.BnagFer.domain.accounts.entity.User;
import com.capstone.BnagFer.domain.myteam.entity.Team;
import com.capstone.BnagFer.domain.tactic.dto.TacticUpdateRequest;
import com.capstone.BnagFer.global.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tactic_tactic")
public class Tactic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tactic_id")
    private Long tacticId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "tactic_name", nullable = false, length = 20)
    private String tacticName;

    @Column(name = "anonymous")
    @ColumnDefault("true")
    private boolean anonymous;

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

    @OneToMany(mappedBy = "tactic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TacticComment> comments = new ArrayList<TacticComment>();

    @OneToMany(mappedBy = "tactic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TacticLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "tactic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Team> teams;

    public void setCopyDetail(User users, Tactic tactic){
        tacticName = tactic.getTacticName();
        user = users;
        anonymous = true;
        famousCoachName = tactic.getFamousCoachName();
        mainFormation = tactic.getMainFormation();
        attackFormation = tactic.getAttackFormation();
        defenseFormation = tactic.getDefenseFormation();
        tacticDetails = tactic.getTacticDetails();
        attackDetails = tactic.getAttackDetails();
        defenseDetails = tactic.getDefenseDetails();
    }

    public void updateTactic(User users, TacticUpdateRequest request){
        tacticName = request.tacticName();
        user = users;
        anonymous = request.anonymous();
        famousCoachName = request.famousCoachName();
        mainFormation = request.mainFormation();
        attackFormation = request.attackFormation();
        defenseFormation = request.defenseFormation();
        tacticDetails = request.tacticDetails();
        attackDetails = request.attackDetails();
        defenseDetails = request.defenseDetails();
    }

    public static Tactic createTactic() {
        return new Tactic();
    }
}
