package com.keya.flexoffice.domain;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Floor {
    private String id;
    private String name;
    private Set<com.keya.flexoffice.domain.Desk> desks= new HashSet<>();


    public Set<com.keya.flexoffice.domain.Desk> getFreeDesk(){
        return this.desks.stream().filter(desk -> desk.isFree()).collect(Collectors.toSet());
    }

    public long getFreeDeskCount(){
        return this.getFreeDesk().stream().count();
    }


    public Set<Desk> findDeskByFloorId(String floorId){
        Set<Desk> desks = new HashSet<>();
        if (StringUtils.equals(floorId,this.id) ){
            desks= this.desks;
        }
        desks.stream().sorted(Comparator.comparing(Desk::getUseStartDate));
        return desks;
    }

    public Desk findDeskByFloorIdAndDeskId(String floorId, String deskId){
        return  this.findDeskByFloorId(floorId).stream().filter(desk -> StringUtils.equalsIgnoreCase(desk.getId(),deskId)).findFirst().get();
    }

    public void addDesk(Desk desk) {
        if (CollectionUtils.isEmpty(this.desks)){
            this.desks = new HashSet<>();
        }
        this.desks.add(desk);
    }

    public void removeByDeskId(String id) {
        this.desks.removeIf(desk -> StringUtils.equals(desk.getId(),id));
    }
}
