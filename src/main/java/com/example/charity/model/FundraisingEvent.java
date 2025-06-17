package com.example.charity.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "fundraising_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundraisingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(name = "account_amount")
    private BigDecimal accountAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "event")
    private List<CollectionBox> boxes;
}
