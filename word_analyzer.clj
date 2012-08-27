(ns word-counter)


;;
;;(word-counter/word-count "text body")
;;

;;
;; Word Splitters
;;
(def ^:dynamic *words* (atom ()))

(defn update-words
  [str-data]
  (swap! *words* (fn [_]
                   (sort
                    (clojure.string/split
                     (apply str str-data) #"\s+"))))
  @*words*)
;;
;; Word Counters
;;
(def ^:dynamic *counted-words* (atom ()))
(def ^:dynamic *counted-board* (atom {})) ;; {:the 10, :and 100, :before 50}

(defn counted?
  "Returns true if in *counted-words* atom"
  [str-word]
  (not (empty?
        (filter #(= str-word %) @*counted-words*))))

(defn count-word
  "Checks and increments a words view count"
  [str-word]
  (let
      [counted *counted-words*
       board *counted-board*
       k (keyword str-word)]
    (if-not (counted? str-word)
      (do
        (swap! counted conj str-word)
        (swap! board assoc k 1))
      (swap! board assoc k (inc (k @board))))))
;;
;; Word Statistic Collector
;;
(defn load-occurances
  "Returns a set of word count => {:word 0}"
  [str-text-body]
  (doseq [w (update-words str-text-body)] (count-word w))
  (reverse (flatten
            (sort-by second @*counted-board*))))

(defn word-count
  "Main function to analyze words"
  [str-text-body]
  (dosync
   (reset! *words* ())
   (reset! *counted-words* ())
   (reset! *counted-board* {})
   (load-occurances str-text-body)))