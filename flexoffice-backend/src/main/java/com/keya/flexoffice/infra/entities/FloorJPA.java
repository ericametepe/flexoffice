package com.keya.flexoffice.infra.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
@Entity(name = "floor")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FloorJPA implements Serializable {
    @Id
    @Column(name = "floor_id")
    private String id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "floor_id")
    private Set<DeskJPA> desks = new HashSet<>();

}
