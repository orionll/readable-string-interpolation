Readable string interpolation for Scala
=============================

Multiline strings in Scala have a well-known issue: they produce annoying leading whitespaces.  
For example:
```scala
object MyApp extends App {
  val html = """
    <html>
      <body>
        <p>Lorem ipsum dolor sit amet...</p>
      </body>
    </html>
  """
  
  println(html)
}
```
Running this code produces:
```

····<html>
······<body>
········<p>Lorem ipsum dolor sit amet...</p>
······</body>
····</html>
··  
```

As you see, there are:
* One extra line at the beginning
* Unnecessary leading spaces before html markup
* One extra line _with two spaces_ at the end

**This small library can solve this problem.**
Everything you need is to put `nice` before your multiline string:

```scala
import com.github.orionll.scala.ReadableStringInterpolation._

object MyApp extends App {
  val html = nice"""
    <html>
      <body>
        <p>Lorem ipsum dolor sit amet...</p>
      </body>
    </html>
  """
  
  println(html)
}
```
Now this code produces:
```html
<html>
  <body>
    <p>Lorem ipsum dolor sit amet...</p>
  </body>
</html>
```

## Expressions
`nice` also plays well with expressions inside strings:
```scala
import com.github.orionll.scala.ReadableStringInterpolation._

object MyApp extends App {
  val paragraphs = nice"""
    <p>line1</p>
    <p>line2</p>
  """
  
  val html = nice"""
    <html>
      <body>
        $paragraphs
      </body>
    </html>
  """
  
  println(html)
}
```

The output will be exactly you expect it to be:
```html
<html>
  <body>
    <p>line1</p>
    <p>line2</p>
  </body>
</html>
```
(Now try to run this code with standard `s` instead of `nice` and compare the resulting mess with this)


=============================

If you would like to have this library in the central repository, please let me know.
