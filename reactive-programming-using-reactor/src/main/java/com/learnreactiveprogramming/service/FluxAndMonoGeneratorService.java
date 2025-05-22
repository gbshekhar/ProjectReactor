package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
@Slf4j
public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){
      return Flux.fromIterable(List.of("shekhar", "bheem", "golla"))
              .log();//db or remote service call
    }

    public Mono<String> nameMono(){
        return Mono.just("shekhar")
                .log();
    }

    //map and filter
    public Flux<String> namesFluxMap(int stringLength){
        return Flux.fromIterable(List.of("shekhar", "bheem", "golla"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + "-"+s)
                .doOnNext(name -> {
                    System.out.println("Name is : "+name);
                    name.toLowerCase();
                })
                .doOnSubscribe(s -> {
                    System.out.println("OnSubscribe :" +s);
                })
                .doOnComplete(() -> {
                    System.out.println("Inside complete callback");
                })
                .doFinally(signalType -> {
                    System.out.println("Inside doFinally :"+ signalType);
                })
                .log();
    }

    //Reactive Streams are immutable
    public Flux<String> namesFlux_Immutability(){
         var namesFlux = Flux.fromIterable(List.of("shekhar", "bheem", "golla"));
         namesFlux.map(String::toUpperCase);
         return namesFlux;
    }

    //FlatMap
    public Flux<String> namesFlux_flatMap(int stringLength){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                //ALEX,CHLOE -> A, L, E, X, C, H, L, O, E
                .flatMap(s -> splitString(s))
                .log();
    }

    //ALEX -> A, L, E, X
    public Flux<String> splitString(String name){
        var stringArray = name.split("");
        return Flux.fromArray(stringArray);
    }

    //FlatMap with Async
    //FlatMap won't preserve the order
    public Flux<String> namesFlux_flatMap_async(int stringLength){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                //ALEX,CHLOE -> A, L, E, X, C, H, L, O, E
                .flatMap(s -> splitString_withDelay(s))
                .log();
    }

    //ALEX -> Flux(A, L, E, X)
    public Flux<String> splitString_withDelay(String name){
        var stringArray = name.split("");
        var delay = new Random().nextInt(1000);
        return Flux.fromArray(stringArray)
                .delayElements(Duration.ofMillis(delay));
    }

    //ConcatMap
    //Same like FlatMap but ordering is perserved and execution time is more than FlatMap
    public Flux<String> namesFlux_concatMap(int stringLength){
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                //ALEX,CHLOE -> A, L, E, X, C, H, L, O, E
                .concatMap(s -> splitString_withDelay(s))
                .log();
    }

    //Transform
    public Flux<String> namesFlux_transform(int stringLength){
        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(s -> splitString(s))
                .defaultIfEmpty("default")
                .log();
    }

    //SwitchIfEmpty
    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength){
        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(s -> splitString(s));

        var defaultFlux = Flux.just("default")
                .transform(filterMap);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    //Mono with map and filter
    public Mono<String> namesMono_map_filter(int stringLength){
        return  Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
    }

    //Mono with FlatMap
    public Mono<List<String>> namesMono_flatMap(int stringLength){
        return  Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }


    //Mono with FlatMapMany
    public Flux<String> namesMono_flatMapMany(int stringLength){
        return  Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        var stringArray = s.split("");
        var stringList = List.of(stringArray);
        return Mono.just(stringList);
    }

    //Concat
    public Flux<String> explore_concat(){
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return Flux.concat(abcFlux, defFlux).log();
    }

    //ConcatWith
    public Flux<String> explore_concatWith(){
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return abcFlux.concatWith(defFlux).log();
    }

    //ConcatWithMono
    public Flux<String> explore_concatWith_Mono(){
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.concatWith(bMono).log();
    }

    //Merge
    public Flux<String> explore_merge(){
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return Flux.merge(abcFlux, defFlux).log();
    }

    //MergeWith
    public Flux<String> explore_merge_with(){
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return abcFlux.mergeWith(defFlux).log();
    }

    //MergeWithMono
    public Flux<String> explore_mergeWith_Mono(){
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.mergeWith(bMono).log();
    }

    //MergeSequential
    public Flux<String> explore_mergeSequential(){
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return Flux.mergeSequential(abcFlux, defFlux).log();
    }

    //Zip
    public Flux<String> explore_zip(){
        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second).log();//AD, BE, CF
    }

    //Zip
    public Flux<String> explore_zip_1(){
        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        var _123Flux = Flux.just("1", "2", "3");

        var _456Flux = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
                .log();//AD14, BE25, CF36

    }

    //Zip with
    public Flux<String> explore_zipWith(){
        var abcFlux = Flux.just("A", "B", "C");

        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.zipWith(defFlux, (first, second) -> first + second).log();//AD, BE, CF
    }

    //ZipWithMono
    public Mono<String> explore_zipWith_Mono(){
        var aMono = Mono.just("A");

        var bMono = Mono.just("B");

        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1() + t2.getT2())
                .log();
    }

    //Exception
    public Flux<String> exception_flux(){
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")))
                .concatWith(Flux.just("D"))
                .log();
    }

    //OnErrorReturn
    public Flux<String> explore_OnErrorReturn(){
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalArgumentException("Exception Occurred")))
                .onErrorReturn("D");
    }

    //OnErrorResume
    public Flux<String> explore_OnErrorResume(Exception e){
        var recoveryFlux = Flux.just("D", "E", "F");

        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(e))
                .onErrorResume(ex -> {
                    log.error("Exception :", ex);
                    if(ex instanceof IllegalStateException){
                        return recoveryFlux;
                    } else{
                        return Flux.error(ex);
                    }

                })
                .log();
    }

    //OnErrorContinue
    public Flux<String> explore_OnErrorContinue(){

        return Flux.just("A", "B", "C")
                .map(name -> {
                    if(name == "B")
                        throw new IllegalArgumentException("Exception occured");
                    return name;
                })
                .onErrorContinue((ex, name) -> {
                    log.error("Exception :", ex);
                    log.info("Name is {}", name);
                })
                .log();
    }

    //OnErrorMap
    public Flux<String> explore_OnErrorMap(){

        return Flux.just("A", "B", "C")
                .map(name -> {
                    if(name == "B")
                        throw new IllegalArgumentException("Exception occured");
                    return name;
                })
                .onErrorMap(ex -> {
                    log.error("Exception :", ex);
                    return new ReactorException(ex, ex.getMessage());
                })
                .log();
    }

    //doOnError
    public Flux<String> explore_doOnError(){
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalArgumentException("Exception Occurred")))
                .doOnError(ex -> {
                    log.error("exception is:", ex);
                })
                .log();
    }

    public static void main(String[] args) {
       FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
       //We need to subscribe then only events will flow
       fluxAndMonoGeneratorService.namesFlux().subscribe(name -> System.out.println("Flux Name is :" + name));

       fluxAndMonoGeneratorService.nameMono().subscribe(name -> System.out.println("Mono Name is:" + name));
    }
}
