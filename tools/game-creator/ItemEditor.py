from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *

import gamesdk as gsdk

import util

WINDOW_TITLE = 'Item Editor'

LABEL_WIDTH = 100
LINE_EDIT_WIDTH = 200
LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE

NAME_LABEL_TEXT = 'Name: '
NAME_LABEL_WIDTH = LABEL_WIDTH
NAME_LABEL_HEIGHT = util.LABEL_HEIGHT
NAME_LABEL_Y = 1
NAME_LABEL_X = 1
NAME_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

NAME_LINE_EDIT_Y = NAME_LABEL_Y
NAME_LINE_EDIT_X = NAME_LABEL_X + NAME_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
NAME_LINE_EDIT_HEIGHT = NAME_LABEL_HEIGHT
NAME_LINE_EDIT_WIDTH = LINE_EDIT_WIDTH

DISPLAY_NAME_LABEL_TEXT = 'Display name:'
DISPLAY_NAME_LABEL_HEIGHT = util.LABEL_HEIGHT
DISPLAY_NAME_LABEL_WIDTH = LABEL_WIDTH
DISPLAY_NAME_LABEL_Y = NAME_LABEL_Y + NAME_LABEL_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
DISPLAY_NAME_LABEL_X = NAME_LABEL_X
DISPLAY_NAME_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

DISPLAY_NAME_LINE_EDIT_Y = DISPLAY_NAME_LABEL_Y
DISPLAY_NAME_LINE_EDIT_X = DISPLAY_NAME_LABEL_X + DISPLAY_NAME_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
DISPLAY_NAME_LINE_EDIT_HEIGHT = DISPLAY_NAME_LABEL_HEIGHT
DISPLAY_NAME_LINE_EDIT_WIDTH = LINE_EDIT_WIDTH

TYPE_LABEL_TEXT = 'Item type: '
TYPE_LABEL_HEIGHT = util.LABEL_HEIGHT
TYPE_LABEL_WIDTH = LABEL_WIDTH
TYPE_LABEL_Y = DISPLAY_NAME_LABEL_Y + DISPLAY_NAME_LABEL_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
TYPE_LABEL_X = DISPLAY_NAME_LABEL_X
TYPE_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

TYPE_COMBO_BOX_HEIGHT = TYPE_LABEL_HEIGHT
TYPE_COMBO_BOX_WIDTH = LINE_EDIT_WIDTH
TYPE_COMBO_BOX_Y = TYPE_LABEL_Y
TYPE_COMBO_BOX_X = TYPE_LABEL_X + TYPE_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

# DISPLAY_NAME_LABEL

'''
== Layout ==
Name: <text field>
Display name: <text field>
Type: <drop down box>
!Basic:
    <no elements>
!Ammo:
    Damage: <int text field>
    Ammo type: <drop down box>
!Weapon:
    Damage type: <drop down box>
    Damage: <int text field> - <int text field>
    Range: <int text field>
    Equip slot: <drop down box>
    Requirements
    STR: <int text field>
    AGI: <int text field>
    Weapon type: <drop down box>
    !Melee:
        <no elements>
    !Ranged:
        Ammo type: <drop down box>
Description
'''

class ItemEditorWindow(QDialog):
    def __init__(self, parent, item=None):
        super().__init__()
        self.parent_window = parent
        self.ok = False
        self.item = item
        if self.item is None:
            self.item = gsdk.ItemData()
        self.initUI()
        self.update_values()

    def update_values(self):
        # item name
        self.name_edit.setText(self.item.name)
        # item display name
        self.display_name_edit.setText(self.item.display_name)
        # item type
        self.type_combo_box.setCurrentText(self.item.selected_type)

    def initUI(self):
        self.setWindowTitle(WINDOW_TITLE)
        # labels

        name_label = QLabel(self)
        name_label.setText(NAME_LABEL_TEXT)
        name_label.move(NAME_LABEL_X, NAME_LABEL_Y)
        name_label.setFixedHeight(NAME_LABEL_HEIGHT)
        name_label.setFixedWidth(NAME_LABEL_WIDTH)
        name_label.setStyleSheet(NAME_LABEL_STYLE_SHEET)

        display_name_label = QLabel(self)
        display_name_label.setText(DISPLAY_NAME_LABEL_TEXT)
        display_name_label.move(DISPLAY_NAME_LABEL_X, DISPLAY_NAME_LABEL_Y)
        display_name_label.setFixedHeight(DISPLAY_NAME_LABEL_HEIGHT)
        display_name_label.setFixedWidth(DISPLAY_NAME_LABEL_WIDTH)
        display_name_label.setStyleSheet(DISPLAY_NAME_LABEL_STYLE_SHEET)

        type_label = QLabel(self)
        type_label.setText(TYPE_LABEL_TEXT)
        type_label.move(TYPE_LABEL_X, TYPE_LABEL_Y)
        type_label.setFixedHeight(TYPE_LABEL_HEIGHT)
        type_label.setFixedWidth(TYPE_LABEL_WIDTH)
        type_label.setStyleSheet(TYPE_LABEL_STYLE_SHEET)

        # line edits

        self.name_edit = QLineEdit(self)
        self.name_edit.move(NAME_LINE_EDIT_X, NAME_LINE_EDIT_Y)
        self.name_edit.setFixedSize(NAME_LINE_EDIT_WIDTH, NAME_LINE_EDIT_HEIGHT)

        self.display_name_edit = QLineEdit(self)
        self.display_name_edit.move(DISPLAY_NAME_LINE_EDIT_X, DISPLAY_NAME_LINE_EDIT_Y)
        self.display_name_edit.setFixedSize(DISPLAY_NAME_LINE_EDIT_WIDTH, DISPLAY_NAME_LINE_EDIT_HEIGHT)

        # combo boxes boxes

        self.type_combo_box = QComboBox(self)
        self.type_combo_box.setFixedHeight(TYPE_COMBO_BOX_HEIGHT)
        self.type_combo_box.setFixedWidth(TYPE_COMBO_BOX_WIDTH)
        self.type_combo_box.move(TYPE_COMBO_BOX_X, TYPE_COMBO_BOX_Y)
        for itype in gsdk.ITEM_TYPES:
            self.type_combo_box.addItem(itype)

        # buttons

    def edit(self) -> tuple[gsdk.ItemData, bool]:
        self.exec_()
        return self.item, self.ok