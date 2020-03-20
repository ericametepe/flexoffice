package com.keya.flexoffice.domain;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.*;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.keya.flexoffice.infra.SiteRepositoryImpl.PI;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class StateEnumTest{

    @Test
    public void partion(){
        final List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7);
        final int chunkSize = 3;
        final AtomicInteger counter = new AtomicInteger();

       // final Collection<List<Integer>> result = numbers.stream()
          //      .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
            //    .values();

        final Map<Object, List<Integer>> result = numbers.stream().peek(integer -> log.info("" +integer))
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize));

        result.forEach((k,v)-> log.info(k + " "+ v));

        Lists.partition(numbers,4).stream().forEach(integers -> log.info(" partition" +integers));

        System.out.println(result);


    }

    @Test
    public void testCalculate(){
        //x + y =20  2x + 3y = 120

        BigDecimal t = BigDecimal.valueOf(9).divide(BigDecimal.valueOf(2));

        BigDecimal piDIV = (PI.divide(new BigDecimal("180"),RoundingMode.CEILING)).setScale(2, RoundingMode.HALF_DOWN);

    }

// Formula : convert angular_units * (PI/180) * 6378137 to meters
    @Test
    public void distance() {
GeometryFactory geometryFactory = new GeometryFactory();
       com.vividsolutions.jts.geom.Point point = geometryFactory.createPoint(new Coordinate(2.5559, 49.0083));
       com.vividsolutions.jts.geom.Point point2 = geometryFactory.createPoint(new Coordinate(-74.1197,40.6976));

        BigDecimal piDIV = (PI.divide(new BigDecimal("180"),2,BigDecimal.ROUND_CEILING)).setScale(2, RoundingMode.HALF_DOWN);



        // d / 180 * PI * 6371 to km = 5853506.86141043
       log.info("distance " +point.distance(point2)*6371);

    }



        @Test
    public void convert() {



        GeometryFactory geometryFactory = new GeometryFactory();


        org.geolatte.geom.Point<G2D> point =
                Geometries.mkPoint(new G2D(12.34343, 12.232424),
                        CoordinateReferenceSystems.WGS84);



        Point point2 =
                Geometries.mkPoint(new G2D(12.34343, 12.232424),
                        CoordinateReferenceSystems.WGS84);
        log.info("  "+ point.getPosition().getLat());
        log.info("  "+ point.getPosition().getLon());


    }


        @Test
   public void nextState() {
        assertEquals(StateEnum.FREE, StateEnum.OCCUPIED.nextState(StateEnum.FREE));

        assertEquals(StateEnum.OCCUPIED, StateEnum.FREE.nextState(StateEnum.OCCUPIED));

        assertEquals(StateEnum.OCCUPIED, StateEnum.valueOf("OCCUPIED"));

        AtomicLong atomicLong = new AtomicLong(0L);
        assertEquals(1L, atomicLong.incrementAndGet());

        LocalDateTime end= LocalDateTime.of(2019,12,6,15,1);
        LocalDateTime now = LocalDateTime.now();

        log.info("Hour : {} ",end.until(now,ChronoUnit.HOURS));
       log.info("Min : {} " + end.until(now, ChronoUnit.MINUTES));
       log.info("Seconds : {}" + end.until(now, ChronoUnit.SECONDS));


    }
}
