package com.keya.flexoffice.domain;


public interface ProfileService {
    Profile getProfileByUserIp(String user);

    Profile save(String user);

    GlobalStatParameter calculate();

}
