package com.example.errorcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@RunWith(SpringRunner.class) // this is junit4 runner
@ExtendWith(SpringExtension.class) // this is junit5 runner
@SpringBootTest
public class ErrorcodeApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //@Test(expected=CustomDuplicateKeyException.class) // this is how junit4 assert exception
    @Test
    public void testThrowingCustomExceptionType() {
        Assertions.assertThrows(CustomDuplicateKeyException.class, () -> {
            jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'a')");
            jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'b')");
        });
    }

    @Test
    public void testThrowingCustomExceptionMessage() {
        Exception exception = Assertions.assertThrows(CustomDuplicateKeyException.class, () -> {
            jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'a')");
            jdbcTemplate.execute("INSERT INTO FOO (ID, BAR) VALUES (1, 'b')");
        });
        System.out.println("Error message: " + exception.getMessage());
        Assertions.assertTrue(exception.getMessage().contains("Unique index or primary key violation"));
    }


}
