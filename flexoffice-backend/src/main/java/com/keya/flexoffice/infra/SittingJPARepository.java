package com.keya.flexoffice.infra;

import com.keya.flexoffice.infra.entities.SittingJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SittingJPARepository extends JpaRepository<SittingJPA, String> {
    List<SittingJPA> findByUserIp(String user);

}
