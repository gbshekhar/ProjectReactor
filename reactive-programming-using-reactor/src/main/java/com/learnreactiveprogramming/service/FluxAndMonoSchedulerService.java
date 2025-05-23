package com.learnreactiveprogramming.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.learnreactiveprogramming.util.CommonUtil.delay;
@Slf4j
public class FluxAndMonoSchedulerService {

    static List<String> namesList = List.of("alex", "ben", "chloe");
    static List<String> namesList1 = List.of("adam", "jill", "jack");

    //PublishOn
    public Flux<String> explore_publishOn(){
        var namesFlux =  Flux.fromIterable(namesList)
                .publishOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();

        var namesFlux1 =  Flux.fromIterable(namesList1)
                .publishOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();

        return namesFlux.mergeWith(namesFlux1);
    }

    //SubscribeOn
    public Flux<String> explore_subscribeOn(){
        var namesFlux =  getMap(namesList)
                .subscribeOn(Schedulers.parallel())
                .map(s -> {
                    log.info("Name is {}", s);
                    return s;
                })
                .log();

        var namesFlux1 =  getMap(namesList1)
                .subscribeOn(Schedulers.parallel())
                .map(s -> {
                    log.info("Name is {}", s);
                    return s;
                })
                .log();

        return namesFlux.mergeWith(namesFlux1);
    }

    //Parallel task in Reactive Streams
    public ParallelFlux<String> explore_parallel() {
        //To get number of cores
        var noOfCores = Runtime.getRuntime().availableProcessors();
        log.info("Number of Cores: {}", noOfCores);

         return  Flux.fromIterable(namesList)
                .parallel()
                 .runOn(Schedulers.parallel())
                .map(this::upperCase)
                .log();

    }

    //Parallel task in Reactive Streams using FlatMap
    public Flux<String> explore_parallel_usingFlatMap() {
        return  Flux.fromIterable(namesList)
                .flatMap(name -> {
                    return Mono.just(name)
                            .map(this::upperCase)
                            .subscribeOn(Schedulers.parallel());
                })
                .log();
    }

    public Flux<String> explore_parallel_usingFlatMap1(){
        var namesFlux =  Flux.fromIterable(namesList)
                .flatMap(name -> {
                    return Mono.just(name)
                            .map(this::upperCase)
                            .subscribeOn(Schedulers.parallel());
                })
                .log();

        var namesFlux1 =  Flux.fromIterable(namesList1)
                .flatMap(name -> {
                    return Mono.just(name)
                            .map(this::upperCase)
                            .subscribeOn(Schedulers.parallel());
                })
                .log();

        return namesFlux.mergeWith(namesFlux1);
    }

    //Parallel task in Reactive Streams using FlatMapSequential
    public Flux<String> explore_parallel_usingFlatMapSequential() {
        return  Flux.fromIterable(namesList)
                .flatMapSequential(name -> {
                    return Mono.just(name)
                            .map(this::upperCase)
                            .subscribeOn(Schedulers.parallel());
                })
                .log();
    }

    private Flux<String> getMap(List<String> namesList) {
        return Flux.fromIterable(namesList)
                .map(this::upperCase);
    }

    private String upperCase(String name){
        delay(1000);
        return name.toUpperCase();
    }
}
