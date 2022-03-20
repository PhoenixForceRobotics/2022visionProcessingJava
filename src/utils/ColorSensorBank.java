package utils;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
//import java.io.IOException;
 
//sample code taken from ControlEverything
//for use with the Adafruit TCS34725 color sensor
//TODO: modify to use dual sensors

public class ColorSensorBank {
	private static I2CBus bus;
	private static I2CDevice device;

    public ColorSensorBank() throws IOException, I2CFactory.UnsupportedBusNumberException {
        //Create I2CBus
		bus = I2CFactory.getInstance(I2CBus.BUS_1);
		//Get I2C device, TCS34725 I2C address is 0x29(41)
		device = bus.getDevice(0x29);
    }

    private void enableSensor() throws IOException {
        //Select enable register
		//Power ON, RGBC enable, wait time disable
		device.write(0x80, (byte)0x03);
		//Select ALS time register
		//Atime = 700 ms
		device.write(0x81, (byte)0x00);
		//Select Wait Time register
		//WTIME : 2.4ms
		device.write(0x83, (byte)0xFF);
		//Select control register
		//AGAIN = 1x
		device.write(0x8F, (byte)0x00);
    }

	public static double[] getData() throws IOException {
		//Read 8 bytes of data
		//cData lsb, cData msb, red lsb, red msb, green lsb, green msb, blue lsb, blue msb
		byte[] data = new byte[8];
		device.read(0x94, data, 0, 8);

        double[] output = new double[5];

		//Convert the data
		int cData = ((data[1] & 0xFF) * 256) + (data[0] & 0xFF);
		int red = ((data[3] & 0xFF) * 256) + (data[2] & 0xFF);
		int green = ((data[5] & 0xFF) * 256) + (data[4] & 0xFF); 
		int blue = ((data[7] & 0xFF) * 256) + (data[6] & 0xFF);
 
		//Calculate final lux
		double luminance = (-0.32466 * red) + (1.57837 * green) + (-0.73191 * blue);

		//java is stinky and wouldn't let me just assign it as a singular list so element by element fun times
        output[0] = cData;
		output[1] = red;
		output[2] = green;
		output[3] = blue;
		output[4] = luminance;
 
		//Output data to Screen
		System.out.printf("Red Color Luminance   : %d lux %n", red);
		System.out.printf("Green Color Luminance : %d lux %n", green);
		System.out.printf("Blue Color Luminance  : %d lux %n", blue);
		System.out.printf("IR Luminance          : %d lux %n", cData);
		System.out.printf("Ambient Light Luminance : %d lux %n", luminance);

        return output;
	}    
}