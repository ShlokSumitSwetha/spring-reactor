package com.spring.reactive.springreactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TestBasicFluxMono {
    public static void main(String[] args) {

        //===============================================================================
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");

        // from the collection list create flux
        List<String> iterable = Arrays.asList("foo", "bar", "foobar");
        Flux<String> seq2 = Flux.fromIterable(iterable);

        // ===============================================================================

        Mono<String> noData = Mono.empty();
        Mono<String> data = Mono.just("foo");

        //================ Subscribe  ==========================================================

        Flux<Integer> numbersFromFiveToSeven = Flux.range(5, 3);

        // --------------------------------------------------------------------

        Flux<Integer> ints = Flux.range(1, 3);
        ints.subscribe(i -> System.out.println(i));

        // --------------------------------------------------------------------

        Flux<Integer> ints2 = Flux.range(1, 4)
                .map(i -> {
                    throw new RuntimeException("Got to 4");
                });
        ints2.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error));

        // --------------------------------------------------------------------

        Flux<Integer> ints3 = Flux.range(1, 4);
        ints3.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"));
        // --------------------------------------------------------------------

        Flux.range(1, 10)
                .doOnRequest(r -> System.out.println("request of " + r))
                .subscribe(new BaseSubscriber<Integer>() {

                    @Override
                    public void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    public void hookOnNext(Integer integer) {
                        System.out.println("Cancelling after having received " + integer);
                        cancel();
                    }
                });

        List<Integer> elements = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .log()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        elements.add(integer);
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });

      //====================Generate ==============================================================

        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3*state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });


        flux.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error));

        Flux<String> flux2 = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3*i);
                    if (i == 10) sink.complete();
                    return state;
                });

        flux2.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error));

    }
}
