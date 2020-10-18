# To start

	M-x markdown-mode
	M-x describe-mode

# Links

[link](https://clojure.org/guides/deps_and_cli)


# Install

	brew install clojure/tools/clojure

# Repl

	$ clj
	Clojure 1.10.1
	user=>

# dependeny 

To work with this library, you need to declare it as a dependency so the tool can ensure it has been downloaded and add it to the classpath. The readme in most projects shows the name and version to use. Create a deps.edn file to declare the dependency:
라이브러리를 사용하려면 `deps.edn` 파일을 만들어야 한다.
그리고 안에 아래처럼 넣자.

	{:deps
	  {clojure.java-time/clojure.java-time {:mvn/version "0.3.2"}}}
	
Restart the REPL with the clj tool:

	$ clj
	Downloading
	...
	user=> (require '[java.time :as t])
	nil
	user=> (str (t/instant))
	"2020-09-01T03:42:47.691119Z"

# Writing a Program

## 프로그램 구조 만들기

1. 예시로 헬로월드 프로그램을 만든다.
2. 그 안에 위에서 만든 edn 파일을 복사한다.
3. src 폴더를 만든다.
4. hello.clj 파일을 만들고 아래 코드를 넣는다.

```
	(ns hello
	  (:require [java-time :as t]))
	
	(defn time-str
	  "Returns a string representation of a datetime in the local time zone."
	  [instant]
	  (t/format
	    (t/with-zone (t/formatter "hh:mm a") (t/zone-id))
	    instant))

	(defn run [opts]
      (println "Hello world, the time is" (time-str (t/instant))))
```

## 실행하기

This program has an entry function run that can be executed by clj using -X:
프로그램의 시작 함수는 `clj -X` 명령어로 실행할 수 있다.

	$ pwd
	.../hello-world/
	$ clj -X hello/run
	Hello world, the time is 05:39 오후
	
## 테스트 추가하기

아쉬운 점은 `lein`에서는 프로젝트를 만들면 저절로 test까지 있다는 점이다.
You can add extra paths as modifications to the primary classpath in the make-classpath step of the classpath construction. 
추가적으로 패스를 추가하면 된다는 것 같다.
To do so, add an alias :test that includes the extra relative source path "test":
alias로 `:test`를 추가하고 다른 상대적 패스 "test"를 추가하자.

	{:deps
	 {org.clojure/core.async {:mvn/version "1.3.610"}}

	 :aliases
	 {:test {:extra-paths ["test"]}}}

Apply that classpath modification and examine the modified classpath by invoking `clj -A:test -Spath:`

```
$ clj -A:test -Spath
test:
src:
/Users/me/.m2/repository/org/clojure/clojure/1.10.1/clojure-1.10.1.jar:
... same as before (split here for readability)
```

## Add Optional Dependency

Aliases in the deps.edn file can also be used to add optional dependencies that affect the classpath:
옵셔널한 의존성이라...
```
{:aliases
 {:bench {:extra-deps {criterium/criterium {:mvn/version "0.4.4"}}}}}
```
Here the :bench alias is used to add an extra dependency, namely the criterium benchmarking library.
`:bench` 알리아스는 추가 의존성을 위해 사용하는 것.
You can add this dependency to your classpath by adding the :bench alias to modify the dependency resolution: clj -A:bench.
이 의존성을 추가하려면 `:bench`를 실행할 때 추가하면 된다. `clj -A:bench`
여기서 A는 Alias를 말하나보다.

## Ahead-of-time (AOT) compilation

