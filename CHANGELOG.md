# 3.2.0 (2019-04-20) #

- Added options to set camera time.

# 3.1.0 (2018-01-11) #

- Added button to stop recording.

# 3.0.0 (2017-11-22) #

- Refactoring by Azzurite
    - control.bat now takes up to three command-line arguments
        1. web browser to use to open the webpage
        1. IP address of the camera
        1. IP mask of the camera
        
        all of the arguments are optional
    - Threaded execution        

# 2.0.0 (2016-09-18) #

- The first version of peci1's fork.

- Added the Live stream viewer Java source code.
- The Live stream viewer can be used also with the camera connected to different than the "default" network interface.
- The Live stream viewer accepts two optional command-line parameters: `cameraIp` `cameraNetMaskBitSize` (specify either both or none).
- The Live stream viewer code has been refactored a bit and documented.
- The Live stream viewer is now distributed as a JAR archive and has an Ant buildfile.

- The Control.html/requests.html now accepts a URL parameter `ip` which designates the camera's IP address (you no longer need to edit the source code).
- If the `ip` parameter is not given in URL, the webpage asks for it in a Javascript prompt.
