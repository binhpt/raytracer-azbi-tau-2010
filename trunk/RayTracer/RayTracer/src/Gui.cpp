#include <string>
#include <iostream>
#include <fstream>
#include <gtkmm.h>
#include <cairo.h>
#include <cairomm/surface.h>

#include "common.h"
#include "Image.h"
//#include "Window.h"

using namespace std;
using namespace Glib;

Image* render (int screenWidth, int screenHeight, std::string config);

class UI
{
protected:
  Cairo::RefPtr <Cairo::ImageSurface> im;
  Gtk::Window* RenderWindow;
  Gtk::TextView* TextArea;
  Gtk::FileChooserButton* FileChooseButton;
  Gtk::Layout* ImageView;
  Gtk::ToolButton* RenderButton;
  Gtk::ScrolledWindow* Scroll;

  int imgWidth;
  int imgHeight;

  void LoadText ()
  {
	char c;
	string buf;
    // You can treat this file name as a regular c++ string
	ifstream f(FileChooseButton->get_filename(), ios::in);

	if (f.is_open())
	{
		while (f.good())
		{
			f.get(c);
			buf.push_back(c);
		}
		f.close();

	}
	else
	{
		//BARAK - file wouldn't open, print some kind of error message
		cout << "could not open file";
	}

    // This code will put the text in the area
	TextArea->get_buffer()->set_text(buf);
  }

  void RenderClick ()
  {
    Image *im;
    /* When you have the size, do */
    setImageSize ();
	
    Glib::RefPtr<Gtk::TextBuffer> TextBufferP = TextArea->get_buffer ();

    /* Do some parsing here! */
    //im = render (20, 20, TextBufferP->get_text ());
    // Small tests for now
    im = render (20, 20, TextBufferP->get_text ());
    this->im = im->getCairoSurface();
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
    Glib::RefPtr<Gtk::Builder> builder = Gtk::Builder::create_from_file("RenderWindow.glade");

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
    Gtk::Main::run( *RenderWindow );

  }
};

int main( int argc, char *argv[] )/* Hello World in Gtkmm */ 
{
    // Initialization
    Gtk::Main kit( argc, argv );

    UI ui;
 
    return 0;
}
