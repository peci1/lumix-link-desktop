# Unofficial desktop Lumix Link application #

This is an unofficial counterpart of the official [Panasonic Lumix Link mobile app](https://play.google.com/store/apps/details?id=jp.co.panasonic.lumix_link.activity&hl=cs). With this application, you can remotely control your Lumix camera, take pictures, record video, adjust capture settings and so on. 

## Usage instructions ##

Basically, the usage instructions can be found at http://www.personal-view.com/talks/discussion/6703/control-your-gh3-from-a-web-browser-now-with-video-/p1 . 

Do not download the application from the forum, download [it here on Github](https://github.com/peci1/lumix-link-desktop/releases).

There are a few differences in this version to the one posted on the forum: both the web and the Java applications ask you for the camera's IP address, so that different connection modes are supported (e.g. the camera connected to your home wifi). Check the [changelog](https://github.com/peci1/lumix-link-desktop/blob/master/CHANGELOG.md) for more.

## Supported models ##

The **basic parts of this application should work with all Lumix cameras that can be used with the mobile app**. 

However, as the cameras have different features, this application might or might not support all of them. Here is a list of tested cameras.

100% compatible (and tested) cameras:
 
 - GH3

mostly compatible (and tested) cameras:

 - GH4
 - G6
 - GM1
 - FZ1000
 - GX80/GX85

You are welcome to report your experience with different types of cameras.

## Origin of this application ##

This is a "fork" of the application published on http://www.personal-view.com/talks/discussion/6703/control-your-gh3-from-a-web-browser-now-with-video-/p1 . Unfortunately, I haven't found a repository for this application, so I had to create my own one and now I'm trying to communicate with the original author to merge our efforts.

Thanks very much to leniusible for the initial work on reverse-engeneering the communication protocol. If you are lenuisible, please, contact me here on github!

## License ##

The license of the original application is unclear. I assumed it is given to the public without any restrictions.

Changes since version 2.0.0 (the first version of this fork) are covered by the 3-clause BSD license.