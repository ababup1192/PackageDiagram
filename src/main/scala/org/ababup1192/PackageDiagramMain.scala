package org.ababup1192

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.DoubleProperty
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.{Cursor, Scene}

object PackageDiagramMain extends JFXApp {

  val rectangleX = new DoubleProperty
  val rectangleY = new DoubleProperty

  var rectangleDragAnchorX: Double = _
  var rectangleDragAnchorY: Double = _

  var initRectangleTranslateX: Double = _
  var initRectangleTranslateY: Double = _

  val rectangle = new Rectangle {
    x = 25
    y = 40
    width = 100
    height = 100
    fill = Color.LightBlue
    cursor = Cursor.HAND
    translateX <== rectangleX
    translateY <== rectangleY
    onMousePressed = (mouseEvent: MouseEvent) => {
      initRectangleTranslateX = translateX()
      initRectangleTranslateY = translateY()

      rectangleDragAnchorX = mouseEvent.sceneX
      rectangleDragAnchorY = mouseEvent.sceneY
      println(s"mousePressed: x = $initRectangleTranslateX, y = $initRectangleTranslateY")
    }
    onMouseDragged = (mouseEvent: MouseEvent) => {
      val dragX = mouseEvent.sceneX - rectangleDragAnchorX
      val dragY = mouseEvent.sceneY - rectangleDragAnchorY

      rectangleX() = initRectangleTranslateX + dragX
      rectangleY() = initRectangleTranslateY + dragY
      println(s"dragged: x = ${rectangleX()}, y = ${rectangleY()}")
    }
  }

  stage = new JFXApp.PrimaryStage {
    title.value = "Package Diagram"
    width = 600
    height = 450
    scene = new Scene {
      content = rectangle
    }
  }
}
