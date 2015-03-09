package org.ababup1192.view

import scalafx.Includes._
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.stage.Stage

class PackageNameDialog {
  var packageName: Option[String] = None
  private[this] var textField: TextField = _

  def show(): Unit = {
    // Create dialog
    val dialogStage = new Stage {
      outer =>

      title = "New Package"

      width = 300
      height = 100

      // TextField
      textField = new TextField {
        onKeyPressed = (keyEvent: KeyEvent) => {
          if (keyEvent.getCode == KeyCode.ENTER.delegate) {
            okHandle()
            outer.close()
          }
        }
      }

      scene = new Scene {
        resizable = false
        root = new VBox {
          alignment = Pos.Center

          children = List(
            // Label and TextField
            new HBox {
              alignment = Pos.Center
              children = List(
                new Label("Name: "),
                textField
              )
              // OK and Cancel Button
            }, new HBox {
              alignment = Pos.Center
              children = List(
                new Button {
                  text = "Cancel"
                  onAction = handle {
                    outer.close()
                  }
                },
                new Button {
                  margin = Insets(10)
                  text = "OK"
                  onAction = handle {
                    okHandle()
                    outer.close()
                  }
                }
              )
            }
          )
        }
      }
    }
    dialogStage.showAndWait()
  }

  private[this] def okHandle(): Unit = {
    val text = textField.text()

    if (!text.trim.isEmpty) {
      packageName = Some(text)
    }
  }

}
