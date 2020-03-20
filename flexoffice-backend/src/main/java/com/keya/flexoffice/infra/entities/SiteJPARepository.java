package com.keya.flexoffice.infra.entities;


import com.keya.flexoffice.domain.SiteMap;
import com.keya.flexoffice.exposition.api.GeoPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SiteJPARepository extends JpaRepository<SiteJPA, String> {


    @Query(" from site st where st.name like %:siteName%")
    List<SiteJPA> findBySearchTerm(String siteName);



    List<SiteJPA> findSiteByNameLike(String site);
    List<SiteJPA> findSiteByNameIsContaining(String site);



   // @Query( "select new FavoriteTrack(desk.id, desk.desknumber, desk.state, site.name, floor.id,  favorite.userIp) from desk, floor,  site, favorite where desk.id = favorite.desk_id and floor.id=desk.floor_id and site.id= floor.site_id and favorite.user=:userIp group by  favorite.user_ip, desk.id, site.name, floor.id")
    //List<FavoriteTrack> findDeskCustomQuery(@Param("userIp") String userIp);


   @Query(value = "SELECT name, ST_DistanceSphere(new_point, st_point(2.5559, 49.0083)) as distance, st_x(new_point) as x, st_y(new_point) as y from site order by dsistance ASC ", nativeQuery = true)
       List<SiteMap> searchKNN(GeoPoint geopoint);
}
