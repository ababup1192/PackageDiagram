package org.ababup1192.util

import scalafx.scene.paint.Color

object ColoredRectangle {

  implicit class ColoredRectangle(rectangle: javafx.scene.shape.Rectangle) {
      def color: Color = {
        getColorCode(rectangle.getId).fold(Color.Black){
          colorCode =>
            Color.web(colorCode)
        }
      }
  }

  def getColorCode(id: String): Option[String] = {
    val idMatcher = """(\d+).:\[SFX\]0x(\w+)""".r
    id match {
      case idMatcher(hash, color) =>
        Some(color)
      case _ =>
        None
    }
  }

}
