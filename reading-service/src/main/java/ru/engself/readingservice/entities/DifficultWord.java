package ru.engself.readingservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "difficult_words")
public class DifficultWord {

    @Id
    @Column(name = "word_id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID wordId;

    @Column(name = "word", nullable = false)
    private String word;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
