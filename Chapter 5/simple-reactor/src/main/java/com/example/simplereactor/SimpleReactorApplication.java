package com.example.simplereactor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@SpringBootApplication
public class SimpleReactorApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(SimpleReactorApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        exceptionTestC();
    }

    /**
     * 这是第一个小方法，在主线程中完成。
     *
     * @throws InterruptedException
     */
    private void normalTestA() throws InterruptedException {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request {} number", n))
                .doOnComplete(() -> log.info("Publisher COMPLETE 1"))
                .map(i -> {
                    log.info("Publish {}, {}", Thread.currentThread(), i);
                    return i;
                })
                .doOnComplete(() -> log.info("Publisher COMPLETE 2"))
                .subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber COMPLETE")
                );
        Thread.sleep(2000);
    }

    /**
     * 在 normalTestA 代码的基础上，在线程池中 Publish，在单线程中 subscribe
     *
     * @throws InterruptedException
     */
    private void normalTestB() throws InterruptedException {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request {} number", n))
                .doOnComplete(() -> log.info("Publisher COMPLETE 1"))
                .publishOn(Schedulers.elastic()) // 缓存的线程池，空闲60秒后回收
                .map(i -> {
                    log.info("Publish {}, {}", Thread.currentThread(), i);
                    return i;
                })
                .doOnComplete(() -> log.info("Publisher COMPLETE 2"))
                .subscribeOn(Schedulers.single()) // 使用当前线程，即 elastic 线程。
                .subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber COMPLETE")
                );
        Thread.sleep(2000);
    }

    /**
     * 在 normalTestB 代码的基础上,
     * 修改了map的返回值，当i=3时，会出现异常。
     * 在onErrorReturn中指定了异常时的默认返回值为-1，然后程序结束。
     *
     * @throws InterruptedException
     */
    private void exceptionTestA() throws InterruptedException {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request {} number", n))
                .doOnComplete(() -> log.info("Publisher COMPLETE 1"))
                .publishOn(Schedulers.elastic()) // 缓存的线程池，空闲60秒后回收
                .map(i -> {
                    log.info("Publish {}, {}", Thread.currentThread(), i);
                    return 10 / (i - 3); // 当i为3时，将出现异常
                })
                .doOnComplete(() -> log.info("Publisher COMPLETE 2")) // 因为i=3时，map出现异常，publish 代码提前结束，这行代码将不会执行
                .subscribeOn(Schedulers.single()) // 使用当前线程，即 elastic 线程。
                .onErrorReturn(-1) // 发生错误时，直接使用此默认值-1，然后终止
                .subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber COMPLETE") // subscribe 仍会正常结束
                );
        Thread.sleep(2000);
    }

    /**
     * 在 exceptionTestB 代码的基础上,
     * 不再使用onErrorReturn直接返回一个简单的默认值；
     * 而是使用onErrorResume添加更多处理，先打印出异常信息，然后再返回默认值-1.
     *
     * @throws InterruptedException
     */
    private void exceptionTestB() throws InterruptedException {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request {} number", n))
                .doOnComplete(() -> log.info("Publisher COMPLETE 1"))
                .publishOn(Schedulers.elastic()) // 缓存的线程池，空闲60秒后回收
                .map(i -> {
                    log.info("Publish {}, {}", Thread.currentThread(), i);
                    return 10 / (i - 3); // 当i为3时，将出现异常
                })
                .doOnComplete(() -> log.info("Publisher COMPLETE 2")) // 因为i=3时，map出现异常，publish 代码提前结束，这行代码将不会执行
                .subscribeOn(Schedulers.single()) // 使用当前线程，即 elastic 线程。
                .onErrorResume(e -> { // 当异常出现时，先打印错误信息，再返回默认值-1
                    log.error("Exception {}", e.toString());
                    return Mono.just(-1);
                })
                //.onErrorReturn(-1) // 发生错误时，直接使用此默认值-1，然后终止
                .subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber COMPLETE") // subscribe 仍会正常结束
                );
        Thread.sleep(2000);
    }

    /**
     * 在 exceptionTestB 代码的基础上,不再让 map 出错；
     * Flux生产6个，但是subscribe只消费4个，最终 publish和subscribe都不会正常结束。
     *
     * @throws InterruptedException
     */
    private void backpressureTest() throws InterruptedException {
        Flux.range(1, 6) // 生产6个
                .publishOn(Schedulers.elastic()) // 缓存的线程池，空闲60秒后回收
                .doOnRequest(n -> log.info("Request {} number", n))
                .doOnComplete(() -> log.info("Publisher COMPLETE 1"))
                .map(i -> {
                    log.info("Publish {}, {}", Thread.currentThread(), i);
                    return i;
                })
                .doOnComplete(() -> log.info("Publisher COMPLETE 2")) // 因为生产了6个，但是只subscribe 4个，这行代码将不会执行
                .subscribeOn(Schedulers.single()) // 使用当前线程，即 elastic 线程。
                .onErrorResume(e -> { // 没有异常，此处代码不会执行
                    log.error("Exception {}", e.toString());
                    return Mono.just(-1);
                })
                //.onErrorReturn(-1) // 发生错误时，直接使用此默认值-1，然后终止
                .subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber COMPLETE"), // subscribe 不再正常结束，因为提前结束了
                        s -> s.request(4) // 只 request 4个
                );
        Thread.sleep(2000);
    }

    /**
     * 在 backpressureTest 代码的基础上,这次让 map 出错；
     * Flux生产6个，但是subscribe只消费4个.
     *
     * @throws InterruptedException
     */
    private void exceptionTestC() throws InterruptedException {
        Flux.range(1, 6) // 生产6个
                .doOnRequest(n -> log.info("Request {} number", n))
                .publishOn(Schedulers.elastic()) // 缓存的线程池，空闲60秒后回收
                .doOnComplete(() -> log.info("Publisher COMPLETE 1"))
                .map(i -> {
                    log.info("Publish {}, {}", Thread.currentThread(), i);
                    return 10 / (i - 3);
                })
                .doOnComplete(() -> log.info("Publisher COMPLETE 2")) // 因为生产了6个，但是只subscribe 4个，这行代码将不会执行
                .subscribeOn(Schedulers.single()) // 使用当前线程，即 elastic 线程。
                .onErrorResume(e -> { // 没有异常，此处代码不会执行
                    log.error("Exception {}", e.toString());
                    return Mono.just(-1);
                })
                //.onErrorReturn(-1) // 发生错误时，直接使用此默认值-1，然后终止
                .subscribe(i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber COMPLETE"), // subscribe 仍会正常结束
                        s -> s.request(4) // 只 request 4个
                );
        Thread.sleep(2000);
    }
}
