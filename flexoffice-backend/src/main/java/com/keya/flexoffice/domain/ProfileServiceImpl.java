package com.keya.flexoffice.domain;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component()
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final SiteService siteService;
    final Comparator<SittingTrack> sittingComparator = Comparator.comparing(SittingTrack::getStart);
    final Comparator<FavoriteTrack> favoriteTrackComparator = Comparator.comparing(FavoriteTrack::getDate);

    @Override
    public Profile getProfileByUserIp(String user) {
         Set<FavoriteTrack> favoriteTracks = new HashSet<>();
         Set<SittingTrack> sittings = new HashSet<>();
         Set<ReportTrack> reportTracks = new HashSet<>();
         Set<Rating> ratingTracks = new HashSet<>();

        siteService.findAll().forEach(site -> {
            site.getFloors()
                    .forEach(floor -> floor.getDesks()
                            .forEach(desk -> {
                                 Set<FavoriteTrack> favorites = extractFavorites(user, site, floor, desk);
                                favoriteTracks.addAll(favorites);

                                Set<SittingTrack> userSittings = extractSittingTracks(user, site, floor, desk);
                                sittings.addAll(userSittings);

                                Set<ReportTrack> userReports = extractReport(user, site, floor, desk);
                                reportTracks.addAll(userReports);

                                Set<Rating> userRatings = extractRating(user, site, floor, desk);
                                ratingTracks.addAll(userRatings);
                                
                            }));
        });



        favoriteTracks.stream().sorted(favoriteTrackComparator.reversed());
        sittings.stream().sorted(sittingComparator.reversed());



        final StatParameter statParameter = StatParameter.builder()
                .reportCounter(sumReport(reportTracks))
                .sittingCounter(new BigDecimal(sittings.size()))
                .ratingCounter(new BigDecimal(ratingTracks.size()))
                .ratingStars(ratingTracks.stream().mapToInt(Rating::getStars).summaryStatistics())
                .build();

        return Profile.builder().userIp(user)
                .favoris(Sets.newHashSet(favoriteTracks))
                .reports(reportTracks).ratings(ratingTracks)
                .sittings(sittings).statParameters(statParameter).build();

    }

    private Set<Rating> extractRating(String user, Site site, Floor floor, Desk desk) {
        return  desk.getRatings().stream()
                .filter(rating -> StringUtils.equalsIgnoreCase(rating.getUserIp(),user))
                .map( rat ->
                        Rating.builder()
                                .stars(rat.getStars())
                                .siteId(site.getId())
                                .siteName(site.getName())
                                .floorId(floor.getId())
                                .deskId(desk.getId())
                                .siteName(site.getName())
                                .deskNumber(desk.getDesknumber())
                                .deskStatus(desk.getState())
                                .userIp(user).
                                build()).collect(Collectors.toSet());

    }

    private Set<FavoriteTrack> extractFavorites(String user, Site site, Floor floor, Desk desk) {
       return  desk.getFavorites().stream()
                 .filter(favorite -> StringUtils.equalsIgnoreCase(favorite.getUserIp(),user))
                .map( favorite ->
                    FavoriteTrack.builder()
                    .siteId(site.getId())
                    .siteName(site.getName())
                    .floorId(floor.getId())
                    .deskId(desk.getId())
                    .siteName(site.getName())
                    .deskNumber(desk.getDesknumber())
                    .deskStatus(desk.getState())
                    .userIp(user).
                            build()).collect(Collectors.toSet());
    }


    private BigDecimal sumReport(Set<ReportTrack> reportTracks) {
        return new BigDecimal(reportTracks.size());

    }

    private Set<ReportTrack> extractReport(String user, Site site, Floor floor, Desk desk) {
        return (desk.getReports().stream()
                .filter(rep -> StringUtils.equalsIgnoreCase(rep.getUserIp(),user)))
                .map(report -> ReportTrack.builder()
                                    .id(report.getId())
                                    .deskId(desk.getId())
                                    .deskNumber(desk.getDesknumber())
                                    .deskState(desk.getState())
                                    .floorId(floor.getId())
                                    .siteId(site.getId())
                                    .siteName(site.getName())
                                    .build()).collect(toSet());

    }

    private Set<SittingTrack> extractSittingTracks(String user, Site site, Floor floor, Desk desk) {
        return (desk.getSittings().stream()
                                            .filter(sitting -> StringUtils.equalsIgnoreCase(sitting.getUserIp(),user))).map(sitting ->
                                                SittingTrack.builder()
                                                        .id(sitting.getId())
                                                        .deskId(desk.getId())
                                                        .deskNumber(desk.getDesknumber())
                                                        .deskStatus(desk.getState())
                                                        .start(sitting.getStart())
                                                        .end(sitting.getEnd())
                                                        .floorId(floor.getId())
                                                        .siteId(site.getId())
                                                        .siteName(site.getName())
                                                        .duration(calculateDur(sitting))
                                                        .state(desk.getState())
                                                        .build()
                                            ).collect(toSet());
    }

    @Override
    public Profile save(String user) {
        Profile profile = Profile.builder().userIp(user).id(UUID.randomUUID().toString()).build();
        return profileRepository.update(profile);
    }

    @Override
    public GlobalStatParameter calculate() {
        Set<String> allUsers = new HashSet<>();

        siteService.findAll().forEach(site -> {
            site.getFloors()
                    .forEach(floor -> floor.getDesks()
                            .forEach(desk -> {
                            Set<String> usersInReport =desk.getReports().stream().filter(Objects::nonNull).map(Report::getUserIp).collect(Collectors.toSet());
                                allUsers.addAll(usersInReport);
                            Set<String> usersInFavorites =desk.getFavorites().stream().filter(Objects::nonNull).map(Favorite::getUserIp).collect(Collectors.toSet());
                                allUsers.addAll(usersInFavorites);
                            Set<String> usersInRating =desk.getRatings().stream().filter(Objects::nonNull).map(Rating::getUserIp).collect(Collectors.toSet());
                                allUsers.addAll(usersInRating);

                            }));

        });

        Set<Profile> profiles = allUsers.stream().map(this::getProfileByUserIp).collect(Collectors.toSet());

        IntSummaryStatistics reportStats = profiles.stream().map(Profile::getReports).filter(Objects::nonNull).mapToInt(Set::size).filter(value -> value>0).summaryStatistics();
        IntSummaryStatistics sittingStats = profiles.stream().map(Profile::getSittings).filter(Objects::nonNull).mapToInt(Set::size).filter(value -> value>0).summaryStatistics();
        IntSummaryStatistics favorisStats = profiles.stream().map(Profile::getFavoris).filter(Objects::nonNull).mapToInt(Set::size).filter(value -> value>0).summaryStatistics();
        IntSummaryStatistics ratingStats = profiles.stream().map(Profile::getRatings).filter(Objects::nonNull).mapToInt(Set::size).filter(value -> value>0).summaryStatistics();

        /*IntSummaryStatistics ratingStarsStats = profiles.stream().map(Profile::getRatings)
                .flatMap(Collection::stream)
                .filter(rating -> Objects.nonNull(rating.getStars()))
                .mapToInt(Rating::getStars).summaryStatistics();*/

        Set<Rating> allRatings = new HashSet<>();

        profiles.forEach(profile -> allRatings.addAll(profile.getRatings()));

        IntSummaryStatistics ratingStarsStats = allRatings.stream().filter(r -> Objects.nonNull(r) && Objects.nonNull(r.getStars())).mapToInt(Rating::getStars).summaryStatistics();



        return GlobalStatParameter.builder()
                .reports(reportStats)
                .sittings(sittingStats)
                .favoris(favorisStats)
                .ratingStats(ratingStats)
                .ratingStarsStats(ratingStarsStats).build();

    }


    private  static long calculateDur(Sitting sitting){
        if (sitting.getStart()!=null && sitting.getEnd()!=null){
        return  Math.abs(Duration.between(sitting.getStart(), sitting.getEnd()).toMillis());
        }
        return 0;
    }
}
