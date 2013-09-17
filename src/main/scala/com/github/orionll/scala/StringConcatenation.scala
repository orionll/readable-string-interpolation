package com.github.orionll.scala

private class StringConcatenation {
  private val builder = new StringBuilder

  def newLine(): Unit = builder.append(StringConcatenation.LineDelimiter)

  def append(obj: Any): Unit = builder.append(obj)

  def append(obj: Any, prefix: String): Unit = {
    if (obj == null) {
      builder.append(obj)
      return
    }

    val lines = StringConcatenation.split(obj.toString)

    lines.size match {
      case 1 => builder.append(lines.head)
      case _ => {
        builder.append(lines.head)
        for (line <- lines.view(1, lines.size)) {
          newLine()
          if (!line.isEmpty) {
            builder.append(prefix)
          }
          builder.append(line)
        }
      }
    }
  }

  override def toString() = builder.toString
}

private object StringConcatenation {
  val LineDelimiter = System.getProperty("line.separator")

  def split(str: String) = str.split("\\r?\\n", -1)
}