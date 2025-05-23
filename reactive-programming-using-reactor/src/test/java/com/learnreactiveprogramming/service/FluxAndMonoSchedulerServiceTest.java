package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoSchedulerServiceTest {

    FluxAndMonoSchedulerService fluxAndMonoSchedulerService =
            new FluxAndMonoSchedulerService();

    @Test
    void explore_publishOn() {
        //given - precondition or setup

        //when - action or behaviour which we are going to test
        var namesFlux = fluxAndMonoSchedulerService.explore_publishOn();

        //then - verify output
        StepVerifier.create(namesFlux)
                //.expectNext("ALEX", "BEN", "CHLOE", "ADAM", "JILL", "JACK")
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_subscribeOn() {
        //given - precondition or setup

        //when - action or behaviour which we are going to test
        var namesFlux = fluxAndMonoSchedulerService.explore_subscribeOn();

        //then - verify output
        StepVerifier.create(namesFlux)
                //.expectNext("ALEX", "BEN", "CHLOE", "ADAM", "JILL", "JACK")
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_parallel() {
        //given - precondition or setup

        //when - action or behaviour which we are going to test
        var namesFlux = fluxAndMonoSchedulerService.explore_parallel();

        //then - verify output
        StepVerifier.create(namesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatMap() {
        //given - precondition or setup

        //when - action or behaviour which we are going to test
        var namesFlux = fluxAndMonoSchedulerService.explore_parallel_usingFlatMap();

        //then - verify output
        StepVerifier.create(namesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatMap1() {
        //given - precondition or setup

        //when - action or behaviour which we are going to test
        var namesFlux = fluxAndMonoSchedulerService.explore_parallel_usingFlatMap1();

        //then - verify output
        StepVerifier.create(namesFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatMapSequential() {
        //given - precondition or setup

        //when - action or behaviour which we are going to test
        var namesFlux = fluxAndMonoSchedulerService.explore_parallel_usingFlatMapSequential();

        //then - verify output
        StepVerifier.create(namesFlux)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }
}