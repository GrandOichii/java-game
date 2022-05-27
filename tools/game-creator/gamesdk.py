from re import S
from PyQt5.QtGui import QColor, QStandardItem, QPixmap, QIcon

import json
import os
import shutil
import os.path

TILE_ICON_HEIGHT = 20
TILE_ICON_WIDTH = TILE_ICON_HEIGHT

MAP_DATA_FILE = 'mapdata.json'
CLASSES_FILE = 'classes.json'
GAME_INFO_FILE = 'game_info.json'
ENEMIES_FILE = 'enemies.json'
ITEMS_FILE = 'items.json'
CONTAINERS_FILE = 'containers.json'

GAME_CREATOR_FILE = 'game_creator_data.json'

SCRIPTS_DIR = 'scripts'

SCRIPT_FILE_FORMAT = '.script'
TILESET_FILE = 'tileset.json'
ROOM_FILE = 'room.room'

CHARS = [str(i) for i in range(0, 10)] + [chr(i) for i in range(ord('a'), ord('z')+1)] + [chr(i) for i in range(ord('A'), ord('Z')+1)]
COLORS = []
for r in range(0, 3):
    for g in range(0, 3):
        for b in range(0, 3):
            COLORS += [[r * 127, g * 127, b * 127]]


DURABILITY_MIN = -1
DURABILITY_MAX = 99

BASE_VISIBLE_RANGE = 10

REQUIRED_FILES = [
    MAP_DATA_FILE,
    CLASSES_FILE,
    GAME_INFO_FILE,
    # ENEMIES_FILE,
    # ITEMS_FILE,
    # CONTAINERS_FILE
]

class CheckError:
    def __init__(self):
        self.ok = True
        self.problems: set[str] = set()

    def add_problem(self, p: str):
        self.problems.add(p)

def file_name(f: str):
    return os.path.splitext(os.path.basename(f))[0]

def create_dir(dir: str):
    try:
        os.mkdir(dir)
    except FileExistsError:
        return

COMMAND_MAP = {
    'iadd': 2,
    'set': -1,
    'tset': 3,
    'run': 1,
    'unset': 1,
    'unsetall': 0,
    'sadd': -1,
    'isub': 2,
    'sleep': 1,
    'warp': 1,
    'mb': -1,
    'log': -1,
    'clearenemycodes': 0,
    'additem': 1,
    'opencontainer': 1
}

def is_good_script(script: str) -> CheckError:
    result = CheckError()
    lines = script.split('\n')
    for line in lines:
        if line == '':
            continue
        words = line.split(' ')
        cname = words[0]
        if cname == 'if':
            # TO-DO
            continue
        if line.count('{') != 0:
            continue
        if line.count('}') != 0:
            continue
        if not cname in COMMAND_MAP:
            result.ok = False
            result.add_problem(f'Command {cname} doesn\'t exist')
            continue
        opn = COMMAND_MAP[cname]
        if opn != -1 and opn != len(words[1:]):
            result.ok = False
            result.add_problem(f'Command {cname} requires {opn} operands (provided: {words[1:]}, count: {len(words[1:])})')
    return result

class MapRoomData:
    def __init__(self):
        self.room_dir_path = ''
        self.tileset_path = ''
        self.load_script_path = ''
        self.visible_range = 0

    def to_json(self) -> dict:
        result = {}
        result['path'] = self.room_dir_path
        result['tileset'] = self.tileset_path
        if self.load_script_path != '':
            result['loadScript'] = self.load_script_path
        result['visibleRange'] = self.visible_range
        return result

class MapData:
    def __init__(self):
        self.index_room = ''
        self.start_y = -1
        self.start_x = -1
        self.rooms: dict = {}

    def load_rooms(self, rooms: dict):
        self.rooms = rooms

    def to_json(self) -> dict:
        result = {}
        result['index'] = self.index_room
        result['startY'] = self.start_y
        result['startX'] = self.start_x
        result['rooms'] = {}
        for name, room in self.rooms.items():
            result['rooms'][name] = room.to_json()
        return result

    def load(self, data: dict, dir: str):
        self.index_room = data['index']
        self.start_y = data['startY']
        self.start_x = data['startX']

class ClassesData:
    def __init__(self):
        self.classes = {'Warrior':{}}

    def to_json(self):
        result = []
        for name, c in enumerate(self.classes):
            ob = {}
            ob['name'] = name
            result += [ob]
        return result

    def load(self, data: dict):
        self.classes = data

class GameInfoData:
    def __init__(self):
        self.name: str = ''
        pass

    def to_json(self):
        return self.__dict__

    def load(self, data: dict):
        self.name = data['name']

class ScriptData:
    def __init__(self):
        self.name: str = ''
        self.text: str = ''

    def save(self, to: str):
        with open(os.path.join(to, self.name + SCRIPT_FILE_FORMAT), 'w') as f:
            f.write(self.text)

ITEM_TYPES = [
    'basic',
    'weapon',
    'ammo'
]

WEAPON_TYPES = [
    'melee',
    'ranged'
]

