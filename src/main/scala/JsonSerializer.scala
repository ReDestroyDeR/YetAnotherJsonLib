package ru.red.json

import JsonValue.{JsonArray, JsonNull, JsonNumber, JsonObject, JsonString}

trait JsonSerializer[T] {
  def toJson(v: T): JsonValue
}
object JsonSerializer {
  def apply[T](f: T => JsonValue): JsonSerializer[T] = (v: T) => f(v)
  def from[A](implicit jS: JsonSerializer[A]): JsonSerializer[A] = jS

  implicit val byteSerializer: JsonSerializer[Byte] = JsonSerializer[Byte](i => JsonNumber(i))
  implicit val shortSerializer: JsonSerializer[Short] = JsonSerializer[Short](i => JsonNumber(i))
  implicit val intSerializer: JsonSerializer[Int] = JsonSerializer[Int](i => JsonNumber(i))
  implicit val floatSerializer: JsonSerializer[Float] = JsonSerializer[Float](i => JsonNumber(i))
  implicit val doubleSerializer: JsonSerializer[Double] = JsonSerializer[Double](i => JsonNumber(i))
  implicit val longSerializer: JsonSerializer[Long] = JsonSerializer[Long](i => JsonNumber(i))


  implicit val stringSerializer: JsonSerializer[String] = JsonSerializer[String](s => JsonString(s))

  implicit val nullSerializer: JsonSerializer[Null] = JsonSerializer[Null](n => JsonNull())

  implicit def seqSerializer[A: JsonSerializer]: JsonSerializer[Seq[A]] =
    JsonSerializer[Seq[A]](a => JsonArray(a.map(e => from[A].toJson(e))))

  implicit def mapSerializer[K: JsonSerializer, V: JsonSerializer]: JsonSerializer[Map[K, V]] =
    JsonSerializer[Map[K, V]](map =>
      JsonObject(map
        .map[JsonValue, JsonValue]
          (entry => (from[K].toJson(entry._1), from[V].toJson(entry._2)))
      )
    )
}
