# E-Commerce Automation Suite — DemoBlaze

### End-to-End Test Automation with Java 21 + Selenium WebDriver 4.40 + TestNG + Allure + ExtentReports

[![CI/CD Pipeline](https://github.com/rpabloesco/qa-ecommerce-automation-selenium/actions/workflows/ci.yml/badge.svg)](https://github.com/rpabloesco/qa-ecommerce-automation-selenium/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-21-orange?logo=oracle)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.40-green?logo=selenium)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.12-red)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)

---

## Description

End-to-end test automation framework built to validate the main user flows of the **[DemoBlaze](https://www.demoblaze.com/)** e-commerce site.

Designed as a professional QA Automation portfolio project, it demonstrates senior-level patterns and practices: Page Object Model, explicit waits, test isolation, data-driven testing, and dual reporting.

---

## Key Features

- **Page Object Model (POM)** — Clean separation between UI logic and test logic
- **Explicit Waits only** — Zero `Thread.sleep()` or implicit waits; all synchronization uses `WebDriverWait` + `ExpectedConditions`
- **Dual Reporting** — Allure Reports (interactive) + ExtentReports (standalone HTML) generated automatically
- **Data-Driven Testing** — Test data externalized in JSON files, read with Jackson ObjectMapper
- **Test Isolation** — `clearCart()` via `localStorage.clear()` in `@BeforeMethod` guarantees a clean state per test
- **Separate Suites** — `testng-smoke.xml` and `testng-regression.xml` allow scope-based execution
- **CI/CD with GitHub Actions** — Automated pipeline on push, PR, and weekly schedule
- **WebDriverManager** — No manual driver configuration required

---

## Test Coverage

| Module | Tests | Groups |
|--------|-------|--------|
| Login | Successful login, failed login, logout, home page load | `smoke` `regression` `login` `negative` |
| Sign Up | Successful registration, duplicate user, empty fields | `smoke` `signup` `negative` |
| Navigation | Home load, categories (Phones/Laptops/Monitors), carousel, product detail | `smoke` `regression` `categories` `carousel` `products` |
| Cart | Add product, verify in cart, multiple products, total calculation, delete, persistence | `smoke` `regression` `cart` |
| Checkout | Full flow, modal total, data-driven by customer, multi-product | `smoke` `regression` `checkout` |

**Total: 45 automated tests** — 23 smoke / 45 regression

---

## Tech Stack

```
Java 21
├── Selenium WebDriver 4.40.0
├── TestNG 7.12.0
├── Maven 3.9
├── WebDriverManager 5.9.2
├── Allure TestNG 2.29.0
├── ExtentReports 5.1.1
├── Jackson Databind 2.18.2
└── GitHub Actions CI/CD
```

---

## Prerequisites

- Java JDK 21+ ([Download](https://adoptium.net/))
- Maven 3.6+ ([Download](https://maven.apache.org/download.cgi))
- Google Chrome (latest version)
- Git

### Verify installation

```bash
java -version   # must show 21+
mvn -version
git --version
```

---

## Installation & Execution

### Clone the repository

```bash
git clone https://github.com/rpabloesco/qa-ecommerce-automation-selenium.git
cd qa-ecommerce-automation-selenium
```

### Install dependencies

```bash
mvn clean install -DskipTests
```

### Run test suites

```bash
# Full suite (smoke + regression) — 45 tests
mvn test "-Dsurefire.suiteXmlFiles=src/test/resources/testng.xml"

# Smoke only — 23 tests (~2.5 min)
mvn test "-Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml"

# Full regression suite — 45 tests (~5 min)
mvn test "-Dsurefire.suiteXmlFiles=src/test/resources/testng-regression.xml"
```

### View reports

```bash
# Allure Report (interactive)
mvn allure:serve

# ExtentReport (auto-generated HTML in test-output/)
start test-output/ExtentReport_<date>.html

# Surefire Report
start target/surefire-reports/index.html
```

---

## Project Architecture

```
qa-ecommerce-automation-selenium/
│
├── .github/workflows/
│   └── ci.yml                          # CI/CD pipeline
│
├── src/
│   ├── main/java/com/raulescobar/
│   │   ├── core/
│   │   │   └── BasePage.java           # POM base class (waits, clicks, alerts)
│   │   ├── driver/
│   │   │   └── DriverFactory.java      # ThreadLocal<WebDriver> for safe execution
│   │   ├── config/
│   │   │   └── ConfigReader.java       # Reads environments/*.properties
│   │   ├── listeners/
│   │   │   ├── AllureListener.java     # Auto-screenshot on failure → Allure
│   │   │   └── ExtentReportListener.java
│   │   ├── pages/
│   │   │   ├── HomePage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── SignUpPage.java
│   │   │   ├── ProductDetailPage.java
│   │   │   ├── CartPage.java
│   │   │   └── CheckoutPage.java
│   │   └── utils/
│   │       ├── TestDataReader.java     # JSON helper with Jackson
│   │       └── WaitHelper.java
│   │
│   ├── test/java/com/raulescobar/
│   │   └── tests/
│   │       ├── base/
│   │       │   └── BaseTest.java       # setUp/tearDown + clearCart()
│   │       └── smoke/
│   │           ├── LoginTest.java
│   │           ├── SignUpTest.java
│   │           ├── NavigationTest.java
│   │           ├── CartTest.java
│   │           └── CheckOutTest.java
│   │
│   ├── main/resources/
│   │   └── environments/
│   │       ├── dev.properties
│   │       └── qa.properties
│   │
│   └── test/resources/
│       ├── testng.xml                  # Full suite
│       ├── testng-smoke.xml            # Smoke group only
│       ├── testng-regression.xml       # Smoke + regression groups
│       └── testdata/
│           ├── navigation-testdata.json
│           ├── cart-testdata.json
│           └── checkout-testdata.json
│
├── pom.xml
├── .gitignore
└── README.md
```

---

## CI/CD Pipeline

The pipeline runs automatically on:

- Every push to `master` or `develop`
- Pull Requests targeting `master`
- Every Monday at 9:00 AM UTC (weekly schedule)
- Manual trigger (`workflow_dispatch`)

**View history:** [GitHub Actions](https://github.com/rpabloesco/qa-ecommerce-automation-selenium/actions)

**Average execution time:** ~5 minutes (45 tests)

---

## Design Patterns & Key Decisions

| Pattern / Decision | Implementation |
|--------------------|---------------|
| **Page Object Model** | One class per page, extends `BasePage` with `PageFactory.initElements` |
| **Explicit Waits** | `WebDriverWait` + `ExpectedConditions` on every interaction; zero `Thread.sleep()` |
| **ThreadLocal Driver** | `DriverFactory` uses `ThreadLocal<WebDriver>` — ready for parallel execution |
| **AJAX Detection** | `stalenessOf()` detects DOM replacement after category filter clicks |
| **Test Isolation** | `localStorage.clear()` in `@BeforeMethod` for cart and checkout tests |
| **Data-Driven** | `@DataProvider` + JSON files read with Jackson |
| **Dual Reporting** | `AllureListener` + `ExtentReportListener` registered in `testng.xml` |

---

## Author

**Pablo Escobar**

- GitHub: [@rpabloesco](https://github.com/rpabloesco)
- LinkedIn: [https://www.linkedin.com/in/raul-pablo-escobar-montalvo-884269147/]
- Email: rpablesmon@gmail.com
