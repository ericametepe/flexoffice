package com.keya.flexoffice.infra.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "favoriteTrack")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FavoriteTrackJPA {
    @Id
    private String id;
    private String siteId;
    private String floorId;
    private String deskId;
    private LocalDateTime date;

}
