package com.keya.flexoffice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FavoriteTrack {
    private String deskId;
    private String deskNumber;
    private String deskStatus;
    private String siteName;
    private String floorId;
    private String siteId;
    private String userIp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    //


}
