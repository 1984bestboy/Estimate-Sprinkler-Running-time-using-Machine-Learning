# from time import sleep
# import serial
# ser = serial.Serial('/dev/cu.usbmodem1421',115200)
# counter=32
# while True:
#     ser.write(str(chr(counter)))
#     print ser.readline(16384)
#     sleep(.99)

# Reference - https://stackoverflow.com/questions/26946337/reading-serial-data-from-arduino-project-pyserial
import serial
import sys
import time

port = "/dev/cu.usbmodem1421"

baudrate = 9600
i=0;
avg_moisture_level=0;
total_mositure_level=0;
if len(sys.argv) == 3:
    ser = serial.Serial(sys.argv[1], sys.argv[2])
else:
    print "# Please specify a port and a baudrate"
    print "# using hard coded defaults " + port + " " + str(baudrate)
    ser = serial.Serial(port, baudrate)

# enforce a reset before we really start
#ser.setDTR(1)
#time.sleep(0.25)
#ser.setDTR(0)

# while i<10:
#     print "Current Value : " + ser.readline()
#     i=i+1
#     total_mositure_level += (int) (ser.readline())
#
# avg_moisture_level = (total_mositure_level/10)
#print ser.readline()
from xlrd import open_workbook
from xlutils.copy import copy

rb = open_workbook("/Users/balaji/Desktop/Workbook2.xls")
wb = copy(rb)

s = wb.get_sheet(0)
s.write(0,0,'A2')
wb.save('/Users/balaji/Desktop/Workbook2.xls')
while 1:
    string_line = ser.readline()
    list_values = string_line.split(',')
    for item in list_values:
        print item
        #sys.stdout.write(string_line)
# sys.stdout.flush()