package com.keya.flexoffice.infra.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keya.flexoffice.domain.ReportTrack;
import com.keya.flexoffice.infra.RefData;
import com.keya.flexoffice.infra.RefDataOther;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class FloorRepositoryTest {
        private static final String TOKEN = "d09b28f75870c9" ;

        @Test
        public void test_compute(){
            List<ReportTrack> reportTracks = new ArrayList<>();
            DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();
            reportTracks.forEach( r -> statistics.accept(1));
        }

        @Test
        public void givenTwoDatesInJava8_whenDifferentiating_thenWeGetSix() {
            LocalDate now = LocalDate.now();
            LocalDate sixDaysBehind = now.minusDays(6);
            Period period = Period.between(now, sixDaysBehind);
            int diff = Math.abs(period.getDays());
            assertEquals(diff, 6);
        }



        @Test
    public void findByName() {
        log.info("basic {}" ,toJson("test.json",RefData.class));

        log.info("other {}",toJson("testo.json",RefDataOther.class));

    }

        public static  <T extends RefData> T toJson(String filename, Class<T> type) {
            ClassPathResource classPathResource = new ClassPathResource(filename);
            T data = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                data =   objectMapper.readValue(classPathResource.getFile(), type);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            return data;

        }

        @Test
    public void testToJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("test.json");
        file.createNewFile();
        ClassPathResource classPathResource = new ClassPathResource("test.json");
        RefData data = RefData.builder().code("code").label("label").build();
        log.info(" {}",data);
        objectMapper.writeValue(file,data);
    }

       // return axios.get(`${url}?token=${key}`);
        //  const url = 'http://ipinfo.io/json';


    public static int calc(int[] array, int n1, int n2) {
        int sum =0;
        for (int a :array){
            if (a<=n2 && a>=n1){
                sum+=a;
            }

        }
    return sum;

    }

    private static int computeJoinPoint(int s1, int s2) {

        return 0;
    }

}
