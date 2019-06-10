(ns chaturpandit.db
  (:require
   [bhatkhande.hindi :as hindi]
   [bhatkhande.english :as english]))

(def dispinfo
  {:x 20 :y 30 :under 30
   :x-start 20
   :y-inc 80
   :x-end (int (.-innerWidth js/window))
   :y-end (int (.-innerHeight js/window))
   :over 30
   :write-part-label true
   :write-comp-label true
   :write-line-separator true
   :language :hindi
   :swaramap #'bhatkhande.hindi/swaramap

   :kan {:kan-raise 10
         :reduce-font-size 5
         :reduce-spacing 3
         :reduce-octave-size 5}
   :octave 15
   :part-coordinates []
   :part-header-font-size 30
   :comp-label-font-size 35
   :header-y-spacing 50
   :sam-khaali 35
   :debug {:disp-swara false}
   :font-size 20
   :spacing 10 :text-align :left})

(def default-db
  {:init-state {:cursor-color 1}
   :dispinfo dispinfo})
