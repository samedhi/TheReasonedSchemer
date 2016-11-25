(ns reasoned.ch2
  (:require
   [clojure.core.logic :as logic :refer [run* == u# s# fresh conde firsto resto lcons lcons? llist]])
  (:refer-clojure :exclude [==]))

(run* [r]
  (firsto (list :a :c :o :r :n) r))

(run* [r]
  (fresh [x y]
    (firsto (list r y) x)
    (== :pear x)))

;;> Why is lcons neccessary in Clojure?
;;< Because clojure does not have improper list, so you cannot cons ANY value.

(defn conso [a d p]
  (== (lcons a d) p))

(run* [r]
  (fresh [x y]
    (firsto (llist :grape :raising :pear) x)
    (firsto [[:a] [:b] [:c]] y)
    (conso x y r)))

(defn caro [p a]
  (fresh [d]
    (conso a d p)))

(defn cdro [p d]
  (fresh [a]
    (conso a d p)))

(run* [r]
  (fresh [v]
    (cdro (llist :a :c :o :r :n) v)
    (caro v r)))

(run* [r]
  (fresh [x y]
    (resto [:grape :raisin :pear] x)
    (firsto [[:a] [:b] [:c]] y)
    (conso x y r)))

(run* [q]
  (resto [:a :c :o :r :n] [:c :o :r :n])
  (== true q))

(run* [x]
  (resto [:c :o :r :n] [x :r :n]))

(run* [l]
  (fresh [x]
    (resto l [:c :o :r :n])
    (firsto l x)
    (== :a x)))

(run* [l]
  (conso [:a :b :c] [:d :e] l))

(run* [r]
  (fresh [x y z]
    (== [:e :a :d x] r)
    (conso y [:a z :c] r)))

(run* [x]
  (conso x [:a x :c] [:d :a x :c]))

(run* [l]
  (fresh [x]
    (== [:d :a x :c] l)
    (conso x [:a x :c] l)))

(run* [l]
  (fresh [x]
    (conso x [:a x :c] l)
    (== [:d :a x :c] l)))

(run* [l]
  (fresh [d x y w s]
    (conso w [:a :n :s] s)
    (resto l s)
    (firsto l x)
    (== :b x)
    (cdro l d)
    (caro d y)
    (== :e y)))

(defn nullo [l]
  (== l '()))

(run* [r]
  (nullo r))

(run* [r]
  (nullo [])
  (== true r))

(run* [r]
  (fresh [x y]
    (caro [r y] x)
    (== :pear x)))

(defn eqo [x y]
  (== x y))

(defn pair? [x]
  (or (lcons? x)
      (and (coll? x)
           (seq x))))

(defn pairo [p]
  (fresh [a d]
    (conso a d p)))

(run* [q]
  (pairo (lcons q q)))

(run* [q]
  (pairo (lcons q q))
  (== true q))

(run* [q]
  (pairo :pear)
  (== true q))

(run* [q]
  (pairo q))

(run* [r]
  (pairo (lcons r :pear)))
