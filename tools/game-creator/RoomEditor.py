from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *
import util
import gamesdk as gsdk

HOR = util.BETWEEN_ELEMENTS_HORIZONTAL
VER = util.BETWEEN_ELEMENTS_VERTICAL
LABEL_STYLE_SHEET = util.BORDER_1PX_BLACK_STYLE + util.PADDING_LEFT_1PX_STYLE

RC_BUTTON_SIZE = 30
ADD_BUTTON_TEXT = '+'
SUB_BUTTON_TEXT = '-'
TILES_PANEL_HEIGHT = 500
EDIT_ROOM_INFO_BUTTON_HEIGHT = util.LABEL_HEIGHT * 2
ZOOM_BUTTON_HEIGHT = EDIT_ROOM_INFO_BUTTON_HEIGHT
ZOOM_BUTTON_WIDTH = ZOOM_BUTTON_HEIGHT

TILES_PANEL_WIDTH = TILES_PANEL_HEIGHT
HALF = TILES_PANEL_WIDTH + 2 * RC_BUTTON_SIZE + int(3.5 * HOR)

WINDOW_WIDTH = 2 * HALF
WINDOW_HEIGHT = TILES_PANEL_HEIGHT + RC_BUTTON_SIZE * 2 + VER * 5 + EDIT_ROOM_INFO_BUTTON_HEIGHT

TILES_PANEL_Y = 2 * VER + RC_BUTTON_SIZE
TILES_PANEL_X = 2 * HOR + RC_BUTTON_SIZE

SUB_ROW_TOP_BUTTON_Y = HOR
SUB_ROW_TOP_BUTTON_X = 2 * HOR + RC_BUTTON_SIZE + (TILES_PANEL_WIDTH - RC_BUTTON_SIZE * 2 - HOR) // 2
ADD_ROW_TOP_BUTTON_Y = SUB_ROW_TOP_BUTTON_Y
ADD_ROW_TOP_BUTTON_X = SUB_ROW_TOP_BUTTON_X + RC_BUTTON_SIZE + HOR

SUB_ROW_BOTTOM_BUTTON_Y = TILES_PANEL_Y + TILES_PANEL_HEIGHT + VER
SUB_ROW_BOTTOM_BUTTON_X = SUB_ROW_TOP_BUTTON_X
ADD_ROW_BOTTOM_BUTTON_Y = SUB_ROW_BOTTOM_BUTTON_Y
ADD_ROW_BOTTOM_BUTTON_X = SUB_ROW_BOTTOM_BUTTON_X + RC_BUTTON_SIZE + HOR

ADD_COLUMN_LEFT_BUTTON_Y = 2 * VER + RC_BUTTON_SIZE + (TILES_PANEL_HEIGHT - RC_BUTTON_SIZE * 2 - VER) // 2
ADD_COLUMN_LEFT_BUTTON_X = VER
SUB_COLUMN_LEFT_BUTTON_Y = ADD_COLUMN_LEFT_BUTTON_Y + RC_BUTTON_SIZE + VER
SUB_COLUMN_LEFT_BUTTON_X = ADD_COLUMN_LEFT_BUTTON_X

ADD_COLUMN_RIGHT_BUTTON_Y = ADD_COLUMN_LEFT_BUTTON_Y
ADD_COLUMN_RIGHT_BUTTON_X = TILES_PANEL_X + TILES_PANEL_WIDTH + HOR
SUB_COLUMN_RIGHT_BUTTON_Y = ADD_COLUMN_RIGHT_BUTTON_Y + RC_BUTTON_SIZE + HOR
SUB_COLUMN_RIGHT_BUTTON_X = ADD_COLUMN_RIGHT_BUTTON_X

EDIT_ROOM_INFO_BUTTON_TEXT = 'Edit room info'
EDIT_ROOM_INFO_BUTTON_WIDTH = 200
EDIT_ROOM_INFO_BUTTON_Y = SUB_ROW_BOTTOM_BUTTON_Y + RC_BUTTON_SIZE + VER
EDIT_ROOM_INFO_BUTTON_X = HOR

ZOOM_PLUS_BUTTON_TEXT = 'Z+'
ZOOM_PLUS_BUTTON_Y = EDIT_ROOM_INFO_BUTTON_Y
ZOOM_PLUS_BUTTON_X = EDIT_ROOM_INFO_BUTTON_X + EDIT_ROOM_INFO_BUTTON_WIDTH + HOR

ZOOM_MINUS_BUTTON_TEXT = 'Z-'
ZOOM_MINUS_BUTTON_Y = ZOOM_PLUS_BUTTON_Y
ZOOM_MINUS_BUTTON_X = ZOOM_PLUS_BUTTON_X + ZOOM_BUTTON_WIDTH + HOR

QUARTER_HEIGHT = WINDOW_HEIGHT // 2
QUARTER_WIDTH = WINDOW_WIDTH - TILES_PANEL_WIDTH - 2 * RC_BUTTON_SIZE - 6 * HOR
B_WIDTH = QUARTER_WIDTH // 3 - util.BETWEEN_ELEMENTS_HORIZONTAL

TILES_LABEL_TEXT = 'Tiles'
TILES_LABEL_HEIGHT = util.LABEL_HEIGHT
TILES_LABEL_WIDTH = QUARTER_WIDTH
TILES_LABEL_Y = ADD_ROW_TOP_BUTTON_Y
TILES_LABEL_X = ADD_COLUMN_RIGHT_BUTTON_X + RC_BUTTON_SIZE + VER
TILES_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

AED_BUTTON_HEIGHT = util.LABEL_HEIGHT

TILES_LIST_HEIGHT = QUARTER_HEIGHT - TILES_LABEL_HEIGHT - AED_BUTTON_HEIGHT - VER * 4
TILES_LIST_WIDTH = TILES_LABEL_WIDTH
TILES_LIST_Y = TILES_LABEL_Y + TILES_LABEL_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
TILES_LIST_X = TILES_LABEL_X

NEW_TILE_BUTTON_TEXT = 'Add tile'
NEW_TILE_BUTTON_HEIGHT = AED_BUTTON_HEIGHT
NEW_TILE_BUTTON_WIDTH = B_WIDTH
NEW_TILE_BUTTON_Y = TILES_LIST_HEIGHT + TILES_LIST_Y + util.BETWEEN_ELEMENTS_VERTICAL
NEW_TILE_BUTTON_X = TILES_LIST_X

EDIT_TILE_BUTTON_TEXT = 'Edit tile'
EDIT_TILE_BUTTON_HEIGHT = AED_BUTTON_HEIGHT
EDIT_TILE_BUTTON_WIDTH = B_WIDTH
EDIT_TILE_BUTTON_Y = NEW_TILE_BUTTON_Y
EDIT_TILE_BUTTON_X = NEW_TILE_BUTTON_X + NEW_TILE_BUTTON_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

DELETE_TILE_BUTTON_TEXT = 'Delete tile'
DELETE_TILE_BUTTON_HEIGHT = AED_BUTTON_HEIGHT
DELETE_TILE_BUTTON_WIDTH = B_WIDTH
DELETE_TILE_BUTTON_Y = EDIT_TILE_BUTTON_Y
DELETE_TILE_BUTTON_X = EDIT_TILE_BUTTON_X + EDIT_TILE_BUTTON_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

SCRIPTS_LABEL_TEXT = 'Scripts'
SCRIPTS_LABEL_HEIGHT = util.LABEL_HEIGHT
SCRIPTS_LABEL_WIDTH = QUARTER_WIDTH
SCRIPTS_LABEL_Y = DELETE_TILE_BUTTON_HEIGHT + DELETE_TILE_BUTTON_Y + util.BETWEEN_ELEMENTS_VERTICAL
SCRIPTS_LABEL_X = TILES_LABEL_X
SCRIPTS_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

SCRIPTS_LIST_HEIGHT = QUARTER_HEIGHT - SCRIPTS_LABEL_HEIGHT - AED_BUTTON_HEIGHT - VER * 4
SCRIPTS_LIST_WIDTH = SCRIPTS_LABEL_WIDTH
SCRIPTS_LIST_Y = SCRIPTS_LABEL_Y + SCRIPTS_LABEL_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
SCRIPTS_LIST_X = TILES_LIST_X

NEW_SCRIPT_BUTTON_TEXT = 'Add script'
NEW_SCRIPT_BUTTON_HEIGHT = AED_BUTTON_HEIGHT
NEW_SCRIPT_BUTTON_WIDTH = B_WIDTH
NEW_SCRIPT_BUTTON_Y = SCRIPTS_LIST_HEIGHT + SCRIPTS_LIST_Y + util.BETWEEN_ELEMENTS_VERTICAL
NEW_SCRIPT_BUTTON_X = SCRIPTS_LIST_X

