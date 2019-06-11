(ns chaturpandit.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::re-pressed-example
 (fn [db _]
   (:re-pressed-example db)))

(re-frame/reg-sub
 ::dispinfo
 (fn [db]
   (-> db :dispinfo)))

(re-frame/reg-sub
 ::data-type
 (fn [db]
   (-> db :data-type)))

(re-frame/reg-sub
 ::comp-data
 (fn [db]
   (-> db :comp-data)))

(re-frame/reg-sub
 ::waiting-mode
 (fn [db]
   (true? (-> db :waiting-mode))))

(re-frame/reg-sub
 ::loading-error?
 (fn [db]
   (true? (-> db :load-error))))
