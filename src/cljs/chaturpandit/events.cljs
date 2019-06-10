(ns chaturpandit.events
  (:require
   [re-frame.core :as re-frame]
   [chaturpandit.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 ::set-re-pressed-example
 (fn [db [_ value]]
   (assoc db :re-pressed-example value)))

(re-frame/reg-event-db
 ::set-comp-data
 (fn [db [_ [comp-data data-type]]]
   (assoc db :data-type data-type 
          :comp-data comp-data)))
