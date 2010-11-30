#ifndef SCENE_H
#define SCENE_H

#include <vector>
#include <string>

#include "common.h";

using namespace std;

class Scene
{
private:
public:
	Scene(void);
	~Scene(void);

	color background_color;
	string* background_tex;
	color ambient_light;
	int super_sample_width;
	bool use_acceleration;
	bool mc_path_trace;
	bool mc_path_trace_rec;
	bool mc_path_trace_num;
};

#endif