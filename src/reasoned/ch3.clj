(ns reasoned.ch3
  (:require
   [clojure.core.logic :as logic :refer [emptyo nilo run run* == u# s# fresh conde firsto lcons llist resto]]
   [reasoned.ch2 :refer [nullo cdro pairo conso caro]])
  (:refer-clojure :exclude [==]))

(defn listo [l]
  (conde
   ((emptyo l) s#)
   ((pairo l)
    (fresh [d]
      (resto l d)
      (listo d)))
   [s# u#]))

(run* [r]
  (listo nil)
  (== true r))

(run* [r]
  (listo [])
  (== true r))

(run* [r]
  (listo [:pear])
  (== true r))

;;< The First Commandment
;;> To transform a function whose value is a Boolean into a function whose value is a goal, replace cond with conde and unnest each question and answer. Unnest the answer #t (or #f) by replacing it with #s (or #u).

(run* [x]
  (listo [:a :b x :d]))

(run 1 [x]
  (listo (llist :a :b :c x)))

;; TODO: This one diverges from expected, ask if this is due to conde/condi?
(run 5 [x]
  (listo (llist :a :b :c x)))

(defn lol? [l]
  (cond
    (empty? l) true
    (sequential? (first l)) (lol? (rest l))
    :else false))

(defn lolo [l]
  (conde
   [(emptyo l) s#]
   [(fresh [a]
     (firsto l a)
      (listo a))
    (fresh [d]
     (resto l d)
      (lolo d))]
   [s# u#]))

(run 1 [l]
  (lolo l))

(run* [q]
  (fresh [x y]
    (lolo [[:a :b] [x :c] [:d y]])
    (== true q)))

(run 1 [q]
  (fresh [x]
    (lolo (lcons [:a :b] x))
    (== true q)))

(run 1 [x]
  (lolo (llist [:a :b] [:c :d] x)))

(run 5 [x]
  (lolo (llist [:a :b] [:c :d] x)))

(defn twinso [s]
  (fresh [x y]
    (conso x y s)
    (conso x [] y)))

(run* [q]
  (twinso [:tofu :tofu])
  (== true q))

(run* [z]
  (twinso [z :tofu]))

;;< Define `twinso` without using `conso`
;;< `(defn twinso [s] ...)
;;>
(defn twinso [s]
  (fresh [x]
    (== [x x] s)))

(defn loto [l]
  (conde
   [(emptyo l) s#]
   [(fresh [a]
      (firsto l a)
      (twinso a))
    (fresh [d]
      (resto l d)
      (loto d))]
   [s# u#]))

(run 1 [z]
  (loto (lcons [:g :g] z)))

(run 3 [out]
  (fresh [w x y z]
    (== (llist [:g :g] [:e w] [x y] z) out)
    (loto out)))

(defn listof [predo l]
  (conde
   [(emptyo l) s#]
   [(fresh [a]
      (firsto l a)
      (predo a))
    (fresh [d]
      (resto l d)
      (listof predo d))]
   [s# u#]))

;; TODO: I don't quite get why this terminates without the (emptyo) portion?
(defn membero [x l]
  (conde
   [(firsto l x) s#]
   [s#
    (fresh [r]
      (resto l r)
      (membero x r))]))

(run* [q]
  (membero :olive [:virgin :olive :oil])
  (== true q))

(run* [y]
  (membero y [:hummus :with :pita]))

(run* [y]
  (membero y []))

;;> Using `run*` define a function called identity (use `membero`)
;;> `(defn identity [l] ...)`
;;<
(defn midentity [l]
  (run* [y]
    (membero y l)))

(run* [x]
  (membero :e [:pasta x :fagioli]))

(run 1 [x]
  (membero :e [:pasta x :e :fagioli]))

(run 1 [x]
  (membero :e [:pasta :e x :fagioli]))

(run* [r]
  (fresh [x y]
    (membero :e [:pasta x :fagioli y])
    (== [x y] r)))

(run 1 [l]
  (membero :tofu l))

(run 3 [l]
  (membero :tofu l))

(defn pmembero [x l]
  (conde
   [(firsto l x) (resto l [])]
   [(firsto l x)
    (fresh [a d]
      (resto l (lcons a d)))]
   [s#
    (fresh [r]
      (resto l r)
      (membero x r))]))

(run* [q]
  (pmembero :tofu [:a :b :tofu :d :tofu])
  (== true q))

;; TODO: Could not figure out how to restructure pmember to get proper list
(run 5 [l]
  (pmembero :tofu l))

(defn first-value [l]
  (run 1 [y]
    (membero y l)))

(first-value [:pasta :e :fagioli])

(defn memberrevo [x l]
  (conde
   [s# (fresh [d]
         (resto l d)
         (memberrevo x d))]
   [s# (caro l x)]))

;; TODO: What does the fact that this does not work mean?
(run* [x]
  (memberrevo x [:pasta :e :fagioli]))
