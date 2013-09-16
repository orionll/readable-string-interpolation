package com.github.orionll.scala

import org.eclipse.xtend2.lib.StringConcatenation
import scala.language.postfixOps

object ReadableStringInterpolation {

  implicit class ReadableStringContext(val stringContext: StringContext) extends AnyVal {

    def is(args: Any*): String = {
      val lineGroups = stringContext.parts.map(_.split("\\r?\\n").toSeq)

      if (lineGroups.forall(_.size == 1)) {
        stringContext.s(args: _*)
      } else {
        interpolate(args, removeStartSpacesFromFirstGroupAndEndSpacesFromLastGroup(lineGroups))
      }
    }
  }

  private def interpolate(args: Seq[Any], lineGroups: Seq[Seq[String]]): String = {
    val individualLines = lineGroups.head ++ lineGroups.tail.flatMap(_.drop(1))

    val indentationLength = findCommonIndentation(individualLines.filterNot(_.isEmpty))

    val lineGroupsIterator = lineGroups.iterator
    val expressionsIterator = args.iterator

    val buf = new StringConcatenation
    var prefix = appendLineGroup(lineGroupsIterator.next, indentationLength, true, buf)

    while (lineGroupsIterator.hasNext) {
      buf.append(expressionsIterator.next, prefix)
      prefix = appendLineGroup(lineGroupsIterator.next, indentationLength, false, buf)
    }

    buf.toString
  }

  private def removeStartSpacesFromFirstGroupAndEndSpacesFromLastGroup(lineGroups: Seq[Seq[String]]): Seq[Seq[String]] = {
    lineGroups match {
      case Seq(lines) => Seq(removeStartSpaces(removeEndSpaces(lines)))
      case _ => removeStartSpaces(lineGroups.head) +: lineGroups.slice(1, lineGroups.size - 1) :+ removeEndSpaces(lineGroups.last)
    }
  }

  private def removeStartSpaces(lines: Seq[String]): Seq[String] = {
    lines match {
      case Seq() => Seq()
      case _ if (isOnlySpacesOrTabs(lines.head)) => lines.drop(1)
      case _ => lines
    }
  }

  private def removeEndSpaces(lines: Seq[String]): Seq[String] = {
    lines match {
      case Seq() => Seq()
      case _ if (isOnlySpacesOrTabs(lines.last)) => lines.dropRight(1)
      case _ => lines
    }
  }

  private def isOnlySpacesOrTabs(str: String): Boolean = {
    str.forall('\t' ==) || str.forall(' ' ==)
  }

  private def findWhiteSpacePrefix(str: String): String = {
    str.takeWhile(c => c == '\t' || c == ' ')
  }

  private def prefix(line: String): (Int, Char) = {
    val spaces = line.takeWhile(' ' ==).size
    if (spaces > 0) {
      spaces -> ' '
    } else {
      val tabs = line.takeWhile('\t' ==).size
      tabs -> '\t'
    }
  }

  private def findCommonIndentation(lines: Seq[String]): Int = {
    val (prefixLengths, prefixCharacters) = lines.map(prefix).unzip

    prefixCharacters match {
      case Seq() => 0
      case chars if chars.forall(' ' ==) => prefixLengths.min
      case chars if chars.forall('\t' ==) => prefixLengths.min
      case _ => 0
    }
  }

  private def appendLineGroup(lines: Seq[String], indentationLength: Int, firstLineGroup: Boolean, buf: StringConcatenation): String = {
    lines match {
      case Seq() => ""

      case Seq(line) => {
        if (firstLineGroup) {
          val lineCut = line.drop(indentationLength)
          buf.append(lineCut)
          findWhiteSpacePrefix(lineCut)
        } else {
          buf.append(line)
          ""
        }
      }

      case Seq(head, tail @ _*) => {
        if (firstLineGroup) {
          buf.append(head.drop(indentationLength))
        } else {
          buf.append(head)
        }

        for (line <- tail) {
          buf.newLine()
          if (!line.isEmpty) {
            buf.append(line.drop(indentationLength))
          }
        }

        if (tail.last.isEmpty) {
          ""
        } else {
          findWhiteSpacePrefix(tail.last.drop(indentationLength))
        }
      }
    }
  }
}