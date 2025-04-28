@file:Suppress("unused")

package io.github.yangentao.anno

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModelTable(
    val version: Int = 0
)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModelView(
    val version: Int = 0
)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModelField(
    val primaryKey: Boolean = false,
    //0:false;  >0 : auto inc and set as  start value
    val autoInc: Int = 0,
    val unique: Boolean = false,
    val uniqueName: String = "",
    val index: Boolean = false,
    val notNull: Boolean = false,
    val defaultValue: String = "",
)

//DecimalFormat
//@Decimal(11, 2, "0.00")
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Decimal(val precision: Int = 11, val scale: Int = 2, val pattern: String = "")

//------------------------------------------------

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SQLFunction(val value: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SQLProcedure(val value: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ParamIn

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ParamOut

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ParamInOut

//自动创建表
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoCreateTable(val value: Boolean = true)
