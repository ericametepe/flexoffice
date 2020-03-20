package com.keya.flexoffice.domain;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReportTrack {
    private String id;
    private String code;
    private String description;
    private LocalDateTime date;
    private String userIp;
    private String deskId;
    private String deskNumber;
    private String floorId;
    private String siteId;
    private String deskState;
    private String siteName;

}
