#!/bin/bash

# downloads the OUI data from http://standards-oui.ieee.org/oui/oui.txt
# and creates a basic "OUI <> vendor" map.
# (the first three octets of the MAC address are the OUI)
#
# output example:
# f4:bd:9e|Cisco Systems, Inc
#
# the raw data received via the url looks like this:
#
# 08-61-95   (hex)		Rockwell Automation
# 086195     (base 16)		Rockwell Automation
#				1 Allen-Bradley Dr.
#				Mayfield Heights  OH  44124-6118
#				US
#
# F4-BD-9E   (hex)		Cisco Systems, Inc
# F4BD9E     (base 16)		Cisco Systems, Inc
#				80 West Tasman Drive
#				San Jose  CA  94568
#				US
# ...

wget -O - http://standards-oui.ieee.org/oui/oui.txt | grep -E '^[0-9A-F][0-9A-F]-[0-9A-F][0-9A-F]-[0-9A-F][0-9A-F]\s*\(hex\)' \
    | sed 's/[[:space:]]\+(hex)[[:space:]]\+/|/' \
    | tr '[:upper:]' '[:lower:]' \
    | sed 's/-/:/' | sed 's/-/:/' \
    > oui.txt
