package com.keya.flexoffice.exposition.api;

import com.keya.flexoffice.domain.GlobalStatParameter;
import com.keya.flexoffice.domain.Profile;
import com.keya.flexoffice.domain.ProfileService;
import com.keya.flexoffice.domain.ZxingBarcodeGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.awt.image.BufferedImage;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/flexoffice")
public class ProfileResource {
    private final ProfileService profileService;


    @GetMapping(value = "/sites/profile/{user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> getProfile(@PathVariable @Validated
                                                  @NotBlank(message = "no user no pref") String user){
        Profile profile = profileService.getProfileByUserIp(user);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @GetMapping(value = "/sites/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlobalStatParameter> getGlobalProfile(){
        GlobalStatParameter globalStatParameter = profileService.calculate();
        return new ResponseEntity<>(globalStatParameter, HttpStatus.OK);
    }



   @PostMapping(value = "/sites/profile/{user}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> saveProfile(@PathVariable String user){
        Profile profile = profileService.save(user);
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }

    @PostMapping(value = "/zxing/qrcode", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> zxingQRCode(@RequestBody String barcode) throws Exception {
        BufferedImage bufferedImage = ZxingBarcodeGenerator.generateQRCodeImage(barcode);

        return ResponseEntity.ok(bufferedImage);
    }




}
