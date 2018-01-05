**AXON DEMO PROJECT**

run `gradlew bootrun`

create new product
`curl.exe -i -X POST -H "Content-Type:application/json" -d '{"orderId":"1","accountId":"1","price":"100"}' localhost:8080/api/order`
