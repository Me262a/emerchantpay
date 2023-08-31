# Payment System Task
Task description: [`Payment System Task.pdf`](./Payment%20System%20Task.pdf)

## Components
- Merchant Microservice to manage Users
- Transaction Microservice to manage Transactions
- Web UI
- Postgres 15 as DB
- Keycloak 22 for handling authorization
- Kafka 3.5 for messaging

## Tech stack
- Java 17, spring boot 3.1.3, gradle, checkstyle
- JUnit 5 and cucumber
- React 18, react router 6, redux, axios, bootstrap, vite, eslint
- Docker and docker-compose
- Tools: Idea Ultimate, Docker Desktop, Keycloak UI, Kafka UI, Swagger.

## How to launch:
### First method. Stage solution. One-click launch, full dockerization.
Run docker compose and ensure that all containers are running:
- docker-compose.exe -f docker-compose-stage.yml -p emerchantpay up -d  
In Intellij Idea you can right-click on the compose file and run.

### Second method. Dev solution. Microservices are not dockerized.
1. Run Postgres, Keycloak, Kafka using docker-compose:
- docker-compose.exe -f docker-compose-dev.yml -p emerchantpay up -d

2. Run Microservices as spring boot application
- com.vvkozlov.emerchantpay.merchant.MerchantApplication (Merchant folder)
- com.vvkozlov.emerchantpay.transaction.TransactionApplication (Transaction folder)

3. Run Web UI
Tested with yarn package manager. But npm should also work.
- execute "yarn start" or "npm start" from web-ui folder

## How to use
1. Ensure that all applications are running
2. Open http://localhost:8080/  
   Login as emp_admin / emp_admin_password
3. Import Merchants
4. You can check other features: View, Edit, Delete a Merchant. You can also import other admins.  
   Users are imported both to the merchant service DB and the keycloak.
5. After importing merchants, you can use the Transaction UI.
6. Log out from merchant ui and press login button. Enter credentials:  
   Name = <merchant_email>  
   password = password  
7. When Merchant is logged in, he can see a list of transactions. Initially it is empty.
8. Click "New Transaction" at the top menu. Start with Authorize transaction.

Notes:
1. Merchants are not intended to use the merchant management service.  
Admins are not intended to use the transaction management service.
2. Each transaction belongs to a merchant that creates it (the "sub" JWT claim is scanned).
3. For Merchants, the main id used across microservices is located in the "sub" claim of their JWT  
This id is generated in keycloak when a Merchant is created there.
This id is declared as String (not UUID) to avoid tight coupling with implementation.
4. Admins are not stored in Merchant DB, they are stored in keycloak and its DB.  
Default password for imported Admins is "password".
5. Kafka handles the following Events:  
5.1. Amount is charged (transaction topic)  
5.2. Amount is refunded (transaction topic)  
5.3. Merchant Status is changed (merchant topic)  
You can check these events in backend service container logs (consume) or Kafka UI

Useful links:
1. Keycloak Admin UI:  
   http://localhost:5451/admin/master/console/  
   dev_kc_admin  
   dev_kc_password  
   (or use "stage_" prefix for stage)  

- You can switch to emerchantpay realm in top left corner.

2. Kafka UI  
   http://localhost:5453/ui/  
   No Authorization

3. Postgres:  
   jdbc:postgresql://localhost:5450/dev_emerchantpay  
   dev_db_admin  
   dev_db_password  
   (or "stage_")  

4. Merchant service Swagger UI:  
   http://localhost:8081/swagger-ui/index.html#

5. Transaction Microservice Swagger UI:  
   http://localhost:8082/swagger-ui/index.html#/

## Tests
All tests are checked and working.  
For keycloak test, run docker-compose-dev to get dev keycloak instance  
In IntelliJ idea, tests can be run by clicking on test folder and choosing "Run tests"

## Swagger Auth
1. Get Merchant Admin JWT from keycloak:  
   curl --location 'http://localhost:5451/realms/emerchantpay/protocol/openid-connect/token' \  
   --header 'content-type: application/x-www-form-urlencoded' \  
   --data-urlencode 'client_id=emerchantpay-test-client' \  
   --data-urlencode 'client_secret=emerchantpay_test_client_secret' \  
   --data-urlencode 'username=emp_admin' \  
   --data-urlencode 'password=emp_admin_password' \  
   --data-urlencode 'grant_type=password'


2. Get Merchant JWT (import merchants first):  
   curl --location 'http://localhost:5451/realms/emerchantpay/protocol/openid-connect/token' \  
   --header 'content-type: application/x-www-form-urlencoded' \  
   --data-urlencode 'client_id=emerchantpay-test-client' \  
   --data-urlencode 'client_secret=emerchantpay_test_client_secret' \  
   --data-urlencode 'username=vasil@store.com' \  
   --data-urlencode 'password=password' \  
   --data-urlencode 'grant_type=password'