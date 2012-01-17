package stefan.reaktionstester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class ReaktionstesterEinstellungen extends Activity implements OnClickListener {

	private Spinner warteZeitSpinner;
	private ArrayAdapter<String> adapter;
	private Button okButton;
    private CheckBox rotGruenCheckbox;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.einstellungen);
		
	    warteZeitSpinner = (Spinner) findViewById(R.id.spinner1);
	    String[] werte = new String[]{"1 s", "1.5 s", "2 s"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, werte); 
        warteZeitSpinner.setAdapter(adapter);
        
        okButton = (Button) findViewById(R.id.button4);
        okButton.setOnClickListener(this);
        
        rotGruenCheckbox = (CheckBox) findViewById(R.id.checkBox1);
	}

	@Override
	public void onClick(View v) {
		// aktuelle Einstellungen zurückgeben an Aufrufer 
		Intent ergebnis = new Intent();
		
		// wartezeit extrahieren
		String str      = (String) warteZeitSpinner.getSelectedItem(); 
		int pos         = str.indexOf('s');
		String strWert  = str.substring(0, pos).trim();
		float warteZeit = Float.valueOf(strWert);
		ergebnis.putExtra(ReaktionstesterActivity.WARTEZEIT, warteZeit);
		
		// rot-grün Flag
		ergebnis.putExtra(ReaktionstesterActivity.ROT_GRUEN_IGNORIEREN, rotGruenCheckbox.isChecked());		
		this.setResult(Activity.RESULT_OK, ergebnis);

		finish();
	}

}
