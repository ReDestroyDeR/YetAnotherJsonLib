package ru.red

package object json {
  implicit class JsonSerializerSyntax[T: JsonSerializer](v: T) {
    def >->>(): JsonValue = JsonSerializer.from[T].toJson(v)
  }

  implicit class JsonDeserializerSyntax(v: JsonValue) {
    def <<-<[T: JsonDeserializer](): T = JsonDeserializer.from[T].fromJson(v)
  }
}
