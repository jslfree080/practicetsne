import os
import imageio

png_dir = 'src/main/kotlin/file/iris'
png_files = [f for f in os.listdir(png_dir) if f.endswith('.png')]
png_files = sorted(png_files)
images = [imageio.imread(os.path.join(png_dir,file)) for file in png_files]
imageio.mimsave('src/main/kotlin/file/iris/iris.gif', images, duration = 1)