EDIT_SCRIPT_BUTTON_TEXT = 'Edit script'
EDIT_SCRIPT_BUTTON_HEIGHT = AED_BUTTON_HEIGHT
EDIT_SCRIPT_BUTTON_WIDTH = B_WIDTH
EDIT_SCRIPT_BUTTON_Y = NEW_SCRIPT_BUTTON_Y
EDIT_SCRIPT_BUTTON_X = NEW_SCRIPT_BUTTON_X + NEW_SCRIPT_BUTTON_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

DELETE_SCRIPT_BUTTON_TEXT = 'Delete script'
DELETE_SCRIPT_BUTTON_HEIGHT = AED_BUTTON_HEIGHT
DELETE_SCRIPT_BUTTON_WIDTH = B_WIDTH
DELETE_SCRIPT_BUTTON_Y = EDIT_SCRIPT_BUTTON_Y
DELETE_SCRIPT_BUTTON_X = EDIT_SCRIPT_BUTTON_X + EDIT_SCRIPT_BUTTON_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

WINDOW_TITLE_FORMAT = 'Room editor ({})'

SELECTED_COLOR = QColor('magenta')
DEFAULT_TILE_COLOR = QColor('white')
TILE_HIGHLIGHT_COLOR = QColor('red')

STARTING_TILE_HW = 20
STARTING_TILE_HOR_COUNT = 10
STARTING_TILE_VER_COUNT = 10

HW_INCREASE_AMOUNT = 5

