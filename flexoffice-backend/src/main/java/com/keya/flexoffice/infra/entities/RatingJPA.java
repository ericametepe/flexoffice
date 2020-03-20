package com.keya.flexoffice.infra.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "rating")
public class RatingJPA {
    @Id
    private String id;
    private Integer stars;
    private String description;
    private LocalDateTime date;
    @NotBlank
    private String userIp;
}
