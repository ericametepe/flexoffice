package com.keya.flexoffice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class SittingTrack {
    private String id;
    private String userIp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;
    private String deskId;
    private String deskNumber;
    private String deskStatus;
    private String siteName;
    private String floorId;
    private String siteId;
    private Long duration;
    private String state;







}
