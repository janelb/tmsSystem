package com.libang.controller;

import com.libang.entity.Movie;
import org.springframework.web.bind.annotation.*;

/**
 * @author libang
 * @date 2018/9/3 11:22
 */
@RestController
public class ProviderController {

    @GetMapping("/movie/{id:\\d+}")
    public String findById(@PathVariable Integer id){

        return "西游记";
    }

    @PostMapping("/movie/new")
    public String newMovie(String movieName,String author){
        System.out.println("movieName=====>"+movieName);
        System.out.println("author=====>"+author);

        return "save success!";
    }

    @PostMapping("/movie/save")
    public String saveMovie(@RequestBody Movie movie){
        System.out.println("movieName"+movie.getMovieName());
        System.out.println("author"+movie.getAuthor());
        return "save success!";

    }

}
