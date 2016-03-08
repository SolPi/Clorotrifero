package com.SolPi2.clorotrifero;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import com.SolPi2.clorotrifero.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import android.R.color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemClock;

public class Clock extends Activity {
	
	private Handler mHandler = new Handler();
	public static Handler handlerAd = new Handler();
	private int Contador= 0;
	private int on_off = 0;
	private boolean invisible= false;
	private long mStartTime = 0;
	private DecimalFormat df = new DecimalFormat("00");
	public TextView cuentaatras;
	int actualizar = 0;
	static final String[] items = new String [100];
	private List<Lista> li = new ArrayList<Lista>();
	private int pos;
	private static MediaPlayer mp;
	private Soniquete sonido;
	private int nCA;
	private Vibrator v;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
		//Uri puri = Uri.parse("file:///mnt/sdcard/duo.mp3");
		mp = MediaPlayer.create(Clock.this, R.raw.alarma);
		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		TextView myTextView = (TextView) findViewById(R.id.textView1);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)myTextView.getLayoutParams();
		
		if (isOnline()){
			params.addRule(RelativeLayout.BELOW, R.id.adView);
			iniciarAds();
		}else {	
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);			
			myTextView.setLayoutParams(params);
		}
    }
    
    public boolean isOnline() {
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    
    public void iniciarAds ( ) {
		handlerAd.post(new Runnable() {
			@Override
			public void run() {
				AdView adView = (AdView) findViewById(R.id.adView);
			    adView.loadAd(new AdRequest());
			}
		});
	}
   

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.clock, menu);
        return true;
    }
    
  
    public void Start(View Button){
    	mStartTime = SystemClock.uptimeMillis() - mStartTime;
    	int resID = getResources().getIdentifier("button3","id","com.SolPi2.clorotrifero");
        Button button = (Button)findViewById(resID); 
    	if (on_off==0){
    		button.setText(R.string.Pause);
    		on_off=1;
    		mHandler.removeCallbacks(mMuestraMensaje);
    		mHandler.postDelayed(mMuestraMensaje, 0);
    	}else {
    		on_off=0;
    		button.setText(R.string.Start);
    		onDestroy();
    	} 
    }
    
    public void Restart(View Button){	
    	if (on_off==0){
    		actualizar = 0;
    		Contador = 0;
    		mStartTime = 0;
    		TextView TextNumbers = (TextView) findViewById(R.id.textView1);
    		TextView Milesimas = (TextView) findViewById(R.id.textView2);
    		TextNumbers.setText("00:00:00");
    		Milesimas.setText("000");
    	}else {
    		invisible = !invisible;  		
    	}   	
    }
    
    private PopupWindow pw;
    public void initiatePopupWindow() {
        try {
        	LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	View layout = inflater.inflate(R.layout.poptime,
                    (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 220, 250, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 120);
     
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   
    
    public void cuenta_atras(View TextView){
    	initiatePopupWindow();
    	cuentaatras= (TextView) TextView;
    	String tag = cuentaatras.getTag().toString();
        if (tag.charAt(1) == 'y'){
            sonido.interrupt();
    		mp.stop();
    		cuentaatras.setTextColor(-13487566);
    		cuentaatras.setTag(cuentaatras.getTag().toString().substring(0,1)+
    						   "n"+cuentaatras.getTag().toString().substring(2));
    		
    		int cuenta = Integer.parseInt(cuentaatras.getTag().toString().substring(2));
    		cuentaatras.setText(String.valueOf(df.format(cuenta/3600000))+
	  	    		':'+String.valueOf(df.format((cuenta/60000)%60))+
	 	    		':'+String.valueOf(df.format((cuenta/1000)%60)));
    	}else{
	    	View ver = (View) findViewById(R.id.restamincontra);
	    	ver.setVisibility(View.VISIBLE);
	    	ver.setClickable(true);
	    	ver = (View) findViewById(R.id.sumamincontra);
	    	ver.setVisibility(View.VISIBLE);
	    	ver.setClickable(true);
	    	ver = (View) findViewById(R.id.mincontra);
	    	ver.setVisibility(View.VISIBLE);	
	    	ver = (View) findViewById(R.id.restahoracontra);
	    	ver.setVisibility(View.VISIBLE);
	    	ver.setClickable(true);
	    	ver = (View) findViewById(R.id.sumahoracontra);
	    	ver.setVisibility(View.VISIBLE);	
	    	ver.setClickable(true);
	    	ver = (View) findViewById(R.id.horacontra);
	    	ver.setVisibility(View.VISIBLE);
	    	ver = (View) findViewById(R.id.restasegcontra);
	    	ver.setVisibility(View.VISIBLE);
	    	ver.setClickable(true);
	    	ver = (View) findViewById(R.id.sumasegcontra);
	    	ver.setVisibility(View.VISIBLE);
	    	ver.setClickable(true);
	    	ver = (View) findViewById(R.id.segcontra);
	    	ver.setVisibility(View.VISIBLE);
	    	ver = (View) findViewById(R.id.aceptarcuentaatras);
	    	ver.setVisibility(View.VISIBLE);
	    	ver.setClickable(true);
    	}
    }
    
    
//    SetTag comenzara con 0 o 1 señalando si la cuenta a sido activada
    public void aceptarcuentaatras(View TextView){
    	TextView hora = (TextView) findViewById(R.id.horacontra);
    	TextView min =  (TextView) findViewById(R.id.mincontra);
    	TextView seg =  (TextView) findViewById(R.id.segcontra);
    	
    	if ((Integer.parseInt(hora.getText().toString())+
    		 Integer.parseInt(min.getText().toString())+
    		 Integer.parseInt(seg.getText().toString()))!=0){
    		
    		cuentaatras.setTag("1n" + String.valueOf((Integer.parseInt(hora.getText().toString())*3600000+
					   Integer.parseInt(min.getText().toString())*60000+
					   Integer.parseInt(seg.getText().toString())*1000)));
		    	
    	}else{
    		cuentaatras.setBackgroundColor(color.white);
    	}
    	
    	cuentaatras.setText((hora.getText().toString()+":"+
    						 min.getText().toString()+":"+
    						 seg.getText().toString()));
    	
    	hora.setVisibility(View.INVISIBLE);
    	min.setVisibility(View.INVISIBLE);
    	seg.setVisibility(View.INVISIBLE);

    	hora.setText("00");
    	min.setText("00");
    	seg.setText("00");
    	
    	View ver = (View) findViewById(R.id.restamincontra);
    	ver.setVisibility(View.INVISIBLE);
    	ver.setClickable(false);
    	ver = (View) findViewById(R.id.sumamincontra);
    	ver.setVisibility(View.INVISIBLE);
    	ver.setClickable(false);
    	ver = (View) findViewById(R.id.restahoracontra);
    	ver.setVisibility(View.INVISIBLE);
    	ver.setClickable(false);
    	ver = (View) findViewById(R.id.sumahoracontra);
    	ver.setVisibility(View.INVISIBLE);	
    	ver.setClickable(false);
    	ver = (View) findViewById(R.id.restasegcontra);
    	ver.setVisibility(View.INVISIBLE);
    	ver.setClickable(false);
    	ver = (View) findViewById(R.id.sumasegcontra);
    	ver.setVisibility(View.INVISIBLE);
    	ver.setClickable(false);
    	ver = (View) findViewById(R.id.aceptarcuentaatras);
    	ver.setVisibility(View.INVISIBLE);
    	ver.setClickable(false);
    }
    
    public void sumar(View B){
    	int Id = getResources().getIdentifier(B.getTag().toString(), "id", "com.SolPi2.clorotrifero");	
    	TextView cuenta = (TextView) findViewById(Id);
    	int num = Integer.parseInt(cuenta.getText().toString())+1;
    	if (num == 60){
    		cuenta.setText("00");
    	}else{
        	cuenta.setText(String.valueOf(df.format(num)));
    	}
    }
    
    public void restar(View B){
    	int Id = getResources().getIdentifier(B.getTag().toString(), "id", "com.SolPi2.clorotrifero");	
    	TextView cuenta = (TextView) findViewById(Id);
    	int num = Integer.parseInt(cuenta.getText().toString())-1;
    	if (num == -1){
    		cuenta.setText("59");
    	}else{
        	cuenta.setText(String.valueOf(df.format(num)));
    	}
    }
    
    
    
    private void Cuentasatras(){
    	for (int i = 1; i < 10; i++) {
	    	int Id = getResources().getIdentifier("cuentaatras" + i, "id", "com.SolPi2.clorotrifero");	
	    	TextView cuentaatras = (TextView) findViewById(Id);
	    	int cuenta = 0;
	        int entrar = Integer.parseInt(cuentaatras.getTag().toString().substring(0,1));
	        if (entrar ==1){
	        	cuenta = Integer.parseInt(cuentaatras.getText().toString().substring(0, 2))*3600000 +
	    			     Integer.parseInt(cuentaatras.getText().toString().substring(3, 5))*60000 +
	    			     Integer.parseInt(cuentaatras.getText().toString().substring(6, 8))*1000;
	        }else {
	        	cuenta = Integer.parseInt(cuentaatras.getTag().toString().substring(2));
	        }
	        
	        if (cuenta > 0 && entrar == 1){
		        cuenta = cuenta - 1000;
		        cuentaatras.setText(String.valueOf(df.format(cuenta/3600000))+
				  	    		':'+String.valueOf(df.format((cuenta/60000)%60))+
				 	    		':'+String.valueOf(df.format((cuenta/1000)%60)));
	        	cuentaatras.setBackgroundColor(Color.CYAN);
			}else if (!(cuenta <= 0 ^ entrar == 1)){
	        	if (entrar == 1){
	        		nCA = i;
					sonido = new Soniquete();
					sonido.start();
					cuentaatras.setTag("0y"+cuentaatras.getTag().toString().substring(2));
					cuentaatras.setBackgroundColor(Color.RED);
					int id =+ 1;
					Notificacion(id);
					
	        	}
				if (cuentaatras.getTag().toString().charAt(1)== 'y'){
					v.cancel();
					v.vibrate(500);
					if (cuentaatras.getCurrentTextColor() == Color.YELLOW){
						cuentaatras.setTextColor(Color.WHITE);
						cuentaatras.setText(R.string.Stop);
					}else{
						cuentaatras.setTextColor(Color.YELLOW);
						cuentaatras.setText(String.valueOf(df.format(cuenta/3600000))+
				  	    		':'+String.valueOf(df.format((cuenta/60000)%60))+
				 	    		':'+String.valueOf(df.format((cuenta/1000)%60)));
					}
				}
			}
    	}
    }
    
    @SuppressWarnings("deprecation")
	public void Notificacion(int id){
    	String ns = Context.NOTIFICATION_SERVICE;
    	NotificationManager notManager = (NotificationManager) getSystemService(ns);
    	
    	int icono = R.drawable.ic_launcher;
    	String textoEstado = getString(R.string.app_name);
    	long hora = System.currentTimeMillis();
    	 
		Notification notif =  new Notification (icono, textoEstado, hora);
    	
    	Context contexto = getApplicationContext();
    	String titulo = getString(R.string.Countdown);
    	String descripcion = getString(R.string.CountdownDescp);
//    	 
    	Intent notIntent = getIntent();
//    	 
    	PendingIntent contIntent = PendingIntent.getActivity(
    			contexto, 0, notIntent, 0);
//    	 
    	notif.setLatestEventInfo(contexto, titulo, descripcion, contIntent);
    	
    	notif.flags |= Notification.FLAG_AUTO_CANCEL;
    	notif.defaults |= Notification.DEFAULT_LIGHTS;
    	
    	notManager.notify(id, notif);
    }
    
    public void pararCuentasAtras(){
    	mp.stop();
    	v.cancel();
    	TextView cuentaatras;
    	for (int i = 1; i < 10; i++) {
    		int Id = getResources().getIdentifier("cuentaatras" + i, "id",
    											  "com.SolPi2.clorotrifero");	
        	cuentaatras = (TextView) findViewById(Id);
    		if (i != nCA && cuentaatras.getTag().toString().charAt(1) == 'y'){
				cuentaatras.setTextColor(-13487566);
	    		cuentaatras.setTag(cuentaatras.getTag().toString().substring(0,1)+
	    						   "n"+cuentaatras.getTag().toString().substring(2));
	    		int cuenta = Integer.parseInt(cuentaatras.getTag().toString().substring(2));
	    		cuentaatras.setText(String.valueOf(df.format(cuenta/3600000))+
		  	    		':'+String.valueOf(df.format((cuenta/60000)%60))+
		 	    		':'+String.valueOf(df.format((cuenta/1000)%60)));
			}
		}
    }
    
    class Soniquete extends Thread{
		public void run(){
	    	if (mp.isPlaying()){
    			pararCuentasAtras();
    		}
			mp = MediaPlayer.create(Clock.this, R.raw.alarma);
    		mp.start();
    		try {	
				Thread.sleep(mp.getDuration()+500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

    	
//    runable no se ejecutara exactamente cada milisegundo por lo que es 
//    necesario utilizar SystemClock para que el reloj no tenga desfase 
//    razon por la que es necesario el int actualizar (porque pocas veces 
//    concidira el runnable con 1000 mls)
    
    private Runnable mMuestraMensaje = new Runnable() {
	    public void run() {
	       	DecimalFormat mf = new DecimalFormat("000");
	       	Contador = (int)(SystemClock.uptimeMillis()- mStartTime);
	        TextView TextNumbers = (TextView) findViewById(R.id.textView1);

	        if ((Contador) >= actualizar * 1000){
	        	actualizar = actualizar + 1;
	        	Cuentasatras();
	        }
	        
	        TextView Milesimas = (TextView) findViewById(R.id.textView2);
	        if (!invisible){
	        	
	     	    TextNumbers.setText(String.valueOf(df.format(Contador/3600000))+
	     	    		':'+String.valueOf(df.format((Contador/60000)%60))+
	     	    		':'+String.valueOf(df.format((Contador/1000)%60)));
	     	    Milesimas.setText(String.valueOf(mf.format(Contador%1000)));
	        }
	        mHandler.removeCallbacks(mMuestraMensaje);
	        mHandler.postDelayed(this, 1);
	     }	
     };
      
      @Override
      protected void onDestroy() {
          super.onDestroy();
          mHandler.removeCallbacks(mMuestraMensaje);
     }


      public void saveLap (View Button){	
      	    	
      	TextView TextNumbers = (TextView) findViewById(R.id.textView1);
        TextView Milesimas = (TextView) findViewById(R.id.textView2);
      	String time = (String)TextNumbers.getText();
      	String milis = (String)Milesimas.getText();
      	
      	String result = time +"  " + milis;
        	
      	ListView listV = (ListView) findViewById(R.id.listView1);
      	
      	
      	li.add(new Lista(result));
      	listV.setAdapter(new ListaAdapter(this,R.layout.single_item, li));
      	
      	registerForContextMenu(listV);
      }
      
      @Override
      public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
           
  	    super.onCreateContextMenu(menu, v, menuInfo);    
  	    AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;
  	       
  	      // Posicion del textview
  	    pos = aInfo.position;
  	    
  	    menu.setHeaderTitle(getString(R.string.Options));
  	    menu.add(1, 1, 1, getString(R.string.DeleteMenu));
  	    menu.add(2, 2, 2, getString(R.string.DeleteMAll));
  	    menu.add(3, 3, 3, getString(R.string.Cancel));
  	  
           
      }
      
      @Override
      public boolean onContextItemSelected(MenuItem item) {
          int itemId = item.getItemId();
          // Implements our logic
          
          if (itemId == 1){
        	  li.remove(pos);
              ListView listV = (ListView) findViewById(R.id.listView1);
              Toast.makeText(getApplicationContext(),"Lap "+ df.format(pos) + " "+ getString(R.string.Deleted), Toast.LENGTH_SHORT).show();
      			
              listV.setAdapter(new ListaAdapter(this,R.layout.single_item, li));
          }else if(itemId==2){
        	  li.clear();
        	  ListView listV = (ListView) findViewById(R.id.listView1);
              Toast.makeText(getApplicationContext(),getString(R.string.LapsDeleted), Toast.LENGTH_SHORT).show();
              listV.setAdapter(new ListaAdapter(this,R.layout.single_item, li));              
          }
          
          return true;
      }
      
      private class ListaAdapter extends ArrayAdapter<Lista> {
    	  
    	  private List<Lista> item;
    	   
    	  public ListaAdapter(Context context, int textViewResourceId, List<Lista> item) {
    		  super(context, textViewResourceId, item);
    		  this.item = item;
    	  }
    	  
    	  // Create a title and detail, icon is created in the xml file
    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent) {
    		  View v = convertView;
    		  if (v == null) {
    			  	LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			  	v = vi.inflate(R.layout.single_item, null);
    		  }
    		  
    		  Lista o = item.get(position);
    		  
    		  //System.out.println(String.valueOf(position));
    		  TextView tt = (TextView) v.findViewById(R.id.textList);
    		  
    		  tt.setText("Lap "+ df.format(position) + ": "+ o.getName());
    		  return v;
    	 }
    }
      /*
   	 * Para sacar un mensaje en el toast al clickar
  	 * 
  	 * listV.setOnItemClickListener(new OnItemClickListener() {
  		public void onItemClick(AdapterView<?> parent, View view,
  				int position, long id) {
  		    // When clicked, show a toast with the TextView text
  		    Toast.makeText(getApplicationContext(),
  			"Fuck yeah", Toast.LENGTH_SHORT).show();
  		}
  	}); */
    
}
