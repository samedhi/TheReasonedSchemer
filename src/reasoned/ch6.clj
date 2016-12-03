(ns reasoned.ch6
  (:require
   [clojure.core.logic :as logic :refer [emptyo nilo run run* == u# s# fresh conde firsto lcons llist resto all]]
   [reasoned.ch2 :refer [nullo cdro pairo conso caro]]
   [reasoned.ch1 :refer [teacupo]])
  (:refer-clojure :exclude [==]))

(defn anyo [g]
  (conde
   [g s#]
   [s# (anyo g)]))

(def nevero (anyo u#))

;; TODO: Fails
;; (run 1 [q]
;;   nevero
;;   (== true q))

(run 1 [q]
  u#
  nevero)

(def alwayso (anyo s#))

(run 1 [q]
  alwayso
  (== true q))

(defn salo [g]
  (conde
   [s# s#]
   [s# g]))

;;< What does `salo` stand for?
;;> Succeeds At Least Once

(run 1 [q]
  (salo alwayso)
  (== true q))

(run 1 [q]
  (salo nevero)
  (== true q))

(run 1 [q]
  (conde
   [(== false q) alwayso]
   [s# (== true q)])
  (== true q))

(run 5 [q]
  (conde
   [(== false q) alwayso]
   [s# (anyo (== true q))])
  (== true q))

;;> The law of condi
;;> condi behaves liek conde, except that its values are interleaved.

;; TODO: This does not agree
(run 5 [r]
  (conde
   [(teacupo r) s#]
   [(== false r) s#]))

(run 5 [q]
  (conde
   [(== false q) alwayso]
   [(== true q) alwayso]))

(run 5 [q]
  (conde
   [(== false q) alwayso]
   [(== true q) alwayso])
  (== true q))

;;TODO: This shoudln't work, but it does?
(run 5 [q]
  (conde
   (alwayso s#)
   [s# nevero])
  (== true q))

;;> What does the i stand for in condi?
;;< interleave

;; TODO: Stopped at frame 28
