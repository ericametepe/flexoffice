package com.keya.flexoffice.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.IntSummaryStatistics;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StatParameter {
    private BigDecimal reportCounter;
    private BigDecimal sittingCounter;
    private BigDecimal ratingCounter;
    private IntSummaryStatistics ratingStars;
    private BigDecimal reportCounterMean;

}
