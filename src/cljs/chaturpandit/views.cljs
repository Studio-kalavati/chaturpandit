(ns chaturpandit.views
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [re-com.core :as re-com :refer [h-box v-box title hyperlink-href input-text button
                                   box gap
                                   md-icon-button]]
   [breaking-point.core :as bp]
   [re-pressed.core :as rp]
   [reagent.core  :as    reagent]
   [chaturpandit.subs :as subs]
   [chaturpandit.events :as events]
   [bhatkhande.views :as bv]
   [cljs.reader :as cljr]
   [bhatkhande.parts :as p]))

(defn home-title []
  [h-box
   :gap "20px"
   ;:align :stretch
   :justify :center
   :children [[box
               :align-self :center
               :child [hyperlink-href
                           :label [:i {:class "zmdi zmdi-home zmdi-hc-2x"}]
                           :href "#/"]]
              [box
               :align-self :center
               :child [title
                           
                           :label (str "Bhatkhande notations online")
                           :level :level2]]]])

(defn home-panel []
  (fn []
    [v-box
     :gap "20px"
     :children [[home-title]
                [gap :size "10vh"]
                [h-box
                 :justify :center
                 :gap "20px"
                 :children [[re-com/hyperlink-href
                                   :label "Test Compositions Page"
                                   :href "#/comp"]
                                  [re-com/hyperlink-href
                                   :label "Test Parts Page"
                                   :href "#/part"]]]]]))

(defn comp-panel []
  (let [comp-val (reagent/atom nil)
        display? (reagent/atom nil)]
    (fn []
      [re-com/v-box
       :align :center
       :gap "1em"
       :children [[home-title]
                  [gap :size "10vh"]
                  [re-com/input-text
                   :model            comp-val
                   :width            "300px"
                   :placeholder      "Copy composition data here"
                   :on-change        #(dispatch [::events/set-comp-data [(cljr/read-string %)
                                                                     :comp]])]
                  [re-com/button
                   :label            "Submit" 
                   :on-click          #(reset! display? true)]
                  (when @display?
                    (let [div-id "viewer"]
                      [bv/disp-swara-canvas (subscribe [::subs/comp-data]) div-id
                       #(bv/viewer-sketch (constantly [@(subscribe [::bp/screen-width])
                                                       @(subscribe [::bp/screen-height])])
                                          (assoc @(subscribe [::subs/dispinfo]) :y 30))]))]])))


(defn part-panel []
  (let [comp-val (reagent/atom nil)
        display? (reagent/atom nil)]
    (fn []
      [re-com/v-box
       :gap "1em"
       :align :center
       :children [[home-title]
                  [gap :size "10vh"]
                  [re-com/input-text
                   :model            comp-val
                   :width            "300px"
                   :placeholder      "Copy part data here"
                   :on-change        #(dispatch [::events/set-comp-data [(cljr/read-string %)
                                                                     :part]])]
                  [re-com/button
                   :label            "Submit" 
                   :on-click          #(reset! display? true)]
                  (when @display?
                    (let [div-id "viewer"]
                      [bv/disp-swara-canvas (subscribe [::subs/comp-data]) div-id
                       #(bv/viewer-sketch (constantly [@(subscribe [::bp/screen-width])
                                                       @(subscribe [::bp/screen-height])])
                                          (assoc @(subscribe [::subs/dispinfo]) :y 30)
                                          p/disp-part)]))]])))

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :comp-panel [comp-panel]
    :part-panel [part-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [re-com/v-box
     :height "100%"
     :children [[panels @active-panel]]]))
