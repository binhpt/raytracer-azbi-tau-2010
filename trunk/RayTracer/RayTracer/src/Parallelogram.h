#ifndef PARALLELOGRAM_H
#define PARALLELOGRAM_H


#include "Surface.h"
#include "common.h"

class Parallelogram :
	public Surface
{
public:
	Parallelogram(void);
	~Parallelogram(void);

	Vector3 Intersection(ray r);
	void BoundingBox(bounding_box* b);

	Vector3 p0;
	Vector3 p1;
	Vector3 p2;
};

#endif
