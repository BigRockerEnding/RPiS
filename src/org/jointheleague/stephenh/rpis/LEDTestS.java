package org.jointheleague.stephenh.rpis;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class LEDTestS {
	private static final Pin[] PINS = new Pin[] {
		RaspiPin.GPIO_00,
		RaspiPin.GPIO_01,
		RaspiPin.GPIO_02,
		RaspiPin.GPIO_03,
		RaspiPin.GPIO_04,
		RaspiPin.GPIO_05,
		RaspiPin.GPIO_06,
		RaspiPin.GPIO_21
	};
	GpioPinDigitalOutput[] leds = new GpioPinDigitalOutput[8];
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java -jar RPiSLED.jar <delay> <repeats>");
			return;
		}
		int delay = Integer.parseInt(args[0]);
		int repeats = Integer.parseInt(args[1]);
		if (delay <= 0 || repeats <= 0) {
			System.out.println("Invalid Arguments: Both args must be greater than 0");
			return;
		}
		new LEDTestS().run(delay, repeats);
	}

	private void run(int delay, int repeats) {
		final GpioController gpio = GpioFactory.getInstance();
		for (int i = 0; i < leds.length; i++) {
			leds[i] = gpio.provisionDigitalOutputPin(PINS[i], PinState.HIGH);
		}
		try {
			for (int i = 0; i < repeats; i++) {
				ledWave(delay);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void ledWave(int delay) throws InterruptedException {
		leds[0].low();
		Thread.sleep(delay);
		leds[1].low();
		Thread.sleep(delay);
		leds[2].low();
		for (int i = 3; i < leds.length; i++) {
			Thread.sleep(delay);
			leds[i].low();
			leds[i - 3].high();
		}
		Thread.sleep(delay);
		leds[5].high();
		Thread.sleep(delay);
		leds[6].high();
		Thread.sleep(delay);
		leds[7].high();
		Thread.sleep(delay);
		leds[7].low();
		Thread.sleep(delay);
		leds[6].low();
		Thread.sleep(delay);
		leds[5].low();
		for (int i = 4; i >= 0; i--) {
			Thread.sleep(delay);
			leds[i].low();
			leds[i + 3].high();
		}
		Thread.sleep(delay);
		leds[2].high();
		Thread.sleep(delay);
		leds[1].high();
		Thread.sleep(delay);
		leds[0].high();
		Thread.sleep(delay);
	}
}
