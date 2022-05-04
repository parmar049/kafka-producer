to start streaming hit URL

localhost:8080/coopX/v1/streamTweets/produceDataStream?ruleFilter=modi

to start Kafka server use command

.\bin\windows\kafka-server-start.bat .\config\server.properties

Improvements :::
    
1. Make rule configurable so that it can be changed dynamically
2. Configure twitter rest API URL in yaml   
3. Add test case to test producer code, consume and test in unit test   
4. Implementation of docker
5. Integration with AWS to get token from AWS parameter store instead of Hardcoding   
6. Build CI/CD pipeline to deploy in cloud
7. Implementation of authentication with Kafka broker