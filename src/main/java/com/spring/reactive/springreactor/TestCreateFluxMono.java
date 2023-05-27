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
       Flux.create(fluxSink -> {
            fluxSink.onRequest(requested -> {
                // Push values into the FluxSink
                fluxSink.next("Value 1");
                fluxSink.next("Value 2");
                fluxSink.next("Value 3");
                fluxSink.complete();
            });
        }).subscribe(System.out::println);


       Flux.create(fluxSink -> {
          fluxSink.next("test1");
       }).subscribe(System.out::println);

        }
}
