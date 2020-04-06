package com.keya.flexoffice.infra.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "profile")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ProfileJPA {
    @Id
    private String id;
    @Column(nullable = false,unique = true)
    private String userIp;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<FavoriteTrackJPA> favorites = new HashSet<>();
}
