package com.keya.flexoffice.infra.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "desk")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class DeskJPA implements Serializable {
    @Id
    @Column(name = "desk_id")
    private String id;
    private String desknumber;
    private String state;
    private LocalDateTime useStartDate;
    private Long favCount;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "desk_id")
    private Set<ReportJPA> reports= new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "desk_id")
    private Set<RatingJPA> ratings= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "desk_id")
    private Set<FavoriteJPA> favorites= new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "desk_id")
    private Set<SittingJPA> sittings= new HashSet<>();



}
