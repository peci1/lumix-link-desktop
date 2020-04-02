# Usage: Control.sh [browser [camera_ip [camera_ip_mask]]] 
#        Control.sh (opens the page in the default browser, asks for IP address and network mask of the camera)
#        Control.sh firefox (opens the page in firefox, asks for IP address and network mask of the camera)
#        Control.sh firefox 192.168.0.1 (opens the page in firefox, doesn't ask for anything, assumes network mask /24)
#        Control.sh firefox 192.168.0.1 16 (opens the page in firefox, doesn't ask for anything, uses the given network mask /16)

browser=xdg-open
ip=
mask=24

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

if [[ $# -gt 0 ]]; then
    browser="$1"
fi

if [[ $# -gt 1 ]]; then
    ip="$2"
fi

if [[ $# -gt 2 ]]; then 
    mask="$3"
fi

function start_with_ip()
{
    "$browser" "file://${SCRIPT_DIR}/Control.html?ip=$ip" &
    java -jar "${SCRIPT_DIR}/StreamViewer.jar" "$ip" "$mask"
}

function start_without_ip()
{
    "$browser" "file://${SCRIPT_DIR}/Control.html" &
    java -jar "${SCRIPT_DIR}/StreamViewer.jar"
}

if [[ "$ip" != "" ]]; then
    start_with_ip
else
    start_without_ip
fi
