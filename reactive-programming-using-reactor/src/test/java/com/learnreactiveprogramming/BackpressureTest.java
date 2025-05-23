package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class BackpressureTest {
    //Junit Test for
    @Test
    public void testBackPressure(){
        var numbersFlux = Flux.range(1, 100).log();

        numbersFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
                //super.hookOnNext(value);
                log.info("hookOnNext :{}", value);
                if(value == 2){
                    cancel();
                }
            }

            @Override
            protected void hookOnComplete() {
                //super.hookOnComplete();
            }

            @Override
            protected void hookOnCancel() {
                //super.hookOnCancel();
                log.info("Inside onCancel");
            }
        });
    }

    @Test
    public void testBackPressure1() throws InterruptedException {
        var numbersFlux = Flux.range(1, 100).log();

        CountDownLatch latch = new CountDownLatch(1);
        numbersFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
                //super.hookOnNext(value);
                log.info("hookOnNext :{}", value);
                if(value % 2 == 0 || value < 50){
                    request(2);
                }else{
                    cancel();
                }
            }

            @Override
            protected void hookOnComplete() {
                //super.hookOnComplete();
            }

            @Override
            protected void hookOnCancel() {
                //super.hookOnCancel();
                log.info("Inside onCancel");
                latch.countDown();
            }
        });
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

    @Test
    public void testBackPressure_drop() throws InterruptedException {
        var numbersFlux = Flux.range(1, 100).log();

        CountDownLatch latch = new CountDownLatch(1);

        numbersFlux
                .onBackpressureDrop(item -> {
                    log.info("Dropped item :{}", item);
                })
                .subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
                //super.hookOnNext(value);
                log.info("hookOnNext :{}", value);
                if(value == 2){
                    hookOnCancel();
                }
            }

            @Override
            protected void hookOnComplete() {
                //super.hookOnComplete();
            }

            @Override
            protected void hookOnCancel() {
                //super.hookOnCancel();
                log.info("Inside onCancel");
                latch.countDown();
            }
        });
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

    @Test
    public void testBackPressure_buffer() throws InterruptedException {
        var numbersFlux = Flux.range(1, 100).log();

        CountDownLatch latch = new CountDownLatch(1);

        numbersFlux
                .onBackpressureBuffer(10, item -> {
                    log.info("last buffered element :{}", item);
                })
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        subscription.request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        //super.hookOnNext(value);
                        log.info("hookOnNext :{}", value);
                        if(value < 50){
                            request(1);
                        } else {
                            hookOnCancel();
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
                        //super.hookOnComplete();
                    }

                    @Override
                    protected void hookOnCancel() {
                        //super.hookOnCancel();
                        log.info("Inside onCancel");
                        latch.countDown();
                    }
                });
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }

    @Test
    public void testBackPressure_error() throws InterruptedException {
        var numbersFlux = Flux.range(1, 100).log();

        CountDownLatch latch = new CountDownLatch(1);

        numbersFlux
                .onBackpressureError()
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        subscription.request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        //super.hookOnNext(value);
                        log.info("hookOnNext :{}", value);
                        if(value < 50){
                            request(1);
                        } else {
                            hookOnCancel();
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
                        //super.hookOnComplete();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        //super.hookOnError(throwable);
                        log.error("Error is {}", throwable);
                    }

                    @Override
                    protected void hookOnCancel() {
                        //super.hookOnCancel();
                        log.info("Inside onCancel");
                        latch.countDown();
                    }
                });
        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }
}
