package com.keya.flexoffice.domain;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class SiteServiceImplTest {
    @Test
    void buildQrcodeUrl() {
        String  result= SiteServiceImpl.buildQrcodeUrl("http","host","s1","f1","d1");
      log.info(" url  {}",result);
      String val = "TEPE LOCAL SITE";
      String term="tepe";
        assertTrue(StringUtils.containsIgnoreCase(val,term));

    }

}
