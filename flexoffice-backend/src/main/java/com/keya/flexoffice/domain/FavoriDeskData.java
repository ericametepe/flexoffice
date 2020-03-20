package com.keya.flexoffice.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FavoriDeskData {
    private String siteId;
    private String floorId;
    private Desk desk;
}
