package com.keya.flexoffice.domain;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FloorTest {

    private Floor floor;

    @BeforeEach
    public void setup(){
        Desk  desk = Desk.builder().desknumber("D1").id("D1").build();
        Set<Desk> desks = new HashSet<>();
        desks.add(desk);
        floor =Floor.builder().id("F1").name("F1").desks(desks).build();
    }

    @Test
    public void findDeskByFloorId() {
        String f1 = "F1";
       assertTrue(floor.findDeskByFloorId(f1).size()==1);
    }

    @Test
    public void findDeskByFloorIdAndDeskId() {
    }
}
