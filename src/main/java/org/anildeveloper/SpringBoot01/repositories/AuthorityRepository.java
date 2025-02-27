package org.anildeveloper.SpringBoot01.repositories;

import org.anildeveloper.SpringBoot01.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long>{
    
}
