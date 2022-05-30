from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *
import gamesdk as gsdk

import util

from ContainerEditor import ContainerItemEditorWindow

WINDOW_TITLE = 'Class Editor'

LABEL_WIDTH = 120
LABEL_HEIGHT = util.LABEL_HEIGHT
ELEMENT_HEIGHT = util.LABEL_HEIGHT
ELEMENT_WIDTH = 200
ELEMENT_HEIGHT = util.LABEL_HEIGHT
LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE

NAME_LABEL_TEXT = 'Class name:'
NAME_LABEL_Y = util.BETWEEN_ELEMENTS_VERTICAL
NAME_LABEL_X = util.BETWEEN_ELEMENTS_HORIZONTAL
LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

NAME_EDIT_Y = NAME_LABEL_Y
NAME_EDIT_X = NAME_LABEL_X + util.BETWEEN_ELEMENTS_HORIZONTAL + LABEL_WIDTH

ITEMS_LIST_HEIGHT = 100
ITEMS_LIST_WIDTH = util.BETWEEN_ELEMENTS_HORIZONTAL * 3 + LABEL_WIDTH + ELEMENT_WIDTH

DESCRIPTION_EDIT_HEIGHT = 100
DESCRIPTION_EDIT_WIDTH = ITEMS_LIST_WIDTH

SAVE_BUTTON_TEXT = 'Save'

CANCEL_BUTTON_TEXT = 'Cancel'

class ClassEditorWindow(QDialog):
    def __init__(self, parent, pclass=None):
        super().__init__()
        self.parent_window = parent
        self.ok = False
        self.initUI()
        self.editing = False
        self.pclass = pclass
        self.items = []
        if pclass is not None:
            self.editing = True
            self.set_values(pclass)

    def set_values(self, pclass: gsdk.ClassData):
        for name, value in pclass.__dict__.items():
            if name == 'items':
                continue
            self.editable[name].setText(str(value))
        self.items = pclass.items
        for item in self.items:
            self.items_list.addItem(item.name)

    def initUI(self):
        self.setWindowTitle(WINDOW_TITLE)

        name_label = QLabel(self)
        name_label.setText(NAME_LABEL_TEXT)
        name_label.move(NAME_LABEL_X, NAME_LABEL_Y)
        name_label.setFixedHeight(LABEL_HEIGHT)
        name_label.setFixedWidth(LABEL_WIDTH)
        name_label.setStyleSheet(LABEL_STYLE_SHEET)

        self.editable = {}
        name_edit = QLineEdit(self)
        name_edit.text_ = name_edit.text
        name_edit.move(NAME_EDIT_X, NAME_EDIT_Y)
        name_edit.setFixedSize(ELEMENT_WIDTH, ELEMENT_HEIGHT)
        self.editable['name'] = name_edit

        el_i = 0
        for attribute in gsdk.ATTRIBUTES:
            label = QLabel(self)
            label.setText(attribute + ': ')
            label.setFixedSize(LABEL_WIDTH, LABEL_HEIGHT)
            y = NAME_EDIT_Y + (el_i + 1) * (ELEMENT_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL)
            label.move(NAME_LABEL_X, y)
            label.setStyleSheet(LABEL_STYLE_SHEET)

            edit = QLineEdit(self)
            edit.text_ = edit.text
            edit.setFixedSize(ELEMENT_WIDTH, ELEMENT_HEIGHT)
            edit.move(NAME_LABEL_X + LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL, y)
            edit.setValidator(QIntValidator(0, gsdk.MAX_ATTRIBUTE, self))
            edit.setText('10')

            self.editable[attribute] = edit
            el_i += 1
        
        y = NAME_EDIT_Y + (el_i + 1) * (ELEMENT_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL)
        self.items_list = QListWidget(self)
        self.items_list.move(NAME_LABEL_X, y)
        self.items_list.setFixedSize(ITEMS_LIST_WIDTH, ITEMS_LIST_HEIGHT)
        self.items_list.itemDoubleClicked.connect(self.edit_item_action)

        y += util.BETWEEN_ELEMENTS_VERTICAL + ITEMS_LIST_HEIGHT

        BUTTON_WIDTH = 100

        add_item_button = QPushButton(self)
        add_item_button.setText('Add item')
        add_item_button.move(NAME_LABEL_X, y)
        add_item_button.setFixedSize(BUTTON_WIDTH, ELEMENT_HEIGHT)
        add_item_button.clicked.connect(self.add_item_action)

        delete_item_button = QPushButton(self)
        delete_item_button.setText('Delete item')
        delete_item_button.move(NAME_LABEL_X + util.BETWEEN_ELEMENTS_HORIZONTAL + BUTTON_WIDTH, y)
        delete_item_button.setFixedSize(BUTTON_WIDTH, ELEMENT_HEIGHT)
        delete_item_button.clicked.connect(self.add_item_action)

        y += util.BETWEEN_ELEMENTS_VERTICAL + ELEMENT_HEIGHT

        description_edit = QTextEdit(self)
        description_edit.text_ = description_edit.toPlainText
        description_edit.move(NAME_LABEL_X, y)
        description_edit.setFixedSize(DESCRIPTION_EDIT_WIDTH, DESCRIPTION_EDIT_HEIGHT)
        self.editable['description'] = description_edit

        y += util.BETWEEN_ELEMENTS_VERTICAL + DESCRIPTION_EDIT_HEIGHT
 
        save_button = QPushButton(self)
        save_button.setText(SAVE_BUTTON_TEXT)
        save_button.setFixedSize(BUTTON_WIDTH, ELEMENT_HEIGHT)
        save_button.move(NAME_LABEL_X, y)
        save_button.clicked.connect(self.save_action)

        cancel_button = QPushButton(self)
        cancel_button.setText(CANCEL_BUTTON_TEXT)
        cancel_button.setFixedSize(BUTTON_WIDTH, ELEMENT_HEIGHT)
        cancel_button.move(NAME_LABEL_X + util.BETWEEN_ELEMENTS_HORIZONTAL + ELEMENT_WIDTH, y)
        cancel_button.clicked.connect(self.cancel_action)

    def cancel_action(self):
        self.ok = False
        self.pclass = None
        self.close()

    def save_action(self):
        result = gsdk.ClassData()
        for name, el in self.editable.items():
            text = el.text_()
            if text == '':
                util.show_message_box('Enter value for ' + name)
                return
            result.__dict__[name] = text
            if name in gsdk.ATTRIBUTES:
                result.__dict__[name] = int(text)
        self.ok = True
        self.pclass = result
        self.pclass.items = self.items
        self.close()

    def edit(self) -> tuple[gsdk.ClassData, bool]:
        self.exec_()
        return self.pclass, self.ok

    def add_item_action(self):
        item, ok = ContainerItemEditorWindow(self).edit()
        if not ok:
            return
        self.items += [item]
        self.items_list.addItem(item.name)

    def delete_item_action(self):
        si = self.items_list.selectedIndexes()
        if len(si) != 1:
            return
        i = si[0].row()
        item = self.items[i]
        self.items.remove(item)
        self.items_list.takeItem(i)

    def edit_item_action(self):
        i = self.items_list.selectedIndexes()[0].row()
        item = self.items[i]
        w = ContainerItemEditorWindow(self, item)
        item, ok = w.edit()
        if not ok:
            return
        self.items[i] = item
        sel = self.items_list.selectedItems()[0]
        sel.setText(item.name)
