package com.keya.flexoffice.infra;

import com.keya.flexoffice.domain.FavoriteTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
@Repository
public class SiteCustomJPARepository {


    @Autowired
    private EntityManagerFactory entityManagerFactory;



    public List<FavoriteTrack> findDeskCustomQuery(String userIp){

        String jpql = "select desk.id, desk.desknumber, desk.state, site.name, floor.id,  favorite.userIp from desk, floor,  site, favorite where desk.id = favorite.desk_id and floor.id=desk.floor_id and site.id= floor.site_id and favorite.userIp=:userIp group by  favorite.user_ip, desk.id, site.name, floor.id";

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query =  entityManager.createQuery("select desk.id, desk.desknumber, desk.state, site.name, floor.id,  favorite.userIp from desk, floor,  site, favorite where desk.id = favorite.desk_id and floor.id=desk.floor_id and site.id= floor.site_id and favorite.userIp=:userIp group by  favorite.user_ip, desk.id, site.name, floor.id",
                FavoriteTrack.class);

        List<FavoriteTrack> tracks = query.setParameter("userIp",userIp).getResultList();

        return tracks;

    }









}
