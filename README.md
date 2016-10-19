Quandl API wrapper in Clojure 
===================================

> A simple wrapper for interacting with [Quandl API](https://www.quandl.com/docs/api)

## Get it
```clojure
[clj-quandl-api "0.2.1"]

;; In your ns statement:
(ns my.ns
  (:require [clj-quandl-api.core :refer :all]))
```

## Usage

Calls can be made anonymously with strict rate limits,
set an API key to avoid them:
```clojure
(set-api-key! "H2O...")
```

The wrapper is just one function `quandl`, it sticks closely to conventions in API.
```clojure
; Only Quandl code is needed.
(quandl "WIKI/AAPL")

; All other parameters are key-value pairs.
(quandl "WIKI/AAPL" :rows 10 :collapse "annual")
```

`clj-time` is supported for dates (in addition to strings).
```clojure
(require '[clj-time.core :as t])

(quandl "WIKI/AAPL" :end_date (t/date-time 1986 10 14))
```

API key can be also passed as an argument:
```clojure
(quandl "WIKI/AAPL" :api_key "H2O...")
```

## License

Copyright © 2016 keorn

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
