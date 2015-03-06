package org.ababup1192

import org.ababup1192.util.ColoredRectangle.ColoredRectangle
import org.ababup1192.util.Draggable

import scalafx.Includes._
import scalafx.scene.Cursor
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

class DraggableRectangle(val initX: Double, val initY: Double, initColor: Color) extends Rectangle with Draggable {
  id = s"${hashCode()}:${initColor.toString()}"

  // Initial Position
  x = initX
  y = initY

  width = 100
  height = 100

  fill = initColor
  cursor = Cursor.HAND

  onMousePressed = {
    (mouseEvent: MouseEvent) =>
      fill = initColor
      if (isSingle) {
        initTranslateX = translateX()
        initTranslateY = translateY()

        dragAnchorX = mouseEvent.sceneX
        dragAnchorY = mouseEvent.sceneY

        this.toFront()

        invertColor()
      }
  }
  onMouseDragged = (mouseEvent: MouseEvent) => {
    // Move a rectangle when this object is single.
    if (isSingle) {
      val dragX = mouseEvent.sceneX - dragAnchorX
      val dragY = mouseEvent.sceneY - dragAnchorY

      translateRefX() = initTranslateX + dragX
      translateRefY() = initTranslateY + dragY

      invertColor()
    }

  }

  onMouseReleased = (mouseEvent: MouseEvent) => {

    sibling.foreach {
      // When target is a Rectangle
      case rect: javafx.scene.shape.Rectangle =>
        // Link process
        if (rect.getBoundsInParent.intersects(this.delegate.getBoundsInParent) && isSingle) {

          // Preparation

          // Reset Color
          this.fill = this.delegate.color
          rect.fill = rect.color

          // Grouping
          val parentChildren = parent.value.getScene.getChildren
          //val stackPane = new StackPane with Draggable
          //stackPane.setPrefSize(rect.getWidth, rect.getHeight)

          val stackPane = new StackPane with Draggable
          // stackPane.getChildren.addAll(Rectangle(100, 100, Color.Orange), Rectangle(50, 50, Color.Blue))

          val newRect = new Rectangle {
            height = 200
            width = 200
            fill = rect.color
          }

          val newThis = new Rectangle {
            height = 100
            width = 100
            fill = initColor
          }

          stackPane.children.addAll(newRect, newThis)
          parentChildren.removeAll(rect, this)
          parentChildren.add(stackPane)
        }
      case group: javafx.scene.Group =>
      case _ =>
    }
  }

  def isSingle = parent.value.getStyleClass.toString == "root"

  def sibling = parent.value.getChildrenUnmodifiable.filter(_ != this.delegate)

  /**
   * invert a Rectangle and Rectangles in Group
   */
  def invertColor() = {

    sibling.foreach {
      // When target is a Rectangle
      case rect: javafx.scene.shape.Rectangle =>
        if (rect.getBoundsInParent.intersects(this.delegate.getBoundsInParent)) {
          rect.fill = rect.color.invert
        } else {
          rect.fill = rect.color
        }

      /*
      // When target is a Group
      case group: javafx.scene.Group =>

        val doRect = (doRectInGroup _).curried(group)

        if (group.getBoundsInParent.intersects(this.delegate.getBoundsInParent)) {
          doRect(
            rect => {
              rect.fill = rect.color.invert
            }
          )
        } else {
          doRect(
            rect => {
              rect.fill = rect.color
            }
          )
        }
        */
      case _ =>
    }
  }
}

