package com.keya.flexoffice.infra;

import com.keya.flexoffice.domain.Site;
import com.keya.flexoffice.domain.SiteMap;
import com.keya.flexoffice.domain.SiteRepository;
import com.keya.flexoffice.exposition.api.GeoPoint;
import com.keya.flexoffice.infra.entities.SiteJPA;
import com.keya.flexoffice.infra.entities.SiteJPARepository;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.Geometry;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SiteRepositoryImpl implements SiteRepository {

    public static final int GEOM_FACTOR = 6378137;
    public static final BigDecimal PI = new BigDecimal(3.14);
    private final SiteJPARepository siteJPARepository;

    Converter<SiteJPA, Site> myConverter = new Converter<SiteJPA, Site>()
    {
        public Site convert(MappingContext<SiteJPA, Site> context)
        {
            SiteJPA siteJPA = context.getSource();
            Site site = context.getDestination();

            Geometry point =  siteJPA.getGeocode();



            return site;
        }
    };

    private PropertyMap<SiteJPA, Site> skipModifiedFieldsMap = new PropertyMap<SiteJPA, Site>() {
        protected void configure() {

        }
    };

    public SiteRepositoryImpl(SiteJPARepository siteJPARepository){
        this.siteJPARepository=siteJPARepository;
    }

    @Override
    public Site findBySiteId(String siteId) {
        SiteJPA siteJPA = siteJPARepository.findById(siteId).orElseThrow(()->  new FlexBusinessException());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(skipModifiedFieldsMap);
        Site site =  modelMapper.map(siteJPA,Site.class);
        return site;
    }
    @Override
    public List<Site> findAll() {

        List<SiteJPA> siteJPAS =  siteJPARepository.findAll();

        ModelMapper modelMapper = new ModelMapper();

        List<Site> sites =mapToSite(siteJPAS, modelMapper);
        return sites;
    }

    private List<Site> mapToSite(List<SiteJPA> siteJPAS, ModelMapper modelMapper) {

        modelMapper.addMappings(skipModifiedFieldsMap);
        List<Site> sites = new ArrayList<>();
        if (!CollectionUtils.isEmpty(siteJPAS)){
            sites=  siteJPAS.stream().map(siteJPA -> modelMapper.map(siteJPA, Site.class))
                    .collect(Collectors.toList());
        }
        return sites;
    }


    @Override
    public Site update(Site site) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(skipModifiedFieldsMap);
       SiteJPA siteJPA =  modelMapper.map(site,SiteJPA.class);
      SiteJPA saveJpa= siteJPARepository.save(siteJPA);

       Site result = modelMapper.map(saveJpa,Site.class);

      return result;
    }

    @Override
    public List<Site> findLikeSiteAndLikeFloor(String siteName, String floorName) {
        List<SiteJPA> siteJPAS  = siteJPARepository.findBySearchTerm(siteName);
        List<SiteJPA> siteJPAList = siteJPARepository.findSiteByNameLike(siteName);
        List<SiteJPA> siteJPAList1 = siteJPARepository.findSiteByNameIsContaining(siteName);

        return  mapToSite(siteJPAS,new ModelMapper());


    }

    @Override
    public List<SiteMap> searchKNN(GeoPoint geoPoint) {
        List<SiteMap> maps = new ArrayList<>();

        List<Site> sites = this.findAll();

        if (!CollectionUtils.isEmpty(sites)){
           maps= sites.stream().filter(site -> Objects.nonNull(site.getGeoPoint()) &&
                   Objects.nonNull(site.getGeoPoint().getLat())
                   && Objects.nonNull(site.getGeoPoint().getLng())).map(site ->
                 SiteMap.builder().id(site.getId())
                        .distance(calculateFromGeoPoint(geoPoint, site.getGeoPoint()))
                        .name(site.getName())
                         .adress(site.getAddressLabel())
                         .geoPoint(site.getGeoPoint())
                         .totalfree(BigDecimal.valueOf(site.getFreeDesk().size()))
                         .build()).collect(Collectors.toList());
        }
        maps.sort(siteMapComparator);
        return maps;
        }

// Formula : convert angular_units * (PI/180) * 6378137 to meters
    private BigDecimal calculateFromGeoPoint(GeoPoint geoPointFrom, GeoPoint geoPointTo) {
        GeometryFactory geometryFactory = new GeometryFactory();
        com.vividsolutions.jts.geom.Point geoFrom = geometryFactory.createPoint(new Coordinate(geoPointFrom.getLng(), geoPointFrom.getLat()));
        com.vividsolutions.jts.geom.Point geoTo = geometryFactory.createPoint(new Coordinate(geoPointTo.getLng(),geoPointTo.getLat()));
        // d / 180 * PI * 6371 to km = 5853506.86141043
        BigDecimal angularUnit = BigDecimal.valueOf(geoFrom.distance(geoTo));
        BigDecimal piDIV = (PI.divide(new BigDecimal("180"),2,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_DOWN);
        BigDecimal result = (piDIV.multiply(angularUnit).multiply(new BigDecimal(6371))).setScale(2,RoundingMode.HALF_DOWN);
             return result;
    }

    Comparator<SiteMap> siteMapComparator = Comparator.comparing(SiteMap::getDistance);









}
