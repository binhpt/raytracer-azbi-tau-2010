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
#include "Image.h"

using namespace std;

/* containers for all the data */
vector<Surface*>* surfaces;
Scene* scene;
Camera* camera;

/******
 *SET TO 0 IF YOU LIKE MEMORY
 I'll try and remember to set it to 0 every time before committing, but just incase
 visual studio won't agree to release stream memory for some reason because of gtk. so this prevents deleting of streams (causing quite a memory leak)
 also, remove this whole nonesense before submitting
*******/
#define HORRIBLE_MEMLEAK 0


/* 
 * gets a float/int/double from a string of the form "<number>"
 */

template<class T>
T fromString(const string& s) 
{
	
	istringstream* stream = new istringstream(s);
	T t;
	(*stream) >> t;
	
#if !HORRIBLE_MEMLEAK
	delete stream;
#endif

	return t;
}

/*
 * gets "param" from a string of the form ...=<param>. useful for float, bool, int, double
 */
template<class T>
T GetSingleParam(const string& line) { 
	int find = line.find('=');
	return fromString<T>(line.substr(line.find(' ', find) + 1, line.length())); 
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

	//take care of the " = " case
	if (line[found + 1] == ' ')
		found++;

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
void SetScene(vector<Surface*>* surfaces, Scene* scene, Camera* camera, string config)
{
	string line;
	string param;

	istringstream* iss = new istringstream(config, stringstream::in);

	
	while (!iss->eof())
	{
		//iss >> line;
		getline(*iss, line);

		if (line == "scene:")
		{
			while (line != "")
			{
				//iss >> line;
				getline(*iss, line);
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
				getline(*iss, line);
				param = line.substr(0, line.find('='));

				if (param == "eye" || param == "eye ")
					camera->eye = GetVectorParam(line);

				else if (param == "direction" || param == "direction ")
				{
					camera->direction = GetVectorParam(line);
					camera->direction = Normalize(camera->direction);
				}
				
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
			{
				camera->direction = camera->look_at - camera->eye;
				camera->direction = Normalize(camera->direction);
			}
		}

		if (line == "sphere:")
		{
			Sphere* sphere = new Sphere();

			while (line != "")
			{
				getline(*iss, line);
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

#if !HORRIBLE_MEMLEAK
	delete iss;
#endif
}

color ShootRay(ray r)
{
	color c;
	intersection_data closest_intersect, temp;
	closest_intersect.T = -1;//500 - max distance. maybe make macro, maybe get from scene

	for (vector<Surface*>::iterator i = surfaces->begin(); i != surfaces->end(); ++i)
		//if there is a collision, and its T is smaller, this is the new closest collision
		if ((*i)->Intersection(r, temp) && (closest_intersect.T == -1 || temp.T < closest_intersect.T))
			closest_intersect = temp;

	//we now have the closest surface and point on the surface
	//obviously enough to get the color
	//surface and point are saved for later implementations of bouncing
	//we'll also need the normal later on, either on collision or we'll get it on its own function using the intersection vector
	//for this excersize they want plain color, angles don't matter
	c = closest_intersect.col;
	return c;
}

ray CreateRay(float xratio, float yratio)
{
	ray r;
	r.origin = camera->eye;
	Vector3 p;
	p = camera->P1 + camera->right_direction * ((xratio + 0.5) * camera->screen_width)
		+ camera->up_direction * ((yratio + 0.5) * camera->screen_width);

	r.direction = Normalize(p - camera->eye); //if we don't need to normalize, remove the normalize
	return r;
}

/* void putColor (int screenX, int screenY, color c); */
/* config = path! for now */
Image* render (int screenWidth, int screenHeight, string config)
{
	color pixel;
	Image *image = new Image (screenWidth, screenHeight);
	if (scene != NULL) delete scene;
	if (surfaces != NULL) delete surfaces;
	if (camera != NULL) delete camera;

	scene = new Scene();
	surfaces = new vector<Surface*>();
	camera = new Camera();
	ray r;

	SetScene(surfaces, scene, camera, config);

	/****ROUGH DRAFT of how it should go:***/
	//additional camera initializations
	camera->right_direction = CrossProduct(camera->up_direction, camera->direction);
	camera->up_direction = CrossProduct(camera->right_direction, camera->direction);
	camera->P1 = (camera->direction * camera->screen_dist) - (camera->right_direction * (camera->screen_width / 2)) - (camera->up_direction * (camera->screen_width / 2));

	camera->screen_height = camera->screen_width * (screenWidth / screenHeight);

	//iterate every pixel of the display screen
	//multithreading should go here
	for (int i = 0; i < screenHeight; i++)
		for (int j = 0; j < screenWidth; j++)
		{
			r = CreateRay(i / screenHeight, j / screenWidth);
			//UI related- image[i, j] = ShootRay(ray);
			pixel = ShootRay(r);
			image->putPixel (j,i,&pixel);
			//1 ray for now, anti aliasing later
		}

	delete surfaces;
	delete scene;
	delete camera;
	return image;
}
