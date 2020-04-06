package com.keya.flexoffice.domain;

import com.keya.flexoffice.exposition.api.GeoPoint;

import java.util.List;

public interface SiteRepository {
    Site findBySiteId(String siteId);
    List<Site> findAll();
    Site update(Site site);

    List<Site> findLikeSiteAndLikeFloor(String siteName, String floorName);

    List<SiteMap> searchKNN(GeoPoint geoPoint);
}
