/*
 * Autores: Leonardo Fernandes Cherubini e Ivoney da Silva Borges
 * Institui��o: IFMT - PET AutoNet
 * Orientadores: Cust�dio Gast�o Silva J�nior e Ronan Marcelo Martins
 * 
 * Classe: Activity 1 (Tela 1)
 */
package com.example.bluetoothteste02;

/************************* BIBLIOTECAS UTILIZADAS ***********************/
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

@SuppressWarnings("unused")
public class MainActivity extends Activity {

	/*--------------------------- ATRIBUTOS -----------------------------*/
	/*Objeto referente ao r�dio bluetooth do dispisitivo*/
	private BluetoothAdapter adaptador;
	
	/*Objeto referente ao dispositivo externo que ser� conectado pelo Android*/
	private BluetoothDevice device;
	
	/*Objeto referente ao socket da conex�o*/
	private BluetoothSocket socket;
	
	/*Objeto referente ao fluxo de sa�da de dados*/
	private DataOutputStream output;
	
	/*Objeto referente a Widget Button Conectar*/
	private Button btConectar;
	
	/*Objeto referente a uma Intent*/
	private Intent it;
	
	/*Vari�vel respons�vel por armazenar o n�mero de identifica��o da tela */
	private static final int TELA2 = 1;
	
	/*String que receber� o endere�o MAC do dispositivo escolhido na widget ListView*/
	private String address;
	
	/*Objeto referene a widget Button cima (w)*/
	private Button btCima;
	
	/*Objeto referene a widget Button esquerda (s)*/
	private Button btEsquerda;
	
	/*Objeto referene a widget Button direita (d)*/
	private Button btDireita;
	
	/*Objeto referene a widget Button baixo (s)*/
	private Button btBaixo;
	
	/*Objeto referente a widget SeekBar medidor de velocidade*/
	private SeekBar seekBar;
	
	/*Objeto do tipo ConnectThread*/
	private ConnectThread teste;
	
	/*Vari�vel auxiliar para os eventos dos bot�es*/
	private boolean pressedUp = false;
	
	/*Vari�vel usada para configurar a velocidade de SeekBar*/
	private byte velocidade = 50;

	/*------------------------------------------------------------------*/
	/*----------------------------- M�TODOS ----------------------------*/
	/*M�todo principal da aplica��o*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*Adicionando null a vari�vel device*/
		device = null;
		
		/*Adicionando null a vari�vel address*/
		address = null;

		/*Inicializando o objeto Intent que representar� a tela 2*/
		it = new Intent(this, Tela2.class);

		/*Apontando a vari�vel seekBar a Widget SeekBar da aplica��o*/
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		
		/*Adicionando a configura��o de velocidade a seekBar*/
		seekBar.setProgress(velocidade);
		
		/*Adicionando o valor m�nimo a velocidade de seekBar*/
		seekBar.setMinimumWidth(1);
		
