import os
import sys
from PIL import Image

def check(d):
    return d - int(d) == 0

def do():
    # if len(sys.argv) != 3:
    #     print('Incorrect amount of arguments')
    #     return
    # fname = sys.argv[1]
    # outdir = sys.argv[2]
    fname = 'tilesets/Wooden Pixel Art GUI 32x32.png'
    outdir = 'GUI'
    im = Image.open(fname)
    imgwidth = im.size[0]
    imgheight = im.size[1]
    count_y = imgheight / 32
    count_x = imgwidth / 32
    # if not check(count_y):
    #     print(f'can\'t split image: image height not divisible by 32 ({imgheight})')
    #     return
    # if not check(count_x):
    #     print(f'can\'t split image: image width not divisible by 32 ({imgwidth})')
    #     return
    count_y = int(count_y)
    count_x = int(count_x)
    counter = 0
    for i in range(count_y):
        for j in range(count_x):
            x = j * 32
            y = i * 32
            image = im.crop((x,y,x+32,y+32))
            p = os.path.join(outdir, f'{os.path.splitext(os.path.split(fname)[1])[0]}{counter}.png')
            image.save(p)
            print(f'{p} saved!')
            counter += 1


if __name__ == '__main__':
    do()