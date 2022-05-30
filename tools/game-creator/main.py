from PyQt5.QtWidgets import QAbstractItemView, QMessageBox, QInputDialog, QApplication, QMainWindow, QAction, QLabel, QComboBox, QPushButton, QDialog, QLineEdit, QListWidget
from PyQt5.QtCore import *
from ContainerEditor import ContainerEditorWindow
from RoomEditor import RoomEditorWindow
from ItemEditor import ItemEditorWindow
from ClassEditor import ClassEditorWindow
import util
import gamesdk as gsdk
import sys
import os.path

# default values

DEFAULT_GAME_NAME = 'My Game'

PANEL_COUNT = 5
LIST_HEIGHT = 300
LIST_WIDTH = 200

BUTTON_HEIGHT = 40

EDIT_GAME_INFO_BUTTON_TEXT = 'Edit game info'
EDIT_GAME_INFO_BUTTON_HEIGHT = BUTTON_HEIGHT
EDIT_GAME_INFO_BUTTON_WIDTH = 120
EDIT_GAME_INFO_BUTTON_Y = util.BETWEEN_ELEMENTS_VERTICAL
EDIT_GAME_INFO_BUTTON_X = util.BETWEEN_ELEMENTS_HORIZONTAL

LABEL_Y = EDIT_GAME_INFO_BUTTON_HEIGHT + EDIT_GAME_INFO_BUTTON_Y + util.BETWEEN_ELEMENTS_VERTICAL

ROOMS_LABEL_TEXT = 'Rooms'
ROOMS_LABEL_HEIGHT = util.LABEL_HEIGHT
ROOMS_LABEL_WIDTH = LIST_WIDTH
ROOMS_LABEL_Y = LABEL_Y
ROOMS_LABEL_X = util.BETWEEN_ELEMENTS_HORIZONTAL
ROOMS_LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE

ITEMS_LABEL_TEXT = 'Items'
ITEMS_LABEL_HEIGHT = util.LABEL_HEIGHT
ITEMS_LABEL_WIDTH = LIST_WIDTH
ITEMS_LABEL_Y = LABEL_Y
ITEMS_LABEL_X = ROOMS_LABEL_X + ROOMS_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
ITEMS_LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE

CONTAINERS_LABEL_TEXT = 'Containers'
CONTAINERS_LABEL_HEIGHT = util.LABEL_HEIGHT
CONTAINERS_LABEL_WIDTH = LIST_WIDTH
CONTAINERS_LABEL_Y = LABEL_Y
CONTAINERS_LABEL_X = ITEMS_LABEL_X + ITEMS_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
CONTAINERS_LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE

ENEMIES_LABEL_TEXT = 'Enemies'
ENEMIES_LABEL_HEIGHT = util.LABEL_HEIGHT
ENEMIES_LABEL_WIDTH = LIST_WIDTH
ENEMIES_LABEL_Y = LABEL_Y
ENEMIES_LABEL_X = CONTAINERS_LABEL_X + CONTAINERS_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
ENEMIES_LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE

CLASSES_LABEL_TEXT = 'Classes'
CLASSES_LABEL_HEIGHT = util.LABEL_HEIGHT
CLASSES_LABEL_WIDTH = LIST_WIDTH
CLASSES_LABEL_Y = LABEL_Y
CLASSES_LABEL_X = ENEMIES_LABEL_X + ENEMIES_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
CLASSES_LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE

LIST_Y = ROOMS_LABEL_HEIGHT + ROOMS_LABEL_Y + util.BETWEEN_ELEMENTS_VERTICAL

ROOM_NAMES_LIST_Y = LIST_Y
ROOM_NAMES_LIST_X = ROOMS_LABEL_X
ROOM_NAMES_LIST_HEIGHT = LIST_HEIGHT
ROOM_NAMES_LIST_WIDTH = LIST_WIDTH

ITEM_NAMES_LIST_Y = LIST_Y
ITEM_NAMES_LIST_X = ROOM_NAMES_LIST_X + ROOM_NAMES_LIST_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
ITEM_NAMES_LIST_HEIGHT = LIST_HEIGHT
ITEM_NAMES_LIST_WIDTH = LIST_WIDTH

CONTAINER_NAMES_LIST_Y = LIST_Y
CONTAINER_NAMES_LIST_X = ITEM_NAMES_LIST_X + ITEM_NAMES_LIST_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
CONTAINER_NAMES_LIST_HEIGHT = LIST_HEIGHT
CONTAINER_NAMES_LIST_WIDTH = LIST_WIDTH

ENEMY_NAMES_LIST_Y = LIST_Y
ENEMY_NAMES_LIST_X = CONTAINER_NAMES_LIST_X + CONTAINER_NAMES_LIST_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
ENEMY_NAMES_LIST_HEIGHT = LIST_HEIGHT
ENEMY_NAMES_LIST_WIDTH = LIST_WIDTH

CLASS_NAMES_LIST_Y = LIST_Y
CLASS_NAMES_LIST_X = ENEMY_NAMES_LIST_X + ENEMY_NAMES_LIST_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
CLASS_NAMES_LIST_HEIGHT = LIST_HEIGHT
CLASS_NAMES_LIST_WIDTH = LIST_WIDTH

