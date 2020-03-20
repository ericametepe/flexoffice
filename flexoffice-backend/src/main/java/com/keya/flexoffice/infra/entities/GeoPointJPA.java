package com.keya.flexoffice.infra.entities;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class GeoPointJPA {
    private Double lng;
    private Double lat;
}
