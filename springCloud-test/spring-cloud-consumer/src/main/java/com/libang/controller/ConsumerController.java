package com.libang.controller;

import com.libang.client.MovieServiceClient;
import com.libang.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author libang
 * @date 2018/9/3 12:53
 */
@RestController
public class ConsumerController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MovieServiceClient movieServiceClient;

  /*  @Autowired
    private LoadBalancerClient loadBalancerClient;*/

  /*frign方式*/

    @GetMapping("/movie/{id:\\d+}")
    public String getMovie(@PathVariable Integer id){
        return movieServiceClient.getMovie(id);

    }

    @PostMapping("/movie/new")
    public String newMovie(String movieName,String author){
        return movieServiceClient.newMovie(movieName,author);

    }

    @PostMapping("/movie/save")
    public String saveMovie(String movieName,String author){
        Movie movie = new Movie();
        movie.setMovieName("西游记");
        movie.setAuthor("吴承恩");
        return  movieServiceClient.saveMovie(movie);
    }






/*ribbon方式*/
 /* @GetMapping("/movie/{id:\\d+}")
  public String getMovie(@PathVariable Integer id){

      String url = "http://MOVIE-SERVICE.PROVIDER/movie/{id}";
      return restTemplate.getForObject(url,String.class,id);
  }
*/



  /*  @GetMapping("/movie/{id:\\d+}")
    public String getMovie(@PathVariable Integer id){
        ServiceInstance serviceInstance =loadBalancerClient.choose("movie-service.provider");

        System.out.println(serviceInstance.getHost());
        System.out.println(serviceInstance.getPort());

        // String url ="http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()+"/movie"+id;
        // String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/movie/" + id;
        String url = serviceInstance.getUri() + "/movie/" + id;
        return restTemplate.getForObject(url,String.class);

    }*/


}