class TileWidget(QWidget):
    def __init__(self, parent: 'TilePanelWidget', size: int, i: int, j: int):
        QWidget.__init__(self, parent)
        self.update_size(size)
        self.setStyleSheet(util.BORDER_1PX_BLACK_STYLE)
        self.selected = False
        self.highlighted = False
        self.i = i
        self.j = j
        self.mousePressEvent = self.click
        self.parent_panel = parent
        # tile info
        self.tile_name = ''
        self.color = DEFAULT_TILE_COLOR
    
    def mouseDoubleClickEvent(self, e: QMouseEvent) -> None:
        util.show_message_box(f'{self.i} {self.j}')

    def set_template(self, tile_template: gsdk.TileData):
        self.tile_name = tile_template.name
        self.update_color(tile_template.rgb)
        self.highlighted = True

    def reset(self):
        self.color = DEFAULT_TILE_COLOR
        self.tile_name = ''
        self.highlighted = False
        self.update()

    def click(self, e):
        if e.button() == Qt.LeftButton:
            self.parent_panel.select(self)
        else:
            self.parent_panel.show_context_menu_for(self, e)

    def update_size(self, new_hw: int):
        self.hw = new_hw
        self.setFixedHeight(self.hw)
        self.setFixedWidth(self.hw)

    def paintEvent(self, e):
        painter = QPainter(self)
        brush = QBrush(self.color)
        painter.setBrush(brush)        
        painter.drawRect(0, 0, self.hw, self.hw)
        s = 1
        if self.selected:
            pen = QPen(SELECTED_COLOR)
            painter.setPen(pen)
            painter.drawRect(s, s, self.hw - s*2, self.hw - s*2)
        if self.highlighted:
            brush.setColor(TILE_HIGHLIGHT_COLOR)
            brush.setStyle(Qt.BrushStyle.DiagCrossPattern)
            painter.setBrush(brush)
            painter.drawRect(2, 2, self.hw-4, self.hw-4)
        painter.drawPoint(self.hw // 2, self.hw // 2)

    def sizeHint(self) -> QSize:
        return QSize(self.hw, self.hw)

    def update_color(self, rgb: list[int]):
        self.color = QColor(rgb[0], rgb[1], rgb[2])
        self.update()

    def set_selected(self, value: bool):
        self.selected = value
        self.update()

SET_AS_SPAWN_ACTION = 'Set as spawn point'

class TilePanelWidget(QWidget):
    def __init__(self, parent_window: 'RoomEditorWindow', thw: int):
        QWidget.__init__(self)
        self.parent_window = parent_window
        self.thw = thw
        self.first_selected_loc = []
        self.second_selected_loc = []
        self.tiles: list[list[TileWidget]] = []
        self.initUI()

    def sizeHint(self) -> QSize:
        # return QSize(1000, 1000)
        h = self.thw * len(self.tiles)
        w = 0
        if h != 0:
            w = self.thw * len(self.tiles[0])
        return QSize(w, h)

    def handle_key(self, e: QKeyEvent):
        tile_template = self.parent_window.get_selected_tile()
        selected_tiles = self.get_selected_tiles()
        if e.text() == 'a':
            self.first_selected_loc = [0, 0]
            self.second_selected_loc = [len(self.tiles) - 1, len(self.tiles[0]) - 1]
            self.update_selected()
            return
        if e.text() == 'p':
            for row in self.tiles:
                for tile in row:
                    g = tile.geometry()
                    util.show_message_box(f'{g.y()} {g.x()}\n{tile.parent()}')
            return
        if e.key() == Qt.Key_Space:
            if tile_template is None:
                return
            for tile in selected_tiles:
                tile.set_template(tile_template)
            return
        if e.key() == Qt.Key_Backspace:
            for tile in selected_tiles:
                tile.reset()
                self.first_selected_loc = []
                self.second_selected_loc = []
                self.update_selected()
            return
    
    def initUI(self):
        # context menu
        self.tile_context_menu = QMenu(self)
        self.set_spawn_action = self.tile_context_menu.addAction('Set as spawn point')
        # tiles
        self.create_tiles(STARTING_TILE_HOR_COUNT, STARTING_TILE_VER_COUNT)

    # methods

    def update_locs(self):
        height = len(self.tiles)
        width = len(self.tiles[0])
        for i in range(height):
            for j in range(width):
                tile = self.tiles[i][j]
                tile.i = i
                tile.j = j
                y = i * self.thw
                x = j * self.thw
                tile.move(x, y)
                tile.setParent(self)
                tile.show()
        # self.update()

    def add_row(self, row: int):
        width = 0
        if len(self.tiles) > 0:
            width = len(self.tiles[0])
        self.tiles.insert(row, [])
        for i in range(width):
            tile = TileWidget(self, self.thw, row, i)
            self.tiles[row] += [tile]
        size = self.sizeHint()
        self.setFixedSize(size.width(), size.height())
        self.update_locs()

    def add_column(self, column: int):
        height = len(self.tiles)
        for i in range(height):
            tile = TileWidget(self, self.thw, i, column)
            self.tiles[i].insert(column, tile)
        size = self.sizeHint()
        self.setFixedSize(size.width(), size.height())
        self.update_locs()
    
    def delete_row(self, row: int):
        if len(self.tiles) == 1:
            return
        tiles = self.tiles.pop(row)
        for tile in tiles:
            tile.setParent(None)
        self.update_locs()

    def delete_column(self, column: int):
        if len(self.tiles[0]) == 1:
            return
        for i in range(len(self.tiles)):
            tile = self.tiles[i].pop(column)
            tile.setParent(None)
        self.update_locs()

    def create_tiles(self, height: int, width: int):
        for i in range(height):
            self.add_row(i)
        for i in range(width):
            self.add_column(i)
        # return
        # for i in range(height):
        #     self.tiles += [[]]
        #     for j in range(width):
        #         y = i * self.thw
        #         x = j * self.thw
        #         tile = TileWidget(self, self.thw, i, j)
        #         tile.move(x, y)
        #         self.tiles[i] += [tile]
        # size = self.sizeHint()
        # self.setFixedSize(size.width(), size.height())

    def get_tiles_with_name(self, name: str) -> list[TileWidget]:
        result = []
        for row in self.tiles:
            for tile in row:
                if tile.tile_name == name:
                    result += [tile]
        return result

    def select(self, tile: TileWidget):
        modifiers = QApplication.keyboardModifiers()
        shift_pressed = modifiers == Qt.ShiftModifier
        if shift_pressed:
            self.second_selected_loc = [tile.i, tile.j]
        else:
            if tile.selected:
                self.first_selected_loc = []
            else:
                self.first_selected_loc = [tile.i, tile.j]
            self.second_selected_loc = self.first_selected_loc
        self.update_selected()

    def update_selected(self):
        for row in self.tiles:
            for tile in row:
                tile.set_selected(self.tile_in_selected_range(tile))

    def get_selected_tiles(self) -> list[TileWidget]:
        result = []
        for row in self.tiles:
            for tile in row:
                if self.tile_in_selected_range(tile):
                    result += [tile]
        return result

    def tile_in_selected_range(self, tile: TileWidget):
        if len(self.first_selected_loc) == 0:
            return False
        mini = min(self.first_selected_loc[0], self.second_selected_loc[0])
        minj = min(self.first_selected_loc[1], self.second_selected_loc[1])
        maxi = max(self.first_selected_loc[0], self.second_selected_loc[0])
        maxj = max(self.first_selected_loc[1], self.second_selected_loc[1])
        result = (tile.i >= mini and tile.i <= maxi) and (tile.j >= minj and tile.j <= maxj)
        return result

    def show_context_menu_for(self, tile: TileWidget, e: QMouseEvent):
        action = self.tile_context_menu.exec_(e.globalPos())
        if action == self.set_spawn_action:
            self.parent_window.parent_window.game.md.start_y = tile.i
            self.parent_window.parent_window.game.md.start_x = tile.j
            util.show_message_box('Spawn point set!')

    # actions

    def change_tile_size(self, diff: int):
        if self.thw + diff <= 0:
            return
        self.thw += diff
        size = self.sizeHint()
        self.setFixedSize(size.width(), size.height())
        for i in range(len(self.tiles)):
            for j in range(len(self.tiles[0])):
                y = i * self.thw
                x = j * self.thw
                self.tiles[i][j].move(x, y)
                self.tiles[i][j].update_size(self.tiles[i][j].hw + diff)

    def increase_tile_size(self):
        self.change_tile_size(HW_INCREASE_AMOUNT)        

    def decrease_tile_size(self):
        self.change_tile_size(-HW_INCREASE_AMOUNT)        

RI_WINDOW_TITLE = 'Room info'

LINE_EDIT_WIDTH = 200

RI_NAME_LABEL_TEXT = 'Room name:'
RI_NAME_LABEL_WIDTH = 100
RI_NAME_LABEL_HEIGHT = util.LABEL_HEIGHT
RI_NAME_LABEL_Y = util.BETWEEN_ELEMENTS_VERTICAL
RI_NAME_LABEL_X = util.BETWEEN_ELEMENTS_HORIZONTAL
RI_NAME_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

RI_NAME_EDIT_WIDTH = LINE_EDIT_WIDTH
RI_NAME_EDIT_HEIGHT = RI_NAME_LABEL_HEIGHT
RI_NAME_EDIT_Y = RI_NAME_LABEL_Y
RI_NAME_EDIT_X = RI_NAME_LABEL_X + RI_NAME_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

BUTTON_WIDTH = 100

RI_SAVE_BUTTON_TEXT = 'Save'
RI_SAVE_BUTTON_Y = RI_NAME_EDIT_Y + RI_NAME_EDIT_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
RI_SAVE_BUTTON_X = RI_NAME_LABEL_X
RI_SAVE_BUTTON_HEIGHT = util.LABEL_HEIGHT
RI_SAVE_BUTTON_WIDTH = BUTTON_WIDTH

RI_CANCEL_BUTTON_TEXT = 'Cancel'
RI_CANCEL_BUTTON_Y = RI_SAVE_BUTTON_Y
RI_CANCEL_BUTTON_X = RI_SAVE_BUTTON_X + RI_SAVE_BUTTON_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL
RI_CANCEL_BUTTON_HEIGHT = util.LABEL_HEIGHT
RI_CANCEL_BUTTON_WIDTH = BUTTON_WIDTH

RI_WINDOW_HEIGHT = 400
RI_WINDOW_WIDTH = 400

class RoomInfoEditorWindow(QDialog):
    def __init__(self, parent: 'RoomEditorWindow'):
        super().__init__()
        self.setWindowTitle(RI_WINDOW_TITLE)
        self.parent_window = parent
        self.setFixedSize(RI_WINDOW_WIDTH, RI_WINDOW_HEIGHT)
        self.initUI()
        self.ok = False

    def initUI(self):
        # labels

        name_label = QLabel(self)
        name_label.setText(RI_NAME_LABEL_TEXT)
        name_label.move(RI_NAME_LABEL_X, RI_NAME_LABEL_Y)
        name_label.setFixedSize(RI_NAME_LABEL_WIDTH, RI_NAME_LABEL_HEIGHT)
        name_label.setStyleSheet(RI_NAME_LABEL_STYLE_SHEET)

        # line edits

        self.name_edit = QLineEdit(self)
        self.name_edit.move(RI_NAME_EDIT_X, RI_NAME_EDIT_Y)
        self.name_edit.setFixedSize(RI_NAME_EDIT_WIDTH, RI_NAME_EDIT_HEIGHT)
        self.name_edit.setText(self.parent_window.rd.name)

        # buttons

        save_button = QPushButton(self)
        save_button.setText(RI_SAVE_BUTTON_TEXT)
        save_button.move(RI_SAVE_BUTTON_X, RI_SAVE_BUTTON_Y)
        save_button.setFixedSize(RI_SAVE_BUTTON_WIDTH, RI_SAVE_BUTTON_HEIGHT)
        save_button.clicked.connect(self.save_action)

        cancel_button = QPushButton(self)
        cancel_button.setText(RI_CANCEL_BUTTON_TEXT)
        cancel_button.move(RI_CANCEL_BUTTON_X, RI_CANCEL_BUTTON_Y)
        cancel_button.setFixedSize(RI_CANCEL_BUTTON_WIDTH, RI_CANCEL_BUTTON_HEIGHT)
        cancel_button.clicked.connect(self.cancel_action)

    # methods

    def edit(self):
        self.exec()
        if not self.ok:
            return
        new_name = self.name_edit.text()
        self.parent_window.parent_window.replace_room_name(self.parent_window.rd.name, new_name)
        self.parent_window.rd.name = new_name
        self.parent_window.setWindowTitle(WINDOW_TITLE_FORMAT.format(new_name))
    
    # actions

    def save_action(self):
        new_name = self.name_edit.text()
        if new_name != self.parent_window.rd.name:
            names = [room.name for room in self.parent_window.parent_window.game.rooms]
            if new_name in names:
                util.show_message_box(f'Room {new_name} already exists')
                return
        self.ok = True
        self.close()

    def cancel_action(self):
        pass

class RoomEditorWindow(QMainWindow):
    def __init__(self, parent, room_name: str):
        super(QMainWindow, self).__init__()
        self.parent_window = parent
        self.rd = gsdk.RoomData(parent.game, room_name)
        self.setWindowTitle(WINDOW_TITLE_FORMAT.format(room_name))
        self.setGeometry(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT)
        self.initUI()

    def get_tile_names(self) -> list[list[str]]:
        result = []
        for row in self.tiles_panel.tiles:
            l = []
            for tile in row:
                l += [tile.tile_name]
            result += [l]
        return result

    def update_elements(self):
        self.tiles_list_model.clear()
        for tile in self.rd.tiles:
            self.add_to_tiles_list(tile)
        self.scripts_list.clear()
        for script in self.rd.scripts:
            self.scripts_list.addItem(script.name)
        height = len(self.rd.room)
        width = len(self.rd.room[0])
        tiles = self.tiles_panel.tiles
        for row in tiles:
            for tile in row:
                tile.setParent(None)
        self.tiles_panel.tiles = []
        # self.tiles_panel.
        self.tiles_panel.create_tiles(height, width)
        d = {}
        for tile in self.rd.tiles:
            d[tile.name] = tile
        a=1
        for i in range(height):
            for j in range(width):
                room = d[self.rd.room[i][j]]
                self.tiles_panel.tiles[i][j].set_template(room)
                self.tiles_panel.tiles[i][j].highlighted = False

    def initUI(self):
        # buttons

        sub_row_top_button = QPushButton(self)
        sub_row_top_button.setText(SUB_BUTTON_TEXT)
        sub_row_top_button.setFixedHeight(RC_BUTTON_SIZE)
        sub_row_top_button.setFixedWidth(RC_BUTTON_SIZE)
        sub_row_top_button.move(SUB_ROW_TOP_BUTTON_X, SUB_ROW_TOP_BUTTON_Y)
        sub_row_top_button.clicked.connect(self.sub_row_top_action)

        add_row_top_button = QPushButton(self)
        add_row_top_button.setText(ADD_BUTTON_TEXT)
        add_row_top_button.setFixedHeight(RC_BUTTON_SIZE)
        add_row_top_button.setFixedWidth(RC_BUTTON_SIZE)
        add_row_top_button.move(ADD_ROW_TOP_BUTTON_X, ADD_ROW_TOP_BUTTON_Y)
        add_row_top_button.clicked.connect(self.add_row_top_action)

        sub_row_bottom_button = QPushButton(self)
        sub_row_bottom_button.setText(SUB_BUTTON_TEXT)
        sub_row_bottom_button.setFixedHeight(RC_BUTTON_SIZE)
        sub_row_bottom_button.setFixedWidth(RC_BUTTON_SIZE)
        sub_row_bottom_button.move(SUB_ROW_BOTTOM_BUTTON_X, SUB_ROW_BOTTOM_BUTTON_Y)
        sub_row_bottom_button.clicked.connect(self.sub_row_bottom_action)

        add_row_bottom_button = QPushButton(self)
        add_row_bottom_button.setText(ADD_BUTTON_TEXT)
        add_row_bottom_button.setFixedHeight(RC_BUTTON_SIZE)
        add_row_bottom_button.setFixedWidth(RC_BUTTON_SIZE)
        add_row_bottom_button.move(ADD_ROW_BOTTOM_BUTTON_X, ADD_ROW_BOTTOM_BUTTON_Y)
        add_row_bottom_button.clicked.connect(self.add_row_bottom_action)

        sub_column_left_button = QPushButton(self)
        sub_column_left_button.setText(SUB_BUTTON_TEXT)
        sub_column_left_button.setFixedHeight(RC_BUTTON_SIZE)
        sub_column_left_button.setFixedWidth(RC_BUTTON_SIZE)
        sub_column_left_button.move(SUB_COLUMN_LEFT_BUTTON_X, SUB_COLUMN_LEFT_BUTTON_Y)
        sub_column_left_button.clicked.connect(self.sub_column_left_action)

        add_column_left_button = QPushButton(self)
        add_column_left_button.setText(ADD_BUTTON_TEXT)
        add_column_left_button.setFixedHeight(RC_BUTTON_SIZE)
        add_column_left_button.setFixedWidth(RC_BUTTON_SIZE)
        add_column_left_button.move(ADD_COLUMN_LEFT_BUTTON_X, ADD_COLUMN_LEFT_BUTTON_Y)
        add_column_left_button.clicked.connect(self.add_column_left_action)

        sub_column_right_button = QPushButton(self)
        sub_column_right_button.setText(SUB_BUTTON_TEXT)
        sub_column_right_button.setFixedHeight(RC_BUTTON_SIZE)
        sub_column_right_button.setFixedWidth(RC_BUTTON_SIZE)
        sub_column_right_button.move(SUB_COLUMN_RIGHT_BUTTON_X, SUB_COLUMN_RIGHT_BUTTON_Y)
        sub_column_right_button.clicked.connect(self.sub_column_right_action)

        add_column_right_button = QPushButton(self)
        add_column_right_button.setText(ADD_BUTTON_TEXT)
        add_column_right_button.setFixedHeight(RC_BUTTON_SIZE)
        add_column_right_button.setFixedWidth(RC_BUTTON_SIZE)
        add_column_right_button.move(ADD_COLUMN_RIGHT_BUTTON_X, ADD_COLUMN_RIGHT_BUTTON_Y)
        add_column_right_button.clicked.connect(self.add_column_right_action)

        edit_room_info_button = QPushButton(self)
        edit_room_info_button.setText(EDIT_ROOM_INFO_BUTTON_TEXT)
        edit_room_info_button.setFixedHeight(EDIT_ROOM_INFO_BUTTON_HEIGHT)
        edit_room_info_button.setFixedWidth(EDIT_ROOM_INFO_BUTTON_WIDTH)
        edit_room_info_button.move(EDIT_ROOM_INFO_BUTTON_X, EDIT_ROOM_INFO_BUTTON_Y)
        edit_room_info_button.clicked.connect(self.edit_room_info_action)

        zoom_plus_button = QPushButton(self)
        zoom_plus_button.setText(ZOOM_PLUS_BUTTON_TEXT)
        zoom_plus_button.setFixedHeight(ZOOM_BUTTON_HEIGHT)
        zoom_plus_button.setFixedWidth(ZOOM_BUTTON_WIDTH)
        zoom_plus_button.move(ZOOM_PLUS_BUTTON_X, ZOOM_PLUS_BUTTON_Y)
        zoom_plus_button.clicked.connect(self.zoom_plus_action)

        zoom_minus_button = QPushButton(self)
        zoom_minus_button.setText(ZOOM_MINUS_BUTTON_TEXT)
        zoom_minus_button.setFixedHeight(ZOOM_BUTTON_HEIGHT)
        zoom_minus_button.setFixedWidth(ZOOM_BUTTON_WIDTH)
        zoom_minus_button.move(ZOOM_MINUS_BUTTON_X, ZOOM_MINUS_BUTTON_Y)
        zoom_minus_button.clicked.connect(self.zoom_minus_action)

        new_tile_button = QPushButton(self)
        new_tile_button.setText(NEW_TILE_BUTTON_TEXT)
        new_tile_button.setFixedHeight(NEW_TILE_BUTTON_HEIGHT)
        new_tile_button.setFixedWidth(NEW_TILE_BUTTON_WIDTH)
        new_tile_button.move(NEW_TILE_BUTTON_X, NEW_TILE_BUTTON_Y)
        new_tile_button.clicked.connect(self.new_tile_action)

        edit_tile_button = QPushButton(self)
        edit_tile_button.setText(EDIT_TILE_BUTTON_TEXT)
        edit_tile_button.setFixedHeight(EDIT_TILE_BUTTON_HEIGHT)
        edit_tile_button.setFixedWidth(EDIT_TILE_BUTTON_WIDTH)
        edit_tile_button.move(EDIT_TILE_BUTTON_X, EDIT_TILE_BUTTON_Y)
        edit_tile_button.clicked.connect(self.edit_tile_action)

        delete_tile_button = QPushButton(self)
        delete_tile_button.setText(DELETE_TILE_BUTTON_TEXT)
        delete_tile_button.setFixedHeight(DELETE_TILE_BUTTON_HEIGHT)
        delete_tile_button.setFixedWidth(DELETE_TILE_BUTTON_WIDTH)
        delete_tile_button.move(DELETE_TILE_BUTTON_X, DELETE_TILE_BUTTON_Y)
        delete_tile_button.clicked.connect(self.delete_tile_action)

        new_script_button = QPushButton(self)
        new_script_button.setText(NEW_SCRIPT_BUTTON_TEXT)
        new_script_button.setFixedHeight(NEW_SCRIPT_BUTTON_HEIGHT)
        new_script_button.setFixedWidth(NEW_SCRIPT_BUTTON_WIDTH)
        new_script_button.move(NEW_SCRIPT_BUTTON_X, NEW_SCRIPT_BUTTON_Y)
        new_script_button.clicked.connect(self.new_script_action)

        edit_script_button = QPushButton(self)
        edit_script_button.setText(EDIT_SCRIPT_BUTTON_TEXT)
        edit_script_button.setFixedHeight(EDIT_SCRIPT_BUTTON_HEIGHT)
        edit_script_button.setFixedWidth(EDIT_SCRIPT_BUTTON_WIDTH)
        edit_script_button.move(EDIT_SCRIPT_BUTTON_X, EDIT_SCRIPT_BUTTON_Y)
        edit_script_button.clicked.connect(self.edit_script_action)

        delete_script_button = QPushButton(self)
        delete_script_button.setText(DELETE_SCRIPT_BUTTON_TEXT)
        delete_script_button.setFixedHeight(DELETE_SCRIPT_BUTTON_HEIGHT)
        delete_script_button.setFixedWidth(DELETE_SCRIPT_BUTTON_WIDTH)
        delete_script_button.move(DELETE_SCRIPT_BUTTON_X, DELETE_SCRIPT_BUTTON_Y)
        delete_script_button.clicked.connect(self.delete_script_action)

        # labels

        tiles_label = QLabel(self)
        tiles_label.setText(TILES_LABEL_TEXT)
        tiles_label.setFixedHeight(TILES_LABEL_HEIGHT)
        tiles_label.setFixedWidth(TILES_LABEL_WIDTH)
        tiles_label.move(TILES_LABEL_X, TILES_LABEL_Y)
        tiles_label.setStyleSheet(TILES_LABEL_STYLE_SHEET)

        scripts_label = QLabel(self)
        scripts_label.setText(SCRIPTS_LABEL_TEXT)
        scripts_label.setFixedHeight(SCRIPTS_LABEL_HEIGHT)
        scripts_label.setFixedWidth(SCRIPTS_LABEL_WIDTH)
        scripts_label.move(SCRIPTS_LABEL_X, SCRIPTS_LABEL_Y)
        scripts_label.setStyleSheet(SCRIPTS_LABEL_STYLE_SHEET)

        # lists

        self.tiles_list = QListView(self)
        self.tiles_list.setEditTriggers(QAbstractItemView.NoEditTriggers)
        self.tiles_list.setFixedHeight(TILES_LIST_HEIGHT)
        self.tiles_list.setFixedWidth(TILES_LIST_WIDTH)
        self.tiles_list.move(TILES_LIST_X, TILES_LIST_Y)
        def dc(e):
            self.edit_tile_action()
        self.tiles_list.mouseDoubleClickEvent = dc
        self.tiles_list.pressed.connect(self.highlight_tiles)
        self.tiles_list_model = QStandardItemModel(self.tiles_list)
        self.tiles_list.setModel(self.tiles_list_model)

        self.scripts_list = QListWidget(self)
        self.scripts_list.setEditTriggers(QAbstractItemView.NoEditTriggers)
        self.scripts_list.setFixedHeight(SCRIPTS_LIST_HEIGHT)
        self.scripts_list.setFixedWidth(SCRIPTS_LIST_WIDTH)
        self.scripts_list.move(SCRIPTS_LIST_X, SCRIPTS_LIST_Y)
        self.scripts_list.itemDoubleClicked.connect(self.tile_double_click_action)

        # tiles

        scroll = QScrollArea(self)
        scroll.setVerticalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        scroll.setHorizontalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        # # scroll.setWidgetResizable(True)
        scroll.setGeometry(TILES_PANEL_X, TILES_PANEL_X, TILES_PANEL_HEIGHT, TILES_PANEL_WIDTH)
        self.tiles_panel = TilePanelWidget(self, STARTING_TILE_HW)
        self.tiles_panel.move(TILES_PANEL_X, TILES_PANEL_Y)
        scroll.setWidget(self.tiles_panel)

        self.tiles_panel.setFocus()

    # methods

    def highlight_tiles(self, e):
        i = self.tiles_list.currentIndex().row()
        tname = self.rd.tiles[i].name
        for row in self.tiles_panel.tiles:
            for tile in row:
                tile.highlighted = tile.tile_name == tname
                tile.update()

    def get_selected_tile(self) -> gsdk.TileData:
        si = self.tiles_list.selectedIndexes()
        if len(si) != 1:
            return None
        i = si[0].row()
        return self.rd.tiles[i]

    def has_script(self, name: str) -> bool:
        return self.rd.has_script(name)

    def has_tile(self, name: str) -> bool:
        return self.rd.has_tile(name)

    def has_warpcode(self, wc: str) -> bool:
        return self.rd.has_warpcode(wc)

    def add_to_tiles_list(self, tile: gsdk.TileData):
        item = tile.to_QStandardItem()
        self.tiles_list_model.appendRow(item)

    # actions

    def edit_room_info_action(self):
        RoomInfoEditorWindow(self).edit()

    def add_column_left_action(self):
        self.tiles_panel.add_column(0)

    def sub_column_left_action(self):
        self.tiles_panel.delete_column(0)

    def add_column_right_action(self):
        width = len(self.tiles_panel.tiles[0])
        self.tiles_panel.add_column(width)

    def sub_column_right_action(self):
        width = len(self.tiles_panel.tiles[0]) - 1
        self.tiles_panel.delete_column(width)

    def add_row_top_action(self):
        self.tiles_panel.add_row(0)

    def sub_row_top_action(self):
        self.tiles_panel.delete_row(0)

    def add_row_bottom_action(self):
        height = len(self.tiles_panel.tiles)
        self.tiles_panel.add_row(height)

    def sub_row_bottom_action(self):
        height = len(self.tiles_panel.tiles) - 1
        self.tiles_panel.delete_row(height)

    def tile_double_click_action(self, e):
        self.rd.load_script = self.scripts_list.selectedItems()[0].text()
        util.show_message_box(f'Script {self.rd.load_script} is set as load script')

    def keyPressEvent(self, e: QKeyEvent) -> None:
        self.tiles_panel.handle_key(e)

    def zoom_plus_action(self):
        self.tiles_panel.increase_tile_size()

    def zoom_minus_action(self):
        self.tiles_panel.decrease_tile_size()

    def new_tile_action(self):
        tile, ok = TileEditor(self).edit()
        if not ok:
            return
        self.rd.tiles += [tile]
        self.add_to_tiles_list(tile)

    def edit_tile_action(self):
        si = self.tiles_list.selectedIndexes()
        if len(si) != 1:
            return
        i = si[0].row()
        tile = self.rd.tiles[i]
        old_name = tile.name
        new_tile, ok = TileEditor(self, tile).edit()
        if not ok:
            return
        self.rd.tiles[i] = new_tile
        self.tiles_list_model.removeRow(i)
        self.tiles_list_model.insertRow(i, new_tile.to_QStandardItem())
        tiles = self.tiles_panel.get_tiles_with_name(old_name)
        for tile in tiles:
            tile.set_template(new_tile)
        for tile in self.rd.tiles:
            if tile.broken_alt == old_name:
                tile.broken_alt = new_tile.name

    def delete_tile_action(self):
        si = self.tiles_list.selectedIndexes()
        if len(si) != 1:
            return
        i = si[0].row()
        tile = self.rd.tiles[i]
        old_name = tile.name
        resp = util.show_message_box(f'Are you sure you want to delete tile {tile.name}?', QMessageBox.Yes|QMessageBox.No)
        if resp == QMessageBox.No:
            return
        self.rd.tiles.remove(tile)
        self.tiles_list_model.removeRow(i)
        tiles = self.tiles_panel.get_tiles_with_name(old_name)
        for tile in tiles:
            tile.reset()

    def new_script_action(self):
        script, ok = ScriptEditor(self).edit()
        if not ok:
            return
        self.rd.scripts += [script]
        self.scripts_list.addItem(script.name)

    def edit_script_action(self):
        si = self.scripts_list.selectedIndexes()
        if len(si) != 1:
            return
        i = si[0].row()
        script = self.rd.scripts[i]
        old_name = script.name
        new_script, ok = ScriptEditor(self, script).edit()
        if not ok:
            return
        self.rd.scripts[i] = new_script
        self.scripts_list.selectedItems()[0].setText(new_script.name)
        if self.rd.load_script == old_name:
            self.rd.load_script = new_script.name
        for tile in self.rd.tiles:
            if tile.step_script == old_name:
                tile.step_script = new_script.name
            if tile.interact_script == old_name:
                tile.interact_script = new_script.name

    def delete_script_action(self):
        si = self.scripts_list.selectedIndexes()
        if len(si) != 1:
            return
        i = si[0].row()
        script = self.rd.scripts[i]
        old_name = script.name
        resp = util.show_message_box(f'Are you sure you want to delete script {script.name}?', QMessageBox.Yes|QMessageBox.No)
        if resp == QMessageBox.No:
            return
        self.rd.scripts.remove(script)
        self.scripts_list.takeItem(self.scripts_list.row(self.scripts_list.selectedItems()[0]))
        warnings = []
        if self.rd.load_script == old_name:
            self.rd.load_script = ''
            warnings += [f'Load script of room {self.rd.name} was removed(deleted {old_name})']
        for tile in self.rd.tiles:
            if tile.step_script == old_name:
                warnings += [f'Step script of tile {tile.name} was removed(deleted {old_name})']
                tile.step_script = ''
            if tile.interact_script == old_name:
                warnings += [f'Interact script of tile {tile.name} was removed(deleted {old_name})']
                tile.interact_script = ''
        if len(warnings) != 0:
            util.show_message_box('Warning:\n' + '\n'.join(warnings))

SCRIPT_TEXT_EDIT_HEIGHT = 600
SCRIPT_TEXT_EDIT_WIDTH = 600

SCRIPT_NAME_LABEL_TEXT = 'Name: '
SCRIPT_NAME_LABEL_HEIGHT = util.LABEL_HEIGHT
SCRIPT_NAME_LABEL_WIDTH = 100
SCRIPT_NAME_LABEL_Y = util.BETWEEN_ELEMENTS_VERTICAL
SCRIPT_NAME_LABEL_X = util.BETWEEN_ELEMENTS_HORIZONTAL
SCRIPT_NAME_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

SCRIPT_NAME_EDIT_HEIGHT = SCRIPT_NAME_LABEL_HEIGHT
SCRIPT_NAME_EDIT_WIDTH = SCRIPT_TEXT_EDIT_WIDTH - SCRIPT_NAME_LABEL_WIDTH - util.BETWEEN_ELEMENTS_HORIZONTAL
SCRIPT_NAME_EDIT_Y = SCRIPT_NAME_LABEL_Y
SCRIPT_NAME_EDIT_X = SCRIPT_NAME_LABEL_X + SCRIPT_NAME_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

SCRIPT_TEXT_EDIT_Y = SCRIPT_NAME_LABEL_HEIGHT +SCRIPT_NAME_LABEL_Y + util.BETWEEN_ELEMENTS_VERTICAL
SCRIPT_TEXT_EDIT_X = SCRIPT_NAME_LABEL_X

SCRIPT_EDITOR_WIDTH = util.BETWEEN_ELEMENTS_HORIZONTAL + SCRIPT_TEXT_EDIT_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

SB_WIDTH = SCRIPT_EDITOR_WIDTH // 2 - util.BETWEEN_ELEMENTS_HORIZONTAL

SCRIPT_SAVE_BUTTON_TEXT = 'Save'
SCRIPT_SAVE_BUTTON_HEIGHT = util.BUTTON_HEIGHT
SCRIPT_SAVE_BUTTON_WIDTH = SB_WIDTH
SCRIPT_SAVE_BUTTON_Y = SCRIPT_TEXT_EDIT_HEIGHT + SCRIPT_TEXT_EDIT_Y + util.BETWEEN_ELEMENTS_VERTICAL
SCRIPT_SAVE_BUTTON_X = SCRIPT_TEXT_EDIT_X

SCRIPT_CANCEL_BUTTON_TEXT = 'Cancel'
SCRIPT_CANCEL_BUTTON_HEIGHT = util.BUTTON_HEIGHT
SCRIPT_CANCEL_BUTTON_WIDTH = SB_WIDTH
SCRIPT_CANCEL_BUTTON_Y = SCRIPT_SAVE_BUTTON_Y
SCRIPT_CANCEL_BUTTON_X = SCRIPT_SAVE_BUTTON_X + SCRIPT_SAVE_BUTTON_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

SCRIPT_EDITOR_HEIGHT = SCRIPT_CANCEL_BUTTON_Y + SCRIPT_CANCEL_BUTTON_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
SCRIPT_EDITOR_TITLE = 'Script Editor'

class ScriptEditor(QDialog):
    def __init__(self, parent: RoomEditorWindow, script: gsdk.ScriptData=None):
        super().__init__()
        self.setWindowTitle(SCRIPT_EDITOR_TITLE)
        self.parent_window = parent
        self.ok = False
        self.setGeometry(0, 0, SCRIPT_EDITOR_WIDTH, SCRIPT_EDITOR_HEIGHT)
        self.override_allowed = True
        if script is None:
            self.override_allowed = False
            script = gsdk.ScriptData()
        self.initUI(script)

    def initUI(self, script: gsdk.ScriptData=None):
        # label

        name_label = QLabel(self)
        name_label.setText(SCRIPT_NAME_LABEL_TEXT)
        name_label.setFixedHeight(SCRIPT_NAME_LABEL_HEIGHT)
        name_label.setFixedWidth(SCRIPT_NAME_LABEL_WIDTH)
        name_label.move(SCRIPT_NAME_LABEL_Y, SCRIPT_NAME_LABEL_X)
        name_label.setStyleSheet(SCRIPT_NAME_LABEL_STYLE_SHEET)

        # line edit

        self.name_edit = QLineEdit(self)
        self.name_edit.setText(script.name)
        self.name_edit.setFixedHeight(SCRIPT_NAME_EDIT_HEIGHT)
        self.name_edit.setFixedWidth(SCRIPT_NAME_EDIT_WIDTH)
        self.name_edit.move(SCRIPT_NAME_EDIT_X, SCRIPT_NAME_EDIT_Y)

        # text edit

        self.text_edit = QTextEdit(self)
        self.text_edit.setText(script.text)
        self.text_edit.setFixedHeight(SCRIPT_TEXT_EDIT_HEIGHT)
        self.text_edit.setFixedWidth(SCRIPT_TEXT_EDIT_WIDTH)
        self.text_edit.move(SCRIPT_TEXT_EDIT_X, SCRIPT_TEXT_EDIT_Y)

        # buttons

        save_button = QPushButton(self)
        save_button.setText(SCRIPT_SAVE_BUTTON_TEXT)
        save_button.setFixedHeight(SCRIPT_SAVE_BUTTON_HEIGHT)
        save_button.setFixedWidth(SCRIPT_SAVE_BUTTON_WIDTH)
        save_button.move(SCRIPT_SAVE_BUTTON_X, SCRIPT_SAVE_BUTTON_Y)
        save_button.clicked.connect(self.save_action)

        cancel_button = QPushButton(self)
        cancel_button.setText(SCRIPT_CANCEL_BUTTON_TEXT)
        cancel_button.setFixedHeight(SCRIPT_CANCEL_BUTTON_HEIGHT)
        cancel_button.setFixedWidth(SCRIPT_CANCEL_BUTTON_WIDTH)
        cancel_button.move(SCRIPT_CANCEL_BUTTON_X, SCRIPT_CANCEL_BUTTON_Y)
        cancel_button.clicked.connect(self.cancel_action)

    def edit(self) -> tuple[gsdk.ScriptData, bool]:
        self.exec()
        result = gsdk.ScriptData()
        result.name = self.name_edit.text()
        result.text = self.text_edit.toPlainText()
        return result, self.ok

    # actions

    def save_action(self):
        name = self.name_edit.text()
        if name == '':
            util.show_message_box('Enter the name of the script')
            return
        if self.text_edit.toPlainText() == '':
            util.show_message_box('Enter the text of the script')
            return
        self.ok = True
        has = self.parent_window.has_script(self.name_edit.text())
        if not self.override_allowed and has:
            util.show_message_box(f'Script {name} already exists')
            return
        check = gsdk.is_good_script(self.text_edit.toPlainText())
        if not check.ok:
            util.show_not_save_ready(check)            
            return
        self.close()

    def cancel_action(self):
        self.ok = False
        self.close()

TILE_EDITOR_WIDTH = 400

TILE_NAME_LABEL_TEXT = 'Name:'
TILE_NAME_LABEL_HEIGHT = util.LABEL_HEIGHT
TILE_NAME_LABEL_WIDTH = 100
TILE_NAME_LABEL_Y = util.BETWEEN_ELEMENTS_VERTICAL
TILE_NAME_LABEL_X = util.BETWEEN_ELEMENTS_HORIZONTAL
TILE_NAME_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

TILE_NAME_EDIT_HEIGHT = util.LABEL_HEIGHT
TILE_NAME_EDIT_WIDTH = TILE_EDITOR_WIDTH - TILE_NAME_LABEL_WIDTH - util.BETWEEN_ELEMENTS_HORIZONTAL * 3
TILE_NAME_EDIT_Y = TILE_NAME_LABEL_Y
TILE_NAME_EDIT_X = TILE_NAME_LABEL_X + TILE_NAME_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

TILE_DISPLAY_NAME_LABEL_TEXT = 'Display name:'
TILE_DISPLAY_NAME_LABEL_HEIGHT = util.LABEL_HEIGHT
TILE_DISPLAY_NAME_LABEL_WIDTH = 100
TILE_DISPLAY_NAME_LABEL_Y = TILE_NAME_EDIT_Y + TILE_NAME_EDIT_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
TILE_DISPLAY_NAME_LABEL_X = TILE_NAME_LABEL_X
TILE_DISPLAY_NAME_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

TILE_DISPLAY_NAME_EDIT_HEIGHT = util.LABEL_HEIGHT
TILE_DISPLAY_NAME_EDIT_WIDTH = TILE_EDITOR_WIDTH - TILE_DISPLAY_NAME_LABEL_WIDTH - util.BETWEEN_ELEMENTS_HORIZONTAL * 3
TILE_DISPLAY_NAME_EDIT_Y = TILE_DISPLAY_NAME_LABEL_Y
TILE_DISPLAY_NAME_EDIT_X = TILE_DISPLAY_NAME_LABEL_X + TILE_DISPLAY_NAME_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

WARPCODE_LABEL_TEXT = 'Warpcode:'
WARPCODE_LABEL_HEIGHT = util.LABEL_HEIGHT
WARPCODE_LABEL_WIDTH = 100
WARPCODE_LABEL_Y = TILE_DISPLAY_NAME_EDIT_Y + TILE_DISPLAY_NAME_EDIT_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
WARPCODE_LABEL_X = TILE_DISPLAY_NAME_LABEL_X
WARPCODE_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

WARPCODE_EDIT_HEIGHT = util.LABEL_HEIGHT
WARPCODE_EDIT_WIDTH = TILE_EDITOR_WIDTH - WARPCODE_LABEL_WIDTH - util.BETWEEN_ELEMENTS_HORIZONTAL * 3
WARPCODE_EDIT_Y = WARPCODE_LABEL_Y
WARPCODE_EDIT_X = WARPCODE_LABEL_X + WARPCODE_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

INTERACT_LABEL_TEXT = 'Interact script:'
INTERACT_LABEL_HEIGHT = util.LABEL_HEIGHT
INTERACT_LABEL_WIDTH = 100
INTERACT_LABEL_Y = WARPCODE_LABEL_Y + WARPCODE_LABEL_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
INTERACT_LABEL_X = WARPCODE_LABEL_X
INTERACT_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

INTERACT_COMBO_BOX_HEIGHT = util.LABEL_HEIGHT
INTERACT_COMBO_BOX_WIDTH = TILE_EDITOR_WIDTH - INTERACT_LABEL_WIDTH - util.BETWEEN_ELEMENTS_HORIZONTAL*3
INTERACT_COMBO_BOX_Y = INTERACT_LABEL_Y
INTERACT_COMBO_BOX_X = INTERACT_LABEL_X + INTERACT_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

STEP_LABEL_TEXT = 'Step script:'
STEP_LABEL_HEIGHT = util.LABEL_HEIGHT
STEP_LABEL_WIDTH = 100
STEP_LABEL_Y = INTERACT_LABEL_Y + INTERACT_LABEL_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
STEP_LABEL_X = INTERACT_LABEL_X
STEP_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

STEP_COMBO_BOX_HEIGHT = util.LABEL_HEIGHT
STEP_COMBO_BOX_WIDTH = TILE_EDITOR_WIDTH - STEP_LABEL_WIDTH - util.BETWEEN_ELEMENTS_HORIZONTAL*3
STEP_COMBO_BOX_Y = STEP_LABEL_Y
STEP_COMBO_BOX_X = STEP_LABEL_X + STEP_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

PADDING = 10

PASSABLE_LABEL_TEXT = 'Passable:'
PASSABLE_LABEL_HEIGHT = util.LABEL_HEIGHT
PASSABLE_LABEL_WIDTH = 100
PASSABLE_LABEL_Y = STEP_LABEL_Y + STEP_LABEL_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
PASSABLE_LABEL_X = STEP_LABEL_X + util.BETWEEN_ELEMENTS_HORIZONTAL * PADDING
PASSABLE_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

PASSABLE_CHECK_BOX_Y = PASSABLE_LABEL_Y
PASSABLE_CHECK_BOX_X = PASSABLE_LABEL_X + PASSABLE_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

TRANSPARENT_LABEL_TEXT = 'Seethrough:'
TRANSPARENT_LABEL_HEIGHT = util.LABEL_HEIGHT
TRANSPARENT_LABEL_WIDTH = 100
TRANSPARENT_LABEL_Y = PASSABLE_LABEL_Y
TRANSPARENT_LABEL_X = TILE_EDITOR_WIDTH - util.BETWEEN_ELEMENTS_HORIZONTAL - util.BETWEEN_ELEMENTS_HORIZONTAL * PADDING - TRANSPARENT_LABEL_WIDTH
TRANSPARENT_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

TRANSPARENT_CHECK_BOX_Y = TRANSPARENT_LABEL_Y
TRANSPARENT_CHECK_BOX_X = TRANSPARENT_LABEL_X + TRANSPARENT_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

TILE_COLOR_BUTTON_TEXT = 'Pick color'
TILE_COLOR_BUTTON_HEIGHT = util.BUTTON_HEIGHT
TILE_COLOR_BUTTON_WIDTH = 100

TILE_COLOR_LABEL_HEIGHT = TILE_COLOR_BUTTON_HEIGHT
TILE_COLOR_LABEL_WIDTH = TILE_COLOR_LABEL_HEIGHT
TILE_COLOR_LABEL_STYLE_SHEET = LABEL_STYLE_SHEET

LR_TILE = (TILE_EDITOR_WIDTH - TILE_COLOR_LABEL_WIDTH - util.BETWEEN_ELEMENTS_HORIZONTAL - TILE_COLOR_BUTTON_WIDTH) // 2

TILE_COLOR_LABEL_Y = TRANSPARENT_LABEL_Y + TRANSPARENT_LABEL_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
TILE_COLOR_LABEL_X = LR_TILE

TILE_COLOR_BUTTON_Y = TILE_COLOR_LABEL_Y
TILE_COLOR_BUTTON_X = TILE_COLOR_LABEL_X + TILE_COLOR_LABEL_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

TB_WIDTH = TILE_EDITOR_WIDTH // 2 - util.BETWEEN_ELEMENTS_HORIZONTAL * 2

TILE_SAVE_BUTTON_TEXT = 'Save'
TILE_SAVE_BUTTON_HEIGHT = util.BUTTON_HEIGHT
TILE_SAVE_BUTTON_WIDTH = TB_WIDTH
TILE_SAVE_BUTTON_Y = TILE_COLOR_BUTTON_Y + TILE_COLOR_BUTTON_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
TILE_SAVE_BUTTON_X = TRANSPARENT_LABEL_X

TILE_CANCEL_BUTTON_TEXT = 'Cancel'
TILE_CANCEL_BUTTON_HEIGHT = util.BUTTON_HEIGHT
TILE_CANCEL_BUTTON_WIDTH = TB_WIDTH
TILE_CANCEL_BUTTON_Y = TILE_SAVE_BUTTON_Y
TILE_CANCEL_BUTTON_X = TILE_SAVE_BUTTON_X + TILE_SAVE_BUTTON_WIDTH + util.BETWEEN_ELEMENTS_HORIZONTAL

TILE_EDITOR_HEIGHT = TILE_CANCEL_BUTTON_Y + TILE_CANCEL_BUTTON_HEIGHT + util.BETWEEN_ELEMENTS_VERTICAL
TILE_EDITOR_TITLE = 'Tile editor'

class TileEditor(QDialog):
    def __init__(self, parent: RoomEditorWindow, tile: gsdk.ScriptData=None):
        super().__init__()
        self.setWindowTitle(TILE_EDITOR_TITLE)
        self.parent_window = parent
        self.ok = False
        self.override_allowed = True
        self.setGeometry(0, 0, TILE_EDITOR_WIDTH, TILE_EDITOR_HEIGHT)
        if tile is None:
            self.override_allowed = False
            tile = gsdk.TileData()
        self.initUI()
        self.apply_values(tile)

    def initUI(self):
        # labels

        name_label = QLabel(self)
        name_label.setText(TILE_NAME_LABEL_TEXT)
        name_label.setFixedHeight(TILE_NAME_LABEL_HEIGHT)
        name_label.setFixedWidth(TILE_NAME_LABEL_WIDTH)
        name_label.move(TILE_NAME_LABEL_X, TILE_NAME_LABEL_Y)
        name_label.setStyleSheet(TILE_NAME_LABEL_STYLE_SHEET)

        display_name_label = QLabel(self)
        display_name_label.setText(TILE_DISPLAY_NAME_LABEL_TEXT)
        display_name_label.setFixedHeight(TILE_DISPLAY_NAME_LABEL_HEIGHT)
        display_name_label.setFixedWidth(TILE_DISPLAY_NAME_LABEL_WIDTH)
        display_name_label.move(TILE_DISPLAY_NAME_LABEL_X, TILE_DISPLAY_NAME_LABEL_Y)
        display_name_label.setStyleSheet(TILE_DISPLAY_NAME_LABEL_STYLE_SHEET)

        warpcode_label = QLabel(self)
        warpcode_label.setText(WARPCODE_LABEL_TEXT)
        warpcode_label.setFixedHeight(WARPCODE_LABEL_HEIGHT)
        warpcode_label.setFixedWidth(WARPCODE_LABEL_WIDTH)
        warpcode_label.move(WARPCODE_LABEL_X, WARPCODE_LABEL_Y)
        warpcode_label.setStyleSheet(WARPCODE_LABEL_STYLE_SHEET)

        interact_label = QLabel(self)
        interact_label.setText(INTERACT_LABEL_TEXT)
        interact_label.setFixedHeight(INTERACT_LABEL_HEIGHT)
        interact_label.setFixedWidth(INTERACT_LABEL_WIDTH)
        interact_label.move(INTERACT_LABEL_X, INTERACT_LABEL_Y)
        interact_label.setStyleSheet(INTERACT_LABEL_STYLE_SHEET)

        step_label = QLabel(self)
        step_label.setText(STEP_LABEL_TEXT)
        step_label.setFixedHeight(STEP_LABEL_HEIGHT)
        step_label.setFixedWidth(STEP_LABEL_WIDTH)
        step_label.move(STEP_LABEL_X, STEP_LABEL_Y)
        step_label.setStyleSheet(STEP_LABEL_STYLE_SHEET)

        passable_label = QLabel(self)
        passable_label.setText(PASSABLE_LABEL_TEXT)
        passable_label.setFixedHeight(PASSABLE_LABEL_HEIGHT)
        passable_label.setFixedWidth(PASSABLE_LABEL_WIDTH)
        passable_label.move(PASSABLE_LABEL_X, PASSABLE_LABEL_Y)
        passable_label.setStyleSheet(PASSABLE_LABEL_STYLE_SHEET)

        transparent_label = QLabel(self)
        transparent_label.setText(TRANSPARENT_LABEL_TEXT)
        transparent_label.setFixedHeight(TRANSPARENT_LABEL_HEIGHT)
        transparent_label.setFixedWidth(TRANSPARENT_LABEL_WIDTH)
        transparent_label.move(TRANSPARENT_LABEL_X, TRANSPARENT_LABEL_Y)
        transparent_label.setStyleSheet(TRANSPARENT_LABEL_STYLE_SHEET)

        self.color_label = QLabel(self)
        self.color_label.setFixedHeight(TILE_COLOR_LABEL_HEIGHT)
        self.color_label.setFixedWidth(TILE_COLOR_LABEL_WIDTH)
        self.color_label.move(TILE_COLOR_LABEL_X, TILE_COLOR_LABEL_Y)
        self.color_label.setStyleSheet(TILE_COLOR_LABEL_STYLE_SHEET)

        # line edits

        self.name_edit = QLineEdit(self)
        self.name_edit.setFixedHeight(TILE_NAME_EDIT_HEIGHT)
        self.name_edit.setFixedWidth(TILE_NAME_EDIT_WIDTH)
        self.name_edit.move(TILE_NAME_EDIT_X, TILE_NAME_EDIT_Y)

        self.display_name_edit = QLineEdit(self)
        self.display_name_edit.setFixedHeight(TILE_DISPLAY_NAME_EDIT_HEIGHT)
        self.display_name_edit.setFixedWidth(TILE_DISPLAY_NAME_EDIT_WIDTH)
        self.display_name_edit.move(TILE_DISPLAY_NAME_EDIT_X, TILE_DISPLAY_NAME_EDIT_Y)

        self.warpcode_edit = QLineEdit(self)
        self.warpcode_edit.setFixedHeight(WARPCODE_EDIT_HEIGHT)
        self.warpcode_edit.setFixedWidth(WARPCODE_EDIT_WIDTH)
        self.warpcode_edit.move(WARPCODE_EDIT_X, WARPCODE_EDIT_Y)

        # radio buttons

        self.passable_check_box = QCheckBox(self)
        self.passable_check_box.setFixedHeight(util.LABEL_HEIGHT)
        self.passable_check_box.move(PASSABLE_CHECK_BOX_X, PASSABLE_CHECK_BOX_Y)

        self.transparent_check_box = QCheckBox(self)
        self.transparent_check_box.setFixedHeight(util.LABEL_HEIGHT)
        self.transparent_check_box.move(TRANSPARENT_CHECK_BOX_X, TRANSPARENT_CHECK_BOX_Y)

        # combo boxes

        script_names = self.parent_window.rd.get_script_names()
        self.interact_combo_box = QComboBox(self)
        self.interact_combo_box.setFixedHeight(INTERACT_COMBO_BOX_HEIGHT)
        self.interact_combo_box.setFixedWidth(INTERACT_COMBO_BOX_WIDTH)
        self.interact_combo_box.move(INTERACT_COMBO_BOX_X, INTERACT_COMBO_BOX_Y)
        self.interact_combo_box.addItem('')
        for sn in script_names:
            self.interact_combo_box.addItem(sn)

        self.step_combo_box = QComboBox(self)
        self.step_combo_box.setFixedHeight(STEP_COMBO_BOX_HEIGHT)
        self.step_combo_box.setFixedWidth(STEP_COMBO_BOX_WIDTH)
        self.step_combo_box.move(STEP_COMBO_BOX_X, STEP_COMBO_BOX_Y)
        self.step_combo_box.addItem('')
        for sn in script_names:
            self.step_combo_box.addItem(sn)

        # buttons

        pick_color_button = QPushButton(self)
        pick_color_button.setText(TILE_COLOR_BUTTON_TEXT)
        pick_color_button.setFixedHeight(TILE_COLOR_BUTTON_HEIGHT)
        pick_color_button.setFixedWidth(TILE_COLOR_BUTTON_WIDTH)
        pick_color_button.move(TILE_COLOR_BUTTON_X, TILE_COLOR_BUTTON_Y)
        pick_color_button.clicked.connect(self.pick_color_action)

        save_button = QPushButton(self)
        save_button.setText(TILE_SAVE_BUTTON_TEXT)
        save_button.setFixedHeight(TILE_SAVE_BUTTON_HEIGHT)
        save_button.setFixedWidth(TILE_SAVE_BUTTON_WIDTH)
        save_button.move(TILE_SAVE_BUTTON_X, TILE_SAVE_BUTTON_Y)
        save_button.clicked.connect(self.save_action)

        cancel_button = QPushButton(self)
        cancel_button.setText(TILE_CANCEL_BUTTON_TEXT)
        cancel_button.setFixedHeight(TILE_CANCEL_BUTTON_HEIGHT)
        cancel_button.setFixedWidth(TILE_CANCEL_BUTTON_WIDTH)
        cancel_button.move(TILE_CANCEL_BUTTON_X, TILE_CANCEL_BUTTON_Y)
        cancel_button.clicked.connect(self.cancel_action)

    def apply_values(self, tile: gsdk.TileData):
        self.name_edit.setText(tile.name)
        self.display_name_edit.setText(tile.display_name)
        self.warpcode_edit.setText(tile.warpcode)
        self.interact_combo_box.setCurrentText(tile.interact_script)
        self.step_combo_box.setCurrentText(tile.step_script)
        self.passable_check_box.setChecked(tile.passable)
        self.transparent_check_box.setChecked(tile.transparent)
        self.breakable_check_box.setChecked(tile.is_breakable)
        self.durability_edit.setText(str(tile.durability))
        self.broken_alt_combo_box.setCurrentText(tile.broken_alt)
        self.apply_color(tile.rgb)
        self.breakable_check_action()

    def edit(self) -> tuple[gsdk.TileData, bool]:
        self.exec()
        result = gsdk.TileData()
        result.name = self.name_edit.text()
        result.display_name = self.display_name_edit.text()
        result.warpcode = self.warpcode_edit.text()
        result.interact_script = self.interact_combo_box.currentText()
        result.step_script = self.step_combo_box.currentText()
        result.passable = self.passable_check_box.isChecked()
        result.transparent = self.transparent_check_box.isChecked()
        result.is_breakable = self.breakable_check_box.isChecked()
        result.durability = int(self.durability_edit.text())
        result.broken_alt = self.broken_alt_combo_box.currentText()
        c = self.color_label.palette().color(self.color_label.backgroundRole())
        result.rgb = [c.red(), c.green(), c.blue()]
        return result, self.ok

    def apply_color(self, rgb: int):
        self.color_label.setStyleSheet(TILE_COLOR_LABEL_STYLE_SHEET + f'background: rgb({rgb[0]}, {rgb[1]}, {rgb[2]});')

    # actions

    def save_action(self):
        name = self.name_edit.text()
        warpcode = self.warpcode_edit.text()
        if name == '':
            util.show_message_box('Enter the name of the tile')
            return
        if self.display_name_edit.text() == '':
            util.show_message_box('Enter the display name of the tile')
            return
        self.ok = True
        if not self.override_allowed:
            has = self.parent_window.has_tile(name)
            if has:
                util.show_message_box(f'Tile {name} already exists')
                return
            has = self.parent_window.has_warpcode(warpcode)
            if has:
                util.show_message_box(f'Warpcode {warpcode} already exists')
                return
        self.close()

    def cancel_action(self):
        self.ok = False
        self.close()

    def breakable_check_action(self):
        checked = self.breakable_check_box.isChecked()
        self.durability_edit.setEnabled(checked)
        self.broken_alt_combo_box.setEnabled(checked)

    def pick_color_action(self):
        color = QColorDialog.getColor()
        if not color.isValid():
            return
        self.apply_color([color.red(), color.green(), color.blue()])