[Вопросы для собеседования](README.md)

# Основы Web
+ [Postman](#postman)
+ [Сервлеты](#сервлеты)
+ [Rest](#rest)
+ [Http](#http)
+ [Xml Json](#xml-json)
+ [gRPC + Protobuf](#grpc--protobuf)
+ [WebSocket](#websocket)
+ [Exception Handling](#exception-handling)
+ [Spring Web](#spring-web)
+ [Getting data from request](#getting-data-from-request)
+ [ResponseEntity or Object](#responseentity-or-object)
+ [DTO](#dto)
+ [Что такое WWW и как работает интернет?](#что-такое-www-и-как-работает-интернет)
+ [Модель OSI и стек TCP/IP](#модель-osi-и-стек-tcpip)
+ [TCP и UDP: в чём разница?](#tcp-и-udp-в-чём-разница)
+ [HTTP: методы, cookies, сессии](#http-методы-cookies-сессии)
+ [Авторизация и аутентификация](#авторизация-и-аутентификация)
+ [Архитектура веб-приложений](#архитектура-веб-приложений)

### Postman
- Postman – это популярное ПО для тестирования API. Оно помогает разработчикам отправлять HTTP-запросы, просматривать ответы и автоматизировать тестирование. Postman поддерживает различные типы запросов, автоматизацию тестов на JavaScript, организацию запросов в коллекции и использование окружений. В одном из проектов я использовал Postman для тестирования REST API, что помогло выявить и исправить критические ошибки, улучшив стабильность приложения.

[к оглавлению](#Основы-web)
### Сервлеты
- Диспетчер сервлетов это как будто мапа, которая знает какие конкретно методы приложения надо вызывать, когда приходит определенный запрос
- Сервлет - это Java-объект, который умеет принимать HTTP-запросы и возвращать HTTP-ответы.

[к оглавлению](#Основы-web)
### Rest
- Representational state transfer - интерфейс приложения, построенный по определённым стандартизированным правилам.
- Resources - главный компонент в Rest, они описывают последовательность зависимых сущностей в видей URL. Используются только существительные в названии. /users/itsfield/itsid
- Http методы отражают действие которое происходит при их вызове.
- Client-server - клиент серверная архитектура, сервер умеет только отправлять JSON на клиент.
- Controller - Service - Repository
- Уровни зрелости REST:
	1. Http - использование протокола Http
	2. Resource - добавление разных ресурсов вместо одного "/api"
	3. Http verbs - GET, PUT, DELETE, POST
	4. Hypermedia countrols - сервер кроме основного ресурса возвращает ссылки на доступные ресурсы из текущего представления (страницы) приложения

[к оглавлению](#Основы-web)
### Http
- Http - hypertext transfer protocol
- Https - Secure Http, работает с помощью протокола TLS, который убеждается что сайт достаточно шифрует данные, таким образом при перехвате их нельзя прочитать и использовать.
- Гипертекст - это текстовые файлы, которые гиперссылками указывают на другие файлы в интернете.
- URL - uniform resource locator
- Код HTTP ответа - 1хх - 5хх отражают статус ответа
	- **1xx**: Информационные — сервер принял запрос и ожидает дальнейших действий  
	- **2xx**: Успешно — запрос успешно обработан  
	- **3xx**: Перенаправление — клиенту нужно выполнить дополнительный запрос  
	- **4xx**: Ошибка клиента — проблема в запросе со стороны клиента  
	- **5xx**: Ошибка сервера — сервер не смог корректно обработать запрос  
- Заголовки - это информация о авторизациях, языках, то есть информация которую нельзя передавать в теле
- Тело запроса - это сама информация которую мы хотим обработать
- Идемпотентность запросов
	1. Идемпотентный запрос - когда повторение одного и того же результата не приводит к ошибке и всегда одинаковый ответ
	2. Идемпотентные методы: GET, DELETE, PUT
	3. Get - т.к. получение ресурса не меняет ничего
	4. Delete - повторное удаление ничего не меняет, и не приводит к ошибке
	5. Put - повторное обновление одних и тех же полей, ничего не меняет
	6. Post и Patch например не являются идемпотентными, т.к. Post создаёт новый ресурс на сервере, а Patch (по логике это обновление, но которое автоматически в разных случаях может сделать другую операцию, например перевод enum ресурса в следующий этап, или инкремент продвинуть)

[к оглавлению](#Основы-web)
### Xml Json
- Форматы файлов для обмена данными
- XML - открывающие закрывающие теги, используется не только для обмена данными но и для конфигурационных файлов
- Json - это пары ключ значение

[к оглавлению](#Основы-web)
### gRPC + Protobuf
**gRPC**  
- A high-performance remote procedure calls framework for calling methods on remote services as if they were local.  
- Uses HTTP/2 for efficient, low-latency communication.  
- Enables streaming, multiplexing, and bi-directional communication.  

**Protobuf**  
- Default data format for gRPC (Protocol Buffers).  
- Strongly typed and schema-based.  
- Compact binary format (faster and smaller than JSON).  
- Supports versioning and backward compatibility.  
- Defines both data models and service interfaces via `.proto` files.  

**Use Cases**  
- Microservices communication  
- Real-time systems (chat, telemetry)  
- Mobile and IoT (low bandwidth)  
- Multi-language systems (Java, Go, Python, etc.)  

**Spring Boot Integration**  
- Use gRPC starter library.  
- Define `.proto` files.  
- Implement services using `@GrpcService` and call with `@GrpcClient`.  

[к оглавлению](#Основы-web)
### WebSocket 
- Двусторонняя связь клиента и сервера, для непрерывной обмена данными без дополнительных запросов.
- Сначала происходит один HTTP Запрос на рукопожатие, если сервер отвечает положительно, то HTTP меняется на WebSocket, и дальше держится эта связь, и сервер сразу же отсылает новые данные если они у него появляются
- Примеры: Мессенджеры, Игры, GPS

[к оглавлению](#Основы-web)
### Exception Handling
- **Global exception handling** 
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateBookmarkException.class)
    public ResponseEntity<ApiError> handleDuplicateBookmarkException(DuplicateBookmarkException e) {
      ApiError error = new ApiError(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
```
[к оглавлению](#Основы-web)
### Spring Web
- RestController - returns JSON
- Controller - return web-view or redirect.
- @PostMapping - annotation for methods in controller that handle creating objects
- @GetMapping - for reading inf from server
- @PutMapping for updating
- @DeleteMapping
- model - view - controller

[к оглавлению](#Основы-web)
### Getting data from request

`@PathVariable` — Extracting data from the URI path
```java
@GetMapping("/users/{id}")
public User getUserById(@PathVariable Long id) { ... }
```
**Example URL**: `/users/42` → `id = 42`

---

`@RequestParam` — Sending data as query parameters
```java
@GetMapping("/users")
public List<User> getUsersByName(@RequestParam String name) { ... }
```
**Example URL**: `/users?name=Alice` → `name = "Alice"`

---

`@RequestBody` — Sending data in the JSON body
```java
@PostMapping("/users")
public User createUser(@RequestBody User user) { ... }
```
**Example JSON body**:
```json
{
  "name": "Alice",
  "email": "alice@example.com"
}
```
---

`@RequestHeader` — Extracting data from HTTP headers
```java
@GetMapping("/profile")
public Profile getProfile(@RequestHeader("Authorization") String token) { ... }
```
**Example Header**: `Authorization: Bearer abc123`

---

`@CookieValue` — Reading cookies from the request
```java
@GetMapping("/dashboard")
public Dashboard getDashboard(@CookieValue("sessionId") String sessionId) { ... }
```
**Example Cookie**: `sessionId=xyz789`

[к оглавлению](#Основы-web)
### **ResponseEntity** or Object:
Response entity is used if:
- I need to send different HTTP Status Codes for different kinds of failures or validation errors.
- I need to add headers.

[к оглавлению](#Основы-web)
### DTO
DTO (Data Transfer Object) - object used by app layers to communicate

**Best Practice**
- Implemented as a records
- In DTO using reference types only to DTO
- DTO should contain minimal information to correct work
- ClassDTO - returned by service. 
- ClassRequest - to extract from URL
- ClassCommand - to send from Controller to Service

 **MapStruct**
- Used to map DTO to Object or another DTO
``` java
@Mapper
public interface EmployeeMapper {
    @Mapping(target = "employeeId", source = "entity.id")
    @Mapping(target = "employeeName", source = "entity.name")
    EmployeeDTO employeeToEmployeeDTO(Employee entity);
}
```
[к оглавлению](#Основы-web)
### Что такое WWW и как работает интернет?
__WWW, World Wide Web (Всемирная паутина)__ — распределённая система, предоставляющая доступ к связанным между собой документам, расположенным на различных компьютерах, подключённых к Интернету. Для обозначения этого термина также используют слово _web_.

Интернет — это глобальная сеть, объединяющая миллионы компьютеров по всему миру. Основные протоколы передачи данных: TCP/IP, HTTP, FTP и др. Веб работает по принципу клиент-сервер: браузер (клиент) отправляет запросы на сервер, который возвращает документы (обычно HTML, CSS, JS, изображения и др.).

[к оглавлению](#Основы-web)

### Модель OSI и стек TCP/IP
| # |                  Уровень (layer)                 |               Тип данных (PDU)            |                          Функции                         |          Примеры           |
|--:|--------------------------------------------------|:-----------------------------------------:|----------------------------------------------------------|:--------------------------:|
| 7 | Прикладной (application)                         |                      -                    | Доступ к сетевым службам                                 | HTTP, FTP                  |
| 6 | Представительский (presentation)                 |                      -                    | Представление и шифрование данных                        | ASCII, JPEG                |
| 5 | Сеансовый (session)                              |                      -                    | Управление сеансом связи                                 | RPC, PAP                   |
| 4 | Транспортный (transport)                         | Сегменты(segment) / Дейтаграммы(datagram) | Прямая связь между конечными пунктами и надежность       | TCP, UDP                   |
| 3 | Сетевой (network)                                |              Пакеты (packet)              | Определение маршрута и логическая адресация              | IP, AppleTalk              |
| 2 | Канальный (data link)                            |         Биты (bit) / Кадры (frame)        | Физическая адресация                                     | Ethernet, IEEE 802.2, L2TP |
| 1 | Физический (physical)                            |                  Биты (bit)               | Работа со средой передачи, сигналами и двоичными данными | USB, витая пара            |

__TCP/IP__ — основной стек протоколов Интернета. Включает 4 уровня: канальный, сетевой (IP), транспортный (TCP/UDP), прикладной (HTTP, FTP, DNS). TCP обеспечивает надёжную доставку данных, IP — маршрутизацию. TCP-соединение устанавливается через handshake (SYN, SYN-ACK, ACK).

[к оглавлению](#Основы-web)

### TCP и UDP: в чём разница?
__TCP__ — протокол с установлением соединения (handshake), гарантирует доставку, порядок, надёжность, потоковую передачу данных. Используется для задач, где важна целостность (HTTP, FTP, email).

__UDP__ — протокол без соединения, не гарантирует доставку и порядок, но быстрее и проще. Используется для стриминга, DNS, VoIP, игр.

| Свойство         | TCP                                   | UDP                                   |
|------------------|---------------------------------------|---------------------------------------|
| Соединение       | Требует (handshake)                   | Не требует                            |
| Надёжность       | Гарантирует доставку                  | Не гарантирует                        |
| Порядок          | Сохраняет                             | Не сохраняет                          |
| Скорость         | Медленнее (из-за контроля)            | Быстрее                               |
| Применение       | HTTP, FTP, email                      | DNS, VoIP, видео, игры                |

[к оглавлению](#Основы-web)

### HTTP: методы, cookies, сессии
__HTTP__ — протокол передачи гипертекста, работает по принципу клиент-сервер. Основные методы:
- **GET** — получение ресурса (идемпотентен)
- **POST** — создание ресурса (не идемпотентен)
- **PUT** — обновление ресурса (идемпотентен)
- **DELETE** — удаление ресурса (идемпотентен)

__HTTP-ответы__:
- 1xx — информационные
- 2xx — успешные
- 3xx — перенаправления
- 4xx — ошибки клиента
- 5xx — ошибки сервера

__Cookies__ — небольшие данные, сохраняемые браузером для идентификации, хранения сессий, предпочтений.

__Сессия__ — период взаимодействия пользователя с сайтом, обычно отслеживается через cookies.

__HTTP vs HTTPS__:
- HTTPS — это HTTP + шифрование (SSL/TLS), защищает данные от перехвата.
- HTTP — порт 80, HTTPS — порт 443.

[к оглавлению](#Основы-web)

### Авторизация и аутентификация
- __Аутентификация__ — проверка личности (логин/пароль, биометрия и т.д.)
- __Авторизация__ — определение прав доступа после аутентификации

Обычно сначала происходит аутентификация, затем авторизация. Без аутентификации авторизация невозможна.

[к оглавлению](#Основы-web)

### Архитектура веб-приложений
- __Web server__ — принимает HTTP-запросы, отдаёт ответы (статические файлы, проксирование, базовые функции)
- __Application server__ — выполняет бизнес-логику, может генерировать динамические страницы, поддерживает сессии, авторизацию, интеграцию с БД
- __Web application__ — клиент-серверное приложение, где клиент — браузер, сервер — web/app server. Данные хранятся на сервере, клиент кроссплатформенный.

[к оглавлению](#Основы-web)

# Источники
+ [Википедия](https://ru.wikipedia.org/)

[Вопросы для собеседования](README.md)
