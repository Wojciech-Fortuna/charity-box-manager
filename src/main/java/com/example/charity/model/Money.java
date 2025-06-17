package com.example.charity.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "money")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Money {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "money_id")
    private Long moneyId;

    @Column(length = 3, nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "box_id", nullable = false)
    private CollectionBox collectionBox;
}
