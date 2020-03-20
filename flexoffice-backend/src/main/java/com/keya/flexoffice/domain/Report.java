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
public class Report {
    private String id;
    @NotBlank(message = "Code must be not blank")
    private String code;
    @NotBlank(message = "description must be not blank")
    private String description;
    private LocalDateTime date;
    @NotBlank
    private String userIp;





}
