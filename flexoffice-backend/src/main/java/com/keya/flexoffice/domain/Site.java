package com.keya.flexoffice.domain;


import com.keya.flexoffice.exposition.api.GeoPoint;
import com.keya.flexoffice.infra.FlexBusinessException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Site {
    private String id;
    private String name;
    private com.keya.flexoffice.domain.Address address;
    private String addressLabel;
    private Set<com.keya.flexoffice.domain.Floor> floors;
    private GeoPoint geoPoint;


    public Set<com.keya.flexoffice.domain.Desk> getFreeDesk(){
        Set<com.keya.flexoffice.domain.Desk> result = new HashSet<>();
        this.floors.forEach(floor -> result.addAll(floor.getFreeDesk()));
        return result;
    }

    public Set<Desk> getAllDesk(){
        Set<Desk> result = new HashSet<>();
        this.floors.forEach(floor -> result.addAll(floor.getDesks()));
        return result;
    }

   public int  getFreeDeskCount(){

        return getFreeDesk().size();
    }



    public Set <Floor> findFloorBySiteId(String siteId){
        if (!StringUtils.equalsIgnoreCase(this.id,siteId)){
           throw new FlexBusinessException(siteId);
        }
    return this.floors;

    }


    public String getAddressLabel() {
        return Objects.isNull(address)?" ":
                this.address.getStreetNumber()
                + " " +this.address.getStreet()
                + " " + this.address.getPostalCode()
                +" " + this.address.getCity()
                +" "  + this.address.getCountry();

    }




    public  Floor findFloorByFloorId(String floorId){
    return this.floors.stream()
            .filter(floor -> StringUtils.equalsIgnoreCase(floorId,floor.getId())).findFirst()
            .orElseThrow(()->new FlexBusinessException());
    }

    public Desk findDeskByFloorIdAndDeskId(String floorId, String deskId){
        Floor floor = findFloorByFloorId(floorId);
        return floor.findDeskByFloorIdAndDeskId(floorId, deskId);
    }

    public  void removeByFloorId( String floorId){
     this.floors.removeIf(floor -> StringUtils.equals(floorId,floor.getId()));
    }


    public void addFloor(Floor newFloor) {
        if (CollectionUtils.isEmpty(this.floors)){
            this.floors = new HashSet<>();
        }
        this.floors.add(newFloor);
    }

    public Set<Floor> getFreeFloor() {
       return this.floors.stream().filter(floor -> floor.getFreeDeskCount()>0).collect(Collectors.toSet());

    }


}
