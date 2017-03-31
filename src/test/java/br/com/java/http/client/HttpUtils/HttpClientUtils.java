package br.com.java.http.client.HttpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpClientUtils {

	/* Metodos para facilitar o que se faz na classe */
	protected JSONObject responseContentHttp(InputStream inputStream) {

		String tranformacao = tranformaRespostaEmString(inputStream);

		// Converte uma String para um JSON Object usando a biblioteca
		JSONObject jsonObject = new JSONObject(tranformacao);

		return jsonObject;
	}

	protected String tranformaRespostaEmString(InputStream inputStream) {

		// BufferReader cria um fluxo de entrada de caracteres em buffer que usa
		// um buffer de entrada padrão em um Leitor (Reader)
		// InputStreamReader usa o charset padrão
		// nos parametros são passado a Resposta pegando a Entidade mensagem
		// dessa resposta,
		// junto com um fluxo de conteudo da Entidade
		// Quando passa uma vez nele, ele vai recortando os valores e deixando a
		// lista de valores vazias
		BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));

		// Contrui um buffer de caracteres sem caracteres com uma capacidade
		// inicial especificada
		StringBuffer result = new StringBuffer();
		String output = "";

		// Usando o while como um laço de repetição até que o valor seja
		// diferente de nulo
		// Fazendo uma leitura das linhas que foram recebidas até da Entidade e
		// armazenando na variavel vazia output
		// Cada vez que passa no laço, ele acrescenta na variavel StringBuffer o
		// que a variavel output recebeu
		try {
			while ((output = rd.readLine()) != null) {
				result.append(output);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return result.toString();
	}

	protected static String convertMapToJson(Map map) throws JSONException {

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

	protected static Map<String, Object> toMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	protected static Object trazerDadosContractor(Map map) throws Exception {
		Map<String, Object> map2 = (Map<String, Object>) map.get("data");
		return ((Map<String, Object>) map2.get("contractor")).get("label");
	}

	@SuppressWarnings("unchecked")
	protected static Object trazerMetaCode(Map map) throws Exception {
		Map<String, Object> map2 = (Map<String, Object>) map.get("meta");
		return map2.get("code");

	}

	@SuppressWarnings("unchecked")
	protected static Object trazerCodeOS(Map map) throws Exception {
		Map<String, Object> map2 = (Map<String, Object>) map.get("data");
		return map2.get("code");

	}
	
	protected Object trazerTotalCountPagination(Map map) {
		Map<String, Object> map2 = (Map<String, Object>) map.get("pagination");
		return map2.get("totalCount");
	}

	protected static Map<String, String> parametrosRequisicao() throws Exception {

		Map<String, String> parametersCreateOS = new HashMap<String, String>();
		parametersCreateOS.put("parametro-chave", "parametro-valor");


		return parametersCreateOS;
	}

}
