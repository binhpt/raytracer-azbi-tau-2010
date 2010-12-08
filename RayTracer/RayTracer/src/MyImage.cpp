#include <iostream>
#include <cairomm/surface.h>
#include "MyImage.h"
#include "common.h"

  MyImage::MyImage (int width, int height)
  {
    this->width = width;
    this->height = height;
    this->im = Cairo::ImageSurface::create (Cairo::FORMAT_ARGB32, width, height);
    this->imData = im->get_data ();
    this->stride = im->get_stride();
  }

  void MyImage::putPixel (int screenX, int screenY, color *c)
  {
    std::cout << "Progress\n";
    unsigned char* px = &(imData [((screenY*width+stride) + screenX)*4]);
    px[0] = int(c->a * 255);
    px[1] = int(c->r * 255);
    px[2] = int(c->g * 255);
    px[3] = int(c->b * 255);
  }

  void MyImage::Test ()
  {
    color c;

    c.a = 1;
    for (int i = 0; i < height; i++)
    {
      c.r = c.g = c.b = float(i) / height;
      for (int j = 0; i < width; j++)
        putPixel (j, i, &c);
    }
  }

  Cairo::RefPtr <Cairo::ImageSurface> MyImage::getCairoSurface()
  {
    return im;
  }

