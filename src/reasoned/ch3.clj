(ns reasoned.ch3
  (:require
   [clojure.core.logic :as logic :refer [run run* == u# s# fresh conde firsto lcons llist]]
   [reasoned.ch2 :refer [nullo cdro pairo]])
  (:refer-clojure :exclude [==]))

(defn listo [l]
  (conde
   ((nullo l) s#)
   ((pairo l)
    (fresh [d]
      (cdro l d)
      (listo d)))
   [s# u#]))

;; > The First Commandment
;; > To transform a function whose value is a Boolean into a function whose value is a goal, replace cond with conde and unnest each question and answer. Unnest the answer #t (or #f) by replacing it with #s (or #u).

(run 1 [x]
  (listo (llist :a :b :c x)))
