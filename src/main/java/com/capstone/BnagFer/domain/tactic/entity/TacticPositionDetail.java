package com.capstone.BnagFer.domain.tactic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tactic_tacticpositiondetail")
public class TacticPositionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @ManyToOne
    @JoinColumn(name = "tactic_id", referencedColumnName = "tactic_id")
    private Tactic tactic;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;

    @Column(name = "position_description")
    private String positionDescription;

}
