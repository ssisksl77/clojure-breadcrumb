(defproject kafka-demo2 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.apache.kafka/kafka-clients "2.5.0"] ; Kafka
                 [com.fzakaria/slf4j-timbre "0.2"] ; for SLF4J warniing
                 ]
  :repl-options {:init-ns kafka-demo2.core} ; repl init
  :main ^:skip-aot kafka-demo2.core
  :target-path "target/%s"
  :plugins [[cider/cider-nrepl "0.25.3"]] ; for nREPL works fine.
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
