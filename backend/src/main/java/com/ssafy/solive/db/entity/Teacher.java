package com.ssafy.solive.db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@SuperBuilder
@DynamicInsert
@DiscriminatorValue("Teacher")
@Entity
public class Teacher extends User {

    // masterCode id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private MasterCode masterCode;

    // 지금까지 푼 문제 수
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer solvedCount;

    // 별점 합
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer ratingSum;

    // 별점 수
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer ratingCount;

    /**
     * 학생이 강사를 평가했을 때
     *
     * @param rating 추가할 평점
     */
    public void addRating(Integer rating) {
        this.ratingCount++;
        this.ratingSum += rating;
    }
}
