#include "common.h"

Vector3::Vector3()
{
	this->x = 0;
	this->y = 0;
	this->z = 0;
}

Vector3::Vector3(float x, float y, float z)
{
	this->x = x;
	this->y = y;
	this->z = z;
}

float& Vector3::operator[](const int &i)
{
	switch (i)
	{
		case 0:
			return x;
		case 1:
			return y;
		case 2:
			return z;
	}
}

Vector3 CrossProduct(const Vector3& a, const Vector3& b)
{
	Vector3 result;

	result.x = a.y * b.z - a.z * b.y;
	result.y = a.z * b.x - a.x * b.z;
	result.z = a.x * b.y - a.y * b.x;

	return result;
}

float InnerProduct(const Vector3& a, const Vector3& b)
{
	return a.x * b.x + a.y * b.y + a.z * b.z;
}