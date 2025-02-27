package org.anildeveloper.SpringBoot01.sevices;

import java.util.Optional;

import org.anildeveloper.SpringBoot01.models.Authority;
import org.anildeveloper.SpringBoot01.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
    
    @Autowired
    private AuthorityRepository authorityRepository;

    public Authority save(Authority authority){
        return authorityRepository.save(authority);
    }

    public Optional<Authority> findById(Long id){
        return authorityRepository.findById(id);  
    }
}
