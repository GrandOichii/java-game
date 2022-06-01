from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *

import gamesdk as gsdk

import util

ITEM_WINDOW_TITLE = 'Container item'
LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE
BUTTON_HEIGHT = 50
BUTTON_WIDTH = 100

ITEM_NAME_LABEL_TEXT = 'Item name:'
ITEM_NAME_LABEL_X = util.BETWEEN_ELEMENTS_HORIZONTAL
ITEM_NAME_LABEL_Y = util.BETWEEN_ELEMENTS_VERTICAL
ITEM_NAME_LABEL_HEIGHT = util.LABEL_HEIGHT
ITEM_NAME_LABEL_WIDTH = 100

ITEM_NAME_COMBO_BOX_HEIGHT = util.LABEL_HEIGHT
ITEM_NAME_COMBO_BOX_WIDTH = 200
ITEM_NAME_COMBO_BOX_X = ITEM_NAME_LABEL_X + util.BETWEEN_ELEMENTS_HORIZONTAL + ITEM_NAME_LABEL_WIDTH
ITEM_NAME_COMBO_BOX_Y = util.BETWEEN_ELEMENTS_VERTICAL

ITEM_AMOUNT_LABEL_TEXT = 'Amount:'
ITEM_AMOUNT_LABEL_X = util.BETWEEN_ELEMENTS_HORIZONTAL
ITEM_AMOUNT_LABEL_Y = ITEM_NAME_LABEL_Y + util.BETWEEN_ELEMENTS_VERTICAL + ITEM_NAME_LABEL_HEIGHT
ITEM_AMOUNT_LABEL_HEIGHT = util.LABEL_HEIGHT
ITEM_AMOUNT_LABEL_WIDTH = 100

ITEM_AMOUNT_LINE_EDIT_X = ITEM_AMOUNT_LABEL_X + ITEM_AMOUNT_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
ITEM_AMOUNT_LINE_EDIT_Y = ITEM_AMOUNT_LABEL_Y
ITEM_AMOUNT_LINE_EDIT_HEIGHT = util.LABEL_HEIGHT
ITEM_AMOUNT_LINE_EDIT_WIDTH = 200

IWINDOW_SAVE_BUTTON_TEXT = 'Add'
IWINDOW_SAVE_BUTTON_X = util.BETWEEN_ELEMENTS_HORIZONTAL
IWINDOW_SAVE_BUTTON_Y = util.BETWEEN_ELEMENTS_VERTICAL + ITEM_AMOUNT_LINE_EDIT_Y + ITEM_AMOUNT_LINE_EDIT_HEIGHT

IWINDOW_CANCEL_BUTTON_TEXT = 'Cancel'
IWINDOW_CANCEL_BUTTON_X = util.BETWEEN_ELEMENTS_HORIZONTAL + BUTTON_WIDTH + IWINDOW_SAVE_BUTTON_X
IWINDOW_CANCEL_BUTTON_Y = IWINDOW_SAVE_BUTTON_Y