		/*Adicionando o evento no momento que a Widget SeekBar for modificada*/
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				/*A vari�vel velocidade recebe o valor recuperado pela Widget SeekBar*/
				velocidade = (byte) progress;
				
			}
		});
		
		/*Vari�vel btConectar representa a Widget Button Conectar*/
		btConectar = (Button) findViewById(R.id.btConectar);
		
		/*Adicionando um evento ao Button Conectar*/
		btConectar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Chamando a tela 2*/
				startActivityForResult(it, TELA2);

			}
		});

		/*Vari�vel btCima representa a Widget Button Cima ('w')*/
		btCima = (Button) findViewById(R.id.btCima);
		
		/*Adicionando um evento ao Button Cima*/
		btCima.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (device == null) {
					alerta("Sem dispositivo Conectado");
				} else if (device != null) {
					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:

						if (pressedUp == false) {
							pressedUp = true;
							
							/*Enviando o caractere 'w' e a velocidade para o rob� quando
							  o Button � clicado*/
							new Enviar('w', velocidade).start();
						}
						break;

					case MotionEvent.ACTION_UP:
						/*Enviando o caractere 'q' (que representa o comando parar) e a 
						  velocidade para o rob� quando o Button � clicado*/
						new Enviar('q', velocidade).start();
						pressedUp = false;

					}

				}
				return true;
			}
		});

		/*Vari�vel btEsquerda representa a Widget Button Esquerda ('a')*/
		btEsquerda = (Button) findViewById(R.id.btEsquerda);
		
		/*Adicionando um evento ao Button Esquerda*/
		btEsquerda.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (device == null) {
					alerta("Sem dispositivo Conectado");
				} else if (device != null) {
					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:

						if (pressedUp == false) {
							pressedUp = true;
							/*Enviando o caractere 'a' e a velocidade para o rob� quando
							  o Button � clicado*/
							new Enviar('a', velocidade).start();
							
						}
						break;

					case MotionEvent.ACTION_UP:
						/*Enviando o caractere 'q' (que representa o comando parar) e a 
						  velocidade para o rob� quando o Button � clicado*/
						new Enviar('q', velocidade).start();
						pressedUp = false;

					}
				}
				return true;
			}
		});

		/*Vari�vel btDireita representa a Widget Button Direita ('d')*/
		btDireita = (Button) findViewById(R.id.btDireita);
		
		/*Adicionando um evento ao Button Direita*/
		btDireita.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (device == null) {
					alerta("Sem dispositivo Conectado");
				} else if (device != null) {
					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:

						if (pressedUp == false) {
							pressedUp = true;
							/*Enviando o caractere 'a' e a velocidade para o rob� quando
							  o Button � clicado*/
							new Enviar('d', velocidade).start();
							
						}
						break;

					case MotionEvent.ACTION_UP:
						/*Enviando o caractere 'q' (que representa o comando parar) e a 
						  velocidade para o rob� quando o Button � clicado*/
						new Enviar('q', velocidade).start();
						pressedUp = false;

					}
				}
				return true;
			}
		});

		/*Vari�vel btBaixo representa a Widget Button Direita ('s')*/
		btBaixo = (Button) findViewById(R.id.btBaixo);
		
		/*Adicionando um evento ao Button Baixo*/
		btBaixo.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (device == null) {
					alerta("Sem dispositivo Conectado");
				} else if (device != null) {
					switch (event.getAction()) {

					case MotionEvent.ACTION_DOWN:

						if (pressedUp == false) {
							pressedUp = true;
							/*Enviando o caractere 's' e a velocidade para o rob� quando
							  o Button � clicado*/
							new Enviar('a', velocidade).start();
							
						}
						break;

					case MotionEvent.ACTION_UP:
						/*Enviando o caractere 'q' (que representa o comando parar) e a 
						  velocidade para o rob� quando o Button � clicado*/
						new Enviar('q', velocidade).start();
						pressedUp = false;

					}
					
				}
				return true;
			}
		});

		/*Confirando o objeto principal para a comunica��o bluetooth */
		adaptador = BluetoothAdapter.getDefaultAdapter();

		/*Estrutura de decis�o para ligar o Bluetooth*/
		if (!adaptador.isEnabled()) {
			
			/*Se o R�dio Bluetooth estiver desligado BluetoothAdapter 
			  solicitar� a permiss�o do usu�rio para ligar o Bluetooth*/
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 1);
		}

	}

	/*M�todo de conex�o com um dispositivo externo*/
	private class ConnectThread extends Thread {
		
		/*Vari�vel final que armazenar� o dispositivo escolhido para a conex�o*/
		private final BluetoothDevice mmDevice;

		/*Construtor do ConnectThread*/
		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			try {
				
				/*Vari�vel socket recebe um identificador �nico UUID*/
				tmp = device.createRfcommSocketToServiceRecord(UUID
						.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			socket = tmp;
		}

		public void run() {
			/*Cancela a pesquisa de dispositivos externos para economizar banda*/
			adaptador.cancelDiscovery();

			try {
				/*Socket conecta a aplicativo Android no dispositivo externo*/
				socket.connect();
				alerta("Conex�o Aberta");
			} catch (IOException e) {
				try {
					socket.close();
					alerta("Erro de Conex�o");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}

		}

		public void cancel() {
			try {
				socket.close();
				alerta("Conex�o Fechada");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*M�todo de retorno de dados da Tela 2*/
	protected void onActivityResult(int codigo, int resultado, Intent it) {
		if (it == null) {
			alerta("Nenhum Endere�o Selecionado");
			return;
		}

		/*Vari�vel String que vai retornar o endere�o MAC do dispositivo selecionado*/
		address = it.getExtras().getString("msg");
		alerta("Novo endere�o " + address);
		
		/*Configurando o BluetoothDevice device com o MAC selecionado*/
		device = adaptador.getRemoteDevice(address);
		
		/*M�todo ConnectThead recebe o argumento device*/
		teste = new ConnectThread(device);
		
		/*A vari�vel texte � executada*/
		teste.start();
	}

	/*M�todo de envio de dados*/
	public class Enviar extends Thread {

		/*Vari�vel que receber� o caractere que ser� enviado ao dispositivo externo*/
		private char letra;
		
		/*Vari�vel que receber� o valor num�rico que ser� enviado ao dispositivo externo*/
		private byte speed;

		/*Construtor Enviar*/
		public Enviar(char letra, byte speed) {
			this.letra = letra;
			this.speed = speed;

		}
		
		/*M�todo run da Thread*/
		public void run() {
			
			try {
				/*Configura��o do fluxo de sa�da de dados*/
				output = new DataOutputStream(socket.getOutputStream()); 									// transmiss�o de dados
				
				/*Fluxo executando o envio do caractere ao dispositivo externo*/
				output.writeChar(letra);
				
				/*Fluxo executando o envio do n�mero ao dispositivo externo*/
				output.writeByte(speed);
				
				output.flush();
				
			} catch (IOException erro) {
				alerta("Erro transferencia");
			}

		}
	}

	/*M�todo para configurar o bot�o menu do celular*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/*M�todos respons�veis para o envio de uma mensagem na tela*/
	private final Handler h = new Handler() {
		public void handleMessage(Message msg) {
			String content = (String) msg.obj;
			Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT)
					.show();
		}
	};

	public void alerta(String message) {
		Message m = h.obtainMessage();
		m.obj = message;
		h.sendMessage(m);
	}
}