AMMO_TYPES = [
    'arrow',
    'bolt'
]

class WeaponData:
    def __init__(self) -> None:
        self.weapon_type = ''
        self.min_damage = 0
        self.max_damage = 0
        self.range = 0

    def to_json(self) -> dict:
        # TO-DO
        pass

class ItemData:
    def __init__(self):
        self.name = ''
        self.display_name = ''
        self.selected_type = 'basic'
        self.equip_data = {}
        self.weapon_data = {}
        self.ammo_type = ''

    def load(data: dict) -> 'ItemData':
        # TO-DO
        pass

    def to_json(self) -> dict:
        # TO-DO
        pass

class TileData:
    def __init__(self):
        self.name: str = ''
        self.display_name: str = ''
        self.warpcode: str = ''
        self.durability: int = -1
        self.passable: bool = True
        self.transparent: bool = True
        self.is_breakable: bool = False
        self.broken_alt: str = ''
        self.interact_script: str = ''
        self.step_script: str = ''
        self.rgb: list[int] = [0, 0, 0]

    def to_QStandardItem(self) -> QStandardItem:
        item = QStandardItem()
        item.setText(self.name)
        pm = QPixmap(TILE_ICON_WIDTH, TILE_ICON_HEIGHT)
        pm.fill(QColor(self.rgb[0], self.rgb[1], self.rgb[2]))
        icon = QIcon(pm)
        item.setIcon(icon)
        return item

    def load(data: dict) -> 'TileData':
        result = TileData()
        result.name = data['name']
        result.display_name = data['displayName']
        result.passable = data['passable']
        result.transparent = data['transparent']
        if 'warpcode' in data:
            result.warpcode = data['warpcode']
        # if 'unique' in data:
            # result.uni
        if 'smashable' in data:
            result.is_breakable = data['smashable']
            result.broken_alt = data['brokenalt']
            result.durability = data['durability']
        if 'interactScript' in data:
            result.interact_script = os.path.splitext(data['interactScript'])[0]
            result.interact_script = file_name(data['interactScript'])
        if 'stepScript' in data:
            result.step_script = file_name(data['stepScript'])
        return result

    def to_json(self) -> dict:
        result = {}
        result['name'] = self.name
        result['displayName'] = self.display_name
        result['passable'] = self.passable
        result['transparent'] = self.transparent
        if self.warpcode != '' :
            result['warpcode'] = self.warpcode
        if self.is_breakable:
            result['unique'] = True
            result['smashable'] = True
            result['brokenalt'] = self.broken_alt
            result['durability'] = self.durability
        if self.interact_script != '':
            result['interactScript'] = os.path.join(SCRIPTS_DIR, self.interact_script + SCRIPT_FILE_FORMAT)
        if self.step_script != '':
            result['stepScript'] = os.path.join(SCRIPTS_DIR, self.step_script + SCRIPT_FILE_FORMAT)
        return result

class RoomData:
    def __init__(self, game: 'GameObject', name: str):
        self.game = game
        self.name = name
        self.room = []
        self.scripts: list[ScriptData] = []
        self.tiles: list[TileData] = []
        self.load_script: str = ''

    def get_tile(self, name: str) -> tuple[TileData, str]:
        for i in range(len(self.tiles)):
            tile = self.tiles[i]
            if tile.name == name:
                return tile, CHARS[i]
        return None, None

    def create_room(self, room) -> CheckError:
        names = room.get_tile_names()
        self.room = []
        result = CheckError()
        for row in names:
            l = []
            for name in row:
                tile, ch = self.get_tile(name)
                if tile is None:
                    result.ok = False
                    result.add_problem(f'No tile with name: {name}')
                else:
                    l += [ch]
            self.room += [l]
        return result

    def extract_map_room_data(self) -> MapRoomData:
        result = MapRoomData()
        result.visible_range = BASE_VISIBLE_RANGE
        result.room_dir_path = os.path.join(self.name, ROOM_FILE)
        result.load_script_path = ''
        if self.load_script != '':
            result.load_script_path = os.path.join(self.name, SCRIPTS_DIR, self.load_script + SCRIPT_FILE_FORMAT)
        result.tileset_path = os.path.join(self.name, TILESET_FILE)
        return result

    def get_script_names(self) -> list[str]:
        return [script.name for script in self.scripts]

    def get_tile_names(self) -> list[str]:
        return [tile.name for tile in self.tiles]

    def has_script(self, name: str) -> bool:
        for s in self.scripts:
            if s.name == name:
                return True
        return False

    def has_tile(self, name: str) -> bool:
        for tile in self.tiles:
            if tile.name == name:
                return True
        return False            

    def has_warpcode(self, wc: str) -> bool:
        if wc == '':
            return False
        for tile in self.tiles:
            if tile.warpcode == wc:
                return True
        return False    

    def save(self, dir: str):
        room_path = os.path.join(dir, self.name)
        # create the room directory
        create_dir(room_path)
        # save scripts
        scripts_path = os.path.join(room_path, SCRIPTS_DIR)
        create_dir(scripts_path)
        for script in self.scripts:
            script.save(scripts_path)
        # save tileset
        tmap = {}
        ci = 0
        for tile in self.tiles:
            tmap[CHARS[ci]] = tile.to_json()
            ci += 1
        s = json.dumps(tmap, indent=4, sort_keys=True)
        with open(os.path.join(room_path, TILESET_FILE), 'w') as f:
            f.write(s)
        # save the room itself
        with open(os.path.join(room_path, ROOM_FILE), 'w') as f:
            s = []
            for row in self.room:
                s += [''.join(row)]
            f.write('\n'.join(s))

