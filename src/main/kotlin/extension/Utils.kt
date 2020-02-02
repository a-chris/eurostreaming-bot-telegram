package extension

fun List<*>.println() {
    this.forEach { println(it.toString()) }
}