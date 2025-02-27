package org.anildeveloper.SpringBoot01.repositories;

import java.util.Optional;

import org.anildeveloper.SpringBoot01.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>{
    Optional<Account> findByEmail(String email);

    Optional<Account> findByPasswordResetToken(String token);
   
}
