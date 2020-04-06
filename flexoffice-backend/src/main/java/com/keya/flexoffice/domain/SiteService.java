package com.keya.flexoffice.domain;
import com.google.zxing.WriterException;
import com.keya.flexoffice.exposition.api.GeoPoint;
import com.keya.flexoffice.exposition.api.SearchTerm;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SiteService {
    Set<Desk> getDesksBySiteIdAndFloorId(String siteId, String floorId);
    Site getSite(String siteId);

    List<Site> findAll();

    Collection<List<Site>> findChunk();

    List<Floor> getFloorsBySiteId(String siteId);

    Desk updateDesk(String siteId, String floorId, String deskId, DeskUpdateRequest desk);

    void createDesk(String siteId, String floorId, Desk desk);

    void createFloor(String siteId, Floor floor);

    void createSite(Site site);

    void partialUpdateDesk(String siteId, String floorId, String deskId, Desk desk);

    Resource getQrcode(String siteId, String floorId, String deskId) throws IOException, WriterException;

    List<Site> searchSite(SearchTerm searchTerm);

    Desk getDeskBySiteIdAndFloorId(String siteId, String floorId, String deskId);

    List<SiteMap> getNearSite(GeoPoint geoPoint);

    Desk report(String siteId, String floorId, String deskId, Report report);


    void deleteFavorite(String siteId, String floorId, String deskId, String user);
}
