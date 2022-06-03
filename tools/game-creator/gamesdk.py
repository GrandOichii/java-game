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
    ITEMS_FILE,
    CONTAINERS_FILE,
    # ENEMIES_FILE,
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
    'opencontainer': -1,
    'give': 1,
    'take': 1
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
            # TODO
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

ATTRIBUTES = ['VIT', 'STR', 'AGI', 'INT', 'WIS', 'LUC']

class ClassData:
    def __init__(self):
        self.name: str = ''
        self.description: str = ''
        for key in ATTRIBUTES:
            self.__dict__[key] = 0
        self.items: list[ContainerItem] = []

    def to_json(self):
        result = {}
        for key in ['name', 'description']:
            result[key] = self.__dict__[key]
        result['attributes'] = {}
        for key in ATTRIBUTES:
            result['attributes'][key] = self.__dict__[key]
        result['items'] = {}
        for item in self.items:
            result['items'][item.name] = item.amount
        return result

    def load(data: dict) -> 'ClassData':
        result = ClassData()
        for key in ['name', 'description']:
            result.__dict__[key] = data[key]
        for key in ATTRIBUTES:
            result.__dict__[key] = data['attributes'][key]
        for name, amount in data['items'].items():
            item = ContainerItem()
            item.name = name
            item.amount = amount
            result.items += [item]
        return result

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

MAX_DAMAGE = 100
MAX_RANGE = 20

MAX_ATTRIBUTE = 50

ITEM_TYPES = [
    'basic',
    'melee weapon',
    'ranged weapon',
    'ammo',
    'armor',
    'incantation book'
    # ADD ITEM TYPE HERE
]

SLOTS = [
    'ARM',
    'ARMS',
    'FINGER',
    'HEAD',
    'TORSO',
    'LEGS'
]

AMMO_TYPES = [
    'arrow',
    'bolt'
]

DAMAGE_TYPES = [
    'PHYSICAL'
]

BASE_KEYS = ['name', 'displayName', 'description']
INCANTATIONS = {
    'targets': ['SELF', 'OTHER'],
    'types': ['HEAL', 'FIRE'],
    'intensities': ['LOW', 'MEDIUM', 'HIGH'],
}
# item_templates = json.loads(open('template-items.json', 'r').read())
ITEM_TYPE_KEYS = {
    # 'basic': list(item_templates['basic'][0].keys()),
    # 'ammo': list(item_templates['ammo'][0].keys()),
    # 'melee weapon': list(item_templates['weapons']['melee'][0].keys()),
    # 'ranged weapon': list(item_templates['weapons']['ranged'][0].keys())
    'basic': BASE_KEYS,
    'ammo': BASE_KEYS + ['ammoType', 'damageType', 'damage'],
    'melee weapon': BASE_KEYS + ['damageType', 'minDamage', 'maxDamage', 'range', 'STR', 'AGI', 'INT', 'slot'],
    'ranged weapon': BASE_KEYS + ['minDamage', 'maxDamage', 'range', 'STR', 'AGI', 'INT', 'slot', 'ammoType'],
    'armor': BASE_KEYS + ['armorRating', 'STR', 'AGI', 'INT', 'slot'],
    'incantation book': BASE_KEYS + ['intRequirement'] + list(INCANTATIONS.keys())
    # ADD ITEM TYPE HERE
}

ITEM_KEYS = set()
for l in ITEM_TYPE_KEYS.values():
    ITEM_KEYS.update(l)

REQUIREMENT_KEYS = [
    'STR',
    'AGI',
    'INT'
]

NUMBER_KEYS = [
    'minDamage',
    'maxDamage',
    'damage',
    'range',
    'intRequirement'
    # ADD ITEM TYPES HERE
]

MAX_ARMOR_RATING = 20

class ItemData:
    def __init__(self):
        self.selected_type: str = ''

        for key in ITEM_KEYS:
            self.__dict__[key] = ''
            if key in NUMBER_KEYS or key in ATTRIBUTES:
                self.__dict__[key] = 0
        _=1
        
    def load(itype: str, data: dict) -> 'ItemData':
        result = ItemData()
        keys = ITEM_TYPE_KEYS[itype]
        for key in keys:
            if key in INCANTATIONS:
                result.__dict__[key] = ' '.join(data[key])
                continue
            if key in REQUIREMENT_KEYS:
                if not key in data['requirements']:
                    continue
                result.__dict__[key] = data['requirements'][key]
                continue
            result.__dict__[key] = data[key]
        result.selected_type = itype
        return result

    def to_json(self) -> dict:
        result = {}
        keys = ITEM_TYPE_KEYS[self.selected_type]
        for key in keys:
            if key in INCANTATIONS:
                result[key] = self.__dict__[key].split(' ')
                continue
            if key in REQUIREMENT_KEYS:
                if not 'requirements' in result:
                    result['requirements'] = {}
                result['requirements'][key] = int(self.__dict__[key])
                continue
            result[key] = self.__dict__[key]
            if key in NUMBER_KEYS:
                result[key] = int(self.__dict__[key])
        return result

