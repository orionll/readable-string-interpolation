package com.github.orionll.scala

import org.scalatest.FunSuite
import scala.collection.immutable.Map

import ReadableStringInterpolation._

class TestReadableStringInterpolation extends FunSuite {
  val N = System.getProperty("line.separator")

  test("1") {
    assert(nice"""
    SELECT * FROM TABLE
    WHERE id = ${5} OR id = ${6}
    """ ===
      s"SELECT * FROM TABLE${N}WHERE id = 5 OR id = 6")
  }

  test("2") {
    assert(nice"""
    SELECT * FROM TABLE
    WHERE
      id = ${5} OR
      id = ${6}
    """ ===
      s"SELECT * FROM TABLE${N}WHERE${N}  id = 5 OR${N}  id = 6")
  }

  test("3") {
    assert(nice"""  
      SELECT * FROM TABLE

      WHERE id = ${5} OR id = ${6}
    """ ===
      s"SELECT * FROM TABLE${N}${N}WHERE id = 5 OR id = 6")
  }

  test("4") {
    assert(nice"""  
      SELECT * FROM TABLE
    
      WHERE id = ${5} OR id = ${6}
        
    """ ===
      s"  SELECT * FROM TABLE${N}${N}  WHERE id = 5 OR id = 6${N}")
  }

  test("5") {
    assert(nice"""SELECT * FROM TABLE""" === s"""SELECT * FROM TABLE""")
  }

  test("6") {
    assert(nice"""
      SELECT * FROM TABLE""" === s"""SELECT * FROM TABLE""")
  }

  test("7") {
    assert(nice"""
      SELECT * FROM TABLE
    """ === s"""SELECT * FROM TABLE""")
  }

  test("8") {
    assert(nice"""
		SELECT * FROM TABLE
		WHERE id = ${5} OR id = ${6}
    """ ===
      s"SELECT * FROM TABLE${N}WHERE id = 5 OR id = 6")
  }

  test("9") {
    assert(nice"""
		  SELECT * FROM TABLE
		WHERE id = ${5} OR id = ${6}
    """ ===
      s"  SELECT * FROM TABLE${N}WHERE id = 5 OR id = 6")
  }

  test("10") {
    assert(nice"""
      <book>
        <title>${"Title"}</title>
        <author>${"Author"}</author>
      </book>
    """ ===
      s"<book>${N}  <title>Title</title>${N}  <author>Author</author>${N}</book>")
  }

  test("11") {
    assert(nice"""
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
    assert(nice"""${5}""" === "5")
  }

  test("13") {
    assert(nice"""
      ${4}
      ${5}
    """ === s"4${N}5")
  }

  test("14") {
    val xml = nice"""
      <title>${"Title"}</title>
      <author>${"Author"}</author>
    """

    assert(xml === s"<title>Title</title>${N}<author>Author</author>")

    assert(nice"""
      <book>
        $xml
      </book>
    """ ===
      s"<book>${N}  <title>Title</title>${N}  <author>Author</author>${N}</book>")
  }

  test("15") {
    assert(nice" test" === " test")
  }

  test("16") {
    assert(nice" ${5}" === " 5")
  }

  test("17") {
    val xml = nice"""
      <title>${"Title"}</title>
      <author>${"Author"}</author>
    """

    assert(xml === s"<title>Title</title>${N}<author>Author</author>")

    assert(nice"""this is $xml""" ===
      s"this is <title>Title</title>${N}<author>Author</author>")
  }

  test("18") {
    val xml = nice"""
      <title>${"Title"}</title>
      <author>${"Author"}</author>
    """

    assert(xml === s"<title>Title</title>${N}<author>Author</author>")

    assert(nice"""
      this is $xml""" ===
      s"this is <title>Title</title>${N}<author>Author</author>")
  }

  test("19") {
    val xml = nice"""
      <title>${"Title"}</title>
      <author>${"Author"}</author>
    """

    assert(xml === s"<title>Title</title>${N}<author>Author</author>")

    assert(nice"""
      text:
      	xml = $xml
    """ ===
      s"text:${N}\txml = <title>Title</title>${N}\t<author>Author</author>")
  }

  test("20") {
    val x = nice"""
      line1
      line2
    """
    assert(x === s"line1${N}line2")

    val str = nice"""
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
    val x = nice"""
      line1
      line2
    """
    assert(x === s"line1${N}line2")

    val str = nice"""
      txt1
        txt2: ${x}
      txt3
        txt4
          txt5: ${x}
    """

    assert(str === s"txt1${N}  txt2: line1${N}  line2${N}txt3${N}  txt4${N}    txt5: line1${N}    line2")
  }

  test("22") {
    val i = "<img />"

    val p = nice"""
      <p>line1</p>
      <p>line2</p>
    """

    val html = nice"""
      <html>
        <body>
          abc $i $p
        </body>
      </html>
    """

    assert(html === s"<html>${N}  <body>${N}    abc <img /> <p>line1</p>${N}    <p>line2</p>${N}  </body>${N}</html>")
  }
}