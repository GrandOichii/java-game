import sys
from PIL import Image

def do():
    if len(sys.argv) != 4:
        print('Incorrect amount of arguments')
        return
    resultfile = sys.argv[3]

    fg = Image.open(sys.argv[1]).convert('RGBA')
    bg = Image.open(sys.argv[2]).convert('RGBA')
    bg.paste(fg, (0, 0), fg)
    bg.save(resultfile)

if __name__ == '__main__':
    do()