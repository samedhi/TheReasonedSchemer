(ns reasoned.ch7
  (:require
   [clojure.core.logic :as logic :refer [emptyo nilo run run* == u# s# fresh conde firsto lcons llist resto all]]
   [reasoned.ch2 :refer [nullo cdro pairo conso caro]]
   [reasoned.ch1 :refer [teacupo]])
  (:refer-clojure :exclude [==]))

(defn bit-nando [x y r]
  (conde
   [(== 0 x) (== 0 y) (== 1 r)]
   [(== 0 x) (== 1 y) (== 1 r)]
   [(== 1 x) (== 0 y) (== 1 r)]
   [(== 1 x) (== 1 y) (== 0 r)]))

(defn bit-noto [x r]
  (bit-nando x x r))

(defn bit-xoro [x y r]
  (conde
   [(== 0 x) (== 0 y) (== 0 r)]
   [(== 0 x) (== 1 y) (== 1 r)]
   [(== 1 x) (== 0 y) (== 1 r)]
   [(== 1 x) (== 1 y) (== 0 r)]))

(defn bit-xoro-using-nando [x y r]
  (fresh [s t u]
    (bit-nando x y s)
    (bit-nando x s t)
    (bit-nando s y u)
    (bit-nando t u r)))

(run* [s]
  (fresh [x y]
    (bit-xoro x y 0)
    (== [x y] s)))

(run* [s]
  (fresh [x y]
    (bit-xoro x y 1)
    (== [x y] s)))

(run* [s]
  (fresh [x y r]
    (bit-xoro x y r)
    (== [x y r] s)))

(defn bit-ando [x y r]
  (conde
   [(== 0 x) (== 0 y) (== 0 r)]
   [(== 0 x) (== 1 y) (== 0 r)]
   [(== 1 x) (== 0 y) (== 0 r)]
   [(== 1 x) (== 1 y) (== 1 r)]))

(defn bit-ando-using-nando&noto [x y r]
  (fresh [s]
    (bit-nando x y s)
    (bit-noto s r)))

(run* [s]
  (fresh [x y]
    (bit-ando x y 1)
    (== [x y] s)))

(defn half-addero-using-xoro&ando [x y r c]
  (all
   (bit-xoro x y r)
   (bit-ando x y c)))

(defn half-addero [x y r c]
  (conde
   [(== 0 x) (== 0 y) (== 0 r) (== 0 c)]
   [(== 1 x) (== 0 y) (== 1 r) (== 0 c)]
   [(== 0 x) (== 1 y) (== 1 r) (== 0 c)]
   [(== 1 x) (== 1 y) (== 0 r) (== 1 c)]))

;;> Describe the half-addero
;;< Given the bits x, y, r, and c; half-addero satisfies x + y = r + 2 * c

(defn full-addero-using-half-addero&xoro [b x y r c]
  (fresh [w xy wz]
    (half-addero x y w xy)
    (half-addero w b r wz)
    (bit-xoro xy wz c)))

(defn full-addero [b x y r c]
  (conde
   [(== 0 b) (== 0 x) (== 0 y) (== 0 r) (== 0 c)]
   [(== 1 b) (== 0 x) (== 0 y) (== 1 r) (== 0 c)]
   [(== 0 b) (== 1 x) (== 0 y) (== 1 r) (== 0 c)]
   [(== 1 b) (== 1 x) (== 0 y) (== 0 r) (== 1 c)]
   [(== 0 b) (== 0 x) (== 1 y) (== 1 r) (== 0 c)]
   [(== 1 b) (== 0 x) (== 1 y) (== 0 r) (== 1 c)]
   [(== 0 b) (== 1 x) (== 1 y) (== 0 r) (== 1 c)]
   [(== 1 b) (== 1 x) (== 1 y) (== 1 r) (== 1 c)]))

(run* [s]
  (fresh [r c]
    (full-addero 1 1 1 r c)
    (== [r c] s)))

(run* [s]
  (fresh [b x y r c]
    (full-addero b x y r c)
    (== [b x y r c] s)))

;;> Describe full-addero
;;< Given the bits b, x, y, r, and c; full-addero satisfies x + x + y = r + 2c

;;> Which list represents the number 0?
;;< ()

;;> Is there any number that (0) represents?
;;< No, each number is reprsented uniquely, therefore (0) cannot also represent the number 0.

;;> What number is represented by (0 1 1)
;;< 6 <= (0 + 2 + 4) <= (0*2^0 + 1*2^1 + 1*2^2)

;;> How do we represent 19?
;;< (1 1 0 0 1)

;;> Does every list representing a number end in 1?
;;< Yes, except for 0, which is represented by ()

;;> In (0 . n), what value can n NOT be?
;;< n cannot be (), as (0) is not valid.

;;> What does prepending a 0 to a number list do to the number?
;;< It doubles it, except for (), which would be invalid.


;;> What does prepending a 1 to a number list do to the number?
;;< It doubles it and add 1.

(defn exp [x n]
  (reduce * (repeat n x)))

(defn build-num [n]
  (loop [ls '()
         n n
         [x & xs :as xss] (->> (range) (map #(exp 2 %)) (take-while #(<= % n)) reverse)]
    (if (empty? xss)
      ls
      (if (<= x n)
        (recur (cons 1 ls) (- n x) xs)
        (recur (cons 0 ls) n xs)))))

(assert (= (build-num 0) []))
(assert (= (build-num 1) [1]))
(assert (= (build-num 5) [1 0 1]))
(assert (= (build-num 16) [0 0 0 0 1]))
(assert (= (build-num 19) [1 1 0 0 1]))

;; Ugh, I feel dumb for not having hit on the fact that I could just check
;; even and odd.... Here is the book implementation

(defn build-num [n]
  (cond
    (odd? n)
    (cons 1 (-> (- n 1) (/ 2) build-num))
    (and (not (zero? n)) (even? n))
    (cons 0 (build-num (/ n 2)))
    :else []))

(assert (= (build-num 0) []))
(assert (= (build-num 1) [1]))
(assert (= (build-num 5) [1 0 1]))
(assert (= (build-num 16) [0 0 0 0 1]))
(assert (= (build-num 19) [1 1 0 0 1]))

;;> What is it called when you can rearange cond lines in any order?
;;< It is called the 'non-overlapping property'.

;;> What does it mean if you have a non-overlapping property in a cond?
;;< It means that only one cond question is true.

;;> Which numbers are represented by (x x 1)?
;;< 4 & 7 (001) & (1 1 1)

;;> Which numbers are represented by (x 0 y 1)?
;;< 8, 9, 12, 13

;;> Which numbers are represented by (x 0 y z)?
;;< 8, 9, 12, 13

;;> Which number is represented by (x)?
;;< 1

;;> Which numbers are represented by (1 . z), where z is all listnumbers?
;;< All the odd numbers

;;> Which numbers are represented by (0 . z), where z is all non-zero listnumbers?
;;< All the even numbers

;;> Which numbers are represented by (0 0 . z), where z is all non-zero listnumbers?
;;< Every other even number, starting with 4.

;;> Which numbers are represented by (0 1 . z), where z is all listnumbers?
;;< Every other even number, starting with 2.

;;> Which numbers are represented by (1 0 . z), where z is all listnumbers?
;;< Every other odd number, starting with 5.

;;> Which numbers are represented by (1 0 y . z), where z is all listnumbers?
;;< Every other odd number, starting with 5.

(defn poso [n]
  (fresh [a d]
    (== (llist a d) n)))

(run* [q]
  (poso [1])
  (== true q))

(run* [q]
  (poso [])
  (== true q))

(run* [r]
  (poso r))

(defn >1o [n]
  (fresh [a ad dd]
    (== (llist a ad dd) n)))

(run* [q]
  (poso [1])
  (== true q))

(run* [q]
  (poso [])
  (== true q))

(run* [r]
  (poso r))

;;> What is an n-representative?
;;< The first n bits of a number, up to an including the rightmost 1.

;;> What is the n-representative of (0 1 1)?
;;< (0 1 1)

;;> What is the n-representative of (0 x 1 0 y . z)?
;;< (0 x 1)

;;> What is the n-representative of (0 0 y . z)?
;;< ()

;;> What is the n-representative of z?
;;< ()

(run 3 [s]
  (fresh [x y r]
    (addero)))

;;> What is a ground value?
;;< A value that contains no variables.

;;> Is [[1] [1] [0 1]] a ground value?
;;< Yes

;;> Is (fresh [a] [a [] a]) a ground value?
;;< No, because it contains one or more variables.

;;> What is the width of a numberlist?
;;< The width of a number is the same as the length of the numberlist.

(declare gen-addero)

(defn addero [d n m r]
  (conde
   [(== 0 d) (== [] m) (== m r)]
   [(== 0 d) (== [] n) (== m r) (poso m)]
   [(== 1 d) (== [] m) (addero 0 n [1] r)]
   [(== 1 d) (== [] n) (addero 0 [1] m r)]
   [(== [1] n) (== [1] m)
    (fresh [a c]
      (== [a c] r)
      (full-addero d 1 1 a c))]
   [(== [1] n) (gen-addero d n m r)]
   [(== [1] m) (>1o n) (>1o r) (addero d [1] n r)]
   [(>1o n) (gen-addero d n m r)]))

(defn gen-addero [d n m r]
  (fresh [a b c e x y z]
    (== (lcons a x) n)
    (== (lcons b y) m)
    (poso y)
    (== (lcons c z) r)
    (poso z)
    (all
     (full-addero d a b c e)
     (addero e x y z))))

(run* [s]
  (gen-addero 1 [0 1 1] [1 1] s))

;;> Describe gen-addero
;;< Given the bit d, and the numbers n, m and r, gen-addero satisfies d + n + m = r,
;;< provided that n is positive and m and r are greater than 1.8.0

;; Why does mine only have five answers? Should be 6?
(comment
  (run* [s]
    (fresh [x y]
      (addero 0 x y [1 0 1])
      (== [x y] s))))

(defn +o [n m k]
  (addero 0 n m k))

(run* [s]
  (fresh [x y]
    (+o x y [1 0 1])
    (== [x y] s)))

(defn -o [n m k]
  (+o m k n))

(run* [q]
  (-o [0 0 0 1] [1 0 1] q))

;; TODO: This should be '([]) but is '()
(comment
  (run* [q]
    (-o [0 1 1] [0 1 1] q)))

(run* [q]
  (-o [0 1 1] [0 0 0 1] q))
