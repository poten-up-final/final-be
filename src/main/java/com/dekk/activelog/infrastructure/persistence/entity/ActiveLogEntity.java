package com.dekk.activelog.infrastructure.persistence.entity;

import com.dekk.activelog.domain.model.ActiveLog;
import com.dekk.activelog.domain.model.SwipeType;
import com.dekk.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "active_logs",
    indexes = {
        @Index(name = "idx_user_card", columnList = "user_id, card_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_active_logs_user_card",
            columnNames = {"user_id", "card_id"}
        )
    }
)
@SQLDelete(sql = "UPDATE active_logs SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ActiveLogEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SwipeType swipeType;

    @Builder
    private ActiveLogEntity(Long id, Long userId, Long cardId, SwipeType swipeType) {
        this.id = id;
        this.userId = userId;
        this.cardId = cardId;
        this.swipeType = swipeType;
    }

    public static ActiveLogEntity from(ActiveLog activeLog) {
        return ActiveLogEntity.builder()
            .id(activeLog.getId())
            .userId(activeLog.getUserId())
            .cardId(activeLog.getCardId())
            .swipeType(activeLog.getSwipeType())
            .build();
    }

    public ActiveLog toDomain() {
        return ActiveLog.builder()
            .id(this.id)
            .userId(this.userId)
            .cardId(this.cardId)
            .swipeType(this.swipeType)
            .build();
    }
}
