# Payment System Task
## Components and tech stack
- Java 17, spring boot 3.1.2, gradle, checkstyle
- Merchant Microservice to manage Users
- Transaction Microservice to manage Transactions
- Postgres 15 as DB
- Keycloak for handling authorization
- Kafka for messaging
- Docker and docker-compose
- IDE: Idea Ultimate
- UI: Docker Desktop, Keycloak UI, Kafka UI, Swagger.

## How to launch:
### First method. Stage solution. (Full dockerization)
Run docker compose and ensure that all containers are running:
- docker-compose.exe -f docker-compose-stage.yml -p emerchantpay up -d

Note: In Intellij Idea you can right-click on the compose file and click run.

### Second method. Dev solution. (Microservices are not dockerized)
Run Postgres, Keycloak, Kafka using docker-compose:
- docker-compose.exe -f docker-compose-dev.yml -p emerchantpay up -d

Run Microservices as spring boot application
- com.vvkozlov.emerchantpay.merchant.MerchantApplication (Merchant folder)
- com.vvkozlov.emerchantpay.transaction.TransactionApplication (Transaction folder)

## How to use
1. Ensure that all applications are running

2. Get Merchant Admin JWT from keycloak:
   curl --location 'http://localhost:5451/realms/emerchantpay/protocol/openid-connect/token' \
   --header 'content-type: application/x-www-form-urlencoded' \
   --data-urlencode 'client_id=emerchantpay-client' \
   --data-urlencode 'client_secret=emerchantpay_client_secret' \
   --data-urlencode 'username=emp_admin' \
   --data-urlencode 'password=emp_admin_password' \
   --data-urlencode 'grant_type=password'

Note: You can copy-paste this to postman or import postman collection from:
https://api.postman.com/collections/20742072-34c5c2b3-0b8b-409a-a121-2fea1abe7c1a?access_key=PMAT-01H8Y8GRME0QYJTDYKHNVDD16S

3. Open the merchant service's Swagger UI:
   http://localhost:8080/swagger-ui/index.html#
   And authorize the admin token copied in step 2 using the UI button

4. Import merchants using this method. They will be imported both to the merchant service and keycloak.
   http://localhost:8080/swagger-ui/index.html#/admin-controller/importMerchantsFromCsv

5. You can now use the merchant controller methods in whatever way you want.
   You can also import other admins

6. Get Merchant JWT:
   curl --location 'http://localhost:5451/realms/emerchantpay/protocol/openid-connect/token' \
   --header 'content-type: application/x-www-form-urlencoded' \
   --data-urlencode 'client_id=emerchantpay-client' \
   --data-urlencode 'client_secret=emerchantpay_client_secret' \
   --data-urlencode 'username=vasil@store.com' \
   --data-urlencode 'password=password' \
   --data-urlencode 'grant_type=password'

Note: You can copy-paste this to postman or import postman collection from:
https://api.postman.com/collections/20742072-34c5c2b3-0b8b-409a-a121-2fea1abe7c1a?access_key=PMAT-01H8Y8GRME0QYJTDYKHNVDD16S

7. Display Swagger UI of Transaction Microservice and authorize Merchant there:
   http://localhost:8081/swagger-ui/index.html#/

Note: Admins are not intended to use the transaction service.
Each transaction belongs to a merchant that creates it (the "sub" JWT claim is scanned).

8. You can start with Authorise transaction method:
   http://localhost:8081/swagger-ui/index.html#/transaction-controller/createAuthorizeTransaction

Note: For Merchants, the main id used across microservices is located in sub claim of their JWT
This id is generated in keycloak when a Merchant  is created there.
Note: Admins are not stored in Merchant DB, they are stored in keycloak and its DB.
Note: Kafka has following Events:
1. Amount is charged (transaction topic)
2. Amount is refunded (transaction topic)
3. Merchant Status is changed (merchant topic)

Useful links:
1. Keycloak Admin UI:
   http://localhost:5451/admin/master/console/
   dev_kc_admin
   dev_kc_password
   (or use "stage_" prefix for stage)

Note: You can switch to emerchantpay realm in top left corner.

2. Kafka UI
   http://localhost:5453/ui/
   No Authorization

3. Postgres:
   jdbc:postgresql://localhost:5450/dev_emerchantpay
   dev_db_admin
   dev_db_password
   (or "stage_")