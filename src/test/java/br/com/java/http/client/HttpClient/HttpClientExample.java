package br.com.java.http.client.HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpClientExample {

	String baseUrl = "http://requisicao";
	String token = "";

	public static void main(String[] args) throws Exception {

		HttpClientExample http = new HttpClientExample();

		System.out.println("Primeiro Teste - Requisição HTTP POST Login");
		http.sendLogin();

		System.out.println(
				"\n----------------------------------------------------------------------------------------------");

		System.out.println("\nSegundo Teste - Requisição HTTP GET");
		http.sendGet();

		System.out.println(
				"\n----------------------------------------------------------------------------------------------");

		System.out.println("\nTerceiro Teste - Requisição HTTP POST");
		http.sendPostOS();

	}

	/* Fazendo Login com HTTP POST */
	private void sendLogin() throws Exception {

		// A Url que irá ser acessada
		String url = (baseUrl + "/login");

		// Ao instanciar o HttpClient usando o HttpClientBuilder,
		// o create retorna o new do HttpClientBuilder
		// dentro do HttpClientBuilder existe uma classe com o metodo build que
		// é CloseableHttpClient
		HttpClient client = HttpClientBuilder.create().build();

		// Inicialização do HttpPost passando como parametro a URL que será feito o POST
		HttpPost post = new HttpPost(url);

		// Adicionando a Requisição ao Header
		// Por baixo dos panos usa a classe AbstractHttpMessage
		// Seta no cabeçalho o Nome e o Valor, atualizando um que já possa
		// existir, senão, ele apenas adiciona no final do cabeçalho
		// post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-type", "application/json");

		// Uma lista de NameValuePair, pega o par do Nome e do Valor que é usado
		// como elemento de mensagem HTTP
		// Nesta lista eu instancio o Array de par de Nome e Valor
		// Adiciono na lista a inicialização do BasicNameValuePair junto do Nome
		// e do Valor que eu preciso passar no HTTP
		// BasicNameValuePair simplesmente pega o par de Nome e Valor, sendo que
		// valor pode ser nulo, mas nome não
		// E adiciona na Minha lista de par de Nome e Valor
		// Desta forma, é passado na URL e não é um JSON, é um form-data*/
		/*List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("username", "username"));
		urlParameters.add(new BasicNameValuePair("password", "password"));*/

		// Associa a Entidade de Resposta a Requisição
		// UrlEncodedFormEntity contrui um novo UrlEncodedFormEntity com a lista
		// de pares Nome e Valor que é passado como paremetro
		/*post.setEntity(new UrlEncodedFormEntity(urlParameters));*/
		
		// Passando a String do JSON para a Entidade que interpreta ele para poder fazer o POST
		post.setEntity(new StringEntity("{'username':'username','password':'password'}"));

		// Executa a requisição que foi feita pelo parametro passado
		// HttpResponse obtém a linha de resposta dessa requisição
		HttpResponse response = client.execute(post);

		int responseCode = response.getStatusLine().getStatusCode();

		System.out.println("\nEnviando a requisição 'POST' na URL : " + url);
		System.out.println("Response Code : " + responseCode);

		// BufferReader cria um fluxo de entrada de caracteres em buffer que usa
		// um buffer de entrada padrão em um Leitor (Reader)
		// InputStreamReader usa o charset padrão
		// nos parametros são passado a Resposta pegando a Entidade mensagem
		// dessa resposta,
		// junto com um fluxo de conteudo da Entidade
		/*BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));*/

		// Contrui um buffer de caracteres sem caracteres com uma capacidade
		// inicial especificada
		/*StringBuffer result = new StringBuffer();
		String output = "";*/

		// Usando o while como um laço de repetição até que o valor seja
		// diferente de nulo
		// Fazendo uma leitura das linhas que foram recebidas até da Entidade e
		// armazenando na variavel vazia output
		// Cada vez que passa no laço, ele acrescenta na variavel StringBuffer o
		// que a variavel output recebeu		
		/*while ((output = rd.readLine()) != null) {
		result.append(output);
		}*/

		InputStream inputStream = response.getEntity().getContent();

		if (responseCode == 200) {
			JSONObject responseContentHttp = responseContentHttp(inputStream);

			System.out.println("A Resposta da Requisição POST de Login: " + responseContentHttp);
			System.out.println("O resultado do Token do Login: " + responseContentHttp.get("accessToken"));

			// Armazenando na variavel token, o valor do token com Bearer
			token = "Bearer " + responseContentHttp.get("accessToken");

		} else {

			System.out.println("Erro: " + tranformaRespostaEmString(inputStream));
		}
	}

	/* Pegando algum dado com HTTP GET */
	private void sendGet() throws Exception {

		String url = (baseUrl + "/get");

		HttpClient client = HttpClientBuilder.create().build();

		HttpGet request = new HttpGet(url);

		// Adicionando a Requisição do Header
		// Enviando no cabeçalho o formato de aceitação e o token de autorização
		request.addHeader("accept", "application/json");
		request.setHeader("Authorization", token);

		HttpResponse response = client.execute(request);

		int responseCode = response.getStatusLine().getStatusCode();

		System.out.println("\nEnviando a requisição 'GET' na URL : " + url);
		System.out.println("Response Code : " + responseCode);

		if (responseCode == 200) {
			JSONObject responseContentHttp = responseContentHttp(response.getEntity().getContent());
			
			// passando o JSON para um MAPA
			Map<String, Object> map = toMap(responseContentHttp);
			
			Object metaCode = ((Map<String,Object>) map.get("meta")).get("code");
			
			System.out.println("Meta Code: " + metaCode);
			
			System.out.println("O resultado do GET: " + responseContentHttp);

		} else {

			System.out.println("Erro: " + tranformaRespostaEmString(response.getEntity().getContent()));
		}
	}

	/* Criação da OS com HTTP POST */
	private void sendPostOS() throws Exception {

		String url = (baseUrl + "/post");

		HttpClient client = HttpClientBuilder.create().build();

		HttpPost post = new HttpPost(url);

		post.setHeader("Content-type", "application/json");
		post.setHeader("Authorization", token);

		Map<String, String> parametersCreateOS = new HashMap<String, String>();
		parametersCreateOS.put("algo", "algo");

		String jsonOS = convertMapToJson(parametersCreateOS);

		post.setEntity(new StringEntity(jsonOS));

		HttpResponse response = client.execute(post);

		int responseCode = response.getStatusLine().getStatusCode();

		System.out.println("\nEnviando a requisição 'POST' na URL : " + url);
		System.out.println("Response Code : " + responseCode);

		if (responseCode == 200) {
			JSONObject responseContentHttp = responseContentHttp(response.getEntity().getContent());

			Map<String, Object> map = toMap(responseContentHttp);
			
			Object metaCode = ((Map<String,Object>) map.get("meta")).get("code");
			
			
			System.out.println("Meta Code: " + metaCode);

			System.out.println("A Resposta do POST: " + responseContentHttp);

		} else {

			System.out.println("Erro: " + tranformaRespostaEmString(response.getEntity().getContent()));
		}
	}

	/* Metodos para facilitar o que se faz na classe */
	private JSONObject responseContentHttp(InputStream inputStream) {

		String tranformacao = tranformaRespostaEmString(inputStream);

		// Converte uma String para um JSON Object usando a biblioteca
		JSONObject jsonObject = new JSONObject(tranformacao);

		return jsonObject;
	}

	private String tranformaRespostaEmString(InputStream inputStream) {

		BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));

		StringBuffer result = new StringBuffer();
		String output = "";

		try {
			while ((output = rd.readLine()) != null) {
				result.append(output);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return result.toString();
	}

	public static String convertMapToJson(Map map) throws JSONException {

		JSONObject obj = new JSONObject();
		// JSONObject main = new JSONObject();
		Set set = map.keySet();

		Iterator iter = set.iterator();

		while (iter.hasNext()) {
			String key = (String) iter.next();
			obj.accumulate(key, map.get(key));
		}
		// main.accumulate("data",obj);

		return obj.toString();
	}
	
	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

}