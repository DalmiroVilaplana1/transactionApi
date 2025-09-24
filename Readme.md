
-> Decisiones de diseño <-

Luego de un analisis inicial, se decidio implementar algunas interfaces que ayudan a cumplir los principios SOLID. Esto por ende, va a hacer que la api sea mas escalable a futuro y que se puedan agregar nuevos tipos de eventos, transacciones o validaciones sin tener que hacer grandes cambios en el codigo. A continuacion quedan listadas las interfaces implementadas, con su proposito, su implementacion actual y la ventaja que nos da a nivel escalabilidad.

- PaymentHandler

Propósito: encapsular la lógica específica de cada método de pago al crear la transacción y sus “details”.

Escalabilidad: agrega nuevos métodos (CARD, WALLET, etc) sin tocar el resto (Open/Closed principle).

Implementación actual: BankTransferHandler -> crea Transaction (PENDING) y BankTransferDetails.

- PaymentHandlerRegistry

Propósito: resolver dinámicamente qué PaymentHandler usar según PaymentMethod.

Escalabilidad: evita switch/if global; descubre handlers por  DI (Inyeccion de dependencias).

Implementación actual: InMemoryPaymentHandlerRegistry (mapa {PaymentMethod → Handler}).

- TransactionService

Propósito: fachada de casos de uso (crear, obtener, listar).

Escalabilidad: orquesta DTO→Command, handler, eventos, async y mapping a respuesta sin acoplar controladores con dominio.

Implementación actual: TransactionServiceImpl.

- TransactionStatusResolver

Propósito: determinar el estado final (APPROVED/REJECTED).

Escalabilidad: fácil reemplazo por reglas reales, antifraude, proveedores externos.

Implementación actual: RandomStatusResolver. Se determino este resolver por la consigna del trabqajo, pero si a futuro se desea cambiar, solo hay que implementar un Resolver que extienda de TransactionStatusResolver y utilizarla.

- DomainEventPublisher

Propósito: publicar eventos de dominio sin acoplar a un transporte (SSE).

Escalabilidad: podés cambiar o agregar canales sin tocar negocio.

Implementación actual: SseDomainEventPublisher.