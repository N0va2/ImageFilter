package com.aakash.ImageFilterApp.imgfilter;

import processing.core.PApplet;

import java.io.File;

@SuppressWarnings("serial")
public class ImageFilterApp extends PApplet {
	
	// The argument passed to main must match the class name
    static final String INSTRUCTIONS = "R: increase red filter\nE: reduce red filter\n"
			+ "G: increase green filter\nF: reduce green filter\nB: increase blue filter\n"
			+ "V: reduce blue filter\nI: increase hue tolerance\nU: reduce hue tolerance\n"
			+ "S: show dominant hue\nH: hide dominant hue\nP: process image\n"
			+ "C: choose a new file\nW: save file\nSPACE: reset image";
	
	static final int FILTER_HEIGHT = 2;
	static final int FILTER_INCREMENT = 5;
	static final int HUE_INCREMENT = 2;
	static final int HUE_RANGE = 100;
	static final int IMAGE_MAX = 640;
	static final int RGB_COLOR_RANGE = 100;
	static final int SIDE_BAR_PADDING = 10;
	static final int SIDE_BAR_WIDTH = RGB_COLOR_RANGE + 2 * SIDE_BAR_PADDING + 50;
	
	private ImageState imageState;

	boolean redrawImage = true;
	
	public static void main(String[] args) {
        PApplet.main("com.aakash.ImageFilterApp.imgfilter.ImageFilterApp");
    }

    // method for setting the size of the window
    // identical use to setup in Processing IDE except for size()
    @Override
    public void setup(){
    	noLoop();
    	imageState = new ImageState(new ColorHelper(new PixelColorHelper()));
        background(0);
        chooseFile();
        //stroke(255);
        //strokeWeight(10);
    }
    
    public void settings()
    {
    	size(IMAGE_MAX + SIDE_BAR_WIDTH, IMAGE_MAX);
    }

    // identical use to draw in Prcessing IDE
    @Override
    public void draw(){
        if(imageState.image().image() != null && redrawImage)
        {
        	background(0);
        	drawImage();
        }
        
        colorMode(RGB, RGB_COLOR_RANGE);
        fill(0);
        rect(IMAGE_MAX, 0, SIDE_BAR_WIDTH, IMAGE_MAX);
        stroke(RGB_COLOR_RANGE);
        line(IMAGE_MAX, 0, IMAGE_MAX, IMAGE_MAX);
        
        //drawing red line
        int x = IMAGE_MAX + SIDE_BAR_PADDING;
        int y = 2*SIDE_BAR_PADDING;
        stroke(RGB_COLOR_RANGE, 0, 0);
        line(x, y, x + RGB_COLOR_RANGE, y);
        line(x + imageState.redFilter(), y - FILTER_HEIGHT, x + imageState.redFilter(), y + FILTER_HEIGHT);
        
        //drawing green line
        y += 2 * SIDE_BAR_PADDING;
		stroke(0, RGB_COLOR_RANGE, 0);
		line(x, y, x + RGB_COLOR_RANGE, y);
		line(x + imageState.greenFilter(), y - FILTER_HEIGHT, x + imageState.greenFilter(), y + FILTER_HEIGHT);
		
		//drawing blue line
		y += 2 * SIDE_BAR_PADDING;
		stroke(0, 0, RGB_COLOR_RANGE);
		line(x, y, x + RGB_COLOR_RANGE, y);
		line(x + imageState.blueFilter(), y - FILTER_HEIGHT, x + imageState.blueFilter(), y + FILTER_HEIGHT);
		
		//drawing white line
		y += 2 * SIDE_BAR_PADDING;
		stroke(HUE_RANGE);
		line(x, y, x + 100, y);
		line(x + imageState.hueTolerance(), y - FILTER_HEIGHT, x + imageState.hueTolerance(), y + FILTER_HEIGHT);
		
		y += 4 * SIDE_BAR_PADDING;
		fill(RGB_COLOR_RANGE);
		text(INSTRUCTIONS, x, y);
		updatePixels();
    }
    
    private void chooseFile()
    {
    	selectInput("Select a file to process:", "fileSelected");
    }
    
    public void fileSelected(File file) {
    	if (file == null)
    		System.out.println("No file selected");
    	else
    	{
    		imageState.setFilepath(file.getAbsolutePath());
    		imageState.setUpImage(this, IMAGE_MAX);
    		redrawImage = true;
    		redraw();
    	}
    }
    
    public void drawImage()
    {
    	imageMode(CENTER);
    	imageState.updateImage(this, HUE_RANGE, RGB_COLOR_RANGE, IMAGE_MAX);
    	image(imageState.image().image(), IMAGE_MAX/2, IMAGE_MAX/2, imageState.image().getWidth(), imageState.image().getHeight());
    	redrawImage = false;
    }
    
    @Override
    public void keyPressed() {
    	switch(key) {
    	case 'c':
    		chooseFile();
    		break;
    	case 'p':
    		redrawImage = true;
    		break;
    	case ' ':
    		imageState.resetImage(this, IMAGE_MAX);
    		redrawImage = true;
    		break;
    	}
    	imageState.processKeyPress(key, FILTER_INCREMENT, RGB_COLOR_RANGE, HUE_INCREMENT, HUE_RANGE);
    	redraw();
   	}

}
