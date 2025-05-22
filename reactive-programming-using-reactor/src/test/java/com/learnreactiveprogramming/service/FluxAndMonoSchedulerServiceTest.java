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
}