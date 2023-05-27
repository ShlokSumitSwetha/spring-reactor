package com.spring.reactive.springreactor;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

public class TestGenerate {
    public static void main(String[] args) {

      //====================Generate ==============================================================

        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3*state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });


        flux.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error));

        Flux<Long> flux2 = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next(i);
                    if (i == 10) sink.complete();
                    return state;
                });

        flux2.delayElements(Duration.ofMillis(100));

        //flux2.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error));

        Flux<Long> flux3 =  Flux.interval(Duration.ofMillis(100))
                .take(10);
        flux3.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error));

    }
}
