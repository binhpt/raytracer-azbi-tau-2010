#include "Parallelogram.h"


Parallelogram::Parallelogram(void)
{
}


Parallelogram::~Parallelogram(void)
{
}

Vector3 Parallelogram::Intersection(ray r)
{
	Vector3 a;
	return a;
}

void Parallelogram::BoundingBox(bounding_box* b)
{
	b->p1.x = min(min(this->p0.x, this->p1.x), this->p2.x);
	b->p1.y = min(min(this->p0.y, this->p1.y), this->p2.y);
	b->p1.z = min(min(this->p0.z, this->p1.z), this->p2.z);

	b->p2.x = max(max(this->p0.x, this->p1.x), this->p2.x);
	b->p2.y = max(max(this->p0.y, this->p1.y), this->p2.y);
	b->p2.z = max(max(this->p0.z, this->p1.z), this->p2.z);	
}
