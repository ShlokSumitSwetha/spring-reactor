package com.spring.reactive.springreactor;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class TestColdPublishers {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Cold flux ---- started");
        coldPublisher();
        System.out.println("Cold flux --- done ");
        System.out.println("Hot flux");
        hotPublisher();
    }

    private static void coldPublisher() throws InterruptedException {
        Flux<String> netFlux = Flux.fromStream(TestColdPublishers::getVideo)
                .delayElements(Duration.ofSeconds(2)); // each part will play for 2 seconds

        // First Subscriber
        netFlux.subscribe(part -> System.out.println("Cold Subscriber 1: " + part));

        // wait 5 seconds before next Subscriber joins
        Thread.sleep(5000);

        // Seconds Subscriber
        netFlux.subscribe(part -> System.out.println("Cold Subscriber 2: " + part));

        Thread.sleep(6000);
    }
    private static void hotPublisher() throws InterruptedException {
        Flux<String> netFlux = Flux.fromStream(TestColdPublishers::getVideo)
                .delayElements(Duration.ofSeconds(2))
                .share(); // turn the cold publisher into a hot publisher
        // First Subscriber
        netFlux.subscribe(part -> System.out.println("Hot Subscriber 1: " + part));

        // wait 5 seconds before next Subscriber joins
        Thread.sleep(5000);

        // Seconds Subscriber
        netFlux.subscribe(part -> System.out.println("Hot Subscriber 2: " + part));

        Thread.sleep(6000);
    }

    private static Stream<String> getVideo() {
        System.out.println("Request for the video streaming received.");
        return Stream.of("part 1", "part 2", "part 3", "part 4", "part 5");
    }
}
