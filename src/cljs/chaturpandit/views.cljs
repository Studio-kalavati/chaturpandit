(ns chaturpandit.views
  (:require
   [re-frame.core :as re-frame :refer [dispatch subscribe]]
   [re-com.core :as re-com :refer [h-box v-box title hyperlink-href input-text button
                                   modal-panel
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
                 :children [[hyperlink-href
                                   :label "Test Compositions Page"
                                   :href "#/comp"]
                                  [hyperlink-href
                                   :label "Test Parts Page"
                                   :href "#/part"]]]]]))

(defn error-modal
  []
  [modal-panel
   :backdrop-opacity 0.4
   :child [v-box
           :children
           [[gap :size "5vh"]
            [v-box
             :justify :center
             :gap "2vh"
             :children
             [[title
               :label (str "Failed to load from URL")
               :level :level3]
              [box
               :align-self :center
               :child [button
                       :label            "Ok" 
                       :on-click          #(dispatch [::events/reset-loading-error ])]]]]]]])

(defn comp-panel []
  (let [comp-val (reagent/atom nil)
        comp-url (reagent/atom nil)
        display? (reagent/atom nil)]
    (fn []
      [v-box
       :align :center
       :gap "1em"
       :children [[home-title]
                  [gap :size "10vh"]
                  [input-text
                   :model            comp-val
                   :width            "40vw"
                   :input-type       :textarea
                   :height            "10vw"
                   :placeholder      "Copy composition data here"
                   :on-change        #(do
                                        (reset! comp-val %)
                                        (dispatch [::events/set-comp-data
                                                   (events/parse-res :comp
                                                                     (js->clj (js/JSON.parse %)))]))]
                  [title
                   :label (str "Or paste a URL")
                   :level :level3]
                  [input-text
                   :model            comp-url
                   :width            "40vw"
                   :placeholder      "Copy composition url"
                   :on-change        #(dispatch [::events/get-gist-data % :comp])]
                  [button
                   :label            "Submit" 
                   :on-click          #(reset! display? true)]
                  (when @display?
                    (let [div-id "viewer"]
                      [bv/disp-swara-canvas (subscribe [::subs/comp-data]) div-id
                       #(bv/viewer-sketch (constantly [@(subscribe [::bp/screen-width])
                                                       @(subscribe [::bp/screen-height])])
                                          (assoc @(subscribe [::subs/dispinfo]) :y 30))]))
                  (when @(subscribe [::subs/loading-error?])
                    [error-modal])]])))


(defn part-panel []
  (let [comp-val (reagent/atom nil)
        part-url (reagent/atom nil)
        display? (reagent/atom nil)]
    (fn []
      [v-box
       :gap "1em"
       :align :center
       :children [[home-title]
                  [gap :size "10vh"]
                  [input-text
                   :model            comp-val
                   :input-type       :textarea
                   :width            "40vw"
                   :height            "10vw"
                   :placeholder      "Copy part data here"
                   ;:change-on-blur? false
                   :on-change        #(do
                                        (reset! comp-val %)
                                        (dispatch [::events/set-comp-data
                                                   (events/parse-res :part
                                                                     (js->clj (js/JSON.parse %)))]))]
                  [title
                   :label (str "Or paste a URL")
                   :level :level3]
                  [input-text
                   :model           part-url 
                   :width            "40vw"
                   :placeholder      "Copy composition url"
                   :on-change        #(dispatch [::events/get-gist-data % :part])]

                  [button
                   :label            "Submit" 
                   :on-click          #(reset! display? true)]
                  (when @display?
                    (let [div-id "viewer"]
                      [bv/disp-swara-canvas (subscribe [::subs/comp-data]) div-id
                       #(bv/viewer-sketch (constantly [@(subscribe [::bp/screen-width])
                                                       @(subscribe [::bp/screen-height])])
                                          (assoc @(subscribe [::subs/dispinfo]) :y 30)
                                          p/disp-part)]))
                  
                  (when @(subscribe [::subs/loading-error?])
                    [error-modal])]])))

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
    [v-box
     :height "100%"
     :children [[panels @active-panel]]]))
