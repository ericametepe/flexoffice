package com.keya.flexoffice.exposition.api;

import com.google.zxing.WriterException;
import com.keya.flexoffice.domain.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/flexoffice")
@Slf4j
public class SiteResource {
    private final SiteService siteService;


    @PostMapping(value = "/sites/recommendations", consumes =MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SiteMap>> getRecommendation(@RequestBody @Valid GeoPoint  geoPoint){

        List<SiteMap> sites = siteService.getNearSite(geoPoint);

        return new ResponseEntity<>(sites,HttpStatus.OK);
    }

    @PostMapping(value = "/sites/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Site>> search(@RequestBody SearchTerm searchTerm){
        List<Site> sites = siteService.searchSite(searchTerm);

        return new ResponseEntity<>(sites,HttpStatus.OK);
    }

    @PostMapping(value = "/sites",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Site> createSite(@RequestBody Site site){
        siteService.createSite(site);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/sites/{siteId}/floors", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFloor(@PathVariable String siteId, @RequestBody Floor floor){
        siteService.createFloor(siteId,floor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/sites/{siteId}/floors/{floorId}/desks",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDesk(@PathVariable String siteId, @PathVariable String floorId, @RequestBody Desk desk){
        siteService.createDesk(siteId, floorId,desk);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping( value = "/sites/{siteId}/floors/{floorId}/desks/{deskId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Desk> useDesk(@PathVariable String siteId, @PathVariable String floorId, @PathVariable String deskId, @RequestBody @Valid DeskUpdateRequest desk){
        Desk resultDesk = siteService.updateDesk(siteId, floorId,deskId, desk);
        return new ResponseEntity<>(resultDesk,HttpStatus.OK);
    }

    @GetMapping( value = "/sites/{siteId}/floors/{floorId}/desks/{deskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDesk(@PathVariable String siteId, @PathVariable String floorId, @PathVariable String deskId){
         Desk resultDesk = siteService.getDeskBySiteIdAndFloorId(siteId, floorId,deskId);
        return new ResponseEntity<>(resultDesk,HttpStatus.OK);
    }

    @PatchMapping( value = "/sites/{siteId}/floors/{floorId}/desks/{deskId}")
    public ResponseEntity<?> updateDesk(@PathVariable String siteId, @PathVariable String floorId, @PathVariable String deskId, @RequestBody Desk desk){
         siteService.partialUpdateDesk(siteId, floorId,deskId,desk);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping( value = "/sites/{siteId}/floors/{floorId}/desks/{deskId}/qrcode")
    public ResponseEntity<Resource> getQrcode(@PathVariable String siteId, @PathVariable String floorId, @PathVariable String deskId, ServletRequest request){
        Resource resource=null;
        String contentType=null;
        try {
            resource = siteService.getQrcode(siteId, floorId,deskId);
        } catch (IOException e) {
           log.error(e.getMessage(),e);
        } catch (WriterException e) {
           log.error(e.getMessage(),e);
        }

        try {
            contentType=request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(contentType)){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + resource.getFilename() + "\"" )
                .body(resource);
    }


    @GetMapping(value = "/sites", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<List<Site>>> getAllSite(HttpServletRequest request){

       Collection<List<Site>> sites = siteService.findChunk();

        return new ResponseEntity<>(sites, HttpStatus.OK);
    }

    @GetMapping(value = "/sites/{siteId}/floors")
    public ResponseEntity<List<Floor>> getFloors(@PathVariable String siteId){
          List<Floor> floors = siteService.getFloorsBySiteId(siteId);
        return new ResponseEntity<>(floors, HttpStatus.OK);
    }

    @GetMapping(value = "/sites/{siteId}")
    public ResponseEntity<Site> getSite(@PathVariable String siteId){
          Site site = siteService.getSite(siteId);
        return new ResponseEntity<>(site, HttpStatus.OK);
    }

    @GetMapping(value = "/sites/{siteId}/floors/{floorId}/desks")
    public ResponseEntity<?> getDeskBySiteAndFloor(@PathVariable String siteId, @PathVariable String floorId){
        Set<Desk> desks = siteService.getDesksBySiteIdAndFloorId(siteId, floorId);
        return new ResponseEntity<>(desks, HttpStatus.OK);
    }

    @PutMapping(value = "/sites/{siteId}/floors/{floorId}/desks/{deskId}/report", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Desk> reportToDesk(@PathVariable String siteId, @PathVariable String floorId, @PathVariable  String deskId, @Valid @RequestBody Report report ){
        Desk desk =siteService.report(siteId, floorId, deskId, report);
         return new ResponseEntity<>(desk,HttpStatus.OK);
    }

    @DeleteMapping(value = "/sites/{siteId}/floors/{floorId}/desks/{deskId}/profile/{user}/favorites", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Desk> deleteFavoriDesk(@PathVariable String siteId, @PathVariable String floorId, @PathVariable  String deskId,@PathVariable String user ){
           siteService.deleteFavorite(siteId, floorId, deskId, user);
         return new ResponseEntity<>(HttpStatus.OK);
    }




}
