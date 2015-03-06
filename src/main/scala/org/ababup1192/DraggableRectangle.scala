package org.ababup1192

import org.ababup1192.util.Draggable

import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.{Cursor, Group}

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
          doRectWithColor(this,
            originalColor =>
              this.fill = originalColor
          )

          doRectWithColor(rect,
            originalColor =>
              rect.setFill(originalColor)
          )

          val ration = 2.0
          rect.setHeight(rect.getHeight * ration)
          rect.setWidth(rect.getWidth * ration)
          this.translateRefX() = rect.layoutX() - rect.getWidth + this.getWidth / ration
          this.translateRefY() = rect.layoutY() + rect.getHeight / ration - this.getHeight / ration

          // Grouping
          val parentChildren = parent.value.getScene.getChildren
          val group = new Group with Draggable

          group.children.addAll(rect, this)
          parentChildren.add(group)
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