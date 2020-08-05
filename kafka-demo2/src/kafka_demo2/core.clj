(ns kafka-demo2.core
  (:import [org.apache.kafka.common.serialization StringSerializer]
           [org.apache.kafka.clients.producer KafkaProducer ProducerRecord])
  (:gen-class))

(def config
  {"bootstrap.servers" "127.0.0.1:9092"
   "key.serializer" StringSerializer
   "value.serializer" StringSerializer})

(defn publish [str]
  (let [producer (KafkaProducer. config)
        record (ProducerRecord. "yhnam-topic" str)]
    (try
      (.send producer record)
      (finally (.close producer)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (publish "A"))
