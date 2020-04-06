package com.keya.flexoffice.domain;

import lombok.*;

import java.util.IntSummaryStatistics;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class GlobalStatParameter {
    private IntSummaryStatistics reports;
    private IntSummaryStatistics sittings;
    private IntSummaryStatistics favoris;
    private IntSummaryStatistics ratingStats;
    private IntSummaryStatistics ratingStarsStats;


}
