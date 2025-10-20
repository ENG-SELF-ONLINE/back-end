package ru.engself.activitieslibrary.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.engself.activitieslibrary.enums.ActivityType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_activities")
public class UserActivity {

    @Id
    @Column(name = "user_activity_id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID userActivityId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

    @Column(name = "activity_title", nullable = false)
    private String activityTitle;

    @Column(name = "activity_date", updatable = false, nullable = false)
    private LocalDateTime activityDate;

}
