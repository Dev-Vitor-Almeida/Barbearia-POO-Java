import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import dao.AgendamentoDAO;
import dao.BarbeiroDAO;
import dao.ClienteDAO;
import dao.ServicoDAO;
import model.Agendamento;
import model.Barbeiro;
import model.Cliente;
import model.Servico;
import util.JsonUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public class ApiServer {
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final BarbeiroDAO barbeiroDAO = new BarbeiroDAO();
    private static final ServicoDAO servicoDAO = new ServicoDAO();
    private static final AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/health", ApiServer::health);
        server.createContext("/api/clientes", ApiServer::clientes);
        server.createContext("/api/barbeiros", ApiServer::barbeiros);
        server.createContext("/api/servicos", ApiServer::servicos);
        server.createContext("/api/agendamentos", ApiServer::agendamentos);
        server.createContext("/api/login/cliente", ApiServer::loginCliente);
        server.createContext("/api/login/barbeiro", ApiServer::loginBarbeiro);

        server.setExecutor(null);
        server.start();

        System.out.println("API da Barbearia rodando em http://localhost:8080");
    }

    private static void health(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) {
            return;
        }

        sendJson(exchange, 200, "{\"status\":\"ok\"}");
    }

    private static void clientes(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) {
            return;
        }

        if (isMethod(exchange, "GET")) {
            sendJson(exchange, 200, clientesToJson(clienteDAO.listarClientes()));
            return;
        }

        if (isMethod(exchange, "POST")) {
            Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));
            Cliente cliente = new Cliente(
                    body.get("nome"),
                    body.get("telefone"),
                    body.get("cpf"),
                    body.get("email"),
                    body.get("senha")
            );

            clienteDAO.salvarCliente(cliente);
            sendJson(exchange, 201, "{\"mensagem\":\"Cliente cadastrado com sucesso\"}");
            return;
        }

        sendJson(exchange, 405, "{\"erro\":\"Metodo nao permitido\"}");
    }

    private static void barbeiros(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) {
            return;
        }

        if (isMethod(exchange, "GET")) {
            sendJson(exchange, 200, barbeirosToJson(barbeiroDAO.listarBarbeiros()));
            return;
        }

        if (isMethod(exchange, "POST")) {
            Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));
            Barbeiro barbeiro = new Barbeiro(
                    body.get("nome"),
                    body.get("telefone"),
                    body.get("email"),
                    body.get("especialidade"),
                    body.get("senha")
            );

            barbeiroDAO.salvarBarbeiro(barbeiro);
            sendJson(exchange, 201, "{\"mensagem\":\"Barbeiro cadastrado com sucesso\"}");
            return;
        }

        sendJson(exchange, 405, "{\"erro\":\"Metodo nao permitido\"}");
    }

    private static void servicos(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) {
            return;
        }

        if (isMethod(exchange, "GET")) {
            sendJson(exchange, 200, servicosToJson(servicoDAO.listarServicos()));
            return;
        }

        if (isMethod(exchange, "POST")) {
            Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));
            Servico servico = new Servico(
                    body.get("nome"),
                    Double.parseDouble(body.get("preco")),
                    Integer.parseInt(body.get("duracaoMinutos"))
            );

            servicoDAO.salvarServico(servico);
            sendJson(exchange, 201, "{\"mensagem\":\"Servico cadastrado com sucesso\"}");
            return;
        }

        sendJson(exchange, 405, "{\"erro\":\"Metodo nao permitido\"}");
    }

    private static void agendamentos(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) {
            return;
        }

        if (isMethod(exchange, "GET")) {
            sendJson(exchange, 200, agendamentosToJson(agendamentoDAO.listarAgendamentosDetalhados()));
            return;
        }

        if (isMethod(exchange, "POST")) {
            criarAgendamento(exchange);
            return;
        }

        if (isMethod(exchange, "DELETE")) {
            int id = Integer.parseInt(queryValue(exchange, "id", "0"));
            boolean removido = agendamentoDAO.cancelarAgendamentoPorId(id);
            sendJson(exchange, removido ? 200 : 404, removido
                    ? "{\"mensagem\":\"Agendamento cancelado com sucesso\"}"
                    : "{\"erro\":\"Agendamento nao encontrado\"}");
            return;
        }

        if (isMethod(exchange, "PATCH")) {
            Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));
            int id = Integer.parseInt(body.getOrDefault("id", "0"));
            String horario = body.get("horario");
            boolean atualizado = agendamentoDAO.atualizarHorarioAgendamentoPorId(id, horario);
            sendJson(exchange, atualizado ? 200 : 404, atualizado
                    ? "{\"mensagem\":\"Horario atualizado com sucesso\"}"
                    : "{\"erro\":\"Agendamento nao encontrado\"}");
            return;
        }

        sendJson(exchange, 405, "{\"erro\":\"Metodo nao permitido\"}");
    }

    private static void criarAgendamento(HttpExchange exchange) throws IOException {
        Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));

        Cliente cliente = clienteDAO.buscarClientePorId(Integer.parseInt(body.getOrDefault("clienteId", "0")));
        Barbeiro barbeiro = barbeiroDAO.buscarBarbeiroPorId(Integer.parseInt(body.getOrDefault("barbeiroId", "0")));
        Servico servico = servicoDAO.buscarServicoPorId(Integer.parseInt(body.getOrDefault("servicoId", "0")));

        if (cliente == null || barbeiro == null || servico == null) {
            sendJson(exchange, 400, "{\"erro\":\"Cliente, barbeiro ou servico nao encontrado\"}");
            return;
        }

        if (!agendamentoDAO.horarioDisponivel(barbeiro.getId(), body.get("data"), body.get("horario"))) {
            sendJson(exchange, 409, "{\"erro\":\"Horario indisponivel para este barbeiro\"}");
            return;
        }

        Agendamento agendamento = new Agendamento(
                cliente,
                barbeiro,
                servico,
                body.get("data"),
                body.get("horario")
        );

        agendamentoDAO.salvarAgendamento(agendamento);
        sendJson(exchange, 201, "{\"mensagem\":\"Agendamento criado com sucesso\"}");
    }

    private static void loginCliente(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) {
            return;
        }

        if (!isMethod(exchange, "POST")) {
            sendJson(exchange, 405, "{\"erro\":\"Metodo nao permitido\"}");
            return;
        }

        Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));
        Cliente cliente = clienteDAO.loginCliente(body.get("email"), body.get("senha"));

        if (cliente == null) {
            sendJson(exchange, 401, "{\"erro\":\"Email ou senha invalidos\"}");
            return;
        }

        sendJson(exchange, 200, clienteToJson(cliente));
    }

    private static void loginBarbeiro(HttpExchange exchange) throws IOException {
        if (handleCors(exchange)) {
            return;
        }

        if (!isMethod(exchange, "POST")) {
            sendJson(exchange, 405, "{\"erro\":\"Metodo nao permitido\"}");
            return;
        }

        Map<String, String> body = JsonUtil.parseJsonObject(JsonUtil.readBody(exchange.getRequestBody()));
        Barbeiro barbeiro = barbeiroDAO.loginBarbeiro(body.get("email"), body.get("senha"));

        if (barbeiro == null) {
            sendJson(exchange, 401, "{\"erro\":\"Email ou senha invalidos\"}");
            return;
        }

        sendJson(exchange, 200, barbeiroToJson(barbeiro));
    }

    private static boolean handleCors(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PATCH, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if (isMethod(exchange, "OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return true;
        }

        return false;
    }

    private static void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, response.length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(response);
        }
    }

    private static boolean isMethod(HttpExchange exchange, String method) {
        return exchange.getRequestMethod().equalsIgnoreCase(method);
    }

    private static String queryValue(HttpExchange exchange, String key, String defaultValue) {
        String query = exchange.getRequestURI().getRawQuery();
        if (query == null || query.isEmpty()) {
            return defaultValue;
        }

        for (String item : query.split("&")) {
            String[] parts = item.split("=", 2);
            if (parts.length == 2 && decode(parts[0]).equals(key)) {
                return decode(parts[1]);
            }
        }

        return defaultValue;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static String clientesToJson(ArrayList<Cliente> clientes) {
        ArrayList<String> json = new ArrayList<>();
        for (Cliente cliente : clientes) {
            json.add(clienteToJson(cliente));
        }
        return "[" + String.join(",", json) + "]";
    }

    private static String clienteToJson(Cliente cliente) {
        return "{"
                + "\"id\":" + cliente.getId() + ","
                + "\"nome\":" + JsonUtil.quote(cliente.getNome()) + ","
                + "\"telefone\":" + JsonUtil.quote(cliente.getTelefone()) + ","
                + "\"cpf\":" + JsonUtil.quote(cliente.getCpf()) + ","
                + "\"email\":" + JsonUtil.quote(cliente.getEmail())
                + "}";
    }

    private static String barbeirosToJson(ArrayList<Barbeiro> barbeiros) {
        ArrayList<String> json = new ArrayList<>();
        for (Barbeiro barbeiro : barbeiros) {
            json.add(barbeiroToJson(barbeiro));
        }
        return "[" + String.join(",", json) + "]";
    }

    private static String barbeiroToJson(Barbeiro barbeiro) {
        return "{"
                + "\"id\":" + barbeiro.getId() + ","
                + "\"nome\":" + JsonUtil.quote(barbeiro.getNome()) + ","
                + "\"telefone\":" + JsonUtil.quote(barbeiro.getTelefone()) + ","
                + "\"email\":" + JsonUtil.quote(barbeiro.getEmail()) + ","
                + "\"especialidade\":" + JsonUtil.quote(barbeiro.getEspecialidade())
                + "}";
    }

    private static String servicosToJson(ArrayList<Servico> servicos) {
        ArrayList<String> json = new ArrayList<>();
        for (Servico servico : servicos) {
            json.add(servicoToJson(servico));
        }
        return "[" + String.join(",", json) + "]";
    }

    private static String servicoToJson(Servico servico) {
        return "{"
                + "\"id\":" + servico.getId() + ","
                + "\"nome\":" + JsonUtil.quote(servico.getNome()) + ","
                + "\"preco\":" + servico.getPreco() + ","
                + "\"duracaoMinutos\":" + servico.getDuracaoMinutos()
                + "}";
    }

    private static String agendamentosToJson(ArrayList<Map<String, String>> agendamentos) {
        ArrayList<String> json = new ArrayList<>();
        for (Map<String, String> agendamento : agendamentos) {
            json.add("{"
                    + "\"id\":" + agendamento.get("id") + ","
                    + "\"clienteId\":" + agendamento.get("clienteId") + ","
                    + "\"clienteNome\":" + JsonUtil.quote(agendamento.get("clienteNome")) + ","
                    + "\"barbeiroId\":" + agendamento.get("barbeiroId") + ","
                    + "\"barbeiroNome\":" + JsonUtil.quote(agendamento.get("barbeiroNome")) + ","
                    + "\"servicoId\":" + agendamento.get("servicoId") + ","
                    + "\"servicoNome\":" + JsonUtil.quote(agendamento.get("servicoNome")) + ","
                    + "\"preco\":" + agendamento.get("preco") + ","
                    + "\"duracaoMinutos\":" + agendamento.get("duracaoMinutos") + ","
                    + "\"data\":" + JsonUtil.quote(agendamento.get("data")) + ","
                    + "\"horario\":" + JsonUtil.quote(agendamento.get("horario"))
                    + "}");
        }

        return "[" + String.join(",", json) + "]";
    }
}
