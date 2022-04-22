package test

import org.apache.commons.dbutils.DbUtils
import org.apache.commons.dbutils.QueryRunner
import org.apache.commons.dbutils.handlers.BeanHandler
import org.apache.commons.dbutils.handlers.BeanListHandler
import org.apache.commons.dbutils.handlers.ScalarHandler
import ru.yandex.clickhouse.BalancedClickhouseDataSource
import ru.yandex.clickhouse.settings.ClickHouseProperties
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*
import javax.sql.DataSource

/**
 * @author tong
 */
fun main() {
    val ds = getDataSource()
    insert(ds);
    update(ds);
    query(ds);
    delete(ds);
}

fun insert(ds: DataSource) {
    val qr = QueryRunner(ds)
    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val num = qr.update("insert into student(id, name, age, birthday) values(?, ?, ?, ?)", 255, "255", 255, df.format(Date()))
    println("insert success: ${num == 1}")
}

fun update(ds: DataSource) {
    val qr = QueryRunner(ds)
    val num = qr.update("alter table student update name = ? where id = ?", "name255", 255)
    println("update success: ${num >= 1}")
}

fun delete(ds: DataSource) {
    val qr = QueryRunner(ds)
    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    qr.update("insert into student(id, name, age, birthday) values(?, ?, ?, ?)", 200, "200", 200, df.format(Date()))
    val num = qr.update("alter table student delete where id = ?", 200)
    println("delete success: ${num >= 1}")
}

fun query(ds: DataSource) {
    val qr = QueryRunner(ds)
    val version = qr.query("select version();", ScalarHandler<String>())
    println("clickhouse version: $version")

    val students = qr.query("select id, name, age, birthday from student;", BeanListHandler(Student::class.java))
    println("students: $students")

    val student = qr.query("select id, name, age, birthday from student where id = ?;", BeanHandler(Student::class.java), 1)
    println("student: $student")

    val count = qr.query("select count(id) from student where id = 100", ScalarHandler<BigInteger>()).toLong()
    println(count)
}

fun getDataSource(): DataSource {
    DbUtils.loadDriver("ru.yandex.clickhouse.ClickHouseDriver")
    val props = ClickHouseProperties()
    props.user = "root"
    props.password = "root"
    return BalancedClickhouseDataSource("jdbc:clickhouse://127.0.0.1:8123/default", props)
}

data class Student(
    var id: Long? = 0,
    var name: String? = "",
    var age: Int? = 0,
    var birthday: Date? = null
)
