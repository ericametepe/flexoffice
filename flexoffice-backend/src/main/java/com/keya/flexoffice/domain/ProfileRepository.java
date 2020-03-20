package com.keya.flexoffice.domain;

public interface ProfileRepository {
    Profile getProfileById(String id);

    Profile update(Profile newprofile);
}
