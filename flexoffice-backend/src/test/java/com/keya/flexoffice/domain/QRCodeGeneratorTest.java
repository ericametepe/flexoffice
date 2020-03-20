package com.keya.flexoffice.domain;

import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;

import static com.keya.flexoffice.domain.QRCodeGenerator.QR_CODE_IMAGE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class QRCodeGeneratorTest {

    @Test
    public void test() throws IOException, WriterException {

        String url = "http://localhost:8081/sites/599c5d94-5772-41ca-9aeb-2e3f5d8dd924/floors/477fcc32-6171-4c92-ad7c-612c10f1e9d3/desks";
        QRCodeGenerator.generateQRCodeImage(url,100,100, QR_CODE_IMAGE_PATH);

        File resultFile= new File(QR_CODE_IMAGE_PATH);
        resultFile.createNewFile();

        UrlResource urlResource = new UrlResource(resultFile.toURI());

        log.info("Resource {}",urlResource.getFilename());
        assertNotNull(urlResource);
    }

    @Test
        public void constructUriFromTemplate() {
            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .scheme("http").host("www.baeldung.com").path("/{article-name}")
                    .buildAndExpand("junit-5");
            log.info(" get it {}",uriComponents.toString());
            assertEquals("http://www.baeldung.com/junit-5", uriComponents.toUriString());
        }



}
