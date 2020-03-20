package com.keya.flexoffice.infra.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@ToString
public class AddressJPA {
    private String streetNumber;
    private String street;
    private String postalCode;
    private String city;
    private String country;
}
