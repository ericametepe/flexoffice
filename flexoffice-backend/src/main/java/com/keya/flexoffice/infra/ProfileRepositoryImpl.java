package com.keya.flexoffice.infra;

import com.keya.flexoffice.domain.Profile;
import com.keya.flexoffice.infra.entities.ProfileJPA;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProfileRepositoryImpl implements com.keya.flexoffice.domain.ProfileRepository {

    private final ProfileJPARepository profileJPARepository;

    @Override
    public Profile getProfileById(String id) {
        ProfileJPA profileJPA =  profileJPARepository.findByUserIp(id).orElseThrow(()-> new FlexBusinessException("no resource: "+id));
        ModelMapper modelMapper = new ModelMapper();
        Profile profile = modelMapper.map(profileJPA,Profile.class);

        return profile;
    }

    @Override
    public Profile update(Profile newprofile) {
        ModelMapper modelMapper = new ModelMapper();
        ProfileJPA profileJPA = modelMapper.map(newprofile,ProfileJPA.class);
        ProfileJPA profileJPA1 = profileJPARepository.save(profileJPA);
        Profile savedprofile = modelMapper.map(profileJPA1,Profile.class);
        return savedprofile;
    }
}
