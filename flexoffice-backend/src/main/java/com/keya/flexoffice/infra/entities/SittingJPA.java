package com.keya.flexoffice.infra.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
@Entity(name="sitting")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SittingJPA {
    @Id
    private String id;
    private String userIp;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
}
