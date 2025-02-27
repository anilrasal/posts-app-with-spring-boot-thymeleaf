package org.anildeveloper.SpringBoot01.sevices;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.anildeveloper.SpringBoot01.models.Account;
import org.anildeveloper.SpringBoot01.models.Authority;
import org.anildeveloper.SpringBoot01.repositories.AccountRepository;
import org.anildeveloper.SpringBoot01.util.constants.Roles;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService{

    //@Value("$(spring.mvc.static-path-pattern)")
    //private String photo_prifix;
    
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Account save(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if(account.getRole()==null){
            account.setRole(Roles.USER.getRole());
        }
        if(account.getPhoto()==null){
            //String path = photo_prefix.replace("**","/img/smileImage.png");
            //account.setPhoto(path);

            account.setPhoto("/assets/img/smileImage.png");
        }
        return accountRepository.save(account);
    }
   
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if(!optionalAccount.isPresent()){
            throw new UsernameNotFoundException("Acccount not found");
        }

        Account account = optionalAccount.get();

        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));

        for(Authority auth: account.getPermissions()){
            grantedAuthority.add(new SimpleGrantedAuthority(auth.getName()));
        }

        return new User(account.getEmail(), account.getPassword(), grantedAuthority);
    }
    public Optional<Account> findByEmail(String email){
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findById(long id){
        return accountRepository.findById(id);
    }

    public Optional<Account> findByToken(String token){
        return accountRepository.findByPasswordResetToken(token);
    }
}
