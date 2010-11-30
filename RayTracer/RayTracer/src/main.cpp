#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>

#include "common.h"
#include "Scene.h"
#include "Surface.h"
#include "Camera.h"
#include "Sphere.h"

using namespace std;

/* containers for all the data */
vector<Surface*>* surfaces;
Scene* scene;
Camera* camera;

/*
 * gets "param" from a string of the form ...=<param>. useful for float, bool, int, double
 */
template<class T>
T GetSingleParam(const string& line) { 
	return fromString<T>(line.substr(line.find('='), line.length())); 
}

/* 
 * gets a float/int/double from a string of the form "<number>"
 */

template<class T>
T fromString(const string& s) 
{
	istringstream stream(s);
	T t;
	stream >> t;
	return t;
}

color GetColorParam(const string &line)
{
	string buf;
	int found = 0;
	color c;
	
	found = line.find('=');
	buf = line.substr(found + 1, line.find(' ', found + 1) - found - 1);
	c.r = fromString<float>(buf);

	found = line.find(' ', found + 1);
	buf = line.substr(found + 1, line.find(' ', found + 1) - found - 1);
	c.g = fromString<float>(buf);

	found = line.find(' ', found + 1);
	buf = line.substr(found + 1, line.find(' ', found + 1) - found - 1);
	c.b = fromString<float>(buf);

	found = line.find(' ', found + 1);
	if (found == -1)
		c.a = 1;
	else
	{
		buf = line.substr(found + 1, line.find(' ', found + 1) - found - 1);
		c.a = fromString<float>(buf);
	}

	return c;
}

Vector3 GetVectorParam(const string &line)
{
	string buf;
	int found = 0;
	Vector3 v;
	
	found = line.find('=');
	buf = line.substr(found + 1, line.find(' ', found + 1) - found - 1);
	v.x = fromString<float>(buf);

	found = line.find(' ', found + 1);
	buf = line.substr(found + 1, line.find(' ', found + 1) - found - 1);
	v.y = fromString<float>(buf);

	found = line.find(' ', found + 1);
	buf = line.substr(found + 1, line.find(' ', found + 1) - found - 1);
	v.z = fromString<float>(buf);

	return v;
}

/*
	would be a perfect place for reflection in java or C#
	sadly, no such thing in C++, so this whole thing becomes ugly and long

	it should also be noted that you are horrible people for allowing there to be spaces or not be before and after the =
*/
void ReadInput(vector<Surface*>* surfaces, Scene* scene, Camera* camera, string config_path)
{
	string line;
	string param;

	ifstream f(config_path);
	
	while (!f.eof())
	{
		getline(f, line);

		if (line == "scene:")
		{
			while (line != "")
			{
				getline(f, line);
				param = line.substr(0, line.find('='));

				if (param == "background-col" || param == "background-col ")
					scene->background_color = GetColorParam(line);

				else if (param == "background-tex" || param == "background-tex ")
					scene->background_tex = new string(line.substr(line.find('=') + 1, line.length()));

				else if (param == "ambient-light" || param == "ambient-light ")
					scene->ambient_light = GetColorParam(line);
				
				else if (param == "super-samp-width" || param == "super-samp-width ")
					scene->super_sample_width = GetSingleParam<int>(line);

				else if (param == "use-acceleration" || param == "use-acceleration ")
					scene->use_acceleration = GetSingleParam<int>(line);

				else if (param == "mc-path-trace" || param == "mc-path-trace ")
					scene->mc_path_trace = GetSingleParam<bool>(line);

				else if (param == "mc-path-trace-rec" || param == "mc-path-trace-rec ")
					scene->mc_path_trace_rec = GetSingleParam<int>(line);

				else if (param == "mc-path-trace-num" || param == "mc-path-trace-num ")
					scene->mc_path_trace_num = GetSingleParam<int>(line);
			}
			continue;
		}

		if (line == "camera:")
		{
			bool lookat = false;

			while (line != "")
			{
				getline(f, line);
				param = line.substr(0, line.find('='));

				if (param == "eye" || param == "eye ")
					camera->eye = GetVectorParam(line);

				else if (param == "direction" || param == "direction ")
					camera->direction = GetVectorParam(line);
				
				else if (param == "look-at" || param == "look-at ")
				{
					camera->look_at = GetVectorParam(line);
					lookat = true;
				}

				else if (param == "up-direction" || param == "up-direction ")
					camera->up_direction = GetVectorParam(line);

				else if (param == "screen-dist" || param == "screen-dist ")
					camera->screen_dist = GetSingleParam<float>(line);

				else if (param == "screen-width" || param == "screen-width ")
					camera->screen_width = GetSingleParam<float>(line);
			}

			if (lookat)
				camera->direction = camera->look_at - camera->eye;
		}

		if (line == "sphere:")
		{
			Sphere* sphere = new Sphere();

			while (line != "")
			{
				getline(f, line);
				param = line.substr(0, line.find('='));

				if (param == "center" || param == "center ")
					sphere->center = GetVectorParam(line);

				else if (param == "radius" || param == "radius ")
					sphere->radius = GetSingleParam<float>(line);

				else if (param == "mtl-diffuse" || param == "mtl-diffuse ")
					sphere->mtl_diffuse = GetColorParam(line);
			}

			surfaces->push_back(sphere);
		}
	}
}

color ShootRay(ray r)
{
	//500 - max distance. maybe make macro, maybe get from scene - need find out
	float closest_collision = 500;
	Vector3* v_closest_collision;
	Surface* s_closest_collision;
	for (vector<Surface*>::iterator i = surfaces->begin(); i != surfaces->end(); ++i)
	{
		//(*i)->Intersection(r);
		//check if it's the closest so far
		//remember the surface and position
	}

	//we now have the closest surface and point on the surface
	//obviously enough to get the color
	//surface and point are saved for later implementations of bouncing
	//we'll also need the normal later on, either on collision or we'll get it on its own function using the intersection vector
	//for this excersize they want plain color, angles don't matter
	color c = s_closest_collision->mtl_diffuse;
	return c;
}

ray CreateRay(int x, int y)
{
	//double angle = camera->screen_width
	ray a;
	return a;
}

int main(int args, const char *argc[])
{
	scene = new Scene();
	surfaces = new vector<Surface*>();
	camera = new Camera();
	
	if (args < 2) cout << "must be given config file" << endl;

	string config_path (argc[1]);
	ReadInput(surfaces, scene, camera, config_path);

	/****ROUGH DRAFT of how it should go:***/


	//iterate every pixel of the screen
	//multithreading should go here
	int height = 50, width = 50;
	for (int i = 0; i < height; i++)
		for (int j = 0; j < width; j++)
		{
			//make ray using some kind of inverse of world/view/perspective
			//UI related- image[i, j] = ShootRay(ray);
			ray r;
			ShootRay(r);
			//1 ray for now, anti aliasing later
		}

	delete surfaces;
	delete scene;
	delete camera;
}