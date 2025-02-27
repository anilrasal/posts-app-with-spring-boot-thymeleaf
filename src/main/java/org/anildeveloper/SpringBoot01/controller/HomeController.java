package org.anildeveloper.SpringBoot01.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.anildeveloper.SpringBoot01.models.Post;
import org.anildeveloper.SpringBoot01.sevices.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;
    
    @GetMapping("/")
    public String home(@RequestParam(required=false, name="sort_by", defaultValue = "createdAt") String sortBy,
    @RequestParam(required=false,name="per_page", defaultValue="2") String perPage,
    @RequestParam(required = false, name="page", defaultValue = "1") String page,
    Model model){
        // List<Post> posts = postService.getAll();
        // model.addAttribute("posts", posts);
        // return "home_views/home";

        Page<Post> postsOnPage = postService.getAll(Integer.parseInt(page)-1, Integer.parseInt(perPage), sortBy);
        //Page starts from 0 hence subtract 1;
        List<Integer> pages = new ArrayList<>();
        int totalPages = postsOnPage.getTotalPages();
        if(totalPages>0){
            pages = IntStream.rangeClosed(0, totalPages-1)
            .boxed().collect(Collectors.toList());
            System.out.println("********These are the pages*****"+pages);
        }

        List<String> links = new ArrayList<>();
        if(pages!=null){
            for(int link: pages){
                String active="";
                if(link==postsOnPage.getNumber()){
                    active = "active";
                }
                System.out.println("****? These are the link numbers " + link);
                String _tempLink = "/?per_page="+perPage +"&page=" +(link+1) + "&sort_by="+sortBy;
                links.add("<li class=\"page-item " + active + "\"><a href=\""+ _tempLink+ "\" class=\"page-link\">"+ (link+1)+"</a></li>");
            }
            System.out.println(links);
            for(String l:links){
                System.out.println(l);
            }
            System.out.println();
            model.addAttribute("links", links);
        }
        model.addAttribute("posts", postsOnPage);
        
        return "home_views/home";

    }

    @GetMapping("/editor")
    public String editor(Model model){
        return "editor_views/editor";
    }
    
}
