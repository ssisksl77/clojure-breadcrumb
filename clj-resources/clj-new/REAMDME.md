# Resource

[link](https://github.com/seancorfield/clj-new)

# 시작하기

clj-new 는 clj-tools를 이용할 때, 시작 템플릿을 쉽게 사용하기 위해 만들어주는 툴이다.

`~/.clojure/deps.edn` 에다가 아래 코드를 넣는다.

```
 {:aliases
     {:new {:extra-deps {seancorfield/clj-new
                         {:mvn/version "1.1.228"}}
            :ns-default clj-new
            :exec-args {:template "app"}}}
     ...}
```

이제 아래처럼 앱을 생성할 수 있다.
```
clojure -X:new create :name myname/myapp
cd myapp
clojure -M -m myname.myapp
```
만들면 아래처럼 보인다.
```
$ tree
.
├── CHANGELOG.md
├── LICENSE
├── README.md
├── deps.edn
├── doc
│   └── intro.md
├── pom.xml
├── resources
├── src
│   └── myname
│       └── myapp.clj
└── test
    └── myname
        └── myapp_test.clj

6 directories, 8 files

$ clojure -M -m myname.myapp
Hello, World!
```

테스트도 실행해보고

```
clojure -M:test:runner
```

or you can create basic library
라이브러리를 만들 수도 있나봄.

```
clojure -X:new create :template lib :name myname/mylib
cd mylib
```

테스트도 다시 실행해보고

```
clojure -M:test:runner
```

