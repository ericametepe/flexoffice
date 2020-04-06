package com.keya.flexoffice.domain;

import com.keya.flexoffice.exposition.api.GeoPoint;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SiteMap {
    private String id;
    private String name;
    private String adress;
    private BigDecimal distance;
    private GeoPoint geoPoint;
    private BigDecimal totalfree;

}
