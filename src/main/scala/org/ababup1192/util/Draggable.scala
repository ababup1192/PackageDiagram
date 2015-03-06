package org.ababup1192.util

import scalafx.Includes._
import scalafx.beans.property.DoubleProperty
import scalafx.scene.input.MouseEvent
import scalafx.scene.{Cursor, Node}

trait Draggable extends Node {
  private[this] val translateRefX = new DoubleProperty
  private[this] val translateRefY = new DoubleProperty

  private[this] var dragAnchorX: Double = _
  private[this] var dragAnchorY: Double = _

  private[this] var initTranslateX: Double = _
  private[this] var initTranslateY: Double = _

  cursor = Cursor.HAND

  translateX <== translateRefX
  translateY <== translateRefY

  onMousePressed = (mouseEvent: MouseEvent) => {

    initTranslateX = translateX()
    initTranslateY = translateY()

    dragAnchorX = mouseEvent.sceneX
    dragAnchorY = mouseEvent.sceneY

    // set the init color and front
    this.toFront()
  }
  onMouseDragged = (mouseEvent: MouseEvent) => {
    val dragX = mouseEvent.sceneX - dragAnchorX
    val dragY = mouseEvent.sceneY - dragAnchorY

    translateRefX() = initTranslateX + dragX
    translateRefY() = initTranslateY + dragY
  }
}
