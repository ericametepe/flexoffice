package com.keya.flexoffice.domain;

import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Profile {
    private String id;
    private String userIp;
    private Set<FavoriteTrack> favoris = new HashSet<>();
    private Set<SittingTrack> sittings = new HashSet<>();
    private Set<ReportTrack> reports = new HashSet<>();
    private Set<Rating> ratings = new HashSet<>();
    private  StatParameter statParameters;
    private GlobalStatParameter userStatParameter;

}
