package stefan.reaktionstester;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ReaktionstesterActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private Button button_start;
	private Button button_ende;
	private Button button_options;
	
	private ReaktionstestView view;
	
	public static String ROT_GRUEN_IGNORIEREN = "rotGrünIgnorieren";
	public static String WARTEZEIT = "warteZeit"; 
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // UI vervollständigen
        view = new ReaktionstestView(this); 
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.linearLayout0);
        verticalLayout.addView(view);
        
        button_start = (Button) findViewById(R.id.button_start);
        button_ende = (Button) findViewById(R.id.button_ende);
        button_options = (Button) findViewById(R.id.button_einstellungen);
        
        button_start.setOnClickListener(this);
        button_ende.setOnClickListener(this);
        button_options.setOnClickListener(this);
        
    }

	@Override
	public void onClick(View v) {
		if(v == button_start) {			//Spiel starten
			new Thread(view).start();
			
		}else if(v == button_ende) {	//Programm beenden
			this.finish();
			
		}else if(v == button_options) {	//zu den Optionen wechseln
			Intent intent = new Intent(this, ReaktionstesterEinstellungen.class);
			this.startActivityForResult(intent, 0);
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// die Einstellungen-Activity ist beendet worden; neue Einstellungen übernehmen
		if(resultCode == Activity.RESULT_OK) {
    	   Bundle daten = data.getExtras();
		   boolean rotGruenIgnorieren = daten.getBoolean(ROT_GRUEN_IGNORIEREN);
		   float warteZeit = daten.getFloat(WARTEZEIT);
			
		   // Einstellungen speichern
		   einstellungenSpeichern(rotGruenIgnorieren, warteZeit);
		}
	}
    
    	
	/**
	 * Die übergebenenen Parameter in den Preferences speichern 
	 * @param rotGruenIgnorieren
	 * @param warteZeit: in Sekunden
	 */
	private void einstellungenSpeichern(boolean rotGruenIgnorieren, float warteZeit) {
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(ROT_GRUEN_IGNORIEREN, rotGruenIgnorieren);
		editor.putFloat(WARTEZEIT, warteZeit); 
		editor.commit();
	}
	
	
	
	
}