BUTTON_Y = ROOM_NAMES_LIST_Y + ROOM_NAMES_LIST_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
BUTTON_WIDTH = 80
ADD_BUTTON_X_BETWEEN = (LIST_WIDTH - 2 * BUTTON_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL) // 2

NEW_ROOM_BUTTON_TEXT = 'Add'
NEW_ROOM_BUTTON_HEIGHT = BUTTON_HEIGHT
NEW_ROOM_BUTTON_WIDTH = BUTTON_WIDTH
NEW_ROOM_BUTTON_Y = BUTTON_Y
NEW_ROOM_BUTTON_X = ADD_BUTTON_X_BETWEEN

DELETE_ROOM_BUTTON_TEXT = 'Remove'
DELETE_ROOM_BUTTON_HEIGHT = BUTTON_HEIGHT
DELETE_ROOM_BUTTON_WIDTH = BUTTON_WIDTH
DELETE_ROOM_BUTTON_Y = BUTTON_Y
DELETE_ROOM_BUTTON_X = NEW_ROOM_BUTTON_X + NEW_ROOM_BUTTON_WIDTH

NEW_ITEM_BUTTON_TEXT = 'Add'
NEW_ITEM_BUTTON_HEIGHT = BUTTON_HEIGHT
NEW_ITEM_BUTTON_WIDTH = BUTTON_WIDTH
NEW_ITEM_BUTTON_Y = BUTTON_Y
NEW_ITEM_BUTTON_X = ROOM_NAMES_LIST_X + ROOM_NAMES_LIST_WIDTH + ADD_BUTTON_X_BETWEEN

DELETE_ITEM_BUTTON_TEXT = 'Remove'
DELETE_ITEM_BUTTON_HEIGHT = BUTTON_HEIGHT
DELETE_ITEM_BUTTON_WIDTH = BUTTON_WIDTH
DELETE_ITEM_BUTTON_Y = BUTTON_Y
DELETE_ITEM_BUTTON_X = NEW_ITEM_BUTTON_X + NEW_ROOM_BUTTON_WIDTH

NEW_CONTAINER_BUTTON_TEXT = 'Add'
NEW_CONTAINER_BUTTON_HEIGHT = BUTTON_HEIGHT
NEW_CONTAINER_BUTTON_WIDTH = BUTTON_WIDTH
NEW_CONTAINER_BUTTON_Y = BUTTON_Y
NEW_CONTAINER_BUTTON_X = ITEM_NAMES_LIST_X + ITEM_NAMES_LIST_WIDTH + ADD_BUTTON_X_BETWEEN

DELETE_CONTAINER_BUTTON_TEXT = 'Remove'
DELETE_CONTAINER_BUTTON_HEIGHT = BUTTON_HEIGHT
DELETE_CONTAINER_BUTTON_WIDTH = BUTTON_WIDTH
DELETE_CONTAINER_BUTTON_Y = BUTTON_Y
DELETE_CONTAINER_BUTTON_X = NEW_CONTAINER_BUTTON_X + NEW_ROOM_BUTTON_WIDTH

NEW_ENEMY_BUTTON_TEXT = 'Add'
NEW_ENEMY_BUTTON_HEIGHT = BUTTON_HEIGHT
NEW_ENEMY_BUTTON_WIDTH = BUTTON_WIDTH
NEW_ENEMY_BUTTON_Y = BUTTON_Y
NEW_ENEMY_BUTTON_X = CONTAINER_NAMES_LIST_X + CONTAINER_NAMES_LIST_WIDTH + ADD_BUTTON_X_BETWEEN

DELETE_ENEMY_BUTTON_TEXT = 'Remove'
DELETE_ENEMY_BUTTON_HEIGHT = BUTTON_HEIGHT
DELETE_ENEMY_BUTTON_WIDTH = BUTTON_WIDTH
DELETE_ENEMY_BUTTON_Y = BUTTON_Y
DELETE_ENEMY_BUTTON_X = NEW_ENEMY_BUTTON_X + NEW_ROOM_BUTTON_WIDTH

NEW_CLASS_BUTTON_TEXT = 'Add'
NEW_CLASS_BUTTON_HEIGHT = BUTTON_HEIGHT
NEW_CLASS_BUTTON_WIDTH = BUTTON_WIDTH
NEW_CLASS_BUTTON_Y = BUTTON_Y
NEW_CLASS_BUTTON_X = ENEMY_NAMES_LIST_X + ENEMY_NAMES_LIST_WIDTH + ADD_BUTTON_X_BETWEEN

DELETE_CLASS_BUTTON_TEXT = 'Remove'
DELETE_CLASS_BUTTON_HEIGHT = BUTTON_HEIGHT
DELETE_CLASS_BUTTON_WIDTH = BUTTON_WIDTH
DELETE_CLASS_BUTTON_Y = BUTTON_Y
DELETE_CLASS_BUTTON_X = NEW_CLASS_BUTTON_X + NEW_ROOM_BUTTON_WIDTH

MAIN_WINDOW_HEIGHT = NEW_ROOM_BUTTON_Y + NEW_ROOM_BUTTON_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
MAIN_WINDOW_WIDTH = util.BETWEEN_ELEMENTS_HORIZONTAL + PANEL_COUNT * (LIST_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL)

def window():
    app = QApplication([])
    win = MainAppWindow()
    win.show()
    sys.exit(app.exec_())

