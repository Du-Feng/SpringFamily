# mybatis

1. Spring
![Spring Start](assets/images/spring.initializr.png)

2. pom.xml

        <dependency>
            <groupId>org.joda</groupId>
            <artifactId>joda-money</artifactId>
            <version>1.0.1</version>
        </dependency>
        <dependency>
            <groupId>org.jadira.usertype</groupId>
            <artifactId>usertype.core</artifactId>
            <version>7.0.0.CR1</version>
        </dependency>
        
3. Maven Clean and Install
![Maven Clean Install](assets/images/run.maven.png)

4. Maven Reload Project
![Maven Reload Project](assets/images/maven.reload.project.png)

5. application.properties中指定handler

        mybatis.type-handlers-package=com.example.mybatis.handler
    
6. 在 MybatisApplication 的MapperScan注解中指定 mapper

        @MapperScan("com.example.mybatis.mapper")
        public class MybatisApplication implements ApplicationRunner {