class ContainerItemEditorWindow(QDialog):
    def __init__(self, parent, item=None):
        super().__init__()
        self.parent_window = parent
        self.ok = False
        self.initUI()
        self.item = item
        if item is not None:
            self.set_values(item)

    def set_values(self, item: gsdk.ContainerItem):
        self.item_name_combo_box.setCurrentText(item.name)
        self.amount_edit.setText(str(item.amount))

    def initUI(self):
        self.setWindowTitle(ITEM_WINDOW_TITLE)

        item_name_label = QLabel(self)
        item_name_label.setText(ITEM_NAME_LABEL_TEXT)
        item_name_label.move(ITEM_NAME_LABEL_X, ITEM_NAME_LABEL_Y)
        item_name_label.setFixedHeight(ITEM_NAME_LABEL_HEIGHT)
        item_name_label.setFixedWidth(ITEM_NAME_LABEL_WIDTH)
        item_name_label.setStyleSheet(LABEL_STYLE_SHEET)

        self.item_name_combo_box = QComboBox(self)
        self.item_name_combo_box.setFixedHeight(ITEM_NAME_COMBO_BOX_HEIGHT)
        self.item_name_combo_box.setFixedWidth(ITEM_NAME_COMBO_BOX_WIDTH)
        self.item_name_combo_box.move(ITEM_NAME_COMBO_BOX_X, ITEM_NAME_COMBO_BOX_Y)
        item_names = [item.name for item in self.parent_window.parent_window.game.items]
        for name in item_names:
            self.item_name_combo_box.addItem(name)

        amount_label = QLabel(self)
        amount_label.setText(ITEM_AMOUNT_LABEL_TEXT)
        amount_label.move(ITEM_AMOUNT_LABEL_X, ITEM_AMOUNT_LABEL_Y)
        amount_label.setFixedHeight(ITEM_AMOUNT_LABEL_HEIGHT)
        amount_label.setFixedWidth(ITEM_AMOUNT_LABEL_WIDTH)
        amount_label.setStyleSheet(LABEL_STYLE_SHEET)

        self.amount_edit = QLineEdit(self)
        self.amount_edit.move(ITEM_AMOUNT_LINE_EDIT_X, ITEM_AMOUNT_LINE_EDIT_Y)
        self.amount_edit.setFixedHeight(ITEM_AMOUNT_LINE_EDIT_HEIGHT)
        self.amount_edit.setFixedWidth(ITEM_AMOUNT_LINE_EDIT_WIDTH)
        self.amount_edit.setValidator(QIntValidator(0, gsdk.CONTAINER_MAX_ITEM_AMOUNT, self))

        save_button = QPushButton(self)
        save_button.setText(IWINDOW_SAVE_BUTTON_TEXT)
        save_button.setFixedSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        save_button.move(IWINDOW_SAVE_BUTTON_X, IWINDOW_SAVE_BUTTON_Y)
        save_button.clicked.connect(self.save_action)
        save_button.setFocus()

        cancel_button = QPushButton(self)
        cancel_button.setText(IWINDOW_CANCEL_BUTTON_TEXT)
        cancel_button.setFixedSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        cancel_button.move(IWINDOW_CANCEL_BUTTON_X, IWINDOW_CANCEL_BUTTON_Y)
        cancel_button.clicked.connect(self.cancel_action)

    def cancel_action(self):
        self.ok = False
        self.container = None
        self.close()

    def save_action(self):
        if self.amount_edit.text() == '':
            util.show_message_box('Set the amount')
            return
        self.item = gsdk.ContainerItem()
        self.item.name = self.item_name_combo_box.currentText()
        self.item.amount = int(self.amount_edit.text())
        self.ok = True
        self.close()

    def edit(self) -> tuple[gsdk.ContainerItem, bool]:
        self.exec_()
        return self.item, self.ok

WINDOW_TITLE = 'Container Editor'

KEY_LABEL_TEXT = 'Container key:'
KEY_LABEL_X = util.BETWEEN_ELEMENTS_HORIZONTAL
KEY_LABEL_Y = util.BETWEEN_ELEMENTS_VERTICAL
KEY_LABEL_HEIGHT = util.LABEL_HEIGHT
KEY_LABEL_WIDTH = 100

KEY_LINE_EDIT_HEIGHT = util.LABEL_HEIGHT
KEY_LINE_EDIT_WIDTH = 200
KEY_LINE_EDIT_Y = KEY_LABEL_Y
KEY_LINE_EDIT_X = KEY_LABEL_X + util.BETWEEN_ELEMENTS_HORIZONTAL + KEY_LABEL_WIDTH

MANAGE_BUTTON_HEIGHT = 30
MANAGE_BUTTON_WIDTH = 100

ADD_BUTTON_TEXT = 'Add item'
ADD_BUTTON_Y = KEY_LINE_EDIT_Y + KEY_LINE_EDIT_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
ADD_BUTTON_X = util.BETWEEN_ELEMENTS_HORIZONTAL

DELETE_BUTTON_TEXT = 'Delete item'
DELETE_BUTTON_Y = ADD_BUTTON_Y
DELETE_BUTTON_X = util.BETWEEN_ELEMENTS_HORIZONTAL + ADD_BUTTON_X + MANAGE_BUTTON_WIDTH

ITEMS_LIST_HEIGHT = 300
ITEMS_LIST_WIDTH = 3 * util.BETWEEN_ELEMENTS_HORIZONTAL + KEY_LABEL_WIDTH + KEY_LINE_EDIT_WIDTH
ITEMS_LIST_X = util.BETWEEN_ELEMENTS_HORIZONTAL
ITEMS_LIST_Y = ADD_BUTTON_Y + MANAGE_BUTTON_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL

SAVE_BUTTON_TEXT = 'Save'
SAVE_BUTTON_Y = ITEMS_LIST_Y + ITEMS_LIST_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
SAVE_BUTTON_X = util.BETWEEN_ELEMENTS_HORIZONTAL

CANCEL_BUTTON_TEXT = 'Cancel'
CANCEL_BUTTON_Y = SAVE_BUTTON_Y
CANCEL_BUTTON_X = SAVE_BUTTON_X + BUTTON_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

