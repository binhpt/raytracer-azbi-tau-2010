#ifndef SPHERE_H
#define SPHERE_H

#include "surface.h"
#include "common.h"
class Sphere :
	public Surface
{
public:
	Sphere(void);
	~Sphere(void);

	bool Intersection(ray r, intersection_data* intersect);
	void BoundingBox(bounding_box* b);

	Vector3 center;
	float radius;
};

#endif