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

    @Column(name = "main_formation", length = 10)
    private String mainFormation;

    @Column(name = "tactic_details", columnDefinition = "TEXT")
    private String tacticDetails;

    @Column(name = "sub_tactic", columnDefinition = "TEXT")
    private String subTactic;

    @Builder.Default
    @OneToMany(mappedBy = "tactic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TacticComment> comments = new ArrayList<TacticComment>();

    @Builder.Default
    @OneToMany(mappedBy = "tactic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TacticPositionDetail> tacticPositionDetails = new ArrayList<TacticPositionDetail>();

    @Builder.Default
    @OneToMany(mappedBy = "tactic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TacticLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "tactic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Team> teams;

    public void setCopyDetail(User users, Tactic tactic){
        tacticName = tactic.getTacticName();
        user = users;
        anonymous = true;
        mainFormation = tactic.getMainFormation();
        tacticDetails = tactic.getTacticDetails();
        subTactic = tactic.getSubTactic();
    }

    public void updateTactic(TacticUpdateRequest request){
        tacticName = request.tacticName();
        anonymous = request.anonymous();
        mainFormation = request.mainFormation();
        tacticDetails = request.tacticDetails();
        subTactic = request.subTactic();
    }

    public static Tactic createTactic() {
        return new Tactic();
    }
}
