package com.example.redisrepository;

import com.example.redisrepository.converter.BytesToMoneyConverter;
import com.example.redisrepository.converter.MoneyToBytesConverter;
import com.example.redisrepository.model.Coffee;
import com.example.redisrepository.service.CoffeeService;
import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
@EnableRedisRepositories
public class RedisRepositoryApplication implements ApplicationRunner {
    @Autowired
    private CoffeeService coffeeService;

    public static void main(String[] args) {
        SpringApplication.run(RedisRepositoryApplication.class, args);
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer customizer() {
        return builder -> builder.readFrom(ReadFrom.MASTER_PREFERRED);
    }

    @Bean
    public RedisCustomConversions redisCustomConversions() {
        return new RedisCustomConversions(
                Arrays.asList(new MoneyToBytesConverter(), new BytesToMoneyConverter())
        );
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<Coffee> coffee = coffeeService.findSimpleCoffeeFromCache("mocha");
        log.info("Coffee {}", coffee);

        for (int i = 0; i < 5; i++) {
            coffee = coffeeService.findSimpleCoffeeFromCache("mocha");
        }
        log.info("Value from Redis: {}", coffee);
    }
}
