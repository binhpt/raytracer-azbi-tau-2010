#ifndef COMMON_H
#define COMMON_H



class Vector3 {
public:
	float x;
	float y;
	float z;

	Vector3();
	Vector3(float x, float y, float z);

	inline Vector3& operator+=(const Vector3 &a){
		this->x += a.x;
		this->y += a.y;
		this->z += a.z;

		return *this;
	}

	inline Vector3 operator+(const Vector3 &a) { return Vector3(*this) += a; }

	inline Vector3& operator-=(const Vector3 &a){
		this->x -= a.x;
		this->y -= a.y;
		this->z -= a.z;

		return *this;
	}

	inline Vector3 operator-(const Vector3 &a){ return Vector3(*this) += a;	}
	
	inline Vector3& operator*=(const float &t){
		this->x *= t;
		this->y *= t;
		this->z *= t;

		return *this;
	}

	/* note that you can only write vec * float and not float * vec */
	inline Vector3 operator*(const float &t) { return Vector3(t * x, t * y, t * z); }

	float& operator[](const int &i);
};

inline Vector3 CrossProduct(const Vector3& a, const Vector3& b)
{
	Vector3 result;

	result.x = a.y * b.z - a.z * b.y;
	result.y = a.z * b.x - a.x * b.z;
	result.z = a.x * b.y - a.y * b.x;

	return result;
}

inline float InnerProduct(const Vector3& a, const Vector3& b) {	return a.x * b.x + a.y * b.y + a.z * b.z; }

inline Vector3 Normalize(const Vector3& a) { 
	Vector3 v(a);
	v *= (1 / InnerProduct(a, a));
	return v; 
}

struct color {
	float r;
	float g;
	float b;
	float a;
};

struct ray {
	Vector3 origin;
	Vector3 direction;
};

struct bounding_box {
	Vector3 p1;
	Vector3 p2;
};

struct intersection_data {
	Vector3 point;
	Vector3 normal;
	color color;
	float T;
};

#endif