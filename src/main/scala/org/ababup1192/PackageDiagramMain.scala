package org.ababup1192

import javafx.scene.{input => jfxsi}

import org.ababup1192.view.{PackageBox, PackageNameDialog}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{BorderPane, Pane, VBox}

object PackageDiagramMain extends JFXApp {

  val centerPane = new Pane

  stage = new JFXApp.PrimaryStage {
    title.value = "Package Diagram"
    width = 600
    height = 450

    scene = new Scene {

      root = new BorderPane {
        top = new VBox {
          children = List(
            createToolBar()
          )
        }
        center = centerPane
      }
    }
  }

  private def createToolBar(): ToolBar = {
    val toolBar = new ToolBar {
      content = List(
        new Button {
          id = "newButton"
          graphic = new ImageView(new Image(this, "/images/btn_package.png"))
          tooltip = Tooltip("New Package... Ctrl+N")
          onAction = handle {
            showPackageNameDialog()
          }
        })
    }
    toolBar
  }

  private def showPackageNameDialog(): Unit = {
    val packageNameDialog = new PackageNameDialog
    packageNameDialog.show()
    packageNameDialog.packageName.foreach {
      name =>
        centerPane.children += PackageBox(name)
    }
  }

  // sceneContent += PackageBox("world")

  /*
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
  */
}
