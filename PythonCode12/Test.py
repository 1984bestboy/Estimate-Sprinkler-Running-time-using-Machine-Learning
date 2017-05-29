
# Reference - https://stackoverflow.com/questions/26946337/reading-serial-data-from-arduino-project-pyserial

import serial
import sys
from xlrd import open_workbook
from xlutils.copy import copy
import time

port = "/dev/cu.usbmodem1421"

baudrate = 9600
i = 0
j = 0
avg_moisture_level = 0.0
total_mositure_level = 0.0

total_humidity = 0.0
average_humidity = 0.0

total_temperature = 0.0
average_temperature = 0.0

if len(sys.argv) == 3:
    ser = serial.Serial(sys.argv[1], sys.argv[2])
else:
    print "# Please specify a port and a baudrate"
    print "# using hard coded defaults " + port + " " + str(baudrate)
    ser = serial.Serial(port, baudrate)



l = -1
rb = open_workbook("/Users/balaji/Documents/Github/IOT/Weather/src/package_iot/newFile.xlsx")
wb = copy(rb)
s = wb.get_sheet(0)
s.write(0, 0, "Humidity")
s.write(0, 1, "Temperature")
s.write(0, 2, "Moisture")
while i < 10:
    print "Current Value : " + ser.readline()

    string_line = ser.readline()
    list_values = string_line.split(',')

    k = 0
    j = 0

    for item in list_values:

        j = j + 1

        if (j == 1):
            print "Humdity :" + item
            total_humidity = total_humidity + float(item)

        if (j == 2):
            print "Temperature : " + item
            total_temperature = total_temperature + float(item)
        if (j == 3):
            print "Moisture : " + item
            total_mositure_level = total_mositure_level + float(item)

s.write(1, 0, total_humidity / 10)
s.write(1, 1, total_temperature / 10)
s.write(1, 2, total_temperature / 10)

# sys.stdout.write(string_line)

wb.save('/Users/balaji/Desktop/Workbook2.xls')
# sys.stdout.flush()