class MainAppWindow(QMainWindow):
    def __init__(self):
        super(QMainWindow, self).__init__()
        self.setGeometry(0, 0, MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT)
        self.setWindowTitle('Game maker')
        self.initValues()
        self.initUI()
        self.show()

    def initValues(self):
        # values
        self.save_dir: str = ''
        self.game: gsdk.GameObject = None
        # room windows
        self.room_windows: list[RoomEditorWindow] = []

        # dialogs
        self.game_info_edit_dialog: GameInfoEditWindow = GameInfoEditWindow()

    def initUI(self):
        # menu bar
        create_new_action = QAction('New game', self)
        create_new_action.setShortcut('Ctrl+Shift+N')
        create_new_action.setStatusTip('Create a new game')
        create_new_action.triggered.connect(self.create_new_game_action)

        create_new_room_action = QAction('New room', self)
        create_new_room_action.setShortcut('Ctrl+n')
        create_new_room_action.setStatusTip('Create a new room')
        create_new_room_action.triggered.connect(self.new_room_action)

        load_game_action = QAction('Load game', self)
        load_game_action.setShortcut('Ctrl+O')
        load_game_action.setStatusTip('Load an existing game')
        load_game_action.triggered.connect(self.load_existing_game_action)

        save_game_action = QAction('Save game', self)
        save_game_action.setShortcut('Ctrl+S')
        save_game_action.setStatusTip('Save game')
        save_game_action.triggered.connect(self.save_game_action)

        save_game_as_action = QAction('Save gams as', self)
        save_game_as_action.setShortcut('Ctrl+Shift+S')
        save_game_as_action.setStatusTip('Save game as')
        save_game_as_action.triggered.connect(self.save_game_as_action)

        edit_game_info_action = QAction(EDIT_GAME_INFO_BUTTON_TEXT, self)
        edit_game_info_action.setShortcut('Ctrl+G')
        edit_game_info_action.setStatusTip('Edit game info')
        edit_game_info_action.triggered.connect(self.edit_game_info_action)

        menu_bar = self.menuBar()

        file_menu = menu_bar.addMenu('&File')
        file_menu.addAction(create_new_action)
        file_menu.addAction(create_new_room_action)
        file_menu.addSeparator()
        file_menu.addAction(load_game_action)
        file_menu.addSeparator()
        file_menu.addAction(save_game_action)
        file_menu.addAction(save_game_as_action)

        edit_menu = menu_bar.addMenu('&Edit')
        edit_menu.addAction(edit_game_info_action)

        # labels

        self.rooms_label = QLabel(self)
        self.rooms_label.setText(ROOMS_LABEL_TEXT)
        self.rooms_label.setFixedHeight(ROOMS_LABEL_HEIGHT)
        self.rooms_label.setFixedWidth(ROOMS_LABEL_WIDTH)
        self.rooms_label.move(ROOMS_LABEL_X, ROOMS_LABEL_Y)
        self.rooms_label.setStyleSheet(ROOMS_LABEL_STYLE_SHEET)
        
        self.items_label = QLabel(self)
        self.items_label.setText(ITEMS_LABEL_TEXT)
        self.items_label.setFixedHeight(ITEMS_LABEL_HEIGHT)
        self.items_label.setFixedWidth(ITEMS_LABEL_WIDTH)
        self.items_label.move(ITEMS_LABEL_X, ITEMS_LABEL_Y)
        self.items_label.setStyleSheet(ITEMS_LABEL_STYLE_SHEET)

        self.containers_label = QLabel(self)
        self.containers_label.setText(CONTAINERS_LABEL_TEXT)
        self.containers_label.setFixedHeight(CONTAINERS_LABEL_HEIGHT)
        self.containers_label.setFixedWidth(CONTAINERS_LABEL_WIDTH)
        self.containers_label.move(CONTAINERS_LABEL_X, CONTAINERS_LABEL_Y)
        self.containers_label.setStyleSheet(CONTAINERS_LABEL_STYLE_SHEET)

        self.enemies_label = QLabel(self)
        self.enemies_label.setText(ENEMIES_LABEL_TEXT)
        self.enemies_label.setFixedHeight(ENEMIES_LABEL_HEIGHT)
        self.enemies_label.setFixedWidth(ENEMIES_LABEL_WIDTH)
        self.enemies_label.move(ENEMIES_LABEL_X, ENEMIES_LABEL_Y)
        self.enemies_label.setStyleSheet(ENEMIES_LABEL_STYLE_SHEET)

        self.classes_label = QLabel(self)
        self.classes_label.setText(CLASSES_LABEL_TEXT)
        self.classes_label.setFixedHeight(CLASSES_LABEL_HEIGHT)
        self.classes_label.setFixedWidth(CLASSES_LABEL_WIDTH)
        self.classes_label.move(CLASSES_LABEL_X, CLASSES_LABEL_Y)
        self.classes_label.setStyleSheet(CLASSES_LABEL_STYLE_SHEET)

        # buttons

        edit_game_info_button = QPushButton(self)
        edit_game_info_button.setText(EDIT_GAME_INFO_BUTTON_TEXT)
        edit_game_info_button.setFixedHeight(EDIT_GAME_INFO_BUTTON_HEIGHT)
        edit_game_info_button.setFixedWidth(EDIT_GAME_INFO_BUTTON_WIDTH)
        edit_game_info_button.move(EDIT_GAME_INFO_BUTTON_X, EDIT_GAME_INFO_BUTTON_Y)
        edit_game_info_button.clicked.connect(self.edit_game_info_action)

        new_room_button = QPushButton(self)
        new_room_button.setText(NEW_ROOM_BUTTON_TEXT)
        new_room_button.setFixedHeight(NEW_ROOM_BUTTON_HEIGHT)
        new_room_button.setFixedWidth(NEW_ROOM_BUTTON_WIDTH)
        new_room_button.move(NEW_ROOM_BUTTON_X, NEW_ROOM_BUTTON_Y)
        new_room_button.clicked.connect(self.new_room_action)

        delete_room_button = QPushButton(self)
        delete_room_button.setText(DELETE_ROOM_BUTTON_TEXT)
        delete_room_button.setFixedHeight(DELETE_ROOM_BUTTON_HEIGHT)
        delete_room_button.setFixedWidth(DELETE_ROOM_BUTTON_WIDTH)
        delete_room_button.move(DELETE_ROOM_BUTTON_X, DELETE_ROOM_BUTTON_Y)
        delete_room_button.clicked.connect(self.delete_room_action)

        new_item_button = QPushButton(self)
        new_item_button.setText(NEW_ITEM_BUTTON_TEXT)
        new_item_button.setFixedHeight(NEW_ITEM_BUTTON_HEIGHT)
        new_item_button.setFixedWidth(NEW_ITEM_BUTTON_WIDTH)
        new_item_button.move(NEW_ITEM_BUTTON_X, NEW_ITEM_BUTTON_Y)
        new_item_button.clicked.connect(self.new_item_action)

        delete_item_button = QPushButton(self)
        delete_item_button.setText(DELETE_ITEM_BUTTON_TEXT)
        delete_item_button.setFixedHeight(DELETE_ITEM_BUTTON_HEIGHT)
        delete_item_button.setFixedWidth(DELETE_ITEM_BUTTON_WIDTH)
        delete_item_button.move(DELETE_ITEM_BUTTON_X, DELETE_ITEM_BUTTON_Y)
        delete_item_button.clicked.connect(self.delete_item_action)

        new_container_button = QPushButton(self)
        new_container_button.setText(NEW_CONTAINER_BUTTON_TEXT)
        new_container_button.setFixedHeight(NEW_CONTAINER_BUTTON_HEIGHT)
        new_container_button.setFixedWidth(NEW_CONTAINER_BUTTON_WIDTH)
        new_container_button.move(NEW_CONTAINER_BUTTON_X, NEW_CONTAINER_BUTTON_Y)
        new_container_button.clicked.connect(self.new_container_action)

        delete_container_button = QPushButton(self)
        delete_container_button.setText(DELETE_CONTAINER_BUTTON_TEXT)
        delete_container_button.setFixedHeight(DELETE_CONTAINER_BUTTON_HEIGHT)
        delete_container_button.setFixedWidth(DELETE_CONTAINER_BUTTON_WIDTH)
        delete_container_button.move(DELETE_CONTAINER_BUTTON_X, DELETE_CONTAINER_BUTTON_Y)
        delete_container_button.clicked.connect(self.delete_container_action)

        new_enemy_button = QPushButton(self)
        new_enemy_button.setText(NEW_ENEMY_BUTTON_TEXT)
        new_enemy_button.setFixedHeight(NEW_ENEMY_BUTTON_HEIGHT)
        new_enemy_button.setFixedWidth(NEW_ENEMY_BUTTON_WIDTH)
        new_enemy_button.move(NEW_ENEMY_BUTTON_X, NEW_ENEMY_BUTTON_Y)
        new_enemy_button.clicked.connect(self.new_enemy_action)

        delete_enemy_button = QPushButton(self)
        delete_enemy_button.setText(DELETE_ENEMY_BUTTON_TEXT)
        delete_enemy_button.setFixedHeight(DELETE_ENEMY_BUTTON_HEIGHT)
        delete_enemy_button.setFixedWidth(DELETE_ENEMY_BUTTON_WIDTH)
        delete_enemy_button.move(DELETE_ENEMY_BUTTON_X, DELETE_ENEMY_BUTTON_Y)
        delete_enemy_button.clicked.connect(self.delete_enemy_action)

        new_class_button = QPushButton(self)
        new_class_button.setText(NEW_CLASS_BUTTON_TEXT)
        new_class_button.setFixedHeight(NEW_CLASS_BUTTON_HEIGHT)
        new_class_button.setFixedWidth(NEW_CLASS_BUTTON_WIDTH)
        new_class_button.move(NEW_CLASS_BUTTON_X, NEW_CLASS_BUTTON_Y)
        new_class_button.clicked.connect(self.new_class_action)

        delete_class_button = QPushButton(self)
        delete_class_button.setText(DELETE_CLASS_BUTTON_TEXT)
        delete_class_button.setFixedHeight(DELETE_CLASS_BUTTON_HEIGHT)
        delete_class_button.setFixedWidth(DELETE_CLASS_BUTTON_WIDTH)
        delete_class_button.move(DELETE_CLASS_BUTTON_X, DELETE_CLASS_BUTTON_Y)
        delete_class_button.clicked.connect(self.delete_class_action)

        # lists

        self.room_names_list = QListWidget(self)
        self.room_names_list.setEditTriggers(QAbstractItemView.NoEditTriggers)
        self.room_names_list.setFixedHeight(ROOM_NAMES_LIST_HEIGHT)
        self.room_names_list.setFixedWidth(ROOM_NAMES_LIST_WIDTH)
        self.room_names_list.move(ROOM_NAMES_LIST_X, ROOM_NAMES_LIST_Y)
        self.room_names_list.itemDoubleClicked.connect(self.room_double_clicked)
        
        self.item_names_list = QListWidget(self)
        self.item_names_list.setEditTriggers(QAbstractItemView.NoEditTriggers)
        self.item_names_list.setFixedHeight(ITEM_NAMES_LIST_HEIGHT)
        self.item_names_list.setFixedWidth(ITEM_NAMES_LIST_WIDTH)
        self.item_names_list.move(ITEM_NAMES_LIST_X, ITEM_NAMES_LIST_Y)
        self.item_names_list.itemDoubleClicked.connect(self.item_double_clicked)

        self.container_names_list = QListWidget(self)
        self.container_names_list.setEditTriggers(QAbstractItemView.NoEditTriggers)
        self.container_names_list.setFixedHeight(CONTAINER_NAMES_LIST_HEIGHT)
        self.container_names_list.setFixedWidth(CONTAINER_NAMES_LIST_WIDTH)
        self.container_names_list.move(CONTAINER_NAMES_LIST_X, CONTAINER_NAMES_LIST_Y)
        self.container_names_list.itemDoubleClicked.connect(self.container_double_clicked)

        self.enemy_names_list = QListWidget(self)
        self.enemy_names_list.setEditTriggers(QAbstractItemView.NoEditTriggers)
        self.enemy_names_list.setFixedHeight(ENEMY_NAMES_LIST_HEIGHT)
        self.enemy_names_list.setFixedWidth(ENEMY_NAMES_LIST_WIDTH)
        self.enemy_names_list.move(ENEMY_NAMES_LIST_X, ENEMY_NAMES_LIST_Y)
        self.enemy_names_list.itemDoubleClicked.connect(self.enemy_double_clicked)

        self.class_names_list = QListWidget(self)
        self.class_names_list.setEditTriggers(QAbstractItemView.NoEditTriggers)
        self.class_names_list.setFixedHeight(CLASS_NAMES_LIST_HEIGHT)
        self.class_names_list.setFixedWidth(CLASS_NAMES_LIST_WIDTH)
        self.class_names_list.move(CLASS_NAMES_LIST_X, CLASS_NAMES_LIST_Y)
        self.class_names_list.itemDoubleClicked.connect(self.class_double_clicked)
        
    # methods

    def replace_room_name(self, old_name: str, new_name: str):
        rns = [room.name for room in self.game.rooms]
        i = rns.index(old_name)
        self.room_names_list.takeItem(i)
        self.room_names_list.insertItem(i, new_name)
        self.game.rooms[i].name = new_name

    def save_game(self) -> bool:
        print('Saving game...')
        if self.game.md.index_room == '':
            util.show_message_box('Set the index room')
            return
        md = self.game.md
        if md.start_x == -1 or md.start_y == -1:
            util.show_message_box(f'Can\' use ({md.start_y}; {md.start_x}) as starting location')
            return
        for i in range(len(self.room_windows)):
            room = self.room_windows[i]
            result = self.game.rooms[i].create_room(room)
            if not result.ok:
                util.show_not_save_ready(result)
                return False
        self.game.save(self.save_dir)
        util.show_message_box('Game saved!')
        return True

    def check_game(self) -> bool:
        if self.game is None:
            util.show_message_box('Create a new game before saving!')
            return False
        return True

    def update_elements(self):
        self.setWindowTitle(self.game.gi.name)

    def clear_game(self):
        self.game = None
        for window in self.room_windows:
            window.close()
        self.room_windows = []
        self.room_names_list.clear()
        self.item_names_list.clear()
        self.container_names_list.clear()
        self.enemy_names_list.clear()
        self.class_names_list.clear()

    #  actions

    def room_double_clicked(self, e):
        i = self.room_names_list.selectedIndexes()[0].row()
        w = self.room_windows[i]
        w.show()        

    def item_double_clicked(self, e):
        i = self.item_names_list.selectedIndexes()[0].row()
        item = self.game.items[i]
        w = ItemEditorWindow(self, item)
        item, ok = w.edit()
        if not ok:
            return
        old_name = self.game.items[i].name
        self.game.items[i] = item
        sel = self.item_names_list.selectedItems()[0]
        sel.setText(item.name)
        # change container item names
        for container in self.game.containers:
            for it in container.items:
                if it.name == old_name:
                    it.name = item.name

    def container_double_clicked(self, e):
        i = self.container_names_list.selectedIndexes()[0].row()
        container = self.game.containers[i]
        w = ContainerEditorWindow(self, container)
        container, ok = w.edit()
        if not ok:
            return
        self.game.containers[i] = container
        sel = self.container_names_list.selectedItems()[0]
        sel.setText(container.key)

    def enemy_double_clicked(self, e):
        # TODO
        pass
    
    def class_double_clicked(self, e):
        i = self.class_names_list.selectedIndexes()[0].row()
        pclass = self.game.classes[i]
        w = ClassEditorWindow(self, pclass)
        pclass, ok = w.edit()
        if not ok:
            return
        self.game.classes[i] = pclass
        sel = self.class_names_list.selectedItems()[0]
        sel.setText(pclass.name)

    def create_new_game_action(self):
        if self.game != None:
            if util.show_message_box('Create new game? Previous game saves will be discarded if not saved.', QMessageBox.No | QMessageBox.Yes) == QMessageBox.No:
                return
        self.save_dir = ''
        self.clear_game()
        name, ok = util.prompt_string(self, 'New game', 'Name of the game:', DEFAULT_GAME_NAME)
        if not ok:
            return
        if name == '':
            return
        self.setWindowTitle(name)
        self.save_dir = ''
        self.game = gsdk.GameObject(name)

    def load_existing_game_action(self):
        game = None
        d = util.prompt_directory(self)
        if d == '':
            return
        # try:
        game = gsdk.GameObject.load(d)       
        # except Exception as e:
            # util.show_message_box(f'Couldn\'t load game directory, error:\n{str(e)}')
            # return
        self.save_dir = os.path.dirname(d)
        self.clear_game()
        self.game = game
        # items
        for item in self.game.items:
            self.item_names_list.addItem(item.name)
        # containers
        for container in self.game.containers:
            self.container_names_list.addItem(container.key)
        # rooms
        for room in self.game.rooms:
            w = RoomEditorWindow(self, room.name)
            self.room_names_list.addItem(room.name)
            self.room_windows += [w]
            w.rd = room
            w.update_elements()
        # classes
        for pclass in self.game.classes:
            self.class_names_list.addItem(pclass.name)

    def save_game_action(self):
        if not self.check_game():
            return     
        if self.save_dir == '':
            self.save_game_as_action()
            return
        self.save_game()

    def save_game_as_action(self):
        if not self.check_game():
            return
        dir = util.prompt_directory(self)
        if dir == '':
            return
        self.save_dir = dir
        print(self.save_dir)
        self.save_game()
    
    def edit_game_info_action(self):
        if self.game is None:
            return
        new_game, ok = self.game_info_edit_dialog.edit(self.game)
        if not ok:
            print('Cancelled editing game info')
            return
        self.game = new_game
        self.update_elements()

    def new_room_action(self):
        if self.game is None:
            return
        room_name, ok = util.prompt_string(self, 'New room', 'Enter room name:')
        if not ok:
            return
        if room_name == '':
            return
        rns = [room.name for room in self.game.rooms]
        if room_name in rns:
            util.show_message_box(f'Room {room_name} already exists')
            return
        self.room_names_list.addItem(room_name)
        rew = RoomEditorWindow(self, room_name)
        self.room_windows += [rew]
        self.game.rooms += [rew.rd]
        rew.show()

    def delete_room_action(self):
        si = self.room_names_list.selectedIndexes()
        if len(si) != 1:
            return
        i = si[0].row()
        room = self.game.rooms[i]
        if util.show_message_box(f'Are you sure you want to delete room {room.name}?', QMessageBox.No | QMessageBox.Yes) == QMessageBox.No:
            return
        self.game.rooms.remove(room)
        self.room_names_list.takeItem(i)
        w = self.room_windows[i]
        w.close()
        self.room_windows.remove(w)
        if self.game.md.index_room == room.name:
            util.show_message_box(f'Warning: index room was deleted (room: {room.name})')
            self.game.md.index_room = ''

    def new_item_action(self):
        if self.game is None:
            return
        w = ItemEditorWindow(self)
        item, ok = w.edit()
        if not ok:
            return
        self.game.items += [item]
        self.item_names_list.addItem(item.name)

    def delete_item_action(self):
        si = self.item_names_list.selectedIndexes()
        if len(si) != 1:
            return
        i = si[0].row()
        item = self.game.items[i]
        if util.show_message_box(f'Are you sure you want to delete item {item.name}?', QMessageBox.No | QMessageBox.Yes) == QMessageBox.No:
            return
        self.game.items.remove(item)
        self.item_names_list.takeItem(i)

    def new_container_action(self):
        if self.game is None:
            return
        if len(self.game.items) == 0:
            util.show_message_box('No items to add to container')
            return
        w = ContainerEditorWindow(self)
        container, ok = w.edit()
        if not ok:
            return
        self.game.containers += [container]
        self.container_names_list.addItem(container.key)

    def delete_container_action(self):
        si = self.container_names_list.selectedIndexes()
        if len(si) != 1:
            return
        i = si[0].row()
        container = self.game.containers[i]
        if util.show_message_box(f'Are you sure you want to delete container {container.name}?', QMessageBox.No | QMessageBox.Yes) == QMessageBox.No:
            return
        self.game.containers.remove(container)
        self.container_names_list.takeItem(i)

    def new_enemy_action(self):
        # TODO
        pass

    def delete_enemy_action(self):
        # TODO
        pass

    def new_class_action(self):
        if self.game is None:
            return
        w = ClassEditorWindow(self)
        pclass, ok = w.edit()
        if not ok:
            return
        self.game.classes += [pclass]
        self.class_names_list.addItem(pclass.name)

    def delete_class_action(self):
        si = self.class_names_list.selectedIndexes()
        if len(si) != 1:
            return
        i = si[0].row()
        pclass = self.game.classes[i]
        if util.show_message_box(f'Are you sure you want to delete class {pclass.name}?', QMessageBox.No | QMessageBox.Yes) == QMessageBox.No:
            return
        self.game.classes.remove(pclass)
        self.class_names_list.takeItem(i)

