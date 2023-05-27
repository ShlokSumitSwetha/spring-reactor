package com.spring.reactive.springreactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestBasicMono {
    public static void main(String[] args) {

        Mono<Object> noData = Mono.empty().log();
        noData.subscribe();

        Mono<String> data = Mono.just("foo").log();
        data.subscribe(System.out::println);

        Mono<Integer> monoreactor = Mono.just(5).log()
                .map(i -> i * 3)
                .flatMap(i -> Mono.just(i * 2));

        monoreactor.subscribe(System.out::println);

        Optional<String> optionalValue = Optional.of("Hello");

        Mono<String> mono = Mono.justOrEmpty(optionalValue).log()
                .map(String::toUpperCase)
                .defaultIfEmpty("No value present");

        mono.subscribe(System.out::println);

       /* Mono<String> monoError = Mono.just("sumith")
                .map(s->{
                    throw new RuntimeException(("Testing error"));
                });

        monoError.subscribe(System.out::print,Throwable::printStackTrace);
*/
        Mono<Object> monoTest = Mono.just("sumith test")
                .log()
                .map(String::toUpperCase)
                .doOnSubscribe(subscription -> mono.log())
                .doOnRequest(System.out::println)
                .doOnNext(System.out::println)
                .flatMap(s -> Mono.empty())
                .doOnNext(System.out::println)
                .doOnSuccess(s->{});

        monoTest.subscribe(System.out::println);


        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Creating a CompletableFuture representing a future result
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            // Simulating a long-running task
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result from CompletableFuture";
        }, executorService);

        // Converting CompletableFuture to Mono
        Mono<String> monoFromFuture = Mono.fromFuture(completableFuture);

        monoFromFuture.log().subscribe(
                result -> System.out.println("Result: " + result),
                error -> System.err.println("Error: " + error.getMessage()),
                () -> System.out.println("Operation completed successfully!")
        );

        // Shutdown executor service
        executorService.shutdown();



    }
}
