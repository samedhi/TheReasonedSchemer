(ns reasoned.ch1
  (:require
   [clojure.core.logic :as logic :refer [run* == u# s# fresh conde]])
  (:refer-clojure :exclude [==]))



(run* [q]
  u#)

(run* [q]
  (== true q))

(run* [q]
  u#
  (== true q))

(run* [q]
  s#
  (== true q))

(run* [q]
  s#
  (== :corn q))

(run* [q]
  (fresh [x]
    (== true x)
    (== true q)))

(run* [x]
  s#)


;; The following is really interesting, as it demonstrates that the two occurences
;; of _0 represent different variables.
(run* [r]
  (fresh [x y z]
    (conde
     [(== y x) (fresh [x] (== z x))]
     [(fresh [x] (== y x)) (== z x)]
     [s# u#])
    (== false x)
    (== (cons y (cons z '())) r)))

;; > The law of fresh
;; > If x is fresh, then (== v x) succeeds and associates x with v.

;; > The law of ==
;; > (== v w) is the same as (== w v)

;; > The law of conde
;; > To get more values from conde, pretned that the successful conde line has failed,
;; > refreshing all variables that got an association from that line.
