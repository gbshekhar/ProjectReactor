package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.List;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    //Junit Test for
    @Test
    public void namesFlux() {
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
    public void nameMono() {
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
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
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
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap_virtualTimer() {
        //given - precondition or setup
        VirtualTimeScheduler.getOrSet();
        int stringLength = 3;

        //when - action or behaviour that we are going to test
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap(stringLength);

        //then - verify output
        StepVerifier.withVirtualTime(() ->namesFlux)
                .thenAwait(Duration.ofSeconds(10))
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
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
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
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

    @Test
    void explore_concat() {
        //given - precondition or setup

        //when - action or behaviour to test
        var concatFlux = fluxAndMonoGeneratorService.explore_concat();

        //then - verify output
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concat_with() {
        //given - precondition or setup

        //when - action or behaviour to test
        var concatFlux = fluxAndMonoGeneratorService.explore_concatWith();

        //then - verify output
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concat_with_Mono() {
        //given - precondition or setup

        //when - action or behaviour to test
        var concatFlux = fluxAndMonoGeneratorService.explore_concatWith_Mono();

        //then - verify output
        StepVerifier.create(concatFlux)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        //given - precondition or setup

        //when - action or behaviour to test
        var value = fluxAndMonoGeneratorService.explore_merge();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_merge_with() {
        //given - precondition or setup

        //when - action or behaviour to test
        var value = fluxAndMonoGeneratorService.explore_merge_with();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_merge_with_Mono() {
        //given - precondition or setup

        //when - action or behaviour to test
        var concatFlux = fluxAndMonoGeneratorService.explore_mergeWith_Mono();

        //then - verify output
        StepVerifier.create(concatFlux)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {
        //given - precondition or setup

        //when - action or behaviour to test
        var concatFlux = fluxAndMonoGeneratorService.explore_mergeSequential();

        //then - verify output
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_zip() {
        //given - precondition or setup

        //when - action or behaviour going to test
        var value = fluxAndMonoGeneratorService.explore_zip();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_1() {
        //given - precondition or setup

        //when - action or behaviour going to test
        var value = fluxAndMonoGeneratorService.explore_zip_1();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }

    @Test
    void explore_zipWith() {
        //given - precondition or setup

        //when - action or behaviour going to test
        var value = fluxAndMonoGeneratorService.explore_zipWith();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_with_Mono() {
        //given - precondition or setup

        //when - action or behaviour to test
        var valueMono = fluxAndMonoGeneratorService.explore_zipWith_Mono();

        //then - verify output
        StepVerifier.create(valueMono)
                .expectNext("AB")
                .verifyComplete();
    }

    @Test
    void exception_flux() {
        //given - precondition or setup

        //when - action or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.exception_flux();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void exception_flux1() {
        //given - precondition or setup

        //when - action or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.exception_flux();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "B", "C")
                .expectError()
                .verify();
    }

    @Test
    void exception_flux2() {
        //given - precondition or setup

        //when - action or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.exception_flux();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "B", "C")
                .expectErrorMessage("Exception occurred")
                .verify();
    }

    @Test
    void explore_OnErrorReturn() {
        //given - precondition or setup

        //when - actin or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.explore_OnErrorReturn();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume() {
        //given - precondition or setup
        var e = new IllegalStateException("Exception Occurred");

        //when - action or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.explore_OnErrorResume(e);

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume1() {
        //given - precondition or setup
        var e = new RuntimeException("Exception Occurred");

        //when - action or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.explore_OnErrorResume(e);

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void explore_OnErrorContinue() {
        //given - precondition or setup

        //when - action or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.explore_OnErrorContinue();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "C")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorMap() {
        //given - precondition or setup

        //when - action or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.explore_OnErrorMap();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();

    }

    @Test
    void explore_doOnError() {
        //given - precondition or setup

        //when - action or behaviour that we are going to test
        var value = fluxAndMonoGeneratorService.explore_doOnError();

        //then - verify output
        StepVerifier.create(value)
                .expectNext("A", "B", "C")
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}


