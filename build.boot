(set-env!
 :source-paths #{"src/"}
 :dependencies
 '[[org.clojure/core.logic                  "0.8.10"]])

(deftask load-code
  "Loads and evaluates the string prints to STDOUT"
  [c code-to-eval VAL str "The code to be evaluated"]
  (-> code-to-eval load-string println))
