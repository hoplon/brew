(defn read-file   [file] (read-string (slurp file)))
(defn get-deps    []     (read-file "./dependencies.edn"))

(set-env!
 :dependencies   (get-deps)
 :resource-paths #{"src"})

(require
 '[degree9.boot-semver :refer :all]
 '[tolitius.boot-check :as check])

(task-options!
 pom    {:project 'hoplon/brew
         :description "Experimental Hoplon Components."
         :url "http://github.com/hoplon/brew"
         :scm {:url "http://github.com/hoplon/brew"}})

(deftask ci-deps
  "Force CI to fetch dependencies."
  []
  identity)

(deftask tests
  "Run code tests."
  []
  (comp
    (check/with-kibit)
    ;(check/with-yagni)
    (check/with-eastwood)
    (check/with-bikeshed)
    ))

(deftask deploy
  "Build project for deployment to clojars."
  []
  (comp
    (version)
    (build-jar)
    (push-release)))

(deftask develop
  "Build project for local development."
  []
  (comp
    (version :develop true
             :minor 'inc
             :patch 'zero
             :pre-release 'snapshot)
    (watch)
    (build-jar)))
