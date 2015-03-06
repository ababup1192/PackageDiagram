package org.ababup1192

import scalafx.Includes._
import scalafx.beans.property.DoubleProperty
import scalafx.scene.Cursor
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

class DraggableRectangle(val initX: Double, val initY: Double, initColor: Color) extends Rectangle {

  private[this] val rectangleX = new DoubleProperty
  private[this] val rectangleY = new DoubleProperty

  private[this] var rectangleDragAnchorX: Double = _
  private[this] var rectangleDragAnchorY: Double = _

  private[this] var initRectangleTranslateX: Double = _
  private[this] var initRectangleTranslateY: Double = _

  id = s"${hashCode()}:${initColor.toString()}"

  // Initial Position
  x = initX
  y = initY

  width = 100
  height = 100

  fill = initColor
  cursor = Cursor.HAND

  translateX <== rectangleX
  translateY <== rectangleY

  onMousePressed = (mouseEvent: MouseEvent) => {

    initRectangleTranslateX = translateX()
    initRectangleTranslateY = translateY()

    rectangleDragAnchorX = mouseEvent.sceneX
    rectangleDragAnchorY = mouseEvent.sceneY

    // set the init color and front
    fill = initColor
    this.toFront()

    invertColor()
  }
  onMouseDragged = (mouseEvent: MouseEvent) => {
    val dragX = mouseEvent.sceneX - rectangleDragAnchorX
    val dragY = mouseEvent.sceneY - rectangleDragAnchorY

    // Move a rectangle when this object is single.
    if (parent.value.getStyleClass.toString == "root") {
      rectangleX() = initRectangleTranslateX + dragX
      rectangleY() = initRectangleTranslateY + dragY

      invertColor()
    }

  }

  onMouseReleased = (mouseEvent: MouseEvent) => {

  }

  def sibling = parent.value.getChildrenUnmodifiable.filter(_ != this.delegate)


  /**
   * invert a Rectangle and Rectangles in Group
   */
  def invertColor() = {

    sibling.foreach {
      // When target is a Rectangle
      case rect: javafx.scene.shape.Rectangle =>
        doRectWithColor(rect,
          originalColor =>
            if (rect.getBoundsInParent.intersects(this.delegate.getBoundsInParent)) {
              rect.setFill(originalColor.invert)
            } else {
              rect.setFill(originalColor)

            }
        )
      // When target is a Group
      case group: javafx.scene.Group =>

        val doRect = (doRectInGroup _).curried(group)

        if (group.getBoundsInParent.intersects(this.delegate.getBoundsInParent)) {
          doRect(
            rect => {
              doRectWithColor(rect,
                originalColor =>
                  rect.setFill(originalColor.invert)
              )
            }
          )
        } else {
          doRect(
            rect => {
              doRectWithColor(rect,
                originalColor =>
                  rect.setFill(originalColor)
              )
            }
          )
        }
    }
  }

  def doRectWithColor(rectangle: javafx.scene.shape.Rectangle, func: (Color) => Unit): Unit = {
    DraggableRectangle.getColorCode(rectangle.getId).foreach {
      originalColorCode =>
        func(Color.web(originalColorCode))
    }
  }

  def doRectInGroup(group: javafx.scene.Group, func: (javafx.scene.shape.Rectangle) => Unit): Unit = {
    group.children.foreach {
      case rect: javafx.scene.shape.Rectangle =>
        func(rect)
      case _ =>
    }
  }
}

object DraggableRectangle {
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