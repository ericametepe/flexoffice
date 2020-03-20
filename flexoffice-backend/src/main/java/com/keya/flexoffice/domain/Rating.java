package com.keya.flexoffice.domain;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Rating {
    private String id;
    private String siteId;
    private String siteName;
    private String floorId;
    private String deskId;
    private String deskNumber;
    private String deskStatus;
    private Integer stars;
    private String description;
    private LocalDateTime date;
    @NotBlank
    private String userIp;

}
