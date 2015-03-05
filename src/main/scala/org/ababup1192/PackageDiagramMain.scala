package org.ababup1192

import javafx.collections.ObservableList
import javafx.scene.{Node, input => jfxsi}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.DoubleProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.{ContextMenu, MenuItem}
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.{Cursor, Group, Scene}
import scalafx.stage.Window

object PackageDiagramMain extends JFXApp {

  var sceneContent: ObservableList[Node] = _

  stage = new JFXApp.PrimaryStage {
    title.value = "Package Diagram"
    width = 600
    height = 450
    scene = new Scene {
      sceneContent = content
      onMousePressed = (mouseEvent: MouseEvent) => {
        if (mouseEvent.getButton == jfxsi.MouseButton.SECONDARY) {
          val rightClickMenu = createMenu(stage, mouseEvent.sceneX, mouseEvent.sceneY)

          rightClickMenu.show(stage, mouseEvent.sceneX, mouseEvent.sceneY)
        }
      }
    }
  }

  val group = new Group {
    private[this] val groupX = new DoubleProperty
    private[this] val groupY = new DoubleProperty

    private[this] var groupDragAnchorX: Double = _
    private[this] var groupDragAnchorY: Double = _

    private[this] var initGroupTranslateX: Double = _
    private[this] var initGroupTranslateY: Double = _

    cursor = Cursor.HAND

    translateX <== groupX
    translateY <== groupY

    onMousePressed = (mouseEvent: MouseEvent) => {

      initGroupTranslateX = translateX()
      initGroupTranslateY = translateY()

      groupDragAnchorX = mouseEvent.sceneX
      groupDragAnchorY = mouseEvent.sceneY

      // set the init color and front
      this.toFront()
    }
    onMouseDragged = (mouseEvent: MouseEvent) => {
      val dragX = mouseEvent.sceneX - groupDragAnchorX
      val dragY = mouseEvent.sceneY - groupDragAnchorY

      groupX() = initGroupTranslateX + dragX
      groupY() = initGroupTranslateY + dragY

    }
  }

  group.children.add(new DraggableRectangle(100d, 100d, Color.LightBlue))
  group.children.add(new DraggableRectangle(300d, 100d, Color.OrangeRed))

  sceneContent += group

  def createMenu(window: Window, x: Double, y: Double) = new ContextMenu(
    new MenuItem("LightBlue Rectangle") {
      onAction = {
        actionEvent: ActionEvent =>
          sceneContent += new DraggableRectangle(x, y, Color.LightBlue)
      }
    },
    new MenuItem("OrangeRed Rectangle") {
      onAction = {
        actionEvent: ActionEvent =>
          sceneContent += new DraggableRectangle(x, y, Color.OrangeRed)
      }
    }
  )
}
