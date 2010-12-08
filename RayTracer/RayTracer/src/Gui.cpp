#include <string>
#include <iostream>
#include <fstream>
#include <gtkmm.h>
#include <cairo.h>
#include <cairomm/surface.h>

#include "common.h"
#include "MyImage.h"
//#include "Window.h"

//see main.cpp
#define HORRIBLE_MEMLEAK 0

using namespace std;
using namespace Glib;
using namespace Gtk;

extern MyImage* render (int screenWidth, int screenHeight, std::string config);

class UI
{
protected:
  Cairo::RefPtr <Cairo::ImageSurface> im;
  Window* RenderWindow;
  TextView* TextArea;
  FileChooserButton* FileChooseButton;
  Layout* ImageView;
  ToolButton* RenderButton;
  ScrolledWindow* Scroll;

  int imgWidth;
  int imgHeight;

  void LoadText ()
  {
	char c;
	string buf;
    // You can treat this file name as a regular c++ string
	string str = FileChooseButton->get_filename();
	
	ifstream* myfile = new ifstream(str.c_str(), ifstream::in);

	if (myfile->is_open())
	{
		while (myfile->good())
		{
			myfile->get(c);
			buf.push_back(c);
		}
		myfile->close();
	}
	else
	{
		//BARAK - file wouldn't open, print some kind of error message
		cout << "could not open file";
	}

    // This code will put the text in the area
	TextArea->get_buffer()->set_text(buf);
#if !HORRIBLE_MEMLEAK
	delete myfile;
#endif
  }

  void RenderClick ()
  {
    Image *im;
    /* When you have the size, do */
    setImageSize ();
	
    Glib::RefPtr<TextBuffer> TextBufferP = TextArea->get_buffer ();

    /* Do some parsing here! */
    //im = render (20, 20, TextBufferP->get_text ());
    // Small tests for now

	//removed to see how many errors we'll get now
    //im = render (20, 20, TextBufferP->get_text ());
	render (20, 20, TextBufferP->get_text ());
	
    //this->im = im->getCairoSurface();
    //this->im->write_to_png ("Render.png");
    //std::cout << "Written PNG\n";
  }

  void setImageSize ()
  {
    imgWidth = Scroll->get_width ();
    imgHeight = Scroll->get_height ();
    ImageView->set_size (imgWidth, imgHeight);
//    cout << "Width is " << imgWidth << "and height is " << imgHeight << "\n";
    ImageView->queue_draw ();
  }

  bool UpdateImageView (GdkEventExpose* event)
  {

    Cairo::RefPtr<Cairo::Context> myContext = ImageView->get_bin_window()->create_cairo_context();
    Gdk::Region region = Gdk::Region(event->region, true);
    Gdk::Rectangle rect;

    Gdk::Cairo::region (myContext, region);
    myContext->clip ();

    int xOff, yOff;

    /* Translate to the center if the area is too big */
/*    region.get_clipbox (rect); */
    /* Non integer offset looks bad :P */
/*
    xOff = (ImageView->get_width() > imgWidth) ? int((ImageView->get_width() - imgWidth) / 2.0) : 0;
    yOff = (ImageView->get_height() > imgHeight) ? int((ImageView->get_height() - imgHeight) / 2.0) : 0;
    myContext->translate (xOff, yOff);
*/
      Cairo::RefPtr<Cairo::LinearGradient> lg = Cairo::LinearGradient::create(0, 0, imgWidth, imgHeight);
    if (imgWidth == -1)
      myContext->set_source (lg);
    else
      myContext->set_source (im, 0, 0);
      myContext->rectangle (0, 0, imgWidth, imgHeight);
      myContext->fill ();
/*
    lg->add_color_stop_rgb (1, 1, 0, 0);
    lg->add_color_stop_rgb (0, 0, 0, 1);
    myContext->set_source (lg);
    myContext->rectangle (0, 0, imgWidth, imgHeight);
    myContext->fill ();
*/

    return true;
  }
public:
  UI ()
  {
    imgWidth = imgHeight = -1;
    Glib::RefPtr<Builder> builder = Builder::create_from_file("RenderWindow.glade");

    builder->get_widget("RenderWindow", RenderWindow);
    builder->get_widget("TextArea", TextArea);
    builder->get_widget("FileChooseButton", FileChooseButton);
    builder->get_widget("ImageView", ImageView);
    builder->get_widget("RenderButton", RenderButton);
    builder->get_widget("ImageScroll", Scroll);
 
    /* Temporary size untill we have something real */
//    setImageSize ();
    FileChooseButton->signal_selection_changed ().connect( sigc::mem_fun(*this, &UI::LoadText) );
    RenderButton->signal_clicked ().connect( sigc::mem_fun(*this, &UI::RenderClick) );
    ImageView->signal_expose_event().connect( sigc::mem_fun(*this, &UI::UpdateImageView ), false );
    
 //   TextWindow::show();
    // gtkmm main loop
    Main::run( *RenderWindow );

  }
};

int main( int argc, char *argv[] )/* Hello World in Gtkmm */ 
{
    // Initialization
    Main kit( argc, argv );

    UI ui;
 
    return 0;
}
