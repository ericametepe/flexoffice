package com.keya.flexoffice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.keya.flexoffice.infra.entities.SittingJPA;
import lombok.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class Desk {
    private String id;
    private String desknumber;
    private String state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime useStartDate;
    private Long favCount;

    private boolean favorite;
    private Set<Report> reports= new HashSet();
    private Set<Rating> ratings= new HashSet();
    private Set<Favorite> favorites= new HashSet();
    private Set<Sitting> sittings= new HashSet();


    //hr:min
    public String getUseDuration(){
        String result = null;
        if(!this.isFree() && !Objects.isNull(this.useStartDate)){
            LocalDateTime now = LocalDateTime.now();
            final long hour = this.useStartDate.until(now, ChronoUnit.HOURS);
         final long min= this.useStartDate.until(now,ChronoUnit.MINUTES);
        result = String.valueOf(hour) +":"+ String.valueOf(min);
        }
        return result;
    }

    public boolean isFree(){
        return StateEnum.FREE.name().equalsIgnoreCase(this.state);
    }

    public void addReport(Report newReport) {
        this.getReports().add(newReport);
    }

    public Integer getCountReport() {
        return CollectionUtils.isEmpty(this.reports)?0:reports.size();
    }

    @JsonDeserialize
    public BigDecimal getRatingCount() {
        BigDecimal mean=null;
        if  (CollectionUtils.isEmpty(this.ratings)){
       return BigDecimal.ZERO;
        }
      Optional<Integer> total = ratings.stream().map(Rating::getStars).filter(star->Objects.nonNull(star)).reduce((x, y)->
              x.intValue()+y.intValue());

        if (total.isPresent()){
        mean = BigDecimal.valueOf(total.get()).divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);
        }

        return mean.setScale(1, RoundingMode.HALF_UP);
    }


}
