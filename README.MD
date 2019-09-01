###Required environment:
1. Java 8+
2. Gradle 5+

###Build project:

```
gradle build
```

###Run server
```shell script
build/libs/account-app-0.1-all.jar
```
or
```shell script
java -jar build/libs/account-app-0.1-all.jar
```

####Examples
There are two predefined accounts: 111111(amount:300) and 222222(amount:500)

POST localhost:8080/transaction
```json
{
	"senderAccount": 111111,
	"receiverAccount": 222222,
	"amount": 100.0
}
```