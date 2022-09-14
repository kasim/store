# Application Summary

## Quick Start

### Prerequisites

* [Java 11 JDK or higher](https://openjdk.java.net/install/)

### Start Service Steps
1. On MacOS/Linux `./mvnw clean spring-boot:run` Or On Windows `.\mvnw.cmd clean spring-boot:run`
2. On MacOS/Linux `./mvnw clean test` Or On Windows `.\mvnw.cmd clean test`

## Endpoints
### Administrator Controller Endpoints
* [POST] http://localhost:8080/admin/create
* [DELETE] http://localhost:8080/admin/remove?id=<productId>
### Customer Controller Endpoints
* [POST] http://localhost:8080/customer/add
* [DELETE] http://localhost:8080/customer/remove?userId=<userId>&productId=<productId>&quantity=<quantity>
* [GET] http://localhost:8080/customer/checkout?userId=<userId>

## Assumptions
* Assumed every single user calls APIs from single device and single thread.
