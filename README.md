# Scala PostgreSQL Slick

Start the PostgreSQL database. It will log all SQL queries.

```
$ docker-compose up
```

Run the program

```
$ sbt 'run foo bar'
You passed in 2 arguments! They are: foo,bar
Coffees:
  Colombian	101	7.99	0	0
  French_Roast	49	8.99	0	0
  Espresso	150	9.99	0	0
  Colombian_Decaf	101	8.99	0	0
  French_Roast_Decaf	49	9.99	0	0
```

Back in the PostgreSQL window

```
select "cof_name", "sup_id", "price", "sales", "total" from "coffees"
```

* [sbt](https://www.scala-sbt.org/1.x/docs/sbt-by-example.html)
* [PostgreSQL](https://hub.docker.com/_/postgres)
* [Slick](https://scala-slick.org/doc/3.3.3/gettingstarted.html)

## build.sbt

```
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42"
)
```

## src/main/resources/application.conf

```
scalaFooDb = {
  connectionPool = "HikariCP"
  dataSourceClass = "org.postgresql.ds.PGSimpleDataSource"
  properties = {
    serverName = "localhost"
    portNumber = "5432"
    databaseName = "scala_foo"
    user = "root"
    password = "password"
  }
  numThreads = 10
}
```
