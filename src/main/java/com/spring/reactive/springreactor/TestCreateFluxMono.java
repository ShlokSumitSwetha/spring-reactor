package com.spring.reactive.springreactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TestCreateFluxMono {
    public static void main(String[] args) throws InterruptedException {

        //===============================================================================
       /* Flux.create(fluxSink -> {
            fluxSink.onRequest(requested -> {
                // Push values into the FluxSink
                fluxSink.next("Value 1");
                fluxSink.next("Value 2");
                fluxSink.next("Value 3");
                fluxSink.complete();
            });
        }).subscribe(System.out::println);*/

        Flux<String> flux = Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                // Simulating asynchronous emission using a scheduler
                Schedulers.single().schedule(() -> sink.next("Element " + finalI));
            }
            sink.complete();
        });

        flux.subscribe(System.out::println);

        // Delay to allow the emissions to complete
        Thread.sleep(1000);

        // Keep the main thread alive for a while to observe the output
        Thread.sleep(2000);

        }
}
