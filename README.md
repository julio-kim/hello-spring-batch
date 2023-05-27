# Hello Spring-Batch

## I. Getting Started

### I-1. Database 설치

> H2 Database Download 사이트에 접속해 자신의 Platform에 맞는 설치파일을 다운로드 진행
> 
> https://h2database.com/html/download.html

* Platform Independent:  https://github.com/h2database/h2database/releases/download/version-2.1.214/h2-2022-06-13.zip
  * MacOS / Linux / Etc..
* Windows: https://github.com/h2database/h2database/releases/download/version-2.1.214/h2-setup-2022-06-13.exe

### I-2. Database 실행

#### [Mac OS]

```shell
$ chmod 755 h2.sh
$ ./h2.sh
```

#### [Windows]

```shell
$ h2.bat
```

### I-3. Batch 실행

* [HelloSpringBatchApplication](/julio-kim/hello-spring-batch/blob/main/src/main/java/com/pnoni/batch/hello/HelloSpringBatchApplication.java) 을 실행하여 배치를 구동한다.
