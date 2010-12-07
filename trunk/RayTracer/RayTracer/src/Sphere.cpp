#include "Sphere.h"
#include <cmath>

Sphere::Sphere(void)
{
}

Sphere::~Sphere(void)
{
}

bool Sphere::Intersection(const ray& r, intersection_data& intersect)
{
	Vector3 result;
	Vector3 L;
	float Tca, Thc, d, T;

	L = center - r.origin;
	Tca = InnerProduct(L, r.direction);
	if (Tca < 0) return false;

	d = InnerProduct(L, L) + Tca * Tca;
	if (d > radius * radius) return false;

	Thc = sqrt(radius * radius - d);
	/* assuming T won't be negative, which should never happen in cases that matter (either we're inside the sphere and so we have bigger problems, or it's behind us)*/
	T = std::min(Tca - Thc, Tca + Thc);

	intersect.point = (Vector3)r.direction * T;
	intersect.col = this->mtl_diffuse; //change to something more complex next submission
	intersect.normal = intersect.point - this->center;
	intersect.T = T;

	return true;
}

void Sphere::BoundingBox(bounding_box& b)
{
	b.p1.x = this->center.x - radius;
	b.p1.y = this->center.y - radius;
	b.p1.y = this->center.z - radius;

	b.p2.x = this->center.x + radius;
	b.p2.y = this->center.y + radius;
	b.p2.y = this->center.z + radius;
}
