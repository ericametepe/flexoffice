package com.keya.flexoffice.domain;

import com.google.common.collect.Lists;
import com.google.zxing.WriterException;
import com.keya.flexoffice.exposition.api.GeoPoint;
import com.keya.flexoffice.exposition.api.SearchTerm;
import com.keya.flexoffice.infra.FlexBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
//127.0.0.1       flexo-backend
@Service
@Slf4j
public class SiteServiceImpl implements SiteService {
    private static final int MAX_FAX = 5;
    @Value(value = "${flexo.scheme:http}")
    private String scheme;
    @Value(value = "${flexo.host: 192.168.1.85:8081}")
    private String host;
    private  final SiteRepository siteRepository;


    public SiteServiceImpl(SiteRepository siteRepository){
        this.siteRepository=siteRepository;
    }

    @Override
    @Cacheable(value = "desks",key="#desksId")
    public Set<Desk> getDesksBySiteIdAndFloorId(String siteId, String floorId) {
        Site site = getSite(siteId);

        Floor floorFound = site.findFloorByFloorId(floorId);
        return floorFound.findDeskByFloorId(floorId);
    }

    @Override
    public Site getSite(String siteId) {
        return siteRepository.findBySiteId(siteId);
    }

    @Override
    public List<Site> findAll() {
       return siteRepository.findAll();
    }

