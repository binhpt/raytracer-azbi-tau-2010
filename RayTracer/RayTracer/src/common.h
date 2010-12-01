#ifndef COMMON_H
#define COMMON_H



class Vector3 {
public:
	float x;
	float y;
	float z;

	Vector3();
	Vector3(float x, float y, float z);

	Vector3& operator+=(const Vector3 &a){
		this->x += a.x;
		this->y += a.y;
		this->z += a.z;

		return *this;
	}

	/*** returns an instance! remember to release ***/
	Vector3 operator+(const Vector3 &a) { return Vector3(*this) += a; }

	inline Vector3& operator-=(const Vector3 &a){
		this->x -= a.x;
		this->y -= a.y;
		this->z -= a.z;

		return *this;
	}

	/*** returns an instance! remember to release ***/
	inline Vector3 operator-(const Vector3 &a){ return Vector3(*this) += a;	}
	
	Vector3& operator*=(const float &t){
		this->x *= t;
		this->y *= t;
		this->z *= t;

		return *this;
	}
	/* Annoying that you can only write vec * float and not float * vec, but oh well */
	Vector3 operator*(const float &t) { return Vector3(t * x, t * y, t * z); }

	float& operator[](const int &i);

	/* WARNING: inner product attached to * for ease of use and code readability. But with great power.. */
	//float operator*(const Vector3 &a);

	//friend Vector3 CrossProduct(const Vector3& a, const Vector3& b);
};

Vector3 CrossProduct(const Vector3& a, const Vector3& b);
float InnerProduct(const Vector3& a, const Vector3& b);

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

struct intersection_data {
	Vector3 point;
	Vector3 normal;
	//Surface surface;
};

struct bounding_box {
	Vector3 p1;
	Vector3 p2;
};

#endif