GAME_INFO_NAME_LABEL_TEXT = 'Game name:'
GAME_INFO_NAME_LABEL_HEIGHT = util.LABEL_HEIGHT
GAME_INFO_NAME_LABEL_WIDTH = 100
GAME_INFO_NAME_LABEL_Y = util.BETWEEN_ELEMENTS_HORIZONTAL
GAME_INFO_NAME_LABEL_X = util.BETWEEN_ELEMENTS_VERTICAL
GAME_INFO_NAME_LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE

GAME_INFO_NAME_LINE_EDIT_HEIGHT = util.LABEL_HEIGHT
GAME_INFO_NAME_LINE_EDIT_WIDTH = 200
GAME_INFO_NAME_LINE_EDIT_Y = GAME_INFO_NAME_LABEL_Y
GAME_INFO_NAME_LINE_EDIT_X = GAME_INFO_NAME_LABEL_X + GAME_INFO_NAME_LABEL_WIDTH + util.BETWEEN_ELEMENTS_VERTICAL

GAME_INFO_INDEX_LABEL_TEXT = 'Index room:'
GAME_INFO_INDEX_LABEL_HEIGHT = util.LABEL_HEIGHT
GAME_INFO_INDEX_LABEL_WIDTH = 100
GAME_INFO_INDEX_LABEL_Y = GAME_INFO_NAME_LABEL_Y + GAME_INFO_NAME_LABEL_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
GAME_INFO_INDEX_LABEL_X = GAME_INFO_NAME_LABEL_X
GAME_INFO_INDEX_LABEL_STYLE_SHEET = GAME_INFO_NAME_LABEL_STYLE_SHEET

