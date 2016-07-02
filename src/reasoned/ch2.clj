(ns reasoned.ch2
  (:require
   [clojure.core.logic :as logic :refer [run* == u# s# fresh conde firsto lcons]])
  (:refer-clojure :exclude [==]))

(run* [r]
  (fresh [x y]
    (firsto [r y] x)
    (== :pear x)))

(defn conso [a d p]
  (== (lcons a d) p))

(defn caro [p a]
  (fresh [d]
    (conso a d p)))

(defn cdro [p d]
  (fresh [a]
    (conso a d p)))

(defn nullo [l]
  (== l []))

(defn pairo [p]
  (fresh [a d]
    (conso a d p)))

(defn eqo [x y]
  (== x y))

(run* [r]
  (fresh [x y]
    (caro [r y] x)
    (== :pear x)))
