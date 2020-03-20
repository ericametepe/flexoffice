package com.keya.flexoffice.infra.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
@Entity(name = "favorite")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FavoriteJPA {
    @Id
    private String id;
    private String userIp;
    private LocalDateTime date;
}
