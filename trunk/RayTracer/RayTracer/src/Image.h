#ifndef _IMAGE_H
#define _IMAGE_H
#include <cairomm/surface.h>
#include "common.h"

class Image
{
protected:
  Cairo::RefPtr <Cairo::ImageSurface> im;
  unsigned char* imData;
  int stride, width, height;

public:
  Image (int width, int height);

  void putPixel (int screenX, int screenY, color *c);

  void Test ();

  Cairo::RefPtr <Cairo::ImageSurface> getCairoSurface();
};

#endif
