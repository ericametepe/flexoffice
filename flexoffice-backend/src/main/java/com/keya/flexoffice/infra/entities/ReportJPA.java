package com.keya.flexoffice.infra.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "report")
public class ReportJPA {
    @Id
    private String id;
    private String code;
    private String description;
    private LocalDateTime date;
    private String userIp;
}
