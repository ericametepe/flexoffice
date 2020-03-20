package com.keya.flexoffice.infra;

import com.keya.flexoffice.infra.entities.ProfileJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileJPARepository extends JpaRepository<ProfileJPA, String> {

    Optional<ProfileJPA> findByUserIp(String userIp);



}
