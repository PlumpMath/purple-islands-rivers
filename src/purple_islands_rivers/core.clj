(ns purple-islands-rivers.core
  "Inspired by Knuth, Experiments with Digital Halftones. 1987

  'regularity would tend to be modulated out by the eye.'

  A grid of mostly alternating blue and red dots is created. As the probability
  of colours not alternating is increased a red and blue rivers and islands
  emerge from a sea of purple."
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def n
  "Width/height of grid."
  600)

(defn row
  "Produces a sequence of alternating true/false values.
  Probability of value not alternating each each step is controlled by probs.
  Produces sequence as long as probs."
  [start probs]
  (reductions
    #(if (< %2 (rand)) (not %1) %1)
    start probs))

(defn grid [n]
  "Produces grid of true false values with increasing probability of values not
  alternating increasing per row."
  (let [row-probs (range 0 0.5 (/ 0.5 n))]
    (map #(row %1 (repeat n %2)) (cycle [true false]) row-probs)))

(defn setup []
  (q/no-loop)
  (q/no-stroke)
  {:grid (grid n)})

(defn update-state [state] state)

(defn draw-state
  [{:keys [grid]}]
  (q/background 255)
  (let [w (/ (q/width) n)
        h (/ (q/height) n)]
  (doseq [[y row] (map-indexed vector grid)]
    (doseq [[x v] (map-indexed vector row)]
      (apply q/fill (if v [255 0 0] [0 0 255]))
      (q/rect (* x w) (* y h) w h)))))

(q/defsketch purple-islands-rivers
  :title "A sea of purple, with red islands and rivers."
  :size [n n]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