    @Override
    public Collection<List<Site>> findChunk() {
        List<Site> sites = this.findAll();
        AtomicInteger counter = new AtomicInteger();
        Integer size =3;

        Collection<List<Site>> result =
                 sites.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement()/size)).values();


        return result;
    }

    @Override
    public List<Floor> getFloorsBySiteId(String siteId) {
        Site site = getSite(siteId);
        return Lists.newArrayList(site.getFreeFloor());
    }



    @Transactional
    @Override
    public Desk updateDesk(String siteId, String floorId, String deskId, DeskUpdateRequest newDesk) {
        Site site = getSite(siteId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(siteId));

        Floor floorFound = site.findFloorByFloorId(floorId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(floorId));

        Desk deskFound = floorFound.findDeskByFloorIdAndDeskId(floorId,deskId);
        Optional.ofNullable(deskFound).orElseThrow(()-> new FlexBusinessException(siteId,floorId, deskId));

        Rating ratingNew = newDesk.getRating();


        Set<Rating> ratingsToSave= new HashSet<>();

        ratingsToSave.addAll(deskFound.getRatings());

        Optional.ofNullable(ratingNew).ifPresent(rating ->  {
            Rating rating1 = buildRating(ratingNew);
            ratingsToSave.add(rating1);
        });


        Set<Report> reportsToSave= new HashSet<>();
        reportsToSave.addAll(deskFound.getReports());

        Optional.ofNullable(newDesk.getReport()).ifPresent(rep ->{
            Report report = buildReport(rep);
            reportsToSave.add(report);

                });

        //favorite
        Set<Favorite> favoritesToSave =  new HashSet<>();
        favoritesToSave.addAll(deskFound.getFavorites());

        if  (Optional.ofNullable(newDesk.getFavorite()).isPresent()
                && StringUtils.isNotBlank(newDesk.getFavorite().getUserIp())
                && StringUtils.isNotBlank(newDesk.getUserIp())){


            checkDeskExistFavoriteRule(deskFound, newDesk.getUserIp());
            checkDeskFavoriteRule(newDesk.getUserIp());

        Favorite newFavorite = Favorite.builder()
                .id(UUID.randomUUID().toString())
                .date(LocalDateTime.now())
                .userIp(newDesk.getUserIp()).build();

        favoritesToSave.add(newFavorite);
        }

        //build desk for use
        Set<Sitting> sittingsToSave =  new HashSet<>();

        sittingsToSave.addAll(deskFound.getSittings());

        String newDeskState = newDesk.getState();

        if  (Optional.ofNullable(newDeskState).isPresent() && StringUtils.isNotBlank(newDesk.getUserIp())){
            // check desk current state
            checkDeskStateRule(deskFound, newDesk);

            if (StringUtils.equalsIgnoreCase(StateEnum.OCCUPIED.name(), newDeskState)){
                checkSittinRule(siteId,floorId,deskId,newDesk);
                Sitting sitting = Sitting.builder()
                        .id(UUID.randomUUID().toString())
                        .start(LocalDateTime.now())
                        .end(null)
                        .userIp(newDesk.getUserIp()).build();
                sittingsToSave.add(sitting);

            }

            if (StringUtils.equalsIgnoreCase(StateEnum.FREE.name(), newDeskState)){
                if (!CollectionUtils.isEmpty(sittingsToSave)){

                      Optional<Sitting> sittingFound =  sittingsToSave.stream()
                              .filter(sitting -> StringUtils.equalsIgnoreCase(sitting.getUserIp(),newDesk.getUserIp())
                                && DateUtils.isSameDay(convertToDate(sitting.getStart()),convertToDate(LocalDateTime.now()))).findFirst();

                      if (sittingFound.isPresent()){
                          Sitting newSitting = Sitting.builder()
                                  .id(sittingFound.get().getId())
                                  .start(sittingFound.get().getStart())
                                  .end(LocalDateTime.now())
                                  .userIp(sittingFound.get().getUserIp()).build();
                          // remove the old one
                          sittingsToSave.remove(sittingFound.get());
                          // add new
                          sittingsToSave.add(newSitting);





                      }

                }

            }

        }


        //create new desk
        Desk deskToSave = buildDeskForSave(newDesk, deskFound, ratingsToSave, reportsToSave, favoritesToSave,sittingsToSave);


        //remove initial deskFound
        floorFound.removeByDeskId(deskId);
        //add the new
        floorFound.addDesk(deskToSave);

        Site savedSite =  siteRepository.update(site);
        Desk deskSaved= savedSite.findDeskByFloorIdAndDeskId(floorId, deskId);

        final Desk deskResult = buildDeskForResult(deskFound, deskSaved);

        return deskResult;
    }

    private void checkDeskExistFavoriteRule(Desk deskFound, String userIp) {

        Set<Favorite> favorites = deskFound.getFavorites();
        if (CollectionUtils.isEmpty(favorites)){
            if (favorites.stream().anyMatch(favorite -> StringUtils.equalsIgnoreCase(favorite.getUserIp(),userIp))){
                throw new FlexBusinessException("Desk already added as favorite");
            }

        }

    }

    private void checkDeskStateRule(Desk deskFound, DeskUpdateRequest newDesk) {
        StateEnum nextState = StateEnum.valueOf(deskFound.getState()).nextState(StateEnum.valueOf(newDesk.getState()));

        Optional.ofNullable(nextState).orElseThrow(()->new FlexBusinessException("DeskStateRule not applicable..."));


    }

    private void checkSittinRule(String siteId, String floorId, String deskId, DeskUpdateRequest deskUpdateRequest) {
        // check the desk initial state and new

        // user hold another desk

        List<Site> sites = findAll();

        Set<Desk> desks = sites.stream().map(Site::getFloors).flatMap(Collection::stream).map(floor -> floor.getDesks()).flatMap(Collection::stream).collect(Collectors.toSet());

        Set<Sitting> sittings =desks.stream().map(desk -> desk.getSittings()).flatMap(Collection::stream).collect(Collectors.toSet());


       if ( sittings.stream().anyMatch(sitting -> StringUtils.equalsIgnoreCase(sitting.getUserIp(),deskUpdateRequest.getUserIp())
                && DateUtils.isSameDay(convertToDate(sitting.getStart()),convertToDate(LocalDateTime.now())) && Objects.isNull(sitting.getEnd()))){

           throw new FlexBusinessException("Remind to release "+ "site: "+siteId+"floor:"+floorId+" "+"desk "+deskId);

       }

    }

    private Report buildReport(Report rep) {
        return Report.builder()
                .id(UUID.randomUUID().toString())
                .code(rep.getCode())
                .date(LocalDateTime.now())
                .description(rep.getDescription())
                .userIp(rep.getUserIp()).build();
    }

    private Rating buildRating(Rating ratingNew) {
        return Rating.builder()
                        .id(UUID.randomUUID().toString())
                        .date(LocalDateTime.now())
                        .description(ratingNew.getDescription())
                        .stars(ratingNew.getStars())
                        .userIp(ratingNew.getUserIp()).build();
    }

    private Desk buildDeskForSave(DeskUpdateRequest newDesk, Desk deskFound, Set<Rating> ratingsToSave, Set<Report> reportsToSave, Set<Favorite> favoritesToSave, Set<Sitting> sittings) {
        return Desk.builder()
                    .id(deskFound.getId())
                    .desknumber(deskFound.getDesknumber())
                    .state(StringUtils.isNotBlank(newDesk.getState())?
                            newDesk.getState() :deskFound.getState())
                    .useStartDate(newDesk.isFree()?null: LocalDateTime.now())
                    .reports(reportsToSave)
                    .ratings(ratingsToSave)
                    .favorites(favoritesToSave)
                    .sittings(sittings)
                    .build();
    }

    private Desk buildDeskForResult(Desk deskFound, Desk deskSaved) {
        return Desk.builder()
                    .id(deskSaved.getId())
                    .desknumber(deskSaved.getDesknumber())
                    .state(StringUtils.isNotBlank(deskSaved.getState())?
                            StateEnum.valueOf(deskSaved.getState()).name():deskFound.getState())
                    .useStartDate(deskSaved.getUseStartDate())
                    .reports(deskSaved.getReports())
                    .ratings(deskSaved.getRatings())
                    .favorites(deskSaved.getFavorites())
                    .favorite(deskSaved.isFavorite())
                    .build();
    }

    //optimize by query
    private void checkDeskFavoriteRule(String userIp) {

         List<Site> sites = findAll();

        Set<Desk> desks = sites.stream().map(Site::getFloors).flatMap(Collection::stream).map(floor -> floor.getDesks()).flatMap(Collection::stream).collect(Collectors.toSet());

        Set<Favorite> favorites =desks.stream().map(desk -> desk.getFavorites()).flatMap(Collection::stream).collect(Collectors.toSet());

        if (favorites.stream().filter(favorite -> StringUtils.equalsIgnoreCase(favorite.getUserIp(),userIp)).collect(Collectors.toSet()).size()>=MAX_FAX){
             throw new FlexBusinessException("U cannot exceed the max :"+MAX_FAX);
         }
    }

    @Override
    public void createDesk(String siteId, String floorId, Desk desk) {
        Site site = getSite(siteId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(siteId));

        Floor floorFound = site.findFloorByFloorId(floorId);

        Optional.ofNullable(floorFound).orElseThrow(()-> new FlexBusinessException(floorId));

        //new desk
        final Desk newDesk = Desk.builder().id(UUID.randomUUID().toString())
                .desknumber(desk.getDesknumber()).state(StateEnum.FREE.name()).build();

        floorFound.addDesk(newDesk);
        siteRepository.update(site);
    }

    @Override
    public void createFloor(String siteId, Floor floor) {
        Site site = getSite(siteId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(siteId));

        Floor newFloor= Floor.builder()
                .id(UUID.randomUUID().toString())
                .name(floor.getName()).build();

        site.addFloor(newFloor);

        siteRepository.update(site);

    }

    @Override
    public void createSite(Site site) {
       Site newSite = Site.builder().id(UUID.randomUUID().toString())
               .name(site.getName())
               .address(site.getAddress()).build();
        siteRepository.update(newSite);
    }

    @Override
    public void partialUpdateDesk(String siteId, String floorId, String deskId, Desk desk) {

        Site site = getSite(siteId);

        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(siteId));

        Floor floorFound = site.findFloorByFloorId(floorId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(floorId));

        Desk deskFound = floorFound.findDeskByFloorIdAndDeskId(floorId,deskId);
        Optional.ofNullable(desk).orElseThrow(()-> new FlexBusinessException(siteId,floorId, deskId));

        Desk newDesk=Desk.builder().id(deskFound.getId()).state(calculateState(desk,deskFound).name())
                .desknumber(StringUtils.isEmpty(desk.getDesknumber())?deskFound.getDesknumber():desk.getDesknumber()).build();
        //remove the old
        floorFound.removeByDeskId(deskFound.getId());

        //add the new
        floorFound.addDesk(newDesk);

        siteRepository.update(site);

    }

    @Override
    public Resource getQrcode(String siteId, String floorId, String deskId) throws IOException, WriterException {

        String qrText = buildQrcodeUrl(host, scheme,siteId,floorId,deskId);
        String filePath = QRCodeGenerator.DESK_CODE_IMAGE_PATH.concat("_").concat(deskId).concat(".png");
        QRCodeGenerator.generateQRCodeImage(qrText,100,100, filePath);

        File resultFile= new File(filePath);
        resultFile.createNewFile();

        UrlResource urlResource = new UrlResource(resultFile.toURI());

        return urlResource;
    }

    @Override
    public List<Site> searchSite(SearchTerm searchTerm) {
       return siteRepository.findLikeSiteAndLikeFloor(searchTerm.getSiteName(),searchTerm.getFloorName());
    }

    @Override
    public Desk getDeskBySiteIdAndFloorId(String siteId, String floorId, String deskId) {
        Desk deskFound = checkDeskExist(siteId, floorId, deskId);
        return deskFound;
    }

    private Desk checkDeskExist(String siteId, String floorId, String deskId) {
        Site site = getSite(siteId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(siteId));

        Floor floorFound = site.findFloorByFloorId(floorId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(floorId));

        Desk deskFound = floorFound.findDeskByFloorIdAndDeskId(floorId,deskId);
        Optional.ofNullable(deskFound).orElseThrow(()-> new FlexBusinessException(siteId,floorId, deskId));
        return deskFound;
    }

    @Override
    public List<SiteMap> getNearSite(GeoPoint geoPoint) {
       return siteRepository.searchKNN(geoPoint);
    }

    @Override
    public Desk report(String siteId, String floorId, String deskId, Report report) {

        Site site = getSite(siteId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(siteId));

        Floor floorFound = site.findFloorByFloorId(floorId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(floorId));

        Desk deskFound = floorFound.findDeskByFloorIdAndDeskId(floorId,deskId);
        Optional.ofNullable(deskFound).orElseThrow(()-> new FlexBusinessException(siteId,floorId, deskId));


        Report newReport = Report.builder()
                .id(UUID.randomUUID().toString())
                .code(report.getCode())
                .date(LocalDateTime.now())
                .description(report.getDescription())
                .userIp(report.getUserIp()).
                        build();

        deskFound.addReport(newReport);

        //build new Desk
       Desk newDesk = Desk.builder()
               .id(deskFound.getId())
                .ratings(deskFound.getRatings())
                .desknumber(deskFound.getDesknumber())
                .state(deskFound.getState())
               .useStartDate(deskFound.getUseStartDate())
               .reports(deskFound.getReports()).build();

        //remove old desk
        floorFound.removeByDeskId(deskFound.getId());

        floorFound.addDesk(newDesk);


        siteRepository.update(site);

        return deskFound;

    }

    @Override
    public void deleteFavorite(String siteId, String floorId, String deskId, String user) {
        Site site = getSite(siteId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(siteId));

        Floor floorFound = site.findFloorByFloorId(floorId);
        Optional.ofNullable(site).orElseThrow(()-> new FlexBusinessException(floorId));

        Desk deskFound = floorFound.findDeskByFloorIdAndDeskId(floorId,deskId);
        Optional.ofNullable(deskFound).orElseThrow(()-> new FlexBusinessException(siteId,floorId, deskId));

       Set<Favorite> newfavorites =deskFound.getFavorites().stream()
               .filter(favorite -> ! StringUtils.equalsIgnoreCase(favorite.getUserIp(),user)).collect(Collectors.toSet());

        //remove old desk
        floorFound.removeByDeskId(deskFound.getId());

        //build new Desk
        Desk newDesk = Desk.builder()
                .id(deskFound.getId())
                .ratings(deskFound.getRatings())
                .desknumber(deskFound.getDesknumber())
                .state(deskFound.getState())
                .useStartDate(deskFound.getUseStartDate())
                .favorites(newfavorites)
                .reports(deskFound.getReports()).build();

        floorFound.addDesk(newDesk);

        siteRepository.update(site);

    }


    public static String buildQrcodeUrl(String scheme, String host, String siteId, String floorId,String deskId){
        return UriComponentsBuilder.newInstance().scheme("http").host(host)
                .path("sites/").path("{siteId}/")
                .path("floors/").path("{floorName}/")
                .path("desks/").path("{deskId}").buildAndExpand(siteId,floorId,deskId).toString();

    }

    private StateEnum calculateState(Desk newDesk,Desk oldDesk) {
        StateEnum stateEnum =  StringUtils.isEmpty(newDesk.getState())?
                StateEnum.valueOf(oldDesk.getState()):StateEnum.valueOf(oldDesk.getState())
                .nextState(StateEnum.valueOf(newDesk.getState()));

       return Optional.ofNullable(stateEnum).orElseThrow(()-> new IllegalStateException(newDesk.getState()));
    }


    public static Date convertToDate(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault())
                .toInstant());
    }


}
