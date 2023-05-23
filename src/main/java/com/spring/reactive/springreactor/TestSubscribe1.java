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

public class TestSubscribe1 {
    public static void main(String[] args) {

        //================ Subscribe  ==========================================================

        Flux<Integer> ints = Flux.range(1, 3);
        ints.log().subscribe(i -> System.out.println(i));

        // --------------------------------------------------------------------

        Flux<Integer> ints2 = Flux.range(1, 4)
                .map(i -> {
                    throw new RuntimeException("Got to 4");
                });
        ints2.log().subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error));

        // --------------------------------------------------------------------

        Flux<Integer> ints3 = Flux.range(1, 4);
        ints3.log().subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"));
        // --------------------------------------------------------------------

        Flux.range(1, 10).log()
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

    }
}
