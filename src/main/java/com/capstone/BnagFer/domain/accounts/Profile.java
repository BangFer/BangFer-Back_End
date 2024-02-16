package com.capstone.BnagFer.domain.accounts;

import com.capstone.BnagFer.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "accounts_profile")
@Entity
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name; // 닉네임

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 소개 설명

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth; // 생년월일

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender; // MALE | FEMALE

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
