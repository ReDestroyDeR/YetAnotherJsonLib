package ru.red.json

import JsonValue.{JsonArray, JsonNull, JsonNumber, JsonObject, JsonString}

trait JsonDeserializer[T] {
  def fromJson(v: JsonValue): T
}
object JsonDeserializer {
  def apply[T](f: JsonValue => T): JsonDeserializer[T] = (v: JsonValue) => f(v)
  def from[A](implicit jS: JsonDeserializer[A]): JsonDeserializer[A] = jS

  implicit val byteDeserializer: JsonDeserializer[Byte] = JsonDeserializer[Byte](i => i.asInstanceOf[JsonNumber].value.toByte)
  implicit val shortDeserializer: JsonDeserializer[Short] = JsonDeserializer[Short](i => i.asInstanceOf[JsonNumber].value.toShort)
  implicit val intDeserializer: JsonDeserializer[Int] = JsonDeserializer[Int](i => i.asInstanceOf[JsonNumber].value.toInt)
  implicit val floatDeserializer: JsonDeserializer[Float] = JsonDeserializer[Float](i => i.asInstanceOf[JsonNumber].value.toFloat)
  implicit val doubleDeserializer: JsonDeserializer[Double] = JsonDeserializer[Double](i => i.asInstanceOf[JsonNumber].value)
  implicit val longDeserializer: JsonDeserializer[Long] = JsonDeserializer[Long](i => i.asInstanceOf[JsonNumber].value.toLong)


  implicit val stringDeserializer: JsonDeserializer[String] = JsonDeserializer[String](s => s.asInstanceOf[JsonString].value)

  implicit val nullDeserializer: JsonDeserializer[Null] = JsonDeserializer[Null](_ => null)

  implicit def seqDeserializer[A: JsonDeserializer]: JsonDeserializer[Seq[A]] =
    JsonDeserializer[Seq[A]](a => a.asInstanceOf[JsonArray[JsonValue]].value.map(b => from[A].fromJson(b)))

  implicit def mapDeserializer[K, V]
                              (implicit k: JsonDeserializer[K],
                                        v: JsonDeserializer[V]): JsonDeserializer[Map[K, V]] =
    JsonDeserializer[Map[K, V]](map =>
      map.asInstanceOf[JsonObject[Map,JsonValue,JsonValue]].value
        .map(entry => (
          k.fromJson(entry._1),
          v.fromJson(entry._2)
        ))
    )
}
