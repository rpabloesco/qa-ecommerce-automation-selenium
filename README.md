# E-Commerce Automation Suite — DemoBlaze

### Automatización E2E con Java 21 + Selenium WebDriver 4.40 + TestNG + Allure + ExtentReports

[![CI/CD Pipeline](https://github.com/rpabloesco/qa-ecommerce-automation-selenium/actions/workflows/ci.yml/badge.svg)](https://github.com/rpabloesco/qa-ecommerce-automation-selenium/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-21-orange?logo=oracle)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.40-green?logo=selenium)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.12-red)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)

---

## Descripción

Framework de automatización de pruebas E2E desarrollado para validar los flujos principales del sitio e-commerce **[DemoBlaze](https://www.demoblaze.com/)**.

Diseñado como portfolio profesional de QA Automation, demuestra el uso de patrones y prácticas de nivel senior: Page Object Model, explicit waits, aislamiento de tests, data-driven testing y dual reporting.

---

## Características principales

- **Page Object Model (POM)** — Separación total entre lógica de UI y lógica de test
- **Explicit Waits únicamente** — Sin `Thread.sleep()` ni implicit waits; todas las esperas son semánticas con `WebDriverWait` + `ExpectedConditions`
- **Dual Reporting** — Allure Reports (interactivo) + ExtentReports (HTML standalone) generados automáticamente
- **Data-Driven Testing** — Datos de test externalizados en JSON, leídos con Jackson ObjectMapper
- **Aislamiento de tests** — `clearCart()` vía `localStorage.clear()` en `@BeforeMethod` garantiza estado limpio por test
- **Suites separadas** — `testng-smoke.xml` y `testng-regression.xml` permiten ejecución por alcance
- **CI/CD con GitHub Actions** — Pipeline automático en push, PR y schedule semanal
- **WebDriverManager** — Sin configuración manual de drivers

---

## Alcance de Pruebas

| Módulo | Tests | Grupos |
|--------|-------|--------|
| Login | Login exitoso, fallido, logout, carga de home | `smoke` `regression` `login` `negative` |
| Sign Up | Registro exitoso, usuario duplicado, campos vacíos | `smoke` `signup` `negative` |
| Navegación | Home load, categorías (Phones/Laptops/Monitors), carrusel, detalle de producto | `smoke` `regression` `categories` `carousel` `products` |
| Carrito | Agregar producto, verificar en carrito, múltiples productos, total, eliminar, persistencia | `smoke` `regression` `cart` |
| Checkout | Flujo completo, total en modal, data-driven por cliente, multi-producto | `smoke` `regression` `checkout` |

**Total: 45 tests automatizados** — 23 smoke / 45 regression

---

## Stack Tecnológico

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

## Pre-requisitos

- Java JDK 21+ ([Descargar](https://adoptium.net/))
- Maven 3.6+ ([Descargar](https://maven.apache.org/download.cgi))
- Google Chrome (versión reciente)
- Git

### Verificar instalación

```bash
java -version   # debe mostrar 21+
mvn -version
git --version
```

---

## Instalación y Ejecución

### Clonar repositorio

```bash
git clone https://github.com/rpabloesco/qa-ecommerce-automation-selenium.git
cd qa-ecommerce-automation-selenium
```

### Instalar dependencias

```bash
mvn clean install -DskipTests
```

### Ejecutar suites

```bash
# Suite completa (smoke + regression) — 45 tests
mvn test "-Dsurefire.suiteXmlFiles=src/test/resources/testng.xml"

# Solo smoke — 23 tests (~2.5 min)
mvn test "-Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml"

# Suite de regression completa — 45 tests (~5 min)
mvn test "-Dsurefire.suiteXmlFiles=src/test/resources/testng-regression.xml"
```

### Ver reportes

```bash
# Allure Report (interactivo)
mvn allure:serve

# ExtentReport (HTML generado automáticamente en test-output/)
start test-output/ExtentReport_<fecha>.html

# Surefire Report
start target/surefire-reports/index.html
```

---

## Arquitectura del Proyecto

```
qa-ecommerce-automation-selenium/
│
├── .github/workflows/
│   └── ci.yml                          # Pipeline CI/CD
│
├── src/
│   ├── main/java/com/raulescobar/
│   │   ├── core/
│   │   │   └── BasePage.java           # Clase base de POMs (waits, clicks, etc.)
│   │   ├── driver/
│   │   │   └── DriverFactory.java      # ThreadLocal<WebDriver> para ejecución segura
│   │   ├── config/
│   │   │   └── ConfigReader.java       # Lectura de environments/*.properties
│   │   ├── listeners/
│   │   │   ├── AllureListener.java     # Screenshots en fallo → Allure
│   │   │   └── ExtentReportListener.java
│   │   ├── pages/
│   │   │   ├── HomePage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── SignUpPage.java
│   │   │   ├── ProductDetailPage.java
│   │   │   ├── CartPage.java
│   │   │   └── CheckoutPage.java
│   │   └── utils/
│   │       ├── TestDataReader.java     # Helper para leer JSON con Jackson
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
│       ├── testng.xml                  # Suite completa
│       ├── testng-smoke.xml            # Solo grupo smoke
│       ├── testng-regression.xml       # Smoke + regression
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

El pipeline se ejecuta automáticamente en:

- Cada push a `master` o `develop`
- Pull Requests hacia `master`
- Lunes a las 9:00 AM UTC (schedule semanal)
- Ejecución manual (`workflow_dispatch`)

**Ver historial:** [GitHub Actions](https://github.com/rpabloesco/qa-ecommerce-automation-selenium/actions)

**Tiempo promedio de ejecución:** ~5 minutos (45 tests)

---

## Patrones y Decisiones de Diseño

| Patrón | Implementación |
|--------|---------------|
| **Page Object Model** | Una clase por página, hereda de `BasePage` con `PageFactory.initElements` |
| **Explicit Waits** | `WebDriverWait` + `ExpectedConditions` en cada interacción; 0 `Thread.sleep()` |
| **ThreadLocal Driver** | `DriverFactory` con `ThreadLocal<WebDriver>` — preparado para ejecución paralela |
| **AJAX Detection** | `stalenessOf()` para detectar reemplazo de DOM en filtros de categoría |
| **Test Isolation** | `localStorage.clear()` en `@BeforeMethod` de tests de carrito y checkout |
| **Data-Driven** | `@DataProvider` + JSON files leídos con Jackson |
| **Dual Reporting** | `AllureListener` + `ExtentReportListener` registrados en `testng.xml` |

---

## Autor

**Pablo Escobar**

- GitHub: [@rpabloesco](https://github.com/rpabloesco)
- LinkedIn: [Tu LinkedIn]
- Email: rpablesmon@gmail.com
