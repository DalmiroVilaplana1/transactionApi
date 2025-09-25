# Solución para el Manejo de Transacciones Financieras de Pago

Proyecto de ejemplo desarrollado en Java 21 + Spring Boot, cuyo objetivo es simular un sistema de pagos en tiempo real con soporte para transferencias bancarias y múltiples monedas.
Incluye endpoints REST para iniciar transacciones, consultar su estado y listar transacciones de un usuario, pensado para ser extensible, escalable y fácil de integrar con frontends en tiempo real.


## Objetivo

- Manejar transacciones financieras de transferencia bancaria.

- Procesamiento en “tiempo real” y consistencia simple (demo) en entorno distribuido.

- Exponer endpoints REST + stream SSE para integrarse fácil con un frontend en tiempo real.


## Clonar el proyecto

El proyecto se encuentra subido en un repositorio publico de GitHub en la rama Main. 

```bash
  git clone https://github.com/DalmiroVilaplana1/transactionApi
```

## Como correr el proyecto (Docker)

Para que cualquiera pueda levantar la API sin instalar Java ni Maven, tome la decision de utilizar Docker. La imagen incluye todo lo necesario para levantar un entorno reproducible y portable. 

Por este motivo es requisito tener docker instalado en el entorno donde se va a ejecutar.

#### Ir al directorio del proyecto

```bash
  cd transactionApi
```

#### Levantar la API

```bash
docker compose up --build
```

#### Bajar la API
```bash
docker compose down
```


## Documentation de la API

Se realizo una documentacion de la API con OpenApi + Swagger UI. Ahi se pueden ver claros ejemplos de lo que recibe y devuelva cada endpoint. 

[Link al Swagger](http://localhost:8080/swagger-ui/index.html#/)

Esta documentacion se puede acceder solo si la API ya se encuentra levantada. 

##  Decisiones de diseño

Luego de un análisis inicial, se decidió implementar algunas interfaces que ayudan a cumplir los principios SOLID.  
Esto hace que la API sea más escalable a futuro, permitiendo agregar nuevos tipos de eventos, transacciones o validaciones sin realizar grandes cambios en el código.

A continuación, se listan las interfaces implementadas con su propósito, implementación actual y la ventaja en escalabilidad:

---

###  `PaymentHandler`
- Propósito: encapsular la lógica específica de cada método de pago al crear la transacción y sus details.  
- Escalabilidad: permite agregar nuevos métodos (`CARD`, `WALLET`, etc.) sin modificar el resto (Open/Closed principle).  
- Implementación actual: `BankTransferHandler` → crea `Transaction (PENDING)` y `BankTransferDetails`.

---

###  `PaymentHandlerRegistry`
- Propósito: resolver dinámicamente qué `PaymentHandler` usar según `PaymentMethod`.  
- Escalabilidad: evita `switch`/`if` global; permite descubrir handlers mediante Inyección de Dependencias (DI).  
- Implementación actual: `InMemoryPaymentHandlerRegistry` (mapa `{PaymentMethod → Handler}`).

---

###  `TransactionService`
- Propósito: actuar como fachada de casos de uso (crear, obtener, listar).  
- Escalabilidad: orquesta `DTO → Command`, handler, eventos, asincronismo y mapping a respuesta sin acoplar controladores con dominio.  
- Implementación actual: `TransactionServiceImpl`.

---

###  `TransactionStatusResolver`
- Propósito: determinar el estado final (`APPROVED` / `REJECTED`).  
- Escalabilidad: fácil reemplazo por reglas reales, antifraude o integración con proveedores externos.  
- Implementación actual: `RandomStatusResolver` (definido por la consigna del trabajo).  
  - A futuro: solo es necesario implementar una clase que extienda de `TransactionStatusResolver` y configurarla.

---

###  `DomainEventPublisher`
- Propósito: publicar eventos de dominio sin acoplar a un transporte (ej. SSE).  
- Escalabilidad: permite cambiar o agregar canales sin modificar la lógica de negocio.  
- Implementación actual: `SseDomainEventPublisher`.

---

## Interfaz grafica

Para la simplicidad del proyecto, inclui un archivo estatico dentro del proyecto. 

Es una pagina sencilla que contiene un textfield donde se debe ingresar el userId al que se le quieren consultar las transacciones. Una vez ingresado el userId se debe tocar en el boton "Conectar". Eso conectara el frontend con el endpoint:
/users/{userId}/transactions/stream 

Si el usuario ya tiene registradas transferencias previas a la conexion, al conectar nos mostrara esas transferencias solo con su estado final -> (APPROVED - REJECTED) pero mientras se mantenga activa la conexion, si se hacen transferencias para ese usuario, se podra ver en tiempo real como "cambia de estado". Pasando asi del estado "PENDING" (cuando recien se creo) al estado APPROVED/REJECTED dependiendo de lo que determino la clase "RandomStatusResolver"



[Link a la interfaz](http://localhost:8080/tester.html)