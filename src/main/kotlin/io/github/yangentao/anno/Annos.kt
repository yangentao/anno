@file:Suppress("unused")

package io.github.yangentao.anno

import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * Serialize this field/property, for json
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class SerialMe()

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class TempValue(val desc: String = "")

/**
 * for proguard
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class KeepMe

/**
 * 对于List/Array/Set, 只使用list:Char
 * 对于map, 先用list:Char分割出entries, 再用map:Char分割Key和Value
 */

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class SepChar(val list: Char = ',', val map: Char = ':')

/**
 * 	@OptionList("0:男", "1:女")
 * 	@OptionList("男", "女")
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class OptionList(vararg val options: String, val bits: Boolean = false)

fun OptionList.verify(textSet: Set<String>): Boolean {
    val optSet = this.options.toSet()
    return optSet.containsAll(textSet)
}

/**
 *  DecimalFormat("#.00")
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class NumberPattern(val pattern: String)

/**
 * String.format
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class StringFormat(val pattern: String)

/**
 * java.util.Date
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class DatePattern(val format: String)

/**
 * fixed > 0时, min和max无效
 * fixed = 0时, min和max有效
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Length(val max: Long = 255, val min: Long = 0, val fixed: Long = 0)

@Suppress("ConvertTwoComparisonsToRangeCheck")
fun Length.verify(value: Long): Boolean {
    if (fixed > 0L) return value == fixed
    if (max > 0L && value > max) return false
    if (min > 0L && value < min) return false
    return true
}

/**
 * exclude this field/property, when serialize to json
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Exclude

val KProperty<*>.excluded: Boolean get() = this.hasAnnotation<Exclude>()

/**
 * 表或字段(属性)的名字, for json/orm
 * 路由时, controller名字或action名字
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Name(val value: String)

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Label(val value: String, val desc: String = "")

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Comment(val value: String)

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class NullValue(val value: String)

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Hidden

/**
 * Trim parameter
 */
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Trim

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class RangeLong(val min: Long, val max: Long)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class RangeInt(val min: Int, val max: Int)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class RangeDouble(val min: Double, val max: Double)

@Suppress("ConvertTwoComparisonsToRangeCheck")
fun RangeDouble.verify(value: Double): Boolean {
    return value >= min && value <= max
}

fun RangeInt.verify(value: Int): Boolean {
    return value in min..max
}

fun RangeLong.verify(value: Long): Boolean {
    return value in min..max
}

/**
 * 字符串非空, 也可以用于集合
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class NotEmpty(val trim: Boolean = true)

/**
 * 字符串长度限制, 也可用于数组或JsonArray
 */
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Match(val value: String, val msg: String = "")

val KClass<*>.userName: String
    get() {
        return this.findAnnotation<Name>()?.value ?: this.simpleName!!
    }
val KFunction<*>.userName: String
    get() {
        return this.findAnnotation<Name>()?.value ?: this.name
    }
val KProperty<*>.userName: String
    get() {
        return this.findAnnotation<Name>()?.value ?: this.name
    }

val KParameter.userName: String
    get() {
        return this.findAnnotation<Name>()?.value ?: this.name ?: throw IllegalStateException("参数没有名字")
    }

//label
val KClass<*>.userLabel: String
    get() {
        return this.findAnnotation<Label>()?.value ?: this.userName
    }

val KClass<*>.userDesc: String
    get() {
        val lb = this.findAnnotation<Label>()
        if (lb != null) {
            if (lb.desc.isNotEmpty()) {
                return lb.desc
            }
            if (lb.value.isNotEmpty()) {
                return lb.value
            }
        }
        return this.userName
    }

val KFunction<*>.userLabel: String
    get() {
        return this.findAnnotation<Label>()?.value ?: this.userName
    }
val KFunction<*>.userDesc: String
    get() {
        val lb = this.findAnnotation<Label>()
        if (lb != null) {
            if (lb.desc.isNotEmpty()) {
                return lb.desc
            }
            if (lb.value.isNotEmpty()) {
                return lb.value
            }
        }
        return this.userName
    }

val KProperty<*>.userLabel: String
    get() {
        return this.findAnnotation<Label>()?.value ?: this.userName
    }
val KParameter.userLabel: String
    get() {
        return this.findAnnotation<Label>()?.value ?: this.userName
    }

/**
 * hidden for client, when serialize to json
 */
val KProperty<*>.isHidden: Boolean
    get() {
        return this.hasAnnotation<Hidden>()
    }

val KAnnotatedElement.labelOnly: String?
    get() {
        return this.findAnnotation<Label>()?.value
    }
//inline fun <reified T : Annotation> KAnnotatedElement.hasAnnotation(): Boolean = null != this.findAnnotation<T>()
/**
 * fun main() {
 * 	val fm = DecimalFormat("#.###")
 * 	println(fm.maximumFractionDigits)
 * }
 *
 */
fun OptionList.toMap(): Map<String, String> {
    val map = LinkedHashMap<String, String>()
    this.options.map {
        if (":" in it) {
            val p = it.split(':')
            map[p[0].trim()] = p[1].trim()
        } else {
            map[it] = it
        }
    }
    return map
}

fun OptionList.display(v: Any): String {
    return this.toMap()[v.toString()] ?: ""
}

//fun DatePattern.display(v: Any): String {
//    return dateDisplay(v, this.format)
//}

//
//val KProperty<*>.fullName: String
//    get() {
//        val tabName = this.ownerClass!!.userName
//        val fname = this.findAnnotation<Name>()?.value ?: this.name
//        return "$tabName.$fname"
//    }