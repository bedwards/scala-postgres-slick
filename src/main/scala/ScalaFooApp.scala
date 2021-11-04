import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import slick.jdbc.PostgresProfile.api._

object ScalaFoo extends App {
    println(args.length match {
        case 0 => "You passed in no arguments!"
        case 1 => s"You passed in 1 argument, which was ${args(0)}"
        case x => s"You passed in $x arguments! They are: ${args.mkString(",")}"
    })

    val db = Database.forConfig("scalaFooDb")


    class Suppliers(tag: Tag) extends Table[(Int, String, String, String, String, String)](tag, "suppliers") {
        def id = column[Int]("sup_id", O.PrimaryKey)
        def name = column[String]("sup_name")
        def street = column[String]("street")
        def city = column[String]("city")
        def state = column[String]("state")
        def zip = column[String]("zip")
        def * = (id, name, street, city, state, zip)
    }
    val suppliers = TableQuery[Suppliers]

    class Coffees(tag: Tag) extends Table[(String, Int, Double, Int, Int)](tag, "coffees") {
        def name = column[String]("cof_name", O.PrimaryKey)
        def supID = column[Int]("sup_id")
        def price = column[Double]("price")
        def sales = column[Int]("sales")
        def total = column[Int]("total")
        def * = (name, supID, price, sales, total)
        def supplier = foreignKey("SUP_FK", supID, suppliers)(_.id)
    }
    val coffees = TableQuery[Coffees]

    Await.result(db.run(DBIO.seq(
            coffees.schema.dropIfExists,
            suppliers.schema.dropIfExists,
            suppliers.schema.create,
            coffees.schema.create
        )), 5.seconds)

    val initialData = DBIO.seq(
        suppliers += (101, "Acme, Inc.",      "99 Market Street", "Groundsville", "CA", "95199"),
        suppliers += ( 49, "Superior Coffee", "1 Party Place",    "Mendocino",    "CA", "95460"),
        suppliers += (150, "The High Ground", "100 Coffee Lane",  "Meadows",      "CA", "93966"),
        coffees ++= Seq(
            ("Colombian",         101, 7.99, 0, 0),
            ("French_Roast",       49, 8.99, 0, 0),
            ("Espresso",          150, 9.99, 0, 0),
            ("Colombian_Decaf",   101, 8.99, 0, 0),
            ("French_Roast_Decaf", 49, 9.99, 0, 0)
        )
    )
    Await.result(db.run(initialData), 5.seconds)

    println("Coffees:")
    db.run(coffees.result).map(_.foreach {
    case (name, supID, price, sales, total) =>
        println("  " + name + "\t" + supID + "\t" + price + "\t" + sales + "\t" + total)
    })

}
