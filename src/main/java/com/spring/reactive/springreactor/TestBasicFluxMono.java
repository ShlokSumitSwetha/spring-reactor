package com.spring.reactive.springreactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TestBasicFluxMono {
    public static void main(String[] args) {

        //===============================================================================
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");

        // from the collection list create flux
        List<String> iterable = Arrays.asList("foo", "bar", "foobar");
        Flux<String> seq2 = Flux.fromIterable(iterable);

        Flux<Integer> flux = Flux.range(1, 10)
                .filter(i -> i % 2 == 0);

        flux.subscribe(System.out::println);

        Flux<Integer> reactor = Flux.range(1, 10)
                .map(i -> i * 2)
                .filter(i -> i % 3 == 0);

        reactor.subscribe(System.out::println);

        Flux.range(1, 5)
                .flatMap(num -> Flux.just(num * 2, num * 3))
                .subscribe(System.out::println);



        // ===============================================================================

        Mono<String> noData = Mono.empty();
        Mono<String> data = Mono.just("foo");

        Mono<Integer> monoreactor = Mono.just(5)
                .map(i -> i * 3)
                .flatMap(i -> Mono.just(i * 2));

        monoreactor.subscribe(System.out::println);


        Flux<String> fluxCreate = Flux.create(sink -> {
            for (int i = 0; i <= 5; i++) {
                sink.next("Element " + i);
            }
        });
        fluxCreate.subscribe(System.out::println);



        Optional<String> optionalValue = Optional.of("Hello");

        Mono<String> mono = Mono.justOrEmpty(optionalValue)
                .map(String::toUpperCase)
                .defaultIfEmpty("No value present");

        mono.subscribe(System.out::println);

    }
}
