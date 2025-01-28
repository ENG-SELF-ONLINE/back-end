package ru.engself.dictionaryservice.entities;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.usertype.UserType;
import ru.engself.dictionaryservice.dtos.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "decks")
public class Deck {

    @Id
    @Column(name = "deck_id")
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID deckId;

    @Column(name = "name")
    private String deckName;

    @Column(name = "photo")
    private String deckPhoto;

//    @Column(name = "words")
//    @OneToMany(mappedBy = "deck")
//    private List<Word> deckWords;

    @Type(JsonBinaryType.class)
    @Column(name = "user_info", columnDefinition = "jsonb")  // Изменено с user на user_info
    private UserDTO userInfo;  // Изменено с user на userInfo

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}