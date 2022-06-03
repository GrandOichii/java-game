from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *

import gamesdk as gsdk

import util

WINDOW_TITLE = 'Item Editor'

LABEL_WIDTH = 200
ELEMENT_WIDTH = 200
ELEMENT_HEIGHT = util.LABEL_HEIGHT
LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE

TYPE_LABEL_TEXT = 'Item type: '
TYPE_LABEL_HEIGHT = ELEMENT_HEIGHT
TYPE_LABEL_WIDTH = LABEL_WIDTH
TYPE_LABEL_Y = util.BETWEEN_ELEMENTS_VERTICAL
TYPE_LABEL_X = util.BETWEEN_ELEMENTS_HORIZONTAL
TYPE_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

TYPE_COMBO_BOX_HEIGHT = TYPE_LABEL_HEIGHT
TYPE_COMBO_BOX_WIDTH = ELEMENT_WIDTH
TYPE_COMBO_BOX_Y = TYPE_LABEL_Y
TYPE_COMBO_BOX_X = TYPE_LABEL_X + TYPE_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

class ItemEditorWindow(QDialog):
    def __init__(self, parent, item=None):
        super().__init__()
        self.parent_window = parent
        self.ok = False
        self.initUI()
        self.editing = False
        self.item = item
        if item is not None:
            self.editing = True
            self.set_values(item)
        self.changed_item_type()

    def set_values(self, item: gsdk.ItemData):
        d = item.__dict__
        self.type_combo_box.setCurrentText(d['selected_type'])
        for key in gsdk.ITEM_KEYS:
            self.elements[key]['element'].setText_(str(d[key]))

    def initUI(self):
        self.setWindowTitle(WINDOW_TITLE)

        type_label = QLabel(self)
        type_label.setText(TYPE_LABEL_TEXT)
        type_label.move(TYPE_LABEL_X, TYPE_LABEL_Y)
        type_label.setFixedHeight(TYPE_LABEL_HEIGHT)
        type_label.setFixedWidth(TYPE_LABEL_WIDTH)
        type_label.setStyleSheet(TYPE_LABEL_STYLE_SHEET)

        self.type_combo_box = QComboBox(self)
        self.type_combo_box.setFixedHeight(TYPE_COMBO_BOX_HEIGHT)
        self.type_combo_box.setFixedWidth(TYPE_COMBO_BOX_WIDTH)
        self.type_combo_box.move(TYPE_COMBO_BOX_X, TYPE_COMBO_BOX_Y)
        for itype in gsdk.ITEM_TYPES:
            self.type_combo_box.addItem(itype)
        self.type_combo_box.currentIndexChanged.connect(self.changed_item_type)

        self.el_i = 1

        def create_label(text: str) -> QLabel:
            result = QLabel()
            result.setFixedHeight(ELEMENT_HEIGHT)
            result.setFixedWidth(LABEL_WIDTH)
            result.setText(text)
            result.setStyleSheet(LABEL_STYLE_SHEET)
            result.move(util.BETWEEN_ELEMENTS_HORIZONTAL, util.BETWEEN_ELEMENTS_VERTICAL + self.el_i * (util.BETWEEN_ELEMENTS_VERTICAL + ELEMENT_HEIGHT))
            return result

        def move_element(element):
            y = util.BETWEEN_ELEMENTS_VERTICAL + self.el_i * (util.BETWEEN_ELEMENTS_VERTICAL + ELEMENT_HEIGHT)
            x = util.BETWEEN_ELEMENTS_HORIZONTAL * 2 + LABEL_WIDTH
            element.move(x, y)
            self.el_i += 1

        def set_element_size(element):
            element.setFixedHeight(ELEMENT_HEIGHT)
            element.setFixedWidth(ELEMENT_WIDTH)

        def create_combo_box(items: list[str]):
            result = QComboBox()
            for item in items:
                result.addItem(item)
            set_element_size(result)
            move_element(result)
            result.getText_ = result.currentText
            result.setText_ = result.setEditText
            return result

        def create_line_edit():
            result = QLineEdit()
            set_element_size(result)
            move_element(result)
            result.getText_ = result.text
            result.setText_ = result.setText
            result.setText('aaa' + str(self.el_i))
            return result

        def create_number_edit(_min: int, _max: int):
            result = create_line_edit()
            result.setValidator(QIntValidator(_min, _max, self))
            result.setText(str(self.el_i))
            return result

        self.elements = {
            # ADD ITEM TYPE HERE
            'name': {
                'label': create_label('Name:'),
                'element': create_line_edit()
            },
            'displayName': {
                'label': create_label('Display name:'),
                'element': create_line_edit()
            },
            'ammoType': {
                'label': create_label('Ammo type:'),
                'element': create_combo_box(gsdk.AMMO_TYPES)
            },
            'damageType': {
                'label': create_label('Damage type'),
                'element': create_combo_box(gsdk.DAMAGE_TYPES)
            },
            'damage': {
                'label': create_label('Damage: '),
                'element': create_number_edit(0, gsdk.MAX_DAMAGE)
            },
            'range': {
                'label': create_label('Range: '),
                'element': create_number_edit(0, gsdk.MAX_RANGE)
            },
            'minDamage': {
                'label': create_label('Min damage: '),
                'element': create_number_edit(0, gsdk.MAX_DAMAGE)
            },
            'maxDamage': {
                'label': create_label('Max damage: '),
                'element': create_number_edit(0, gsdk.MAX_DAMAGE)
            },
            'slot': {
                'label': create_label('Slot:'),
                'element': create_combo_box(gsdk.SLOTS)
            },
            'armorRating': {
                'label': create_label('Armor rating:'),
                'element': create_number_edit(0, gsdk.MAX_ARMOR_RATING)
            },
            'STR': {
                'label': create_label('STR requirement:'),
                'element': create_number_edit(0, gsdk.MAX_ATTRIBUTE)
            },
            'AGI': {
                'label': create_label('AGI requirement:'),
                'element': create_number_edit(0, gsdk.MAX_ATTRIBUTE)
            },
            'INT': {
                'label': create_label('INT requirement:'),
                'element': create_number_edit(0, gsdk.MAX_ATTRIBUTE)
            },
            'targets': {
                'label': create_label('Target incantations:'),
                'element': create_line_edit()
            },
            'types': {
                'label': create_label('Type incantations:'),
                'element': create_line_edit()
            },
            'intensities': {
                'label': create_label('Intensity incantations:'),
                'element': create_line_edit()
            },
            'intRequirement': {
                'label': create_label('INT required for reading:'),
                'element': create_number_edit(0, gsdk.MAX_ATTRIBUTE)
            },
            'description': {
                'label': create_label('Description: '),
                'element': create_line_edit()
            }
        }
        
        for pair in self.elements.values():
            pair['label'].setParent(self)
            pair['element'].setParent(self)

        BUTTON_HEIGHT = 50
        BUTTON_WIDTH = 100
        y = util.BETWEEN_ELEMENTS_VERTICAL + self.el_i * (util.BETWEEN_ELEMENTS_VERTICAL + ELEMENT_HEIGHT)
        x = util.BETWEEN_ELEMENTS_HORIZONTAL

        save_button = QPushButton(self)
        save_button.setText('Save')
        save_button.setFixedSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        save_button.move(x, y)
        save_button.clicked.connect(self.save_action)

        cancel_button = QPushButton(self)
        cancel_button.setText('Cancel')
        cancel_button.setFixedSize(BUTTON_WIDTH, BUTTON_HEIGHT)
        cancel_button.move(x + util.BETWEEN_ELEMENTS_HORIZONTAL + BUTTON_WIDTH, y)
        cancel_button.clicked.connect(self.cancel_action)

    def cancel_action(self):
        self.ok = False
        self.item = None
        self.close()

    def save_action(self):
        # check field validity
        name = self.elements['name']['element'].getText_()
        if self.parent_window.game.count_items_with_name(name) == 1:
            if self.item == None or self.item.name != name:
                util.show_message_box('Item with name ' + name + ' already exists')
                return
        t = self.type_combo_box.currentText()
        keys = gsdk.ITEM_TYPE_KEYS[t]
        for key in keys:
            if self.elements[key]['element'].getText_() == '':
                util.show_message_box('Enter item ' + key)
                return
        for incantation, values in gsdk.INCANTATIONS.items():
            el = self.elements[incantation]['element']
            split = el.getText_().split(' ')
            for word in split:
                if not word in values:
                    util.show_message_box('Unknown incantation ' + word)
                    return
        # save the item
        self.item = gsdk.ItemData()
        self.item.selected_type = self.type_combo_box.currentText()
        for key in gsdk.ITEM_KEYS:
            self.item.__dict__[key] = self.elements[key]['element'].getText_()
        self.ok = True
        self.close()

    def changed_item_type(self):
        t = self.type_combo_box.currentText()
        tags = gsdk.ITEM_TYPE_KEYS[t]
        for tag, pair in self.elements.items():
            pair['element'].setEnabled(False)
            if tag in tags:
                pair['element'].setEnabled(True)

    def edit(self) -> tuple[gsdk.ItemData, bool]:
        self.exec_()
        return self.item, self.ok