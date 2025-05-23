package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

@Slf4j
public class ColdAndHotPublisherTest {

    //Junit Test for
    @Test
    public void coldPublisherTest(){

        var flux = Flux.range(1, 10);

        flux.subscribe(i -> log.info("Subcriber 1 :{}", i));

        flux.subscribe(i -> log.info("Subcriber 1 :{}", 2));

    }

    //Hot Stream - connect : will emit data even with zero publisher
    @Test
    public void hotPublisherTest(){

        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<Integer> connectableFlux = flux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(i -> log.info("Subcriber 1 :{}", i));
        delay(4000);
        connectableFlux.subscribe(i -> log.info("Subcriber 2:{}", i));
        delay(10000);//Test case to wait
    }

    //Hot Stream - auto connect : will emit data even only when minimum subscribers are subscribed
    @Test
    public void hotPublisherTest_autoConnect(){

        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));

        var hotSourceFlux = flux.publish().autoConnect(2);

        hotSourceFlux.subscribe(i -> log.info("Subcriber 1 :{}", i));
        delay(2000);
        hotSourceFlux.subscribe(i -> log.info("Subcriber 2:{}", i));
        log.info("Two subscribers are connected");
        delay(2000);
        hotSourceFlux.subscribe(i -> log.info("Subcriber 3:{}", i));
        delay(10000);//Test case to wait
    }

    //Hot Stream - refCount : will emit data even only when minimum subscribers are subscribed
    //It always checking for subscribers
    @Test
    public void hotPublisherTest_refCount(){

        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));

        var hotSourceFlux = flux.publish().refCount(2)
                .doOnCancel(() -> log.info("Cancel Signal received"));

        var disposable1 = hotSourceFlux.subscribe(i -> log.info("Subcriber 1 :{}", i));
        delay(2000);
        var disposable2 =hotSourceFlux.subscribe(i -> log.info("Subcriber 2:{}", i));
        log.info("Two subscribers are connected");
        delay(2000);
        disposable1.dispose();
        disposable2.dispose();
        hotSourceFlux.subscribe(i -> log.info("Subcriber 3:{}", i));
        delay(2000);
        hotSourceFlux.subscribe(i -> log.info("Subcriber 4:{}", i));
        delay(10000);//Test case to wait
    }
}