CONTAINER_MAX_ITEM_AMOUNT = 100

class ContainerItem:
    def __init__(self):
        self.name: str = ''
        self.amount: int = 0

class ContainerData:
    def __init__(self):
        self.key: str = ''
        self.items: list[ContainerItem] = []

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
        s = json.dumps(tmap, indent=4)
        with open(os.path.join(room_path, TILESET_FILE), 'w') as f:
            f.write(s)
        # save the room itself
        with open(os.path.join(room_path, ROOM_FILE), 'w') as f:
            s = []
            for row in self.room:
                s += [''.join(row)]
            f.write('\n'.join(s))

def extract_items(t: str, l: list):
    result = []
    for d in l:
        result += [ItemData.load(t, d)]
    return result

class GameObject:
    def __init__(self, name: str):
        # game info
        self.gi = GameInfoData()
        self.gi.name = name
        # map data
        self.md = MapData()
        # classes
        self.classes: list[ClassData] = []
        # rooms
        self.rooms: list[RoomData] = []
        # items
        self.items: list[ItemData] = []
        # conteiners
        self.containers: list[ContainerData] = []

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
        # load the items
        with open(os.path.join(path, ITEMS_FILE), 'r') as f:
            s = f.read()
            data = json.loads(s)
            result.items += extract_items('basic', data['basic'])
            result.items += extract_items('armor', data['armor'])
            result.items += extract_items('ammo', data['ammo'])
            result.items += extract_items('incantation book', data['incantationBooks'])
            result.items += extract_items('ranged weapon', data['weapons']['ranged'])
            result.items += extract_items('melee weapon', data['weapons']['melee'])
            # ADD ITEM TYPES HERE
        # load the containers
        with open(os.path.join(path, CONTAINERS_FILE), 'r') as f:
            s = f.read()
            data = json.loads(s)
            for key, items in data.items():
                container = ContainerData()
                container.key = key
                container.items = []
                for item_name, amount in items.items():
                    item = ContainerItem()
                    item.name = item_name
                    item.amount = amount
                    container.items += [item]
                result.containers += [container]
        # load the classes
        with open(os.path.join(path, CLASSES_FILE), 'r') as f:
            data = json.loads(f.read())
            result.classes = []
            for c in data:
                result.classes += [ClassData.load(c)]
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
        }
        path_to_game = os.path.join(dir, self.gi.name)
        shutil.rmtree(path_to_game, ignore_errors=True)
        create_dir(path_to_game)
        for path, ob in rfm.items():
            js = json.dumps(ob.to_json(), indent=4)
            with open(os.path.join(path_to_game, path), 'w') as f:
                f.write(js)
        # save room data
        for room in self.rooms:
            room.save(path_to_game)
        # save items
        with open(os.path.join(path_to_game, ITEMS_FILE), 'w') as f:
            # ADD ITEM TYPE HERE
            data = {}
            data['incantationBooks'] = []
            data['basic'] = []
            data['ammo'] = []
            data['armor'] = []
            data['weapons'] = {'ranged': [], 'melee': []}
            m = {
                'basic': data['basic'],
                'ammo': data['ammo'],
                'melee weapon': data['weapons']['melee'],
                'ranged weapon': data['weapons']['ranged'],
                'armor': data['armor'],
                'incantation book': data['incantationBooks']
            }
            # ADD ITEM TYPES HERE
            for item in self.items:
                m[item.selected_type] += [item.to_json()]
            f.write(json.dumps(data, indent=4))
        # save containers
        with open(os.path.join(path_to_game, CONTAINERS_FILE), 'w') as f:
            data = {}
            for container in self.containers:
                _is = {}
                for item in container.items:
                    _is[item.name] = item.amount
                data[container.key] = _is
            f.write(json.dumps(data, indent=4))
        # save classes
        with open(os.path.join(path_to_game, CLASSES_FILE), 'w') as f:
            data = []
            for c in self.classes:
                data += [c.to_json()]
            f.write(json.dumps(data, indent=4))
        # save game creator data
        gcd = {}
        # gcd['tile_colors'] = {}
        for room in self.rooms:
            data = {}
            for tile in room.tiles:
                data[tile.name] = tile.rgb
            gcd[room.name] = data
        js = json.dumps(gcd, indent=4)
        with open(os.path.join(path_to_game, GAME_CREATOR_FILE), 'w') as f:
            f.write(js)

    def count_items_with_name(self, name: str):
        result = 0
        for item in self.items:
            if item.name == name:
                result += 1
        return result

    def count_containers_with_key(self, key: str):
        result = 0
        for container in self.containers:
            if container.key == key:
                result += 1
        return result