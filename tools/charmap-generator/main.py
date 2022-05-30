import json
import sys
import os

CHARMAP = {
    'wall': '#',
    'sign': '!',
    'chest': '${yellow}C',
    'door': '+',
    'button': 'o',
    'floor': '.',
    'portal': '${blue}O',
    'man': '${orange}@'
}

MAPDATA_FILE = 'mapdata.json'

def create_result():
    result = {}
    result['templates'] = CHARMAP
    result['tiles'] = {}
    return result

def do(in_path, out_path):
    if not os.path.exists(in_path):
        print(f'Dir {in_path} does not exist')
        return
    mdf = os.path.join(in_path, MAPDATA_FILE)
    if not os.path.exists(mdf):
        print(f'Failed to locate map data file in {in_path}')
        return
    data = json.loads(open(mdf, 'r').read())['rooms']
    result = create_result()
    for room_name, tile_values in data.items():
        tileset_path = os.path.join(in_path, tile_values['tileset'])
        if not os.path.exists(tileset_path):
            print('File {} not found'.format(tileset_path))
            return
        tileset = json.loads(open(tileset_path, 'r').read())
        for tile_values in tileset.values():
            tile_name = room_name + ':' + tile_values['name']
            for key, value in CHARMAP.items():
                if key in tile_name:
                    result['tiles'][tile_name] = key
                    break
    with open(out_path, 'w') as f:
        f.write(json.dumps(result, indent=4))

def main():
    if len(sys.argv) != 3:
        print('Incorrect number of arguments')
        return
    do(sys.argv[1], sys.argv[2])

if __name__  == '__main__':
    main()