package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

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

    @Test
    void namesFluxMap() {
        //given - Precondition or setup
        int stringLength = 5;

        //when - action or behaviour that we are going to test
        var namesFlux = fluxAndMonoGeneratorService.namesFluxMap(stringLength);

        //then - verify output
        StepVerifier.create(namesFlux)
                .expectNext("7-SHEKHAR")
                .expectComplete();

    }

    @Test
    void namesFlux_Immutability() {
        //given - precondition or setup

        //when - action or behaviour that we are going to test
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_Immutability();

        //then - verify output
        StepVerifier.create(namesFlux)
                .expectNext("shekhar", "bheem", "golla")
                .expectComplete();
    }

    @Test
    void namesFlux_flatMap() {
        //given - precondition or setup
        int stringLength = 3;

        //when - action or behaviour that we are going to test
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatMap(stringLength);

        //then - verify output
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X","C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatMap_async() {
        //given - precondition or setup
        int stringLength = 3;

        //when - action or behaviour that we are going to test
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatMap_async(stringLength);

        //then - verify output
        StepVerifier.create(namesFlux)
                //.expectNext("A", "L", "E", "X","C", "H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap() {
        //given - precondition or setup
        int stringLength = 3;

        //when - action or behaviour that we are going to test
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap(stringLength);

        //then - verify output
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X","C", "H", "L", "O", "E")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesMono_flatMap() {
        //given - precondition or setup
        var stringLength = 3;

        //when - action or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.namesMono_flatMap(stringLength);

        //then - verify output
        StepVerifier.create(value)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void namesMono_flatMapMany() {
        //given - precondition or setup
        var stringLength = 3;

        //when - action or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLength);

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {
        //given - precondition or setup
        int stringLength = 3;

        //when - action or behaviour going to test
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        //verify - output
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X","C", "H", "L", "O", "E")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_DefaultIfEmpty() {
        //given - precondition or setup
        int stringLength = 5;

        //when - action or behaviour going to test
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        //verify - output
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchIfEmpty() {
        //given - precondition or setup
        int stringLength = 5;

        //when - action or behaviour going to test
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform_switchIfEmpty(stringLength);

        //verify - output
        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }
}
