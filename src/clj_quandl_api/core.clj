(ns clj-quandl-api.core
  (:require [cheshire.core :refer [parse-string]]
            [org.httpkit.client :as http]
            [clj-time.core :as t]))

(def api-key (atom nil))
(defn set-api-key!
  "Use your Quandl API key to avoid anonymous calls."
  [new-key]
  (reset! api-key new-key))

(def ^:private base-url "https://www.quandl.com/api/v3/datasets/")
(defn- request [url params]
  (let [auth-params (assoc params :api_key @api-key)
        {:keys [status headers body error]} @(http/get url {:query-params auth-params})]
    (if error
        (println "Failed request, exception:" error)
        body)))
(defn- assemble-url [dataset] (str base-url dataset "/data.json"))

;; Predicates for checking arguments.
(defn- date-time? [d] (or (string? d) (instance? org.joda.time.DateTime d)))
(def allowed {:collapse     #{:none :daily :weekly :monthly :quarterly :annual}
              :transform    #{:none :rdiff :diff :cumul :normalize}
              :order        #{:asc :desc}
              :rows         integer?
              :limit        integer?
              :column_index integer?
              :start_date   date-time?
              :end_date     date-time?})
(defn- allowed? [[k v]] ((allowed k) v))

(defn quandl
  "Request dataset by Quandl code, uses the same parameters as Quandl API but keywordized."
  [dataset & {:as params}]
  {:pre [(every? allowed? params)]}
  (let [response (request (assemble-url dataset) params)
        {:keys [quandl_error dataset_data]} (:dataset_data (parse-string response true))]
    (if quandl_error
        (println (:message quandl_error))
        dataset_data)))
