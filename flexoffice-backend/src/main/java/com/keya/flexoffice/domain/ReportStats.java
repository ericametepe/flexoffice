package com.keya.flexoffice.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReportStats implements StatComputable {
    private long countUser;
    private long sum;
    private int min = 0;
    private int max = 0;


    @Override
    public void compute() {

    }
}
