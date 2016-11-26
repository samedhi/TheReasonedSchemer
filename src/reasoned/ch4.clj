(ns reasoned.ch4
  (:require
   [clojure.core.logic :as logic :refer [emptyo nilo run run* == u# s# fresh conde firsto lcons llist resto]]
   [reasoned.ch2 :refer [nullo cdro pairo conso caro]])
  (:refer-clojure :exclude [==]))

(defn memo [x l out]
  (conde
   [(firsto l x) (== l out)]
   [s# (fresh [d]
         (resto l d)
         (memo x d out))]))

;;> The Second Commandment
;;< To transform a function whose value is not a Boolean into a function whose value is a goal
;;< add an extra argument to hold its value, replace cond with conde, and unnest each question
;;< and answer.

(run 1 [out]
  (memo :tofu [:a :b :tofu :d :tofu :e] out))

(run 1 [out]
  (fresh [x]
    (memo :tofu [:a :b x :d :tofu :e] out)))

(run* [r]
  (memo r [:a :b :tofu :d :tofu :e] [:tofu :d :tofu :e]))

(run* [q]
  (memo :tofu [:tofu :e] [:tofu :e])
  (== true q))

(run* [q]
  (memo :tofu [:tofu :e] [:tofu])
  (== true q))

(run* [x]
  (memo :tofu [:tofu :e] [:peas x]))

(run* [out]
  (fresh [x]
    (memo :tofu [:a :b x :d :tofu] out)))

(run 5 [z]
  (fresh [u]
    (memo :tofu (llist :a :b :tofu :d :tofu :e z) u)))

(defn rembero [x l out]
  (conde
   [(emptyo l) (== [] out)]
   [(firsto l x) (resto l out)]
   [s#
    (fresh [res a d]
      (conso a d l)
      (conso a res out)
      (rembero x d res))]))

(run 1 [out]
  (fresh [y]
    (rembero :peas [:a :b y :d :peas :e] out)))


(run* [out]
  (fresh [y z]
    (rembero y [:a :b y :d z :e] out)))

(run* [r]
  (fresh [y z]
    (rembero y [y :d z :e] [y :d :e])
    (== [y z] r)))

(run 13 [w]
  (fresh [y z out]
    (rembero y (llist :a :b y :d z w) out)))

(defn surpriseo [s]
  (rembero s [:a :b :c] [:a :b :c]))

(run* [r]
  (== :d r)
  (surpriseo r))

(run* [r]
  (surpriseo r))

(run* [r]
  (== :b r)
  (surpriseo r))
