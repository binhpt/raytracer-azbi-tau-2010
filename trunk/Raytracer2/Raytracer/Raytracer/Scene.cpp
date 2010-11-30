#include "Scene.h"


Scene::Scene(void)
{
	background_tex = NULL;
}


Scene::~Scene(void)
{
	if (this->background_tex != NULL)
		delete this->background_tex;
}
