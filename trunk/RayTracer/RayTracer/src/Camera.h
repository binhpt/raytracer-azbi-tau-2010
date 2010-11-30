#ifndef CAMERA_H
#define CAMERA_H

#include <vector>
#include "common.h"

using namespace std;

class Camera
{
public:
	Camera(void);
	~Camera(void);

	Vector3 eye;
	Vector3 direction;
	Vector3 look_at;
	Vector3 up_direction;
	float screen_dist;
	float screen_width;
};


#endif