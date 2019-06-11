(ns chaturpandit.events
  (:require
   [clojure.spec.alpha :as s :refer [valid?]]
   [re-frame.core :as re-frame :refer [reg-event-fx reg-event-db]]
   [chaturpandit.db :as db]
   [clojure.walk :as w]
   [sargam.spec :as ss]
   [ajax.core :as ajax] 
   ))

(reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(reg-event-db
 ::set-re-pressed-example
 (fn [db [_ value]]
   (assoc db :re-pressed-example value)))

(reg-event-db
 ::set-comp-data
 (fn [db [_ [comp-data data-type]]]
   (assoc db :data-type data-type 
          :comp-data comp-data)))

(reg-event-fx
 ::get-gist-data
 (fn [{:keys [db]} [ _ url ctype]]                    
   (println " url to get from " url)
   {:db  (assoc  db :waiting-mode true)    
    :http-xhrio {:method          :get
                 :uri             url 
                 :timeout         8000                                           ;; optional see API docs
                 :response-format (ajax/json-response-format)  ;; IMPORTANT!: You must provide this.
                 :on-success      [::success-http-result ctype]
                 :on-failure      [::fail-http-result]}}))

(defn kwdize
  "Handle 2 cases: if map key is a number, dont keywordize it.
  and for :note key, keywordize the map value"
  [m]
  (let [f (fn [[k v]] (let [parsed (js/parseInt k)
                            k1 (if (true? (js/isNaN parsed))
                                   (keyword k)
                                   parsed)]
                        [ k1 (cond (or (= k1 :kan) (= k1 :note))
                                   (mapv keyword v)
                                   :default v)]))]
    (w/postwalk (fn [x] (if (map? x) (into {} (map f x)) x)) m)))

(reg-event-fx
 ::success-http-result
 (fn [{db :db} [_ ctype result]]
   (let [res (kwdize result)
         ikey (if (= ctype :comp)
                ::ss/composition
                ::ss/comp-part)]
     (println ctype " got result " " valid? "
              (s/valid? ikey res) 
              (when (not (s/valid? ikey res))
                (s/explain ikey res)))
     {:db (assoc db :waiting-mode false)
      :dispatch [::set-comp-data [res ctype]]})))

(reg-event-db
 ::fail-http-result
 (fn [db [_ result]]
   (println " http result fail " result)
   (assoc db :load-error true)))

(reg-event-db
 ::reset-loading-error
 (fn [db _]
   (dissoc db :load-error)))
