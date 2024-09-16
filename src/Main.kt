import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Duration
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.*
import kotlin.random.Random

fun main() {
    val sourceFilePath = "originalFilePath.txt"
    val newFileName = "newRenamedFile.txt"
    val newFilePath = copyFileAndRename(sourceFilePath, newFileName)
    if (newFilePath != null) {
        println("File copied and renamed successfully to: $newFilePath")
    } else {
        println("Failed to copy and rename file.")
    }
}

fun copyFileAndRename(sourcePath: String, newFileName: String): Path? {
    val source: Path = Paths.get(sourcePath)
    val targetDir = source.parent
    val newTargetPath = targetDir.resolve(newFileName)
    return try {
        Files.copy(source, newTargetPath, StandardCopyOption.REPLACE_EXISTING)
        newTargetPath
    } catch (e: Exception) {
        null
    }
}

fun getFirstStr() {
    val s = "123465789"
    println(s.take(3))
}

fun parseTime() {
    val time = "1s"
    var s = time.uppercase()
    if (!s.contains("T")) {
        if (s.contains("D")) {
            if (s.contains("H") || s.contains("M") || s.contains("S")) {
                s = s.replace("D", "DT")
            }
        } else {
            s = if (s.startsWith("P")) {
                "PT" + s.substring(1)
            } else {
                "T$s"
            }
        }
    }
    if (!s.startsWith("P")) {
        s = "P$s"
    }
    try {
        println(Duration.parse(s).toMillis())
    } catch (_: Exception) {
    }
}

fun getItemLevel(): String? {
    val check = "xxx:"
    val values = listOf("a", "b", "c", "d")
    val lores = listOf("abc", "123", "xxx: ****** [a]")

    val found = lores
        .filter { it.contains(check) }
        .flatMap { lore ->
            values.filter { lore.contains(it) }
        }
        .firstOrNull()

    return found
}


fun getItemQuality(): String? {
    val check = "xxx:"
    val sort = listOf("a" to "*****", "b" to "****", "c" to "***")
    val lores = listOf("abc", "123", "xxx: ****** []")

    val found = lores
        .filter { it.contains(check) }
        .flatMap { lore ->
            sort.filter { lore.contains(it.second) }
        }
        .firstOrNull()

    return found?.first
}

fun flatMap() {
    val newMap =
        mapOf("a" to mapOf("A" to 1), "b" to mapOf("B" to 2), "c" to mapOf("C" to 3))

    val result = newMap.flatMap { (_, innerMap) ->
        innerMap.map { (key, value) ->
            "$key $value"
        }
    }

    println(result) // 输出: [物理攻击 1, 魔法攻击 2]
}

fun test1() {
    val randMap = randomNumber()

    println(randMap)

    val addMap = addNumber(randMap)

    println(addMap)

    val subMap = mutableMapOf<String, Int>()

    randMap.forEach { (k, v) ->
        subMap[k] = (addMap[k] ?: 0) - v
    }

    println(subMap)
}


fun addNumber(map: Map<String, Int>): MutableMap<String, Int> {
    val mutableMap = map.filter { it.value > 0 }.toMutableMap()
    val mapA = mutableMapOf<String, Int>()
    mutableMap.forEach { (k, _) ->
        mapA[k] = 0
    }

    /**
     * 单个词条最大值
     */
    val maxV = 8

    /**
     * 单个词条增加值上限
     */
    val maxA = 2

    /**
     * 强化券能增加的数量
     */
    val maxS = 9

    var total = 0
    val new = mutableMap.keys.shuffled()
    var rd = listOf<String>()

    while (total < maxS) {
        val randNew = new.shuffled()
        randNew.forEach {
            if (rd == randNew) {
                return@forEach
            }
            rd = randNew
            if ((mapA[it] ?: 0) >= maxA) {
                return@forEach
            }
            if ((mutableMap[it] ?: 0) >= maxV) {
                return@forEach
            }
            mutableMap[it] = (mutableMap[it] ?: 0) + 1
            mapA[it] = (mapA[it] ?: 0) + 1
            total++
        }

        if (mutableMap.all { it.value >= maxV }) {
            return mutableMap
        }

        if (mapA.all { it.value >= maxA }) {
            return mutableMap
        }
    }
    return mutableMap
}

fun randomNumber(): Map<String, Int> {
    val map = ConcurrentHashMap<String, Int>()
    val randList = listOf("A", "B", "C", "D")
    val news = randList.shuffled()
    map[news.last()] = 0
    val new = news.dropLast(1)

    val maxS = 4
    val maxV = 10

    var total = 0
    var rd = listOf<String>()
    while (total < maxV) {
        val randNew = new.shuffled()
        randNew.forEach {
            if (rd == randNew) {
                return@forEach
            }
            rd = randNew
            if ((map[it] ?: 0) >= maxS) {
                return@forEach
            }
            map[it] = (map[it] ?: 0) + 1
            total++
        }
    }

    return map
}

fun String.getIntList(): List<Int> {
    val s = this.split("~")
    return (s[0].toInt()..(s.getOrElse(1) { s[0] }).toInt()).toList()
}

