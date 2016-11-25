(ns reasoned.ch1
  (:require
   [clojure.core.logic :as logic :refer [run run* == u# s# fresh conde]])
  (:refer-clojure :exclude [==]))

;; > What is the name of s#?
;; < succeed, because it succeeds

;; > What is u#?
;; < fail, it is a goal that fails.

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
  u#
  (== :corn q))

(run* [q]
  s#
  (== false q))

(run* [q]
  (fresh [x]
    (== true x)
    (== true q)))

;; > What does the value of x represent?
(run* [x]
  s#)
;; < This represents a fresh variable

;; > When is a variable fresh?
;; < When it has no association.

(run* [x]
  (let [x false]
    (fresh [x]
      (== true x))))

;; > What observation can be made comparing this
(run* [r]
  (fresh [x]
    (let [y x]
      (fresh [x]
        (== (list x y) r)))))
;; > and the following evaluation?
(run* [r]
  (fresh [x]
    (let [y x]
      (fresh [x]
        (== (list x y) r)))))
;; < The value associated is the same, this is because r's values are reified
;; < in the order in which they appear in the list.

(run* [q]
  (== false q)
  (== true q))

(run* [q]
  (== false q)
  (== false q))

;; > Do x and q co-refer or share?
(run* [q]
  (let [x q]
    (== true x)))
;; < No, they are the same variable.

;; > The law of fresh
;; < If x is fresh, then (== v x) succeeds and associates x with v.

;; > The law of ==
;; < (== v w) is the same as (== w v)

;; > We can say that x and r ____ or _____?
(run* [r]
  (fresh [x]
    (== x r)))
;; < co-refer or share

(run* [q]
  (fresh [x]
    (== true x)
    (== x q)))

(run* [q]
  (fresh [x]
    (== x q)
    (== true x)))

;; > Are q and x different variables in
(run* [q]
  (fresh [x]
    (== true x)
    (== x q)))
;; Yes, the are different variables that co-refer

;; > What does this demonstrate?
(run* [q]
  (fresh [x]
    (== (= x q) q)))
;; < That x and q are different variables (not equal)

;; > What does this demonstrate?
(run* [q]
  (let [x q]
    (fresh [q]
      (== (= x q) x))))
;; < That every variable introduced by run or fresh is different from every other variable
;; < introduced by fresh or run.

(run* [x]
  (conde
   ((== :olive x) s#)
   ((== :oil x) s#)
   (s# u#)))

;; > The law of conde
;; < To get more values from conde, pretend that the successful conde line has failed,
;; < refreshing all variables that got an association from that line.

;; > What does the e stand for in conde?
;; < It stands for every line, since every line can succeed.

(run 1 [x]
  (conde
   ((== :olive x) s#)
   ((== :oil x) s#)
   (s# u#)))

(run* [x]
  (conde
   ((== :virgin x) u#)
   ((== :olive x) s#)
   (s# s#)
   ((== :oil x) s#)
   (s# u#)))

(run 2 [x]
  (conde
   ((== :extra x) s#)
   ((== :virgin x) u#)
   ((== :olive x) s#)
   ((== :oil x) s#)
   (s# u#)))

(run 2 [r]
  (fresh [x y]
    (== :split x)
    (== :pea y)
    (== (list x y) r)))

(run* [r]
  (fresh [x y]
    (conde
     ((== :split x) (== :pea y))
     ((== :navy x) (== :bean y)))
    (== (list x y) r)))

;; > Given
(defn teacupo [x]
  (conde
   ((== :tea x) s#)
   ((== :cup x) s#)))
;; > What is the value of
(run* [r]
  (teacupo r))

;; > Given
(defn teacupo [x]
  (conde
   ((== :tea x) s#)
   ((== :cup x) s#)))
;; > What is the value of
(run* [r]
  (fresh [x y]
    (conde
     ((teacupo x) (== true y) s#)
     ((== false x) (== true y)))
    (== (list x y) r)))

(run* [r]
  (fresh [x y z]
    (conde
     ((== x y) (fresh [x] (== z x)))
     ((fresh [x] (== y x)) (== z x)))
    (== (list y z) r)))

(run* [r]
  (fresh [x y z]
    (conde
     ((== x y) (fresh [x] (== z x)))
     ((fresh [x] (== y x)) (== z x)))
    (== false x)
    (== (list y z) r)))

;; > What does this demonstrate about a and b?
(run* [q]
  (let [a (== true q)
        b (== false q)]
    b))
;; < It demonstrates that a and b are expressions, whose value is a goal.

(run* [q]
  (let [a (== true q)
        b (fresh [x]
            (== x q)
            (== false x))
        c (conde
           ((== true q) s#)
           (s# (== false q)))]
    b))
