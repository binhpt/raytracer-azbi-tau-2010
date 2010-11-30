#ifndef SURFACE_H
#define SURFACE_H

#include "common.h"
#include <string>

using namespace std;

class Surface
{
public:
	Surface() {};
	~Surface() {};

	virtual bool Intersection(ray r, intersection_data* intersect) = 0;
	virtual void BoundingBox(bounding_box* b) = 0;

	string mtl_type;
	color mtl_diffuse;
	color mtl_specular;
	color mtl_ambient;
	int mtl_shininess;
	int checkers_size;
	color checkers_diffuse1;
	color checkers_diffuse2;
	string texture;
	int reflectence;
};

#endif