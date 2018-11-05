package com.libang.client;

import com.libang.entity.Movie;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author libang
 * @date 2018/9/3 17:52
 */
@FeignClient(name = "MOVIE-SERVICE.PROVIDER")
public interface MovieServiceClient {

    @GetMapping("/movie/{id}")
    String getMovie(@PathVariable(name = "id" ) Integer id);


    @PostMapping("/movie/new")
    String newMovie(@RequestParam(name = "movieName") String movieName,@RequestParam(name = "author") String author);

    @PostMapping("/movie/save")
    String saveMovie(Movie movie);

}
