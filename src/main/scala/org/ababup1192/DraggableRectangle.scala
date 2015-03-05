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

    //reverse()
  }
  onMouseDragged = (mouseEvent: MouseEvent) => {
    val dragX = mouseEvent.sceneX - rectangleDragAnchorX
    val dragY = mouseEvent.sceneY - rectangleDragAnchorY

    if (parent.value.getStyleClass.toString == "root") {
      rectangleX() = initRectangleTranslateX + dragX
      rectangleY() = initRectangleTranslateY + dragY

      // reverse()
    }

    /* sibling.foreach { node =>
      node.translateX() = initRectangleTranslateX + dragX
      node.translateY() = initRectangleTranslateY + dragY
    }*/


    // reverse()
  }

  onMouseReleased = (mouseEvent: MouseEvent) => {
    /* val children = parent.value.getScene.getChildren.filter(_ != this.delegate)

     children.foreach {
       case rect: javafx.scene.shape.Rectangle =>
         if (rect.getBoundsInParent.intersects(this.delegate.getBoundsInParent)) {
           rect.setWidth(rect.getWidth * 2d)
           rect.setHeight(rect.getHeight * 2d)
         }
     }*/
  }

  def reverse() = {
    // get sibling
    val children = parent.value.getScene.getChildren.filter(_ != this.delegate)

    children.foreach {
      case rect: javafx.scene.shape.Rectangle =>

        DraggableRectangle.getColorCode(rect.getId).foreach {
          originalColorCode =>
            val originalColor = Color.web(originalColorCode)

            if (rect.getBoundsInParent.intersects(this.delegate.getBoundsInParent)) {
              rect.setFill(originalColor.invert)
            } else {
              rect.setFill(originalColor)
            }
        }
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