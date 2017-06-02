
# Reference - https://stackoverflow.com/questions/26946337/reading-serial-data-from-arduino-project-pyserial
# Reference - https://stackoverflow.com/questions/13381384/modify-an-existing-excel-file-using-openpyxl-in-python?rq=1

import serial
import sys
from threading import Timer
from xlrd import open_workbook
from xlutils.copy import copy
import time


def main_code():
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

    # New Code
    from openpyxl import load_workbook
    import csv

    #Open an xlsx for reading
    wb = load_workbook("/Users/balaji/Documents/Github/IOT/Weather/src/newFile.xlsx")
    #Get the current Active Sheet
    ws = wb.get_active_sheet()
    #You can also select a particular sheet
    #based on sheet name
    ws = wb.get_sheet_by_name("Current Weather")

    #ws.cell(row=1,column=12).value = 123456789
    #ws.cell(row=1,column=13).value = 123456789
    #ws.cell(row=1, column=14).value = 123456789
    #save the csb file
    wb.save("/Users/balaji/Documents/Github/IOT/Weather/src/newFile.xlsx")
    # New Code
    last_row=ws.max_row
    l = -1
    # rb = open_workbook("/Users/balaji/Documents/Github/IOT/Weather/src/package_iot/newFile.xlsx")
    # wb = copy(rb)
    # s = wb.get_sheet(0)
    # s.write(0, 0, "Humidity")
    # s.write(0, 1, "Temperature")
    # s.write(0, 2, "Moisture")
    # while i<1:
    print "Current Value : " + ser.readline()
    i=i+1
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

    ws.cell(row=last_row,column=13).value = (total_humidity / 10)
    ws.cell(row=last_row,column=14).value = (total_temperature / 10)
    ws.cell(row=last_row,column=15).value = (total_mositure_level / 10)

    wb.save("/Users/balaji/Documents/Github/IOT/Weather/src/newFile.xlsx")
    t = Timer(13, main_code)
    t.start()



main_code()
# sys.stdout.write(string_line)

#wb.save('/Users/balaji/Desktop/Workbook2.xls')
# sys.stdout.flush()