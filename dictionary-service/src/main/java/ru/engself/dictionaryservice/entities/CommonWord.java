package ru.engself.dictionaryservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "common_words")
public class CommonWord {

    @Id
    @Column(name = "common_word_id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID commonWordId;

    @Column(name = "name", nullable = false, unique = true)
    private String wordName;

    @Column(name = "transcription")
    private String wordTranscription;

    @Column(name = "translation")
    private String wordTranslation;

    @Column(name = "photo")
    private String wordPhoto;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

}
