package ru.engself.dictionaryservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import ru.engself.dictionaryservice.enums.WordReviewResult;
import ru.engself.dictionaryservice.enums.WordStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "word_progresses")
public class WordProgress {

    @Id
    @Column(name = "word_progress_id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID wordProgressId;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.REMOVE})
    @JoinColumn(name = "word_id", referencedColumnName = "word_id")
    private Word word;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private WordStatus wordStatus;

    @Column(name = "next_review_date")
    private LocalDateTime nextReviewDate;

    @Column(name = "previous_result")
    @Enumerated(EnumType.STRING)
    private WordReviewResult previousResult;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}