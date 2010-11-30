#include <string.h>
#include <iostream>
#include <gtkmm.h>
#include <cairo.h>
//#include "Window.h"

class UI
{
protected:
  Gtk::Window* RenderWindow;
  Gtk::TextView* TextArea;
  Gtk::FileChooserButton* FileChooseButton;
  Gtk::Layout* ImageView;
  Gtk::ToolButton* RenderButton;

  int imgWidth;
  int imgHeight;

  void LoadText ()
  {
    Glib::ustring fileName = FileChooseButton->get_filename ();
    // You can treat this file name as a regular c++ string
    std::string buf;
    // TODO: Read the file here, to the string buf

    // This code will put the text in the area
    Glib::RefPtr<Gtk::TextBuffer> TextBufferP = TextArea->get_buffer ();
    TextBufferP->set_text (buf);
  }

  void Render ()
  {
    /* Do some parsing here! */

    /* When you have the size, do */
    setImageSize (50, 50);
  }

  void setImageSize (int w, int h)
  {
    imgWidth = w;
    imgHeight = h;
    ImageView->set_size (w, h);
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
    region.get_clipbox (rect);
    /* Non integer offset looks bad :P */
    xOff = (rect.get_width() > imgWidth) ? int((rect.get_width() - imgWidth) / 2.0) : 0;
    yOff = (rect.get_height() > imgHeight) ? int((rect.get_height() - imgHeight) / 2.0) : 0;
    myContext->translate (xOff, yOff);

    /* TODO: Draw something on the canvas here! */
    myContext->rectangle (0, 0, imgWidth, imgHeight);
    myContext->fill ();

    return true;
  }
public:
  UI ()
  {
    Glib::RefPtr<Gtk::Builder> builder = Gtk::Builder::create_from_file("RenderWindow.glade");

    builder->get_widget("RenderWindow", RenderWindow);
    builder->get_widget("TextArea", TextArea);
    builder->get_widget("FileChooseButton", FileChooseButton);
    builder->get_widget("ImageView", ImageView);
    builder->get_widget("RenderButton", RenderButton);
 
    /* Temporary size untill we have something real */
    setImageSize (200, 150);
    FileChooseButton->signal_selection_changed ().connect( sigc::mem_fun(*this, &UI::LoadText) );
    RenderButton->signal_clicked ().connect( sigc::mem_fun(*this, &UI::Render) );
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
