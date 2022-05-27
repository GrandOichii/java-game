from PIL import Image

im = Image.open('mage.png').convert('RGB')
# gray = im.convert('L')
width = im.size[0]
height = im.size[1]
for i in range(height):
    for j in range(width):
        # gr = gray.getpixel((j, i))
        # im.putpixel((j, i), (0, gr, 0))
        p = im.getpixel((j, i))
        print(p, end=' ')
    print()

im.save('base1.png')