GAME_INFO_INDEX_COMBO_BOX_HEIGHT = util.LABEL_HEIGHT
GAME_INFO_INDEX_COMBO_BOX_WIDTH = GAME_INFO_NAME_LINE_EDIT_WIDTH
GAME_INFO_INDEX_COMBO_BOX_Y = GAME_INFO_INDEX_LABEL_Y
GAME_INFO_INDEX_COMBO_BOX_X = GAME_INFO_NAME_LINE_EDIT_X

SPAWN_LABEL_TEXT = 'Spawn (y x):'
SPAWN_LABEL_HEIGHT = util.LABEL_HEIGHT
SPAWN_LABEL_WIDTH = 100
SPAWN_LABEL_Y = GAME_INFO_INDEX_LABEL_Y + GAME_INFO_INDEX_LABEL_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
SPAWN_LABEL_X = GAME_INFO_INDEX_LABEL_X
SPAWN_LABEL_STYLE_SHEET = GAME_INFO_INDEX_LABEL_STYLE_SHEET

SPAWN_LINE_EDIT_HEIGHT = util.LABEL_HEIGHT
SPAWN_LINE_EDIT_WIDTH = 200
SPAWN_LINE_EDIT_Y = SPAWN_LABEL_Y
SPAWN_LINE_EDIT_X = SPAWN_LABEL_X + SPAWN_LABEL_WIDTH + util.BETWEEN_ELEMENTS_VERTICAL

