# Testinium Case Study Automation

## Description  
1. **Web UI tests** targeting **www.beymen.com**, implemented with Selenium 4, JUnit 4 and Log4J 2, following the Page Object Pattern.  
2. **REST API tests** against Trello’s public endpoints, implemented with Rest-Assured and JUnit 4.

All code is written in **Java 17**, structured as a Maven project, and follows OOP principles.  

---

## Prerequisites  
- JDK 17  
- Maven 3.6+  
- Internet access to download dependencies  
- A valid Trello API Key & Token (see **Configuration** below)  
- Excel file (`src/test/resources/beymen-test-data.xlsx`) containing test data for “şort” (cell A1) and “gömlek” (cell B1)

---

## Technologies & Libraries  
- **Java 17**  
- **Maven** (project management & build)  
- **Selenium WebDriver** (UI automation)  
- **JUnit 4** (test framework)  
- **Log4J 2** (logging)  
- **Rest-Assured** (API testing)  
- **Apache POI** (Excel handling)  
- **Page Object Pattern** (test architecture)

---

## Test Scenarios

** Selenium Web Automation (Beymen) **
-Open www.beymen.com and verify homepage load
-Read “şort” from Excel, enter it in search box, then clear it
-Read “gömlek” from Excel, enter it and press Enter
-Select a random product from results
-Write product name & price to a TXT file
-Add the product to cart and verify price consistency
-Increase quantity to 2 and verify
-Remove product and confirm empty cart 


** API Automation (Trello) **
-Create a new Trello board
-Add two cards to the board
-Pick one at random and update it
-Delete both cards
-Delete the board 
