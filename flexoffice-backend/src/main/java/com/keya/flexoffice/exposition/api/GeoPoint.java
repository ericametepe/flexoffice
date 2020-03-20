package com.keya.flexoffice.exposition.api;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoPoint {
    @NotNull
    private Double lng;
    @NotNull
    private Double lat;

}
