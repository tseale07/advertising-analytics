# Advertising Analytics Backend

## Main Technologies
- java 1.8
- maven
- spring
- postgres
- hibernate

## Using the Application
### Set up postgres
- service postgresql start
- psql postgres
  - CREATE ROLE root WITH LOGIN PASSWORD 'root';
  - CREATE DATABASE advertising_analytics;
  - GRANT ALL PRIVILEGES ON DATABASE advertising_analytics TO root;
  - \q

### run program
mvn spring-boot:run

### Test API Calls
- https://www.getpostman.com/collections/27cffc83e774ef3976d3
  - Test Data for these calls are in the TestData directory
  - relative path must be used in the API call
    - TestData/...

### view the database
- psql -d advertising_analytics -U root
- Tables
  - product
  - product_data
  - provider
  
### Java Tests
- run on the same database as the app at the moment
- Drop the database and recreate it to start fresh
  - DROP DATABASE advertising_analytics;
  - CREATE DATABASE advertising_analytics;
  - GRANT ALL PRIVILEGES ON DATABASE advertising_analytics TO root;
  - \q