# Learn mongo in Docker

1. 本示例所依赖的 mongo db 运行在 docker上，请按照如下步骤配置 docker
- 获取 mongo 镜像

        docker pull mongo

- 运行MongoDB 镜像

        docker run --name mongo -p 27017:27017 -v ~/docker-data/mongo:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=admin -d mongo
      
- 运行docker container shell
- 创建库

        use springbucks;
        
- 创建用户

        db.createUser(
            {
	            user: "springbucks",
	        	pwd: "springbucks",
	        	roles: [
	    	        { role: "readWrite", db: "springbucks" }
	    	    ]
	        }
        )
        
2. Start spring io
![Start Spring io](assets/images/spring.initializr.png)

3. pom.xml

		<dependency>
			<groupId>org.joda</groupId>
			<artifactId>joda-money</artifactId>
			<version>1.0.1</version>
		</dependency>

4. Maven clean and install
![Maven Clean and Install](assets/images/run.maven.png)

5. Maven reload project
![Maven Reload Project](assets/images/maven.reload.project.png)

6. 在 application.properties 中添加如下配置：

        spring.data.mongodb.uri=mongodb://springbucks:springbucks@localhost:27017/springbucks


