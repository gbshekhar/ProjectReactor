package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){
      return Flux.fromIterable(List.of("shekhar", "bheem", "golla"))
              .log();//db or remote service call
    }

    public Mono<String> nameMono(){
        return Mono.just("shekhar")
                .log();
    }
    public static void main(String[] args) {
       FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
       //We need to subscribe then only events will flow
       fluxAndMonoGeneratorService.namesFlux().subscribe(name -> System.out.println("Flux Name is :" + name));

       fluxAndMonoGeneratorService.nameMono().subscribe(name -> System.out.println("Mono Name is:" + name));
    }
}
