package com.keya.flexoffice.domain;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeskUpdateRequest {
    private String id;
    private String desknumber;
    private String state;
    private Report report;
    private Rating rating;
    private Favorite favorite;
    @NotBlank
    private String userIp;

    public boolean isFree() {
        return StateEnum.FREE.name().equalsIgnoreCase(this.state);
    }
}