class ContainerEditorWindow(QDialog):
    def __init__(self, parent, container=None):
        super().__init__()
        self.parent_window = parent
        self.items: list[gsdk.ContainerItem] = []
        self.ok = False
        self.initUI()
        self.container = container
        if container is not None:
            self.set_values(container)

    def set_values(self, container: gsdk.ContainerData):
        self.key_line_edit.setText(container.key)
        self.items = container.items
        for item in self.items:
            self.items_list.addItem(item.name + ' x ' + str(item.amount))

    def initUI(self):
        self.setWindowTitle(WINDOW_TITLE)

        key_label = QLabel(self)
        key_label.setText(KEY_LABEL_TEXT)
        key_label.move(KEY_LABEL_X, KEY_LABEL_Y)
        key_label.setFixedHeight(KEY_LABEL_HEIGHT)
        key_label.setFixedWidth(KEY_LABEL_WIDTH)
        key_label.setStyleSheet(LABEL_STYLE_SHEET)

        self.key_line_edit = QLineEdit(self)
        self.key_line_edit.setFixedHeight(KEY_LINE_EDIT_HEIGHT)
        self.key_line_edit.setFixedWidth(KEY_LINE_EDIT_WIDTH)
        self.key_line_edit.move(KEY_LINE_EDIT_X, KEY_LINE_EDIT_Y)

        add_button = QPushButton(self)
        add_button.setText(ADD_BUTTON_TEXT)
        add_button.setFixedSize(MANAGE_BUTTON_WIDTH, MANAGE_BUTTON_HEIGHT)
        add_button.move(ADD_BUTTON_X, ADD_BUTTON_Y)
        add_button.clicked.connect(self.add_action)

        delete_button = QPushButton(self)
        delete_button.setText(DELETE_BUTTON_TEXT)
        delete_button.setFixedSize(MANAGE_BUTTON_WIDTH, MANAGE_BUTTON_HEIGHT)
        delete_button.move(DELETE_BUTTON_X, DELETE_BUTTON_Y)
        delete_button.clicked.connect(self.delete_action)

        self.items_list = QListWidget(self)
        self.items_list.move(ITEMS_LIST_X, ITEMS_LIST_Y)
        self.items_list.setFixedSize(ITEMS_LIST_WIDTH, ITEMS_LIST_HEIGHT)
        self.items_list.itemDoubleClicked.connect(self.edit_action)

        save_button = QPushButton(self)
        save_button.setText(SAVE_BUTTON_TEXT)
        save_button.setFixedSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        save_button.move(SAVE_BUTTON_X, SAVE_BUTTON_Y)
        save_button.clicked.connect(self.save_action)
        save_button.setFocus()

        cancel_button = QPushButton(self)
        cancel_button.setText(CANCEL_BUTTON_TEXT)
        cancel_button.setFixedSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        cancel_button.move(CANCEL_BUTTON_X, CANCEL_BUTTON_Y)
        cancel_button.clicked.connect(self.cancel_action)

    def add_action(self):
        item, ok = ContainerItemEditorWindow(self).edit()
        if not ok:
            return
        self.items += [item]
        self.items_list.addItem(item.name + ' x ' + str(item.amount))

    def delete_action(self):
        si = self.items_list.selectedIndexes()
        if len(si) != 1:
            return
        i = si[0].row()
        item = self.items[i]
        self.items.remove(item)
        self.items_list.takeItem(i)

    def edit_action(self):
        i = self.items_list.selectedIndexes()[0].row()
        item = self.items[i]
        w = ContainerItemEditorWindow(self, item)
        item, ok = w.edit()
        if not ok:
            return
        self.items[i] = item
        sel = self.items_list.selectedItems()[0]
        sel.setText(item.name + ' x ' + str(item.amount))

    def cancel_action(self):
        self.ok = False
        self.container = None
        self.close()

    def save_action(self):
        key = self.key_line_edit.text()
        if key == '':
            util.show_message_box('Enter container key')
            return
        if len(self.items) == 0:
            util.show_message_box('Add at least one item')
            return
        if self.parent_window.game.count_containers_with_key(key) == 1:
            if self.container is None or self.container.key != key:
                util.show_message_box('Container with key ' + key + ' already exists')
                return
        self.container = gsdk.ContainerData()
        self.container.key = key
        self.container.items = self.items
        self.ok = True
        self.close()

    def edit(self) -> tuple[gsdk.ContainerData, bool]:
        self.exec_()
        return self.container, self.ok