# E-Commerce Automation Suite — DemoBlaze

### Automatización E2E con Java + Selenium WebDriver + TestNG (POM)

[![Test Automation CI/CD](https://github.com/rpabloesco/qa-ecommerce-automation-selenium-java/actions/workflows/ci.yml/badge.svg)](https://github.com/rpabloesco/qa-ecommerce-automation-selenium-java/actions)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=oracle)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.18-green?logo=selenium)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.9-red)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apachemaven)](https://maven.apache.org/)

---

## 🚀 Descripción

Framework de automatización de pruebas E2E desarrollado para validar los flujos principales del sitio e-commerce **[DemoBlaze](https://www.demoblaze.com/)**.

### ✨ Características principales

-   **Arquitectura POM** (Page Object Model) - Código mantenible y escalable
-   **CI/CD Integrado** - Pipeline automatizado con GitHub Actions
-   **Cross-Browser Testing** - Soporte para Chrome, Firefox, Edge
-   **Ejecución Paralela** - Tests más rápidos
-   **Screenshots Automáticos** - Captura en fallos
-   **Reportes Detallados** - TestNG y Surefire reports
-   **WebDriverManager** - Sin configuración manual de drivers

---

## 🧪 Alcance de Pruebas

| Módulo | Casos de Prueba | Estado |
|--------|----------------|--------|
| 🔐 Login | Login exitoso, fallido, logout |   Passing |
| 🏪 Navegación | Categorías, detalles de productos |   Passing |
| 🛒 Carrito | Agregar/eliminar, validaciones |   Passing |
| 💳 Checkout | Flujo completo, confirmación |   Passing |

**Total:** 15+ pruebas automatizadas

---

## 🛠️ Stack Tecnológico
Java 17
├── Selenium WebDriver 4.18
├── TestNG 7.9
├── Maven 3.9
├── WebDriverManager 5.6
└── GitHub Actions
---

## 📋 Pre-requisitos

- Java JDK 17+ ([Descargar](https://adoptium.net/))
- Maven 3.6+ ([Descargar](https://maven.apache.org/download.cgi))
- Git

### Verificar instalación
```bash
java -version
mvn -version
git --version
```

---

## 🚀 Instalación y Ejecución

### Clonar repositorio
```bash
git clone https://github.com/rpabloesco/qa-ecommerce-automation-selenium-java.git
cd qa-ecommerce-automation-selenium-java
```

### Instalar dependencias
```bash
mvn clean install -DskipTests
```

### Ejecutar tests
```bash
# Todos los tests
mvn clean test

# Browser específico
mvn test -Dbrowser=chrome

# Modo headless
mvn test -Dheadless=true

# Ejecución paralela
mvn test -DthreadCount=3
```

---

## 📊 Reportes

### Ver reporte de TestNG
```bash
open target/surefire-reports/index.html
```

### Ver reporte de Surefire
```bash
open target/site/surefire-report.html
```

---

## 📁 Arquitectura del Proyecto
qa-ecommerce-automation-selenium-java/
│
├── .github/
│   └── workflows/
│       └── ci.yml                    # Pipeline CI/CD
│
├── src/
│   ├── main/java/
│   │   ├── pages/                    # Page Objects
│   │   │   ├── BasePage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── HomePom.java
│   │   │   ├── ProductPage.java
│   │   │   └── CartPage.java
│   │   │
│   │   └── utils/                    # Utilidades
│   │       ├── DriverFactory.java
│   │       └── ConfigReader.java
│   │
│   └── test/java/
│       └── tests/                    # Clases de Test
│           ├── LoginTests.java
│           ├── AddToCartTests.java
│           └── CheckoutTests.java
│
├── target/                           # Build output
├── pom.xml                          # Dependencias Maven
├── .gitignore
└── README.md
---

## 🔄 CI/CD Pipeline

El pipeline se ejecuta automáticamente en:

-   Cada push a `master`
-   Pull requests
-   Lunes a las 9 AM (ejecución programada)
-   Manual (workflow_dispatch)

**Ver historial:** [GitHub Actions](https://github.com/rpabloesco/qa-ecommerce-automation-selenium-java/actions)

**Tiempo promedio de ejecución:** ~1 minuto

---

## 🏗️ Patrones de Diseño

- **Page Object Model (POM)** - Separación UI/Tests
- **Factory Pattern** - Creación dinámica de WebDrivers
- **Singleton** - Gestión de configuración

---

## 🎯 Objetivos del Proyecto

-   Automatizar flujos críticos de e-commerce
-   Implementar framework modular y escalable
-   Facilitar ejecución por suites con TestNG
-   Generar reportes automáticos
-   Integración con CI/CD para testing continuo

---

## 📈 Mejoras Futuras

- [ ] Integración con Allure Reports
- [ ] Data-driven testing con Excel/JSON
- [ ] Soporte multi-idioma
- [ ] Tests de API
- [ ] Performance testing
- [ ] Docker containerization

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama (`git checkout -b feature/MejoraPruebas`)
3. Commit cambios (`git commit -m 'Add: nuevas pruebas de checkout'`)
4. Push a la rama (`git push origin feature/MejoraPruebas`)
5. Abre un Pull Request

   
---

## 👤 Autor

**Pablo Escobar**

- GitHub: [@rpabloesco](https://github.com/rpabloesco)
- LinkedIn: [Tu LinkedIn]
- Email: tu@email.com

---

## ⭐ Soporte

Si este proyecto te fue útil, considera darle una estrella ⭐

---

<div align="center">

**Desarrollado para la comunidad QA**

</div>