GIB_WIDTH = 100

GAME_INFO_SAVE_BUTTON_TEXT = 'Save'
GAME_INFO_SAVE_BUTTON_HEIGHT = util.BUTTON_HEIGHT
GAME_INFO_SAVE_BUTTON_WIDTH = GIB_WIDTH
GAME_INFO_SAVE_BUTTON_Y = SPAWN_LABEL_HEIGHT + SPAWN_LABEL_Y + util.BETWEEN_ELEMENTS_VERTICAL
GAME_INFO_SAVE_BUTTON_X = SPAWN_LABEL_Y

GAME_INFO_CANCEL_BUTTON_TEXT = 'Cancel'
GAME_INFO_CANCEL_BUTTON_HEIGHT = util.BUTTON_HEIGHT
GAME_INFO_CANCEL_BUTTON_WIDTH = GIB_WIDTH
GAME_INFO_CANCEL_BUTTON_Y = GAME_INFO_SAVE_BUTTON_Y
GAME_INFO_CANCEL_BUTTON_X = GAME_INFO_SAVE_BUTTON_X + GAME_INFO_SAVE_BUTTON_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

GAME_INFO_WINDOW_HEIGHT = GAME_INFO_SAVE_BUTTON_Y + GAME_INFO_SAVE_BUTTON_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
GAME_INFO_WINDOW_WIDTH = GAME_INFO_NAME_LINE_EDIT_X + GAME_INFO_NAME_LINE_EDIT_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
GAME_INFO_WINDOW_TITLE = 'Game info edit'

