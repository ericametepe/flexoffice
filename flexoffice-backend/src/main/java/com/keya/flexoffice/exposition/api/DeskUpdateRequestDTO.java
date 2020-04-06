package com.keya.flexoffice.exposition.api;

import com.keya.flexoffice.domain.Rating;
import com.keya.flexoffice.domain.Report;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeskUpdateRequestDTO {
    private String id;
    private String desknumber;
    private String state;
    private Report report;
    private Rating rating;
}
