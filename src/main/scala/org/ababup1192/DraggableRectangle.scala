package org.ababup1192

import scalafx.Includes._
import scalafx.beans.property.DoubleProperty
import scalafx.scene.Cursor
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

class DraggableRectangle(val initX: Double, val initY: Double, color: Color) extends Rectangle {

  private[this] val rectangleX = new DoubleProperty
  private[this] val rectangleY = new DoubleProperty

  private[this] var rectangleDragAnchorX: Double = _
  private[this] var rectangleDragAnchorY: Double = _

  private[this] var initRectangleTranslateX: Double = _
  private[this] var initRectangleTranslateY: Double = _

  // Initial Position
  x = initX
  y = initY

  width = 100
  height = 100

  fill = color
  cursor = Cursor.HAND

  translateX <== rectangleX
  translateY <== rectangleY

  onMousePressed = (mouseEvent: MouseEvent) => {
    initRectangleTranslateX = translateX()
    initRectangleTranslateY = translateY()

    rectangleDragAnchorX = mouseEvent.sceneX
    rectangleDragAnchorY = mouseEvent.sceneY
  }
  onMouseDragged = (mouseEvent: MouseEvent) => {
    val dragX = mouseEvent.sceneX - rectangleDragAnchorX
    val dragY = mouseEvent.sceneY - rectangleDragAnchorY

    rectangleX() = initRectangleTranslateX + dragX
    rectangleY() = initRectangleTranslateY + dragY
    val children = parent.value.getScene.getChildren.filter(_ != this.delegate)
    children.foreach {
      case rect: javafx.scene.shape.Rectangle =>
        if (rect.getBoundsInParent.intersects(this.delegate.getBoundsInParent)) {
          println("重なった")
        } else{
          println("重なってない")
        }
    }
  }

  onMouseReleased = (mouseEvent: MouseEvent) => {
    println(s"released: x = ${mouseEvent.sceneX}, y = ${mouseEvent.sceneY}")


  }

}

