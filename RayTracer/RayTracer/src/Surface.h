#ifndef _SURFACE_H_
#define _SURFACE_H_

#include "common.h"
#include <string>

class Surface
{
public:
	Surface() {};
	~Surface() {};

	virtual bool Intersection(const ray& r, intersection_data& intersect) = 0;
	virtual void BoundingBox(bounding_box& b) = 0;

	std::string mtl_type;
	color mtl_diffuse;
	color mtl_specular;
	color mtl_ambient;
	int mtl_shininess;
	int checkers_size;
	color checkers_diffuse1;
	color checkers_diffuse2;
	std::string texture;
	int reflectence;
};

#endif