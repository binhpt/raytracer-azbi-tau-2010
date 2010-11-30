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

Vector3 Vector3::operator+(const Vector3 &a)
{
	return Vector3(x + a.x, y + a.y, z + a.z);
}

Vector3 Vector3::operator-(const Vector3 &a)
{
	return Vector3(x - a.x, y - a.y, z - a.z);
}

float Vector3::operator*(const Vector3 &a)
{
	return (x * a.x + y * a.y + z * a.z);
}

Vector3 Vector3::operator*(const float &t)
{
	return Vector3(t * x, t * y, t * z);
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