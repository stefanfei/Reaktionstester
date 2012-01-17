package stefan.reaktionstester;

import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;


public class ReaktionstestView extends View implements Runnable {
	private final String[] FARBNAMEN = {"rot", "grün", "blau", "gelb", "schwarz", "grau", "magenta"};
	private final int[]  FARBEN      = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK, Color.GRAY, Color.MAGENTA};
	

    private int farbIndex;
    private int nameIndex;
    private Paint textPaint;
    private Paint anweisungPaint; 
	private Random zufallsGenerator; 
    private long letzterZeitPunktDraw;
    private SharedPreferences einstellungen;
    
	public ReaktionstestView(Context context) {
	   super(context);

	   Drawable background = getResources().getDrawable(R.drawable.background); 
	   this.setBackgroundDrawable(background);
	   
       textPaint = new Paint();
       textPaint.setTextSize(50);
       textPaint.setStrokeWidth(10);
       anweisungPaint = new Paint();
       anweisungPaint.setTextSize(20);
       anweisungPaint.setStrokeWidth(5);
       anweisungPaint.setColor(Color.BLACK);
       
       
       nameIndex   = -1;
       farbIndex = -2; 
       
 	   // Zufallszahlengenerator
 	   zufallsGenerator    = new Random(System.currentTimeMillis());
 	   
 	   einstellungen = ((ReaktionstesterActivity) context).getPreferences(Context.MODE_PRIVATE); 
	}
	


	/**
	 * Farbe/Name Kombination auswählen und anzeigen lassen
	 */
	public void run() {
		int indexUngleich = 0;
		
		while(true) {
			nameIndex = zufallsGenerator.nextInt(FARBNAMEN.length);			
			farbIndex = zufallsGenerator.nextInt(FARBEN.length);
			
			boolean rotGruenIgnorieren = einstellungen.getBoolean(ReaktionstesterActivity.ROT_GRUEN_IGNORIEREN, false);
			
			if(rotGruenIgnorieren) {
				if(nameIndex <= 1 || farbIndex <= 1) {  // an Position 0 und 1 sind rot und grün; überspringen falls gesetzt
					continue; 
				}
			}
			
			if(nameIndex != farbIndex) {
				indexUngleich++;
				
				if(indexUngleich == 10) { // nicht zu lange den Benutzer auf eine richtige Kombination warten lassen...
					farbIndex = nameIndex;
					indexUngleich = 0;
				}
			}

			this.postInvalidate(); // Anzeige neu zeichnen lassen
			
			try {
			    float zeit = einstellungen.getFloat(ReaktionstesterActivity.WARTEZEIT, 1.5f);
                // kurz warten  (Zeit in Millisekunden)
                long warteZeit = (long)  zeit * 1000;
                Thread.sleep(warteZeit);
			}
			catch(Exception ex) {}
		}
	}
	
	
	/**
     * Spieler hat reagiert
     */
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		   	
    	int action = event.getAction();
    	
    	if(action == MotionEvent.ACTION_DOWN) {
            
    		if(nameIndex == farbIndex) {
    			// der Spieler hat richtig reagiert: Farbe == Text
    			ergebnisAnzeigen();
    		}
    		
    		return true;
    	}
    	
    	

 		return super.onTouchEvent(event);    	
    }

    
    private void ergebnisAnzeigen() {
    	long reaktionszeit = System.currentTimeMillis() - this.letzterZeitPunktDraw;
    	

		AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
		
    	Resources resources = getResources();
	   	String nachricht    = resources.getText(R.string.reaktionsZeit) + ": " + reaktionszeit + " ms";
	    
		alertDialog.setTitle(resources.getText(R.string.ergebnis));
		alertDialog.setMessage(nachricht);
		CharSequence okMsg         = resources.getText(R.string.ok);
		android.os.Handler handler =  new Handler(new MyHandler());
		Message dummy              = Message.obtain(handler, 0);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, okMsg, dummy); 

		alertDialog.show();
    }


    /** 
     *  Ansicht neu zeichnen
     */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int screenWidth  = getWidth();
		int screenHeight = getHeight();

	    int posX = screenWidth / 3 ;
	    int posY = screenHeight / 2; 

	    
		
		if(nameIndex == -1) {
			canvas.drawText("Berühre Bildschirm, wenn Farbe = Text!", posX / 2, (int) (1.2 * posY), anweisungPaint);
		}
		else {
	       String colourName = FARBNAMEN[nameIndex];
	       int colour        = FARBEN[farbIndex];
	       textPaint.setColor(colour);	    
	       canvas.drawText(colourName, posX, posY, textPaint);
	       letzterZeitPunktDraw = System.currentTimeMillis();
		}
	}
    
    
	private class MyHandler implements Callback {

		@Override
		public boolean handleMessage(Message arg0) {
			return true;
		}
	}  
}