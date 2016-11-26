(ns reasoned.ch5
  (:require
   [clojure.core.logic :as logic :refer [emptyo nilo run run* == u# s# fresh conde firsto lcons llist resto]]
   [reasoned.ch2 :refer [nullo cdro pairo conso caro]])
  (:refer-clojure :exclude [==]))

(defn appendo [l s out]
  (conde
   [(emptyo l) (== out s)]
   [s#
    (fresh [a d res]
      (conso a d l)
      (conso a res out)
      (appendo d s res))]))

(run* [x]
  (appendo [:cake] [:taste :yummy] x))

(run* [x]
  (fresh [y]
    (appendo [:cake :with :ice y] [:taste :yummy] x)))

(run* [x]
  (fresh [y]
    (appendo [:cake :with :ice] y x)))

(run 1 [x]
  (fresh [y]
    (appendo (llist :cake :with :ice y) [:d :t] x)))

(run 5 [x]
  (fresh [y]
    (appendo (llist :cake :with :ice y) [:d :t] x)))

;; TODO: This fails
;; (run* [x]
;;   (fresh [z]
;;     (appendo (llist :cake :with :ice x) (llist :d :t z) x)))

;; TODO: This fails
;; (run 6 [x]
;;   (fresh [y]
;;     (appendo x y (llist :cake :with :ice :d :t))))

;; TODO: Stopped at frame 38
