package com.keya.flexoffice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportType {
    INUSE("01"),
    NOTFOUND("02"),
    NONFUNCTIONAL("03"),
    OTHER("04");
    private String code;

}