class GameInfoEditWindow(QDialog):
    def __init__(self):
        super().__init__()
        self.setGeometry(0, 0, GAME_INFO_WINDOW_WIDTH, GAME_INFO_WINDOW_HEIGHT)
        self.setWindowTitle(GAME_INFO_WINDOW_TITLE)
        util.move_to_center(self)
        self.initValues()
        self.initUI()

    def initValues(self):
        self.ok = False
        self.go = None

    def initUI(self):
        # labels

        self.game_name_label = QLabel(self)
        self.game_name_label.setText(GAME_INFO_NAME_LABEL_TEXT)
        self.game_name_label.setFixedHeight(GAME_INFO_NAME_LABEL_HEIGHT)
        self.game_name_label.setFixedWidth(GAME_INFO_NAME_LABEL_WIDTH)
        self.game_name_label.setStyleSheet(GAME_INFO_NAME_LABEL_STYLE_SHEET)
        self.game_name_label.move(GAME_INFO_NAME_LABEL_X, GAME_INFO_NAME_LABEL_Y)

        self.index_label = QLabel(self)
        self.index_label.setText(GAME_INFO_INDEX_LABEL_TEXT)
        self.index_label.setFixedHeight(GAME_INFO_INDEX_LABEL_HEIGHT)
        self.index_label.setFixedWidth(GAME_INFO_INDEX_LABEL_WIDTH)
        self.index_label.setStyleSheet(GAME_INFO_INDEX_LABEL_STYLE_SHEET)
        self.index_label.move(GAME_INFO_INDEX_LABEL_X, GAME_INFO_INDEX_LABEL_Y)

        self.spawn_label = QLabel(self)
        self.spawn_label.setText(SPAWN_LABEL_TEXT)
        self.spawn_label.setFixedHeight(SPAWN_LABEL_HEIGHT)
        self.spawn_label.setFixedWidth(SPAWN_LABEL_WIDTH)
        self.spawn_label.setStyleSheet(SPAWN_LABEL_STYLE_SHEET)
        self.spawn_label.move(SPAWN_LABEL_X, SPAWN_LABEL_Y)

        # line edits

        self.game_name_line_edit = QLineEdit(self)
        self.game_name_line_edit.setFixedHeight(GAME_INFO_NAME_LINE_EDIT_HEIGHT)
        self.game_name_line_edit.setFixedWidth(GAME_INFO_NAME_LINE_EDIT_WIDTH)
        self.game_name_line_edit.move(GAME_INFO_NAME_LINE_EDIT_X, GAME_INFO_NAME_LINE_EDIT_Y)

        self.spawn_line_edit = QLineEdit(self)
        self.spawn_line_edit.setFixedHeight(SPAWN_LINE_EDIT_HEIGHT)
        self.spawn_line_edit.setFixedWidth(SPAWN_LINE_EDIT_WIDTH)
        self.spawn_line_edit.move(SPAWN_LINE_EDIT_X, SPAWN_LINE_EDIT_Y)

        # buttons

        save_button = QPushButton(self)
        save_button.setText(GAME_INFO_SAVE_BUTTON_TEXT)
        save_button.setFixedHeight(GAME_INFO_SAVE_BUTTON_HEIGHT)
        save_button.setFixedWidth(GAME_INFO_SAVE_BUTTON_WIDTH)
        save_button.move(GAME_INFO_SAVE_BUTTON_X, GAME_INFO_SAVE_BUTTON_Y)
        save_button.clicked.connect(self.save_action)

        cancel_button = QPushButton(self)
        cancel_button.setText(GAME_INFO_CANCEL_BUTTON_TEXT)
        cancel_button.setFixedHeight(GAME_INFO_CANCEL_BUTTON_HEIGHT)
        cancel_button.setFixedWidth(GAME_INFO_CANCEL_BUTTON_WIDTH)
        cancel_button.move(GAME_INFO_CANCEL_BUTTON_X, GAME_INFO_CANCEL_BUTTON_Y)
        cancel_button.clicked.connect(self.cancel_action)

        # combo boxes
        self.index_combo_box = QComboBox(self)
        self.index_combo_box.setFixedHeight(GAME_INFO_INDEX_COMBO_BOX_HEIGHT)
        self.index_combo_box.setFixedWidth(GAME_INFO_INDEX_COMBO_BOX_WIDTH)
        self.index_combo_box.move(GAME_INFO_INDEX_COMBO_BOX_X, GAME_INFO_INDEX_COMBO_BOX_Y)

    # methods

    def edit(self, go: gsdk.GameObject) -> tuple[gsdk.GameObject, bool]:
        self.ok = True
        self.go = go
        self.update_elements()
        self.exec()
        # self.hi
        return self.go, self.ok

    def update_elements(self):
        self.game_name_line_edit.setText(self.go.gi.name)
        rns = [room.name for room in self.go.rooms]
        self.index_combo_box.clear()
        for name in rns:
            self.index_combo_box.addItem(name)
        self.index_combo_box.setCurrentText(self.go.md.index_room)
        self.spawn_line_edit.setText(f'{self.go.md.start_y} {self.go.md.start_x}')

    # actions

    def save_action(self):
        s = self.spawn_line_edit.text().strip().split(' ')
        if len(s) != 2:
            util.show_message_box(f'Spawn location can\'t be {self.spawn_line_edit.text()} (has to be two positive integers)')
            return
        start_y = None
        start_x = None
        try:
            start_y = int(s[0])
        except ValueError:
            util.show_message_box(f'start_y can\'t be {s[0]}')
            return
        try:
            start_x = int(s[1])
        except ValueError:
            util.show_message_box(f'start_x can\'t be {s[1]}')
            return
        self.go.md.start_y = start_y
        self.go.md.start_x = start_x
        self.ok = True
        self.go.gi.name = self.game_name_line_edit.text()
        self.go.md.index_room = self.index_combo_box.currentText()
        self.close()

    def cancel_action(self):
        self.ok = False
        self.close()
    
if __name__ == '__main__':
    window()

