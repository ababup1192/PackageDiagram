package org.ababup1192

import javafx.collections.ObservableList
import javafx.scene.{Node, input => jfxsi}

import org.ababup1192.util.Draggable

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.scene.control.{ContextMenu, MenuItem}
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.{Group, Scene}
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

  sceneContent += new DraggableRectangle(100d, 100d, Color.LightBlue)
  sceneContent += new DraggableRectangle(300d, 100d, Color.OrangeRed)

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
