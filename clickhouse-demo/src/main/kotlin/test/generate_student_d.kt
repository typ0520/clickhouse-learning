package test

import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
fun main() {
    val sb = StringBuilder("insert into student_d values")
    val random = Random()
    val df = SimpleDateFormat("yyyy-MM-dd")
    for (i in 1 .. 1000) {
        val m = System.currentTimeMillis() - Duration.ofDays(random.nextInt(30).toLong()).toMillis()
        sb.append("\n(${i}, 's${i}', ${random.nextInt(100)}, '${df.format(Date(m))}')")
        sb.append(if (i == 1000) ";" else ",")
    }
    println(sb.toString())
}