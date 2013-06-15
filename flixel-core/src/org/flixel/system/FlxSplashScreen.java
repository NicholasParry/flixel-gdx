package org.flixel.system;

import org.flixel.FlxG;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxU;
import org.flixel.event.IFlxCamera;

/**
 *
 * @author Ka Wing Chin
 */
public class FlxSplashScreen extends FlxState
{
	private final static String ImgGearBlue = "org/flixel/data/splashscreen:gear_blue";
	private final static String ImgGearPurple = "org/flixel/data/splashscreen:gear_purple";
	private final static String ImgGearGrey = "org/flixel/data/splashscreen:gear_grey";
	private final static String ImgGearGreen = "org/flixel/data/splashscreen:gear_green";
	private final static String ImgPoweredBy = "org/flixel/data/splashscreen:powered_by";
	private final static String ImgFlixelGDX = "org/flixel/data/splashscreen:flixel-gdx";
	
	private final static String ImgHeart = "org/flixel/data/splashscreen:heart";
	private final static String ImgLightBulb = "org/flixel/data/splashscreen:lightbulb";
	private final static String ImgCode = "org/flixel/data/splashscreen:code";
	private final static String ImgDPAD = "org/flixel/data/splashscreen:dpad";
	
	private FlxState state;
	private static int CENTER_X;
	private static int CENTER_Y;
	private float _counter = 2.25f;
	private float tempZoom;
	
	public FlxSplashScreen(FlxState _requestedState)
	{
		this.state = _requestedState;
	}
	
	@Override
	public void create()
	{
		tempZoom = FlxG.camera.getZoom();
		int tempWidth = FlxG.width;
		int tempHeight = FlxG.height;
		
		FlxG.camera.setZoom(1f);
		if(FlxG.width > FlxG.height)
		{
			FlxG.width = 800;
			FlxG.height = 480;
		}
		else
		{
			FlxG.width = 480;
			FlxG.height = 800;
		}
		
		CENTER_X = FlxG.width / 2;
		CENTER_Y = FlxG.height / 2;
		FlxG.resetCameras();
		FlxG.width = tempWidth;
		FlxG.height = tempHeight;
		
		FlxG.setBgColor(0xFFFFFFFF);
		
		add(new Gear(ImgGearBlue, CENTER_X - 160, CENTER_Y - 198, 1.822f));
		add(new Gear(ImgGearPurple, CENTER_X + 18, CENTER_Y - 193, 2.55f));
		add(new Gear(ImgGearGrey, CENTER_X - 3, CENTER_Y - 114, -1.5f));
		add(new Gear(ImgGearGreen, CENTER_X - 87, CENTER_Y - 10, 2.55f));

		add(new Lightbulb(CENTER_X + 54, CENTER_Y - 68));
		add(new Heart(CENTER_X - 55, CENTER_Y + 30));
		add(new Code(CENTER_X + 41, CENTER_Y + -163));
		add(new DPad(CENTER_X - 105, CENTER_Y - 143));

		add(new FlxSprite(CENTER_X - 63, CENTER_Y + 113, ImgPoweredBy));
		add(new FlxSprite(CENTER_X - 129, CENTER_Y + 140, ImgFlixelGDX));
		
//		FlxG.log(FlxG.camera._screenScaleFactorX);
//		FlxG.log(FlxG.camera._screenScaleFactorY);
		
//		int scale = (int) FlxU.ceil(FlxG.camera._screenScaleFactorX);
		
//		cam = new FlxCamera(0, 0, FlxG.width * scale, FlxG.height * scale);
//		cam.setZoom(1f / scale);
//		cam.setColor(0xFFCCCC);
//		FlxG.addCamera(cam);
//		cam.flash(0x00000000, .25f);
//		FlxG.camera.fade(0xFFFFFFFF, 0);
		FlxG.camera.flash(0x00000000, .25f);
	}
	
	@Override
	public void update()
	{
		super.update();
		if(FlxG.mouse.justPressed())
		{
			FlxU.openURL("http://www.flixel-gdx.com");
		}
		
		if(_counter > 0)
		{
			_counter -= FlxG.elapsed;
			if(_counter <= 0)
			{
				FlxG.fade(0xFF000000, .75f, new IFlxCamera()
				{
					@Override
					public void callback()
					{
						FlxG.camera.setZoom(tempZoom);
						FlxG.switchState(state);
					}
				});
			}
		}
	}
	
	@Override
	public void destroy()
	{
		FlxG.disposeTextureAtlas("org/flixel/data/splashscreen");
		super.destroy();
	}
	
	private class Gear extends FlxSprite
	{
		private float rotationSpeed;
		
		public Gear(String img, float x, float y, float rotationSpeed)
		{
			super(x, y, img);
			this.rotationSpeed = rotationSpeed;
		}
		
		@Override
		public void update()
		{
			angle += rotationSpeed;
			super.update();
		}
	}
	
	private class Lightbulb extends FlxSprite
	{		
		private float _offCounter;
		private float _blinkCounter;
		
		public Lightbulb(float x, float y)
		{
			super(x, y);
			loadGraphic(ImgLightBulb, true, false, 48, 71);
			addAnimation("off", new int[]{0}, 0, false);
			addAnimation("on", new int[]{1}, 0, false);
			addAnimation("blink", new int[]{0,1}, 20);
			play("off");
			_offCounter = 1f;
		}
		
		@Override
		public void update()
		{
			if(_offCounter > 0)
			{
				_offCounter -= FlxG.elapsed;
				if(_offCounter <= 0)
				{
					_blinkCounter = 1f;
					play("blink");
				}
			}
			
			if(_blinkCounter > 0)
			{
				_blinkCounter -= FlxG.elapsed;
				if(_blinkCounter <= 0)
					play("on");
			}		
			super.update();
		}
	}
	
	private class Heart extends FlxSprite
	{		
		public Heart(float x, float y)
		{
			super(x, y);
			loadGraphic(ImgHeart, true, false, 42, 38);
			addAnimation("bounce", new int[]{0,0,0,1,1,2,2,1,1}, 12);
			play("bounce");
		}
	}
	
	private class DPad extends FlxSprite
	{	
		public DPad(float x, float y)
		{
			super(x, y);
			loadGraphic(ImgDPAD, true, false, 67, 67);			
			int[] array = new int[20];
			for(int i = 0; i < 20; i++)
			{
				array[i] = (int) ((FlxG.random() * 4) + 1);
				i++;
				array[i] = 0;
			}			
			addAnimation("gameOn", array, 4, true);
			play("gameOn");
		}
	}
	
	private class Code extends FlxSprite
	{		
		public Code(float x, float y)
		{
			super(x, y);
			loadGraphic(ImgCode, true, false, 37, 24);
			addAnimation("coding", new int[]{
					0,8,0,8,0,8,
					1,2,1,2,1,2,
					3,4,3,4,3,4,
					5,6,5,6,5,6,
					7
				}, 12, false);
			play("coding");
		}
	}
}

