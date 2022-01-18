SHELL = bash

.EXPORT_ALL_VARIABLES:
.PHONY: test build

repl:
	rm -rf .cpcache/ && DEBUG=true && clojure -A:test:nrepl -m nrepl.cmdline --middleware "[cider.nrepl/cider-middleware]"

build:
	clojure -A:build
	mv target/app-1.0.0-SNAPSHOT-standalone.jar app.jar

run-jar:
	java -jar app.jar -m app.core

test:
	clojure -A:test:runner
