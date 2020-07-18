package nicestring

fun String.isNice(): Boolean {
    var counter = 0
    if (!(this.contains("bu") || this.contains("ba") || this.contains("be")))
        counter++
    val s = this.filter { it == 'a' || it == 'e' || it == 'i' || it == 'o' || it == 'u'}
    if (s.length >= 3)
        counter++
    var c = ' '
    for (s in this) {
        if (c == s)
            counter++
        c = s
    }
    return counter >= 2
}

fun String.isNiceFunc() : Boolean {
    val noBadSubstring = setOf("ba", "bu", "be").none{ this.contains(it) }
    val hasThreeVowels = count{ it in "aeiou" } >= 3
    val hasDouble = zipWithNext().any{ it.first == it.second }
    return listOf(noBadSubstring, hasThreeVowels, hasDouble).count { it } >= 2
}