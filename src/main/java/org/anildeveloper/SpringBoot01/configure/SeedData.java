package org.anildeveloper.SpringBoot01.configure;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.anildeveloper.SpringBoot01.models.Account;
import org.anildeveloper.SpringBoot01.models.Authority;
import org.anildeveloper.SpringBoot01.models.Post;
import org.anildeveloper.SpringBoot01.sevices.AccountService;
import org.anildeveloper.SpringBoot01.sevices.AuthorityService;
import org.anildeveloper.SpringBoot01.sevices.PostService;
import org.anildeveloper.SpringBoot01.util.constants.Authorities;
import org.anildeveloper.SpringBoot01.util.constants.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedData implements CommandLineRunner{

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {

        for(Authorities auth: Authorities.values()){
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getAuthorityName());
            authorityService.save(authority);
        }

        Account account01 = new Account();
        Account account02 = new Account();
        Account account03 = new Account();
        Account account04 = new Account();



        account01.setEmail("user01@gmail.com");
        account01.setFirstName("user01");
        account01.setLastName("user01last");
        account01.setPassword("password01");
        account01.setAge(25);
        account01.setDateOfBirth(LocalDate.parse("1990-05-28"));
        account01.setPhoto("/assets/img/avaters/avatar3.png");
        account01.setGender("Male");
        accountService.save(account01);

        
        account02.setEmail("annurasal123@gmail.com");
        account02.setFirstName("admin");
        account02.setLastName("lastName");
        account02.setPassword("password02");
        account02.setRole(Roles.ADMIN.getRole());
        account02.setAge(28);
        account02.setDateOfBirth(LocalDate.parse("1990-06-28"));
        account02.setPhoto("/assets/img/avaters/avatar1.png");
        account02.setGender("Female");        
        accountService.save(account02);

        account03.setEmail("editor@editor.com");
        account03.setFirstName("editor");
        account03.setLastName("lastName");
        account03.setPassword("password03");
        account03.setAge(35);
        account03.setDateOfBirth(LocalDate.parse("1990-05-27"));
        account03.setGender("Female");
        account03.setRole(Roles.EDITOR.getRole());
        accountService.save(account03);

        account04.setEmail("super_editor@editor.com");
        account04.setFirstName("super_editor");
        account04.setLastName("lastName");
        account04.setPassword("password04");
        account04.setRole(Roles.EDITOR.getRole());
        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Authorities.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);;
        authorityService.findById(Authorities.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);;
        account04.setPermissions(authorities);
        account04.setAge(45);
        account04.setDateOfBirth(LocalDate.parse("1980-05-28"));
        account04.setPhoto("/assets/img/avaters/avatar2.png");
        account04.setGender("Male");
        accountService.save(account04);

        List<Post> posts = postService.getAll();
        if(posts.size()==0){
            Post post01 = new Post();
            post01.setTitle("Post 01");
            post01.setBody("Post 01: body.................");
            post01.setAccount(account01);
            postService.save(post01);

            Post post02 = new Post();
            post02.setTitle("Post 02");
            post02.setBody("Post 02: body....................");
            post02.setAccount(account02);
            postService.save(post02);

            Post post03 = new Post();
            post03.setTitle("Post 03");
            post03.setBody("Post 02: body....................");
            post03.setAccount(account03);
            postService.save(post03);

            Post post04 = new Post();
            post04.setTitle("Post 04");
            post04.setBody("Post 02: body....................");
            post04.setAccount(account02);
            postService.save(post04);

            Post post05 = new Post();
            post05.setTitle("Post 05");
            post05.setBody("Post 02: body....................");
            post05.setAccount(account03);
            postService.save(post05);

            Post post06 = new Post();
            post06.setTitle("Post 06");
            post06.setBody("Post 02: body....................");
            post06.setAccount(account02);
            postService.save(post06);

            Post post07 = new Post();
            post07.setTitle("Post 07");
            post07.setBody("Post 02: body....................");
            post07.setAccount(account03);
            postService.save(post07);

        }
    }
    
}
