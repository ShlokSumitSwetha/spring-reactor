package com.spring.reactive.springreactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TestBackpressure {
    public static void main(String[] args) {

        //===============================================================================

       Flux.range(1, Integer.MAX_VALUE)
                .log()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)), 1) // simulate that processing takes time
                .blockLast();

        Flux.range(1, 10)
                .onBackpressureBuffer(7)
                .delayElements(Duration.ofNanos(5))
                .subscribe(System.out::println);

       Flux.range(1, 10)
                .onBackpressureDrop()
                .subscribe(System.out::println);

        Flux.interval(Duration.ofMillis(1))
                .onBackpressureDrop()
                .concatMap(a -> Mono.delay(Duration.ofMillis(1000)).thenReturn(a))
                .doOnNext(a -> System.out.println("Element kept by consumer: " + a))
                .blockLast();

        Flux.range(1,10)
                .log()
                .onBackpressureBuffer(5)
                .subscribe();

        Flux.range(1, 10)
                .log()
                .onBackpressureLatest()
                .count()
                .subscribe(System.out::println);

        Flux.range(1, 10)
                .flatMap(item -> Flux.just(item).delayElements(Duration.ofSeconds(1)))
                .onBackpressureLatest()
                .subscribe(System.out::println);

    }
}
