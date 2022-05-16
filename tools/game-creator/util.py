from PyQt5.QtWidgets import QMainWindow, QMessageBox, QInputDialog, QFileDialog, QDesktopWidget
import gamesdk as gsdk

LABEL_HEIGHT = 25
BUTTON_HEIGHT = 40

BETWEEN_ELEMENTS_VERTICAL = 7
BETWEEN_ELEMENTS_HORIZONTAL = 7

BORDER_1PX_BLACK_STYLE = 'border: 1px solid black;'
PADDING_LEFT_1PX_STYLE = 'padding-left: 1px;'

def show_message_box(text: str, buttons=QMessageBox.Ok):
    msg = QMessageBox()
    msg.setText(text)
    msg.setStandardButtons(buttons)
    return msg.exec_()

def prompt_string(parent: QMainWindow, title: str, label: str, text: str = '') -> tuple[str, bool]:
    return QInputDialog.getText(parent, title, label, text=text)

def prompt_directory(parent: QMainWindow):
    return str(QFileDialog.getExistingDirectory(parent, "Select game directory"))

def move_to_center(window: QMainWindow):
    qtRectangle = window.frameGeometry()
    centerPoint = QDesktopWidget().availableGeometry().center()
    qtRectangle.moveCenter(centerPoint)
    window.move(qtRectangle.topLeft())

def show_not_save_ready(error: gsdk.CheckError):
    reasons = '\n'.join(list(error.problems))
    show_message_box(f'Not save ready, reasons:\n{reasons}')