fun extractNumberFromLv(input: String): Int? {
    // 定义正则表达式模式
    val pattern = Pattern.compile("\\[lv.(\\d+)\\]")

    // 创建 Matcher 对象
    val matcher: Matcher = pattern.matcher(input)

    // 查找匹配项
    if (matcher.find()) {
        // 提取匹配的数字
        val number = matcher.group(1)

        // 将数字转换为整数并返回
        return number.toInt()
    }

    // 没有匹配到数字，返回 null
    return null
}

fun <K, V> Map<K, V>.getRandomEntries(count: Int): List<Map.Entry<K, V>> {
    // 创建一个 Random 对象用于生成随机索引
    val random = java.util.Random()

    // 获取 Map 中的键值对列表
    val entries = this.entries.toList()

    // 使用 shuffled() 函数打乱键值对列表的顺序
    val shuffledEntries = entries.shuffled(random)

    // 使用 take() 函数获取指定数量的键值对
    return shuffledEntries.take(count)
}


fun calOffset() {
    /**
     * 稀有度偏移量
     * 1星 0
     * 2星 1
     * 3星 2
     * 4星 3
     *
     * 时间节点 = 2025-01-01 ~ 2025-02-28
     * y = sin((x * π - 对应稀有度偏移量)/2)
     * x = 2025-01-25 在这区间时间点的百分比
     * y = 权重
     *
     * 1星的y
     * 2星的y
     * 3星的y
     * 4星的y
     *
     * 2星概率 = 2星的y / y总和
     *
     */
    val map: Map<String, Double> = mapOf("a" to 0.0, "b" to 1.0, "c" to 2.0, "d" to 2.5, "e" to 3.15)

    val offset = map["d"] ?: 0.0

    val now = LocalDate.of(2024, 6, 29)
    // 开始时间
    val startDate = LocalDate.of(2024, 5, 1)
    // 结束时间
    val endDate = LocalDate.of(2024, 6, 30)

    val x = calculateDayPercentage(startDate, endDate, now)

    println("选中的偏移量: $offset")
    println("进度百分比: $x")

    val y = abs(sin((PI * x - offset) / 2))

    val totalWeight = map.map { it.value }.fold(0.0) { acc, d -> acc + abs(sin((PI * x - d) / 2)) }

    val a = map.map { it }.fold(0.0) { acc, d ->
        println(d.key + "的偏移量为" + d.value)
        val fx = abs(sin((PI * x - d.value) / 2))
        println("权重为: ${fx.toFloat()}")
        acc + fx
    }

    println("选中的权重: $y")
    println("权重总和: $totalWeight")
    println("选中的概率: ${y / totalWeight}")

}

fun calculateDayPercentage(startDate: LocalDate, endDate: LocalDate, targetDate: LocalDate): Double {
    val totalDays = endDate.toEpochDay() - startDate.toEpochDay()
    val daysSinceStart = targetDate.toEpochDay() - startDate.toEpochDay()

    return (daysSinceStart / totalDays.toDouble())
}


fun String.replaceLast(oldValue: String, newValue: String): String {
    val lastIndex = lastIndexOf(oldValue)
    return if (lastIndex >= 0) {
        substring(0, lastIndex) + newValue + substring(lastIndex + oldValue.length)
    } else {
        this
    }
}

fun generateRandEntry(amount: Int, weightMap: Map<String, Int>): List<Triple<String, String, Boolean>> {
    val entries = arrayListOf<String>()
    val entryList = arrayListOf<Triple<String, String, Boolean>>()
    var weight = 0
    weightMap.forEach { weight += it.value }

    val conf = mapOf("crit_chance" to Triple("暴击率", "10", true), "crit_damage" to Triple("暴击伤害", "20", false))
    while (entries.size < amount) {
        weightMap.forEach { (k, v) ->
            if (entries.size >= amount) {
                return@forEach
            }
            val rand = Random.nextInt(weight + 1)
            if (v < rand) {
                return@forEach
            }
            if (conf[k]!!.third && entries.contains(k)) {
                return@forEach
            }
            entries += k
        }
    }
    entryList.addAll(
        entries.map {
            Triple(conf[it]!!.first, conf[it]!!.second, conf[it]!!.third)
        }
    )

    return entryList
}


fun String.parseArgs(): Map<String, String> {
    val chars = split("")
    val strMap = mutableMapOf<String, String>()
    val charStr = arrayListOf<String>()
    var isValue = false
    var isMark = false
    var keyName: String? = null
    for (char in chars) {
        if (!isValue) {
            if (char == "=") {
                isValue = true
                keyName = charStr.joinToString("")
                charStr.clear()
                continue
            }
            charStr += char
        } else {
            if (char == "\"") {
                if (isMark) {
                    strMap["%${keyName!!}%"] = charStr.joinToString("")
                    keyName = null
                    charStr.clear()
                }
                isMark = !isMark
                continue
            }
            if (isMark) {
                charStr += char
            } else {
                if (char == " ") {
                    isValue = false
                    strMap["%${keyName!!}%"] = charStr.joinToString("")
                    keyName = null
                    charStr.clear()
                    continue
                }
                charStr += char
            }
        }
    }
    keyName?.let { strMap["%$it%"] = charStr.joinToString("") }
    return strMap
}
