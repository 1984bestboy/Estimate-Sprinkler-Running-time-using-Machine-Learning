import serial, time
arduino = serial.Serial('/dev/cu.usbmodem1421', 9600, timeout=.1)
time.sleep(1) #give the connection a second to settle
input_string=""
input_value=1254
input_string = str(input_value)



while True:
	arduino.write(input_string)
	time.sleep(1)
	data = arduino.readline()

	print "data is :" + data
	#if data:
		#print data #strip out the new lines for now
		# (better to do .read() in the long run for this reason