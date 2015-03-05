package org.ababup1192

import javafx.collections.ObservableList
import javafx.scene.{Node, input => jfxsi}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.scene.{Group, Scene}
import scalafx.scene.control.{ContextMenu, MenuItem}
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
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

  val group = new Group()

  // group.children.add(new DraggableRectangle(100d, 100d, Color.LightBlue))
  // group.children.add(new DraggableRectangle(200d, 200d, Color.OrangeRed))

  // sceneContent += group
  sceneContent += new DraggableRectangle(100d, 100d, Color.LightBlue)
  sceneContent += new DraggableRectangle(200d, 200d, Color.OrangeRed)

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
