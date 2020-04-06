package com.keya.flexoffice.infra;

import lombok.*;

@Setter
@Getter
public class RefDataOther extends RefData {

    private String other;


    public RefDataOther() {
    }

    public RefDataOther(String other) {
        this.other = other;
    }

    public RefDataOther(String code, String label, String other) {
        super(code, label);
        this.other = other;
    }

    @Override
    public String toString() {
        return "RefDataOther{" +
                "other='" + other + '\'' +
                ", code='" + code + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