class GameObject:
    def __init__(self, name: str):
        # game info
        self.gi = GameInfoData()
        self.gi.name = name
        # map data
        self.md = MapData()
        # classes data
        self.cs = ClassesData()
        # rooms
        self.rooms: list[RoomData] = []
        # items
        self.items: list[ItemData] = []

    def load(path: str) -> 'GameObject':
        result = GameObject('')
        fsys = {}
        for (dirpath, dirnames, filenames) in os.walk(path):
            fsys[dirpath] = {
                'files': filenames,
                'directories': dirnames
            }
        rf = fsys[path]
        for f in REQUIRED_FILES:
            if not f in rf['files']:
                raise Exception(f'required file {f} not found in game directory')
        load_map = {
            GAME_INFO_FILE: result.gi,
            CLASSES_FILE: result.cs
        }
        for key, value in load_map.items():
            with open(os.path.join(path, key), 'r') as f:
                s = f.read()
                data = json.loads(s)
                value.load(data)
        # load the map data
        with open(os.path.join(path, MAP_DATA_FILE), 'r') as f:
            s = f.read()
            data = json.loads(s)
            result.load_rooms(data['rooms'], path)
            result.md.load(data, path)
        # load the scripts and tile colors
        for room in result.rooms:
            room.game = result
            p = os.path.join(path, room.name, SCRIPTS_DIR)
            script_paths = fsys[p]['files']
            for script_path in script_paths:
                script = ScriptData()
                script.name = os.path.splitext(script_path)[0]
                with open(os.path.join(p, script_path), 'r') as f:
                    script.text = f.read()
                room.scripts += [script]
            for i in range(len(room.tiles)):
                room.tiles[i].rgb = COLORS[i]
        if GAME_CREATOR_FILE in rf['files']:
            with open(os.path.join(path, GAME_CREATOR_FILE), 'r') as f:
                gcd = json.loads(f.read())
                for room in result.rooms:
                    if room.name in gcd:
                        tiles_data = gcd[room.name]
                        for tile in room.tiles:
                            if tile.name in tiles_data:
                                tile.rgb = tiles_data[tile.name]
        return result

    # methods 

    def load_rooms(self, data: dict, dir: str):
        self.rooms = []
        for room_name, room_data in data.items():
            room = RoomData(None, room_name)
            tileset = None
            room_layout = None
            with open(os.path.join(dir, room_data['tileset']), 'r') as f:
                tileset = json.loads(f.read())
                for tile in tileset.values():
                    room.tiles += [TileData.load(tile)]
            with open(os.path.join(dir, room_data['path']), 'r') as f:
                room_layout = f.read()
                lines = room_layout.split('\n')
                for line in lines:
                    for tile in line:
                        if not tile in tileset:
                            raise Exception(f'tile {tile} in room {room_name} not recognized in tileset')
            room.room = []
            split = room_layout.split('\n')
            for row in split:
                if row == '':
                    continue
                l = []
                for tchar in row:
                    l += [tileset[tchar]['name']]
                room.room += [l]
            if 'loadScript' in room_data:
                room.load_script = file_name(room_data['loadScript'])
            self.rooms += [room]

    def tidy_md(self):
        for room in self.rooms:
            mrd = room.extract_map_room_data()
            self.md.rooms[room.name] = mrd

    def save(self, dir: str):
        self.tidy_md()
        # save required files
        rfm = {
            MAP_DATA_FILE: self.md,
            GAME_INFO_FILE: self.gi,
            CLASSES_FILE: self.cs
        }
        path_to_game = os.path.join(dir, self.gi.name)
        shutil.rmtree(path_to_game, ignore_errors=True)
        create_dir(path_to_game)
        for path, ob in rfm.items():
            js = json.dumps(ob.to_json(), indent=4, sort_keys=True)
            with open(os.path.join(path_to_game, path), 'w') as f:
                f.write(js)
        # save room data
        for room in self.rooms:
            room.save(path_to_game)
        # save game creator data
        gcd = {}
        # gcd['tile_colors'] = {}
        for room in self.rooms:
            data = {}
            for tile in room.tiles:
                data[tile.name] = tile.rgb
            gcd[room.name] = data
        js = json.dumps(gcd, indent=4, sort_keys=True)
        with open(os.path.join(path_to_game, GAME_CREATOR_FILE), 'w') as f:
            f.write(js)
