package com.example.charity.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "collection_box")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "box_id")
    private Long boxId;

    @Column(unique = true, nullable = false)
    private String identifier;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private FundraisingEvent event;

    @OneToMany(mappedBy = "collectionBox", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Money> moneyList;

    public boolean isEmpty() {
        return moneyList == null || moneyList.isEmpty();
    }

    public boolean isAssigned() {
        return event != null;
    }
}
