package com.github.orionll.scala

import org.scalatest.FunSuite
import scala.collection.immutable.Map

import ReadableStringInterpolation._

class TestReadableStringInterpolation extends FunSuite {
  val N = System.getProperty("line.separator")

  test("1") {
    assert(is"""
    SELECT * FROM TABLE
    WHERE id = ${5} OR id = ${6}
    """ ===
      s"SELECT * FROM TABLE${N}WHERE id = 5 OR id = 6")
  }

  test("2") {
    assert(is"""
    SELECT * FROM TABLE
    WHERE
      id = ${5} OR
      id = ${6}
    """ ===
      s"SELECT * FROM TABLE${N}WHERE${N}  id = 5 OR${N}  id = 6")
  }

  test("3") {
    assert(is"""  
      SELECT * FROM TABLE

      WHERE id = ${5} OR id = ${6}
    """ ===
      s"SELECT * FROM TABLE${N}${N}WHERE id = 5 OR id = 6")
  }

  test("4") {
    assert(is"""  
      SELECT * FROM TABLE
    
      WHERE id = ${5} OR id = ${6}
        
    """ ===
      s"  SELECT * FROM TABLE${N}${N}  WHERE id = 5 OR id = 6${N}")
  }

  test("5") {
    assert(is"""SELECT * FROM TABLE""" === s"""SELECT * FROM TABLE""")
  }

  test("6") {
    assert(is"""
      SELECT * FROM TABLE""" === s"""SELECT * FROM TABLE""")
  }

  test("7") {
    assert(is"""
      SELECT * FROM TABLE
    """ === s"""SELECT * FROM TABLE""")
  }

  test("8") {
    assert(is"""
		SELECT * FROM TABLE
		WHERE id = ${5} OR id = ${6}
    """ ===
      s"SELECT * FROM TABLE${N}WHERE id = 5 OR id = 6")
  }

  test("9") {
    assert(is"""
		  SELECT * FROM TABLE
		WHERE id = ${5} OR id = ${6}
    """ ===
      s"  SELECT * FROM TABLE${N}WHERE id = 5 OR id = 6")
  }

  test("10") {
    assert(is"""
      <book>
        <title>${"Title"}</title>
        <author>${"Author"}</author>
      </book>
    """ ===
      s"<book>${N}  <title>Title</title>${N}  <author>Author</author>${N}</book>")
  }

  test("11") {
    assert(is"""
      def removeEndSpaces(lines: Seq[String]): Seq[String] = {
        lines match {
          case Seq() => Seq()
          case _ if (isOnlySpacesOrTabs(lines.last)) => lines.dropRight(1)
          case _ => lines
        }
      }
    """ === s"def removeEndSpaces(lines: Seq[String]): Seq[String] = {${N}  lines match {${N}    case Seq() => Seq()${N}    case _ if (isOnlySpacesOrTabs(lines.last)) => lines.dropRight(1)${N}    case _ => lines${N}  }${N}}")
  }

  test("12") {
    assert(is"""${5}""" === "5")
  }

  test("13") {
    assert(is"""
      ${4}
      ${5}
    """ === s"4${N}5")
  }

  test("14") {
    val xml = is"""
      <title>${"Title"}</title>
      <author>${"Author"}</author>
    """

    assert(xml === s"<title>Title</title>${N}<author>Author</author>")

    assert(is"""
      <book>
        $xml
      </book>
    """ ===
      s"<book>${N}  <title>Title</title>${N}  <author>Author</author>${N}</book>")
  }

  test("15") {
    assert(is" test" === " test")
  }

  test("16") {
    assert(is" ${5}" === " 5")
  }

  test("17") {
    val xml = is"""
      <title>${"Title"}</title>
      <author>${"Author"}</author>
    """

    assert(xml === s"<title>Title</title>${N}<author>Author</author>")

    assert(is"""this is $xml""" ===
      s"this is <title>Title</title>${N}<author>Author</author>")
  }

  test("18") {
    val xml = is"""
      <title>${"Title"}</title>
      <author>${"Author"}</author>
    """

    assert(xml === s"<title>Title</title>${N}<author>Author</author>")

    assert(is"""
      this is $xml""" ===
      s"this is <title>Title</title>${N}<author>Author</author>")
  }

  test("19") {
    val xml = is"""
      <title>${"Title"}</title>
      <author>${"Author"}</author>
    """

    assert(xml === s"<title>Title</title>${N}<author>Author</author>")

    assert(is"""
      text:
      	xml = $xml
    """ ===
      s"text:${N}\txml = <title>Title</title>${N}\t<author>Author</author>")
  }

  test("20") {
    val x = is"""
      line1
      line2
    """
    assert(x === s"line1${N}line2")

    val str = is"""
      txt1
        txt2:
          ${x}
      txt3
        txt4
          txt5:
            ${x}
    """


    assert(str === s"txt1${N}  txt2:${N}    line1${N}    line2${N}txt3${N}  txt4${N}    txt5:${N}      line1${N}      line2")
  }

  test("21") {
    val x = is"""
      line1
      line2
    """
    assert(x === s"line1${N}line2")

    val str = is"""
      txt1
        txt2: ${x}
      txt3
        txt4
          txt5: ${x}
    """

    assert(str === s"txt1${N}  txt2: line1${N}  line2${N}txt3${N}  txt4${N}    txt5: line1${N}    line2")
  }
}