# 2.0.0 (2016-09-18) #

- The first version of peci1's fork.

- Added the Live stream viewer Java source code.
- The Live stream viewer can be used also with the camera connected to different than the "default" network interface.
- The Live stream viewer accepts two optional command-line parameters: `cameraIp` `cameraNetMaskBitSize` (specify either both or none).
- The Live stream viewer code has been refactored a bit and documented.
- The Live stream viewer is now distributed as a JAR archive and has an Ant buildfile.

- The Control.html/requests.html now accepts a URL parameter `ip` which designates the camera's IP address (you no longer need to edit the source code).
- If the `ip` parameter is not given in URL, the webpage asks for it in a Javascript prompt.
