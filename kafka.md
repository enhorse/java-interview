[Вопросы для собеседования](README.md)

# Apache Kafka
* [Что такое Apache Kafka?](#что-такое-apache-kafka)
* [Основные компоненты Kafka](#основные-компоненты-kafka)

**Архитектура компонентов**

* Topic
  * [Архитектура топика](#архитектура-топика)
  * [Настройки топика Kafka](#настройки-топика-kafka)
* Broker
  * [Архитектура брокера](#архитектура-брокера)
  * [Настройки брокера Kafka](#настройки-брокера-kafka)
* Producer
  * [Архитектура продюсера](#архитектура-продюсера)
  * [Настройки продюсера](#настройки-продюсера)
  * [Пример конфигурации Kafka Producer](#пример-конфигурации-kafka-producer)
* Consumer
  * [Архитектура консюмера](#архитектура-консюмера)
  * [Настройки консюмера](#настройки-консюмера)
  * [Пример конфигурации Kafka Consumer](#пример-конфигурации-kafka-consumer)

**Kafka API**

* [Основные API Kafka](#основные-api-kafka)
* [Какова роль Producer API?](#какова-роль-producer-api)
* [Какова роль Consumer API?](#какова-роль-consumer-api)
* [Какова роль Connector API?](#какова-роль-connector-api)
* [Какова роль Streams API?](#какова-роль-streams-api)
* [Какова роль Transactions API?](#какова-роль-transactions-api)
* [Какова роль Quota API?](#какова-роль-quota-api)
* [Какова роль AdminClient API?](#какова-роль-AdminClient-api)

**Kafka Consumer**

* [Для чего нужен координатор группы?](#для-чего-нужен-координатор-группы)
* [Для чего нужен Consumer heartbeat thread?](#для-чего-нужен-consumer-heartbeat-thread)
* [Как Kafka обрабатывает сообщения?](#как-kafka-обрабатывает-сообщения)
* [Как Kafka обрабатывает задержку консюмера?](#как-kafka-обрабатывает-задержку-консюмера)
* [Для чего нужны методы subscribe() и poll()?](#для-чего-нужны-методы-subscribe-и-poll)
* [Для чего нужен метод position()?](#для-чего-нужен-метод-position)
* [Для чего нужны методы commitSync() и commitAsync()?](#для-чего-нужны-методы-commitsync-и-commitasync)

**Другие вопросы**

* [Для чего нужен идемпотентный продюсер?](#для-чего-нужен-идемпотентный-продюсер)
* [Для чего нужен интерфейс Partitioner?](#для-чего-нужен-интерфейс-partitioner)
* [Для чего нужен Broker log cleaner thread?](#для-чего-нужен-broker-log-cleaner-thread)
* [Для чего нужен Kafka Mirror Maker?](#для-чего-нужен-kafka-mirror-maker)
* [Для чего нужна Schema Registry?](#для-чего-нужна-schema-registry)
* [Для чего нужен Streams DSL?](#для-чего-нужен-streams-dsl)
* [Как Kafka обеспечивает версионирование сообщений?](#как-kafka-обеспечивает-версионирование-сообщений)
* [Как потребители получают сообщения от брокера?](#как-потребители-получают-сообщения-от-брокера)

**Сравнение с другими компонентами и системами**

* [В чем разница между Kafka Consumer и Kafka Stream?](#в-чем-разница-между-kafka-consumer-и-kafka-stream)
* [В чем разница между Kafka Streams и Apache Flink?](#в-чем-разница-между-kafka-streams-и-apache-flink)
* [В чем разница между Kafka и Flume?](#в-чем-разница-между-kafka-и-flume)
* [В чем разница между Kafka и RabbitMQ?](#в-чем-разница-между-kafka-и-rabbitmq)


## Что такое Apache Kafka?

Это распределённая система с открытым исходным кодом, разработанная для высокоскоростной передачи больших объёмов данных 
с минимальной задержкой.

### Преимущества

* Персистентность данных
* Высокая производительность
* Независимость пайплайнов обработки
* Возможность просмотреть историю записей заново
* Гибкость в использовании

### Когда использовать

* λ-архитектура или k-архитектура
* Стриминг больших данных
* Много клиентов (producer и consumer)
* Требуется кратное масштабирование

### Чего в Kafka нет из коробки

* Это не брокер сообщений
* Отложенные сообщения
* DLQ
* AMQP / MQTT
* TTL на сообщение
* Очереди с приоритетами

[к оглавлению](#apache-kafka)

## Основные компоненты Kafka

* **Producer (Производитель)** — приложение, которое публикует сообщения в топики Kafka
* **Consumer (Потребитель)** — приложение, которое подписывается на топики и читает сообщения
* **Broker (Брокер)** — сервер Kafka, который принимает, хранит и распределяет сообщения. В кластере Kafka может быть несколько брокеров
* **Topic (Топик)** — логическое разделение, по которому организуются данные. Производители отправляют сообщения в топики, а потребители читают из них
* **Partition (Раздел)** — каждый топик разделён на партиции для параллельной обработки. Сообщения в партициях упорядочены
* **Zookeeper** — сервис, используемый Kafka для управления состоянием кластера и координации брокеров. 
Однако в новых версиях Kafka отказывается от Zookeeper в пользу собственного механизма метаданных KRaft (Kafka Raft). 
Это новая внутренняя архитектура метаданных Kafka, которая устраняет зависимость от Zookeeper. Она основана на Raft-консенсусе, 
позволяя Kafka брокерам самостоятельно управлять метаданными и координировать взаимодействие между собой.

[к оглавлению](#apache-kafka)

## Архитектура топика

* **Топик разбит на партиции** — сообщения в топике распределяются по партициям для более эффективной параллельной обработки и хранения
* **Партиции хранятся на диске** — Kafka сохраняет данные на диск, что позволяет долговременно хранить сообщения
* **Партиции делятся на сегменты** — сегмент представляет собой обычный файл на диске, сегменты делятся на пассивные и активный.
  Запись происходит в активный сегмент
* **Данные удаляются либо по времени, либо по размеру**. Удаление происходит посегментно, с самого старого сегмента
  * **retention.bytes** - по максимальному размеру
  * **retention.ms** - по времени
* **Сообщение можно быстро найти по его Offset** — каждому сообщению в партиции присваивается уникальный смещающий индекс (offset), по которому можно легко найти сообщение

[к оглавлению](#apache-kafka)

## Настройки топика Kafka

### Репликация

* `replication.factor`
  * **Описание**: Количество реплик для каждой партиции топика
  * **Пример**: `replication.factor=3`
* `min.insync.replicas`
  * **Описание**: Минимальное количество синхронизированных реплик
  * **Пример**: `min.insync.replicas=2`

### Хранение данных

* `retention.ms`
  * **Описание**: Время хранения сообщений в топике в миллисекундах
  * **Пример**: `retention.ms=604800000` (7 дней)
* `retention.bytes`
  * **Описание**: Максимальный объём данных в топике, после чего старые сообщения удаляются
  * **Пример**: `retention.bytes=10737418240` (10 GB)
* `segment.bytes`
  * **Описание**: Размер сегмента логов топика
  * **Пример**: `segment.bytes=1073741824` (1 GB)

### Политики очистки

* `cleanup.policy`
  * **Описание**: Как Kafka обрабатывает старые сообщения
  * **Значения**: `delete`, `compact`
  * **Пример**: `cleanup.policy=delete`

### Партиции

* `num.partitions`
  * **Описание**: Количество партиций в топике
  * **Пример**: `num.partitions=3`

[к оглавлению](#apache-kafka)

## Архитектура брокера

* **У каждой партиции свой лидер** — в Kafka для каждой партиции в топике назначается лидер-брокер, который отвечает
  за запись и чтение данных
* **Сообщения пишутся в лидера** — производители отправляют сообщения напрямую в брокер-лидер партиции
* **Данные реплицируются между брокерами** — для обеспечения отказоустойчивости Kafka реплицирует данные партиций на
  другие брокеры, которые становятся репликами
* **Автоматический фейловер лидера** — в случае сбоя брокера-лидера Kafka автоматически назначает новый лидер из числа
  реплик, обеспечивая бесшовную работу системы

[к оглавлению](#apache-kafka)

## Настройки брокера Kafka

### Репликация и консистентность

* `min.insync.replicas`
  * **Описание**: Минимальное количество синхронизированных реплик для подтверждения записи
  * **Пример**: `min.insync.replicas=2`
* `unclean.leader.election.enable`
  * **Описание**: Разрешает выбор лидера из неактуальных реплик, если нет синхронизированных реплик
  * **Пример**: `unclean.leader.election.enable=false`

### Логирование и хранение данных

* `log.dirs`
  * **Описание**: Директория на диске, где хранятся логи партиций
  * **Пример**: `log.dirs=/var/lib/kafka/logs`
* `log.retention.hours`
  * **Описание**: Максимальное время хранения данных в логах
  * **Пример**: `log.retention.hours=168` (7 дней)
* `log.segment.bytes`
  * **Описание**: Максимальный размер сегмента лога, после чего создаётся новый
  * **Пример**: `log.segment.bytes=1073741824` (1 GB)

### Производительность и задержки

* `num.network.threads`
  * **Описание**: Количество потоков для обработки сетевых запросов
  * **Пример**: `num.network.threads=3`
* `num.io.threads`
  * **Описание**: Количество потоков для ввода-вывода
  * **Пример**: `num.io.threads=8`
* `socket.send.buffer.bytes`
  * **Описание**: Размер буфера для отправки данных по сети
  * **Пример**: `socket.send.buffer.bytes=102400`

### Управление сообщениями

* `message.max.bytes`
  * **Описание**: Максимальный размер сообщения, которое брокер может принять
  * **Пример**: `message.max.bytes=1048576` (1 MB)
* `replica.fetch.max.bytes`
  * **Описание**: Максимальный размер данных для запроса реплики
  * **Пример**: `replica.fetch.max.bytes=1048576` (1 MB)

### Безопасность

* `ssl.keystore.location`
  * **Описание**: Путь к хранилищу ключей SSL
  * **Пример**: `ssl.keystore.location=/var/private/ssl/kafka.keystore.jks`
* `ssl.truststore.location`
  * **Описание**: Путь к хранилищу доверенных сертификатов
  * **Пример**: `ssl.truststore.location=/var/private/ssl/kafka.truststore.jks`

[к оглавлению](#apache-kafka)

## Архитектура продюсера

* **Создание сообщения (Record)**: Продюсер формирует сообщение, содержащее ключ (необязательный), значение и метаданные,
  такие как время отправки. Сообщение отправляется в топик (Topic), который состоит из одной или нескольких партиций
* **Выбор партиции**: Если ключ сообщения указан, Kafka использует его для хеширования и определения, в какую партицию
  записать сообщение (сообщения с одинаковым ключом попадают в одну и ту же партицию). Если ключа нет, Kafka распределяет
  сообщения по партициям с помощью round-robin или по другим правилам
* **Отправка сообщений в буфер (Batching)**: Для повышения производительности продюсер Kafka не отправляет каждое сообщение
  по отдельности, а группирует несколько сообщений в пакеты (batching), прежде чем отправить их брокеру. Это снижает
  сетевые задержки и нагрузку на брокера
* **Сжатие (Compression)**: Для уменьшения объёма передаваемых данных продюсер может сжимать сообщения с использованием
  таких алгоритмов, как GZIP, Snappy или LZ4. Сжатие снижает нагрузку на сеть и хранение, но добавляет небольшие накладные
  расходы на процессор
* **Асинхронная отправка**: Продюсер отправляет пакеты сообщений асинхронно. Это означает, что сообщения записываются в
  буфер памяти и отправляются брокеру, не ожидая завершения предыдущих операций. Это повышает пропускную способность
* **Подтверждения (Acknowledgments)**: Kafka позволяет настраивать уровень подтверждений от брокеров
* **Ретрай и идемпотентность**: Если отправка сообщения не удалась, продюсер может повторить попытку отправки (ретрай).
  Также можно включить идемпотентный режим продюсера, что предотвращает повторную отправку одного и того же сообщения в
  случае сбоя, обеспечивая отправку уникального сообщения один раз
* **Error handling**: Продюсер обрабатывает ошибки при отправке сообщений. В зависимости от настроек продюсер может
  попытаться переотправить сообщение или сообщить о проблеме через callback

### Резюме

* Продюсер выбирает партицию для сообщения
* Продюсер выбирает уровень гарантии доставки
* В продюсере можно тюнить производительность

[к оглавлению](#apache-kafka)

## Настройки продюсера

### Bootstrap-серверы (`bootstrap.servers`)

* **Описание**: Указывает адреса брокеров Kafka, к которым продюсер должен подключаться для отправки сообщений
* **Пример**: `bootstrap.servers: localhost:9092,localhost:9093`
* **Зачем это нужно**: Kafka продюсер использует эти брокеры для получения метаданных о кластере (например, информация о топиках и партициях). Эти брокеры служат точками входа в кластер Kafka.

### Сериализация ключа и значения

Продюсер должен преобразовывать (сериализовать) данные в байтовый формат перед отправкой в Kafka

* **Ключевая настройка для сериализации ключа:**
  * `key.serializer`
  * Пример: `key.serializer: org.apache.kafka.common.serialization.StringSerializer`
* **Ключевая настройка для сериализации значения:**
  * `value.serializer`
  * Пример: `value.serializer: org.apache.kafka.common.serialization.StringSerializer`

**Варианты сериализаторов:**
* `StringSerializer` для строк
* `ByteArraySerializer` для массива байтов
* `LongSerializer` для чисел
* Также можно реализовать свои собственные сериализаторы

### Отправка сообщений в буфер

Продюсер Kafka отправляет сообщения асинхронно, и для этого используется буферизация сообщений

* **batch.size**: Размер одного пакета (batch), который продюсер отправляет брокеру
  * **Описание**: Определяет количество байтов сообщений, которые могут быть буферизованы в одном пакете перед отправкой брокеру
  * **Пример**: `"batch.size": 16384` (16 KB)
  * **Зачем это нужно**: Большие пакеты могут повысить производительность, но могут увеличить задержки
* **linger.ms**: Максимальное время ожидания перед отправкой пакета
  * **Описание**: Продюсер может немного подождать, пока буфер накопит сообщения, чтобы отправить больше данных за один раз
  * **Пример**: `linger.ms: 5` (время ожидания 5 мс)
  * **Зачем это нужно**: Позволяет продюсеру собирать больше сообщений в пакете перед отправкой, что может улучшить эффективность использования сети
* **buffer.memory**: Размер выделенной памяти для буферизации сообщений
  * **Описание**: Общий объем памяти, который продюсер может использовать для хранения сообщений, ожидающих отправки
  * **Пример**: `buffer.memory: 33554432` (32 MB)
  * **Зачем это нужно**: Если буфер заполняется, продюсер приостанавливает отправку сообщений, пока буфер не освободится

### Сжатие сообщений

Продюсер может сжимать сообщения для уменьшения объема передаваемых данных

* **compression.type**
  * **Описание**: Указывает тип сжатия для сообщений
  * **Пример**: `compression.type: gzip` (варианты: none, gzip, snappy, lz4, zstd)
  * **Зачем это нужно**: Сжатие уменьшает объем данных, передаваемых по сети, что может снизить нагрузку на сеть и хранилище,
    особенно при больших объемах сообщений. Однако это может потребовать дополнительных ресурсов на сжатие/разжатие

### Распределение сообщений по партициям (партицирование)

* **partitioner.class**
  * **Описание**: определяет логику, по которой продюсер выбирает партицию для каждого сообщения
  * **Примеры**:
    * **если настройка не задана**, по умолчанию используется `DefaultPartitioner` , который может распределять сообщения по партициям
      равномерно или на основе ключа сообщения
    * `partitioner.class: o.a.k.clients.producer.RoundRobinPartitioner` использует метод Round Robin для распределения сообщений
    * `partitioner.class: o.a.k.clients.producer.UniformStickyPartitioner` равномерно отправляет сообщения, привязываясь
      к партиции на короткий промежуток времени, чтобы уменьшить нагрузку на брокеры

### Подтверждения (acks)

Настройка определяет, как много брокеров должны подтвердить получение сообщения перед тем, как продюсер будет считать его
успешно отправленным

* **acks**
  * **Описание**: Определяет количество подтверждений от брокеров
  * **Значения**:
    * `0`: Продюсер не ждёт подтверждений (самая быстрая отправка, но высокий риск потери сообщений)
    * `1`: Продюсер ждёт подтверждения от лидера партиции
    * `all` (или `-1`): Продюсер ждёт подтверждений от всех реплик (наибольшая надежность, но увеличенные задержки)
  * **Пример**: `acks: all`
  * **Зачем это нужно**: Позволяет выбрать баланс между скоростью и надежностью отправки данных.

### Дополнительные важные настройки

* **Количество повторных попыток (retries):**
  * **Описание**: Определяет, сколько раз продюсер должен попытаться отправить сообщение при неудаче
  * **Пример**: `retries: 3`
  * **Зачем это нужно**: Если произошёл временный сбой, продюсер может попытаться повторить отправку сообщений, что
    увеличивает шанс доставки
* **Идемпотентность продюсера (enable.idempotence):**
  * **Описание**: Включение идемпотентного режима, что предотвращает дублирование сообщений при сбоях
  * **Пример**: `enable.idempotence: true`
  * **Зачем это нужно**: Гарантирует, что каждое сообщение будет доставлено ровно один раз
* **Максимальный размер сообщения (max.request.size):**
  * **Описание**: Максимальный размер сообщения, которое продюсер может отправить брокеру
  * **Пример**: `max.request.size: 1048576` (1 MB)
  * **Зачем это нужно**: Ограничивает размер сообщений, которые могут быть отправлены, чтобы избежать перегрузки сети и брокеров.
* **Таймаут ожидания подтверждений (request.timeout.ms):**
  * **Описание**: Максимальное время ожидания подтверждения от брокера
  * **Пример**: `request.timeout.ms: 30000` (30 секунд)
  * **Зачем это нужно**: Помогает избежать бесконечного ожидания ответа от брокера в случае его сбоя

[к оглавлению](#apache-kafka)

## Пример конфигурации Kafka Producer

```java
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class KafkaStringArrayProducer {
    
    public static void main(String[] args) {
        // Настройки Kafka Producer
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Создание Kafka Producer
        KafkaProducer<String, String[]> producer = new KafkaProducer<>(props);

        String key = "user123";
        String[] value = {"message1", "message2", "message3"};

        // Создание записи и добавление заголовков
        ProducerRecord<String, String> record = new ProducerRecord<>("my_topic", key, value);
        record.headers().add("traceId", "someTraceId");

        // Отправка сообщения в Kafka
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                System.out.println("Ошибка при отправке сообщения: " + exception.getMessage());
            } else {
                System.out.println("Сообщение отправлено в топик " + metadata.topic() + " с партицией " + metadata.partition());
            }
        });

        producer.close();
    }
}
```

```properties
acks=all
retries=3
compression.type=gzip
```

### С использованием Spring Kafka

```java
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.config.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.producer.Producer;
import org.springframework.kafka.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProducerConfig {
    
    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServer());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProperties.getProducerId());
        props.put(
                ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, 
                "com.example.configuration.kafka.KafkaProducerLoggingInterceptor"
        );
        
        if ("SASL_SSL".equals(kafkaProperties.getSecurityProtocol())) {
            props.put("ssl.truststore.location", kafkaProperties.getSslTrustStoreLocation());
            props.put("ssl.truststore.password", kafkaProperties.getSslTrustStorePassword());
            props.put("ssl.truststore.type", kafkaProperties.getSslTrustStoreType());
            props.put("ssl.keystore.type", kafkaProperties.getSslKeyStoreType());
          
            props.put("sasl.mechanism", kafkaProperties.getSaslMechanism());
            props.put("security.protocol", kafkaProperties.getSecurityProtocol());
            props.put("sasl.jaas.config", kafkaProperties.getJaasConfigCompiled());
        }
        
        return props;
    }
  
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        var stringSerializerKey = new StringSerializer();
        stringSerializerKey.configure(Map.of("key.serializer.encoding", "UTF-8"), true);
        stringSerializerKey.configure(Map.of("serializer.encoding", "UTF-8"), true);
    
        var stringSerializerValue = new StringSerializer();
        stringSerializerValue.configure(Map.of("value.serializer.encoding", "UTF-8"), false);
        stringSerializerValue.configure(Map.of("serializer.encoding", "UTF-8"), false);
    
        return new DefaultKafkaProducerFactory<>(producerConfigs(), stringSerializerKey, stringSerializerValue);
    }
  
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
```

```java
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message, String key, String topic) {
      try {
        log.info("Sending message {}", data);
        kafkaTemplate.send(topic, key, message);
        log.info("Successfully send message {}", data);
      } catch (Exception ex) {
        log.error("Failed send message to {} topic by key {}", key, topic);
        throw ex;
      }
    }
}
```

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message, @RequestParam String key, @RequestParam String topic) {
        kafkaProducerService.sendMessage(message, key, topic);
        return "Message sent to Kafka!";
    }
}
```

### С использованием Spring Cloud Stream

```yaml
spring:
  cloud:
    stream:
      bindings:
        output:
          destination: my_topic
      kafka:
        binder:
          brokers: localhost:9092
```

```java
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Source.class) // Подключение к каналу сообщений
public class KafkaStreamProducer {

    private final Source source;

    public KafkaStreamProducer(Source source) {
        this.source = source;
    }

    public void sendMessage(String message) {
        Message<String> msg = MessageBuilder.withPayload(message).build();
        source.output().send(msg); // Отправка сообщения в Kafka
    }
}
```

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka-stream")
public class KafkaStreamController {

    @Autowired
    private KafkaStreamProducer kafkaStreamProducer;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) {
        kafkaStreamProducer.sendMessage(message);
        return "Message sent to Kafka via Spring Cloud Stream!";
    }
}
```

[к оглавлению](#apache-kafka)

## Архитектура консюмера

Потребители используют **Kafka Consumer API** для взаимодействия с брокерами Kafka. Они получают сообщения и обрабатывают
их согласно своей логике. Потребители могут быть объединены в группы **Consumer Groups**.

### Резюме

* "Smart" консюмер
* Консюмер опрашивает кафку
* Консюмер отвечает за гарантию обработки
* Автоматические фейловер в консюмер-группе
* Независимая обработка разными консюмер-группе

### Компоненты

#### Consumer Group

Kafka использует концепцию Consumer Groups, что позволяет нескольким потребителям работать вместе, чтобы параллельно
обрабатывать данные из топиков. Каждый потребитель в группе обрабатывает только часть данных из топика, обеспечивая масштабируемость и балансировку нагрузки.

* Все сообщения из одного Kafka Topic делятся между всеми потребителями в группе
* Если в группе несколько потребителей, Kafka гарантирует, что каждая партиция топика будет обрабатываться только одним потребителем
* В случае если один из потребителей выходит из строя, его партиции автоматически перераспределяются между оставшимися активными потребителями

#### Offset (Смещение)

Потребитель отслеживает offset каждой партиции, чтобы понимать, с какого сообщения продолжать чтение. Смещение — это
уникальный идентификатор каждого сообщения в партиции.

Потребители могут хранить offset в Kafka или вне её (например, в базе данных или файловой системе). Если потребитель
отключается, он может возобновить обработку с того места, где остановился, прочитав сохранённый offset.

####  Poll (Опрос)

Потребители используют метод poll() для опроса Kafka на наличие новых сообщений. Это асинхронный процесс, и Kafka будет
отправлять потребителю доступные сообщения по мере их поступления.

* Потребитель может указывать тайм-аут, после которого метод poll() вернёт пустой результат, если сообщений нет.
* Потребитель должен обрабатывать сообщения, а затем снова опрашивать Kafka для получения новых данных.

### Процесс работы

1. **Инициализация**: Потребитель подключается к Kafka-брокерам и присоединяется к consumer group. Он получает информацию о партиции топика, который будет читать.
2. **Подписка на топик**: Потребитель подписывается на определённые топики с помощью метода `subscribe()`.
3. **Опрос**: Потребитель вызывает метод `poll()` для получения новых сообщений. Если в очереди есть сообщения, они передаются потребителю для обработки.
4. **Обработка сообщений**: Потребитель обрабатывает сообщения, извлекая полезную информацию из каждого.
5. **Подтверждение обработки**: После обработки сообщения потребитель подтверждает обработку с помощью `commit()`.
   Это обновляет **offset**, позволяя потребителю продолжить чтение с места, на котором остановился.
6. **Обработка ошибок**: В случае ошибки потребитель может решить, как повторить обработку сообщения
   (например, с использованием механизма повторных попыток).
7. **Завершение работы**: Когда потребитель завершает обработку, он выходит из consumer group и может закрыть соединение с Kafka.

[к оглавлению](#apache-kafka)

## Настройки консюмера

* **bootstrap.servers** — список брокеров, к которым будет подключаться потребитель
* **group.id** — идентификатор группы потребителей
* **auto.offset.reset** — настройка поведения при отсутствии offset (`earliest` для чтения с самого начала или `latest` для чтения с конца)
* **enable.auto.commit** — указывает, должен ли потребитель автоматически коммитить offset. Если `false`, потребитель должен делать это вручную
* **auto.commit.interval.ms** — определяет интервал времени между автоматическими коммитами offset сообщений, если включена автоматическая фиксация
* **max.poll.records** — максимальное количество сообщений, которые потребитель будет получать за один вызов `poll()`
* **session.timeout.ms** — максимальное время без общения с Kafka перед тем, как потребитель считается недоступным
* **client.rack** — используется для указания серверной стойки или дата-центра. Это особенно важно в случае, если у вас
  есть распределённая инфраструктура Kafka с несколькими стойками или дата-центрами, где сообщения могут быть реплицированы
  между разными физическими местоположениями (например, несколькими дата-центрами).

### Что такое Rack в контексте Kafka?

**Rack** — это метка, которая идентифицирует физическое местоположение брокеров Kafka. В Kafka можно задать rack для каждого брокера
с помощью параметра `broker.rack`, чтобы управлять репликацией данных, предпочтительно размещая реплики на разных физических машинах или в разных дата-центрах.

**Преимущества использования client.rack**

* **Снижение задержек**: Kafka будет предпочитать, чтобы данные попадали в тот же rack, где находится клиент, что уменьшает время отклика
* **Повышенная отказоустойчивость**: С правильной настройкой client.rack и broker.rack можно улучшить отказоустойчивость
  за счет размещения реплик в разных физически удаленных местах
* **Лучшее использование ресурсов**: Правильное распределение нагрузки по rack помогает избежать перегрузки одного физического местоположения

[к оглавлению](#apache-kafka)

## Пример конфигурации Kafka Consumer

```java
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class KafkaConsumerExample {

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String groupId = "my-consumer-group";
        String topic = "my-topic";

        // Настройки Consumer
        Map<String, Object> consumerConfigs = new HashMap<>();
        consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Создание Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfigs);

        // Подписка на тему
        consumer.subscribe(Collections.singletonList(topic));

        try {
            // Чтение сообщений из Kafka
            while (true) {
                var records = consumer.poll(Duration.ofSeconds(1));
                records.forEach(record -> System.out.println("Received message: " + record.value()));
            }
        } finally {
            consumer.close();
        }
    }
}
```

**At least once**

Чтобы гарантировать обработку сообщений хотя бы один раз, нужно коммитить после обработки.

```java
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class KafkaConsumerAtLeastOnce {

  public static void main(String[] args) {
    try {
      // Чтение сообщений
      while (true) {
        var records = consumer.poll(Duration.ofSeconds(1));  // Ожидание 1 секунду для получения сообщений
        process(records);
        consumer.commitAsync(); // Commit после обработки
      }
    } finally {
      consumer.close();  // Закрытие consumer
    }
  }
}
```

**At most once**

Чтобы гарантировать обработку сообщений не более одного раза, нужно коммитить до обработки или включить авто-подтверждение смещений
`enable.auto.commit=true`.

```java
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class KafkaConsumerAtLeastOnce {

  public static void main(String[] args) {
    try {
      // Чтение сообщений
      while (true) {
        var records = consumer.poll(Duration.ofSeconds(1));  // Ожидание 1 секунду для получения сообщений
        consumer.commitAsync(); // Commit перед обработкой
        process(records);
      }
    } finally {
      consumer.close();  // Закрытие consumer
    }
  }
}
```

### С использованием Spring Kafka

```java
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServer());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumerGroupId());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return configs;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentMessageListenerContainerFactory<String, String> factory = new ConcurrentMessageListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```

```java
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "my_topic", groupId = "group_id")
    public void listen(@Payload String message,
                       @Header("traceId") String traceId,
                       @Header("correlationId") String correlationId) {
        System.out.println("Received message: " + message);
        System.out.println("Trace ID: " + traceId);
        System.out.println("Correlation ID: " + correlationId);
    }
}
```

**At least once**

```yaml
spring:
  kafka:
    consumer:
      enable-auto-commit: false  # Отключение авто-commit
      auto-offset-reset: earliest  # Начинать чтение с самого начала (если нет смещения)
      group-id: my-consumer-group
      max-poll-records: 500  # Максимальное количество сообщений для обработки за один раз
    listener:
      ack-mode: manual  # Ручное подтверждение
```

```java
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.config.DefaultMessageListenerContainer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

@EnableKafka
public class AtLeastOnceConsumer {

    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group")
    public void listen(String message, Acknowledgment acknowledgment) {
        System.out.println("Received message: " + message);
        // Обработка сообщения
        // Подтверждение смещения вручную после успешной обработки
        acknowledgment.acknowledge();
    }
}
```

**At most once**

```yaml
spring:
  kafka:
    consumer:
      enable-auto-commit: true  # Включение авто-commit
      group-id: my-consumer-group
      auto-offset-reset: earliest  # Начинать чтение с самого начала
      max-poll-records: 100  # Максимальное количество сообщений для обработки за один раз
```

```java
import org.springframework.kafka.annotation.KafkaListener;

public class AtMostOnceConsumer {

    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
        // Обработка сообщения...
        // Смещение будет автоматически зафиксировано после получения сообщения
    }
}
```

### С использованием Spring Cloud Stream

```yaml
spring:
  cloud:
    stream:
      bindings:
        input:
          destination: my-topic
          group: my-consumer-group
          content-type: application/json
      kafka:
        binder:
          brokers: localhost:9092
          auto-create-topics: false
```

```java
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(KafkaProcessor.class)  // Указывает на интерфейс, с которым связывается этот сервис
public class KafkaConsumerService {

    // Метод будет слушать сообщения из указанного канала
    @StreamListener("input")
    public void handle(@Payload String message) {
        System.out.println("Received message: " + message);
    }
}
```

```java
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface KafkaProcessor {

    @Input("input")  // Имя канала, которое мы используем в application.yml
    SubscribableChannel input();
}
```

**At least once**

```yaml
spring:
  cloud:
    stream:
      bindings:
        input:
          destination: my-topic
          group: my-consumer-group
          content-type: application/json
          consumer:
            ackMode: manual  # Ручное подтверждение
            maxAttempts: 3  # Максимальное количество попыток
```

```java
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)  // Sink - это интерфейс, предоставляющий Binding для входных сообщений
public class AtLeastOnceConsumer {

    @StreamListener(Sink.INPUT)
    public void handleMessage(Message<String> message, @Header(name = "kafka_offset") String offset) {
        // Обработка сообщения
        System.out.println("Received message: " + message.getPayload());
        // После успешной обработки подтверждаем сообщение
        // Spring Cloud Stream автоматически подтвердит сообщение после завершения метода
        // благодаря ackMode=manual и настроенному acknowledgment
    }
}
```

**At most once**

```yaml
spring:
  cloud:
    stream:
      bindings:
        input:
          destination: my-topic
          group: my-consumer-group
          content-type: application/json
          consumer:
            ackMode: batch  # Автоматическое подтверждение после пакета сообщений
```

```java
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)
public class AtMostOnceConsumer {

    @StreamListener(Sink.INPUT)
    public void handleMessage(Message<String> message) {
        // Обработка сообщения
        System.out.println("Received message: " + message.getPayload());
        // Смещение будет автоматически зафиксировано после получения сообщения
    }
}
```

**Mostly Once**

Это гибридный режим, который стремится быть чем-то средним между At least once и At most once. Он предполагает, что сообщения
будут доставлены обычно один раз, но иногда, в случае сбоев, может быть обработано больше одного раза. Для реализации
такого режима в Spring Cloud Stream потребуется дополнительная логика, например, фильтрация дублированных сообщений или
использование уникальных идентификаторов сообщений.

В рамках Spring Cloud Stream, можно обработать Mostly Once с использованием уникальных идентификаторов сообщений или
кеширования состояния, чтобы отфильтровать повторно обработанные сообщения.

```yaml
spring:
  cloud:
    stream:
      bindings:
        input:
          destination: my-topic
          group: my-consumer-group
          content-type: application/json
          consumer:
            ackMode: manual  # Ручное подтверждение
            maxAttempts: 3  # Максимальное количество попыток
```

```java
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@EnableBinding(Sink.class)
public class MostlyOnceConsumer {

    private Set<String> processedMessageIds = new HashSet<>();

    @StreamListener(Sink.INPUT)
    public void handleMessage(Message<String> message, @Header("messageId") String messageId) {
        if (processedMessageIds.contains(messageId)) {
            System.out.println("Duplicate message: " + messageId);
            return;  // Пропускаем дублированное сообщение
        }
        // Обработка сообщения
        System.out.println("Received message: " + message.getPayload());
        // Добавляем идентификатор в обработанные
        processedMessageIds.add(messageId);
        // После успешной обработки подтверждаем сообщение вручную
        // Spring Cloud Stream подтвердит сообщение после выполнения метода
    }
}
```

[к оглавлению](#apache-kafka)

## Основные API Kafka

* Producer API
* Consumer API
* Streams API
* Connector API

[к оглавлению](#apache-kafka)

## Какова роль Producer API?

Используется для публикации потока сообщений в топики Kafka. Он управляет партицированием сообщений, сжатием и балансировкой 
нагрузки между несколькими брокерами. Продюсер также отвечает за повторные неудачные попытки публикации и может быть 
настроен на различные уровни гарантий доставки.

[к оглавлению](#apache-kafka)

## Какова роль Consumer API?

Обеспечивает механизм для потребления сообщений топиков. Оно позволяет приложениям и микросервисам читать данные, 
поступающие в Kafka, и обрабатывать их для дальнейшего использования, будь то хранение, анализ или реактивная обработка.

[к оглавлению](#apache-kafka)

## Какова роль Connector API?

Connector API в Apache Kafka является частью Kafka Connect, которая представляет собой инфраструктуру для интеграции
внешних систем с Kafka. Connector API играет ключевую роль в упрощении процесса подключения различных источников данных
и систем-приемников к Kafka, предоставляя возможность автоматического перемещения данных между ними.

[к оглавлению](#apache-kafka)

## Какова роль Streams API?

Это компонент Apache Kafka, предназначенный для создания приложений и микросервисов, которые обрабатывают потоки данных 
в реальном времени. Его основная роль заключается в том, чтобы позволить разработчикам легко обрабатывать и анализировать 
данные, поступающие в виде непрерывных потоков из топиков. Kafka Streams API предоставляет высокоуровневый интерфейс для 
выполнения таких операций, как фильтрация, агрегация, объединение данных и вычисление оконных функций.

[к оглавлению](#apache-kafka)

## Какова роль Transactions API?

Kafka Transactions API позволяет выполнять атомарные обновления для нескольких топиков. Он включает exactly-once
гарантию для приложений, которые читают данные из одного топика и пишут в другой. Это особенно полезно для приложений потоковой 
обработки, которым необходимо гарантировать, что каждое входное событие влияет на выходные данные ровно один раз, даже в случае сбоев.

[к оглавлению](#apache-kafka)

## Какова роль Quota API?

Quota API позволяет настраивать квоты для каждого клиента для ограничения скорости создания или потребления данных, чтобы 
один клиент не потреблял слишком много ресурсов брокера. Это помогает обеспечить справедливое распределение ресурсов и 
предотвратить сценарии отказа в обслуживании.

[к оглавлению](#apache-kafka)

## Какова роль AdminClient API?

AdminClient API предоставляет операции для управления топиками, брокерами, конфигурацией и другими объектами Kafka. 
Его можно использовать для создания, удаления и описания топиков, управления списками ACL, получения информации о кластере и
программного выполнения других административных задач.

[к оглавлению](#apache-kafka)

## Kafka Consumer

## Для чего нужен координатор группы?

Координатор группы отвечает за управление группами потребителей. Он управляет членством в группах потребителей, назначает
партиции потребителям внутри группы и управляет фиксацией смещения. Когда потребитель присоединяется к группе или покидает ее,
координатор группы запускает перебалансировку для переназначения партиций среди оставшихся потребителей.

[к оглавлению](#apache-kafka)

## Для чего нужен Consumer heartbeat thread?

Consumer Heartbeat Thread отвечает за отправку периодических сигналов брокеру Kafka (в частности, координатору группы).
Эти сигналы указывают на то, что потребитель жив и все еще является частью группы потребителей. Если потребитель не отправляет
данные сигналы в течение настроенного периода, он считается неживым, и координатор группы инициирует перебалансировку
для переназначения его партиций другим потребителям в группе.

[к оглавлению](#apache-kafka)

## Как Kafka обрабатывает сообщения?

Kafka поддерживает два основных способа обработки сообщений:
* **Queue**: каждое сообщение обрабатывается одним потребителем в группе потребителей. Это достигается за счет наличия
  в группе нескольких потребителей, каждый из которых считывает данные из отдельных партиций.
* **Publish-Subscribe**: все сообщения обрабатываются всеми потребителями. Это достигается за счет того, что каждый
  потребитель находится в своей собственной группе потребителей, что позволяет всем потребителям читать все сообщения.

[к оглавлению](#apache-kafka)

## Как Kafka обрабатывает задержку консюмера?

Задержка (лаг) консюмера в Kafka относится к разнице между оффсетом последнего созданного сообщения и оффсетом последнего
полученного сообщения. Kafka предоставляет инструменты и API для мониторинга задержек консюмеров такие, как инструмент
командной строки Kafka Consumer Groups и API AdminClient. Высокая задержка консюмеров может указывать на проблемы с
производительностью или недостаточную пропускную способность консюмеров. Kafka не обрабатывает задержки автоматически,
но предоставляет информацию, необходимую приложениям для принятия решений о масштабировании или оптимизации производительности.

[к оглавлению](#apache-kafka)

## Для чего нужны методы subscribe() и poll()?

Метод subscribe() используется для подписки на один или несколько топиков. Фактически он не извлекает никаких данных.
Метод poll(), с другой стороны, используется для извлечения данных из топиков. Он возвращает записи, которые были опубликованы
с момента последнего запроса топиков и партиций. Метод poll() обычно вызывается в цикле для непрерывного получения данных.

[к оглавлению](#apache-kafka)

## Для чего нужен метод position()?

Метод position() возвращает смещение следующей записи, которая будет извлечена для данной партиции. Это полезно для
отслеживания хода получения данных и может использоваться в сочетании с методом committed(), чтобы определить насколько
сильно потребитель отстал от своего последнего комита оффсета. Эта информация может быть ценной для мониторинга и
управления показателями потребителей.

[к оглавлению](#apache-kafka)

## Для чего нужны методы commitSync() и commitAsync()?

Эти методы используются для фиксации смещений:
* **commitSync()**: синхронно фиксирует последнее смещение, возвращенное poll(). Он будет повторять попытку до тех пор,
  пока не завершится успешно или не столкнется с непроверяемой ошибкой.
* **commitAsync()**: асинхронно фиксирует смещения. Он не повторяет попытку при сбое, что делает его более быстрым,
  но менее надежным, чем commitSync(). Выбор между этими методами зависит от баланса между производительностью и надежностью,
  требуемого приложением.

[к оглавлению](#apache-kafka)

## Другие вопросы

## Для чего нужен идемпотентный продюсер?

Идемпотентный продюсер гарантирует exactly-once гарантию доставки, предотвращая дублирование записей в Kafka в случае 
повторных попыток отправки сообщений. Это важно для поддержания целостности данных и правильности их обработки в системе, 
особенно в распределенных системах, где могут возникать ошибки связи или сбои.

[к оглавлению](#apache-kafka)

## Для чего нужен интерфейс Partitioner?

Интерфейс Partitioner в Producer API определяет в какую партицию топика будет отправлено сообщение. Partitioner по-умолчанию
использует хэш ключа (если он присутствует) для выбора партиции, гарантируя, что сообщения с одним и тем же ключом всегда
отправляются в одну и ту же партицию. Могут быть реализованы пользовательские Partitioner для управления распределением
сообщений по партициям на основе определенной бизнес-логики или характеристик данных.

[к оглавлению](#apache-kafka)

## Для чего нужен Broker log cleaner thread?

Поток очистки журнала в Kafka отвечает за выполнение сжатия журнала. Сжатие журнала - это механизм, при котором Kafka 
удаляет избыточные записи, сохраняя только последнее значение для каждого ключа. Это полезно в тех случаях, когда требуется 
только последнее обновление для данного ключа, например, для обслуживания changelog или состояния БД. Программа очистки журналов 
периодически запускается для сжатия соответствующих партиций.

[к оглавлению](#apache-kafka)

## Для чего нужен Kafka Mirror Maker?

Это инструмент, позволяющий реплицировать данные между кластерами Kafka, потенциально находящихся в разных дата-центрах.
Он работает, потребляя данные из одного кластера и передавая в другой. Можно использовать для создания резервной копии данных, 
объединения данных из нескольких дата-центров в единое хранилище или для переноса данных между кластерами.

[к оглавлению](#apache-kafka)

## Для чего нужна Schema Registry?

Kafka Schema Registry предоставляет RESTful интерфейс для хранения и извлечения схем Avro. Schema Registry используется
совместно с Kafka для обеспечения совместимости схем данных между производителями и потребителями. Это особенно полезно
при разработке моделей данных с течением времени, сохраняя обратную и прямую совместимость.

[к оглавлению](#apache-kafka)

## Для чего нужен Streams DSL?

Kafka Streams DSL предоставляет высокоуровневый API для операций потоковой обработки. Он позволяет разработчикам описывать 
сложную логику обработки, такую как фильтрация, преобразование, агрегирование и объединение потоков данных. DSL абстрагирует 
многие низкоуровневые детали потоковой обработки, упрощая создание и обслуживание приложений потоковой обработки.

[к оглавлению](#apache-kafka)

## Как Kafka обеспечивает версионирование сообщений?

Сама по себе Kafka не обеспечивает версионирование сообщений напрямую, но предоставляет механизмы, позволяющие реализовывать 
управление версиями. Одним из распространенных подходов является включение поля версии в схему сообщения. Для более сложных задач 
управления версиями используются реестры схем (например, Confluent Schema Registry), которые могут управлять изменением схемы и совместимостью.

[к оглавлению](#apache-kafka)

## Как потребители получают сообщения от брокера?

Kafka использует pull-модель для извлечения сообщений. Потребители запрашивают сообщения у брокеров, а не брокеры 
отправляют сообщения потребителям. Это позволяет потребителям контролировать скорость, с которой они получают сообщения. 
Потребители отправляют запросы на получение данных от брокера, указывая топик, партицию и начальное смещение для каждой партиции. 
Брокер отвечает сообщениями с объемом до указанного максимального предела в байтах.

[к оглавлению](#apache-kafka)

## В чем разница между Kafka Streams и Apache Flink?

Kafka Streams и Apache Flink — это два мощных инструмента для обработки потоков данных в режиме реального времени, но
они различаются по архитектуре, возможностям и сценариям применения.

### Сравнение Kafka Streams и Apache Flink

| **Критерий**           | **Kafka Streams**                           | **Apache Flink**                        |
|------------------------|---------------------------------------------|-----------------------------------------|
| **Архитектура**         | Встроенная библиотека, работающая внутри приложения. Зависит от Kafka. | Независимая распределенная система потоковой обработки данных с возможностью интеграции с различными источниками и приемниками данных. |
| **Обработка данных**    | Обрабатывает потоки событий непосредственно из Kafka. Подходит для обработки событий и транзакционных данных с минимальной задержкой. | Поддерживает как потоковую (streaming), так и пакетную (batch) обработку данных. Специализируется на сложной обработке событий с гибкими возможностями управления состоянием. |
| **Зависимость от Kafka**| Построена исключительно вокруг Kafka. Требует Kafka для получения и отправки данных. | Работает с широким спектром источников данных (Kafka, HDFS, базы данных и т. д.). Kafka — лишь один из многих источников. |
| **Установка**          | Легко интегрируется в существующее Java/Scala-приложение как библиотека. Не требует развертывания кластеров. | Требует отдельного кластера для выполнения, что подходит для высокопроизводительных распределенных систем. |
| **Управление состоянием** | Встроенное состояние с использованием RocksDB, также поддержка репликации состояния. | Имеет развитую систему управления состоянием, поддерживает сложные функции восстановления состояния и обработки данных. |
| **Гарантия доставки**   | Поддерживает "at-least-once" и "exactly-once" семантику, когда Kafka настроена соответствующим образом. | Имеет гибкие гарантии доставки: поддержка "exactly-once", "at-least-once" и "at-most-once". |
| **Масштабируемость**    | Масштабируется автоматически вместе с Kafka-партициями. Каждая инстанция потребителя Kafka обрабатывает свою партицию. | Поддерживает масштабирование на уровне задач (task), с более гибкой моделью масштабирования и управления ресурсами. |
| **Обработка событий**   | Подходит для обработки событий с низкой задержкой и транзакционными требованиями. | Специализируется на сложной обработке событий, таких как windowing, агрегирование и работа с изменяющимся состоянием. Поддерживает сложные аналитические операции. |
| **Инструменты и API**   | Легковесная библиотека с простыми API для работы с потоками данных. Основные операции — фильтрация, маппинг, объединение потоков, windowing. | Продвинутая система с богатыми API для сложных вычислений, поддерживающая потоковую и пакетную обработку, обработку событий и контроль сложных бизнес-процессов. |
| **Требования к ресурсам**| Менее ресурсоемка, так как не требует отдельного кластера. Работает в рамках JVM-приложения. | Требует более высоких вычислительных ресурсов, так как выполняется на отдельном кластере и поддерживает высокую степень параллелизма. |

### Когда выбрать Kafka Streams
- Если вы уже используете Kafka и вам нужна легковесная библиотека для обработки данных непосредственно внутри вашего приложения.
- Для сценариев с низкой задержкой, где данные приходят из Kafka и должны быть быстро обработаны с минимальными накладными расходами.
- Если вам нужно встроить обработку потоков данных в существующую Java/Scala программу без необходимости развертывания отдельных кластеров.

### Когда выбрать Apache Flink
- Если вы работаете с потоковой и пакетной обработкой данных, где источники и приемники могут быть не только Kafka, но и другие системы (например, HDFS, базы данных).
- Для сложных задач обработки событий, требующих управления состоянием, временных окон, аналитики и восстановления после сбоев.
- Если ваш проект требует высокой производительности, гибкости, точных гарантий доставки и распределенной обработки в кластере.

### Заключение
- **Kafka Streams** — это идеальный выбор, если ваша инфраструктура уже основана на Kafka, и вам нужна быстрая и легковесная обработка потоков данных.
- **Apache Flink** — это мощный инструмент для сложных аналитических задач, потоковой обработки данных в режиме реального
  времени с поддержкой сложных схем обработки, который предоставляет больше возможностей для работы с разнообразными источниками данных.

[к оглавлению](#apache-kafka)

## В чем разница между Kafka Consumer и Kafka Stream?

**Kafka Consumer** - это клиент, который читает данные из топика и производит некоторую обработку. Обычно используется для
простых сценариев получения данных. **Kafka Stream**, с другой стороны, более подвинутый клиент, который может потреблять,
обрабатывать и класть данные обратно в Kafka. Он предоставляет DSL для сложных операций потоковой обработки, таких как
фильтрация, преобразование, агрегирование и объединение потоков.

[к оглавлению](#apache-kafka)

## В чем разница между Kafka и Flume?

**Apache Kafka** и **Apache Flume** — это два популярных инструмента для обработки и передачи данных, однако они имеют 
разные цели и архитектуры. Вот основные различия между ними:

### 1. **Назначение и использование**
- **Kafka**: Это распределенная платформа для потоковой передачи данных, которая обеспечивает высокую пропускную способность 
и низкую задержку для обработки больших объемов данных. Kafka используется для создания стриминговых приложений и обработки 
данных в реальном времени. Она может быть использована для передачи логов, событий, метрик и других данных, требующих 
высокой доступности и масштабируемости.
- **Flume**: Это распределенная система для сбора, агрегации и передачи логов и событий. Flume обычно используется для 
доставки логов с серверов в HDFS, HBase или другие системы хранения данных. Его основное предназначение — это сбор данных 
из различных источников (например, лог-файлов) и передача их в системы хранения или аналитики.

### 2. **Архитектура**
- **Kafka**: В Kafka данные отправляются в топики и партиции, которые могут быть независимо прочитаны несколькими потребителями. 
Kafka ориентирована на высокую пропускную способность и масштабируемость. Это решает задачу обработки потоковых данных и 
событий в реальном времени.
- **Flume**: Flume состоит из **источников (sources)**, **каналов (channels)** и **приемников (sinks)**. Источник получает 
данные, канал их буферизует, а приемник отправляет их в конечную систему. Flume использует систему "event-based" и часто 
применяется для сбора логов.

### 3. **Хранение данных**
- **Kafka**: Kafka сохраняет сообщения на диске в течение длительного времени (по умолчанию — до 7 дней) в топиках. 
Потребители могут читать данные в любой момент времени, и Kafka поддерживает концепцию **сохранения и ретрансляции данных**.
- **Flume**: Flume не имеет встроенного механизма долговременного хранения. Он просто передает данные в назначенные места 
хранения (например, HDFS). Данные в Flume не сохраняются долго, и если система хранения не доступна, они теряются.

### 4. **Производительность**
- **Kafka**: Kafka предназначен для работы с высокими объемами данных. Он поддерживает масштабируемость как по производителям, 
так и по потребителям, и может обрабатывать миллионы сообщений в секунду с минимальной задержкой.
- **Flume**: Flume может быть менее масштабируемым по сравнению с Kafka и больше ориентирован на сбор логов и событий 
с различных источников. Хотя Flume тоже может обрабатывать большие объемы данных, он не предназначен для работы с 
такими большими потоками, как Kafka.

### 5. **Использование и кейсы**
- **Kafka**: Используется для стриминга данных, аналитики в реальном времени, интеграции различных систем, работы с 
большими данными и построения событийных приложений.
- **Flume**: Используется для сбора, агрегации и передачи логов и событий в системы хранения, такие как HDFS, HBase, 
или внешние системы. Это идеальный выбор для организации потоков логирования и мониторинга.

### 6. **Поддержка и интеграция**
- **Kafka**: Kafka поддерживает широкий спектр интеграций и может быть использован с различными системами для построения 
распределенных приложений и аналитических решений.
- **Flume**: Flume ориентирован на интеграцию с Hadoop-экосистемой, и основное его использование — это интеграция с HDFS, 
HBase и другими хранилищами данных в этой экосистеме.

### 7. **Потребительская модель**
- **Kafka**: Kafka поддерживает много потребителей, которые могут читать из одного и того же топика независимо, а также 
возможность **повторного прочтения данных**.
- **Flume**: Flume имеет фиксированную схему доставки данных и не поддерживает такую гибкость, как Kafka в части потребителей и обработки.

### 8. **Гарантии доставки**
- **Kafka**: Kafka поддерживает **гарантии доставки** с различными уровнями подтверждения (acknowledgment), а также может 
обеспечивать **доставку сообщений точно один раз** (exactly-once semantics).
- **Flume**: Flume обеспечивает базовые гарантии доставки, но они менее строгие, чем у Kafka, и больше ориентированы на 
устойчивость к сбоям, а не на гарантированную доставку.

[к оглавлению](#apache-kafka)

## В чем разница между Kafka и RabbitMQ?

**RabbitMQ** и **Apache Kafka** — это две популярные системы обмена сообщениями, каждая из которых имеет свои особенности 
и используется для разных типов приложений. Вот основные различия между ними:

### 1. **Архитектура**
- **RabbitMQ** использует **очереди сообщений**. Сообщения отправляются в очередь, и один потребитель извлекает сообщение 
из очереди для обработки.
- **Apache Kafka** использует **топики и партиции**. Сообщения отправляются в топики, которые могут быть разделены на 
партиции, и несколько потребителей могут читать эти сообщения в любом порядке. Kafka ориентирован на большие потоки данных и масштабируемость.

### 2. **Модель доставки сообщений**
- **RabbitMQ**: Сообщения передаются в очереди, и каждый потребитель получает одно сообщение. Сообщения могут быть 
подтверждены (acknowledged) или отклонены (rejected). RabbitMQ гарантирует, что сообщение будет доставлено хотя бы одному потребителю.
- **Kafka**: Сообщения сохраняются в топиках на длительный срок, и потребители могут читать их в любом порядке. Kafka 
гарантирует доставку сообщений всем потребителям, если они подписаны на топик, и может позволить многократное чтение старых сообщений.

### 3. **Гарантии доставки**
- **RabbitMQ**: Предоставляет подтверждения доставки и может повторно отправить сообщение, если потребитель не подтвердил 
его получение. Можно настроить разные уровни надежности (например, за счет использования подтверждений или транзакций).
- **Kafka**: Сообщения сохраняются на диске, что позволяет потребителям считывать их в любое время. Kafka гарантирует 
доставку сообщений при определенной конфигурации репликации и сохранения.

### 4. **Производительность и масштабируемость**
- **RabbitMQ**: Лучше подходит для небольших и средних систем, где требуется высокая надежность и гарантированная доставка. 
Он поддерживает **горизонтальное масштабирование**, но требует дополнительных усилий для настройки и управления.
- **Kafka**: Отличается высокой **производительностью** и возможностью обработки больших объемов данных. Kafka легко 
масштабируется за счет **партиционирования** и репликации данных.

### 5. **Потребительская модель**
- **RabbitMQ**: Один потребитель получает одно сообщение. Если потребитель не успевает обработать сообщение, оно может быть повторно отправлено.
- **Kafka**: Потребители могут читать сообщения независимо друг от друга. Kafka сохраняет все сообщения в топиках, 
и потребители могут читать их в любое время. Kafka также поддерживает концепцию **групп потребителей**, где каждый 
потребитель группы обрабатывает разные партиции.

### 6. **Использование и кейсы**
- **RabbitMQ**: Идеален для обработки запросов и ответов, распределенных приложений, микросервисов с гарантией доставки, 
бизнес-процессов с очередями задач.
- **Kafka**: Используется для обработки потоков данных, интеграции с большими данными, записи журналов, мониторинга, 
обработки событий в реальном времени и сохранения больших объемов данных для последующего анализа.

### 7. **Производители и потребители**
- **RabbitMQ**: Один производитель отправляет сообщения в очередь, и несколько потребителей могут обрабатывать эти сообщения.
- **Kafka**: Множество производителей могут отправлять сообщения в топики, и несколько потребителей могут читать их 
одновременно, поддерживая масштабируемость.

### 8. **Сообщения и хранение**
- **RabbitMQ**: Сообщения удаляются из очереди после их обработки потребителем. Хранение сообщений обычно краткосрочное.
- **Kafka**: Сообщения сохраняются на диске в топиках до тех пор, пока не истечет срок хранения (по конфигурации). 
Это позволяет повторно читать данные.

[к оглавлению](#apache-kafka)
