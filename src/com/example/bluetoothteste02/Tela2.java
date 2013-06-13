/*
 * Autores: Leonardo Fernandes Cherubini e Ivoney da Silva Borges
 * Instituição: IFMT - PET AutoNet
 * Orientadores: Custódio Gastão Silva Júnior e Ronan Marcelo Martins
 * 
 * Classe: Activity 2 (Tela 2)
 */

package com.example.bluetoothteste02;

/************************* BIBLIOTECAS UTILIZADAS ***********************/

import java.util.Set;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;                               
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;                    
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Tela2 extends Activity {

	/*--------------------------- ATRIBUTOS -----------------------------*/
	/*Objeto referente ao rádio bluetooth do dispisitivo*/
	private BluetoothAdapter adaptador;
	
	/*Objeto referente a Widget ListView*/
	private ListView lista;
	
	/*Objeto responsável por retornar dados de um dispositivo exteno e adiciona-lo ao ListView*/
	private ArrayAdapter<String> dispositivos;
	
	/*Objeto referente a Widget Button Pesquisar*/
	private Button btPesquisar;

	/*------------------------------------------------------------------*/
	/*----------------------------- MÉTODOS ----------------------------*/
	/*Método principal onde a Activity será inicializada*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tela02);

		/*Confirando o objeto principal para a comunicação bluetooth */
		adaptador = BluetoothAdapter.getDefaultAdapter();

		/*Apontando o objeto Button ao botão Pesquisar do xml*/
		btPesquisar = (Button) findViewById(R.id.btPesquisar);
		/*Configurando o evento do botão Pesquisar*/
		btPesquisar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Chamando o método pesquisar*/
				pesquisar();
			}
		});
		
		/*Apontando o objeto ListView a lista de dispositivos extenos xml*/
		lista = (ListView) findViewById(R.id.lvLista);
		
		/*Inicializando a variável ArrayAdapter e seu tipo de texto*/
		dispositivos = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1);
		
		/*Adicionando a ArrayAdapter a widget ListView*/
		lista.setAdapter(dispositivos);
		/*Configurando o evento de clique na ListView*/
		lista.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				/*Variáveis responsáveis por retornar o endereço do item da lista selecionado*/
				String info = ((TextView) view).getText().toString();
				String address = info.substring(info.length() - 17);
				
				/*Instanciando um objeto intent*/
				Intent it = new Intent();
				
				/*Configurando os dados que serão retornados para a primeira tela (Activity)
				 * o primeiro parâmetro é um id de identificação e o segundo são os dados que
				 * serão retornados para a primeira tela*/
				it.putExtra("msg", address);
				
				/*Método que envia os dados para a outra tela*/
				setResult(Activity.RESULT_OK, it);
				
				/*Método nativo do Android que finaliza esta tela*/
				finish();
			}
		});
		
		/*Configuração padrão para retornar dados dos dispositivos encontrados
		 * e adicioná-los ao ListView*/
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, filter);

		Set<BluetoothDevice> pairedDevices = adaptador.getBondedDevices();

		if (pairedDevices.size() > 0) {

			for (BluetoothDevice device : pairedDevices) {

				dispositivos.add(device.getName() + "\n"+ device.getAddress());
			}

		}

	}

	/*Método responsável pela pesquisa por dispositivos externos*/
	private void pesquisar() {
		/*Método de alerta de mensagem*/
		alerta("Pesquisando ... ");

		if (adaptador.isDiscovering()) {
			/*Cancela a conexão se caso o aparelho estiver fazendo uma pesquisa*/
			adaptador.cancelDiscovery();
		}

		/*Começa a fazer uma pesquisa por dispositivos externos*/
		adaptador.startDiscovery();
		
		/*limpa os dispositivos da ListView*/
		dispositivos.clear();
	}
	
	/*Método nativo do Android sobrescrito para evitar erros*/
	@Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (adaptador != null) {
        	adaptador.cancelDiscovery();
        }
        
        this.unregisterReceiver(mReceiver);
    }
	
	/*Método responsável por utilizar o hardware do celular para retornar os dispositivos encontrados*/
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {

				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				dispositivos.add(device.getName() + "\n"+ device.getAddress());
			}
		}
	};
	
	/*Método para configurar o botão menu do celular*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/*Métodos responsáveis para o envio de uma mensagem na tela*/
	private final Handler h = new Handler() {
		public void handleMessage(Message msg) {
			String content = (String) msg.obj;
			Toast.makeText(Tela2.this, content, Toast.LENGTH_SHORT).show();
		}
	};

	public void alerta(String message) {
		Message m = h.obtainMessage();
		m.obj = message;
		h.sendMessage(m);
	}
}
