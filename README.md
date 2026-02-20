# E-Commerce Automation Suite — DemoBlaze  
### Automatización E2E con Java + Selenium WebDriver + TestNG (POM)
[![🚀 Test Automation CI/CD](https://github.com/rpabloesco/qa-ecommerce-automation-selenium/actions/workflows/ci.yml/badge.svg)](https://github.com/rpabloesco/qa-ecommerce-automation-selenium/actions/workflows/ci.yml)

<p align="left">
  <img src="https://img.shields.io/badge/Language-Java-blue" alt="Java" />
  <img src="https://img.shields.io/badge/Framework-Selenium%20WebDriver-brightgreen" alt="Selenium" />
  <img src="https://img.shields.io/badge/Test-TestNG-orange" alt="TestNG" />
  <img src="https://img.shields.io/badge/Build-Maven-yellow" alt="Maven" />
</p>

---

## 🚀 Descripción  

Este proyecto contiene un framework de automatización desarrollado para validar los flujos principales del sitio e-commerce **DemoBlaze**.  

Incluye pruebas funcionales de:

- Login  
- Navegación por categorías  
- Selección de productos  
- Carrito  
- Checkout  

Construido con **Selenium WebDriver**, **TestNG**, **Maven** y el patrón **Page Object Model (POM)**, siguiendo buenas prácticas usadas en entornos reales de QA Automation.

---

## 🎯 Objetivos  

- Automatizar los flujos críticos del sitio DemoBlaze.  
- Implementar un framework modular basado en Page Object Model.  
- Facilitar la ejecución por suites mediante TestNG.  
- Generar reportes automáticos (Allure / Extent).  
- Servir como base para integración con pipelines de CI/CD.

---

## 🧪 Alcance de Pruebas Automatizadas  

### 🔹 Login  
- Login exitoso  
- Login fallido  
- Logout  

### 🔹 Navegación de productos  
- Selección de categorías (Phones, Laptops, Monitors)  
- Validación de nombre y precio del producto  

### 🔹 Carrito  
- Agregar productos al carrito  
- Validar alertas del sistema al agregar productos  
- Eliminar productos del carrito  

### 🔹 Checkout  
- Completar formulario de compra  
- Confirmar mensaje de éxito  
- Validar monto total de la compra  

---

## 📁 Arquitectura del Framework  

```text
qa-ecommerce-automation-selenium-java/
src/
  main/
    java/
      com/raulescobar/
        config/
        core/
        driver/
        pages/
        utils/
    resources/
      config.properties
      environments/
        dev.properties
        qa.properties
  test/
    java/
      com/raulescobar/tests/
        base/
        smoke/
        regression/
    resources/
      testng.xml

