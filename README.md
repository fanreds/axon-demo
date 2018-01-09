**AXON DEMO PROJECT**

run `gradlew bootrun`

Story

Firstly you need to make transfer money to account, and after that, you can create orders

create new order
`curl.exe -i -X POST -H "Content-Type:application/json" -d '{"orderId":"1","accountId":"1","price":"100"}' localhost:8080/api/order`

case create new order
1. order created
2. order payment accepted -> can exceed limit balance exception
3. decrease account balance 
4. order canceled or order approved


do money transfer for account(if account not exists, will be created)

`curl.exe -i -X POST -H "Content-Type:application/json" -d '{"accountId":"1","amount":"100"}' localhost:8080/api/moneyTransfer`
