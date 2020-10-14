package com.example.jsonview.model;

import lombok.*;

import javax.persistence.*;
import javax.print.attribute.standard.MediaSize;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "T_ORDER")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeOrder extends BaseEntity implements Serializable {
    private String customer;
    @ManyToMany
    @JoinTable(name = "T_ORDER_COFFEE")
    @OrderBy("id")
    private List<Coffee> items;
    @Enumerated
    @Column(nullable = false)
    private OrderState state;
}