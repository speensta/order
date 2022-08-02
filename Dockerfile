FROM adoptopenjdk/openjdk11:ubi
ADD target/order-1.0.jar order.jar
ENTRYPOINT ["java","-jar","order.jar"]