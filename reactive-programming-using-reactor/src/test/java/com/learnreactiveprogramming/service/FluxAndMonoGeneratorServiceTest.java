package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    //Junit Test for
    @Test
    public void namesFlux(){
        //given - precondition or setup

        //when  - action or behaviour that we are going to test
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then - verify output
        //Here create function call basically takes care of invoking the subscribe call which automatically
        //trigger publisher to send the event
        StepVerifier.create(namesFlux)
                //.expectNext("shekhar", "bheem", "golla")
                //.expectNextCount(3)
                .expectNext("shekhar")
                .expectNextCount(2)
                .expectComplete();
    }

    //Junit Test for
    @Test
    public void nameMono(){
        //given - precondition or setup

        //when  - action or behaviour that we are going to test
        var nameMono = fluxAndMonoGeneratorService.nameMono();

        //then - verify output
        StepVerifier.create(nameMono)
                .expectNext("shekhar")
                .expectComplete();
    }
}
