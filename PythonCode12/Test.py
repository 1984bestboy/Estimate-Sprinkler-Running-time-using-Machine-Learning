# from time import sleep
# import serial
# ser = serial.Serial('/dev/cu.usbmodem1421',115200)
# counter=32
# while True:
#     ser.write(str(chr(counter)))
#     print ser.readline(16384)
#     sleep(.99)

import serial
import sys
import time

port = "/dev/cu.usbmodem1421"

baudrate = 9600

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

while 1:
    sys.stdout.write(ser.readline())
    sys.stdout.flush()