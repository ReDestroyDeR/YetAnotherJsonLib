package ru.red.json

sealed trait JsonValue
object JsonValue {
  final case class JsonObject[M[_, _] <: Map[_, _], K <: JsonValue, V <: JsonValue](value: M[K, V]) extends JsonValue
  final case class JsonArray[T <: JsonValue](value: Seq[T]) extends JsonValue
  final case class JsonString(value: String) extends JsonValue
  final case class JsonNumber(value: Double) extends JsonValue
  final case class JsonBoolean(value: Boolean) extends JsonValue
  final case class JsonNull() extends JsonValue
}
