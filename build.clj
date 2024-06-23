(ns build
    (:refer-clojure :exclude [test])
    (:require [org.corfield.build :as bb]))

(def lib 'net.clojars.insta-mem/core)
(def version "0.1.0-SNAPSHOT")
(def main 'retromiks.core)

(defn test "Run the tests." [opts]
      (bb/run-tests opts))

(defn ci "Run the CI pipeline of tests (and build the uberjar)." [opts]
      (-> opts
          (assoc :lib lib :version version :main main)
          ; (bb/run-tests)
          (bb/clean)
          (bb/uber)))

; # run the tests using default options:
; bb/run-tests
; # clean the target folder:
; bb/clean

; # build a library JAR:
; bb/jar {:lib myname/mylib :version '"1.0.123"}

; # deploy that library to Clojars:
; bb/deploy {:lib myname/mylib :version '"1.0.123"}

; # build an application uberjar:
; bb/uber {:lib myname/myapp :main my.app.core}
